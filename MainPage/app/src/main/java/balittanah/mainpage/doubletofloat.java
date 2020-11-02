package balittanah.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class doubletofloat extends AppCompatActivity {

    TextView d;
    EditText f;
    Button tofloat;
    float floa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doubletofloat);
        final float[] abc = {(float) 3.8704317, (float) 3.481731, (float) 3.112639, (float) 2.7631557, (float) 2.4332812,(float) 2.1230156,(float) 1.8218051,
                (float)1.5428181,(float) 1.2832037,(float) 1.0538046,(float) 0.85943544,(float) 0.68840075,(float) 0.5289379,(float) 0.38665625,(float) 0.27332777,
                (float)0.18161932,(float) 0.08654734,(float) -0.0206862,(float) -0.108966105,(float) -0.12719381,(float) -0.0497196,(float) 0.101320304,
                (float)0.27595317,(float) 0.42437986,(float) 0.5014028,(float) 0.46307,(float) 0.28456038,(float) -0.010958054,(float) -0.35087943,(float) -0.6505429,
                (float)-0.8579766,(float) -0.966181,(float) -0.99124056,(float) -0.95187587,(float) -0.86913055,(float) -0.76952547,(float) -0.67543626,(float) -0.5922723,
                (float)-0.5098972,(float) -0.41574466,(float) -0.3033343,(float) -0.1700764,(float) -0.013439285,(float) 0.16726658,(float) 0.36836216,
                (float)0.58351094,(float) 0.8051464,(float) 1.0229396,(float) 1.2285992,(float) 1.4324409,(float) 1.6730129,(float) 1.9892191,(float) 2.3624659,
                (float)2.6959639,(float) 2.8669596,(float) 2.7966132,(float) 2.477003,(float) 1.9541258,(float) 1.3045062,(float) 0.6307705,(float) 0.05442586,
                (float)-0.32863703,(float) -0.5063816,(float) -0.5508209,(float) -0.5508058,(float) -0.55125475,(float) -0.54923165,(float) -0.5316622,(float) -0.5056818,
                (float)-0.4905323,(float) -0.4927884,(float) -0.5026369,(float) -0.511617,(float) -0.5226538,(float) -0.5397273,(float) -0.557486,(float) -0.5681975,
                (float)-0.5737165,(float) -0.5825461,(float) -0.5962707,(float) -0.60664606,(float) -0.6077426,(float) -0.6040681, (float)-0.6027949,(float) -0.6036157,
                (float)-0.60227346,(float) -0.6007115,(float) -0.60546523,(float) -0.6151927, (float)-0.6173562,(float) -0.6018677,(float) -0.5740234,
                (float)-0.54852605,(float) -0.53334844,(float) -0.52554464,(float) -0.521515,(float) -0.52359456,(float) -0.5329663,(float) -0.5419728,
                (float)-0.5402035,(float) -0.5268787,(float) -0.51201147,(float) -0.5050308,(float) -0.5069488,(float) -0.5145626,(float) -0.5267139,
                (float)-0.542692,(float) -0.5577988,(float) -0.56623465,(float) -0.56824076,(float) -0.5693699,(float) -0.57191694,(float) -0.57105494,
                (float)-0.5614464,(float) -0.5438553,(float) -0.52165323,(float) -0.49445596,(float) -0.46071154,(float) -0.42623934,(float) -0.4034071,
                (float)-0.39805487,(float) -0.40123755,(float) -0.3976369,(float) -0.3812221,(float) -0.35917893,(float) -0.34149748,(float) -0.33199087,
                (float)-0.32950383,(float) -0.3319943,(float) -0.33497623,(float) -0.32861322,(float) -0.30119225,(float) -0.24604103,(float) -0.16432618,
                (float)-0.06356148,(float) 0.042029366,(float) 0.13077156,(float) 0.18077044,(float) 0.18170333,(float) 0.13687417,(float) 0.052706983,
                (float)-0.06808441,(float) -0.21786228,(float) -0.37439826,(float) -0.51085794,(float) -0.61846566,(float) -0.71201676,(float) -0.8074793,
                (float)-0.90100205,(float) -0.97350466,(float) -1.0353855,(float) -1.0866444,(float) -1.1272817,(float) -1.157297};

        final float[] bcd = {(float)0.028378237,(float) 0.030245677,(float) 0.031419344,(float) 0.027555017,(float) 0.022902323,(float) 0.020302963, (float)0.0213796,
                (float)0.0218173,(float) 0.026278969,(float) 0.0221616,(float) 0.038160913,(float) 0.0821695,(float) 0.1232464,(float) 0.16267784,
                (float)0.19794585,(float) 0.21372886,(float) 0.22042903,(float) 0.22302136,(float) 0.21341941,(float) 0.19546969,(float) 0.17937447,
                (float)0.18448135,(float) 0.17884763,(float) 0.19281621,(float) 0.20258111,(float) 0.19732678,(float) 0.20392486,(float) 0.20044695,
                (float)0.2128191, (float)0.21248448,(float) 0.20886306,(float) 0.17595667,(float) 0.16297331,(float) 0.15385528,(float) 0.16112156,(float) 0.17114128,
                (float)0.115065575, (float)0.13479415,(float) 0.098892845,(float) 0.06299959,(float) 0.18057562,(float) 0.32248792,(float) 0.41074303,
                (float)0.48057634,(float) 0.53849775,(float) 0.5488432,(float) 0.5671945,(float) 0.5831319,(float) 0.5886776,(float) 0.59237003,
                (float)0.58985007,(float) 0.57855076,(float) 0.5578863,(float) 0.5179483,(float) 0.5353617,(float)  0.5551277,(float)  0.55228,(float)  0.5464329,
                (float) 0.5228598,(float)  0.50139326,(float)  0.4457506,(float)  0.42666402,(float)  0.38203126,(float)  0.3710118,(float)  0.3479468,(float)  0.366245,
                (float) 0.40816256,(float)  0.34967023,(float)  0.45421335,(float)  0.57049996,(float)  0.62758434,(float)  0.66224647,(float)  0.6607958,(float)  0.67534685,
                (float) -0.90100205,(float) -0.97350466,(float)  -1.0353855,(float)  -1.0866444,(float)  -1.1272817,(float)  -1.157297};
        d = findViewById(R.id.DoubleBounce);
        f = findViewById(R.id.floating);
        tofloat = findViewById(R.id.tofloat);

        final double x = 2.1960178267602544;

//        d.setText(String.valueOf(x));

        tofloat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("tipe data", x.getClass().getName());
//                floa = (float) x;
                for (int i = 0; i < 70; i++){
                    f.append("0.38203126, ");
                }
//                f.setText(String.valueOf(abc.length)+"\n"+String.valueOf(bcd.length));

            }
        });
    }
}