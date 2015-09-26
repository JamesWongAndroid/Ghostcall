package com.kickbackapps.ghostcall.User;

import com.google.gson.annotations.Expose;

/**
 * Created by Ynott on 9/16/15.
 */
public class Sip {
    @Expose
    private String username;
    @Expose
    private String password;

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The password
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     * The password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
