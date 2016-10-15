package com.wfcrc.analytics;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by maria on 10/13/16.
 */
public class GAImp implements Analytics{

    private static final String GA_TRACKER_ID = "UA-85734842-1";//FIXME

    private Tracker mTracker;

    public GAImp(Application application){
        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        //AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = getDefaultTracker(application);
        // [END shared_tracker]
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker(Application application) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(application);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(GA_TRACKER_ID);
        }
        return mTracker;
    }


    @Override
    public void sendPageView(String screenName) {
        if (screenName != null) {
            mTracker.setScreenName(screenName);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        }
    }

    @Override
    public void sendEvent(String category, String action , String label, long value) {
        if(action != null && category!= null){//at least you need to specify the action and the category
            HitBuilders.EventBuilder eventBuilder = new HitBuilders.EventBuilder(action, category);
            if(label != null) {
                eventBuilder.setLabel(label);
                eventBuilder.setValue(value);
            }
            mTracker.send(eventBuilder.build());
        }
    }
}
