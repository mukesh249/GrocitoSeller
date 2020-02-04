package in.wingstud.grocitoseller.activity;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import in.wingstud.grocitoseller.Common.Constrants;
import in.wingstud.grocitoseller.Common.SharedPrefManager;
import in.wingstud.grocitoseller.R;
import in.wingstud.grocitoseller.util.Utils;

public class SplashActi extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        SharedPrefManager.getInstance(SplashActi.this);
        SharedPrefManager.setImagePath(Constrants.ImagePath,"https://www.grocito.com/public/admin/uploads/product");
        thread();

    }

    private void thread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    Utils.setDeviceSreenH(SplashActi.this);
                    if (SharedPrefManager.isLogin(SplashActi.this)) {

                        if (SharedPrefManager.isLogin(SplashActi.this)) {

                            Utils.startActivityWithFinish(SplashActi.this, Dashboard.class);
                        }
//
                    } else {
                        Utils.startActivityWithFinish(SplashActi.this, LoginActi.class);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

}
