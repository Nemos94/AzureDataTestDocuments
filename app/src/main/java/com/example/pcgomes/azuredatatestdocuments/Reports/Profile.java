package com.example.pcgomes.azuredatatestdocuments.Reports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.pcgomes.azuredatatestdocuments.MainActivity;
import com.example.pcgomes.azuredatatestdocuments.QrCodeActivity;
import com.example.pcgomes.azuredatatestdocuments.R;

public class Profile extends Activity {

    private String displayName;
    private String id;
    private String userPrincipalName;
    private Button signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            displayName = extras.getString("displayName");
            id = extras.getString("id");
            userPrincipalName = extras.getString("userPrincipalName");
            // and get whatever type user account id is
        }

        TextView name = findViewById(R.id.name);
        name.setText(displayName);
        TextView mail = findViewById(R.id.technicianmail);
        mail.setText(userPrincipalName);
        TextView ids = findViewById(R.id.technicianid);
        ids.setText(id);
        Button qr = findViewById(R.id.buttonQrCode);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent c = new Intent(getApplicationContext(), QrCodeActivity.class);
                startActivity(c);
            }
        });

        signOutButton = findViewById(R.id.clearCache);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              MainActivity.onSignOutClicked(getApplicationContext());
               Intent d = new Intent(getApplicationContext(), MainActivity.class);
               startActivity(d);
            }
        });
    }


}
