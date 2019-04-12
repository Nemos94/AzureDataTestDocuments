package com.example.pcgomes.azuredatatestdocuments;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class TableProblems extends AppCompatActivity {

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;

    public void populateTable(Context augmentedReality, TableLayout mTableLayoutaux, ArrayList<String> datasAlertas,
                              HashMap<String, ArrayList<String>> listaValuesSensor,String event) {

        this.mTableLayout = mTableLayoutaux;
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;
        textSize = (int) mTableLayout.getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) mTableLayout.getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) mTableLayout.getResources().getDimension(R.dimen.font_size_medium);

        Invoices invoices = new Invoices();
        //
        InvoiceData[] data = invoices.getInvoices(listaValuesSensor, datasAlertas,event);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        DecimalFormat decimalFormat = new DecimalFormat("0.00");



            int rows = data.length;
          // getSupportActionBar().setTitle("Invoices (" + String.valueOf(rows) + ")");
            TextView textSpacer = null;

            mTableLayout.removeAllViews();

            // -1 means heading row
            for (int i = -1; i < rows; i++) {
                InvoiceData row = null;
                if (i > -1)
                    row = data[i];
                else {
                    textSpacer = new TextView(augmentedReality);
                    textSpacer.setText("");
                }
                // data columns
                final TextView tv = new TextView(augmentedReality);
                tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));

                tv.setGravity(Gravity.CENTER);

                tv.setPadding(0, 15, 0, 15);
                if (i == -1) {
                    tv.setText("Nºoc.#");
                    tv.setBackgroundColor(Color.parseColor("#f0f0f0"));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv.setText(String.valueOf(row.id));
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }

                final TextView tv2 = new TextView(augmentedReality);
                if (i == -1) {
                    tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }

                tv2.setGravity(Gravity.CENTER);

                tv2.setPadding(5, 15, 0, 15);
                if (i == -1) {
                    tv2.setText("Date");
                    tv2.setBackgroundColor(Color.parseColor("#f7f7f7"));
                } else {
                    tv2.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv2.setTextColor(Color.parseColor("#000000"));
                    tv2.setText(row.invoiceDate.substring(0, 20));
                }
                final LinearLayout layCustomer = new LinearLayout(augmentedReality);
                layCustomer.setOrientation(LinearLayout.VERTICAL);
                layCustomer.setPadding(0, 10, 0, 10);
                layCustomer.setBackgroundColor(Color.parseColor("#f8f8f8"));

                final TextView tv3 = new TextView(augmentedReality);
                if (i == -1) {
                    tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv3.setPadding(5, 5, 0, 5);
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);


                } else {
                    tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv3.setPadding(5, 0, 0, 5);
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }

                tv3.setGravity(Gravity.TOP);


                if (i == -1) {
                    tv3.setText("Cause");
                    tv3.setBackgroundColor(Color.parseColor("#f0f0f0"));
                } else {
                    tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    tv3.setTextColor(Color.parseColor("#000000"));
                    tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);

                    tv3.setText(row.cause);
                }
                layCustomer.addView(tv3);


                if (i > -1) {
                    final TextView tv3b = new TextView(augmentedReality);
                    tv3b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    tv3b.setGravity(Gravity.CENTER);
                    tv3b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    tv3b.setPadding(5, 1, 0, 5);
                    tv3b.setTextColor(Color.parseColor("#aaaaaa"));
                    tv3b.setBackgroundColor(Color.parseColor("#f8f8f8"));
                    layCustomer.addView(tv3b);
                }

                final LinearLayout layAmounts = new LinearLayout(augmentedReality);
                layAmounts.setOrientation(LinearLayout.VERTICAL);
                layAmounts.setGravity(Gravity.RIGHT);
                layAmounts.setPadding(0, 10, 0, 10);
                layAmounts.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT));


                final TextView tv4 = new TextView(augmentedReality);
                if (i == -1) {
                    tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT));
                    tv4.setPadding(5, 5, 1, 5);
                    layAmounts.setBackgroundColor(Color.parseColor("#f7f7f7"));
                } else {
                    tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    tv4.setPadding(5, 0, 1, 5);
                    layAmounts.setBackgroundColor(Color.parseColor("#ffffff"));
                }

                tv4.setGravity(Gravity.RIGHT);

                if (i == -1) {
                    tv4.setText("Value");
                    tv4.setBackgroundColor(Color.parseColor("#f7f7f7"));
                    tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                } else {
                    tv4.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv4.setTextColor(Color.parseColor("#000000"));
                    tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                }

                layAmounts.addView(tv4);


                if (i > -1) {
                    final TextView tv4b = new TextView(augmentedReality);
                    tv4b.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));

                    tv4b.setGravity(Gravity.RIGHT);
                    tv4b.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    tv4b.setPadding(2, 2, 1, 5);
                    tv4b.setTextColor(Color.parseColor("#00afff"));
                    tv4b.setBackgroundColor(Color.parseColor("#ffffff"));
                    tv4.setText(row.valor);
                    layAmounts.addView(tv4b);
                }


                // add table row
                final TableRow tr = new TableRow(augmentedReality);
                tr.setId(i + 1);
                TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
                trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
                tr.setPadding(0, 0, 0, 0);
                tr.setLayoutParams(trParams);


                tr.addView(tv);
                tr.addView(tv2);
                tr.addView(layCustomer);
                tr.addView(layAmounts);

                if (i > -1) {

                    tr.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            TableRow tr = (TableRow) v;
                            //do whatever action is needed

                        }
                    });


                }
                mTableLayout.addView(tr, trParams);

                if (i > -1) {

                    // add separator row
                    final TableRow trSep = new TableRow(augmentedReality);
                    TableLayout.LayoutParams trParamsSep = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.WRAP_CONTENT);
                    trParamsSep.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);

                    trSep.setLayoutParams(trParamsSep);
                    TextView tvSep = new TextView(augmentedReality);
                    TableRow.LayoutParams tvSepLay = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT);
                    tvSepLay.span = 4;
                    tvSep.setLayoutParams(tvSepLay);
                    tvSep.setBackgroundColor(Color.parseColor("#d9d9d9"));
                    tvSep.setHeight(1);

                    trSep.addView(tvSep);
                    mTableLayout.addView(trSep, trParamsSep);
                }


            }
        }

}
