package com.wfcrc;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfcrc.analytics.Analytics;
import com.wfcrc.config.AppConfig;
import com.wfcrc.pojos.Program;
import com.wfcrc.social.Share;

public class ProgramDetailActivity extends AppCompatActivity {

    private Program mProgram;

    private Analytics mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.program_detail);
        this.setTitle(null);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mProgram = (Program) getIntent().getSerializableExtra("Program");
        initProgramDetailView();
        //GA
        mTracker = ((AppConfig)getApplication()).getAnalytics();
        mTracker.sendPageView(getString(R.string.ga_programs_detail));
        //donate floating button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.donate);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent donateIntent = new Intent(ProgramDetailActivity.this, DonateActivity.class);
                startActivity(donateIntent);

            }
        });
    }

    private void initProgramDetailView(){
        int imageId = getResources().getIdentifier(mProgram.getImageId(), "drawable", getPackageName());
        /*if(imageId == 0)
            findViewById(R.id.programImage).setVisibility(ImageView.GONE);
        else
            ((ImageView)findViewById(R.id.programImage)).setImageResource(imageId);*/
        if(imageId != 0)
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
            //GA
            mTracker.sendEvent(getString(R.string.ga_category_share), getString(R.string.ga_programs_detail_share), mProgram.getTitle());
        }

        return super.onOptionsItemSelected(item);
    }

}
