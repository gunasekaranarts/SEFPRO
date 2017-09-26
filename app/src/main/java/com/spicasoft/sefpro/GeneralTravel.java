package com.spicasoft.sefpro;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import utils.NetworkUtil;

/**
 * Created by USER on 12-09-2017.
 */

public class GeneralTravel extends Fragment {
    public static final String[] Names = new String[] { "Rajaram",
            "Sandeep" };
    public static final String[] Emails = new String[] {
            "rajaramk@sefpro.com",
            "sandeeps@sefpro.com" };
    public static final String[] Mobiles = new String[] {
            "+919500000000",
            "+919500000001" };
    public static final Integer[] images = { R.drawable.ic_contact_info,
            R.drawable.ic_contact_info };
    MapView mapView;
    GoogleMap googleMap;
    int locationCount = 0;
    TextView lblweather=null;
    ListView listView;
    List<RowItem> rowItems;
    public GeneralTravel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_general_travel, container, false);
        listView = (ListView) view.findViewById(R.id.lstContact);
        setAdapter();
        lblweather=(TextView) view.findViewById(R.id.lblweather);
        lblweather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NetworkUtil.isNetworkAvailable(getContext())){
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.accuweather.com/en/in/kozhikode/188806/weather-forecast/188806"));
                startActivity(intent);
                }
                else{
                    Toast.makeText(getContext(), getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                            .show();
                }
            }
        });
        return view;
    }

    private void setAdapter() {
        rowItems = new ArrayList<RowItem>();
        for (int i = 0; i < Names.length; i++) {
            RowItem item = new RowItem(images[i], Names[i], Emails[i],Mobiles[i]);
            rowItems.add(item);
        }
        CustomContactAdapter adapter = new CustomContactAdapter(getActivity(), rowItems);
        listView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Map view
        mapView = (MapView) view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        if (mapView != null) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    try {
                        // Customise the styling of the base map using a JSON object defined
                        // in a raw resource file.
                        boolean success = googleMap.setMapStyle(
                                MapStyleOptions.loadRawResourceStyle(
                                        getContext(), R.raw.style_json));

                        if (!success) {

                        }
                    } catch (Resources.NotFoundException e) {

                    }

                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        new MainActivity().setUpPermission();
                        return;
                    }
                    googleMap.setMyLocationEnabled(true);
                    googleMap.getUiSettings().setCompassEnabled(true);
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    LatLng Kanjikode = new LatLng(10.7921007, 76.7411948);
                    googleMap.addMarker(new MarkerOptions().position(Kanjikode)
                            .title("SEFPRO India – Palakkad")
                            .snippet("Kanjikode West, Palakkad, Kerala, India, 678623")
                    );
                    LatLng Perundurai = new LatLng(11.2554205,77.5648672);
                    googleMap.addMarker(new MarkerOptions().position(Perundurai)
                            .title("SEFPRO India – Perundurai")
                            .snippet("SEZ UNIT, SIPCOT Industrial Growth Centre Perundurai, Erode Dist - 638 052, Tamilnadu, India")
                    );

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Kanjikode,7));
//                    CameraPosition cameraPosition = new CameraPosition.Builder()
//                            .target(Kanjikode)      // Sets the center of the map to Mountain View
//                            .zoom(17)                   // Sets the zoom
//                            .bearing(90)                // Sets the orientation of the camera to east
//                            .tilt(30)                   // Sets the tilt of the camera to 30 degrees
//                            .build();                   // Creates a CameraPosition from the builder
//                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    mapView.onResume();
                }
            });
//
        }
    }
}
