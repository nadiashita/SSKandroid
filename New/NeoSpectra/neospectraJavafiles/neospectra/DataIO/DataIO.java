package com.si_ware.neospectra.DataIO;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.si_ware.neospectra.Models.dbModule;
import com.si_ware.neospectra.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static com.si_ware.neospectra.Global.Global.KEY_ERROR_NOT_FOUND;
import static com.si_ware.neospectra.Global.Global.KEY_MODULE_ID;

/**
 * Created by AmrWinter on 12/28/17.
 */

public class DataIO {
    private static final int File_MODE_APPEND = Context.MODE_APPEND;
    private static final int FILE_MODE_PRIVATE = Context.MODE_PRIVATE;

    public static void saveToSharedPreferences(Context context, String key, String value){
        SharedPreferences sharedPref = context.getSharedPreferences(
                String.valueOf(R.string.key_Preferences_File),
                FILE_MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }

//    public static void addNewModuleToSharedPreferences(Context context, String key, String value){
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                String.valueOf(R.string.key_Preferences_File),
//                File_MODE_APPEND);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(key, value);
//        editor.apply();
//    }

    public static String readFromSharedPreferences(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(
                String.valueOf(R.string.key_Preferences_File),
                FILE_MODE_PRIVATE);
        return sharedPref.getString(key, KEY_ERROR_NOT_FOUND);
    }

    public static void addModuleMetaData2SharedFile(Context context, dbModule module){
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                String.valueOf(R.string.key_Preferences_File),
//                File_MODE_APPEND);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(KEY_MODULE_NAME, module.getModuleName());
//        editor.putString(KEY_MODULE_ID, module.getModuleID());
        //Save variable data to internal storage
//        for (dbVariable var :
//                module.getModuleVariables()) {
//            editor.putFloat(var.getVarName(), ((float) var.getVarResult()));
//        }
//        editor.apply();
    }

//    public static void saveModuleData2SharedFile(Context context, dbModule module){
//        SharedPreferences sharedPref = context.getSharedPreferences(
//                String.valueOf(R.string.Key_Data_file),
//                File_MODE_APPEND);
//
//        SharedPreferences.Editor editor = sharedPref.edit();
////        editor.putString(KEY_MODULE_ID, module.getModuleID());
//        editor.putStringSet(KEY_MODULE_DATA, module.getModuleData().getStrData());
//        editor.apply();
//    }

    public static Set<String> retrieveUserModules(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences(
                String.valueOf(R.string.key_Preferences_File),
                FILE_MODE_PRIVATE);
        return sharedPref
                .getStringSet(KEY_MODULE_ID, new HashSet<String>());
    }

    public static void saveJsonData2File(Context context, String fileName, String data){
        try {
            fileName = context.getFilesDir() + "/" +fileName;
            FileOutputStream file = context.openFileOutput(fileName, FILE_MODE_PRIVATE);
            file.write(data.getBytes(), 0, data.length());
            file.close();
            Toast.makeText(context, "File Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
            Log.e("err_io", e.getMessage());
        }
    }

    public static void writeStringAsFile(Context context, String fileName, final String fileContents) {
        Log.v("data_writeStringAsFile", "fileName: " + fileName + ", content: "+ fileContents);
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName));
            out.write(fileContents);
            Log.v("Modulefile", fileContents);
            out.close();
            Toast.makeText(context,
                    "File Saved, data length= " + fileContents.length(),
                    Toast.LENGTH_SHORT).show();
            Log.v("data_fileContent", fileContents);
        } catch (IOException e) {
            Log.e("err_io_writeString", e.getMessage());
        }
    }

    public static void appendDataToFile(Context context, final String fileContents, String fileName) {
        try {
            FileWriter out = new FileWriter(new File(context.getFilesDir(), fileName), true);
            out.write(fileContents);
            out.close();
            Toast.makeText(context,
                    "File Saved, data length= " + fileContents.length(),
                    Toast.LENGTH_SHORT).show();
            Log.v("data_fileContent", fileContents);
        } catch (IOException e) {
            Log.e("err_io", e.getMessage());
        }
    }

    public static String readFileAsString(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;
        try {
            in = new BufferedReader(new FileReader(new File(context.getFilesDir(), fileName)));
            while ((line = in.readLine()) != null) {
                stringBuilder.append(line);
                Log.v("data_fileLine", line);
            }
        } catch (FileNotFoundException e) {
            Log.e("err_io", e.getMessage());
        } catch (IOException e) {
            Log.e("err_io", e.getMessage());
        }

        return stringBuilder.toString();
    }

    public static String readJsonDataFromFile(Context context, String fileName){
        String data = null;
        try {

            InputStream file = context.getAssets().open("file_name.json");
            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();
            data = new String(buffer, "UTF-8");
            Toast.makeText(context, "File read", Toast.LENGTH_SHORT).show();
        } catch (IOException e){
            e.printStackTrace();
            Log.e("err_io", e.getMessage());
        }
        return data;
    }

    public void removeItemFromJsonFile(){

    }
}
