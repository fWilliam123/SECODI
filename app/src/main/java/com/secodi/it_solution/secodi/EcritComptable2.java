package com.secodi.it_solution.secodi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EcritComptable2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecrit_comptable2);

        Button svt = (Button) findViewById(R.id.valider);
        svt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EcritComptable2.this, EcritAnalytique.class));
            }
        });
    }
}
