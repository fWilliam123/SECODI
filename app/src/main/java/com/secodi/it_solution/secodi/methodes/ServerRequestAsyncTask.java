package com.secodi.it_solution.secodi.methodes;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import static com.secodi.it_solution.secodi.methodes.Controller.Save_sharedString;
import static com.secodi.it_solution.secodi.methodes.Controller.downloadJson;
import static com.secodi.it_solution.secodi.methodes.Controller.serverRequest;

/**
 * Created by wilson on 27/04/17.
 */

public class ServerRequestAsyncTask extends AsyncTask<Void, Void, String> {

        public String reqUrl;
        public JSONObject params;
        public String method;
        public int timeout;
        public String title;
        public String message;
        public Context ctx;
        public String shared_string;
        private String reqJson = "";
        public Controller controller;
        private ProgressDialog mProgressBar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            controller.showDialog(this.title, this.message);
        }

        @Override
        protected String doInBackground(Void... voids) {
            reqJson = downloadJson(this.reqUrl,this.timeout,this.method);
            JSONObject errjson= null;
            try {
                errjson = new JSONObject(reqJson);
                Log.e("dsd", reqJson);
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("ERROR", e.getLocalizedMessage());
                try {
                    errjson.put("response_type", "error");
                    errjson.put("message", "UNEXPECTED_ERROR");
                    reqJson=errjson.toString();
                } catch (JSONException e1) {
                    Log.e("ERROR",e1.getLocalizedMessage());
                }

            }


            return reqJson;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            controller.hideDialog();
            Save_sharedString(ctx, shared_string, result);
        }


}
