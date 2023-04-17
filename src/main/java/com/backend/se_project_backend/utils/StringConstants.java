package com.backend.se_project_backend.utils;

public class StringConstants {
    //Info about system
    public static final String NO_REPLY_EMAIL_ADDRESS = "no-reply@cluj-bike-sharing.ro";

    //Email
    public static final String EMAIL_CONFIRM_SUBJECT = "Welcome aboard!";
    public static final String EMAIL_CONFIRM_BODY = "This email confirms your registration to Cluj Bike Sharing System! PS: If this was not your action, too bad, we did not implement a way for you to tell us. (stay tuned for future implementations that may let you out of this #evilLaughter).";

    //Exceptions messages
    public static final String EX_MAIL_NOT_SENT = "info: Unfortunately the confirmation email was not sent. However, registration was successful";
    public static final String STATION_NOT_FOUND = "There is no station having the given name";
}
