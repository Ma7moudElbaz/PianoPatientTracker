package com.cat.pianopatienttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.cat.pianopatienttracker.login.LoginActivity;
import com.cat.pianopatienttracker.login.LoginSsoWebview;

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    int SPLASH_TIME_OUT = 600;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // This method will be executed once the timer is over
//                // Start your app main activity
////                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                Intent i = new Intent(getApplicationContext(), LoginSsoWebview.class);
//                startActivity(i);
//
//                // close this activity
//                finish();
//            }
//        }, SPLASH_TIME_OUT);

        if (RootDetectionUtil.isDeviceRooted()) {
            new AlertDialog.Builder(this)
                    .setTitle("Root Alert")
                    .setMessage("You Device Is Rooted You Can't access to our app")
                    .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            finishAffinity();
                        }
                    })
                    .setCancelable(true)
                    .show();
        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                    Intent i = new Intent(getApplicationContext(), LoginSsoWebview.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }, SPLASH_TIME_OUT);

        }

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
    }
}