package fr.caranouga.expeditech.utils;

public class ColorUtils {
    public static int getColor(String hex){
        return Integer.parseInt(hex.replace("#", ""), 16);
    }

    public static int getColor(int r, int g, int b){
        return (r << 16) | (g << 8) | b;
    }
}
