package com.think.gradualchangescrollview.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hankkin.gradationscroll.GradationScrollView;
import com.think.gradualchangescrollview.R;
import com.think.gradualchangescrollview.model.NewsInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Gradient1Activity extends AppCompatActivity {

    protected ImageView iv;
    protected TextView contentTv;
    protected GradationScrollView gradationSv;
    protected TextView titleTv;
    protected TextView timeTv;
    protected TextView contentTitleTv;
    private String TAG = this.getClass().getSimpleName();
    private int ivHeight = 40;
    private int maxChange;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_gradient);
        EventBus.getDefault().register(this);
//        initView();
    }

    private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
        contentTv = (TextView) findViewById(R.id.content_tv);
        gradationSv = (GradationScrollView) findViewById(R.id.gradation_sv);
        titleTv = (TextView) findViewById(R.id.title_tv);
        gradationSv.setScrollViewListener(new GradationScrollView.ScrollViewListener() {
            @Override
            public void onScrollChanged(GradationScrollView scrollView, int x, int y, int oldx, int oldy) {
                Log.i(TAG, "onScrollChanged: ***x=" + x + "***y=" + y);
                Log.i(TAG, "onScrollChanged: ***oldx=" + oldx + "***oldx=" + oldy);
                if (y <= 0) {
                    titleTv.setBackgroundColor(getResources().getColor(R.color.transparent));
                    titleTv.setTextColor(getResources().getColor(R.color.white));
                } else if (y > 0 && y <= maxChange) {
                    //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
                    float scale = (float) y / maxChange;
                    float alpha = (255 * scale);
                    titleTv.setTextColor(Color.argb((int) alpha, 255, 255, 255));
                    titleTv.setBackgroundColor(Color.argb((int) alpha, 144, 151, 166));
                } else {
                    //滑动到banner下面设置普通颜色
                    titleTv.setBackgroundColor(getResources().getColor(R.color.white));
                    titleTv.setTextColor(getResources().getColor(R.color.c_a8a8a8));
                }
            }
        });
        timeTv = (TextView) findViewById(R.id.time_tv);
        contentTitleTv = (TextView) findViewById(R.id.content_title_tv);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void handEvent( NewsInfo.ParamzBean.FeedsBean bean) {
        initView();
        String title = bean.getData().getSubject();
//        Log.i(TAG, "handEvent: " + title);//"》"
//        int i = title.indexOf("》");
//        String text = title.substring(0, i + 1);
        titleTv.setText(title);
        Glide.with(this)
                .load("http://litchiapi.jstv.com/" + bean.getData().getCover())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        ivHeight = iv.getHeight();
                        int intrinsicHeight = resource.getIntrinsicHeight();
                        ivHeight = intrinsicHeight;
                        maxChange = ivHeight - titleTv.getHeight();
                        Log.i(TAG, "onResourceReady: ***" + ivHeight);

                        return false;
                    }
                })
                .into(iv);
        contentTv.setText(bean.getData().getSummary());
        contentTitleTv.setText(bean.getData().getSubject());
        /*String pattern = "yyyy-MM-dd HH:mm:ss";
        Date date = new Date(bean.getData().getChanged());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String format = simpleDateFormat.format(date);*/
        timeTv.setText(bean.getData().getChanged());
    }
}
