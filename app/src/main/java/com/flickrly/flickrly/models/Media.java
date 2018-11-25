package com.flickrly.flickrly.models;


import java.io.Serializable;

public class Media implements Serializable {

    private String m;

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getHQ() {
        return getM().replace("_m.", "_b.");
    }


    @Override
    public String toString() {
        return "[m = " + m + "]";
    }
}
	