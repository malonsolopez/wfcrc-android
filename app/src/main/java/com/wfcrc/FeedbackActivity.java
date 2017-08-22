package com.wfcrc;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wfcrc.utils.FormUtils;

public class FeedbackActivity extends AppCompatActivity {

    public static final int PICKFILE_REQUEST_CODE = 1;
    public static final int SENDEMAIL_REQUEST_CODE = 2;

    private FeedbackForm feedbackForm = new FeedbackForm();

    MenuItem attachButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.feedback_title);
        setContentView(R.layout.activity_feedback);
        //type of coral spinner
        //TODO: multiple selection
        /*Spinner spinner = (Spinner) findViewById(R.id.spinnerAbundantCoral);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.type_coral_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);*/
        //remove attached cv button
        ((ImageButton)findViewById(R.id.removeAttachmentButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show attach button
                attachButton.setVisible(true);
                //hide attached cv
                findViewById(R.id.attachmentLayout).setVisibility(View.GONE);
                //clear old cv
                feedbackForm.attachment = null;
            }
        });
        //toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.feedbackToolbar);
        myToolbar.setNavigationIcon(R.drawable.ic_clear_white_24dp);
        setSupportActionBar(myToolbar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICKFILE_REQUEST_CODE && data != null /* && resultCode == RESULT_OK*/) {
            //hide attach button
            attachButton.setVisible(false);
            //show attached cv
            findViewById(R.id.attachmentLayout).setVisibility(View.VISIBLE);
            feedbackForm.attachment = data.getDataString();
            ((TextView) findViewById(R.id.attachmentTextView)).setText(feedbackForm.attachment);
        }else if(requestCode == SENDEMAIL_REQUEST_CODE){
            //NOTE THAT WE CANNOT VERIFY IF SENDING WENT WELL BECAUSE ANDROID ALWAYS RETURNS 0 AS RESULT CODE
            Toast.makeText(this, getString(R.string.volunteer_end), Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.feedback_menu, menu);
        attachButton = menu.findItem(R.id.attachFeedback);
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
                        FormUtils.sendFormByEmail(this, getString(R.string.volunteer_address), getString(R.string.feedback_email_subject), feedbackForm.toString(), feedbackForm.attachment,
                                SENDEMAIL_REQUEST_CODE);
                    } catch (ActivityNotFoundException e) {
                        //TODO
                        e.printStackTrace();
                    }
                }
                return true;
            case R.id.attachFeedback:
                FormUtils.attachFile(this, PICKFILE_REQUEST_CODE);
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
        feedbackForm.name = ((EditText)findViewById(R.id.editTextName)).getText().toString();
        feedbackForm.lastName = ((EditText)findViewById(R.id.editTextLastName)).getText().toString();
        feedbackForm.phone = ((EditText)findViewById(R.id.editTextPhone)).getText().toString();
        feedbackForm.email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString();
        feedbackForm.comments = ((TextInputEditText)findViewById(R.id.editTextComments)).getText().toString();
        //verify that the it' filled
        if(feedbackForm.name.isEmpty()){
            ((EditText)findViewById(R.id.editTextName)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        if(feedbackForm.lastName.isEmpty()){
            ((EditText)findViewById(R.id.editTextLastName)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        if(feedbackForm.phone.isEmpty()){
            ((EditText)findViewById(R.id.editTextPhone)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        if(feedbackForm.email.isEmpty()){
            ((EditText)findViewById(R.id.editTextEmail)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }

        /*if(feedbackForm.cv == null){
            ((Button)findViewById(R.id.attachCVButton)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }*/

        if(feedbackForm.comments.isEmpty()){
            ((TextInputEditText)findViewById(R.id.editTextComments)).setError(getString(R.string.not_filled));
            isComplete =  false;
        }
        return isComplete;
    }

    private class FeedbackForm {

        private static final String COLON = ": ";
        private static final String NEW_LINE = "\n";

        private String name;
        private String lastName;
        private String phone;
        private String email;
        private String comments;
        private String attachment = null;

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
            //email
            volunteer.append(getString(R.string.email));
            volunteer.append(COLON);
            volunteer.append(email);
            volunteer.append(NEW_LINE);
            //comments
            volunteer.append(getString(R.string.motivation));
            volunteer.append(COLON);
            volunteer.append(comments);
            volunteer.append(NEW_LINE);
            return volunteer.toString();
        }
    }
}
