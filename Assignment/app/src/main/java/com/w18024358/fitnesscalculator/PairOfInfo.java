package com.w18024358.fitnesscalculator;

public class PairOfInfo
{
    String fname;
    String sname;
    String test;

    public PairOfInfo(String fname, String sname, String test) {
        this.fname = fname;
        this.sname = sname;
        this.test = test;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getTest() { return test; }
    public void setTest(String test) { this.test = test;}
}
