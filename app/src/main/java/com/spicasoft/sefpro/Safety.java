package com.spicasoft.sefpro;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import utils.AppPreferences;
import utils.NetworkUtil;
import utils.RequestToServer;

/**
 * Created by USER on 12-09-2017.
 */

public class Safety extends Fragment {

    TextView lblSafetyinfo=null;
    TextView lblShoesize=null;
    DownloadManager dManager;
    long did;
    Button btn_Shoesize=null;
    Spinner spn_Shoe_Size=null;
    String userName=null;
    String password=null;
    String userId=null;
    ProgressDialog processDialog=null;
    // BroadcastReceiver to receive intent broadcast by DownloadManager
    private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(did);
            Cursor cursor = dManager.query(q);
            if(cursor.moveToFirst()){
                String message="";
                int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                if(status==DownloadManager.STATUS_SUCCESSFUL){
                    message="Download successful";
                    //  openDownloadedAttachment(arg0, Uri.parse(downloadLocalUri), downloadMimeType);

                }
                else if(status==DownloadManager.STATUS_FAILED){
                    message="Download failed";
                }
                Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
            }

            cursor.close();
        }
    };


    public Safety() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety, container, false);
        processDialog= new ProgressDialog(getContext());
        processDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        processDialog.setCancelable(false);
        lblSafetyinfo=(TextView) view.findViewById(R.id.lblSafetyinfo);
        lblSafetyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtil.isNetworkAvailable(getActivity())){
                    dManager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                    downloadFile(v);
                }  else{
                    Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                            .show();
                }

            }
        });

        userId=AppPreferences.getInstance(getContext()).getUserId();
        lblShoesize=(TextView) view.findViewById(R.id.lblShoesize);
        spn_Shoe_Size=(Spinner) view.findViewById(R.id.spnShoeSize) ;
        btn_Shoesize=(Button) view.findViewById(R.id.btn_Shoesize);
        if(!"".equals(userId)){

            userName=AppPreferences.getInstance(getContext()).getUserName();
            password=AppPreferences.getInstance(getContext()).getPassword();

            btn_Shoesize.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Code here executes on main thread after user presses button
                    String Size =spn_Shoe_Size.getSelectedItem().toString();
                    final AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(getContext());
                    }
                    if(Size.equals("Select")) {
                        builder.setTitle("Alert")
                                .setMessage("Please select your shoe size")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                    }
                                })
                                .show();
                    }else {
                       updateShoeSizeOnServer(Size, builder);
                    }
                }
            });

        }else{
            spn_Shoe_Size.setVisibility(View.GONE);
            btn_Shoesize.setVisibility(View.GONE);
            lblShoesize.setVisibility(View.GONE);
        }


        return view;
    }

    private void updateShoeSizeOnServer(String Size,final AlertDialog.Builder builder) {
        processDialog.show();
        processDialog.getWindow().setLayout(300,300);
        processDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        JSONObject safety = new JSONObject();
        try {
            safety.put("shoeSize",Size);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> responseListener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // parseResponse(response);
                try {

                    builder.setTitle("Updated")
                            .setMessage(response.getString("Message"))
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
                    if(processDialog.isShowing())
                        processDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener errorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(processDialog.isShowing())
                    processDialog.dismiss();
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 401) {
                    // HTTP Status Code: 401 Unauthorized
                    Toast.makeText(getContext(),
                            "Username or Password incorrect.",Toast.LENGTH_SHORT)
                            .show();
                }else{
                    Toast.makeText(getContext(),
                            error.getMessage(),Toast.LENGTH_SHORT)
                            .show();
                }
                error.printStackTrace();

            }
        };
        RequestToServer.RequestToServer(getContext(),"UpdateShoeSize",safety,responseListener,errorListener);

    }

    public void downloadFile(View view){

        String urlString="http://www.chakarov.com/powerpoints/webtech1b.ppt";
        if(!urlString.equals("")){
            try{
                // Get file name from the url
                String fileName="Sefpro_Inspection_Safety_Instructions.ppt";
                // Create Download Request object
                DownloadManager.Request request=new DownloadManager.Request(Uri.parse((urlString)));
                // Display download progress and status message in notification bar
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                // Set description to display in notification
                request.setDescription("Download "+fileName+" from "+ urlString);
                // Set title
                request.setTitle("SEFPRO Safety Instructions");
                // Set destination location for the downloaded file
                request.setDestinationUri(Uri.parse("file://"+ Environment.getExternalStorageDirectory()+"/SEFPRO/"+fileName));
                // Download the file if the Download manager is ready
                did=dManager.enqueue(request);
                Toast.makeText(getContext(),"Downloading...",Toast.LENGTH_LONG).show();
            }catch(Exception e){

            }

        }


    }

    private String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }
    public void onResume(){
        super.onResume();
        // Register the receiver to receive an intent when download complete
        IntentFilter intentFilter= new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        getContext().registerReceiver(downloadReceiver, intentFilter);
    }
    @Override
    public void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        // Unregister the receiver
        getActivity().unregisterReceiver(downloadReceiver);
    }
    private void openDownloadedAttachment(final Context context, Uri attachmentUri, final String attachmentMimeType) {
        if(attachmentUri!=null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                File file = new File(attachmentUri.getPath());
                // attachmentUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID+".provider", file);;
                attachmentUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + "com.spicasoft.user.sampleapp.fileProvider", file);
            }
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(attachmentUri, attachmentMimeType);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                getActivity().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context,"Unable to open file", Toast.LENGTH_LONG).show();
            }
        }
    }
}
