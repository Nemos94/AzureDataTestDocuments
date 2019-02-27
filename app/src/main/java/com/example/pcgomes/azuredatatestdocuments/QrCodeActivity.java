package com.example.pcgomes.azuredatatestdocuments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

import com.azure.data.AzureData;
import com.azure.data.model.DictionaryDocument;
import com.azure.data.model.Document;
import com.azure.data.model.PermissionMode;
import com.azure.data.model.Query;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.azure.data.util.FunctionalUtils.onCallback;

public class QrCodeActivity extends Activity {

    SurfaceView cameraPreview;
    TextView txtResult;
    BarcodeDetector barcodeDetector;
    CameraSource cameraSource;
    final int RequestCameraPermissionID = 1001;
    String _idDevice;
    private Intent intent;


    private static final String TAG = "CollectionsActivity";

    private CardAdapter<Document> _adapter;

    private String _databaseId;

    private String _collectionId;

    private ArrayList<Integer> listaValues;
    private ArrayList<String> idnomes;
    private ArrayList<String> datasAlertas;
    private HashMap<String, ArrayList<Integer> > listaValuesSensor;


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,  int[] grantResults) {
        switch (requestCode){
            case RequestCameraPermissionID :
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.qrcode_activity);

        cameraPreview = findViewById(R.id.cameraPreview);
        txtResult = findViewById(R.id.txtresult);

        barcodeDetector = new BarcodeDetector.Builder(this).
                setBarcodeFormats(Barcode.QR_CODE).build();
        cameraSource = new CameraSource.Builder(getApplicationContext(), barcodeDetector)
                .setRequestedPreviewSize(640, 480).build();
        intent = new Intent(getApplicationContext(),ValuesActivity.class);
        idnomes = new ArrayList<>();
        listaValues = new ArrayList<>();
        listaValuesSensor = new HashMap<>();
        datasAlertas = new ArrayList<>();


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> qrcodes = detections.getDetectedItems();
                if(qrcodes.size() != 0)
                {
                    txtResult.post(new Runnable() {
                        @Override
                        public void run() {
                            //Create vibrate
                            Vibrator vibrator = (Vibrator)getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(100);
                            _idDevice = qrcodes.valueAt(0).displayValue;
                            txtResult.setText(qrcodes.valueAt(0).displayValue);

                            getQuery();

                        }

                    });

                }

            }
        });

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(QrCodeActivity.this, new String[]{Manifest.permission.CAMERA},RequestCameraPermissionID);
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });



    }

    public void getQuery(){

        try
        {
            final ProgressDialog dialog = ProgressDialog.show(QrCodeActivity.this, "", "Loading. Please wait...", true);

            Query query = Query.Companion.select()
                    .from("Equipment")
                    .where("deviceId", _idDevice);


            AzureData.queryDocuments("Equipment", "valuesDatabase", query, DictionaryDocument.class,null, onCallback( response  -> {
                Log.e(TAG, "Document list result: " + response.isSuccessful());

                    int i = 0;
                    for( Document d : response.getResource().getItems()){
                        if(response.getResource().getItems().get(i).get("deviceId").equals(_idDevice)) {
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
                            }
                        }
                        dialog.cancel();
                    }

                intent.putExtra("id_equipamento", _idDevice);
                //intent.putExtra("listaValuesSensor", listaValuesSensor.get(coll.getId()).toString());
                intent.putExtra("listaValuesSensor", listaValuesSensor);
                intent.putExtra("dataValues", datasAlertas);
                startActivity(intent);

            }));

        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
