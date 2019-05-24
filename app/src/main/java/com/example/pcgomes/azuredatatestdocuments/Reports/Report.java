package com.example.pcgomes.azuredatatestdocuments.Reports;

import com.azure.data.model.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Report extends Document implements  java.io.Serializable {

    private String type;
    private String machine;
    private String idMachine;
    private String problems;
    private String description;
    private String date;
    private String technician ;
    private ArrayList<String> pushListNamesProject = new ArrayList<>();
    private ArrayList<Report> pushListAllProject = new ArrayList<>();

    public Report(ArrayList<String> listProblemsSolved, String problemDescription, String machineName,String id,
                  String description,String date,String typeReport){

         this.type = typeReport;
         this.idMachine = id;
         this.problems = problems(listProblemsSolved);
         this.description = description;
         this.date = date;
         this.technician = "Pedro Gomes";


    }
    public String problems(ArrayList<String> listProblems){

        StringBuilder b = new StringBuilder();
        for (String h : listProblems) {

            b.append(h);
            b.append(" ");
        }
        return b.toString();
    }
    public String gettypeReport(){
        return type;
    }
    public String getdate(){
        return date;
    }
    public String getDescription(){
        return description;
    }
    public String getid(){
        return idMachine;
    }
    public String getMachineName(){
        return machine;
    }
    public String getProblemsDescription(){
        return problems;
    }
    public ArrayList<String> pushNamesProject(String name){
        pushListNamesProject.add(name);
        return pushListNamesProject;
    }
    public void pushAllReports(Report r){
        pushListAllProject.add(r);
    }
}
