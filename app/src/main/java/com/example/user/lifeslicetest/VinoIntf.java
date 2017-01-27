package com.example.user.lifeslicetest;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by User on 26.01.2017.
 */

public interface VinoIntf {

    public static final String SERVER_URL = "https://api.vineapp.com/";

    @GET("timelines/tags/cat")
    Call<Response>get(@Query("page") int page);
}
