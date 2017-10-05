package com.iiitb.datausage.Model;

/**
 * Created by Bala Manoj on 20-10-2016.
 */
public class ApplicationDataModel
{
    private String applicationName;
    private long total;
    private long TX;
    private long RX;

    public ApplicationDataModel()
    {

    }

    public ApplicationDataModel(String applicationName, long TX, long RX)
    {
        this.applicationName = applicationName;
        this.TX = TX;
        this.RX = RX;
        this.total = TX + RX;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTX() {
        return TX;
    }

    public void setTX(long TX) {
        this.TX = TX;
    }

    public long getRX() {
        return RX;
    }

    public void setRX(long RX) {
        this.RX = RX;
    }
}
