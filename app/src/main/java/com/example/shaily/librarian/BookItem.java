package com.example.shaily.librarian;

/**
 * Created by Shaily on 14-07-2017.
 */

public class BookItem {

    private String bna,authn,iss;
    private int total_fine;

    public BookItem(String bn,String au,String is,int tf){
        this.setBna(bn);
        this.setAuthn(au);
        this.setIss(is);
        this.setTotal_fine(tf);
    }

    public int getTotal_fine() {
        return total_fine;

    }

    public String getAuthn() {
        return authn;
    }

    public String getBna() {
        return bna;
    }

    public String getIss() {
        return iss;
    }

    public void setAuthn(String authn) {
        this.authn = authn;
    }

    public void setBna(String bna) {
        this.bna = bna;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public void setTotal_fine(int total_fine) {
        this.total_fine = total_fine;
    }



}
