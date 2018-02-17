package com.example.bellvantage.scadd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.bellvantage.scadd.Adapters.MenuAdapter;
import com.example.bellvantage.scadd.MC.MyMenu;

import java.util.ArrayList;

public class EvaluationActivity extends AppCompatActivity {

    //ToolBar
    Toolbar toolbar;

    //RecycleView for menu
    RecyclerView rvMenuItem;
    MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        intializeViews();

        toolbar.setTitle("Evaluation");

        //Create Menu
        insertMenu();
    }

    private void intializeViews() {
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        //RecyclerView
        rvMenuItem =(RecyclerView)findViewById(R.id.rvMenuItem);
    }


    private void insertMenu() {

        LinearLayoutManager layoutManager = new GridLayoutManager(EvaluationActivity.this,
                                            3,LinearLayoutManager.VERTICAL,false);
        rvMenuItem.setLayoutManager(layoutManager);
        rvMenuItem.setHasFixedSize(true);


        String[] menuLabels = { "Target Achievement","Daily Summery"};
        int[] menuImages = {
                R.drawable.mm_target_achivement,
                R.drawable.mm_daily_summery
        };

        ArrayList<MyMenu> arrayList = new ArrayList<>();

        for(int i = 0; i < menuLabels.length; i++) {
            String menuName = menuLabels[i];
            int img = menuImages[i];
            arrayList.add(new MyMenu(menuName,img));
        }
        adapter = new MenuAdapter(getApplicationContext(),arrayList,0);
        rvMenuItem.setAdapter(adapter);
    }

}
