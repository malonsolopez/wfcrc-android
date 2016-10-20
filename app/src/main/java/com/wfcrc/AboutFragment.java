package com.wfcrc;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wfcrc.analytics.Analytics;
import com.wfcrc.config.AppConfig;
import com.wfcrc.social.FollowUsImp;
import com.wfcrc.social.Share;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AboutFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AboutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Analytics mTracker;

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View aboutView = inflater.inflate(R.layout.fragment_about, container, false);
        ((Button)aboutView.findViewById(R.id.followTWButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowUsImp(AboutFragment.this.getActivity()).follow(getString(R.string.follow_twitter));
                //GA
                mTracker.sendEvent(getString(R.string.ga_about_follow), getString(R.string.ga_about_TW));
            }
        });
        ((Button)aboutView.findViewById(R.id.followFBButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowUsImp(AboutFragment.this.getActivity()).follow(getString(R.string.follow_facebook));
                //GA
                mTracker.sendEvent(getString(R.string.ga_about_follow), getString(R.string.ga_about_FB));
            }
        });
        ((Button)aboutView.findViewById(R.id.followLIButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FollowUsImp(AboutFragment.this.getActivity()).follow(getString(R.string.follow_linkein));
                //GA
                mTracker.sendEvent(getString(R.string.ga_about_follow), getString(R.string.ga_about_LI));
            }
        });
        ((TextView)aboutView.findViewById(R.id.webLink)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.webURL)));
                startActivity(browserIntent);
                //GA
                mTracker.sendEvent(getString(R.string.ga_about_web), getString(R.string.ga_about_web));
            }
        });
        //custom toolbar with donate option for this fragment
        setHasOptionsMenu(true);
        //GA
        mTracker = ((AppConfig)getActivity().getApplication()).getAnalytics();
        mTracker.sendPageView(getString(R.string.ga_about));
        return aboutView;
    }

    //custom toolbar with share option for this fragment
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.about_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            (new Share(getActivity(), subject, body)).share();
            //GA
            mTracker.sendEvent(getString(R.string.ga_category_share), getString(R.string.ga_about_share));
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void donateButton(){
        //TODO
    }

}
