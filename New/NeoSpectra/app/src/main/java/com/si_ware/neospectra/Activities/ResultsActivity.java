package com.si_ware.neospectra.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.si_ware.neospectra.ChartView.ChartView;
import com.si_ware.neospectra.Models.dbReading;
import com.si_ware.neospectra.R;
import android.widget.Switch;
import android.Manifest;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.ActivityCompat;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;
import static com.si_ware.neospectra.Global.GlobalVariables.OutputDirectory;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUMFILE_Y_AXIS_A;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUMFILE_Y_AXIS_R_T;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_ABS_PATH_TEMPLATE;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_DEFAULT_PATH_TEMPLATE;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_FILE_X_AXIS_CM;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_FILE_X_AXIS_NM;
import static com.si_ware.neospectra.Global.GlobalVariables.SPECTRUM_REFL_PATH_TEMPLATE;
import static com.si_ware.neospectra.Global.GlobalVariables.gAllSpectra;
import static com.si_ware.neospectra.Global.GlobalVariables.measurmentsViewCaller;
import static com.si_ware.neospectra.Global.MethodsFactory.formatString;
import static com.si_ware.neospectra.Global.MethodsFactory.showAlertMessage;
import static com.si_ware.neospectra.Global.MethodsFactory.writeGraphFile;




public class ResultsActivity extends AppCompatActivity  implements Comparator<File> {


    private Context mContext;
    private Button mClearButton;
    private Button mSaveButton;
    private Button mLoadButton;
    private GraphView mGraphView;
    private static final String TAG = "Results Activity";
    private static int colors[] = {0xFFFF0000, 0xFF0000FF, 0xFF00FF00, 0xFF00FFFF, 0xFFFF007F, 0xFF7F00FF, 0xFFFF00FF, 0xFFFFFF00, 0xFF007FFF, 0xFFFF7F00, 0xFF00FF7F, 0xFF7FFF00};
    private double maxValue = 0;
    private static int measurementCount_Spectroscopy = 0;

    public static boolean rdbtn_nm_Spec = true;
    public static boolean rdbtn_ref_Spec = true;
    private Intent intent ;
    private File[] files = null;

    private String filesName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        mContext = this;
        isWriteStoragePermissionGranted();

        mGraphView = findViewById(R.id.chart_view);

        mClearButton = findViewById(R.id.btn_clear);

        // This is a listener to clear chart
        mClearButton.setOnClickListener(v -> {
            maxValue = 0;
            if(!mGraphView.getSeries().isEmpty())
            mGraphView.removeAllSeries();
            mGraphView.getViewport().setMinY(90);
            mGraphView.getViewport().setMaxY(110);
            mGraphView.getViewport().setMinX(1100);
            mGraphView.getViewport().setMaxX(2650);
            mGraphView.getViewport().setScalable(false);
            mGraphView.getViewport().setScrollable(false);
            mGraphView.getViewport().setScalableY(false);
            mGraphView.getViewport().setScrollableY(false);
            gAllSpectra.clear();

            measurementCount_Spectroscopy = 0;

        }
        );

        mSaveButton = findViewById(R.id.btn_SaveResults);

