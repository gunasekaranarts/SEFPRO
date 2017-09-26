package utils;

import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.spicasoft.sefpro.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 20-09-2017.
 */

public class RequestToServer {
    static String userName=null;
    static String password=null;
    static String Server="http://192.168.1.2:45455/api/sefpro/";

    public static void RequestToServer(Context context,String Url,JSONObject ReqObj
            ,Response.Listener<JSONObject> responseListener,Response.ErrorListener errListener){

        userName=AppPreferences.getInstance(context).getUserName();
        password=AppPreferences.getInstance(context).getPassword();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Server+Url, ReqObj, responseListener,
                errListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String credentials = userName + ":" + password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("accept", "application/json");

                return headers;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getmInstance(context).addToRequestQue(jsonObjectRequest);

    }
    public static void RequestToServer(Context context,String Url
            ,Response.Listener<JSONArray> responseListener,Response.ErrorListener errListener){

        userName=AppPreferences.getInstance(context).getUserName();
        password=AppPreferences.getInstance(context).getPassword();

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Server+Url, responseListener,
                errListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                if(!("".equals(userName))) {
                    String credentials = userName + ":" + password;
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                }
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("accept", "application/json");

                return headers;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getmInstance(context).addToRequestQue(jsonObjectRequest);

    }

    public static void RequestToLogin(Context context, String Url, final String userName, final String password, JSONObject ReqObj
            , Response.Listener<JSONObject> responseListener, Response.ErrorListener errListener){

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Server+Url, ReqObj, responseListener,
                errListener) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                String credentials = userName + ":" + password;
                String auth = "Basic "
                        + Base64.encodeToString(credentials.getBytes(),
                        Base64.NO_WRAP);
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("accept", "application/json");

                return headers;
            }
        };
        int socketTimeout = 30000; // 30 seconds. You can change it
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

        jsonObjectRequest.setRetryPolicy(policy);
        MySingleton.getmInstance(context).addToRequestQue(jsonObjectRequest);

    }
}
