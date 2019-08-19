package com.example.popsapp.data.model

enum class OrderStatus(val status: String) {
    CANCELED("canceled"),
    DELIVERED("delivered"),
    DEPOSITED("deposited"),
    ERROR_PROCESSING("error_processing"),
    ERROR("error"),
    PACKING("packing"),
    PAID("paid"),
    PAID_PENDING_1("paid_pending_check_1"),
    PAID_PENDING_2("paid_pending_check_2"),
    PAUSED("paused"),
    PENDING("pending"),
    PRINTED("printed"),
    PRINTING("printing"),
    PROCESSING("processing"),
    REFUNDED("refunded"),
    SENT("sent"),
    SENT_DELIVENGO("sent_delivengo"),
    SENT_FRANCE("sent_france")
}