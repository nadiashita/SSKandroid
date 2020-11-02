package balittanah.mainpage.ui.scanPage;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.ybq.android.spinkit.style.Wave;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import balittanah.mainpage.Myobj;
import balittanah.mainpage.R;
import balittanah.mainpage.ui.recomendationPage.recomendationPageFragment;

public class scanPageFragment extends Fragment {

    public LineChart lineChart;
    CardView btnRefresh, btnBackground, btnScan, btnProcess;
    Spinner edtResolution, edtOptical;
    TextView tampung;
    public Myobj datareflect = new Myobj();

    String[] Resolution = {"1px", "2px", "3px", "4px"};
    String[] Optical = {"1px", "2px", "3px", "4px"};

    RequestQueue mQueue;
    LinearLayout lLoading;
    RelativeLayout lUtama;

    public boolean loading = false;

    public double x;
    public double y;

    public ArrayList<Entry> allAxis;

    public ProgressBar progressBar;

    recomendationPageFragment mrecomendationPageFragment = new recomendationPageFragment();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_scan_page, container, false);
        return root;
    }

    String URL = "https://sskapi.azurewebsites.net/api/Inference/ProcessData";


    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        final String data = "{\"Reflectance\":[3.8704317,3.481731,3.112639,2.7631557,2.4332812,2.1230156,1.8218051,1.5428181,1.2832037,1.0538046,0.85943544,0.68840075,0.5289379,0.38665625,0.27332777,0.18161932,0.08654734,-0.0206862,-0.108966105,-0.12719381,-0.0497196,0.101320304,0.27595317,0.42437986,0.5014028,0.46307,0.28456038,-0.010958054,-0.35087943,-0.6505429,-0.8579766,-0.966181,-0.99124056,-0.95187587,-0.86913055,-0.76952547,-0.67543626,-0.5922723,-0.5098972,-0.41574466,-0.3033343,-0.1700764,-0.013439285,0.16726658,0.36836216,0.58351094,0.8051464,1.0229396,1.2285992,1.4324409,1.6730129,1.9892191,2.3624659,2.6959639,2.8669596,2.7966132,2.477003,1.9541258,1.3045062,0.6307705,0.05442586,-0.32863703,-0.5063816,-0.5508209,-0.5508058,-0.55125475,-0.54923165,-0.5316622,-0.5056818,-0.4905323,-0.4927884,-0.5026369,-0.511617,-0.5226538,-0.5397273,-0.557486,-0.5681975,-0.5737165,-0.5825461,-0.5962707,-0.60664606,-0.6077426,-0.6040681,-0.6027949,-0.6036157,-0.60227346,-0.6007115,-0.60546523,-0.6151927,-0.6173562,-0.6018677,-0.5740234,-0.54852605,-0.53334844,-0.52554464,-0.521515,-0.52359456,-0.5329663,-0.5419728,-0.5402035,-0.5268787,-0.51201147,-0.5050308,-0.5069488,-0.5145626,-0.5267139,-0.542692,-0.5577988,-0.56623465,-0.56824076,-0.5693699,-0.57191694,-0.57105494,-0.5614464,-0.5438553,-0.52165323,-0.49445596,-0.46071154,-0.42623934,-0.4034071,-0.39805487,-0.40123755,-0.3976369,-0.3812221,-0.35917893,-0.34149748,-0.33199087,-0.32950383,-0.3319943,-0.33497623,-0.32861322,-0.30119225,-0.24604103,-0.16432618,-0.06356148,0.042029366,0.13077156,0.18077044,0.18170333,0.13687417,0.052706983,-0.06808441,-0.21786228,-0.37439826,-0.51085794,-0.61846566,-0.71201676,-0.8074793,-0.90100205,-0.97350466,-1.0353855,-1.0866444,-1.1272817,-1.157297]}";

        datareflect.setDataReflectance(data);

        ArrayAdapter<String> itemResolution = new ArrayAdapter<String>
                (getActivity(), R.layout.support_simple_spinner_dropdown_item, Resolution);
        ArrayAdapter<String> itemOptical = new ArrayAdapter<String>
                (getActivity(), R.layout.support_simple_spinner_dropdown_item, Optical);

        mQueue = Volley.newRequestQueue(getActivity());

        lineChart = view.findViewById(R.id.graph);
        btnRefresh = view.findViewById(R.id.btnRefresh);
        btnBackground = view.findViewById(R.id.btnBackground);
        btnScan = view.findViewById(R.id.btnScan);
        btnProcess = view.findViewById(R.id.btnProcess);
        edtResolution = view.findViewById(R.id.edtResolution);
        edtOptical = view.findViewById(R.id.edtOptical);
        progressBar = view.findViewById(R.id.spin_kit);
        tampung = view.findViewById(R.id.tampung);
        lUtama = view.findViewById(R.id.lUtama);
        lLoading = view.findViewById(R.id.lLoading);

        edtResolution.setAdapter(itemResolution);
        edtOptical.setAdapter(itemOptical);

        allAxis = new ArrayList<Entry>();

        lineChart.setDrawGridBackground(false);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setPinchZoom(true);
        XAxis xl = lineChart.getXAxis();
        xl.setAvoidFirstLastClipping(true);
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);


        progressBar.setVisibility(View.GONE);


        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fungsi refresh
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

                bottomSheetDialog.setCanceledOnTouchOutside(false);

                LinearLayout bottom_sheet_refresh = bottomSheetDialog.findViewById(R.id.bottom_sheet_refresh);
                CardView btn_yes = bottomSheetDialog.findViewById(R.id.btn_yes);
                CardView btn_cancel = bottomSheetDialog.findViewById(R.id.btn_cancel);
                bottom_sheet_refresh.setVisibility(View.VISIBLE);

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fungsi yes pada bottom sheet
                        mQueue.getCache().clear();
                        getActivity().finish();
                        startActivity(getActivity().getIntent());
                    }
                });

                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fungsi cancel bottom sheet
                        bottomSheetDialog.hide();
                    }
                });
                bottomSheetDialog.show();
            }
        });

        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fungsi scan background
                double elementvalue = 0.12312;
