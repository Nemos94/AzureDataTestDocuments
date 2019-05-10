package com.example.pcgomes.azuredatatestdocuments.Reports;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.azure.data.AzureData;
import com.azure.data.model.DictionaryDocument;
import com.azure.data.model.Document;
import com.azure.data.model.Query;
import com.example.pcgomes.azuredatatestdocuments.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static com.azure.data.util.FunctionalUtils.onCallback;

public class ExpandableList extends AppCompatActivity {
    private static ProgressDialog dialog;
    static SparseArray<Group> groups = new SparseArray<Group>();
    private ArrayList<Report> reports;
    private String s;
    private static final String TAG = "ExpandableList";


    Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_documents);

        reports = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //_documentId = extras.getString("id_equipamento");
            reports = (ArrayList<Report>) extras.getSerializable("reports");

        }
        createData(4, reports.size());
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.listView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(ExpandableList.this, groups);
        listView.setAdapter(adapter);

    }

    public static void createData(int nproblems, int reports) {
        for (int j = 0; j < 4; j++) {
            Group group = new Group("Test " + j);
            for (int i = 0; i < reports ; i++) {
                group.children.add("Sub Item" + i);
            }
            groups.append(j, group);
        }
    }




    public void createPopup(Context c, Button getreportButton,Activity y) throws InterruptedException {


        onWindowFocusChanged(true,getreportButton);

        //Open popup window
        if (p != null && reports != null)
            showPopup(c, y, p);

    }
    // Get the x and y position after the button is draw on screen
// (It's important to note that we can't get the position in the onCreate(),
// because at that stage most probably the view isn't drawn yet, so it will return (0, 0))

    public void onWindowFocusChanged(boolean hasFocus, Button getreportButton) {

        int[] location = new int[2];
        // Get the x, y location and store it in the location[] array
        // location[0] = x, location[1] = y.
        getreportButton.getLocationOnScreen(location);

        //Initialize the Point with x, and y positions
        p = new Point();
        p.x = location[0];
        p.y = location[1];

    }

    // The method that displays the popup.
    private void showPopup(Context context, Activity y,Point p) {
        int popupWidth = 400;
        int popupHeight = 400;

        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) y.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.popup, viewGroup);

        // Creating the PopupWindow
        final PopupWindow popup = new PopupWindow(context);
        popup.setContentView(layout);
        popup.setWidth(popupWidth);
        popup.setHeight(popupHeight);
        popup.setFocusable(false);

        // Some offset to align the popup a bit to the right, and a bit down, relative to button's position.
        int OFFSET_X = -30;
        int OFFSET_Y = 30;

        // Clear the default translucent background
        popup.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        popup.showAtLocation(layout, Gravity.CENTER
                , p.x + OFFSET_X, p.y + OFFSET_Y);

        new Handler().postDelayed(new Runnable(){
            public void run() {
                popup.dismiss();
            }
        }, 3 *1000);

    }

}
