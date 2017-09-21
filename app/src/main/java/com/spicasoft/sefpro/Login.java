package com.spicasoft.sefpro;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import utils.AppPreferences;
import utils.FcmTokenUpdate;
import utils.NetworkUtil;
import utils.RequestToServer;


/**
 * Created by USER on 12-09-2017.
 */

public class Login extends Fragment {

    Button btn_Login=null;
    TextView NavtxtUserId=null;
    TextView NavtxtUserName=null;
    TextView NavlnkBtnLogin=null;
    NavigationView navigationView=null;
    TextView txt_UserName=null;
    TextView txt_Password=null;
    ProgressDialog processDialog=null;

    Fragment fragment = null;
    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_login, container, false);
        btn_Login=(Button) view.findViewById(R.id.btn_Login);
        processDialog= new ProgressDialog(getContext());
        processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processDialog.setCancelable(false);
        btn_Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                if (NetworkUtil.isNetworkAvailable(getContext())) {

                    InputMethodManager inputManager = (InputMethodManager)
                            getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    txt_UserName = (TextView) view.findViewById(R.id.txt_UserName);
                    txt_Password = (TextView) view.findViewById(R.id.txt_Password);


                    if (TextUtils.isEmpty(txt_UserName.getText()) || TextUtils.isEmpty(txt_Password.getText())) {
                        AlertDialog.Builder builder;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                        } else {
                            builder = new AlertDialog.Builder(getContext());
                        }

                        builder.setTitle("Alert")
                                .setMessage("Please enter username and password")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .show();

                    } else {
                        processDialog.show();
                        processDialog.getWindow().setLayout(300,300);
                        processDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                        final String userName = txt_UserName.getText().toString();
                        final String password = txt_Password.getText().toString();

//http://10.0.2.2:55941/api/sefpro/Login" http://ip.jsontest.com/
                        //Login Response Listener
                        Response.Listener<JSONObject> responseListener= new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // parseResponse(response);
                                View headerView = navigationView.getHeaderView(0);
                                try {
                                    AppPreferences.getInstance(getContext()).setAll(
                                            response.getString("User_Name").toString()
                                            ,userName
                                            ,password
                                            ,response.getString("First_Name").toString()
                                                    +" "+response.getString("Last_Name").toString()
                                    );
//                                  After login success Code to update FCM Token to user at server * * * Start * * *
                                    FcmTokenUpdate.updateTokenToServer(getContext());

                                    NavtxtUserId = (TextView) headerView.findViewById(R.id.txtUserId);
                                    NavtxtUserId.setText(response.getString("User_Name").toString());
                                    NavtxtUserName = (TextView) headerView.findViewById(R.id.txt_UserName);
                                    NavtxtUserName.setText(AppPreferences.getInstance(getContext()).getDisplayName());
                                    FirebaseMessaging.getInstance().subscribeToTopic("registered_news");
                                    FirebaseMessaging.getInstance().unsubscribeFromTopic("unregistered_news");
                                    if ( processDialog.isShowing())
                                        processDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                NavlnkBtnLogin = (TextView) headerView.findViewById(R.id.lnkBtnLogin);
                                NavlnkBtnLogin.setText("Logout");
                                fragment = new Safety();
                                FragmentTransaction fragmentTransaction =
                                        getActivity().getSupportFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.fragment_container, fragment);
                                fragmentTransaction.commit();
                            }
                        };
                        Response.ErrorListener errorListener=new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if ( processDialog.isShowing())
                                    processDialog.dismiss();
                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null && networkResponse.statusCode == 401) {
                                    // HTTP Status Code: 401 Unauthorized
                                    Toast.makeText(getContext(),
                                            "Username or Password incorrect.",Toast.LENGTH_SHORT)
                                            .show();
                                }else{
                                    Toast.makeText(getContext(),
                                            "Something went wrong.",Toast.LENGTH_SHORT)
                                            .show();
                                }
                                error.printStackTrace();

                            }
                        };
                        RequestToServer.RequestToLogin(getContext()
                                ,"Login",
                                userName,password,null,responseListener,errorListener);
                    }
                }
                else{
                    Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        return view;
    }


}