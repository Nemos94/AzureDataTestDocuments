package com.example.pcgomes.azuredatatestdocuments.Reports;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.azure.data.AzureData;
import com.example.pcgomes.azuredatatestdocuments.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.azure.data.util.FunctionalUtils.onCallback;

public class Field_Report_Service extends AppCompatActivity implements View.OnClickListener {
    private String id;
    private ArrayList<String> listproblemssolved;
    private String date;
    private String typeReport;
    private String stringtypeReport;
    private String description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_field_service);
        listproblemssolved = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("typeReport") == null)
                stringtypeReport = " ";
            else
                stringtypeReport = extras.getString("typeReport");
            id = extras.getString("idmachine");
            listproblemssolved = (ArrayList<String>) extras.getSerializable("listproblemssolved");
            date = (String) extras.getString("date");
            if(extras.getString("description") == null)
                description = " ";
            else
                description = extras.getString("description");
        }

        EditText problemsview = findViewById(R.id.problems);
        EditText datereport = findViewById(R.id.dateReport);
        TextView idm = findViewById(R.id.idmachine);
        idm.setText(id);
        StringBuilder b = new StringBuilder();
        for (String f : listproblemssolved) {
            b.append(f);
            b.append(" ");
        }
        problemsview.setText(b.toString(),TextView.BufferType.EDITABLE);
        datereport.setText(date);

        Button br = findViewById(R.id.sendReportButton);
        br.setOnClickListener(view -> generateReport(listproblemssolved,id,date,typeReport));
        Button repair = findViewById(R.id.repairButton);
        repair.setOnClickListener(this);
        Button maintenance = findViewById(R.id.MaintenanceButton);
        maintenance.setOnClickListener(this);
        Button install = findViewById(R.id.InstallationButton);
        install.setOnClickListener(this);
        Button other = findViewById(R.id.otherButton);
        other.setOnClickListener(this);
        EditText dr = findViewById(R.id.edtInput);
        if(dr.getText().toString().isEmpty())
            dr.setText(description);

        if(stringtypeReport.equals("Repair"))
            repair.setBackgroundColor(Color.parseColor("#3299CC"));
        if(stringtypeReport.equals("Maintenance"))
            repair.setBackgroundColor(Color.parseColor("#3299CC"));
        if(stringtypeReport.equals("Installation"))
            repair.setBackgroundColor(Color.parseColor("#3299CC"));
        if(stringtypeReport.equals("Other"))
            repair.setBackgroundColor(Color.parseColor("#3299CC"));
    }

    public void generateReport(ArrayList<String> listproblemsSolved, String idMachine,
                               String date, String typeReport) {

        EditText dr = findViewById(R.id.edtInput);
        description = dr.getText().toString();
        Report r = new Report(listproblemsSolved,"aaa","aaa", idMachine,
                description,date,typeReport);

        AzureData.createDocument(r,"Equipment", "valuesDatabase", onCallback(response -> {
            if(response.isSuccessful()){
                String d = "xlslsl";
            }
        }));
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.repairButton:
                v.setBackgroundColor(Color.parseColor("#3299CC"));
                typeReport = "Repair";
                break;
            case R.id.MaintenanceButton:
                typeReport = "Maintenance";
                v.setBackgroundColor(Color.parseColor("#3299CC"));
                break;
            case R.id.InstallationButton:
                typeReport = "Installation";
                v.setBackgroundColor(Color.parseColor("#3299CC"));
                break;
            case R.id.otherButton:
                typeReport = "Other";
                v.setBackgroundColor(Color.parseColor("#3299CC"));
                break;
        }
    }
}
