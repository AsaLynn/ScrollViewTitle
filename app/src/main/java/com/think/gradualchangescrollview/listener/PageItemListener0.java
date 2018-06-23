package com.think.gradualchangescrollview.listener;

import android.app.Activity;

import com.example.demonstrate.DialogPage;
import com.think.gradualchangescrollview.MainActivity;
import com.think.gradualchangescrollview.activity.Test1Activity;

/**
 * Created by think on 2018/3/9.
 */

public class PageItemListener0 implements DialogPage.OnDialogItemListener {

    private final Activity mActivity;

    public PageItemListener0(Activity activity) {
        mActivity = activity;
    }

    @Override
    public Activity getActivity() {
        return mActivity;
    }

    @Override
    public String getTitle() {
        return "项目实战周考1第1页";
    }

    @Override
    public Class<?> getStartActivity(int which) {
        if (which == 0) {
            return MainActivity.class;
        } else if (which == 1) {
            return Test1Activity.class;
        }
        return null;
    }
}
