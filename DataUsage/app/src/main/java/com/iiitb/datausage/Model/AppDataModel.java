package com.iiitb.datausage.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by Bala Manoj on 20-10-2016.
 */
public class AppDataModel
{
    private String applicationName;
    private long total;
    private long TX;
    private long RX;
    private Drawable icon;

    public AppDataModel()
    {

    }

    public AppDataModel(String applicationName, long TX, long RX, Drawable icon)
    {
        this.applicationName = applicationName;
        this.TX = TX;
        this.RX = RX;
        this.total = TX + RX;
        this.icon = icon;
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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
