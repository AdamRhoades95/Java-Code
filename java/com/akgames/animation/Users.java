package com.akgames.animation;

public class Users {
    private String username, password, userPassword;

    public Users() {
    }

    public Users(String username, String password){
        this.username = username;
        this.password = password;
        //this.userPassword = username+"-"+password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    //public String getUserPassword() {
    //    return userPassword;
    //}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //public void setUserPassword(String userPassword) {
        //this.userPassword = userPassword;
    //}
}
