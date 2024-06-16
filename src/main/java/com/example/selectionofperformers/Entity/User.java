package com.example.selectionofperformers.Entity;

public class User {
    protected String login;
    protected String password;
    public User(String login, String password){

        this.login = login;
        this.password = password;
    }

    public User() {

    }

    public String getLogin() {
        return login;
    }
    public String getPassword() {
        return password;
    }
    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
