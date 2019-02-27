package com.example.pcgomes.azuredatatestdocuments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.UiThread;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.azure.data.AzureData;
import com.azure.data.model.DictionaryDocument;
import com.azure.data.model.Document;
import com.azure.data.model.Query;
import com.azure.data.model.Query.Companion;
import com.azure.data.model.ResourceList;
import com.azure.data.model.Result;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import okhttp3.ResponseBody;

import static com.azure.data.util.FunctionalUtils.onCallback;

public class DocumentsActivity extends Activity {

    private static final String TAG = "CollectionsActivity";

    private CardAdapter<Document> _adapter;

    private String _databaseId;

    private String _collectionId;


    private ArrayList<Integer> listaValues;
    private ArrayList<String> idnomes;
    private ArrayList<String> datasAlertas;
    private HashMap<String, ArrayList<Integer> > listaValuesSensor;

    private int i = 0;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override protected void onCreate(Bundle savedInstanceState) {
        getQuery();
        super.onCreate(savedInstanceState);

        setContentView(R.layout.documents_activity);

        LinearLayoutManager linearLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        _adapter = new CardAdapter<>(R.layout.document_view, new Callback<Object>() {
            @Override
            public Void call() {
                Document coll = (Document)this._result;
                DocumentViewHolder vHolder = (DocumentViewHolder)this._viewHolder;

                vHolder.idTextView.setText(coll.getId());
                vHolder.ridTextView.setText(coll.getResourceId());
                vHolder.selfTextView.setText(coll.getSelfLink());
                vHolder.eTagTextView.setText(coll.getEtag());
                i++;
                vHolder.itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(getBaseContext(), ValuesActivity.class);
                    intent.putExtra("id_equipamento", coll.getId());
                    //intent.putExtra("listaValuesSensor", listaValuesSensor.get(coll.getId()).toString());
                    intent.putExtra("listaValuesSensor", listaValuesSensor);
                    intent.putExtra("dataValues", datasAlertas);
                    startActivity(intent);
                });

                return null;
            }
        }, DocumentViewHolder.class);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(_adapter);

        TextView collectionIdTextView = findViewById(R.id.collectionId);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _databaseId = extras.getString("db_id");
            _collectionId = extras.getString("coll_id");

            collectionIdTextView.setText(_collectionId);
        }

        Button clearButton = findViewById(R.id.button_clear);
        Button fetchButton = findViewById(R.id.button_fetch);
        Button deleteButton = findViewById(R.id.button_delete);

        clearButton.setOnClickListener(v -> _adapter.clear());

        deleteButton.setOnClickListener(v -> {
            final ProgressDialog dialog = ProgressDialog.show(DocumentsActivity.this, "", "Deleting. Please wait...", true);

            AzureData.deleteCollection(_collectionId, _databaseId, onCallback(response -> {

                Log.e(TAG, "Collection delete result: " + response.isSuccessful());

                runOnUiThread(() -> {
                    _adapter.clear();
                    dialog.cancel();
                    finish();
                });
            }));
        });

    }

    public void getQuery(){

        try
        {
            final ProgressDialog dialog = ProgressDialog.show(DocumentsActivity.this, "", "Loading. Please wait...", true);

            Query query = Query.Companion.select()
                    .from("Equipment")
                    .where("deviceId", "Box2");


            AzureData.queryDocuments("Equipment", "valuesDatabase", query, DictionaryDocument.class,null, onCallback( response  -> {
                Log.e(TAG, "Document list result: " + response.isSuccessful());

                runOnUiThread(() -> {


                    int i = 0;
                    for( Document d : response.getResource().getItems()){
                        if(response.getResource().getItems().get(i).get("deviceId").equals("Box2")) {
                            Object alert = response.getResource().getItems().get(i).get("alert");
                            Object valueSensor = response.getResource().getItems().get(i).get("value");
                            Object datavalor = response.getResource().getItems().get(i).get("data");
                            i++;
                            if(listaValuesSensor.isEmpty()){
                                listaValues.add(Integer.valueOf(valueSensor.toString()));
                                listaValuesSensor.put(alert.toString(),listaValues );
                                datasAlertas.add(datavalor.toString());
                            }else{
                                if(listaValuesSensor.containsKey(alert.toString())){
                                    ArrayList<Integer> o =  listaValuesSensor.get(alert.toString());
                                    o.add(Integer.valueOf(valueSensor.toString()));
                                    listaValuesSensor.put(alert.toString(),o );
                                    datasAlertas.add(datavalor.toString());
                                }else{
                                    listaValues = new ArrayList<>();
                                    listaValues.add(Integer.valueOf(valueSensor.toString()));
                                    listaValuesSensor.put(alert.toString(), listaValues);
                                    datasAlertas.add(datavalor.toString());
                                }
                            }
                            if(!idnomes.contains(alert.toString())) {
                                d.setId(alert.toString());
                                idnomes.add(alert.toString());
                                _adapter.addData(d);
                            }
                        }
                        dialog.cancel();
                    }

                });
            }));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}