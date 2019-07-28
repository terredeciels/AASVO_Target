package main;

import Coordinate_Converter.astroj.SkyAlgorithms;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.System.arraycopy;

public class AAVSO_Target extends ETraitement {

    private double lstNow;


    public static void main(String[] args) throws IOException {
        new AAVSO_Target().traitement();
    }

    private void traitement() throws IOException {

        ArrayList<String[]> tabListInputFile = fileToTabListFilter();
        writeToFile(tabListInputFile);
        writeNamesToFile(tabListInputFile);
    }

    private ArrayList<String[]> fileToTabListFilter() throws IOException {
        ArrayList<String[]> tabList = new ArrayList<>();


        double[] utDateNow = getDateTime(day_of_month0, month0, year0,
                hours0, minutes0, seconds0, milliseconds0);
        double[] utDateNow1 = getDateTime(day_of_month1, month1, year1,
                hours1, minutes1, seconds1, milliseconds1);

        filtreAltAz = new Filtre(20, 70, 330, 30);
        filtrePeriod = new Filtre(2, 30);// 2 jours à 30 jours
        filtreMag = new Filtre(12);
        // traitement(utDateNow0, 0);
        //  traitement(utDateNow1, 1);

        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], longitude, leapSec);

        CSVReader reader = new CSVReader(new FileReader(path + inputFile + ".csv"));
        String[] nextLine ;
        reader.readNext();//pass first line
       // Ltab = nextLine.length + 2;// +alt +az
        tabOutPutFile = new String[Ltab];
        temPoutPutFile = new String[Ltab];
        //System.out.println(Ltab);
        while ((nextLine = reader.readNext()) != null) {
            boolean Valid = convert(nextLine);
            if (Valid)
                tabList.add(tabOutPutFile);
            //writer.writeNext(outPutFile);
        }
        // writer.close();

        return tabList;
    }

    private boolean convert(String[] nextLine) {
        double ra, dec;
        temPoutPutFile[0] = nextLine[0];//name

        String raS = nextLine[1];
        ra = 15 * ETraitement.sexToDec(raS);
        temPoutPutFile[1] = Double.toString(ra);// ra
        String decS = nextLine[2];
        dec = ETraitement.sexToDec(decS);
        temPoutPutFile[2] = Double.toString(dec);// dec

        temPoutPutFile[3] = nextLine[3];//Const
        temPoutPutFile[4] = nextLine[4];//VarType

        String smin = nextLine[5].split(" ")[0];//MaxMag
        String smax = nextLine[6].split(" ")[0];//MinMag
        String speriod = nextLine[7].split(" ")[0];//Period

        double dmax, dmin, dperiod;
        String Dmax, Dmin, Dperiod;

        try {
            dmax = Double.parseDouble(smax);
            Dmax = String.valueOf(dmax);
        } catch (NumberFormatException e) {
            Dmax = "?";
        }
        try {
            dmin = Double.parseDouble(smin);
            Dmin = String.valueOf(dmin);
        } catch (NumberFormatException e) {
            Dmin = "?";
        }
        try {
            dperiod = Double.parseDouble(speriod);
            Dperiod = String.valueOf(dperiod);
        } catch (NumberFormatException e) {
            Dperiod = "?";
        }

        temPoutPutFile[5] = Dmin;//MaxMag
        temPoutPutFile[6] = Dmax;//MinMag
        temPoutPutFile[7] = Dperiod;//Period

        for (int k = 8; k < Ltab-2; k++) {
            temPoutPutFile[k] = nextLine[k];
        }

        double ha = lstNow - ra;
        double[] altaz = SkyAlgorithms.EquatorialToHorizontal(ha, dec, latitude);
        double altitude = altaz[0];
        temPoutPutFile[14] = Double.toString(altitude);
        double azimuth = altaz[1];
        temPoutPutFile[15] = Double.toString(azimuth);

        return validate(altitude, azimuth, Dmin, Dperiod);
    }

    private boolean validate(double altitude, double azimuth, String minmag, String period) {
        boolean valid = filtreAltAz.exe(altitude, azimuth);
        valid = valid && filtreMag.exeMag(minmag);
        valid = valid && filtrePeriod.exeP(period);
        tabOutPutFile = new String[Ltab];
        if (valid)
            arraycopy(temPoutPutFile, 0, tabOutPutFile, 0, temPoutPutFile.length);
        return valid;
    }
}
