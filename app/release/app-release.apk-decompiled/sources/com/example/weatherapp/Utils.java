package com.example.weatherapp;

public class Utils {
    public static String temperatureParseFahrenheit(double d) {
        return String.valueOf(((float) (d - 32.0d)) * 0.5555556f);
    }

    public static String temperatureParseKelvin(double d) {
        return String.valueOf(((float) d) - 273.0f);
    }
}
