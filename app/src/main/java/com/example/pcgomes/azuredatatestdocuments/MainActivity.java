package com.example.pcgomes.azuredatatestdocuments;

import com.azure.data.AzureData;
import com.azure.data.model.PermissionMode;
import com.example.pcgomes.azuredatatestdocuments.Augmented_Reality.AugmentedReality;
import com.example.pcgomes.azuredatatestdocuments.Reports.Field_Report_Service;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import static com.azure.data.util.FunctionalUtils.onCallback;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button n = findViewById(R.id.buttonChangeActivity);
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(getApplicationContext(), DatabaseActivity.class );
                startActivity(c);
            }
        });

        Button ar = findViewById(R.id.AugmentedRealityButton);
        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent c = new Intent(getApplicationContext(), Field_Report_Service.class);
                startActivity(c);
            }
        });
        Button qr = findViewById(R.id.buttonQrCode);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent c = new Intent(getApplicationContext(), QrCodeActivity.class);
                startActivity(c);
            }
        });
        AzureData.configure(getApplicationContext(), "iotprivatenewsqldb",
                "7E9XRXFSmbOf8EZw8Z2aeakp278EhvN1jPmksYlXlOpoaR5pLLBF7ppjFnYzUfwhBjQxsb7f21xGHd29NJaABg==", PermissionMode.All, onCallback(builder -> {

        }));

    }
}
