package main;

class Filtre {

    private double min;
    private double max;
    private double altmax;
    private double altmin;
    private double azmax;
    private double azmin;

    Filtre(double altmin, double altmax, double azmin, double azmax) {
        this.altmin = altmin;
        this.altmax = altmax;
        this.azmin = azmin;
        this.azmax = azmax;
    }

    Filtre(double min, double max) {
        this.min = min;
        this.max = max;

    }

    Filtre(double min) {
        this.min = min;

    }

    boolean exe(double altitude, double azimuth) {
        //new Filtre(20, 70, 330, 30);
        boolean v = altitude < altmax && altitude > altmin;
        v = v && (azimuth < azmax && azimuth >= 0 || azimuth > azmin && azimuth <= 360);
        return v;
    }

    boolean exeMag(String val) {
        try {
            double dmin = Double.parseDouble(val);
            return dmin < min;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    boolean exeP(String val) {
        try {
            double dperiod = Double.parseDouble(val);
            return (dperiod > min && dperiod < max);
        } catch (NumberFormatException e) {
            return false;
        }

    }

//    boolean exe(double val) {
//        return !(val>min && val <max);
//    }

}
