package com.example.cabinetmedical.Authh;

import android.content.Context;

import com.example.cabinetmedical.Entity.user;
import com.example.cabinetmedical.dao.AppDatabase;

public class Authh {

    public static user login(Context context, String email, String password) {
        return AppDatabase.getInstance(context).userDao().login(email, password);
    }
}