package com.cowork.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.cowork.bean.Bill;
import com.cowork.util.DBUtil;

public class BillDAO {

   
    public int generateBillID() throws Exception {

        Connection con = DBUtil.getDBConnection();
        Statement st = con.createStatement();

        ResultSet rs = st.executeQuery("SELECT NVL(MAX(BILL_ID), 610000) + 1 FROM BILL_TBL");
        rs.next();
        int billID = rs.getInt(1);

        rs.close();
        st.close();
        con.close();

        return billID;
    }

 
    public boolean recordBill(Bill bill) throws Exception {

        Connection con = DBUtil.getDBConnection();

        String query = "INSERT INTO BILL_TBL VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setInt(1, bill.getBillID());
        ps.setString(2, bill.getMemberID());
        ps.setDate(3, new Date(bill.getBillingPeriodFrom().getTime()));
        ps.setDate(4, new Date(bill.getBillingPeriodTo().getTime()));
        ps.setDouble(5, bill.getTotalHours());
        ps.setDouble(6, bill.getAmount());
        ps.setString(7, bill.getStatus());

        int rows = ps.executeUpdate();
        con.commit();

        ps.close();
        con.close();

        return rows > 0;
    }

  
    public boolean updateBillStatus(int billID, String status) throws Exception {

        Connection con = DBUtil.getDBConnection();

        String query = "UPDATE BILL_TBL SET STATUS = ? WHERE BILL_ID = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, status);
        ps.setInt(2, billID);

        int rows = ps.executeUpdate();
        con.commit();

        ps.close();
        con.close();

        return rows > 0;
    }

   
    public List<Bill> findPendingBillsByMember(String memberID) throws Exception {

        List<Bill> list = new ArrayList<>();
        Connection con = DBUtil.getDBConnection();

        String query = "SELECT * FROM BILL_TBL WHERE MEMBER_ID = ? AND STATUS = 'PENDING'";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, memberID);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Bill bill = new Bill();
            bill.setBillID(rs.getInt("BILL_ID"));
            bill.setMemberID(rs.getString("MEMBER_ID"));
            bill.setBillingPeriodFrom(rs.getDate("BILLING_PERIOD_FROM"));
            bill.setBillingPeriodTo(rs.getDate("BILLING_PERIOD_TO"));
            bill.setTotalHours(rs.getDouble("TOTAL_HOURS"));
            bill.setAmount(rs.getDouble("AMOUNT"));
            bill.setStatus(rs.getString("STATUS"));

            list.add(bill);
        }

        rs.close();
        ps.close();
        con.close();

        return list;
    }
}
