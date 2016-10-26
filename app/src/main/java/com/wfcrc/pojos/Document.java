package com.wfcrc.pojos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by maria on 8/29/16.
 */
public class Document implements Serializable{

    private String title;
    private String format;
    private String category;
    private String url;

    public Document(){

    }

    public Document(String title, String format, String category, String url) {
        this.title = title;
        this.format = format;
        this.category = category;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // Decodes program json into program model object
    public static Document fromJson(JSONObject jsonObject) {
        Document document = new Document();
        // Deserialize json into object fields
        try {
            document.title = jsonObject.getJSONObject("title").getString("rendered");
            document.format = jsonObject.getString("mime_type");
            document.category = jsonObject.getString("description");
            document.url = jsonObject.getString("source_url");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return document;
    }



    public static HashMap<String, ArrayList<Document>> sortDocuments(List<Document> documents){
        HashMap<String, ArrayList<Document>> result = new HashMap<String, ArrayList<Document>>();
        for (Document document: documents) {
            if(result.containsKey(document.category)){
                result.get(document.category).add(document);
            }else{
                ArrayList<Document> newEntry = new ArrayList<Document>();
                newEntry.add(document);
                result.put(document.category, newEntry);
            }
        }
        return result;
    }
}
