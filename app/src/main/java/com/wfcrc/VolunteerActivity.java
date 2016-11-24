package com.wfcrc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.wfcrc.utils.FormUtils;

public class VolunteerActivity extends AppCompatActivity {

    public static final int PICKFILE_REQUEST_CODE = 1;
    public static final int SENDEMAIL_REQUEST_CODE = 2;

    private VolunteerForm volunteerForm = new VolunteerForm();

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
                //remove not attached error if necessary
                ((Button)view).setError(null);
                //attach
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
                volunteerForm.cv = null;
            }
        });
        //toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.volunteerToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICKFILE_REQUEST_CODE && data != null /* && resultCode == RESULT_OK*/) {
            //hide attach button
            findViewById(R.id.attachCVButton).setVisibility(View.GONE);
            //show attached cv
            findViewById(R.id.cvLayout).setVisibility(View.VISIBLE);
            volunteerForm.cv = data.getDataString();
            ((TextView) findViewById(R.id.cvTextView)).setText(volunteerForm.cv);
        }else if(requestCode == SENDEMAIL_REQUEST_CODE){
            //NOTE THAT WE CANNOT VERIFY IF SENDING WENT WELL BECAUSE ANDROID ALWAYS RETURNS 0 AS RESULT CODE
            Toast.makeText(this, "That's for your submission. We'll keep in touch.", Toast.LENGTH_LONG).show();
            this.finish();
        }else{
            //TODO
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
                if(retrieveDataFromForm()) {
                    try {
                        FormUtils.sendFormByEmail(this, getString(R.string.volunteer_address), "subject", volunteerForm.toString(), volunteerForm.cv,
                                SENDEMAIL_REQUEST_CODE);
                    } catch (ActivityNotFoundException e) {
                        //TODO
                        e.printStackTrace();
                    }
                }
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean retrieveDataFromForm(){
        boolean isComplete = true;
        //getData
        volunteerForm.name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
        volunteerForm.lastName = ((EditText)findViewById(R.id.editTextLastName)).getText().toString();
        volunteerForm.phone = ((EditText)findViewById(R.id.editTextPhone)).getText().toString();
        if (((RadioButton)findViewById(R.id.radioButtonVolunteer)).isChecked())
            volunteerForm.volunteerType = getString(R.string.volunteer);
        else
            volunteerForm.volunteerType = getString(R.string.ambassador);
        volunteerForm.skill = ((Spinner)findViewById(R.id.spinnerVolunteerSkill)).getSelectedItem().toString();
        volunteerForm.diver = ((CheckBox)findViewById(R.id.diverCheckBox)).isChecked();
        volunteerForm.motivation = ((TextInputEditText)findViewById(R.id.editTextComments)).getText().toString();
        //verify that the it' filled
        if(volunteerForm.name.isEmpty()){
            ((EditText)findViewById(R.id.editTextName)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        if(volunteerForm.lastName.isEmpty()){
            ((EditText)findViewById(R.id.editTextLastName)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        if(volunteerForm.phone.isEmpty()){
            ((EditText)findViewById(R.id.editTextPhone)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        if(volunteerForm.cv == null){
            ((Button)findViewById(R.id.attachCVButton)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        if(volunteerForm.motivation.isEmpty()){
            ((TextInputEditText)findViewById(R.id.editTextComments)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        return isComplete;
    }

    private class VolunteerForm{

        private static final String COLON = ": ";
        private static final String NEW_LINE = "\n";

        private String name;
        private String lastName;
        private String phone;
        //private String email;
        private String volunteerType;
        private String skill;
        private boolean diver;
        private String cv = null;
        private String motivation;

        @Override
        public String toString() {
            StringBuffer volunteer = new StringBuffer();
            //name
            volunteer.append(getString(R.string.name));
            volunteer.append(COLON);
            volunteer.append(name);
            volunteer.append(NEW_LINE);
            //last name
            volunteer.append(getString(R.string.last_name));
            volunteer.append(COLON);
            volunteer.append(lastName);
            volunteer.append(NEW_LINE);
            //phone
            volunteer.append(getString(R.string.phone));
            volunteer.append(COLON);
            volunteer.append(phone);
            volunteer.append(NEW_LINE);
            //volunteer type
            volunteer.append(getString(R.string.volunteer_type));
            volunteer.append(COLON);
            volunteer.append(volunteerType);
            volunteer.append(NEW_LINE);
            //skill
            volunteer.append(getString(R.string.skills));
            volunteer.append(COLON);
            volunteer.append(skill);
            volunteer.append(NEW_LINE);
            //diver
            volunteer.append(getString(R.string.diver));
            volunteer.append(COLON);
            volunteer.append(diver);
            volunteer.append(NEW_LINE);
            //comments
            volunteer.append(getString(R.string.motivation));
            volunteer.append(COLON);
            volunteer.append(motivation);
            volunteer.append(NEW_LINE);
            return volunteer.toString();
        }
    }
}
