package com.cowork.util;

public class SlotAlreadyBookedException extends Exception {

    @Override
    public String toString() {
        return "Selected desk and time slot is already booked";
    }
}
