package com.iiitb.datausage.Model;

/**
 * Created by Bala Manoj on 21-10-2016.
 */
public class DatabaseModel
{
    private int uid;
    private long tx;
    private long rx;
    private String name;

    public DatabaseModel()
    {

    }

    public DatabaseModel(int uid, long tx, long rx, String name)
    {
        this.uid = uid;
        this.tx = tx;
        this.rx = rx;
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public long getTx() {
        return tx;
    }

    public void setTx(long tx) {
        this.tx = tx;
    }

    public long getRx() {
        return rx;
    }

    public void setRx(long rx) {
        this.rx = rx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
