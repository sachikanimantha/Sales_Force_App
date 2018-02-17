package com.example.bellvantage.scadd;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.Utils.SessionManager;
import com.example.bellvantage.scadd.Utils.SyncManager;
import com.example.bellvantage.scadd.swf.LoginUser;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_LASTLOG_TIME;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_SALESREPID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SYNC_TABLE_NAME;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_SYNC;
import static com.example.bellvantage.scadd.Utils.SyncManager.Salesrep;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    EditText etUserName, etPassword;
    TextInputLayout tilUserName, tilPassword;
    String userName, password;
    TextView tvCordinates;
    AlertDialog.Builder builder;
    ProgressDialog dialog;
    LoginUser prefUser;
    int rowcounts = 0;
    int distriID;
    int salesrepID;
    SharedPreferences sp_disid;

    AlertDialog dialog_;

    // AlertDialog
    AlertDialog custuDialog;
    AlertDialog.Builder mBuilder;

    TextView tv_status;
    ProgressBar pb_status;
    Button btn_closeStatus;
    String time;
    int lastsalesrepid;
    String lastUserall_synctime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        //Views
        tilUserName = (TextInputLayout) findViewById(R.id.tilContact1);
        tilPassword = (TextInputLayout) findViewById(R.id.tilPassword);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        builder = new AlertDialog.Builder(LoginActivity.this);

        //tvCordinates = (TextView) findViewById(R.id.tvCoordinates);
        time = (new DateManager()).getDateWithTime();

