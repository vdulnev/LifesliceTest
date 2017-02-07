package com.example.user.lifeslicetest;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.gson.Gson;

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
    private String tag;
    private FirstFragment firstFragment;
    private SecondFragment secondFragment;
    private boolean stopLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setNavigationM
        // Create the adapter that will return adapter fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        stopLoading = true;
                        if (secondFragment != null) {
                            secondFragment.stopVideo();
                        }
                        break;
                    case 1:
                        stopLoading = false;
                        if (secondFragment != null) {
                            InputMethodManager imm = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(mViewPager.getWindowToken(), 0);
                            tag = firstFragment.et.getText().toString();
                            secondFragment.loadRecords(tag, 0);
                        }
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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

    /**
     * A placeholder fragment containing adapter simple view.
     */
    public static class FirstFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private EditText et;

        public FirstFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_first, container, false);
            final MainActivity parent = (MainActivity) getActivity();
            et = (EditText) rootView.findViewById(R.id.editText);
            et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (!b) {
                        parent.tag = et.getText().toString();
                        Log.d(TAG, "Focus lost, new tag: " + parent.tag);
                    } else {
                        Log.d(TAG, "Focus got");
                    }
                }
            });
            parent.tag = et.getText().toString();
            Button b = (Button) rootView.findViewById(R.id.button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parent.mViewPager.setCurrentItem(1);
                }
            });
            return rootView;
        }
    }

    public static class SecondFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        public static final String FAKE_JSON = "{\n" +
                "\t\"count\": 5,\n" +
                "\t\"records\": [{\n" +
                "\t\t\"avatarUrl\": \"http://vines.s3.amazonaws.com/avatars/FC220C82-1370-4340-887C-01254FBAFB7D-4274-000002E4D4C0B0D4.jpg\",\n" +
                "\t\t\"comments\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/D6360E08-0BF5-4750-A883-9F9F09F91E06-25016-0000191A7355BDF7.jpg\",\n" +
                "\t\t\t\t\"comment\": \"When pizza's on a bagel you can eat pizza anytime!\",\n" +
                "\t\t\t\t\"commentId\": 898291497017937920,\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:19:56.000000\",\n" +
                "\t\t\t\t\"location\": \"Brooklyn, NY\",\n" +
                "\t\t\t\t\"userId\": 110,\n" +
                "\t\t\t\t\"username\": \"Kristian Bauer\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"created\": \"2013-01-02T16:12:23.000000\",\n" +
                "\t\t\"description\": \"Pizza??? For breakfast?!?!\",\n" +
                "\t\t\"foursquareVenueId\": null,\n" +
                "\t\t\"latitude\": 40.70322799682617,\n" +
                "\t\t\"liked\": false,\n" +
                "\t\t\"likes\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/1194B685-5366-40A9-B36A-A3403AA35716-2999-000000D804A9A9AA.jpg\",\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:26:28.000000\",\n" +
                "\t\t\t\t\"likeId\": 898293141113806848,\n" +
                "\t\t\t\t\"location\": \"NYC\",\n" +
                "\t\t\t\t\"userId\": 5,\n" +
                "\t\t\t\t\"username\": \"Dom Hofmann\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"location\": \"NY USA\",\n" +
                "\t\t\"longitude\": -73.94598388671875,\n" +
                "\t\t\"postId\": 898289598352990208,\n" +
                "\t\t\"postToFacebook\": 0,\n" +
                "\t\t\"postToTwitter\": 0,\n" +
                "\t\t\"promoted\": 0,\n" +
                "\t\t\"tags\": null,\n" +
                "\t\t\"thumbnailUrl\": \"http://vines.s3.amazonaws.com/thumbs/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4.jpg\",\n" +
                "\t\t\"userId\": 108,\n" +
                "\t\t\"username\": \"Bobby McKenna\",\n" +
                "\t\t\"videoLowURL\": null,\n" +
                "\t\t\"videoUrl\": \"http://vines.s3.amazonaws.com/videos/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4\"\n" +
                "\t}, \n" +
                "\t{\n" +
                "\t\t\"avatarUrl\": \"http://vines.s3.amazonaws.com/avatars/FC220C82-1370-4340-887C-01254FBAFB7D-4274-000002E4D4C0B0D4.jpg\",\n" +
                "\t\t\"comments\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/D6360E08-0BF5-4750-A883-9F9F09F91E06-25016-0000191A7355BDF7.jpg\",\n" +
                "\t\t\t\t\"comment\": \"When pizza's on a bagel you can eat pizza anytime!\",\n" +
                "\t\t\t\t\"commentId\": 898291497017937920,\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:19:56.000000\",\n" +
                "\t\t\t\t\"location\": \"Brooklyn, NY\",\n" +
                "\t\t\t\t\"userId\": 110,\n" +
                "\t\t\t\t\"username\": \"Kristian Bauer\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"created\": \"2013-01-02T16:12:23.000000\",\n" +
                "\t\t\"description\": \"Pizza??? For breakfast?!?!\",\n" +
                "\t\t\"foursquareVenueId\": null,\n" +
                "\t\t\"latitude\": 40.70322799682617,\n" +
                "\t\t\"liked\": false,\n" +
                "\t\t\"likes\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/1194B685-5366-40A9-B36A-A3403AA35716-2999-000000D804A9A9AA.jpg\",\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:26:28.000000\",\n" +
                "\t\t\t\t\"likeId\": 898293141113806848,\n" +
                "\t\t\t\t\"location\": \"NYC\",\n" +
                "\t\t\t\t\"userId\": 5,\n" +
                "\t\t\t\t\"username\": \"Dom Hofmann\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"location\": \"NY USA\",\n" +
                "\t\t\"longitude\": -73.94598388671875,\n" +
                "\t\t\"postId\": 898289598352990208,\n" +
                "\t\t\"postToFacebook\": 0,\n" +
                "\t\t\"postToTwitter\": 0,\n" +
                "\t\t\"promoted\": 0,\n" +
                "\t\t\"tags\": null,\n" +
                "\t\t\"thumbnailUrl\": \"http://vines.s3.amazonaws.com/thumbs/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4.jpg\",\n" +
                "\t\t\"userId\": 108,\n" +
                "\t\t\"username\": \"Bobby McKenna\",\n" +
                "\t\t\"videoLowURL\": null,\n" +
                "\t\t\"videoUrl\": \"http://vines.s3.amazonaws.com/videos/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4\"\n" +
                "\t}, \n" +
                "\t{\n" +
                "\t\t\"avatarUrl\": \"http://vines.s3.amazonaws.com/avatars/FC220C82-1370-4340-887C-01254FBAFB7D-4274-000002E4D4C0B0D4.jpg\",\n" +
                "\t\t\"comments\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/D6360E08-0BF5-4750-A883-9F9F09F91E06-25016-0000191A7355BDF7.jpg\",\n" +
                "\t\t\t\t\"comment\": \"When pizza's on a bagel you can eat pizza anytime!\",\n" +
                "\t\t\t\t\"commentId\": 898291497017937920,\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:19:56.000000\",\n" +
                "\t\t\t\t\"location\": \"Brooklyn, NY\",\n" +
                "\t\t\t\t\"userId\": 110,\n" +
                "\t\t\t\t\"username\": \"Kristian Bauer\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"created\": \"2013-01-02T16:12:23.000000\",\n" +
                "\t\t\"description\": \"Pizza??? For breakfast?!?!\",\n" +
                "\t\t\"foursquareVenueId\": null,\n" +
                "\t\t\"latitude\": 40.70322799682617,\n" +
                "\t\t\"liked\": false,\n" +
                "\t\t\"likes\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/1194B685-5366-40A9-B36A-A3403AA35716-2999-000000D804A9A9AA.jpg\",\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:26:28.000000\",\n" +
                "\t\t\t\t\"likeId\": 898293141113806848,\n" +
                "\t\t\t\t\"location\": \"NYC\",\n" +
                "\t\t\t\t\"userId\": 5,\n" +
                "\t\t\t\t\"username\": \"Dom Hofmann\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"location\": \"NY USA\",\n" +
                "\t\t\"longitude\": -73.94598388671875,\n" +
                "\t\t\"postId\": 898289598352990208,\n" +
                "\t\t\"postToFacebook\": 0,\n" +
                "\t\t\"postToTwitter\": 0,\n" +
                "\t\t\"promoted\": 0,\n" +
                "\t\t\"tags\": null,\n" +
                "\t\t\"thumbnailUrl\": \"http://vines.s3.amazonaws.com/thumbs/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4.jpg\",\n" +
                "\t\t\"userId\": 108,\n" +
                "\t\t\"username\": \"Bobby McKenna\",\n" +
                "\t\t\"videoLowURL\": null,\n" +
                "\t\t\"videoUrl\": \"http://vines.s3.amazonaws.com/videos/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4\"\n" +
                "\t},\n" +
                "\t{\n" +
                "\t\t\"avatarUrl\": \"http://vines.s3.amazonaws.com/avatars/FC220C82-1370-4340-887C-01254FBAFB7D-4274-000002E4D4C0B0D4.jpg\",\n" +
                "\t\t\"comments\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/D6360E08-0BF5-4750-A883-9F9F09F91E06-25016-0000191A7355BDF7.jpg\",\n" +
                "\t\t\t\t\"comment\": \"When pizza's on a bagel you can eat pizza anytime!\",\n" +
                "\t\t\t\t\"commentId\": 898291497017937920,\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:19:56.000000\",\n" +
                "\t\t\t\t\"location\": \"Brooklyn, NY\",\n" +
                "\t\t\t\t\"userId\": 110,\n" +
                "\t\t\t\t\"username\": \"Kristian Bauer\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"created\": \"2013-01-02T16:12:23.000000\",\n" +
                "\t\t\"description\": \"Pizza??? For breakfast?!?!\",\n" +
                "\t\t\"foursquareVenueId\": null,\n" +
                "\t\t\"latitude\": 40.70322799682617,\n" +
                "\t\t\"liked\": false,\n" +
                "\t\t\"likes\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/1194B685-5366-40A9-B36A-A3403AA35716-2999-000000D804A9A9AA.jpg\",\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:26:28.000000\",\n" +
                "\t\t\t\t\"likeId\": 898293141113806848,\n" +
                "\t\t\t\t\"location\": \"NYC\",\n" +
                "\t\t\t\t\"userId\": 5,\n" +
                "\t\t\t\t\"username\": \"Dom Hofmann\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"location\": \"NY USA\",\n" +
                "\t\t\"longitude\": -73.94598388671875,\n" +
                "\t\t\"postId\": 898289598352990208,\n" +
                "\t\t\"postToFacebook\": 0,\n" +
                "\t\t\"postToTwitter\": 0,\n" +
                "\t\t\"promoted\": 0,\n" +
                "\t\t\"tags\": null,\n" +
                "\t\t\"thumbnailUrl\": \"http://vines.s3.amazonaws.com/thumbs/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4.jpg\",\n" +
                "\t\t\"userId\": 108,\n" +
                "\t\t\"username\": \"Bobby McKenna\",\n" +
                "\t\t\"videoLowURL\": null,\n" +
                "\t\t\"videoUrl\": \"http://vines.s3.amazonaws.com/videos/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4\"\n" +
                "\t}, \n" +
                "\t{\n" +
                "\t\t\"avatarUrl\": \"http://vines.s3.amazonaws.com/avatars/FC220C82-1370-4340-887C-01254FBAFB7D-4274-000002E4D4C0B0D4.jpg\",\n" +
                "\t\t\"comments\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/D6360E08-0BF5-4750-A883-9F9F09F91E06-25016-0000191A7355BDF7.jpg\",\n" +
                "\t\t\t\t\"comment\": \"When pizza's on a bagel you can eat pizza anytime!\",\n" +
                "\t\t\t\t\"commentId\": 898291497017937920,\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:19:56.000000\",\n" +
                "\t\t\t\t\"location\": \"Brooklyn, NY\",\n" +
                "\t\t\t\t\"userId\": 110,\n" +
                "\t\t\t\t\"username\": \"Kristian Bauer\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"created\": \"2013-01-02T16:12:23.000000\",\n" +
                "\t\t\"description\": \"Pizza??? For breakfast?!?!\",\n" +
                "\t\t\"foursquareVenueId\": null,\n" +
                "\t\t\"latitude\": 40.70322799682617,\n" +
                "\t\t\"liked\": false,\n" +
                "\t\t\"likes\": {\n" +
                "\t\t\t\"count\": 1,\n" +
                "\t\t\t\"nextPage\": null,\n" +
                "\t\t\t\"previousPage\": null,\n" +
                "\t\t\t\"records\": [{\n" +
                "\t\t\t\t\"avatarUrl\": \"https://vines.s3.amazonaws.com/avatars/1194B685-5366-40A9-B36A-A3403AA35716-2999-000000D804A9A9AA.jpg\",\n" +
                "\t\t\t\t\"created\": \"2013-01-02T16:26:28.000000\",\n" +
                "\t\t\t\t\"likeId\": 898293141113806848,\n" +
                "\t\t\t\t\"location\": \"NYC\",\n" +
                "\t\t\t\t\"userId\": 5,\n" +
                "\t\t\t\t\"username\": \"Dom Hofmann\"\n" +
                "\t\t\t}],\n" +
                "\t\t\t\"size\": 10\n" +
                "\t\t},\n" +
                "\t\t\"location\": \"NY USA\",\n" +
                "\t\t\"longitude\": -73.94598388671875,\n" +
                "\t\t\"postId\": 898289598352990208,\n" +
                "\t\t\"postToFacebook\": 0,\n" +
                "\t\t\"postToTwitter\": 0,\n" +
                "\t\t\"promoted\": 0,\n" +
                "\t\t\"tags\": null,\n" +
                "\t\t\"thumbnailUrl\": \"http://vines.s3.amazonaws.com/thumbs/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4.jpg\",\n" +
                "\t\t\"userId\": 108,\n" +
                "\t\t\"username\": \"Bobby McKenna\",\n" +
                "\t\t\"videoLowURL\": null,\n" +
                "\t\t\"videoUrl\": \"http://vines.s3.amazonaws.com/videos/04DAF1DE-189D-4A14-829B-84932099763E-3016-000001F8531FB4AA_0.9.1.mp4\"\n" +
                "\t}],\n" +
                "\t\"size\": 5\n" +
                "}";
        private List<Record> records = new ArrayList<>();
        private MainActivity parent;
        private VinoIntf srv;
        private VideoListAdapter adapter;
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
                    startNextVideo();
                }
            });
            vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                    Log.d(TAG, "onError");
                    startNextVideo();
                    return true;
                }
            });
            l = (ListView) rootView.findViewById(R.id.listView);
            parent = (MainActivity) getActivity();
            srv = new VinoImpl().getService();
            adapter = new VideoListAdapter(getActivity(), -1, records);
            l.setAdapter(adapter);
            l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapter.getSelectedItem() != i) {
                        if (!vv.isPlaying()) {
                            startVideo(i);
                        } else {
                            stopVideo();
                            startVideo(i);
                        }
                    } else {
                        stopVideo();
                    }
                }
            });
            return rootView;
        }

        private void startNextVideo() {
            int pos = adapter.getSelectedItem();
            if (pos > -1)
                if (pos < adapter.getCount() - 1) {
                    startVideo(pos + 1);
                } else {
                    stopVideo();
                }
        }

        private void loadRecords(final String tag, final int page) {
            if (tag != null && tag.trim().length() > 0) {
                if (!parent.stopLoading) {
                    if (page == 0) records.clear();
                    Callback<com.example.user.lifeslicetest.Records> callback = new Callback<com.example.user.lifeslicetest.Records>() {
                        @Override
                        public void onResponse(Call<com.example.user.lifeslicetest.Records> call, Response<com.example.user.lifeslicetest.Records> response) {
                            com.example.user.lifeslicetest.Records resp = response.body();
                            if (resp != null) {
                                Records data = resp;
                                loadData(data, page, tag, true);
                            } else {
                                String msg = "No data received used fake response!";
                                Log.e(TAG, msg);
                                Gson gson = new Gson();
                                Records data = gson.fromJson(FAKE_JSON, Records.class);
                                if (data != null) {
                                    loadData(data, page, tag, true);
                                } else {
                                    msg = "No data found!";
                                    Log.e(TAG, msg);
                                    if (parent != null) {
                                        Toast.makeText(parent, msg, Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<com.example.user.lifeslicetest.Records> call, Throwable t) {
                            Log.e(TAG, "Load videos failed: " + t.getLocalizedMessage());
                        }
                    };
                    getForTag(tag, page, callback);
                } else {
                    Log.d(TAG, "Load videos stopped");
                }
            } else {
                Log.e(TAG, "Load data failed: tag is empty");
            }
        }

        private void loadData(Records data, int page, String tag, boolean isFake) {
            int n = 0;
            if (data != null) {
                Log.d(TAG, "Got records: " + data.getSize());
                Log.d(TAG, "Overall count: " + data.getCount());
                Log.d(TAG, "Next page: " + data.getNextPage());
                Record[] dataRecords = data.getRecords();
                if (dataRecords != null) {
                    for (Record r : dataRecords) {
                        if (addRecord(r, isFake)) {
                            adapter.notifyDataSetChanged();
                            n++;
                        }
                    }
                    Log.d(TAG, "Added records: " + n);
                    Log.d(TAG, "Overall number of records: " + records.size());
                }
                int nextPage = page + 1;
                if (data.getNextPage() != null) loadRecords(tag, nextPage);
            } else {
                Log.d(TAG, "No records received");
            }
        }

        private boolean addRecord(Record r, boolean isFake) {
            if (!isFake) {
                for (Record r1 : records) {
                    if (r1.getVideoUrl() != null && r1.getVideoUrl().equals(r.getVideoUrl())) {
                        Log.e(TAG, "found duplicate record: " + r.getVideoUrl());
                        return false;
                    }
                }
            }
            records.add(r);
            return true;
        }

        private void getForTag(String tag, int page, Callback<com.example.user.lifeslicetest.Records> callback) {
            srv.getForTag(tag, page).enqueue(callback);
        }

        private void stopVideo() {
            setPosition(-1);
            vv.stopPlayback();
            vv.setVisibility(View.GONE);
            vv.setVisibility(View.VISIBLE);
        }

        private void startVideo(int pos) {
            setPosition(pos);
            Record r = records.get(pos);
            vv.setVideoURI(Uri.parse(r.getVideoUrl()));
            vv.start();
        }

        private void setPosition(int pos) {
            adapter.setSelectedItem(pos);
            if (pos > -1) {
                l.smoothScrollToPosition(pos);
            }
            adapter.notifyDataSetChanged();
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
                    firstFragment = new FirstFragment();
                    return firstFragment;
                case 1:
                    secondFragment = new SecondFragment();
                    return secondFragment;
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
                    return "A";
                case 1:
                    return "B";
            }
            return null;
        }
    }
}
