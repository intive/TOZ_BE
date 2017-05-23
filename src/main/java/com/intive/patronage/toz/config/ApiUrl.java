package com.intive.patronage.toz.config;

public final class ApiUrl {
    public final static String PETS_PATH = "/pets";
    public final static String NEWS_PATH = "/news";
    public final static String USERS_PATH = "/users";
    public final static String ORGANIZATION_PATH = "/organization";
    public final static String SUPER_ADMIN_USERS_PATH = "/admin" + USERS_PATH;
    public final static String ORGANIZATION_INFO_PATH = ORGANIZATION_PATH + "/info";
    public final static String TOKENS_PATH = "/tokens";
    public final static String ACTIVATION_PATH = "/activate";
    public final static String REGISTER_PATH = USERS_PATH  + "/register";
    public final static String ACQUIRE_TOKEN_PATH = TOKENS_PATH + "/acquire";
    public final static String HOW_TO_BECOME_VOLUNTEER_PATH = ORGANIZATION_PATH + "/becomevolunteer";
    public final static String HOW_TO_DONATE_PATH = ORGANIZATION_PATH + "/howtodonate";
    public static final String PASSWORDS_PATH = USERS_PATH  + "/passwords";
    public static final String PASSWORDS_RESET_PATH = "/reset";
    public static final String PASSWORDS_RESET_SEND_TOKEN_PATH = "/reset/send";
    public final static String COMMENTS_PATH = "/comments";
    public final static String PROPOSAL_PATH = "/proposals";

    private ApiUrl() {
    }
}
