package com.example.pcgomes.azuredatatestdocuments.Augmented_Reality;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.azure.data.AzureData;
import com.example.pcgomes.azuredatatestdocuments.MainActivity;
import com.example.pcgomes.azuredatatestdocuments.R;
import com.example.pcgomes.azuredatatestdocuments.Reports.Field_Report_Service;
import com.example.pcgomes.azuredatatestdocuments.Reports.Report;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import static com.azure.data.util.FunctionalUtils.onCallback;

public class AugmentedReality extends AppCompatActivity {

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;
    private ArrayList<String> idnomes;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;

    ArFragment arFragment;
    ModelRenderable lampPostRenderable;
    ModelRenderable arduinoPostRenderable;

    ViewRenderable animal_name;
    ViewRenderable alert_view;
    TransformableNode lamp;
    ArrayList<ViewRenderable> viewlistRenderable;
    ViewRenderable resolvido_label1;
    ViewRenderable resolvido_label2;
    ViewRenderable resolvido_label3;
    //preencher a tabela
    private String _documentId;
    private ArrayList<String> datasAlertas;
    private HashMap<String, ArrayList<String>> listaValuesSensor;
    private ArrayList<String> listproblemsSolved;

    private int j = 0;
    private ViewRenderable alert_view_problem1;
    private ViewRenderable alert_view_problem2;
    private ViewRenderable alert_view_problem3;
    private ViewRenderable correct_image1;
    private ViewRenderable correct_image2;
    private ViewRenderable correct_image3;
    private String idMachine;
    private Button reportButton;
    private ViewRenderable instrutions_label;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView collectionIdTextView = findViewById(R.id.titleDocument);
        idnomes = new ArrayList<String>();
        listproblemsSolved = new ArrayList<String>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //_documentId = extras.getString("id_equipamento");
            listaValuesSensor = (HashMap<String, ArrayList<String>>) extras.getSerializable("listaValuesSensor");
            datasAlertas = (ArrayList<String>) extras.getSerializable("dataValues");
            idMachine = extras.getString("idmachine");
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

