package com.hlaway.co.domain;

/**
 * User: hl-away
 * Date: 06.02.14
 */
public class GameStep {
    public static final String TYPE_ADD_CITY = "add_city";
    public static final String TYPE_ADD_NEW_CITY = "add_new_city";
    public static final String TYPE_CONNECT_USER = "connect_user";

    private long step;
    private String type;
    private long userId;
    private String value;

    public long getStep() {
        return step;
    }

    public void setStep(long step) {
        this.step = step;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