//                datareflect.setValue1(elementvalue);
//                Bundle bundle = new Bundle();
//                bundle.putDouble("element1", elementvalue);
//                mrecomendationPageFragment.setArguments(bundle);
////                getFragmentManager().beginTransaction().add(R.id.container, mrecomendationPageFragment).commit();
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.nav_host_fragment, mrecomendationPageFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fungsi scan
                ParsingJson();
            }
        });

        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                fungsi process
                final BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(getActivity());
                bottomSheetDialog2.setContentView(R.layout.bottom_sheet_dialog);

                bottomSheetDialog2.setCanceledOnTouchOutside(false);

                LinearLayout bottom_sheet_process = bottomSheetDialog2.findViewById(R.id.bottom_sheet_process);
                CardView btn_yes2 = bottomSheetDialog2.findViewById(R.id.btn_yes2);
                CardView btn_cancel2 = bottomSheetDialog2.findViewById(R.id.btn_cancel2);
                bottom_sheet_process.setVisibility(View.VISIBLE);

                btn_yes2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        send();
//                        String isi = "3.8704317,3.481731,3.112639,2.7631557,2.4332812,2.1230156,1.8218051,1.5428181,1.2832037,1.0538046,0.85943544,0.68840075,0.5289379,0.38665625,0.27332777,0.18161932,0.08654734,-0.0206862,-0.108966105,-0.12719381,-0.0497196,0.101320304,0.27595317,0.42437986,0.5014028,0.46307,0.28456038,-0.010958054,-0.35087943,-0.6505429,-0.8579766,-0.966181,-0.99124056,-0.95187587,-0.86913055,-0.76952547,-0.67543626,-0.5922723,-0.5098972,-0.41574466,-0.3033343,-0.1700764,-0.013439285,0.16726658,0.36836216,0.58351094,0.8051464,1.0229396,1.2285992,1.4324409,1.6730129,1.9892191,2.3624659,2.6959639,2.8669596,2.7966132,2.477003,1.9541258,1.3045062,0.6307705,0.05442586,-0.32863703,-0.5063816,-0.5508209,-0.5508058,-0.55125475,-0.54923165,-0.5316622,-0.5056818,-0.4905323,-0.4927884,-0.5026369,-0.511617,-0.5226538,-0.5397273,-0.557486,-0.5681975,-0.5737165,-0.5825461,-0.5962707,-0.60664606,-0.6077426,-0.6040681,-0.6027949,-0.6036157,-0.60227346,-0.6007115,-0.60546523,-0.6151927,-0.6173562,-0.6018677,-0.5740234,-0.54852605,-0.53334844,-0.52554464,-0.521515,-0.52359456,-0.5329663,-0.5419728,-0.5402035,-0.5268787,-0.51201147,-0.5050308,-0.5069488,-0.5145626,-0.5267139,-0.542692,-0.5577988,-0.56623465,-0.56824076,-0.5693699,-0.57191694,-0.57105494,-0.5614464,-0.5438553,-0.52165323,-0.49445596,-0.46071154,-0.42623934,-0.4034071,-0.39805487,-0.40123755,-0.3976369,-0.3812221,-0.35917893,-0.34149748,-0.33199087,-0.32950383,-0.3319943,-0.33497623,-0.32861322,-0.30119225,-0.24604103,-0.16432618,-0.06356148,0.042029366,0.13077156,0.18077044,0.18170333,0.13687417,0.052706983,-0.06808441,-0.21786228,-0.37439826,-0.51085794,-0.61846566,-0.71201676,-0.8074793,-0.90100205,-0.97350466,-1.0353855,-1.0866444,-1.1272817,-1.157297";
//
//                        String data = "{\"Reflectance\":[";
//                        data += isi;
////                        for (int i = 0; i < b.length; i++){
////                            if (i != 0) // if it isn't the first one
////                                data += ", "; // insert a separator
////                            else
////                                data += a[i]; // append the index's toString
////                        }
//                        data += "]}";
//                        send(data);
//                        FrameLayout listFragment;
//                        listFragment = view.findViewById(R.id.nav_host_fragment);
//                        FragmentManager fragmentManager = getFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        dataPageFragment mdataPageFragment = new dataPageFragment();
//                        fragmentTransaction.replace(R.id.nav_host_fragment, mdataPageFragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
                        bottomSheetDialog2.hide();
                    }
                });

                btn_cancel2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        fungsi cancel bottom sheet
                        progressBar.setVisibility(View.GONE);
                        bottomSheetDialog2.hide();
                    }
                });
                bottomSheetDialog2.show();
            }
        });
    }

    public void ParsingJson() {
        loading = true;
        if (loading) {
            Wave rotatingCircle = new Wave();
            progressBar.setVisibility(View.VISIBLE);
            lLoading.setVisibility(View.VISIBLE);
            lUtama.setVisibility(View.GONE);
            getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            progressBar.setIndeterminateDrawable(rotatingCircle);
        }


        String BASE_URL = "http://192.168.43.125/ssk/index.php"; //dari local host
        String URL2 = "https://graphdataandro.000webhostapp.com/"; //dari hosting

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONArray jsonArray = response.getJSONArray("absorbance");
                            String arrayR = "{\"Reflectance\":" + jsonArray + "}";
//                            Toast.makeText(getActivity(), arrayR, Toast.LENGTH_LONG).show();
//
//                            datareflect.dataReflectance = arrayR;
                            JSONArray jsonArray2 = response.getJSONArray("wavenumber");

                            for (int i = 0; i < jsonArray.length(); i++) {


                                y = jsonArray.getDouble(i);
                                x = jsonArray2.getDouble(i);

                                Log.d("absorbance :", String.valueOf(y));
                                Log.d("wavenumber :", String.valueOf(x));

                                allAxis.add(new Entry((float) x, (float) y));
                                LineDataSet dataSet = new LineDataSet(allAxis, "Absorbance v/s wavelength graph");
                                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                                dataSets.add(dataSet);
                                LineData data = new LineData(dataSets);
                                lineChart.setData(data);
                                lineChart.animateY(5000);
                                lineChart.getDescription().setEnabled(false);
                                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        lineDataSet1.setMode(LineDataSet.Mode.CUBIC_BEZIER);  //curve line
                                lineChart.invalidate();


                                btnBackground.setEnabled(false);
                                btnBackground.setCardBackgroundColor(Color.parseColor("#FF0000"));
                                btnScan.setEnabled(false);
                                btnScan.setCardBackgroundColor(Color.parseColor("#FF0000"));
                                btnProcess.setCardBackgroundColor(Color.parseColor("#3B7FF4"));
                                progressBar.setVisibility(View.GONE);
                                lUtama.setVisibility(View.VISIBLE);
                                lLoading.setVisibility(View.GONE);
                                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            lUtama.setVisibility(View.VISIBLE);
                            lLoading.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressBar.setVisibility(View.GONE);
                lUtama.setVisibility(View.VISIBLE);
                lLoading.setVisibility(View.GONE);
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    public void send() {
        Wave rotatingCircle = new Wave();
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminateDrawable(rotatingCircle);
        lUtama.setVisibility(View.GONE);
        lLoading.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        final String reflect = datareflect.getDataReflectance();
        String URL = "https://sskapi.azurewebsites.net/api/Inference/ProcessData";
        mQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            Log.e("jumlah array:", String.valueOf(jsonArray.length()));
//                            double[][] arrayOfArrays = new double[jsonArray.length()][];

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                String elementName = jsonObject1.getString("elementName");
                                double elementValue = jsonObject1.getDouble("elementValue");

                                if (i == 0) {
                                    Bundle bundle = new Bundle();
                                    datareflect.setValue1(elementValue);
                                    bundle.putDouble("element1", datareflect.getValue1());
                                    mrecomendationPageFragment.setArguments(bundle);
                                }
                                if (i == 1) {
                                    Bundle bundle = new Bundle();
                                    datareflect.setValue2(elementValue);
                                    bundle.putDouble("element1", datareflect.getValue2());
                                    mrecomendationPageFragment.setArguments(bundle);
                                }

                                Toast.makeText(getActivity(), "RESPONSE:" + elementName + " = " + elementValue, Toast.LENGTH_SHORT).show();
                                Log.e("response:", String.valueOf(elementName + " = " + elementValue));
                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.nav_host_fragment, mrecomendationPageFragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }
                            lUtama.setVisibility(View.VISIBLE);
                            lLoading.setVisibility(View.GONE);
                            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return reflect == null ? null : reflect.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    return null;
                }
            }
        };
        mQueue.add(stringRequest);
    }

    public double getA() {
        double a = datareflect.getValue1();
        return a;
    }
}