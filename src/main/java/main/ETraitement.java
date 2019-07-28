package main;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

class ETraitement {
    final int Ltab=16;
    final String inputFile = "index";
    final String outPutFile = "aavso_target_filtre";
    final String nameOutPutFile = "aavso_target_filtre_name";
    final String path = "D:\\Astronomie\\";
    final double latitude = 49.0;
    final double longitude = 1.887;
    final double day_of_month0 = 22;
    final double day_of_month1 = 22;
    final double month0 = 7;
    final double month1 = 7;
    final double year0 = 2019;
    final double year1 = 2019;
    final int hours0 = 21;
    final int hours1 = 23;
    final int minutes0 = 0;
    final int minutes1 = 30;
    final int seconds0 = 0;
    final int seconds1 = 0;
    final int milliseconds0 = 0;
    final int milliseconds1 = 0;
    Filtre filtreAltAz;
    Filtre filtrePeriod;
    Filtre filtreMag;
    String[] tabOutPutFile;
    String[] temPoutPutFile;
    double leapSec = 0;// ?

    static double sexToDec(String text) {
        double X = Double.NaN;
        boolean XNegative = false;
        String[] pieces = text.replace("-", " -").replaceAll("[^0-9\\.\\-]{1,}", " ").trim().split("[^0-9\\.\\-]{1,}");
        if (pieces.length > 0) {
            X = parseDouble(pieces[0]);
            if (!Double.isNaN(X) && pieces[0].contains("-")) {
                X = -X;
                XNegative = true;
            }
            if (pieces.length > 1) {
                X += Math.abs(parseDouble(pieces[1])) / 60.0;
            }
            if (pieces.length > 2) {
                X += Math.abs(parseDouble(pieces[2])) / 3600.0;
            }
        }

        if (XNegative) {
            X = -X;
        }
        return X;
    }

    private static double parseDouble(String s) {
        return parseDouble(s, Double.NaN);

    }

    private static double parseDouble(String s, double defaultValue) {
        if (s == null) {
            return defaultValue;
        }
        try {
            defaultValue = Double.parseDouble(s);
        } catch (NumberFormatException ignored) {
        }
        return defaultValue;
    }

    double[] getDateTime(double DAY_OF_MONTH, double MONTH, double YEAR,
                         int hours, int minutes, int seconds, int milliseconds) {
        // UTC
        double[] utdate = {0, 0, 0, 0};
        double ut;

        utdate[0] = YEAR;
        utdate[1] = MONTH;
        utdate[2] = DAY_OF_MONTH;

        ut = ((double) milliseconds) / 3600000.
                + ((double) seconds) / 3600.
                + ((double) minutes) / 60.
                + ((double) hours);
        utdate[3] = ut;
        return utdate;
    }

    /* void print(ArrayList<String[]> LTinData) {
        StringBuilder line = new StringBuilder();
        for (String[] tab : LTinData) {
            for (String s : tab) {
                line.append(s).append(";");
            }
            System.out.println(line);
            line = new StringBuilder();
        }

    }*/

    private void firstLine(CSVWriter writer) {
       // Star Name","RA (J2000.0)","Dec (J2000.0)","Constellation","Var. Type","Min Mag","Max Mag","Period (d)",
        // "Obs. Cadence (d)","Obs. Section","Filter/Mode","Last observed","High Priority","Notes"
        tabOutPutFile[0] = "Star Name";
        tabOutPutFile[1] = "RA (J2000.0)";
        tabOutPutFile[2] = "Dec (J2000.0)";
        tabOutPutFile[3] = "Constellation";
        tabOutPutFile[4] = "Var. Type";
        tabOutPutFile[5] = "Min Mag";
        tabOutPutFile[6] = "Max Mag";
        tabOutPutFile[7] = "Period (d)";
        tabOutPutFile[8] = "Obs. Cadence (d)";
        tabOutPutFile[9] = "Obs. Section";
        tabOutPutFile[10] = "Filter/Mode";
        tabOutPutFile[11] = "Last observed";
        tabOutPutFile[12] = "High Priority";
        tabOutPutFile[13] = "Notes";

        tabOutPutFile[14] = "alt";
        tabOutPutFile[15] = "az";
        writer.writeNext(tabOutPutFile);
    }

    void writeNamesToFile(ArrayList<String[]> tabList) throws IOException {
        ArrayList<String> list = new ArrayList<>();
        for (String[] tab : tabList) {
            list.add(tab[0]);
        }

        FileWriter writer = new FileWriter(path + nameOutPutFile + ".txt");
        writer.write("AAVSO Target");
        writer.write("\r\n");
        for (String s : list) {
            writer.write(s);
            writer.write("\r\n");
        }
        writer.close();
    }

    void writeToFile(ArrayList<String[]> tabList) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path + outPutFile + ".csv"), ',');
        firstLine(writer);
        for (String[] tab : tabList) {
            writer.writeNext(tab);
        }
        writer.close();
    }
}
