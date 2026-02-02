package com.cowork.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.cowork.bean.Member;
import com.cowork.util.DBUtil;

public class MemberDAO {

    
    public Member findMember(String memberID) throws Exception {

        Connection con = DBUtil.getDBConnection();
        String query = "SELECT * FROM MEMBER_TBL WHERE MEMBER_ID = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, memberID);

        ResultSet rs = ps.executeQuery();
        Member member = null;

        if (rs.next()) {
            member = new Member();
            member.setMemberID(rs.getString("MEMBER_ID"));
            member.setFullName(rs.getString("FULL_NAME"));
            member.setEmail(rs.getString("EMAIL"));
            member.setMobile(rs.getString("MOBILE"));
            member.setMembershipTier(rs.getString("MEMBERSHIP_TIER"));
            member.setOutstandingBalance(rs.getDouble("OUTSTANDING_BALANCE"));
        }

        rs.close();
        ps.close();
        con.close();

        return member; // returns null if not found
    }

    
    public List<Member> viewAllMembers() throws Exception {

        List<Member> list = new ArrayList<>();
        Connection con = DBUtil.getDBConnection();

        String query = "SELECT * FROM MEMBER_TBL";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Member member = new Member();
            member.setMemberID(rs.getString("MEMBER_ID"));
            member.setFullName(rs.getString("FULL_NAME"));
            member.setEmail(rs.getString("EMAIL"));
            member.setMobile(rs.getString("MOBILE"));
            member.setMembershipTier(rs.getString("MEMBERSHIP_TIER"));
            member.setOutstandingBalance(rs.getDouble("OUTSTANDING_BALANCE"));

            list.add(member);
        }

        rs.close();
        ps.close();
        con.close();

        return list;
    }


    public boolean insertMember(Member member) throws Exception {

        Connection con = DBUtil.getDBConnection();

        String query = "INSERT INTO MEMBER_TBL VALUES (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setString(1, member.getMemberID());
        ps.setString(2, member.getFullName());
        ps.setString(3, member.getEmail());
        ps.setString(4, member.getMobile());
        ps.setString(5, member.getMembershipTier());
        ps.setDouble(6, member.getOutstandingBalance());

        int rows = ps.executeUpdate();
        con.commit();

        ps.close();
        con.close();

        return rows > 0;
    }

    public boolean updateOutstandingBalance(String memberID, double newBalance) throws Exception {

        Connection con = DBUtil.getDBConnection();

        String query = "UPDATE MEMBER_TBL SET OUTSTANDING_BALANCE = ? WHERE MEMBER_ID = ?";
        PreparedStatement ps = con.prepareStatement(query);

        ps.setDouble(1, newBalance);
        ps.setString(2, memberID);

        int rows = ps.executeUpdate();
        con.commit();

        ps.close();
        con.close();

        return rows > 0;
    }


    public boolean deleteMember(String memberID) throws Exception {

        Connection con = DBUtil.getDBConnection();

        String query = "DELETE FROM MEMBER_TBL WHERE MEMBER_ID = ?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, memberID);

        int rows = ps.executeUpdate();
        con.commit();

        ps.close();
        con.close();

        return rows > 0;
    }
}
