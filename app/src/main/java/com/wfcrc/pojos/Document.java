package com.wfcrc.pojos;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.DOMConfiguration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by maria on 8/29/16.
 */
public class Document implements Serializable, Parcelable{

    private int id;
    private String title;
    private String format;
    private String category;
    private String url;
    private boolean downloaded = false;

    public Document(){

    }

    public Document(int id, String title, String format, String category, String url, boolean downloaded) {
        this.id = id;
        this.title = title;
        this.format = format;
        this.category = category;
        this.url = url;
        this.downloaded = downloaded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    // Decodes program json into program model object
    public static Document fromJson(JSONObject jsonObject) {
        Document document = new Document();
        // Deserialize json into object fields
        try {
            document.id = jsonObject.getInt("id");
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

    //PARCELABLE IMPLEMENTATION

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeArray(new Object[] {this.id, this.title, this.format, this.category, this.url, this.downloaded});
    }

    public static final Parcelable.Creator<Document> CREATOR
            = new Parcelable.Creator<Document>() {
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        public Document[] newArray(int size) {
            return new Document[size];
        }
    };

    private Document(Parcel in) {
        Object[] data = in.readArray(Document.class.getClassLoader());
        this.id = Integer.parseInt(data[0].toString());
        this.title = data[1].toString();
        this.format = data[2].toString();
        this.category = data[3].toString();
        this.url = data[4].toString();
        this.downloaded = Boolean.parseBoolean(data[5].toString());
    }
}
