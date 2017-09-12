package com.spicasoft.sefpro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Toolbar toolbar=null;
    NavigationView navigationView=null;
    Fragment fragment = null;
    private static final int REQUEST_APP_PERMISSIONS = 101;
    private static final int REQUEST_GPS = 102;
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set the fragment initially
        setUpPermission();
        Login login_fragment=new Login();
        FragmentTransaction fragmentTransaction=
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,login_fragment);
        fragmentTransaction.commit();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView lnkbtnlogin=(TextView) headerView.findViewById(R.id.lnkBtnLogin);
        lnkbtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment=new Login();
                FragmentTransaction fragmentTransaction=
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.commit();
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.show();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        //String Token= AppPreferences.getInstance(this).getFcmToken();
        //Toast.makeText(this,AppPreferences.getInstance(this).getFcmToken(),Toast.LENGTH_LONG).show();
        //FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void setUpPermission() {
        if (isLollipopAbove()) {

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_APP_PERMISSIONS);
                return;
            }
        }
    }
    public static boolean isLollipopAbove() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null &&
                permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context,
                        permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_APP_PERMISSIONS) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                setUpPermission();
            }
        }
    }
    public void CloseMenu(View view){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (id == R.id.nav_Inspection) {
            if(!(fragment instanceof Inspection)) {
                fragment = new Inspection();
            }
            fab.hide();
        } else if (id == R.id.nav_Safety) {
            if(!(fragment instanceof Safety)) {
                fragment = new Safety();
            }
            fab.hide();
        } else if (id == R.id.nav_GeneralTravel) {
            if(!(fragment instanceof GeneralTravel)) {
                fragment = new GeneralTravel();
            }
            fab.hide();

        }else if (id == R.id.nav_food) {
            if(!(fragment instanceof Food_Info)) {
                fragment = new Food_Info();
            }
            fab.hide();

        }else if (id == R.id.nav_Travel_Flight) {
            if(fragment instanceof TravelAgenda)
            {
                ((TravelAgenda) fragment).scrollToItem(TravelAgendaItem.FLIGHT);
            }
            else {
                fragment = new TravelAgenda();
                Bundle bundle=new Bundle();
                bundle.putInt("Type",TravelAgendaItem.FLIGHT);
                fragment.setArguments(bundle);
            }

            fab.hide();
        }else if (id == R.id.nav_Travel_Taxi) {
            if(fragment instanceof TravelAgenda)
            {
                ((TravelAgenda) fragment).scrollToItem(TravelAgendaItem.TAXI);
            }
            else {
                fragment = new TravelAgenda();
                Bundle bundle=new Bundle();
                bundle.putInt("Type",TravelAgendaItem.TAXI);
                fragment.setArguments(bundle);
            }

            fab.hide();
        }else if (id == R.id.nav_Travel_Hotel) {
            if(fragment instanceof TravelAgenda)
            {
                ((TravelAgenda) fragment).scrollToItem(TravelAgendaItem.HOTEL);
            }
            else {
                fragment = new TravelAgenda();
                Bundle bundle=new Bundle();
                bundle.putInt("Type",TravelAgendaItem.HOTEL);
                fragment.setArguments(bundle);
            }

            fab.hide();
        }
         else if (id == R.id.nav_Twitter) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "SG_SEFPRO")));
            }catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SG_SEFPRO")));
            }

        } else if (id == R.id.nav_LinkedIn) {

        }
        else if (id == R.id.nav_YouTube) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("https://www.youtube.com/channel/UCqBq0SIlv0eMIo1FATKEQew"));
            startActivity(intent);
        }

        if(id!=R.id.nav_YouTube && id!=R.id.nav_Twitter) {
            if (fragment == null) {
                fragment = new Login();
                fab.show();
            }

            FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
