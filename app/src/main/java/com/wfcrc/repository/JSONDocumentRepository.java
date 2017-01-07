package com.wfcrc.repository;

import com.wfcrc.R;
import com.wfcrc.pojos.Document;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maria on 11/10/16.
 */
public class JSONDocumentRepository {

    public static List<Document> getAll(String jsonString) throws RepositoryException {
        ArrayList<Document> documents = new ArrayList<Document>();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject documentJson;
            documents = new ArrayList<Document>(jsonArray.length());
            // Process each result in json array, decode and convert to business object
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    documentJson = jsonArray.getJSONObject(i);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }

                Document document = Document.fromJson(documentJson);
                if (document != null) {
                    documents.add(document);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RepositoryException(e.getMessage(), e.getCause());
        }
        return documents;
    }
}
