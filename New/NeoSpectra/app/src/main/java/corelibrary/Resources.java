package corelibrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Resources {
    public static String PathToData = "D:\\jobs\\BalitTanah\\SSK.Mobile\\SSK.Desktop\\src\\main\\java\\data\\";
    public static String GetResources(String Name) {
        String data = "";
        switch (Name) {
            case "Data":
                data = ReadFromFile(PathToData+"Data.txt");
                return data;
                //break;
            case "NPK":
                data = ReadFromFile(PathToData+"NPK.csv");
                return data;
            //break;
            case "DataRekomendasi":
                data = ReadFromFile(PathToData+"DataRekomendasi.txt");
                return data;
            //break;
            case "Lokasi":
                data = ReadFromFile(PathToData+"Lokasi.txt");
                return data;
            //break;
            default:
                return null;

        }
    }

    public static String ReadFromFile(String FileName){
        String AllText = "";
        try {

            File myObj = new File(FileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //System.out.println(data);
                AllText+=data+ System.lineSeparator();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return AllText;
    }

}
