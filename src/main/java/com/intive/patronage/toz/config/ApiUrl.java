package com.intive.patronage.toz.config;

public final class ApiUrl {
    public final static String PETS_PATH = "/pets";
    public final static String NEWS_PATH = "/news";
    public final static String USERS_PATH = "/users";
    public final static String SUPER_ADMIN_USERS_PATH = "/admin" + USERS_PATH;
    public final static String ORGANIZATION_INFO_PATH = "/organization/info";
    public final static String TOKENS_PATH = "/tokens";
    public final static String ACTIVATION_PATH = "/activate";
    public final static String ACQUIRE_TOKEN_PATH = TOKENS_PATH + "/acquire";
    public static final String PASSWORDS_PATH = "/users/passwords";

    private ApiUrl() {
    }
}
