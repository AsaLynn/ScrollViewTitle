package com.think.gradualchangescrollview;

import com.think.gradualchangescrollview.model.NewsInfo;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by think on 2018/1/9.
 */

public interface NetInterface {
    //http://api.shigeten.net/api/Critic/GetCriticList
    @GET("api/Critic/GetCriticList")
    Call<CriticInfo> getCritic();

    @GET("api/GetFeeds?column=5&PageSize=30&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41")
    Call<NewsInfo> GetFeeds();

}
