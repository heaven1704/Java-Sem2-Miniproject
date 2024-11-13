package com.example.utils;

public class DistanceCalculator {


    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Haversine formula
        double nlat = lat2Rad - lat1Rad;
        double nlon = lon2Rad - lon1Rad;

        double a = Math.sin(nlat / 2) * Math.sin(nlat / 2) +
                   Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                   Math.sin(nlon / 2) * Math.sin(nlon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));


        return EARTH_RADIUS_KM * c;
    }
    public static void main(String[] args) {
        // Mumbai
        double lat1 = 19.0760;
        double lon1 = 72.8777;
        
        //  Pune
        double lat2 = 18.5204;
        double lon2 = 73.8567;

        double distance = calculateDistance(lat1, lon1, lat2, lon2);

        System.out.printf("Distance between Mumbai and Pune: %.2f km%n", distance);
    }
}
