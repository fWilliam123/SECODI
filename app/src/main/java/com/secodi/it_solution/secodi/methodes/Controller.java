package com.secodi.it_solution.secodi.methodes;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by wilson on 27/04/17.
 */

public class Controller {
    private ProgressDialog mProgressBar;
    private Context context;


    public Controller(Context context){this.context = context;}

    /**
     * showing dialog box
     * @param title title of dialog box
     * @param message msg -----
     */
    public void showDialog(String title,String message) {
        if(mProgressBar!=null) mProgressBar.dismiss();
        mProgressBar = new ProgressDialog(this.context);
        mProgressBar.setTitle(title);
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage(message);
        mProgressBar.show();
    }

    /**
     * hide dialog box
     */
    public void hideDialog() {

        if(mProgressBar.isShowing())
        {
            mProgressBar.dismiss();
        }
    }
    /**
     * telecharge le fichier json
     * @param reqUrl
     * @return l'url
     */
    public static String downloadJson(String reqUrl, int timeOut, String method){
        StringBuilder sb = new StringBuilder();
        InputStream in = null;
        try {
            //connection a l'url du json
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeOut);

            //on se rassure que la requette s'est bien effectue
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                //recupere la requette
                in = connection.getInputStream();

                //lecture de la reponse
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));

                String line;

                while ((line=bufferedReader.readLine()) != null){
                    sb.append(line).append('\n');
                }
            }
            else if (connection.getResponseCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
                sb.append("timeOut");
            }
            else if(connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
            {
                sb.append("notFound");
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return sb.toString();
    }
    /**
     * api_request : call serverRequest
     * @param reqUrl the url we are calling
     * @param params a JSONObject of keyed parameters
     * @param method the method of the api we are calling
     * @param timeout the time to wait before to stop the request
     * @return json string
     */
    public static String serverRequest(String reqUrl, JSONObject params, String method, int timeout) throws JSONException {
        StringBuilder sb = new StringBuilder();
        InputStream in = null;
        String input=mapparameters(params);
        String result=null;
        JSONObject errjs=new JSONObject();
        errjs.put("response_type","error");
        try {
            //connection at url
            URL url = new URL(reqUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(method);
            connection.setConnectTimeout(timeout);

            if (params != null){
                //envoie des donnees a la requette
                connection.setRequestProperty("Content-Type", "application/json");
                OutputStream os = connection.getOutputStream();
                os.write(input.getBytes());
                os.flush();
                os.close();
            }

            //on se rassure que la requette s'est bien effectue
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){

                //recupere la requette
                in = connection.getInputStream();
                result= new Scanner(in).useDelimiter("\\A").next();

            }
            else if (connection.getResponseCode() == HttpURLConnection.HTTP_CLIENT_TIMEOUT){
                errjs.put("message","REQUEST_TIMEOUT");
                result=errjs.toString();
            }
            else if(connection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
            {
                errjs.put("message","BAD_REQUEST");
                result=errjs.toString();
            }else{
                errjs.put("message","TEMPORARY_ERROR "+connection.getResponseCode());
                result=errjs.toString();
            }

        }catch (MalformedURLException e) {
            e.printStackTrace();
            errjs.put("message","REQUEST_ERROR");
            result=errjs.toString();
        } catch (IOException e) {
            e.printStackTrace();
            errjs.put("message","REQUEST_ERROR");
            result=errjs.toString();
        }

        return result;
    }


    public static String mapparameters(JSONObject jsonObj) throws JSONException {
        Iterator<?> keys =  jsonObj.keys();
        Map<String,String> params=new HashMap<>();
        while( keys.hasNext() ) {
            String key = (String)keys.next();
            String value= String.valueOf(jsonObj.get(key));
            params.put(key,value);
        }
        String input = params.toString();
        return input;

    }

    /**
     * save shared String Preference
     * @param ctx
     * @param SharedName
     * @param SharedValue
     */
    public static void Save_sharedString(Context ctx, String SharedName, String SharedValue) {
        delete_sharedString(ctx, SharedName);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.putString(SharedName, SharedValue);
        editor.apply();
    }

    /**
     * delete shared string preference
     * @param ctx
     * @param SharedName
     */
    public static void delete_sharedString(Context ctx, String SharedName) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(ctx).edit();
        editor.remove(SharedName);
        editor.apply();
    }

    /**
     * load shared string preference
     * @param ctx
     * @param SharedName
     * @return
     */
    public static String Load_sharedString(Context ctx, String SharedName) {
        String retour = PreferenceManager.getDefaultSharedPreferences(ctx).getString(SharedName, null);
        if (retour == null) {
            return "";
        }
        return retour;
    }

}
