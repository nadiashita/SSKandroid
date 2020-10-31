import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.prefs.Preferences;

import corelibrary.FertilizerCalculator;
import corelibrary.ModelRunner;
import corelibrary.Resources;
import corelibrary.model.FertilizerInfo;
import corelibrary.model.InferenceResult;

//import static sun.misc.Version.print;
public class Test {
    public static void main(String[] args) {
        String filename = "SSK.Mobile\\SSK.Desktop\\src\\main\\java\\data\\config.ini";
        Ini ini = null;
        try {
            ini = new Ini(new File(filename));
            Preferences prefs = new IniPreferences(ini);
            //System.out.println("grumpy/homePage: " + prefs.node("grumpy").get("homePage", null));

            String DataRekomendasi = ini.get("Config","DataRekomendasi");
            String WorkingDirectory = ini.get("Config","WorkingDirectory");
            String ModelScript = ini.get("Config","ModelScript");
            String SensorData = ini.get("Config","SensorData");
            String AnacondaFolder = ini.get("Config","AnacondaFolder");
            Resources.PathToData = ini.get("Config","PathToData");
            double ureaConst = Double.parseDouble(ini.get("Config","Urea"));
            double sp36Const = Double.parseDouble(ini.get("Config","SP36"));
            double kclConst = Double.parseDouble(ini.get("Config","KCL"));

            ModelRunner ml = new ModelRunner(WorkingDirectory, ModelScript, SensorData, AnacondaFolder);

            InferenceResult hasil = ml.InferenceModel(false, true);
            if (hasil.getIsSucceed())
            {
                try
                {

                    System.out.println("start recommendation process");
                    FertilizerCalculator calc = new FertilizerCalculator(DataRekomendasi);
                    String TxtUrea = String.valueOf(calc.GetFertilizerDoze(hasil.getModel().getCN(), "Padi", "Urea")*ureaConst);
                    String TxtSP36 = String.valueOf(calc.GetFertilizerDoze(hasil.getModel().getHCl25P2O5(), "Padi", "SP36")*sp36Const);
                    String TxtKCL = String.valueOf(calc.GetFertilizerDoze(hasil.getModel().getHCl25K2O(), "Padi", "KCL")*kclConst);
                    System.out.println(String.format("Rekomendasi KCL : %1$s, SP36 : %2$s, Urea : %3$s", TxtKCL, TxtSP36, TxtUrea));

                    FertilizerInfo x = calc.GetNPKDoze(hasil.getModel().getHCl25P2O5(), hasil.getModel().getHCl25K2O(), "Padi");

                    System.out.println(String.format("Rekomendasi NPK 15:15:15 = %1$s",x.getNPK()));
                    System.out.println(String.format("UREA 15:15:15 = %1$s",x.getUrea()));

                }
                catch (RuntimeException ex)
                {
                    System.out.println(ex);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void print(PrintStream out) {
        out.println("Hello, World!");
    }
}
