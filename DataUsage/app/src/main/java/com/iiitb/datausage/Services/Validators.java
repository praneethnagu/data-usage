package com.iiitb.datausage.Services;

/**
 * Created by Bala Manoj on 20-10-2016.
 */
public class Validators
{
    /*
     To validate the Email Address
  */
    public boolean isValidMail(String email)
    {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    /*
        To validate the Mobile Number
     */
    public boolean isValidMobile(String phone)
    {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
