package utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by USER on 15-09-2017.
 */

public class AppPreferences {
    public static final String SEFPRO_PREFERENCE = "SEFPROPreference";
    private static final String KEY_IS_FIRST_TIME = "Is_First_Time_Launch";
    private static final String KEY_FCM_TOKEN = "Fcm_Token";
    private static final String USERID="User_Id";
    private static final String USER_NAME="User_Name";
    private static final String PASSWORD="Password";
    private static final String DISPLAY_NAME="Display_Name";
    private static final String KEY_IS_FCM_TOKEN_UPDATED = "Fcm_Token_Updated";
    private static AppPreferences appPreferences = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefEditor;
    private AppPreferences() {
    }

    public static AppPreferences getInstance(Context context) {
        if (appPreferences == null) {
            appPreferences = new AppPreferences();
            appPreferences.setContext(context);
        }
        return appPreferences;
    }
    public void setContext(Context context) {
        if (sharedPreferences != null) {
            return;
        }
        sharedPreferences = context.getSharedPreferences(SEFPRO_PREFERENCE,
                Context.MODE_PRIVATE);
        prefEditor = sharedPreferences.edit();
    }

    public String getFcmToken() {
        return sharedPreferences.getString(KEY_FCM_TOKEN, "");
    }

    public void setFcmToken(String token) {
        prefEditor.putString(KEY_FCM_TOKEN, token);
        prefEditor.commit();
    }

    public String getUserId() {
        return sharedPreferences.getString(USERID, "");
    }
    public void setUserId(String userId) {
        prefEditor.putString(USERID, userId);
        prefEditor.commit();
    }
    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }
    public void setUserName(String userName) {
        prefEditor.putString(USER_NAME, userName);
        prefEditor.commit();
    }
    public String getPassword() {
        return sharedPreferences.getString(PASSWORD, "");
    }
    public void setPassword(String password) {
        prefEditor.putString(PASSWORD, password);
        prefEditor.commit();
    }
    public String getDisplayName() {
        return sharedPreferences.getString(DISPLAY_NAME, "");
    }
    public void setDisplayName(String displayName) {
        prefEditor.putString(DISPLAY_NAME, displayName);
        prefEditor.commit();
    }
    public void clearAll(){
        setUserId("");
        setUserName("");
        setPassword("");
        setDisplayName("");
        setFcmTokenUpdated("false");
    }
    public void setAll(String Userid,String userName,String password,String displayName){
        setUserId(Userid);
        setUserName(userName);
        setPassword(password);
        setDisplayName(displayName);
    }
    public String getFcmTokenUpdated() {
        return sharedPreferences.getString(KEY_IS_FCM_TOKEN_UPDATED, "");
    }

    public void setFcmTokenUpdated(String Fcm_Token_Updated) {
        prefEditor.putString(KEY_IS_FCM_TOKEN_UPDATED, Fcm_Token_Updated);
        prefEditor.commit();
    }

}
