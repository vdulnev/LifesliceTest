package com.example.user.lifeslicetest;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use adapter
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to adapter
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private VinoIntf srv;
    private List<Record> records = new ArrayList<>();
    private VideoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        srv = new VinoImpl().getService();
        adapter = new VideoListAdapter(this, -1, records);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return adapter fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify adapter parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        records.clear();
        loadRecords(0);
    }

    private void loadRecords(final int page) {
        Callback<com.example.user.lifeslicetest.Response> callback = new Callback<com.example.user.lifeslicetest.Response>() {
            @Override
            public void onResponse(Call<com.example.user.lifeslicetest.Response> call, Response<com.example.user.lifeslicetest.Response> response) {
                com.example.user.lifeslicetest.Response resp = response.body();
                Records data = resp.getData();
                if (data != null) {
                    Log.d(TAG, "Got records: " + data.getSize());
                    Log.d(TAG, "Overall count: " + data.getCount());
                    Log.d(TAG, "Next page: " + data.getNextPage());
                    Record[] dataRecords = data.getRecords();
                    if (dataRecords != null) {
                        for (Record r : dataRecords) {
                            records.add(r);
                            adapter.notifyDataSetChanged();
                        }
                        Log.d(TAG, "Added records: " + dataRecords.length);
                        Log.d(TAG, "Overall number of records: " + records.size());
                    }
                    int nextPage = page + 1;
                    if (data.getNextPage() != null) loadRecords(nextPage);
                } else {
                    Log.d(TAG, "No records received");
                }
            }

            @Override
            public void onFailure(Call<com.example.user.lifeslicetest.Response> call, Throwable t) {
                Log.e(TAG, "Load videos failed.");
            }
        };
        getData(page, callback);
    }

    private void getData(int page, Callback<com.example.user.lifeslicetest.Response> callback) {
        srv.get(page).enqueue(callback);
    }

    /**
     * A placeholder fragment containing adapter simple view.
     */
    public static class FirstFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public FirstFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_first, container, false);
            return rootView;
        }
    }

    public static class SecondFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private MainActivity parent;
        private VideoView vv;
        private ListView l;

        public SecondFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_second, container, false);
            vv = (VideoView) rootView.findViewById(R.id.videoView);
            vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    int pos = parent.adapter.getSelectedItem();
                    if (pos > -1)
                        if (pos < parent.adapter.getCount() - 1) {
                            startVideo(pos + 1);
                        } else {
                            stopVideo();
                        }
                }
            });
            l = (ListView) rootView.findViewById(R.id.listView);
            parent = (MainActivity) getActivity();
            l.setAdapter(parent.adapter);
            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (!vv.isPlaying()) {
                        startVideo(i);
                    } else {
                        stopVideo();
                    }
                }
            });
            return rootView;
        }

        private void stopVideo() {
            setPosition(-1);
            vv.stopPlayback();
        }

        private void startVideo(int pos) {
            setPosition(pos);
            Record r = parent.records.get(pos);
            vv.setVideoURI(Uri.parse(r.getVideoUrl()));
            vv.start();
        }

        private void setPosition(int pos) {
            parent.adapter.setSelectedItem(pos);
            if (pos > -1) {
                l.smoothScrollToPosition(pos);
            }
            parent.adapter.notifyDataSetChanged();
            Log.d("lifeslicetest", "notifyDataSetChanged");
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns adapter fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FirstFragment();
                case 1:
                    return new SecondFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
            }
            return null;
        }
    }
}
