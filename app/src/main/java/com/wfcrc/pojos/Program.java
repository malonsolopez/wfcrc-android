package com.wfcrc.pojos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by maria on 8/29/16.
 */
public class Program implements Serializable{

    private String title;
    private String subtitle;
    private String description;
    private String imageId;

    public Program(){

    }

    public Program(String title, String subtitle, String description, String imageId) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    // Decodes program json into program model object
    public static Program fromJson(JSONObject jsonObject) {
        Program program = new Program();
        // Deserialize json into object fields
        try {
            program.title = jsonObject.getString("title");
            program.subtitle = jsonObject.getString("subtitle");
            program.description = jsonObject.getString("description");
            program.imageId = jsonObject.getString("imageId");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return program;
    }

    // Decodes array of program json results into program model objects
    public static ArrayList<Program> fromJson(String strJson) throws JSONException {
        JSONObject  jsonRootObject = new JSONObject(strJson);
        //Get the instance of JSONArray that contains JSONObjects
        JSONArray jsonArray = jsonRootObject.optJSONArray("program");
        JSONObject programJson;
        ArrayList<Program> programs = new ArrayList<Program>(jsonArray.length());
        // Process each result in json array, decode and convert to business object
        for (int i=0; i < jsonArray.length(); i++) {
            try {
                programJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Program program = Program.fromJson(programJson);
            if (program != null) {
                programs.add(program);
            }
        }
        return programs;
    }
}
