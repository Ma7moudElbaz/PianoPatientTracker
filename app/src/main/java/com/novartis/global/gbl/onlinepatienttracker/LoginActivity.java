package com.novartis.global.gbl.onlinepatienttracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;
import com.novartis.global.gbl.onlinepatienttracker.flm.Flm_home;
import com.novartis.global.gbl.onlinepatienttracker.network.Webservice;
import com.novartis.global.gbl.onlinepatienttracker.admin_manager_regional.Admin_home;
import com.novartis.global.gbl.onlinepatienttracker.rep.Rep_home;

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

    /* Azure AD Variables */
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading....");
        dialog.setCancelable(false);


        //sso
        accessToken = "Bearer "+getIntent().getStringExtra("access_token");
        Log.e("TAG", accessToken );
        getMyData(accessToken);

        loginBtn = findViewById(R.id.login_btn);
        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_password);




        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createSingleAccountPublicClientApplication(LoginActivity.this,
                R.raw.auth_config_single_account,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        /**
                         * This test app assumes that the app is only going to support one account.
                         * This requires "account_mode" : "SINGLE" in the config json file.
                         **/
                        mSingleAccountApp = application;
                        loadAccount();
                    }

                    @Override
                    public void onError(MsalException exception) {
                        displayError(exception);
                    }
                });


        loginBtn.setOnClickListener(v -> {
//                login();
            loginSsoSdk();});
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

    public void loginSsoSdk(){


        if (mSingleAccountApp == null) {
            return;
        }
        mSingleAccountApp.signIn(LoginActivity.this, null, new String[]{"user.read"}, getAuthInteractiveCallback());


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
                        } else if (role.equals("admin")) {
                            Toast.makeText(LoginActivity.this, "Admin has no access to this app", Toast.LENGTH_SHORT).show();
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




    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                mAccount = activeAccount;
//                updateUI();
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    showToastOnSignOut();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
//                displayError(exception);
            }
        });
    }

    private void showToastOnSignOut() {
        final String signOutText = "Signed Out.";
        Toast.makeText(LoginActivity.this, signOutText, Toast.LENGTH_SHORT)
                .show();
    }

    /**
     * Display the error message
     */
    private void displayError(@NonNull final Exception exception) {

        Log.e("SSO Error", "exception.toString()", exception);
    }

    /**
     * Callback used for interactive request.
     * If succeeds we use the access token to call the Microsoft Graph.
     * Does not check cache.
     */
    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d("SSO CallBack Response", "Successfully authenticated");
                Log.d("SSO CallBack Response", "ID Token: " + authenticationResult.getAccount().getClaims().get("id_token"));

                /* Update account */
                mAccount = authenticationResult.getAccount();
//                updateUI();

                /* call graph */
                callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d("SSO Error response", "Authentication failed: " + exception.toString());
                displayError(exception);

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d("SSO Cancel response", "User cancelled login.");
            }
        };
    }

    private void callGraphAPI(final IAuthenticationResult authenticationResult) {
        MSGraphRequestWrapper.callGraphAPIUsingVolley(
                LoginActivity.this,
                "https://graph.microsoft.com/v1.0/me",
                authenticationResult.getAccessToken(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /* Successfully called graph, process data and send to UI */
                        Log.d("Response", "Response: " + response.toString());
                        displayGraphResult(response);
                    }
                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Response", "Error: " + error.toString());
                        displayError(error);
                    }
                });
    }

    /**
     * Display the graph response
     */
    private void displayGraphResult(@NonNull final JSONObject graphResponse) {
        Log.d("Graph Result", graphResponse.toString());
    }


}
