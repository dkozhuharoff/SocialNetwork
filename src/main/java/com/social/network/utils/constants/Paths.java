package com.social.network.utils.constants;

public class Paths {
    public static final String CATEGORY_ID = "/{categoryId}";
    public static final String POST_ID = "/{postId}";
    public static final String MAIN_PATH = "/api/v1";
    public static final String MAIN_AUTH_PATH = MAIN_PATH + "/auth";
    public static final String MAIN_CATEGORY_PATH = MAIN_PATH + "/category";
    public static final String MAIN_ADMIN_PATH =  MAIN_PATH + "/admin";
    public static final String MAIN_POST_PATH = MAIN_PATH + "/post";
    public static final String MAIN_CATEGORYID_POST_PATH = MAIN_CATEGORY_PATH + CATEGORY_ID + "/post";

    public static final String MAIN_COMMENT_PATH = MAIN_PATH + "/comment";
    public static final String MAIN_POSTID_COMMENT_PATH = MAIN_POST_PATH + POST_ID + "/comment";
    private Paths() {
    }
}
