package com.example.pcgomes.azuredatatestdocuments.Augmented_Reality;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pcgomes.azuredatatestdocuments.MainActivity;
import com.example.pcgomes.azuredatatestdocuments.R;
import com.example.pcgomes.azuredatatestdocuments.TableProblems;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class AugmentedReality extends AppCompatActivity {

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    private ArrayList<String> idnomes;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    ArFragment arFragment;
    ModelRenderable lampPostRenderable;

    ViewRenderable animal_name;
    ViewRenderable alert_view;
    TransformableNode lamp;
    ArrayList<ViewRenderable> viewlistRenderable;
    ViewRenderable problems_equipment1;
    ViewRenderable problems_equipment2;
    ViewRenderable problems_equipment3;
    ViewRenderable problems_equipment4;
    ViewRenderable problems_equipment5;
    //preencher a tabela
    private String _documentId;
    private ArrayList<String> datasAlertas;
    private HashMap<String, ArrayList<String>> listaValuesSensor;

    private int j = 0;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView collectionIdTextView = findViewById(R.id.titleDocument);
        idnomes = new ArrayList<String>();



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //_documentId = extras.getString("id_equipamento");
            listaValuesSensor = (HashMap<String, ArrayList<String>>) extras.getSerializable("listaValuesSensor");
            datasAlertas = (ArrayList<String>) extras.getSerializable("dataValues");
            for (String d: listaValuesSensor.keySet()) {
                idnomes.add(d);
            }
            //collectionIdTextView.setText(_documentId);
        }

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.ar_fragment);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        //////////////////////Renderable da máquina/////////////////////////
        ViewRenderable.builder()
                .setView(this, R.layout.ar_table_equipment)
                .build().thenAccept(renderable -> animal_name = renderable );

        //////////////////////Renderable dos problemas/////////////////////////
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> problems_equipment1 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> problems_equipment2 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> problems_equipment3 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> problems_equipment4 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> problems_equipment5 = renderable );

        viewlistRenderable = new ArrayList<ViewRenderable>();
        viewlistRenderable.add(problems_equipment1);
        viewlistRenderable.add(problems_equipment2);
        viewlistRenderable.add(problems_equipment3);
        viewlistRenderable.add(problems_equipment4);
        viewlistRenderable.add(problems_equipment5);
        /////////////////////////////////////////////////////////////////////////
        ViewRenderable.builder()
                .setView(this, R.layout.ar_alert_equipment)
                .build().thenAccept(renderable -> alert_view = renderable );

        ModelRenderable.builder()
                .setSource(this, Uri.parse("nespresso_vertuo_plus.sfb"))
                .build()
                .thenAccept(renderable -> lampPostRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                    if (lampPostRenderable == null){
                        return;
                    }
                    Anchor anchor = hitresult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());
                    lamp = new TransformableNode(arFragment.getTransformationSystem());
                    lamp.setParent(anchorNode);
                    lamp.setRenderable(lampPostRenderable);
                    lamp.select();

                    //addName(anchorNode, lamp, "lamp");
                    addAlert(anchorNode, lamp);

                }
        );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addAlert(AnchorNode anchorNode, TransformableNode model) {
        setNode(anchorNode, model, 0,0.6f,0, alert_view);

        ImageView img = (ImageView) alert_view.getView();
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //anchorNode.removeChild(anchorNode.getChildren().get(1));
                //addName(anchorNode, lamp, "lamp");
                addProblems(anchorNode, lamp,"lamp");
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addTable(AnchorNode anchorNode, TransformableNode model, String name) {

        setNode(anchorNode, model, 0,anchorNode.getChildren().get(2).getLocalPosition().y + 0.2f, 0 ,animal_name);

        mTableLayout = (TableLayout) animal_name.getView();
        mTableLayout.setStretchAllColumns(true);
        TableProblems t = new TableProblems();
        t.populateTable(this,mTableLayout,datasAlertas,listaValuesSensor,name);
       // loadData(name);
    }

    private void setNode(AnchorNode anchorNode, TransformableNode model, float x,float y,float z, ViewRenderable equipment) {
        TransformableNode nameView = new TransformableNode(arFragment.getTransformationSystem());
        nameView.setName(equipment.toString());
        nameView.setLocalPosition(new Vector3(x, y, z));
        nameView.setParent(anchorNode);
        nameView.setRenderable(equipment);
        nameView.select();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addProblems(AnchorNode anchorNode, TransformableNode model, String name) {

        if(!idnomes.isEmpty()){
            for (int i = 0; i < idnomes.size(); i++){
                if(i==0) {
                    setNode(anchorNode, model, 0,anchorNode.getChildren().get(1).getLocalPosition().y + 0.2f, 0 ,problems_equipment1);
                    TextView txt_name1 = (TextView) problems_equipment1.getView();
                    txt_name1.setText(idnomes.get(i));
                    txt_name1.setOnClickListener(view -> {addTable(anchorNode,lamp,idnomes.get(0));j++;});
                }
                if(i==1){
                    j++;
                    setNode(anchorNode, model, 0.3f, anchorNode.getChildren().get(1).getLocalPosition().y, 0f ,problems_equipment2);
                    TextView txt_name2 = (TextView) problems_equipment2.getView();
                    txt_name2.setText(idnomes.get(i));
                    txt_name2.setOnClickListener(view -> {addTable(anchorNode,lamp,idnomes.get(1));j++;
                    });
                }
                if(i==2){
                    j++;
                    setNode(anchorNode, model, -0.3f, anchorNode.getChildren().get(1).getLocalPosition().y, 0f ,problems_equipment3);
                    TextView txt_name3 = (TextView) problems_equipment3.getView();
                    txt_name3.setText(idnomes.get(i));
                    txt_name3.setOnClickListener(view -> {addTable(anchorNode,lamp,idnomes.get(2));j++;});
                }
                if(i==3){
                    j++;
                    setNode(anchorNode, model, 0, anchorNode.getChildren().get(i).getLocalPosition().y, 0f ,problems_equipment4);
                    TextView txt_name4 = (TextView) problems_equipment4.getView();
                    txt_name4.setText(idnomes.get(i));
                    txt_name4.setOnClickListener(view -> {addTable(anchorNode,lamp,idnomes.get(3));j++;});
                }
                if(i==4){
                    j++;
                    setNode(anchorNode, model, 0.4f, anchorNode.getChildren().get(i).getLocalPosition().y, 0f ,problems_equipment5);
                    TextView txt_name5 = (TextView) problems_equipment5.getView();
                    txt_name5.setText(idnomes.get(i));
                    txt_name5.setOnClickListener(view -> {addTable(anchorNode,lamp,idnomes.get(4));j++;});
                }

            }
        }
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    //
    // The params are dummy and not used
    //
    class LoadDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {

            try {
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return "Task Completed.";
        }
        @Override
        protected void onPostExecute(String result) {
            mProgressBar.hide();
           // loadData("pressure");
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
