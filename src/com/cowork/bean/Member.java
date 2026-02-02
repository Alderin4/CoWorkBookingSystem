package com.cowork.bean;

public class Member {
	    private String memberID;
	    private String fullName;
	    private String email;
	    private String mobile;
	    private String membershipTier;
	    private double outstandingBalance;
		public String getMemberID() {
			return memberID;
		}
		public void setMemberID(String memberID) {
			this.memberID = memberID;
		}
		public String getFullName() {
			return fullName;
		}
		public void setFullName(String fullName) {
			this.fullName = fullName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
		public String getMembershipTier() {
			return membershipTier;
		}
		public void setMembershipTier(String membershipTier) {
			this.membershipTier = membershipTier;
		}
		public double getOutstandingBalance() {
			return outstandingBalance;
		}
		public void setOutstandingBalance(double outstandingBalance) {
			this.outstandingBalance = outstandingBalance;
		}
	    


}
