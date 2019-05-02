package com.example.pcgomes.azuredatatestdocuments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.azure.data.model.Document;
import com.example.pcgomes.azuredatatestdocuments.Augmented_Reality.AugmentedReality;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class ValuesActivity extends Activity {

    private static final String TAG = "ValueActivity";
    private String _documentId;
    private TableLayout mTableLayout;
    private CardAdapter<Document> _adapter;

    private String _databaseId;

    private String _collectionId;

    private ArrayList<String> listaValues;
    private ArrayList<String> idnomes;
    private ArrayList<String> datasAlertas;
    private HashMap<String, ArrayList<String>> listaValuesSensor;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.values_activity);

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        TextView collectionIdTextView = findViewById(R.id.titleDocument);
        TextView valuesTextList = findViewById(R.id.values);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _documentId = extras.getString("id_equipamento");
            listaValuesSensor = (HashMap<String, ArrayList<String>>) extras.getSerializable("listaValuesSensor");
            datasAlertas = (ArrayList<String>) extras.getSerializable("dataValues");

            collectionIdTextView.setText(_documentId);
        }

       mTableLayout = (TableLayout) findViewById(R.id.tableInvoices);
        mTableLayout.setStretchAllColumns(true);
      //  loadData();
        TableProblems t = new TableProblems();
        t.populateTable(this,mTableLayout,datasAlertas,listaValuesSensor,"all");

        Button ar = findViewById(R.id.arButton);
        ar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AugmentedReality.class);
               // intent.putExtra("id_equipamento", _idDevice);
                //intent.putExtra("listaValuesSensor", listaValuesSensor.get(coll.getId()).toString());
                intent.putExtra("listaValuesSensor", listaValuesSensor);
                intent.putExtra("dataValues", datasAlertas);
                intent.putExtra("idmachine", _documentId);
                startActivity(intent);
            }
        });

    }



}
