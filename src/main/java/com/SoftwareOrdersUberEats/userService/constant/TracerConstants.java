package com.SoftwareOrdersUberEats.userService.constant;


public class TracerConstants {
    public static final String CORRELATION_KEY = "correlationId";
    public static final String CORRELATION_HEADER = "X-Correlation-Id";

    //MESSAGE
    public static final String MESSAGE_UPDATE_USER = "Update user {}";
    public static final String MESSAGE_SAVE_EVENT = "Save event topic name topic {}";
    public static final String MESSAGE_SAVE_USER = "Save user {}";
    public static final String MESSAGE_SEND_EVENT = "id event {}";
    public static final String MESSAGE_DATA_VALIDATION_TO_CREATE_USER_ERROR = "Data validation error to create user ";
    //EXCEPTION
    public static final String EXCEPTION_ALREADY_EVENT_PROCESSED = "Exception already event processed {}";
    //ERROR
    public static final String ERROR_SEND_EVENT = "Error send event";
}
