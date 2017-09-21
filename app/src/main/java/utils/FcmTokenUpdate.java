package utils;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.spicasoft.sefpro.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by USER on 19-09-2017.
 */

public class FcmTokenUpdate {

    public static void updateTokenToServer(final Context context){
        JSONObject DeviceFcmToken = new JSONObject();
        try {
            DeviceFcmToken.put("DeviceId","");
            DeviceFcmToken.put("FCMToken",AppPreferences.getInstance(context).getFcmToken());
            DeviceFcmToken.put("App_UserId",AppPreferences.getInstance(context).getUserId());
            DeviceFcmToken.put("Token_DateTime", Calendar.getInstance().getTime());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> tokenResponseListener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // parseResponse(response);
                try {
                    if(response.getString("MessageCode").equals("00")){
                        AppPreferences.getInstance(context).setFcmTokenUpdated("True");
                    }else{
                        AppPreferences.getInstance(context).setFcmTokenUpdated("false");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener tokenErrorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppPreferences.getInstance(context).setFcmTokenUpdated("false");
                error.printStackTrace();

            }
        };
        RequestToServer.RequestToServer(context
                ,"UpdateFcmToken",
                DeviceFcmToken,tokenResponseListener,tokenErrorListener);
    }
    public static void removeTokenFromServer(final Context context){
        JSONObject DeviceFcmToken = new JSONObject();
        try {
            DeviceFcmToken.put("DeviceId","");
            DeviceFcmToken.put("FCMToken",AppPreferences.getInstance(context).getFcmToken());
            DeviceFcmToken.put("App_UserId",AppPreferences.getInstance(context).getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Response.Listener<JSONObject> tokenResponseListener=new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // parseResponse(response);
                try {
                    if(response.getString("MessageCode").equals("00")){

                    }else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Response.ErrorListener tokenErrorListener= new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AppPreferences.getInstance(context).setFcmTokenUpdated("false");
                error.printStackTrace();

            }
        };
        RequestToServer.RequestToServer(context
                ,"UnRegisterToken",
                DeviceFcmToken,tokenResponseListener,tokenErrorListener);
    }

    /**
     * Return pseudo unique ID
     * @return ID
     */
    public static String getUniquePsuedoID() {
        // If all else fails, if the user does have lower than API 9 (lower
        // than Gingerbread), has reset their device or 'Secure.ANDROID_ID'
        // returns 'null', then simply the ID returned will be solely based
        // off their Android device information. This is where the collisions
        // can happen.
        // Thanks http://www.pocketmagic.net/?p=1662!
        // Try not to use DISPLAY, HOST or ID - these items could change.
        // If there are collisions, there will be overlapping data
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);

        // Thanks to @Roman SL!
        // https://stackoverflow.com/a/4789483/950427
        // Only devices with API >= 9 have android.os.Build.SERIAL
        // http://developer.android.com/reference/android/os/Build.html#SERIAL
        // If a user upgrades software or roots their device, there will be a duplicate entry
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();

            // Go ahead and return the serial for api => 9
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            // String needs to be initialized
            serial = "serial"; // some value
        }

        // Thanks @Joe!
        // https://stackoverflow.com/a/2853253/950427
        // Finally, combine the values we have found by using the UUID class to create a unique identifier
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
}
