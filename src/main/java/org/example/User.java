package org.example;

import java.util.Date;

public class User {

    private final String login;
    private final String password;
    private final String email;
    private final Date date;

    public User(String login, String password, String email, Date date) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.date = date;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Date getDate() {
        return date;
    }

}
