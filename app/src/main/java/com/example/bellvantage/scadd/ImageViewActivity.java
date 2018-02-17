package com.example.bellvantage.scadd;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.bellvantage.scadd.Adapters.ImageAdapter;
import com.example.bellvantage.scadd.Adapters.Img;
import com.example.bellvantage.scadd.DB.DbHelper;

import java.util.ArrayList;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_IMAGE1;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_IMAGE;

public class ImageViewActivity extends AppCompatActivity {

    RecyclerView rvImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        rvImageView = (RecyclerView) findViewById(R.id.rvImageView);
        loadRecyclerView(getImages());
    }

    private ArrayList<Img> getImages() {
        ArrayList<Img> imgArrayList = new ArrayList<>();
        DbHelper dbHelper = new DbHelper(getApplicationContext());
        Cursor cursor = dbHelper.getAllData(TABLE_IMAGE);
        if (cursor.getCount()>0){
            System.out.println("Image Cursor: "+cursor.getCount());
            while (cursor.moveToNext()){
                byte[] image = cursor.getBlob(cursor.getColumnIndex(COL_IMAGE1));
                Img i = new Img(image);
                imgArrayList.add(i);
            }
        }

        return  imgArrayList;
    }

    private void loadRecyclerView(ArrayList<Img> imgArrayList) {
        System.out.println(" load recycler Array size: "+imgArrayList.size());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                false
        );

        rvImageView.setLayoutManager(layoutManager);
        rvImageView.setHasFixedSize(true);
        ImageAdapter imageAdapter= new ImageAdapter(
                ImageViewActivity.this,
                imgArrayList
        );
        rvImageView.setAdapter(imageAdapter);
    }
}
