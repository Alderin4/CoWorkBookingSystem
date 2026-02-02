package com.cowork.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.cowork.bean.Booking;
import com.cowork.util.DBUtil;

public class BookingDAO {


    public int generateBookingID() throws Exception {

        Connection con = DBUtil.getDBConnection();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT NVL(MAX(BOOKING_ID), 510000) + 1 FROM BOOKING_TBL");
        rs.next();
        int bookingID = rs.getInt(1);

        rs.close();
        st.close();
        con.close();

        return bookingID;
    }

  
    public boolean recordBooking(Booking booking) throws Exception {

        Connection con = DBUtil.getDBConnection();

        String query = "INSERT INTO BOOKING_TBL VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, booking.getBookingID());
        ps.setString(2, booking.getMemberID());
        ps.setString(3, booking.getDeskCode());
        ps.setDate(4, new Date(booking.getBookingDate().getTime()));
        ps.setString(5, booking.getStartTime());
        ps.setString(6, booking.getEndTime());
        ps.setString(7, booking.getStatus());

        int rows = ps.executeUpdate();
        con.commit();

        ps.close();
        con.close();

        return rows > 0;
    }

  
    public boolean updateBookingStatus(int bookingID, String status) throws Exception {

        Connection con = DBUtil.getDBConnection();

        String query = "UPDATE BOOKING_TBL SET STATUS = ? WHERE BOOKING_ID = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, status);
        ps.setInt(2, bookingID);

        int rows = ps.executeUpdate();
        con.commit();

        ps.close();
        con.close();

        return rows > 0;
    }

    
    public List<Booking> findBookingsByMember(String memberID) throws Exception {

        List<Booking> list = new ArrayList<>();
        Connection con = DBUtil.getDBConnection();

        String query = "SELECT * FROM BOOKING_TBL WHERE MEMBER_ID = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, memberID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Booking booking = new Booking();
            booking.setBookingID(rs.getInt("BOOKING_ID"));
            booking.setMemberID(rs.getString("MEMBER_ID"));
            booking.setDeskCode(rs.getString("DESK_CODE"));
            booking.setBookingDate(rs.getDate("BOOKING_DATE"));
            booking.setStartTime(rs.getString("START_TIME"));
            booking.setEndTime(rs.getString("END_TIME"));
            booking.setStatus(rs.getString("STATUS"));

            list.add(booking);
        }

        rs.close();
        ps.close();
        con.close();

        return list;
    }

 
    public List<Booking> findBookingsForDeskAndDate(String deskCode, java.util.Date bookingDate) throws Exception {

        List<Booking> list = new ArrayList<>();
        Connection con = DBUtil.getDBConnection();

        String query = "SELECT * FROM BOOKING_TBL WHERE DESK_CODE = ? AND BOOKING_DATE = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, deskCode);
        ps.setDate(2, new Date(bookingDate.getTime()));

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Booking booking = new Booking();
            booking.setBookingID(rs.getInt("BOOKING_ID"));
            booking.setMemberID(rs.getString("MEMBER_ID"));
            booking.setDeskCode(rs.getString("DESK_CODE"));
            booking.setBookingDate(rs.getDate("BOOKING_DATE"));
            booking.setStartTime(rs.getString("START_TIME"));
            booking.setEndTime(rs.getString("END_TIME"));
            booking.setStatus(rs.getString("STATUS"));

            list.add(booking);
        }

        rs.close();
        ps.close();
        con.close();

        return list;
    }
}
