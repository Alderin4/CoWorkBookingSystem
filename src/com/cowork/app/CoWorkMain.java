package com.cowork.app;

import java.sql.Date;
import java.util.Scanner;

import com.cowork.bean.Member;
import com.cowork.service.BookingService;
import com.cowork.util.ActiveBillingException;
import com.cowork.util.SlotAlreadyBookedException;
import com.cowork.util.ValidationException;

public class CoWorkMain {

    private static BookingService bookingService = new BookingService();

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        System.out.println("=======================================");
        System.out.println(" Co-Working Space Desk Booking System ");
        System.out.println("=======================================");

        do {
            System.out.println("\n1. Add New Member");
            System.out.println("2. View Member Details");
            System.out.println("3. View All Members");
            System.out.println("4. Create Booking");
            System.out.println("5. Cancel Booking");
            System.out.println("6. Generate Usage Bill");
            System.out.println("7. Remove Member");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {

                case 1:
                    Member m = new Member();
                    System.out.print("Member ID: ");
                    m.setMemberID(sc.nextLine());
                    System.out.print("Full Name: ");
                    m.setFullName(sc.nextLine());
                    System.out.print("Email: ");
                    m.setEmail(sc.nextLine());
                    System.out.print("Mobile: ");
                    m.setMobile(sc.nextLine());
                    System.out.print("Membership Tier (BASIC/STANDARD/PREMIUM): ");
                    m.setMembershipTier(sc.nextLine());

                    boolean added = bookingService.addNewMember(m);
                    System.out.println(added ? "Member Added Successfully" : "Member Already Exists");
                    break;

                case 2:
                    System.out.print("Enter Member ID: ");
                    String mid = sc.nextLine();
                    Member member = bookingService.viewMemberDetails(mid);
                    if (member != null) {
                        System.out.println("Name: " + member.getFullName());
                        System.out.println("Email: " + member.getEmail());
                        System.out.println("Mobile: " + member.getMobile());
                        System.out.println("Tier: " + member.getMembershipTier());
                        System.out.println("Outstanding Balance: " + member.getOutstandingBalance());
                    } else {
                        System.out.println("Member not found");
                    }
                    break;

                case 3:
                    bookingService.viewAllMembers().forEach(mem -> {
                        System.out.println(mem.getMemberID() + " | " +
                                mem.getFullName() + " | " +
                                mem.getMembershipTier() + " | " +
                                mem.getOutstandingBalance());
                    });
                    break;

                case 4:
                    System.out.print("Member ID: ");
                    String bm = sc.nextLine();
                    System.out.print("Desk Code: ");
                    String desk = sc.nextLine();
                    System.out.print("Booking Date (yyyy-mm-dd): ");
                    Date bdate = Date.valueOf(sc.nextLine());
                    System.out.print("Start Time (HH:MM): ");
                    String st = sc.nextLine();
                    System.out.print("End Time (HH:MM): ");
                    String et = sc.nextLine();

                    boolean booked = bookingService.createBooking(bm, desk, bdate, st, et);
                    System.out.println(booked ? "Booking Successful" : "Booking Failed");
                    break;

                case 5:
                    System.out.print("Booking ID: ");
                    int bid = sc.nextInt();
                    boolean cancelled = bookingService.cancelBooking(bid);
                    System.out.println(cancelled ? "Booking Cancelled" : "Cancellation Failed");
                    break;

                case 6:
                    System.out.print("Member ID: ");
                    String memid = sc.nextLine();
                    System.out.print("From Date (yyyy-mm-dd): ");
                    Date from = Date.valueOf(sc.nextLine());
                    System.out.print("To Date (yyyy-mm-dd): ");
                    Date to = Date.valueOf(sc.nextLine());

                    boolean bill = bookingService.generateUsageBill(memid, from, to);
                    System.out.println(bill ? "Bill Generated" : "Bill Generation Failed");
                    break;

                case 7:
                    System.out.print("Member ID: ");
                    String del = sc.nextLine();
                    boolean removed = bookingService.removeMember(del);
                    System.out.println(removed ? "Member Removed" : "Cannot Remove Member");
                    break;

                case 0:
                    System.out.println("Thank you. Exiting...");
                    break;

                default:
                    System.out.println("Invalid choice");
                }

            } catch (SlotAlreadyBookedException e) {
                System.out.println("Error: " + e);
            } catch (ActiveBillingException e) {
                System.out.println("Error: " + e);
            } catch (ValidationException e) {
                System.out.println("Validation Error: " + e);
            } catch (Exception e) {
                System.out.println("System Error: " + e.getMessage());
            }

        } while (choice != 0);

        sc.close();
    }
}
