package com.si_ware.neospectra.dbtable;


public class Constants {

    //db name

    public static final String  DB_NAME="NEO_DB";

    public static final int  DB_VERSION = 1 ;

    public static final String  TABLE_NAME="NEO_TB";

    public static final String C_ID = "ID";
    public static final String C_BRAY = "BRAY";
    public static final String C_CA = "CA";
    public static final String C_CLAY = "CLAY";
    public static final String C_CN = "CN";
    public static final String C_HCL25_K2O = "HCLK2O";
    public static final String C_HCL25_P2O5 = "HCLP2O5";
    public static final String C_JUMLAH = "JUMLAH";
    public static final String C_K = "K";
    public static final String C_KB_ADJUSTED = "KBADJ";
    public static final String C_KJELDAHL_N= "KJELDAHL";
    public static final String C_KTK= "KTK";
    public static final String C_MG= "MG";
    public static final String C_MORGAN_K2O= "MORGAN";
    public static final String C_NA= "NA";
    public static final String C_OLSEN_P2O5= "OLSEN";
    public static final String C_PH_H2O= "PHH2O";
    public static final String C_PH_KCL= "PHKCL";
    public static final String C_RETENSIP= "RETENSIP";
    public static final String C_SAND= "SAND";
    public static final String C_SILT= "SILT";
    public static final String C_WBC= "WBC";
    public static final String C_ADDED_TIME_STAMP = "ADDED_TIME_STAMP";


    //create table query

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + C_BRAY + " TEXT,"
            + C_CA + " TEXT,"
            + C_CLAY + " TEXT,"
            + C_CN + " TEXT,"
            + C_HCL25_K2O + " TEXT,"
            + C_HCL25_P2O5 + " TEXT,"
            + C_JUMLAH + " TEXT,"
            + C_K + " TEXT,"
            + C_KB_ADJUSTED + " TEXT,"
            + C_KJELDAHL_N + " TEXT,"
            + C_KTK + " TEXT,"
            + C_MG + " TEXT,"
            + C_MORGAN_K2O + " TEXT,"
            + C_NA + " TEXT,"
            + C_OLSEN_P2O5 + " TEXT,"
            + C_PH_H2O + " TEXT,"
            + C_PH_KCL + " TEXT,"
            + C_RETENSIP + " TEXT,"
            + C_SAND + " TEXT,"
            + C_SILT + " TEXT,"
            + C_WBC + " TEXT,"
            + C_ADDED_TIME_STAMP+ " TEXT"

            + ")";
}
