package com.tournamate.tournamate;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Christian on 26-04-2015.
 */
@ParseClassName("Tournaments")
public class Tournament extends ParseObject {

    private String name;
    private Boolean isDone;
    private String winner;
    private String createdAt;
    private String objectID;
    private long objectID_sql;

    public void Tournament_parse(){
    }

    public String getName(){return this.name;}
    public void setName(String value){this.name = value;}
    public String getWinner(){return this.winner;}
    public void setWinner(String value){this.winner = value;}
    public Boolean getIsDone(){return this.isDone;}
    public void setIsDone(Boolean value){this.isDone = value;}
    public void setCreatedAt(String value){this.createdAt = value;}
    public void setObjectID(String value){this.objectID = value;}
    public String getObjectIdLocal(){return objectID;}
    public String getCreatedAtString() {
        return createdAt;
    }

    // SQL

    public long getObjectID_sql() {
        return objectID_sql;
    }
    public void setObjectID_sql(long objectID_sql) {
        this.objectID_sql = objectID_sql;
    }

    //PARSE

    public String getNameParse() {
        return getString("Name");
    }
    public void putIsDone(boolean value) {
        put("isDone", value);
    }
    public Boolean getIsDoneParse() {
        return getBoolean("isDone");
    }
    public void putName(String value) {
        put("Name", value);
    }
    public String getWinnerParse() {
        return getString("Winner");
    }
    public void putWinner(String value) {
        put("Winner", value);
    }
    public String getDateParse() {
        return getCreatedAt().toString();
    }
    public String getObjectIdParse(){return getObjectId();};





}
