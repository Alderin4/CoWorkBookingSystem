package com.cowork.service;

import java.sql.Connection;
import java.sql.Date;
import java.time.LocalTime;
import java.util.List;

import com.cowork.bean.Bill;
import com.cowork.bean.Booking;
import com.cowork.bean.Member;
import com.cowork.dao.BillDAO;
import com.cowork.dao.BookingDAO;
import com.cowork.dao.MemberDAO;
import com.cowork.util.ActiveBillingException;
import com.cowork.util.DBUtil;
import com.cowork.util.SlotAlreadyBookedException;
import com.cowork.util.ValidationException;

public class BookingService {

    private MemberDAO memberDAO = new MemberDAO();
    private BookingDAO bookingDAO = new BookingDAO();
    private BillDAO billDAO = new BillDAO();

    /* ================= VIEW MEMBER ================= */
    public Member viewMemberDetails(String memberID)
            throws ValidationException, Exception {

        if (memberID == null || memberID.trim().isEmpty()) {
            throw new ValidationException();
        }
        return memberDAO.findMember(memberID);
    }

    /* ================= VIEW ALL MEMBERS ================= */
    public List<Member> viewAllMembers() throws Exception {
        return memberDAO.viewAllMembers();
    }

    /* ================= ADD NEW MEMBER ================= */
    public boolean addNewMember(Member member)
            throws ValidationException, Exception {

        if (member == null ||
            member.getMemberID() == null || member.getMemberID().trim().isEmpty() ||
            member.getFullName() == null || member.getFullName().trim().isEmpty() ||
            member.getEmail() == null || member.getEmail().trim().isEmpty() ||
            member.getMobile() == null || member.getMobile().trim().isEmpty()) {
            throw new ValidationException();
        }

        Member existing = memberDAO.findMember(member.getMemberID());
        if (existing != null) {
            return false;
        }

        member.setOutstandingBalance(0.0);
        return memberDAO.insertMember(member);
    }

    /* ================= REMOVE MEMBER ================= */
    public boolean removeMember(String memberID)
            throws ValidationException, ActiveBillingException, Exception {

        if (memberID == null || memberID.trim().isEmpty()) {
            throw new ValidationException();
        }

        List<Bill> pendingBills =
                billDAO.findPendingBillsByMember(memberID);

        if (pendingBills != null && !pendingBills.isEmpty()) {
            throw new ActiveBillingException();
        }

        return memberDAO.deleteMember(memberID);
    }

    /* ================= CREATE BOOKING (TRANSACTIONAL) ================= */
    public boolean createBooking(String memberID, String deskCode,
                                 Date bookingDate, String startTime, String endTime)
            throws ValidationException, SlotAlreadyBookedException, Exception {

        if (memberID == null || memberID.trim().isEmpty() ||
            deskCode == null || deskCode.trim().isEmpty() ||
            bookingDate == null ||
            startTime == null || endTime == null) {
            throw new ValidationException();
        }

        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        if (!start.isBefore(end)) {
            throw new ValidationException();
        }

        Member member = memberDAO.findMember(memberID);
        if (member == null) {
            return false;
        }

        List<Booking> existingBookings =
                bookingDAO.findBookingsForDeskAndDate(deskCode, bookingDate);

        for (Booking b : existingBookings) {
            if ("BOOKED".equals(b.getStatus()) ||
                "COMPLETED".equals(b.getStatus())) {

                LocalTime es = LocalTime.parse(b.getStartTime());
                LocalTime ee = LocalTime.parse(b.getEndTime());

                if (start.isBefore(ee) && end.isAfter(es)) {
                    throw new SlotAlreadyBookedException();
                }
            }
        }

        Connection con = DBUtil.getDBConnection();
        try {
            int bookingID = bookingDAO.generateBookingID();

            Booking booking = new Booking();
            booking.setBookingID(bookingID);
            booking.setMemberID(memberID);
            booking.setDeskCode(deskCode);
            booking.setBookingDate(bookingDate);
            booking.setStartTime(startTime);
            booking.setEndTime(endTime);
            booking.setStatus("BOOKED");

            boolean inserted = bookingDAO.recordBooking(booking);
            if (!inserted) {
                con.rollback();
                return false;
            }

            con.commit();
            return true;

        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }

    /* ================= CANCEL BOOKING ================= */
    public boolean cancelBooking(int bookingID)
            throws ValidationException, Exception {

        if (bookingID <= 0) {
            throw new ValidationException();
        }

        Connection con = DBUtil.getDBConnection();
        try {
            boolean updated =
                    bookingDAO.updateBookingStatus(bookingID, "CANCELLED");

            if (!updated) {
                con.rollback();
                return false;
            }

            con.commit();
            return true;

        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }

    /* ================= GENERATE USAGE BILL ================= */
    public boolean generateUsageBill(String memberID,
                                     Date periodFrom, Date periodTo)
            throws ValidationException, Exception {

        if (memberID == null || memberID.trim().isEmpty() ||
            periodFrom == null || periodTo == null ||
            periodFrom.after(periodTo)) {
            throw new ValidationException();
        }

        Member member = memberDAO.findMember(memberID);
        if (member == null) {
            return false;
        }

        List<Booking> bookings =
                bookingDAO.findBookingsByMember(memberID);

        double totalHours = 0.0;

        for (Booking b : bookings) {
            if ("COMPLETED".equals(b.getStatus()) &&
                !b.getBookingDate().before(periodFrom) &&
                !b.getBookingDate().after(periodTo)) {

                LocalTime st = LocalTime.parse(b.getStartTime());
                LocalTime et = LocalTime.parse(b.getEndTime());
                totalHours += (et.toSecondOfDay() - st.toSecondOfDay()) / 3600.0;
            }
        }

        double rate;
        switch (member.getMembershipTier()) {
            case "PREMIUM":
                rate = 150;
                break;
            case "STANDARD":
                rate = 200;
                break;
            default:
                rate = 250;
        }

        double amount = totalHours * rate;

        Connection con = DBUtil.getDBConnection();
        try {
            int billID = billDAO.generateBillID();

            Bill bill = new Bill();
            bill.setBillID(billID);
            bill.setMemberID(memberID);
            bill.setBillingPeriodFrom(periodFrom);
            bill.setBillingPeriodTo(periodTo);
            bill.setTotalHours(totalHours);
            bill.setAmount(amount);
            bill.setStatus("PENDING");

            boolean saved = billDAO.recordBill(bill);
            if (!saved) {
                con.rollback();
                return false;
            }

            memberDAO.updateOutstandingBalance(
                    memberID,
                    member.getOutstandingBalance() + amount);

            con.commit();
            return true;

        } catch (Exception e) {
            con.rollback();
            throw e;
        }
    }
}
