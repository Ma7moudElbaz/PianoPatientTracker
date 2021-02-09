package com.cat.pianopatienttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {
    // Splash screen timerr
    int SPLASH_TIME_OUT = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        byte[] sha1 = {
//                (byte)0x4C,(byte) 0xEC,(byte) 0x2D, (byte) 0x46,(byte) 0x6D, (byte) 0x2B, (byte) 0xEC, (byte) 0x16, (byte) 0xC3, (byte) 0x61, (byte) 0x7C,(byte) 0x85,(byte) 0xBA,(byte) 0x85, (byte) 0x54,(byte) 0x87, (byte) 0x75, (byte) 0x17, (byte) 0x09, (byte) 0xB7
//        };
//        Log.e("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));
//
//
////                Generat Keyhash installed app
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    getPackageName(),
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
//                messageDigest.update(signature.toByteArray());
//                String key = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);
//                Log.d("KeyHash:", key);
//
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//
//        } catch (NoSuchAlgorithmException e) {
//
//        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}