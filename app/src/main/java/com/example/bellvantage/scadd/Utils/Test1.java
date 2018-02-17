package com.example.bellvantage.scadd.Utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;

import java.lang.ref.WeakReference;

/**
 * Created by Bellvantage on 07/10/2017.
 */

public class Test1 extends Application {

        private WeakReference<Activity> mActivity = null;

        @Override
        public void onCreate() {
            super.onCreate();
            registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    mActivity = new WeakReference<Activity>(activity);
                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    mActivity.clear();
                }

                /** Unused implementation **/
                @Override
                public void onActivityStarted(Activity activity) {}

                @Override
                public void onActivityResumed(Activity activity) {}
                @Override
                public void onActivityPaused(Activity activity) {}

                @Override
                public void onActivityStopped(Activity activity) {}

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {}
            });
        }

        public Activity getCurrentActivity() {
            return mActivity.get();
        }

    public void displayStatusMessage(String s,int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk,tvMessage,tvCancel;

        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3
        //1,2,3

        int color = defaultColor;
        if(colorValue == 1){
            color = successColor;
        }else if(colorValue == 2){
            color = errorColor;
        }else if(colorValue == 3){
            color = warningColor;
        }

        builder = new AlertDialog.Builder(getCurrentActivity().getApplication());
        view = getCurrentActivity().getLayoutInflater().inflate(R.layout.layout_for_custom_message_with_ok_cancel,null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);


        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    }