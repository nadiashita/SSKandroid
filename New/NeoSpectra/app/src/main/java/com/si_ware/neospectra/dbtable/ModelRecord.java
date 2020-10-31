package com.si_ware.neospectra.dbtable;


import java.io.Serializable;

public class ModelRecord {




    //Variables
    String id, bray, ca, clay, cn, hclk2o, hclp2o5, jumlah, k, kbadj, kjeldahl, ktk,mg, morgan, na, olsen, phh2o, phkcl, retensip, sand ,silt,wbc,  addedTime;
    boolean isSelect;


//constructor

    public ModelRecord(String id, String bray, String ca, String clay, String cn, String hclk2o, String hclp2o5, String jumlah, String k, String kbadj, String kjeldahl, String ktk, String mg, String morgan, String na, String olsen, String phh2o, String phkcl, String retensip, String sand, String silt, String wbc, String addedTime) {
        this.id = id;
        this.bray = bray;
        this.ca = ca;
        this.clay = clay;
        this.cn = cn;
        this.hclk2o = hclk2o;
        this.hclp2o5 = hclp2o5;
        this.jumlah = jumlah;
        this.k = k;
        this.kbadj = kbadj;
        this.kjeldahl = kjeldahl;
        this.ktk = ktk;
        this.mg = mg;
        this.morgan = morgan;
        this.na = na;
        this.olsen = olsen;
        this.phh2o = phh2o;
        this.phkcl = phkcl;
        this.retensip = retensip;
        this.sand = sand;
        this.silt = silt;
        this.wbc = wbc;
        this.addedTime = addedTime;
    }


    //getter setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBray() {
        return bray;
    }

    public void setBray(String bray) {
        this.bray = bray;
    }

    public String getCa() {
        return ca;
    }

    public void setCa(String ca) {
        this.ca = ca;
    }

    public String getClay() {
        return clay;
    }

    public void setClay(String clay) {
        this.clay = clay;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getHclk2o() {
        return hclk2o;
    }

    public void setHclk2o(String hclk2o) {
        this.hclk2o = hclk2o;
    }

    public String getHclp2o5() {
        return hclp2o5;
    }

    public void setHclp2o5(String hclp2o5) {
        this.hclp2o5 = hclp2o5;
    }

    public String getJumlah() {
        return jumlah;
    }

    public void setJumlah(String jumlah) {
        this.jumlah = jumlah;
    }

    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getKbadj() {
        return kbadj;
    }

    public void setKbadj(String kbadj) {
        this.kbadj = kbadj;
    }

    public String getKjeldahl() {
        return kjeldahl;
    }

    public void setKjeldahl(String kjeldahl) {
        this.kjeldahl = kjeldahl;
    }

    public String getKtk() {
        return ktk;
    }

    public void setKtk(String ktk) {
        this.ktk = ktk;
    }

    public String getMg() {
        return mg;
    }

    public void setMg(String mg) {
        this.mg = mg;
    }

    public String getMorgan() {
        return morgan;
    }

    public void setMorgan(String morgan) {
        this.morgan = morgan;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

    public String getOlsen() {
        return olsen;
    }

    public void setOlsen(String olsen) {
        this.olsen = olsen;
    }

    public String getPhh2o() {
        return phh2o;
    }

    public void setPhh2o(String phh2o) {
        this.phh2o = phh2o;
    }

    public String getPhkcl() {
        return phkcl;
    }

    public void setPhkcl(String phkcl) {
        this.phkcl = phkcl;
    }

    public String getRetensip() {
        return retensip;
    }

    public void setRetensip(String retensip) {
        this.retensip = retensip;
    }

    public String getSand() {
        return sand;
    }

    public void setSand(String sand) {
        this.sand = sand;
    }

    public String getSilt() {
        return silt;
    }

    public void setSilt(String silt) {
        this.silt = silt;
    }

    public String getWbc() {
        return wbc;
    }

    public void setWbc(String wbc) {
        this.wbc = wbc;
    }

    public String getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(String addedTime) {
        this.addedTime = addedTime;
    }



    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
