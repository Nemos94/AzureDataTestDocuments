package com.example.pcgomes.azuredatatestdocuments.Reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Group {
    private HashMap<String, ArrayList<Report>> hashReports;
    public String string;
    public final List<String> children = new ArrayList<String>();
    public final List<Report> report = new ArrayList<Report>();
   // public final HashMap<String,Report> children = new HashMap<String,Report>();
    public Group(String string) {
        this.string = string;
        //this.report = report;
    }

}
