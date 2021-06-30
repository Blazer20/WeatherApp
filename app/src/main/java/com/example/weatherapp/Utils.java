package com.example.weatherapp;

public class Utils{

    public static String temperatureParseFahrenheit(double degree) {

        float C = (float) (degree - 32) * ((float) 5 / 9);

        return String.valueOf(C);
    }

    public static String temperatureParseKelvin(double degree) {

        float C = (float) degree - 273;

        return String.valueOf(C);
    }

    public static String temperatureMadeIntString(String degree){
        int mDegree = Integer.parseInt(degree);
        String temp = String.valueOf(mDegree);
        return temp;
    }
}