        //////////////////////Renderable da resolução problemas/////////////////////////
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> resolvido_label1 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> resolvido_label2 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> resolvido_label3 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_name_equipment)
                .build().thenAccept(renderable -> instrutions_label = renderable );
        //////////////////////Renderable da resolução problemas/////////////////////////


        /////////////////////////////////////////////////////////////////////////
        ////////////////////ALERT PROBLEM///////////////////////////////////////
        ViewRenderable.builder()
                .setView(this, R.layout.ar_alert_equipment)
                .build().thenAccept(renderable -> alert_view = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_alert_equipment)
                .build().thenAccept(renderable -> alert_view_problem1 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_alert_equipment)
                .build().thenAccept(renderable -> alert_view_problem2 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_alert_equipment)
                .build().thenAccept(renderable -> alert_view_problem3 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_problem_solved)
                .build().thenAccept(renderable -> correct_image1 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_problem_solved)
                .build().thenAccept(renderable -> correct_image2 = renderable );
        ViewRenderable.builder()
                .setView(this, R.layout.ar_problem_solved)
                .build().thenAccept(renderable -> correct_image3 = renderable );
        /////////////////////////////////////////////////////////////////////////
        ModelRenderable.builder()
                .setSource(this, Uri.parse("machineCoffeeSmalLabelobj.sfb"))
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
                    addAlert(anchorNode, lamp);

                }
        );
        ////////////////////////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////Generate Report////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////////////////
        reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(view -> {//generateReport(listproblemsSolved,idMachine);
        Toast.makeText(getBaseContext(),"Send report to DataBase",
                Toast.LENGTH_SHORT).show();Intent n = new Intent(getApplicationContext(),Field_Report_Service.class);
                n.putExtra("idmachine",idMachine);n.putExtra("listproblemssolved",listproblemsSolved);
                n.putExtra("date",getDate());startActivity(n);});
    }

    public String getDate(){
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String todayString = formatter.format(todayDate);
        return todayString;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addAlert(AnchorNode anchorNode, TransformableNode model) {
        //setNode(anchorNode, model, 0,anchorNode.getChildren().get(0).getLocalPosition().y + 1.0f,0, alert_view);

                addProblems(anchorNode, lamp,"lamp");

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addTable(AnchorNode anchorNode, TransformableNode model, String name) {

        setNode(anchorNode, model, 0,anchorNode.getChildren().get(2).getLocalPosition().y + 0.5f, 0 ,animal_name);

        mTableLayout = (TableLayout) animal_name.getView();
        mTableLayout.setStretchAllColumns(true);
        TableProblems t = new TableProblems();
        t.populateTable(this,mTableLayout,datasAlertas,listaValuesSensor,name);

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

        if (!idnomes.isEmpty()) {
            for (int i = 0; i < idnomes.size(); i++) {
                if (i == 0) {
                    setNode(anchorNode, model, 0f
                            , 0.5f, 0.35f, alert_view_problem1);
                    ImageView img1 = (ImageView) alert_view_problem1.getView();
                    img1.setOnClickListener(view -> {
                        addTable(anchorNode, lamp, idnomes.get(0));
                        j++;
                    });

                    setNode(anchorNode, model, anchorNode.getChildren().get(1).getLocalPosition().x - 0.1f
                            , anchorNode.getChildren().get(1).getLocalPosition().y - 0.05f,
                            0.35f, resolvido_label1);
                    TextView t1 = (TextView) resolvido_label1.getView();
                    t1.setOnClickListener(view -> {
                        Toast.makeText(getBaseContext(),"Problem Solved",
                                Toast.LENGTH_SHORT).show();
                        listproblemsSolved.add(idnomes.get(1));
                        changeImage(anchorNode,1,0f
                                , 0.5f, 0.35f, correct_image1);
                        correct_image1.getView();

                    });

                    //////////////////////Instruções//////////////////////
                    setNode(anchorNode, model, anchorNode.getChildren().get(1).getLocalPosition().x + 0.1f
                            , anchorNode.getChildren().get(1).getLocalPosition().y - 0.05f,
                            0.35f, instrutions_label);

                    TextView t2 = (TextView) instrutions_label.getView();
                    t2.setText("Instruções");
                }////////////////////////////////////////////////////////////
                if (i == 1) {
                    setNode(anchorNode, model, 0f
                            , 0.3f, 0.35f, alert_view_problem2);


                    ImageView img0 = (ImageView) alert_view_problem2.getView();


                    img0.setOnClickListener(view -> {
                        addTable(anchorNode, lamp, idnomes.get(1));
                        j++;
                    });

                    setNode(anchorNode, model, anchorNode.getChildren().get(4).getLocalPosition().x - 0.1f
                            , anchorNode.getChildren().get(4).getLocalPosition().y - 0.05f,
                            0.35f, resolvido_label2);
                    TextView t5 = (TextView) resolvido_label2.getView();
                    t5.setOnClickListener(view -> {
                        Toast.makeText(getBaseContext(),"Problem Solved",
                                Toast.LENGTH_SHORT).show();
                        listproblemsSolved.add(idnomes.get(0));

                        //////changeImage////////
                        changeImage(anchorNode,4,0f
                                , 0.3f, 0.35f, correct_image2);
                        correct_image2.getView();

                    });
                    setNode(anchorNode, model, anchorNode.getChildren().get(4).getLocalPosition().x + 0.1f
                            , anchorNode.getChildren().get(4).getLocalPosition().y - 0.05f,
                            0.35f, instrutions_label);

                    TextView t2 = (TextView) instrutions_label.getView();
                    t2.setText("Instruções");
                }////////////////////////////////////////////////////////////
                if (i == 2) {
                    j++;
                    setNode(anchorNode, model, 0f
                            , 0.1f, 0.35f, alert_view_problem3);
                    ImageView img2 = (ImageView) alert_view_problem3.getView();
                    img2.setOnClickListener(view -> {
                        addTable(anchorNode, lamp, idnomes.get(2));
                        j++;
                    });
                    //generate a report
                    setNode(anchorNode, model, anchorNode.getChildren().get(7).getLocalPosition().x - 0.1f
                            , anchorNode.getChildren().get(7).getLocalPosition().y - 0.05f,
                            0.35f, resolvido_label3);
                    TextView t4 = (TextView) resolvido_label3.getView();
                    t4.setOnClickListener(view -> {
                        Toast.makeText(getBaseContext(),"Problem Solved",
                                Toast.LENGTH_SHORT).show();
                        listproblemsSolved.add(idnomes.get(2));
                        changeImage(anchorNode,7,0f
                                , 0.1f, 0.35f,correct_image3);
                        correct_image3.getView();

                    });

                    setNode(anchorNode, model, anchorNode.getChildren().get(7).getLocalPosition().x + 0.1f
                            , anchorNode.getChildren().get(7).getLocalPosition().y - 0.05f,
                            0.35f, instrutions_label);


                    TextView t2 = (TextView) instrutions_label.getView();
                    t2.setText("Instruções");



                    if (i == 3) {
                        j++;

                    }
                    if (i == 4) {
                        j++;

                    }

                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void changeImage(AnchorNode anchorNode, int nalert, float x, float y, float z, ViewRenderable correct_image) {

        setNode(anchorNode, lamp, x,y,z , correct_image);
        anchorNode.getChildren().get(nalert).setEnabled(false);
        //anchorNode.removeChild(anchorNode.getChildren().get(nalert));

        Toast.makeText(getBaseContext(),"Actualizadooooooo",
                Toast.LENGTH_SHORT).show();
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
