package com.think.gradualchangescrollview.activity;

import com.example.demonstrate.DialogPage;
import com.example.demonstrate.FirstActivity;
import com.think.gradualchangescrollview.listener.PageItemListener0;

public class EnterActivity extends FirstActivity {


    @Override
    protected void click0() {
        DialogPage
                .getInstance()
                .setOnOnDialogItemListener(new PageItemListener0(this));
    }
}
