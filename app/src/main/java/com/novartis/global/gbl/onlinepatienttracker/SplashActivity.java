package com.novartis.global.gbl.onlinepatienttracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    // Splash screen timer
    int SPLASH_TIME_OUT = 500;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        navigateLogin();
        navigateLoginRootDetected();


    }

    private void navigateLoginRootDetected() {
        if (RootDetectionUtil.isDeviceRooted()) {
            showRootDetectedDialog();
        } else {
            navigateLogin();
        }
    }

    private void navigateLogin() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
//                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                Intent i = new Intent(getApplicationContext(), LoginSsoWebview.class);
//                startActivity(i);
//                finish();


//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://dev.ptracker.org/test"));

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://test.piano-tracker.net/login-sso"));
                startActivity(intent);
            }
        }, SPLASH_TIME_OUT);
    }

    private void showRootDetectedDialog() {
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
    }

    private void generateKeyHash() {
        byte[] sha1 = {
                (byte) 0x4C, (byte) 0xEC, (byte) 0x2D, (byte) 0x46, (byte) 0x6D, (byte) 0x2B, (byte) 0xEC, (byte) 0x16, (byte) 0xC3, (byte) 0x61, (byte) 0x7C, (byte) 0x85, (byte) 0xBA, (byte) 0x85, (byte) 0x54, (byte) 0x87, (byte) 0x75, (byte) 0x17, (byte) 0x09, (byte) 0xB7
        };
        Log.e("keyhash", Base64.encodeToString(sha1, Base64.NO_WRAP));

//                Generat Keyhash installed app
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                String key = Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT);
                Log.d("KeyHash:", key);

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}