package com.iiitb.datausage.Model;

/**
 * Created by Bala Manoj on 17-11-2016.
 */
public class StaticVariables
{
    //public static String serverURL = "http://172.16.82.80:8888/"; //SANAT LOCAL IIITB-BL
    //public static String serverURL = "http://172.16.85.227:8080/Sensors/webapi/sensors/"; //MANOJ LOCAL IIITB-BM
    public static String serverURL = "http://mobiledatausage.herokuapp.com/"; //DOMAIN
    public static String number = null;

    //Input bytes, Output: data + unit
    public static String dataConverter(long bytes)
    {
        String result = "";
        String units[] = {"Bytes", "KB", "MB", "GB", "TB"};

        double value = bytes;
        int divisor = 1024;
        int count = 0;

        result = String.format("%.2f", value) + " " + units[0];
        while(value > 1024)
        {
            value = (value * 1.0) / divisor;
            result = String.format("%.2f", value) + " " + units[++count];
        }

        return result;
    }
}
