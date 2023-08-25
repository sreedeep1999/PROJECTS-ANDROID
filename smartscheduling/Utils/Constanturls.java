package com.example.smartscheduling.Utils;

import android.content.Context;

public class Constanturls {
    private final String ip;
    Context mctx;

    public Constanturls(Context mctx) {
        this.mctx = mctx;
        GlobalPreference globalPreference = new GlobalPreference(mctx);
        ip = globalPreference.RetriveIP();
    }

    public String getImageUrl()
    {
        return "http://"+ip+"/SmartScheduling/admin/tbl_hotel/uploads/";
    }
}
