package com.example.bellvantage.scadd;

import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DisplayMessages extends AppCompatActivity {


    // AlertDialog
    AlertDialog dialog;
    AlertDialog.Builder mBuilder;


    static TextView tv_status;
    TextView tv_lastsalesrep;
    TextView tv_lastdate;
    static ProgressBar pb_status;
    static Button btn_closeStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_messages);


//        mBuilder = new AlertDialog.Builder(DisplayMessages.this);
//        View view_ = getLayoutInflater().inflate(R.layout.layout_for_progress, null);
//        mBuilder.setView(view_);
//        dialog = mBuilder.create();
//        dialog.setCancelable(false);
//        dialog.setCanceledOnTouchOutside(false);
//
//        tv_status = (TextView) view_.findViewById(R.id.tv_status);
//        pb_status = (ProgressBar) view_.findViewById(R.id.pb_status);
//        btn_closeStatus = (Button) view_.findViewById(R.id.btn_close_status);

            //displayMessage();

    }


    public void displayMessage(String s) {
        mBuilder = new AlertDialog.Builder(DisplayMessages.this);
        View mView = getLayoutInflater().inflate(R.layout.layout_for_custom_message,null);

        TextView tvOk,tvMessage;
        tvOk = (TextView) mView.findViewById(R.id.tvOk);
        tvMessage = (TextView) mView.findViewById(R.id.tvMessage);
        tvMessage.setText(s);

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DisplayMessages.this.finish();
            }
        });

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();
    }



    public static class loginAsyncTask_newone extends AsyncTask<Integer, Integer, Integer> {
        //param,pross,result

        int tablerows;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv_status.setText("Starting ... ");
            pb_status.setVisibility(View.VISIBLE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            tablerows = params[0];
            int threadtime = 150;

            if (tablerows == 0) {
                tablerows = 100;
                threadtime = 150;
            }

            int k = 0 ;
            for (k = 0; k < tablerows; k++) {
                System.out.println("g1 - " + k);
                try {
                    Thread.sleep(threadtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(k);
            }
            return k;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String text = "";
            int pbProgress = 0;
            String txtTABLE = "";

            if (tablerows == 0) {
                tablerows = 100;
                text = "Server Error ";
                pbProgress = 1;
            }

            text = "Updating - (" + values[0] + "/" + tablerows + ")";
            pbProgress = values[0];

            tv_status.setText(text);
            pb_status.setProgress(pbProgress);
        }

        @Override
        protected void onPostExecute(Integer integers) {
            super.onPostExecute(integers);
            pb_status.setVisibility(View.GONE);
            btn_closeStatus.setEnabled(true);
            tv_status.setText("Table synced completed.");
            //tv_status.setTextColor(getResources().getColor(R.color.md_light_green_500));
            try {
                Thread.sleep(2000);
                //dialog_.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
