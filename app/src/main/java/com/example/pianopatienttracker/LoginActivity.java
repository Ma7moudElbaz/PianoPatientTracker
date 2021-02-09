package com.example.pianopatienttracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.pianopatienttracker.R;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;

import org.json.JSONObject;

import static java.security.AccessController.getContext;

public class LoginActivity extends AppCompatActivity {

    /* Azure AD Variables */
    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.login_btn);

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSingleAccountApp == null) {
                    return;
                }

                mSingleAccountApp.signIn(LoginActivity.this, null, new String[]{"user.read"}, getAuthInteractiveCallback());
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
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /* Successfully called graph, process data and send to UI */
                        Log.d("Response", "Response: " + response.toString());
                        displayGraphResult(response);
                    }
                }, new Response.ErrorListener() {
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