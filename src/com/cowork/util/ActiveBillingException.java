package com.cowork.util;

public class ActiveBillingException extends Exception {

    @Override
    public String toString() {
        return "Member has active pending bills and cannot be removed";
    }
}