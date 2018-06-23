package com.think.gradualchangescrollview;

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
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

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

public class MainActivity extends AppCompatActivity {

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
        Observable.create(new ObservableOnSubscribe<List<CriticInfo.ResultBean>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CriticInfo.ResultBean>> e) throws Exception {
                String baseUrl = "http://api.shigeten.net/";

                CriticInfo criticInfo = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(NetInterface.class)
                        .getCritic()
                        .execute()
                        .body();
                e.onNext(criticInfo.getResult());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CriticInfo.ResultBean>>() {
                    @Override
                    public void accept(List<CriticInfo.ResultBean> resultBeans) throws Exception {
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
        actionBar.setTitle("好文章");
        //设置当前的控件可用
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.mipmap.ic_launcher_round);

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
                CriticInfo.ResultBean resultBean = rvAdapter.getItem(position);
                Toast.makeText(MainActivity.this, "您点击了:" + resultBean.getTitle(), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().postSticky(resultBean);
                MainActivity.this.startActivity(new Intent(MainActivity.this, GradientActivity.class));
            }
        });
    }

    class RvAdapter extends BGARecyclerViewAdapter<CriticInfo.ResultBean> {

        public RvAdapter(RecyclerView recyclerView) {
            super(recyclerView, R.layout.item_recycler);
        }

        @Override
        protected void fillData(BGAViewHolderHelper helper, int position, CriticInfo.ResultBean model) {
            helper.setText(R.id.tv, model.getTitle());
            Glide.with(MainActivity.this)
                    .load("http://api.shigeten.net/" + model.getImage())
                    .apply(new RequestOptions().override(100)
                            .transform(new RoundedCorners(15)))
                    .into(helper.getImageView(R.id.iv));
        }
    }
}
