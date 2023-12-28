package com.example.restoran;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class KeepLogged {
    Context context;

    public KeepLogged(Context context) {
        this.context = context;
    }

    public void kepUserLoged() {
        SharedPreferences.Editor editor = context.getSharedPreferences("Loged",MODE_PRIVATE).edit();
        editor.putBoolean("isloged",true);
        editor.apply();
    }
}
