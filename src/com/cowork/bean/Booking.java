package com.cowork.bean;
import java.util.Date;
 public class Booking {

	    private int bookingID;
	    private String memberID;
	    private String deskCode;
	    private Date bookingDate;
	    private String startTime;
	    private String endTime;
	    private String status; // BOOKED / CANCELLED / COMPLETED

	    public int getBookingID() {
	        return bookingID;
	    }

	    public void setBookingID(int bookingID) {
	        this.bookingID = bookingID;
	    }

	    public String getMemberID() {
	        return memberID;
	    }

	    public void setMemberID(String memberID) {
	        this.memberID = memberID;
	    }

	    public String getDeskCode() {
	        return deskCode;
	    }

	    public void setDeskCode(String deskCode) {
	        this.deskCode = deskCode;
	    }

	    public Date getBookingDate() {
	        return bookingDate;
	    }

	    public void setBookingDate(Date bookingDate) {
	        this.bookingDate = bookingDate;
	    }

	    public String getStartTime() {
	        return startTime;
	    }

	    public void setStartTime(String startTime) {
	        this.startTime = startTime;
	    }

	    public String getEndTime() {
	        return endTime;
	    }

	    public void setEndTime(String endTime) {
	        this.endTime = endTime;
	    }

	    public String getStatus() {
	        return status;
	    }

	    public void setStatus(String status) {
	        this.status = status;
	    }
	}



