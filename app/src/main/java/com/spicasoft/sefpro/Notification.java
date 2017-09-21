package com.spicasoft.sefpro;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.NetworkUtil;
import utils.RequestToServer;

/**
 * Created by USER on 13-09-2017.
 */

public class Notification extends Fragment {
    ListView listView;
    List<NotificationItem> notificationItems;
    private CustomNotificationAdapter adapter;
    ProgressDialog processDialog;
    public Notification() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        processDialog= new ProgressDialog(getContext());
        processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processDialog.setCancelable(false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.lstNotifications);
        setAdapter();
        if(!NetworkUtil.isNetworkAvailable(getActivity())){
            Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                    .show();
        }else {
            processDialog.show();
            processDialog.getWindow().setLayout(300,300);
            processDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            fetchNotifications();
        }
    }

    private void fetchNotifications() {
        Response.Listener<JSONArray> responseListener = new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // parseResponse(response);
                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject jresponse = response.getJSONObject(i);
                        NotificationItem item = new NotificationItem(
                                jresponse.getString("Notification_Title"),
                                jresponse.getString("Message"),
                                jresponse.getString("image"),
                                jresponse.getString("CreatedOn_Date")
                        );
                        notificationItems.add(item);
                        ;
                    }

                    adapter.notifyDataSetChanged();
                    processDialog.dismiss();
                    // progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                if (progressBar != null)
//                    progressBar.setVisibility(View.GONE);
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 401) {
                    // HTTP Status Code: 401 Unauthorized
                    Toast.makeText(getContext(),
                            "Username or Password incorrect.", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Toast.makeText(getContext(),
                            error.getMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
                error.printStackTrace();

            }
        };
        RequestToServer.RequestToServer(getContext(), "GetGeneralNotification", responseListener, errorListener);
    }

    private void setAdapter() {
        notificationItems = new ArrayList<>();
        adapter = new CustomNotificationAdapter(getActivity(), notificationItems);
        listView.setAdapter(adapter);
    }
}
