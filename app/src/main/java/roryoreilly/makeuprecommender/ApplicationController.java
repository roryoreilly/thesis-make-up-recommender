package roryoreilly.makeuprecommender;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by roryoreilly on 26/02/16.
 */
public class ApplicationController extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
    }
}
