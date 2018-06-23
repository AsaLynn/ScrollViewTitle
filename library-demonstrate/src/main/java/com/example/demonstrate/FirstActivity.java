package com.example.demonstrate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

/**
 * Created by think on 2018/3/8.
 */

public abstract class FirstActivity extends AppCompatActivity implements View.OnClickListener {
    protected LinearLayout scrollLl;
    protected String[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_first);
        initView();
    }

    private void initView() {
        scrollLl = (LinearLayout) findViewById(R.id.scroll_ll);
        buttons = getResources().getStringArray(R.array.buttons);
        for (int i = 0; i < buttons.length; i++) {
            Button button = new Button(this);
            button.setText(buttons[i]);
            button.setTag(buttons[i]);
            button.setOnClickListener(this);
            scrollLl.addView(button);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getTag().equals(buttons[0])) {
            click0();
        } else if (v.getTag().equals(buttons[1])) {
            //click0();
        }
    }

    protected abstract void click0();
}
