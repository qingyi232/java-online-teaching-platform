package com.teaching.common;

public class Constants {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_TEACHER = "TEACHER";
    public static final String ROLE_STUDENT = "STUDENT";

    public static final int STATUS_DISABLED = 0;
    public static final int STATUS_ENABLED = 1;

    public static final int AUDIT_PENDING = 0;
    public static final int AUDIT_APPROVED = 1;
    public static final int AUDIT_REJECTED = 2;

    public static final int QA_UNSOLVED = 0;
    public static final int QA_SOLVED = 1;

    public static final String REDIS_RESOURCE_PREFIX = "resource:";
    public static final String REDIS_HOT_RESOURCE = "hot:resources";
    public static final long REDIS_EXPIRE_SECONDS = 3600;
}
