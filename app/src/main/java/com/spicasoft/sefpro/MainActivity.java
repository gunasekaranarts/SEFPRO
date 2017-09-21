package com.spicasoft.sefpro;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import utils.AppPreferences;
import utils.FcmTokenUpdate;
import utils.NetworkUtil;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int REQUEST_APP_PERMISSIONS = 101;
    private static final int REQUEST_GPS = 102;
    private static String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET
    };
    Toolbar toolbar=null;
    NavigationView navigationView=null;
    boolean doubleBackToExitPressedOnce = false;
    Fragment fragment = null;
    TextView txtUserId=null;
    TextView txtUserName=null;
    TextView lnkBtnLogin=null;
    String UserId=null;
    private int hot_number = 0;
    private TextView ui_hot = null;

//    To Check OS version for request permission
    public static boolean isLollipopAbove() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

//    It checks whether app having specific permission or not
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpPermission();
        setContentView(R.layout.activity_main);
//       Show Tool bar and disable title instead of title we showing logo
         toolbar = (Toolbar) findViewById(R.id.toolbar);
//        AppPreferences.getInstance(this).setUserId("10234");
//        AppPreferences.getInstance(this).setUserName("gunasekaran.arts");
//        AppPreferences.getInstance(this).setPassword("Guna@9500");
//        AppPreferences.getInstance(this)
//                .setDisplayName("Gunasekaran K");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        UserId = AppPreferences.getInstance(this).getUserId();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final View headerView = navigationView.getHeaderView(0);

            //        Reads extra data from intent
            Intent i = getIntent();
            Bundle extras = i.getExtras();
            if(extras != null) {
                //            To detect whether app is opened from launcher or from Notification
                //            if it opened from notification it will directly go to notification fragment
                String notification_count = extras.getString("notification_count");
                if (notification_count != null) {
                    try {
                        updateHotCount(Integer.parseInt(notification_count));
                    } catch (NumberFormatException nfe) {
                        // Handle parse error.
                    }
                    fragment = new Notification();

                }
            }
        if("".equals(UserId)){
            if(fragment==null)
                fragment=new Login();
            FirebaseMessaging.getInstance().subscribeToTopic("unregistered_news");
            FirebaseMessaging.getInstance().unsubscribeFromTopic("registered_news");
        } else{
            if(fragment==null)
            fragment=new Safety();
            lnkBtnLogin= (TextView) headerView.findViewById(R.id.lnkBtnLogin);
            lnkBtnLogin.setText("Logout");
            txtUserId= (TextView) headerView.findViewById(R.id.txtUserId);
            txtUserId.setText(AppPreferences.getInstance(getApplicationContext()).getUserName());
            txtUserName= (TextView) headerView.findViewById(R.id.txt_UserName);
            txtUserName.setText(AppPreferences.getInstance(getApplicationContext()).getDisplayName());
            if(AppPreferences.getInstance(this).getFcmTokenUpdated().equals("false")){
                FcmTokenUpdate.updateTokenToServer(this);
            }
        }

        //set the fragment initially

        FragmentTransaction fragmentTransaction=
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.commit();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        if(fragment instanceof Login){
            fab.show();
        }else
            fab.hide();

        final TextView lnkBtnlogin=(TextView) headerView.findViewById(R.id.lnkBtnLogin);
        lnkBtnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lnkBtnlogin.getText().equals("Logout"))
                {
                    FcmTokenUpdate.removeTokenFromServer(getApplicationContext());
                    AppPreferences.getInstance(getApplicationContext()).clearAll();
                    lnkBtnLogin= (TextView) headerView.findViewById(R.id.lnkBtnLogin);
                    lnkBtnLogin.setText("Login");
                    txtUserId= (TextView) headerView.findViewById(R.id.txtUserId);
                    txtUserId.setText("");
                    txtUserName= (TextView) headerView.findViewById(R.id.txt_UserName);
                    txtUserName.setText("Gust");
                    FirebaseMessaging.getInstance().subscribeToTopic("unregistered_news");
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("registered_news");
                }
                fragment=new Login();
                FragmentTransaction fragmentTransaction=
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,fragment);
                fragmentTransaction.commit();
                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.hide();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        if(!NetworkUtil.isNetworkAvailable(this)){
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                    .show();
        }


        AppPreferences.getInstance(this).setFcmToken(FirebaseInstanceId.getInstance().getToken());
        Log.d("MyFirebaseIIDService", "Refreshed token: " + FirebaseInstanceId.getInstance().getToken());
        //Toast.makeText(this,AppPreferences.getInstance(this).getFcmToken(),Toast.LENGTH_LONG).show();
//         Subscribing to news Topic
        FirebaseMessaging.getInstance().subscribeToTopic("news");
    }

//    To Handle back button press event whith double tab to exit
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }

    // Asks required permissions from user.
    public void setUpPermission() {
        if (isLollipopAbove()) {

            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_APP_PERMISSIONS);
                return;
            }
        }
    }

