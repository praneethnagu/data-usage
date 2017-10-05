package com.iiitb.datausage.Model;

import java.util.Date;

/**
 * Created by Bala Manoj on 20-10-2016.
 */
public class RegistrationModel
{
    private String name;
    private String gender;
    private Date dob;
    private String email;
    private String password;
    private String profession;
    private String mobileNumber;
    private String wifiUsage;
    private String dataUsage;

    public RegistrationModel(String name, String gender, String email, String password, String profession, String mobileNumber,
                             String wifiUsage, String dataUsage, Date dob)
    {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.profession = profession;
        this.mobileNumber = mobileNumber;
        this.wifiUsage = wifiUsage;
        this.dataUsage = dataUsage;
        this.dob = dob;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getWifiUsage() {
        return wifiUsage;
    }

    public void setWifiUsage(String wifiUsage) {
        this.wifiUsage = wifiUsage;
    }

    public String getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(String dataUsage) {
        this.dataUsage = dataUsage;
    }
}
