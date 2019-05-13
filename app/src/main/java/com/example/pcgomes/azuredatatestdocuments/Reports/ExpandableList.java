package com.example.pcgomes.azuredatatestdocuments.Reports;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.example.pcgomes.azuredatatestdocuments.R;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableList extends AppCompatActivity {
    private static ProgressDialog dialog;
    static SparseArray<Group> groups = new SparseArray<Group>();
    private ArrayList<Report> reports;
    private String s;
    private static final String TAG = "ExpandableList";
    private HashMap<String, ArrayList<Report>> hashReports;
    private ArrayList<Report> re;
    Point p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_documents);

        hashReports = new HashMap<String, ArrayList<Report>>();
        reports = new ArrayList<>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //_documentId = extras.getString("id_equipamento");
            reports = (ArrayList<Report>) extras.getSerializable("reports");

        }
        createHashMapReports(reports);
        ExpandableListView listView = (ExpandableListView)findViewById(R.id.listView);
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(ExpandableList.this, groups);
        listView.setAdapter(adapter);

    }
    public void createHashMapReports(ArrayList<Report> reports){
        int z =0;

        for(int i = 0; i < reports.size() ; i++) {
            if (!hashReports.containsKey(reports.get(i).gettypeReport())) {
                re = new ArrayList<Report>();
                re.add(reports.get(i));
                hashReports.put(reports.get(i).gettypeReport(), re);
            }else{
                ArrayList<Report> h = hashReports.get(reports.get(i).gettypeReport());
                h.add(reports.get(i));
                hashReports.put(reports.get(i).gettypeReport(), h);
            }
        }
        createData(hashReports.size(),hashReports, 4);
    }
    public static void createData(int nproblems,HashMap<String, ArrayList<Report>> re,int reports) {
        for (int j = 0; j < nproblems; j++) {
            Group group = new Group(re.keySet().toArray()[j].toString());
            for (Report xs : re.get(re.keySet().toArray()[j].toString())) {
                //group.children.add("Sub Item" + i);
                group.children.add(xs.getId());
                group.report.add(xs);

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
