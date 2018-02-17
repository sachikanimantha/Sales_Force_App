package com.example.bellvantage.scadd;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.Adapters.AdapterForTarget;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.Utils.DateManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_REP_SalesRepName;

public class TargetAchievementActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvTarget;
    TextView tvSalesRepName, tvMonth, tvPreviousMonth, tvSearchButoon;
    ArrayList<Target> targetArrayList = new ArrayList<>();
    AdapterForTarget adapterForTarget;
    DbHelper db;
    DateManager dateManager;


    //date picker
    Calendar calendar;
    int year, month, day, id;

    //date
    String dateString = "";
    String fstDate = "", endDate = "";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target_achievement);

        initializeViews();
        toolbar.setTitle("Target Achievement");

        setSalesRepName();
        setYearMonth();

        targetArrayList = getTargetArrayList();
        loadRecyclerView(targetArrayList, fstDate, endDate);

        tvPreviousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(67);
            }
        });

        tvSearchButoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myMonth = tvMonth.getText().toString();
                if (dateString.isEmpty() || myMonth.isEmpty()) {
                    Toast.makeText(TargetAchievementActivity.this, "Please Select a date", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    System.out.println("Search Button ");
                    DateManager dateManager = new DateManager();

                    Date startDate = null;
                    try {
                        startDate = dateManager.getFirstDateOfAnyMonth(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date lastDate = null;
                    try {
                        lastDate = dateManager.getLastDateOfAnyMonth(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                    fstDate = sdf.format(startDate);
                    endDate = sdf.format(lastDate);

                    System.out.println("First Date: " + fstDate);
                    System.out.println("End Date: " + endDate);

                    loadRecyclerView(targetArrayList, fstDate, endDate);
                }
            }
        });
    }

    private void setYearMonth() {
        String year = dateManager.getYear();
        String month = dateManager.getMonth();
        tvMonth.setText("Target Month: " + month + " of " + year);
    }

    private void setSalesRepName() {
        Cursor cursor = db.getAllData(DbHelper.TABLE_SALES_REP);
        String salesRepName = null;
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                salesRepName = cursor.getString(cursor.getColumnIndex(COL_SALES_REP_SalesRepName));
            }
        }
        tvSalesRepName.setText("Name: " + salesRepName);
    }

    private ArrayList<Target> getTargetArrayList() {
        ArrayList<Target> arrayList = new ArrayList<>();
        SQLiteDatabase database = db.getWritableDatabase();
        String sql = "SELECT SalesRepTarget.targetCategoryId, " +
                "SalesRepTarget.targetQty, " +
                "TargetCategory.targetCategoryName, " +
                "TargetCategoryLineItem.productId " +
                "FROM SalesRepTarget INNER JOIN TargetCategory ON SalesRepTarget.targetCategoryId = TargetCategory.targetCategoryId " +
                "INNER JOIN TargetCategoryLineItem ON TargetCategoryLineItem.targetCategoryId=TargetCategory.targetCategoryId GROUP BY SalesRepTarget.targetCategoryId";
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(sql, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String TargetCategoryId = cursor.getString(0);
                    int TargetQuantity = cursor.getInt(1);
                    String TargetCategoryName = cursor.getString(2);
                    int ProductId = cursor.getInt(3);

                    Target target = new Target(TargetCategoryId, TargetQuantity, TargetCategoryName, ProductId);
                    arrayList.add(target);
                }

            } else {
                System.out.println("No data in Target Array List");
            }
        } catch (Exception e) {
            System.out.println("Error At get TargetArray List");
            e.printStackTrace();
        } finally {
            database.close();
            cursor.close();
        }
        System.out.println("TargetArrayList Size " + arrayList.size());
        return arrayList;
    }

    private void initializeViews() {

        //Database
        db = new DbHelper(TargetAchievementActivity.this);
        //ToolBar
        toolbar = (Toolbar) findViewById(R.id.tb_main);

        //RecyclerView
        rvTarget = (RecyclerView) findViewById(R.id.rvTarget);

        //TextViews
        tvSalesRepName = (TextView) findViewById(R.id.tvSalesRepName);
        tvMonth = (TextView) findViewById(R.id.tvMonth);
        tvPreviousMonth = (TextView) findViewById(R.id.tvPreviousMonth);
        tvSearchButoon = (TextView) findViewById(R.id.tvSearchButoon);

        //Calender
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void loadRecyclerView(ArrayList<Target> targetArrayList, String fstDate, String endDate) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        rvTarget.setLayoutManager(layoutManager);
        rvTarget.setHasFixedSize(true);

        adapterForTarget = new AdapterForTarget(
                targetArrayList,
                TargetAchievementActivity.this, fstDate, endDate
        );
        rvTarget.setAdapter(adapterForTarget);
    }

    @Override
    protected Dialog onCreateDialog(final int id) {

        if (id == 67) {
            return new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            showDate(year, month + 1, dayOfMonth, id);
                        }
                    }, year, month, day);
        }

        if (id == 63) {
            return new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            showDate(year, month + 1, dayOfMonth, id);
                        }
                    }, year, month, day);
        }
        return null;
    }


    private void showDate(int Year, int month, int day, int id) {
        String mnt = null;
        String dy = null;
        if (month < 10) {
            mnt = "0" + month;
        } else if (month >= 10) {
            mnt = "" + month;
        }
        if (day < 10) {
            dy = "0" + day;
        } else if (day >= 10) {
            dy = "" + day;
        }

        dateString = dy + " " + mnt + " " + Year;
        tvPreviousMonth.setText(dateString);
    }
}