//        int start_r = returnDate.indexOf("(");
//        int end_r = returnDate.indexOf(")");
//        //String iDate_r = returnDate.substring(start_r+1,end_r);
//        long iDate_r = Long.parseLong(returnDate.substring(start_r+1,end_r));

        // String onlyDate = DateManager.getDateAccordingToMilliSeconds(iDate_r,"dd.MM.yyyy");


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = etUserName.getText().toString();
                password = etPassword.getText().toString();

                if (NetworkConnection.checkNetworkConnection(getApplicationContext()) == false) {
                    displayStatusMessage(" Please check your internet connection ", 2);
                    btnLogin.setEnabled(true);
                } else if (userName.isEmpty() || password.isEmpty()) {
                    displayStatusMessage("Enter valid username and password.", 2);
                    btnLogin.setEnabled(true);
                } else {

                    btnLogin.setEnabled(false);
                    String url = HTTPPaths.seriveUrl + "GetDefLogin?userName=" + userName + "&userPassword=" + password;
                    System.out.println("url - " + url);
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,

                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    JsonObject object = Json.parse(response).asObject();
                                    int id = object.get("ID").asInt();
                                    if (id == 200) {
                                        String newUrl = response.replace("{\"Data\":\"", "{\"Data\": ");
                                        String new2Url = newUrl.replace("\",\"ID\":200}", "}");
                                        String new3Url = new2Url.replace("\\", "");
                                        new3Url = new3Url.trim();
                                        try {
                                            JSONObject jsonObj_main = new JSONObject(new3Url);
                                            final JSONObject data = jsonObj_main.getJSONObject("Data");
                                            LoginUser user = new LoginUser();
                                            user.setAddress(data.getString("Address"));
                                            user.setContactNumber(data.getString("ContactNumber"));
                                            user.setName(data.getString("Name"));
                                            user.setUserType(data.getString("UserType"));
                                            user.setSyncDate(data.getString("SyncDate"));
                                            user.setIsSync(data.getInt("IsSync"));
                                            user.setServiceUrl(data.getString("ServiceUrl"));
                                            user.setUserTypeId(data.getInt("UserTypeId"));
                                            user.setUserPassword(data.getString("UserPassword"));
                                            user.setUserName(data.getString("UserName"));
                                            user.setEpfNumber(data.getString("EpfNumber"));

                                            SessionManager.pref = getSharedPreferences("login.conf", Context.MODE_PRIVATE);
                                            SessionManager.editor = SessionManager.pref.edit();

                                            Gson gson = new Gson();
                                            String json = gson.toJson(user);
                                            SessionManager.editor.putString("user", json);
                                            SessionManager.editor.commit();
                                            SessionManager.editor.apply();

                                            String getJson = SessionManager.pref.getString("user", "");
                                            prefUser = gson.fromJson(getJson, LoginUser.class);
                                            System.out.println("prefUser.getUserTypeId() - " + prefUser.getUserTypeId() + ", prefUser.getUserType() - " + prefUser.getUserType());

                                            builder = new AlertDialog.Builder(LoginActivity.this);
                                            View view_ = getLayoutInflater().inflate(R.layout.layout_for_progress, null);
                                            builder.setView(view_);
                                            dialog_ = builder.create();
                                            dialog_.setCancelable(false);
                                            dialog_.setCanceledOnTouchOutside(false);

                                            tv_status = (TextView) view_.findViewById(R.id.tv_status);
                                            pb_status = (ProgressBar) view_.findViewById(R.id.pb_status);
                                            btn_closeStatus = (Button) view_.findViewById(R.id.btn_close_status);
                                            btn_closeStatus.setEnabled(false);

                                            if (prefUser == null) {
                                                Toast.makeText(LoginActivity.this, "Authentication fail", Toast.LENGTH_SHORT).show();
                                            } else if (prefUser.getUserType().equalsIgnoreCase("D")) {//dont need right now

                                                System.out.println("dont need right now D");
//
//                                                distriID = prefUser.UserTypeId;
//                                                rowcounts = (new DbHelper(getApplicationContext())).totalROWS_where_Id(TABLE_SALES_REP, COL_SALES_REP_DistributorId, prefUser.getUserTypeId());
//                                                System.out.println("rows - " + rowcounts);
//
//                                                if (rowcounts == 0) {
//                                                    rowcounts = 100;
//                                                }
//
//                                                pb_status.setMax(2);
//                                                (new loginAsyncTask_dis()).execute(2);
//                                                (new SyncManager(getApplicationContext())).getSalesRep_and_DistributorDetailsFromServer_accordingto_distributor(prefUser.getUserTypeId());
//
//                                                btn_closeStatus.setOnClickListener(new View.OnClickListener() {
//                                                    @Override
//                                                    public void onClick(View v) {
//                                                        //syncListAdapter.notifyDataSetChanged();
//                                                        Intent intent = new Intent(getApplicationContext(), MainActivityDistributor.class);
//                                                        intent.putExtra("userTYPE_", prefUser.getUserType());
//                                                        intent.putExtra("userID_", prefUser.getUserTypeId());
//                                                        startActivity(intent);
//                                                        dialog_.dismiss();
//                                                    }
//                                                });

                                            } else if (prefUser.getUserType().equalsIgnoreCase("S")) {

                                                System.out.println("============ SSSSSSSSS login ============");
                                                salesrepID = prefUser.getUserTypeId();
                                                System.out.println("============salesrepID - " + salesrepID);

                                                if (rowcounts == 0) {
                                                    rowcounts = 100;
                                                }

                                                Date date = new Date();
                                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                                String today = sdf.format(date);
                                                System.out.println("## today - " + today);

                                                //after deleting service count table data
                                                //(new SyncManager(getApplicationContext())).getServicesCounts(salesrepID,"2017-09-28");
                                                (new SyncManager(getApplicationContext())).getServicesCounts(salesrepID, today);
//

                                                dialog_.show();
                                                pb_status.setMax(rowcounts);//distributor & salesrep
                                                (new loginAsyncTask_dis()).execute(rowcounts);

                                                String sql2 = "SELECT DISTINCT " + COL_SYNC_SALESREPID + " FROM " + TABLE_SYNC;
                                                String sql3 = "SELECT " + COL_SYNC_LASTLOG_TIME + " FROM " + TABLE_SYNC + " WHERE " + COL_SYNC_TABLE_NAME + " = '" + Salesrep + "'";
                                                System.out.println("sql3 - " + sql3);

                                                try {
                                                    lastsalesrepid = Integer.parseInt((new SyncManager(getApplicationContext())).getStringValueFromSQLite(sql2, COL_SYNC_SALESREPID));
                                                    System.out.println("lastsalesrepid - " + lastsalesrepid);

                                                    lastUserall_synctime = (new SyncManager(getApplicationContext())).getStringValueFromSQLite(sql3, COL_SYNC_LASTLOG_TIME);
                                                    System.out.println("lastUserall_synctime - " + lastUserall_synctime);

                                                } catch (Exception e) {
                                                    dialog_.dismiss();
                                                    System.out.println("error - " + e.toString());
                                                    lastsalesrepid = 0;
                                                    lastUserall_synctime = "null";
                                                }
                                                //Get saled rep details from server
                                                System.out.println("sales Rep ID: "+ salesrepID);
                                                (new SyncManager(getApplicationContext())).getSalesRep_and_DistributorDetailsFromServer_accordingto_salesrep(0, salesrepID);

                                                btn_closeStatus.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        //syncListAdapter.notifyDataSetChanged();
                                                        Intent intent = new Intent(getApplicationContext(), LoginSyncActivity.class);
                                                        intent.putExtra("userTYPE", prefUser.getUserType());
                                                        intent.putExtra("userID", prefUser.getUserTypeId());
                                                        intent.putExtra("logTime", time);

                                                        //CHECK whether dis and salesrep data syncd to sqlite
                                                        int distributorid = (new SyncManager(getApplicationContext())).getDitstributerId();
                                                       // String s_sql = "SELECT "+COL_SALES_REP_SalesRepId+" FROM "+TABLE_SALES_REP;
                                                        //    int salesrepid = (new SyncManager(getApplicationContext())).getSalesRepDetailsFromSQLite(s_sql).get(0).getSalesRepId();

                                                        System.out.println("@distributorid - " + distributorid);
                                                        if (distributorid > 0) {
                                                            try {
                                                                intent.putExtra("salesrepName", data.getString("Name"));
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                            intent.putExtra("lastSalesRep", lastsalesrepid);
                                                            intent.putExtra("lastSalesRepLogTime", lastUserall_synctime);

                                                            startActivity(intent);
                                                            dialog_.dismiss();
                                                        } else {
                                                            displayStatusMessage("User data not inserted to device.\nPlease log again.", 2);
                                                            dialog_.dismiss();
                                                        }
                                                    }
                                                });
                                            }
                                        } catch (Exception e) {

                                            btn_closeStatus.setEnabled(true);
                                            System.out.println("Error Login === " + e.getMessage());
                                            Toast.makeText(LoginActivity.this, "Error JSON" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        //alertDailog("Authentication fail.. \nEnter valid username and password.");

                                        btn_closeStatus.setEnabled(true);
                                        displayStatusMessage("Authentication fail.. \nEnter valid username and password.", 2);
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    //dialog.dismiss();
                                    System.out.print("Error Volley" + error.getMessage());
                                    //Toast.makeText(LoginActivity.this, "Error Volley"+ error.toString(), Toast.LENGTH_SHORT).show();
                                    displayStatusMessage("Something went wrong.. Server failure ", 2);
                                }
                            }
                    );
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            }
        });
    }


    public class loginAsyncTask_dis extends AsyncTask<Integer, Integer, Integer> {
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

            Integer g = 0;
            int threadtime = 20;

            if (tablerows == 0) {
                tablerows = 100;
                threadtime = 500;
            }

            for (int k = 0; k < tablerows; k++) {

                g = k;
                System.out.println("g - " + g);
                try {
                    Thread.sleep(threadtime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(g);
            }
            return g;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String text = "";
            int pbProgress = 0;
            String txtTABLE = " SALESREP & DISTRIBUTOR ";

            if (tablerows == 0) {
                tablerows = 100;
                text = "Server Error ";
                pbProgress = 1;
            }

            tv_status.setText("Synchronizing.... ");
            //text = "Updating - " + txtTABLE + " (" + values[0] + "/" + tablerows + ")";
            text = "Updating - " + txtTABLE + " ( " + values[0] + "% )";
            pbProgress = values[0];

            tv_status.setText(text);
            pb_status.setProgress(pbProgress);
        }

        @Override
        protected void onPostExecute(Integer integers) {
            super.onPostExecute(integers);

            String txtTABLE = "";
            int tableRows = integers;

            btn_closeStatus.setEnabled(true);
            pb_status.setVisibility(View.GONE);
            tv_status.setText(txtTABLE + " Table synced completed");
            tv_status.setTextColor(getResources().getColor(R.color.md_light_green_500));
            try {
                Thread.sleep(2000);
                //dialog_.dismiss();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void displayStatusMessage(String s, int colorValue) {

        AlertDialog.Builder builder = null;
        View view = null;
        TextView tvOk, tvMessage;
        ImageView imageView;

        int defaultColor = R.color.textGray;
        int successColor = R.color.successColor; // 1
        int errorColor = R.color.errorColor; // 2
        int warningColor = R.color.warningColor; // 3

        int success = R.mipmap.ic_success;
        int error_image = R.mipmap.ic_error;
        int warning_image = R.drawable.ic_warning;
        //1,2,3

        int color = defaultColor;
        int img = success;
        if (colorValue == 1) {
            color = successColor;
            img = success;

        } else if (colorValue == 2) {
            color = errorColor;
            img = error_image;

        } else if (colorValue == 3) {
            color = warningColor;
            img = warning_image;
        }

        builder = new AlertDialog.Builder(LoginActivity.this);
        view = getLayoutInflater().inflate(R.layout.layout_for_custom_message, null);

        tvOk = (TextView) view.findViewById(R.id.tvOk);
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        imageView = (ImageView) view.findViewById(R.id.iv_status_k);

        tvMessage.setTextColor(getResources().getColor(color));
        tvMessage.setText(s);
        imageView.setImageResource(img);


        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                btnLogin.setEnabled(true);
            }
        });

    }

}
