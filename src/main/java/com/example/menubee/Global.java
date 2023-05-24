package com.example.menubee;

import android.app.Application;


public class Global extends Application {
    private static boolean tip_value = true;

    public static boolean getTip_value()
    {
        return tip_value;
    }

    public static void setTip_value(boolean value)
    {
        tip_value = value;
    }
}
