package fr.caranouga.expeditech.client;

public class ClientState {
    private static boolean showExpBar = false;

    public static boolean isShowExpBar() {
        return showExpBar;
    }

    public static void toggleShowExpBar() {
        showExpBar = !showExpBar;
    }
}
