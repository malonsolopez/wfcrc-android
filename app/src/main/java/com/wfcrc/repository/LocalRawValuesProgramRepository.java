package com.wfcrc.repository;

import android.content.Context;
import android.support.annotation.Nullable;

import com.wfcrc.R;
import com.wfcrc.pojos.Document;
import com.wfcrc.pojos.Program;
import com.wfcrc.utils.JSONUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maria on 10/6/16.
 */
public class LocalRawValuesProgramRepository implements Repository {

    private Context mContext;

    public LocalRawValuesProgramRepository(Context context){
        mContext = context;
    }

    @Override
    public List<Program> getAll()   throws RepositoryException{
        String strJson = JSONUtils.loadJSONFromAsset(mContext, R.raw.programs);
        ArrayList<Program> programs = new ArrayList<Program>();
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);
            //Get the instance of JSONArray that contains JSONObjects
            JSONArray jsonArray = jsonRootObject.optJSONArray("program");
            JSONObject programJson;
            programs = new ArrayList<Program>(jsonArray.length());
            // Process each result in json array, decode and convert to business object
            for (int i = 0; i < jsonArray.length(); i++) {
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
        }catch (Exception e){
            e.printStackTrace();
            throw new RepositoryException(e.getMessage(), e.getCause(), mContext.getString(R.string.programs_error));
        }
        return programs;
    }


}
