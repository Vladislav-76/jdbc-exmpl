package org.example;

import java.util.Date;

public record User(String login, String password, String email, Date date) { }
