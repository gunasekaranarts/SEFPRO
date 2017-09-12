package com.spicasoft.sefpro;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

/**
 * Created by USER on 12-09-2017.
 */

public class Safety extends Fragment {

    TextView lblSafetyinfo=null;
    DownloadManager dManager;
    long did;
    Button btn_Shoesize=null;
    Spinner spn_Shoe_Size=null;
    public Safety() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_safety, container, false);

        lblSafetyinfo=(TextView) view.findViewById(R.id.lblSafetyinfo);
        lblSafetyinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dManager=(DownloadManager)getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                downloadFile(v);

            }
        });
        spn_Shoe_Size=(Spinner) view.findViewById(R.id.spnShoeSize) ;


        btn_Shoesize=(Button) view.findViewById(R.id.btn_Shoesize);
        btn_Shoesize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                String Size =spn_Shoe_Size.getSelectedItem().toString();
                AlertDialog.Builder builder;
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
                    builder.setTitle("Updated")
                            .setMessage("Your shoe size updated with us!")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                            .show();
                }
            }
        });


        return view;
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
