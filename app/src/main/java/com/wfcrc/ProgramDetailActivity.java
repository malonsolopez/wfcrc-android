package com.wfcrc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.myapplication.R;
import com.wfcrc.pojos.Program;
import com.wfcrc.social.Share;

public class ProgramDetailActivity extends AppCompatActivity {

    private Program mProgram;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_detail);
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        mProgram = (Program) getIntent().getSerializableExtra("Program");
        initProgramDetailView();
    }

    private void initProgramDetailView(){
        int imageId = getResources().getIdentifier(mProgram.getImageId(), "drawable", getPackageName());
        if(imageId == 0)
            findViewById(R.id.programImage).setVisibility(ImageView.GONE);
        else
            ((ImageView)findViewById(R.id.programImage)).setImageResource(imageId);
        ((TextView)findViewById(R.id.programTitle)).setText(mProgram.getTitle());
        ((TextView)findViewById(R.id.programDescription)).setText(mProgram.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share) {
            //TODO: content for sharing
            String subject ="";
            String body ="";
            (new Share(this, subject, body)).share();
        }

        return super.onOptionsItemSelected(item);
    }

}
