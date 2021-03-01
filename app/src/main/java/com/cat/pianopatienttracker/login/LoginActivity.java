package com.cat.pianopatienttracker.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cat.pianopatienttracker.R;
import com.cat.pianopatienttracker.flm.Flm_home;
import com.cat.pianopatienttracker.network.Webservice;
import com.cat.pianopatienttracker.admin_manager_regional.Admin_home;
import com.cat.pianopatienttracker.rep.Rep_home;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;

    EditText email, password;
    private ProgressDialog dialog;
    String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);

        accessToken = getIntent().getStringExtra("accessToken");
        getMyData(accessToken);

        loginBtn = findViewById(R.id.login_btn);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    public void login() {
        Map<String, String> map = new HashMap<>();
        final String emailtxt = email.getText().toString();
        final String passwordtxt = password.getText().toString();

        if (emailtxt.length() == 0 || passwordtxt.length() == 0) {
            Toast.makeText(getBaseContext(), "Please Fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            map.put("email", emailtxt);
            map.put("password", passwordtxt);
            dialog.show();
            Webservice.getInstance().getApi().login(map).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.code() == 200) {
                            JSONObject res = new JSONObject(response.body().string());
                            String accessToken = "Bearer " + res.getString("access_token");

                            JSONObject userData = res.getJSONObject("data").getJSONObject("original").getJSONObject("data");
                            String userName = userData.getString("name");
                            int userId = userData.getInt("id");
                            JSONArray roleArr = userData.getJSONArray("role");
                            String role = roleArr.getJSONObject(0).getString("name");
                            String roleName = roleArr.getJSONObject(0).getString("display_name");


                            if (role.equals("manager") || role.equals("regional")) {
                                Intent i = new Intent(getBaseContext(), Admin_home.class);
                                i.putExtra("accessToken", accessToken);
                                i.putExtra("role", role);
                                i.putExtra("roleName", roleName);
                                i.putExtra("userName", userName);
                                startActivity(i);
                                finish();
                            } else if (role.equals("rep")) {
                                Intent i = new Intent(getBaseContext(), Rep_home.class);
                                i.putExtra("accessToken", accessToken);
                                i.putExtra("role", role);
                                i.putExtra("roleName", roleName);
                                i.putExtra("userName", userName);
                                i.putExtra("userId", userId);
                                startActivity(i);
                                finish();
                            } else if (role.equals("flm")) {
                                Intent i = new Intent(getBaseContext(), Flm_home.class);
                                i.putExtra("accessToken", accessToken);
                                i.putExtra("role", role);
                                i.putExtra("roleName", roleName);
                                i.putExtra("userName", userName);
                                startActivity(i);
                                finish();
                            }


                        } else {
                            Toast.makeText(LoginActivity.this, response.errorBody().string(), Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getBaseContext(), "Network Error", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });
        }
    }

    public void getMyData(String accessToken) {
        dialog.show();
        Webservice.getInstance().getApi().getLoginData(accessToken).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    JSONObject responseObject = null;
                    try {
                        JSONObject res = new JSONObject(response.body().string());

                        JSONObject userData = res.getJSONObject("data");
                        String userName = userData.getString("name");
                        int userId = userData.getInt("id");
                        JSONArray roleArr = userData.getJSONArray("role");
                        String role = roleArr.getJSONObject(0).getString("name");
                        String roleName = roleArr.getJSONObject(0).getString("display_name");


                        if (role.equals("manager") || role.equals("regional")) {
                            Intent i = new Intent(getBaseContext(), Admin_home.class);
                            i.putExtra("accessToken", accessToken);
                            i.putExtra("role", role);
                            i.putExtra("roleName", roleName);
                            i.putExtra("userName", userName);
                            startActivity(i);
                            finish();
                        } else if (role.equals("rep")) {
                            Intent i = new Intent(getBaseContext(), Rep_home.class);
                            i.putExtra("accessToken", accessToken);
                            i.putExtra("role", role);
                            i.putExtra("roleName", roleName);
                            i.putExtra("userName", userName);
                            i.putExtra("userId", userId);
                            startActivity(i);
                            finish();
                        } else if (role.equals("flm")) {
                            Intent i = new Intent(getBaseContext(), Flm_home.class);
                            i.putExtra("accessToken", accessToken);
                            i.putExtra("role", role);
                            i.putExtra("roleName", roleName);
                            i.putExtra("userName", userName);
                            startActivity(i);
                            finish();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response.code() == 401) {
                    Intent i = new Intent(getBaseContext(), LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getBaseContext(), "Network error", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}