package com.wfcrc.analytics;

/**
 * Created by maria on 10/13/16.
 */
public interface Analytics {

    void sendPageView(String screenName);

    void sendEvent(String category, String action);

    void sendEvent(String category, String action , String label);
}
