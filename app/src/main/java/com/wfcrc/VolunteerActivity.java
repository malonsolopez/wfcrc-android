package com.wfcrc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.wfcrc.utils.FormUtils;

public class VolunteerActivity extends AppCompatActivity {

    public static final int PICKFILE_REQUEST_CODE = 1;

    private String mCV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.volunteer_title);
        setContentView(R.layout.activity_volunteer);
        //skills spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinnerVolunteerSkill);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.skills_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        //attach cv button
        ((Button)findViewById(R.id.attachCVButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FormUtils.attachFile(VolunteerActivity.this, PICKFILE_REQUEST_CODE);
            }
        });
        //remove attached cv button
        ((ImageButton)findViewById(R.id.removeAttachedCVButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show attach button
                findViewById(R.id.attachCVButton).setVisibility(View.VISIBLE);
                //hide attached cv
                findViewById(R.id.cvLayout).setVisibility(View.GONE);
                //clear old cv
                mCV = null;
            }
        });
        //toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.volunteerToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICKFILE_REQUEST_CODE/* && resultCode == RESULT_OK*/){
            //hide attach button
            findViewById(R.id.attachCVButton).setVisibility(View.GONE);
            //show attached cv
            findViewById(R.id.cvLayout).setVisibility(View.VISIBLE);
            mCV = data.getDataString();
            ((TextView)findViewById(R.id.cvTextView)).setText(mCV);
        }
        //super.onActivityResult(requestCode, resultCode, data);
    }

    /*RIGHT MENU*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.volunteer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.sendFeedback:
                // TODO: retrieve data from form and make sure all fields are filled
                try {
                    FormUtils.sendFormByEmail(this, "subject", "body", null);
                } catch (ActivityNotFoundException e) {
                    //TODO
                    e.printStackTrace();
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
