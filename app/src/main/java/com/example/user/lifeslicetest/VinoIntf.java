package com.example.user.lifeslicetest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by User on 26.01.2017.
 */

public interface VinoIntf {

    public static final String SERVER_URL = "https://api.lifeslice.io/";

    @GET("timelines/tags/{tag}")
    Call<Records> getForTag(@Path("tag") String tag, @Query("page") int page);
}
