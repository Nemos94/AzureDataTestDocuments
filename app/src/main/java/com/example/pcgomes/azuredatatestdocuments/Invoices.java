package com.example.pcgomes.azuredatatestdocuments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Invoices {
    private int sizeNumberValuesList;
    private ArrayList<String> cause;
    private ArrayList<Integer> numberofValues;

    public InvoiceData[] getInvoices(HashMap<String, ArrayList<String>> map, ArrayList<String> _data) {

        sizeNumberValuesList = 0;
        cause = new ArrayList<>();
        numberofValues = new ArrayList<>();

        for (Map.Entry<String, ArrayList<String>> val : map.entrySet()) {
            sizeNumberValuesList += val.getValue().size();
            cause.add(val.getKey().toString());
            numberofValues.add(val.getValue().size());

        }
        InvoiceData[] data = new InvoiceData[sizeNumberValuesList];
        int j = 0;
        int z = 0;
        for (int i = 0; i < sizeNumberValuesList; i++) {
            if (z != numberofValues.get(j)) {
                InvoiceData row = new InvoiceData();
                row.id = (i + 1);
                row.cause = cause.get(j);
                row.valor = getSpecificValue(cause.get(j), z, map);
                row.invoiceDate = _data.get(i);
                z++;

                data[i] = row;
            } else {
                InvoiceData row = new InvoiceData();
                row.id = (i + 1);
                row.cause = cause.get(j + 1);
                z = 0;
                row.valor = getSpecificValue(cause.get(j + 1), z, map);
                row.invoiceDate = _data.get(i);
                z++;
                j++;
                data[i] = row;

                ;
            }
        }
        return data;

    }

    public String getSpecificValue(String key, int value, HashMap<String, ArrayList<String>> map) {

        Object l = map.get(key).get(value);
        return l.toString();
    }


}
