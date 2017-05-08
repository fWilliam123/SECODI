package com.secodi.it_solution.secodi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.secodi.it_solution.secodi.methodes.Controller;
import com.secodi.it_solution.secodi.methodes.ServerRequestAsyncTask;

import static com.secodi.it_solution.secodi.methodes.Controller.Load_sharedString;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerRequestAsyncTask serverRequestAsyncTask = new ServerRequestAsyncTask();
        serverRequestAsyncTask.ctx = this;
        serverRequestAsyncTask.controller = new Controller(this);
        serverRequestAsyncTask.message = "chargement";
        serverRequestAsyncTask.timeout=1000;
        serverRequestAsyncTask.reqUrl="http://209.126.69.121:1821/ZUMI_DEV/indexServeur.php?page=depot";
        serverRequestAsyncTask.title="cdds";
        serverRequestAsyncTask.shared_string="test";

        TextView t = (TextView) findViewById(R.id.test);
        String x = Load_sharedString(this,"test");
        Log.e("dfd", x);
        t.setText(x);

        Button svt = (Button) findViewById(R.id.svt);
        svt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
    }
}
