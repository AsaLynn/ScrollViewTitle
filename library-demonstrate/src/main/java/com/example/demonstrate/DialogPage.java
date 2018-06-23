package com.example.demonstrate;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;

/**
 * Created by think on 2018/3/8.
 */

public class DialogPage implements DialogInterface.OnClickListener {
    private Activity mActivity;
    private String[] items;
    protected static DialogPage mDialogPage;
    private OnDialogItemListener mOnPageItemListener;

    private DialogPage() {
    }

    public static DialogPage getInstance() {
        if (null == mDialogPage) {
            synchronized (DialogPage.class) {
                if (null == mDialogPage) {
                    mDialogPage = new DialogPage();
                }
            }
        }
        return mDialogPage;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (null == mOnPageItemListener) {
            DemonstrateUtil.showLogResult("未设置对话框列表监听!!!");
            return;
        }
        if (mOnPageItemListener.getStartActivity(which) == null) {
            DemonstrateUtil.showToastResult(mOnPageItemListener.getActivity(), "请设置要跳转的Activity!!!");
            return;
        }
        mOnPageItemListener
                .getActivity()
                .startActivity(new Intent(mOnPageItemListener.getActivity(), mOnPageItemListener.getStartActivity(which)));
    }

    public interface OnDialogItemListener {
        Activity getActivity();

        String getTitle();

        Class<?> getStartActivity(int which);
    }

    public void setOnOnDialogItemListener(OnDialogItemListener listener) {
        mOnPageItemListener = listener;
        if (null == mOnPageItemListener){
            return;
        }
        if (mOnPageItemListener.getActivity() == null) {
            return;
        }
        if (null == items) {
            items = mOnPageItemListener.getActivity().getResources().getStringArray(R.array.items);
        }

        DialogUtil.showListDialog(mOnPageItemListener.getActivity(), mOnPageItemListener.getTitle(), items, mDialogPage);
    }
}