        // Save button listener
        mSaveButton.setOnClickListener(v -> {

            android.support.v7.app.AlertDialog.Builder myAlert = new android.support.v7.app.AlertDialog
                    .Builder(mContext);
            myAlert.setTitle("Add Files name");

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText("");
            myAlert.setView(input);


            myAlert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    Boolean isRepeated = false;
                    filesName = input.getText().toString();
                    File directory = getExternalStoragePublicDirectory(OutputDirectory);
                    String [] filesInDirectory = directory.list();
                    for(int j=0; j< filesInDirectory.length; j++)
                    {
                        if(filesInDirectory[j].contains(filesName))
                        {
                            isRepeated = true;
                            break;
                        }
                    }

                    if(!isRepeated) {
                        saveResults();
                        dialogInterface.dismiss();

                        showAlertMessage(mContext,
                                "Success in saving files",
                                "Data are saved successfully in DCIM/NeoSpectra folder");
                    }
                    else
                    {// If the inserted name was saved previuslly
                        dialogInterface.dismiss();
                        android.support.v7.app.AlertDialog.Builder myAlertOk = new android.support.v7.app.AlertDialog
                                .Builder(mContext);
                        myAlertOk.setTitle("Warning");
                        final TextView text = new EditText(mContext);
                        myAlertOk.setMessage("There a file/files saved with the same name");


                        myAlertOk.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(@NonNull DialogInterface dialogInterfaceOk, int i) {

                                saveResults();
                                dialogInterfaceOk.dismiss();


                                showAlertMessage(mContext,
                                        "Success in saving files",
                                        "Data are saved successfully in DCIM/NeoSpectra folder");

                            }
                        });

                        myAlertOk.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(@NonNull DialogInterface dialogInterfaceOk, int i) {
                                dialogInterfaceOk.dismiss();

                                mSaveButton.callOnClick();
                            }
                        });

                        myAlertOk.show();
                    }
                }
            });

            myAlert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(@NonNull DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            myAlert.show();
            filesName = "";

        });

        displayGraph();


        mLoadButton =  findViewById(R.id.btn_LoadResults);

        mLoadButton.setOnClickListener(v ->{

            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);

            Uri neoSpectraURL = Uri.parse("content://com.android.externalstorage.documents/document/primary%3ADCIM%2FNeoSpectra");

            intent.setDataAndType(neoSpectraURL, "*/*");


            startActivityForResult(intent, 7);


        });

        mGraphView.getLegendRenderer().setVisible(true);
        mGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        if(maxValue == 0) {
            mGraphView.getViewport().setMinY(90);
            mGraphView.getViewport().setMaxY(110);
            mGraphView.getViewport().setMinX(1100);
            mGraphView.getViewport().setMaxX(2650);
        }
        else {
            mGraphView.getViewport().setMaxY(maxValue);
            mGraphView.getViewport().setScalable(true);
            mGraphView.getViewport().setScrollable(true);
            mGraphView.getViewport().setScalableY(true);
            mGraphView.getViewport().setScrollableY(true);
        }
        mGraphView.getViewport().setYAxisBoundsManual(true);
        mGraphView.getViewport().setXAxisBoundsManual(true);


        mGraphView.getGridLabelRenderer().setHorizontalLabelsAngle(45);
        mGraphView.getGridLabelRenderer().setHorizontalAxisTitle("nm");

        mGraphView.getGridLabelRenderer().setVerticalAxisTitle("%Refl.");
        mGraphView.getGridLabelRenderer().setVerticalLabelsAlign(Paint.Align.RIGHT);

        mGraphView.getViewport().setDrawBorder(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch(requestCode){

            case 7:
                if(resultCode==RESULT_OK){

                    // Checking whether data is null or not
                    if (data != null) {

                        // Checking for selection multiple files or single.
                        if (data.getClipData() != null){

                            files = new File[data.getClipData().getItemCount()];

                            // Getting the length of data and logging up the logs using index
                            for (int index = 0; index < data.getClipData().getItemCount(); index++) {

                                // Getting the URIs of the selected files and logging them into logcat at debug level
                                Uri uri = data.getClipData().getItemAt(index).getUri();

                                File file= new File(uri.getPath());


                                isReadStoragePermissionGranted();

                                File directory = getExternalStoragePublicDirectory(OutputDirectory);

                                if(directory == null) {
                                    showAlertMessage(this,
                                            "Error in loading files",
                                            "No data to be loaded!");

                                    return;
                                }


                                String str = directory.getAbsolutePath() + File.separator + file.getName();
                                files[index] = new File(str);




                            }



                        }else{

                            // Getting the URI of the selected file and logging into logcat at debug level
                            Uri uri = data.getData();

                            File file= new File(uri.getPath());


                            isReadStoragePermissionGranted();

                            File directory = getExternalStoragePublicDirectory(OutputDirectory);

                            if(directory == null) {
                                showAlertMessage(this,
                                        "Error in loading files",
                                        "No data to be loaded!");

                                return;
                            }

                            files = new File[1];
                            String str = directory.getAbsolutePath() + File.separator + file.getName();
                            files[0] = new File(str);


                        }

                        loadResults();
                    }

                }
                break;

        }
    }

    // Set graph series
    public void displayGraph() {
        for (int i = 0; i < gAllSpectra.size(); ++i) {
            dbReading sensorReading = gAllSpectra.get(i);
            ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

            if (sensorReading != null) {
                if ((sensorReading.getXReading().length != 0) && (sensorReading.getYReading().length != 0)) {
                    double[] xVals = sensorReading.getXReading();
                    double[] yVals = sensorReading.getYReading();

                    for (int j = xVals.length - 1; j >= 0; --j) {
                        dataPoints.add(new DataPoint(1e7 / xVals[j], yVals[j] * 100));
                        if (maxValue < yVals[j] * 100)
                            maxValue = yVals[j] * 100;
                    }

                    DataPoint dataPointsArray[] = dataPoints.toArray(new DataPoint[dataPoints.size()]);
                    LineGraphSeries<DataPoint> series = new LineGraphSeries(dataPointsArray);
                    series.setThickness(4);
                    series.setColor(colors[i % 12]);
                    series.setTitle("Meas. " + String.valueOf(i + 1));
                    mGraphView.addSeries(series);


                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }


    public void back(View view) {
        goBack();
    }


    private void goBack() {
        Intent i = new Intent(this, measurmentsViewCaller);
        i.putExtra("goto", NavDrawerActivity.GOTO_FRAGMENT_SCAN);
        startActivity(i);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static double[] switch_NM_CM(double[] data) {
        double[] xAxis = new double[data.length];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = 10000000 / data[i];
        }
        return xAxis;
    }


    public static double[] convertRefl(double[] data) {
        double[] xAxis = new double[data.length];
        for (int i = 0; i < xAxis.length; i++) {
            xAxis[i] = 10000000 / data[i];
        }
        return xAxis;
    }

    /*
     * !converts transmission data to absorbance data
     */
    public static double[] convertRefltoAbs(double[] data) {
        double[] xInverse = new double[data.length];
        for (int i = 0; i < xInverse.length; i++) {
            xInverse[i] = -(Math.log10(data[i] / 100.0));
        }
        return xInverse;
    }

    /*
     * !converts absorbance data to transmission data
     */
    public static double[] convertAbstoRefl(double[] data) {
        double[] xInverse = new double[data.length];
        for (int i = 0; i < xInverse.length; i++) {
            xInverse[i] = 100.0 * Math.pow(10.0, -data[i]);
        }
        return xInverse;
    }

    public static double[] convertDataToT(double[] data) {
        double[] TArray = new double[data.length];
        for (int i = 0; i < TArray.length; i++) {
            TArray[i] =  data[i]  * 100;
        }
        return TArray;
    }

    public static double[] convertTToData(double[] data) {
        double[] TArray = new double[data.length];
        for (int i = 0; i < TArray.length; i++) {
            TArray[i] =  data[i]  / 100;
        }
        return TArray;
    }
    // Creat output directory in case it is not found
    public void createOutDir(String dir){

        File file = new File(getExternalStoragePublicDirectory(dir).toString());
        System.out.println("************************* Path : "+dir);
        if (!file.exists())
            file.mkdir();

    }

    // Give read access permission in case it is not granted
    public  boolean isReadStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {
                // Request permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    // Give write access permission in case it is not granted
    public   boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                createOutDir(OutputDirectory);
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            createOutDir(OutputDirectory);
            return true;
        }
    }


    // Handle Read/Write request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 2:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

                    createOutDir(OutputDirectory);
                }else{

                }
                break;

            case 3:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){

                }else{
                }
                break;
        }
    }


    public void writeRunDataFile(double[] x, double[] y, int graphCount) {
        // spectrum file
        double[] xInverse = switch_NM_CM(x);
        double[] TArray = convertDataToT(y);
        double[] Absorbance = convertRefltoAbs(TArray);
        double transmittanceError = 1;//UserInterface.applicationManager.getErrorData();
        for(int i = 0; i < TArray.length; i++) {
            TArray[i] = TArray[i] / transmittanceError;
        }

        if(filesName.isEmpty()|| filesName==null)
            filesName = SPECTRUM_DEFAULT_PATH_TEMPLATE;

        if(rdbtn_nm_Spec)
        {
            // reflectance file
            if (rdbtn_ref_Spec)
            {
                if(!writeGraphFile(xInverse, TArray, OutputDirectory, filesName+ SPECTRUM_REFL_PATH_TEMPLATE + String.format("%03d",graphCount)+
                        SPECTRUM , SPECTRUM_FILE_X_AXIS_NM + "\t" + SPECTRUMFILE_Y_AXIS_R_T))
                {
                    showAlertMessage(this,
                            "Error in saving files",
                            "Sorry, don't have enough memory to store data!");
                }

            }
            // absorbance file
            else{

                if(!writeGraphFile(xInverse, Absorbance, OutputDirectory,  filesName+formatString(SPECTRUM_ABS_PATH_TEMPLATE,  graphCount), SPECTRUM_FILE_X_AXIS_NM + "\t" + SPECTRUMFILE_Y_AXIS_A))
                {
                    showAlertMessage(this,
                            "Error in saving files",
                            "Sorry, don't have enough memory to store data!");
                }
            }
        }
        else
        {
            if (rdbtn_ref_Spec) {
                // reflectance file
                if (!writeGraphFile(x, TArray, OutputDirectory, filesName+formatString(SPECTRUM_REFL_PATH_TEMPLATE,  graphCount), SPECTRUM_FILE_X_AXIS_CM + "\t" + SPECTRUMFILE_Y_AXIS_R_T)) {

                    showAlertMessage(this,
                            "Error in saving files",
                            "Sorry, don't have enough memory to store data!");


                }
                // absorbance file
            }
            else {
                if(!writeGraphFile(x, Absorbance,OutputDirectory,  filesName+formatString(SPECTRUM_ABS_PATH_TEMPLATE,  graphCount), SPECTRUM_FILE_X_AXIS_CM + "\t" + SPECTRUMFILE_Y_AXIS_A))
                {

                    showAlertMessage(this,
                            "Error in saving files",
                            "Sorry, don't have enough memory to store data!");

                }
            }
        }
        xInverse = null;
        TArray = null;

    }
    @Override
    public int compare(File f1, File f2) {
        return f1.getName().compareTo(f2.getName());
    }

    // Load saved reading from certain files to chart series
    public void loadResults()
    {

        if(files ==null)
        {
            showAlertMessage(this,
                    "Error in loading files",
                    "No data to be loaded!");
            return;
        }
        else
        {

            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return f1.getName().compareTo(f2.getName());
                }
            });
        }

        measurementCount_Spectroscopy = gAllSpectra.size();


        // Load the data from the files to the public ArrayList
        for (int i = 0; i < files.length; i++)
        {

            ArrayList<DataPoint> dataPoints = new ArrayList<DataPoint>();

            double[][] arraysToPlot=  loadGraphDataFromFile(files[i].getAbsolutePath());
            if(!rdbtn_nm_Spec)
            {
                arraysToPlot[0] = switch_NM_CM(arraysToPlot[0]);
            }
            for (int j = arraysToPlot[0].length - 1; j >= 0; --j) {
                dataPoints.add(new DataPoint(arraysToPlot[0][j], arraysToPlot[1][j]));

                if (maxValue < arraysToPlot[1][j])
                    maxValue = arraysToPlot[1][j];
            }

                dbReading newReading = new dbReading();
                newReading.setReading(convertTToData(arraysToPlot[1]), switch_NM_CM(arraysToPlot[0]));
                gAllSpectra.add(newReading);


            DataPoint dataPointsArray[] = dataPoints.toArray(new DataPoint[dataPoints.size()]);
            LineGraphSeries<DataPoint> series = new LineGraphSeries(dataPointsArray);
            series.setThickness(4);
            series.setColor(colors[measurementCount_Spectroscopy % 12]);
            series.setTitle("Meas. " + String.valueOf(measurementCount_Spectroscopy + 1 ));
            mGraphView.addSeries(series);


            measurementCount_Spectroscopy++;

        }


        // Set graph chart with the all data in the ArrayList
        mGraphView.getLegendRenderer().setVisible(true);
        mGraphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

        if(maxValue == 0) {
            mGraphView.getViewport().setMinY(90);
            mGraphView.getViewport().setMaxY(110);
            mGraphView.getViewport().setMinX(1100);
            mGraphView.getViewport().setMaxX(2650);
        }
        else {
            mGraphView.getViewport().setMaxY(maxValue);
            mGraphView.getViewport().setScalable(true);
            mGraphView.getViewport().setScrollable(true);
            mGraphView.getViewport().setScalableY(true);
            mGraphView.getViewport().setScrollableY(true);
        }
        mGraphView.getViewport().setYAxisBoundsManual(false);
        mGraphView.getViewport().setXAxisBoundsManual(false);



    }



    // Load X and Y data from file to double array
    static double[][] loadGraphDataFromFile(String strFilePath) {
        ArrayList<Double> xList = new ArrayList<Double>();
        ArrayList<Double> yList = new ArrayList<Double>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(strFilePath));

            // Read units line (do nothing with it for now)
            String line = reader.readLine();
            if (line.contains("y_Axis:Absorbance")) {

                rdbtn_ref_Spec = false;
            } else if (line.contains("y_Axis:%Transmittance") || line.contains("y_Axis:%Reflectance")) {

                rdbtn_ref_Spec = true;
            }
            if (line.contains("x_Axis:Wavenumber (cm-1)")) {

                rdbtn_nm_Spec = false;

            } else if (line.contains("x_Axis:Wavelength (nm)")) {

                rdbtn_nm_Spec = true;

            }
            while ((line = reader.readLine()) != null) {
                String[] strLineTokens = line.split("\t");

                if (strLineTokens.length == 2) {
                    xList.add(Double.parseDouble(strLineTokens[0]));
                    yList.add(Double.parseDouble(strLineTokens[1]));
                }
            }

        } catch (Exception ex) {
            return null;
        } finally {
            try {
                reader.close();
            } catch (Exception ex) {

            }
        }

        double[][] arrayToReturn = new double[2][];
        arrayToReturn[0] = new double[xList.size()];
        arrayToReturn[1] = new double[yList.size()];

        for (int y = 0; y < arrayToReturn[0].length; y++) {
            arrayToReturn[0][y] = xList.get(y);
        }

        for (int y = 0; y < arrayToReturn[1].length; y++) {
            arrayToReturn[1][y] = yList.get(y);
        }

        xList = null;
        yList = null;

        return arrayToReturn;
    }

    // Save all data in the public ArrayList to files
    public void saveResults()
    {
        if(gAllSpectra.size() == 0 || gAllSpectra == null)
        {
            showAlertMessage(this,
                    "Error in Saving files",
                    "No data to be saved!");

            return;

        }
        for (int i = 0; i < gAllSpectra.size(); i++)
        {
            dbReading sensorReading = gAllSpectra.get(i);

            if (sensorReading != null) {
                if ((sensorReading.getXReading().length != 0) && (sensorReading.getYReading().length != 0)) {
                    double[] xVals = sensorReading.getXReading();
                    double[] yVals = sensorReading.getYReading();

                    writeRunDataFile(xVals, yVals, i+1);
                }

            }
        }


    }

}
