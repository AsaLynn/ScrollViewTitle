package com.think.gradualchangescrollview.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.think.gradualchangescrollview.NetInterface;
import com.think.gradualchangescrollview.R;
import com.think.gradualchangescrollview.model.NewsInfo;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGARecyclerViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test1Activity extends AppCompatActivity {

    protected RecyclerView rv;
    private RvAdapter rvAdapter;
    private Toolbar mToolbar;
    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
        request();
    }

    private void request() {
        Observable.create(new ObservableOnSubscribe<List<NewsInfo.ParamzBean.FeedsBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<NewsInfo.ParamzBean.FeedsBean>> e) throws Exception {
//                String baseUrl = "http://api.shigeten.net/";
                //--http://litchiapi.jstv.com/api/GetFeeds?column=5&PageSize=30&pageIndex=1&val=100511D3BE5301280E0992C73A9DEC41
                String baseUrl = "http://litchiapi.jstv.com/";

                NewsInfo criticInfo = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(NetInterface.class)
                        .GetFeeds()
                        .execute()
                        .body();
                e.onNext(criticInfo.getParamz().getFeeds());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<NewsInfo.ParamzBean.FeedsBean>>() {
                    @Override
                    public void accept(List<NewsInfo.ParamzBean.FeedsBean> resultBeans) throws Exception {
                        Log.i(TAG, "accept:*** "+ resultBeans.toString());
                        rvAdapter.setData(resultBeans);
                    }
                });

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        //设置actionbar的标题
        actionBar.setTitle("好大的胃口");
        //设置当前的控件可用
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round);
        actionBar.setHomeAsUpIndicator(R.mipmap.tab_mine_selected);

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);
        rv.setHasFixedSize(true);

        rvAdapter = new RvAdapter(rv);
        rv.setAdapter(rvAdapter);
        rvAdapter.setOnRVItemClickListener(new BGAOnRVItemClickListener() {
            @Override
            public void onRVItemClick(ViewGroup parent, View itemView, int position) {
                NewsInfo.ParamzBean.FeedsBean resultBean = rvAdapter.getItem(position);
                Toast.makeText(Test1Activity.this, "您点击了:" + resultBean.getData().getSubject(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky(resultBean);
                Test1Activity.this.startActivity(new Intent(Test1Activity.this, Gradient1Activity.class));
            }
        });
    }

    class RvAdapter extends BGARecyclerViewAdapter<NewsInfo.ParamzBean.FeedsBean> {

        public RvAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_recycler1);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, NewsInfo.ParamzBean.FeedsBean model) {
            helper.setText(R.id.tv, model.getData().getSubject());
            helper.setText(R.id.tv2, model.getData().getSummary());

            Glide.with(Test1Activity.this)
                    .load("http://litchiapi.jstv.com/" + model.getData().getCover())
                    .apply(new RequestOptions().override(100)
                            .circleCrop())
                    .into(helper.getImageView(R.id.iv));
        }
    }
}
