package com.iiitb.datausage.Model;

/**
 * Created by Bala Manoj on 20-10-2016.
 */
public class LoginModel
{
    private String id;
    private String password;

    public LoginModel(String id, String password)
    {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