//    To handle permission request result
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
    public void isOnline() {
        if(!NetworkUtil.isNetworkAvailable(this)){
            Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_LONG)
                    .show();
        }
    }
//    Close Navigation Drawer by manually
    public void CloseMenu(View view){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
//    ActionBar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        final View menu_hotlist = menu.findItem(R.id.menu_hotlist).getActionView();
        ui_hot = (TextView) menu_hotlist.findViewById(R.id.hotlist_hot);
        updateHotCount(hot_number);
        new MyMenuItemStuffListener(menu_hotlist, "Show Notifications") {
//            Action Bar menu click event - Notification Icon
            @Override
            public void onClick(View v) {
                FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
                if(!(fragment instanceof Notification)) {
                    fragment = new Notification();
                }
                fab.hide();
                FragmentTransaction fragmentTransaction =
                        getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.commit();
            }
        };
        return true;
    }
//    Update unread notification count
    public void updateHotCount(final int new_hot_number) {
        hot_number = hot_number+new_hot_number;
        if (ui_hot == null) return;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (new_hot_number == 0)
                    ui_hot.setVisibility(View.INVISIBLE);
                else {
                    ui_hot.setVisibility(View.VISIBLE);
                    ui_hot.setText(Integer.toString(new_hot_number));
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id) {
            case R.id.menu_hotlist:

        }
         return super.onOptionsItemSelected(item);
    }

//    Navigation Drawer Item selected event
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        isOnline();
        UserId=AppPreferences.getInstance(this).getUserId();
        int id = item.getItemId();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (id == R.id.nav_Inspection) { // Checks which navigation item selected
            if(!(fragment instanceof Inspection)) {
                //checks whether the existing fragment same as selected fragment
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
            if("".equals(UserId)){
                fragment=new Login();
            }else {
                if (fragment instanceof TravelAgenda) {
                    ((TravelAgenda) fragment).scrollToItem(TravelAgendaItem.FLIGHT);
                } else {
                    fragment = new TravelAgenda();
//                Travel agenda list will be scrolled to specific item selected in navigation
//                this will directly select flight details agenda in list
                    Bundle bundle = new Bundle();
                    bundle.putInt("Type", TravelAgendaItem.FLIGHT);
                    fragment.setArguments(bundle);
                }

                fab.hide();
            }
        }else if (id == R.id.nav_Travel_Taxi) {
            if("".equals(UserId)){
                fragment=new Login();
            }else {
                if (fragment instanceof TravelAgenda) {
                    ((TravelAgenda) fragment).scrollToItem(TravelAgendaItem.TAXI);
                } else {
                    fragment = new TravelAgenda();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Type", TravelAgendaItem.TAXI);
                    fragment.setArguments(bundle);
                }

                fab.hide();
            }
        }else if (id == R.id.nav_Travel_Hotel) {
            if("".equals(UserId)){
                fragment=new Login();
            }else {
                if (fragment instanceof TravelAgenda) {
                    ((TravelAgenda) fragment).scrollToItem(TravelAgendaItem.HOTEL);
                } else {
                    fragment = new TravelAgenda();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Type", TravelAgendaItem.HOTEL);
                    fragment.setArguments(bundle);
                }

                fab.hide();
            }
        }
         else if (id == R.id.nav_Twitter) {
            if(NetworkUtil.isNetworkAvailable(this)) {
                try {
                    // Opens twiter app if app exists
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=" + "SG_SEFPRO")));
                } catch (Exception e) {
                    // opens twitter web page via browser when twitter app does not exists
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/SG_SEFPRO")));
                }
            }

        } else if (id == R.id.nav_LinkedIn) {


        }
        else if (id == R.id.nav_YouTube) {
            if(NetworkUtil.isNetworkAvailable(this)) {
//            Opens Youyube application
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/channel/UCqBq0SIlv0eMIo1FATKEQew"));
                startActivity(intent);
            }
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

//    Action Bar Notification icon Click and long Click listener class
    static abstract class MyMenuItemStuffListener implements View.OnClickListener, View.OnLongClickListener {
        private String hint;
        private View view;
        private Fragment fragment;

        MyMenuItemStuffListener(View view, String hint) {
            this.view = view;
            this.hint = hint;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override abstract public void onClick(View v);

        @Override public boolean onLongClick(View v) {
            final int[] screenPos = new int[2];
            final Rect displayFrame = new Rect();
            view.getLocationOnScreen(screenPos);
            view.getWindowVisibleDisplayFrame(displayFrame);
            final Context context = view.getContext();
            final int width = view.getWidth();
            final int height = view.getHeight();
            final int midy = screenPos[1] + height / 2;
            final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            Toast cheatSheet = Toast.makeText(context, hint, Toast.LENGTH_SHORT);
            if (midy < displayFrame.height()) {
                cheatSheet.setGravity(Gravity.TOP | Gravity.RIGHT,
                        screenWidth - screenPos[0] - width / 2, height);
            } else {
                cheatSheet.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, height);
            }
            cheatSheet.show();
            return true;
        }
    }

}
