package com.example.pcgomes.azuredatatestdocuments.Reports;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.azure.data.AzureData;
import com.azure.data.model.DictionaryDocument;
import com.azure.data.model.Document;
import com.azure.data.model.Query;
import com.example.pcgomes.azuredatatestdocuments.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.azure.data.util.FunctionalUtils.onCallback;

public class Get_Field_Report_Service extends AppCompatActivity {

    private static final String TAG = "Get_Field_Report";
    private ArrayList<Report> reportList;
    private ArrayList<String> reportproblemsList;
    private ProgressDialog dialog;
    Point p;
    private Intent expandableIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ar_fragment);



        //getQuery(getApplication());
    }

}
