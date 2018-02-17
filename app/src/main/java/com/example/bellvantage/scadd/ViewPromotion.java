package com.example.bellvantage.scadd;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.swf.PromotionList;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_PROMOTION_CatalogueType;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PROMOTION_IMAGE_INDEX;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION_IMAGE;
import static com.example.bellvantage.scadd.web.HTTPPaths.baseUrl;

public class ViewPromotion extends AppCompatActivity {

    TextView tv_batchid,tv_product_name,tv_productid;
    Button btn_close;
    ArrayList<PromotionList> promotionLists = new ArrayList<>();

    int batchid,productid,cat_type;
    ImageView proImage;
    String name;
    int imageIndex;

    String imageurl = "";

    DbHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_promotion);

        initi();
        db = new DbHelper(ViewPromotion.this);

        getImageIndex(productid,batchid);
        System.out.println("Image Index Before URL: "+ imageIndex);
        imageurl = baseUrl+"Catalogue/"+imageIndex+".jpg";
        System.out.println("Image url "+ imageurl+ "\n Product ID: "+ productid+ " Batch ID: "+ batchid);
        //getImage(productid,batchid);

        PhotoViewAttacher photoViewAttacher = new PhotoViewAttacher(proImage);
        photoViewAttacher.update();



        File file = new File( "sdcard/photoAlbum/"+imageIndex+".jpg");
        if(file.exists()){
            System.out.println("file is already there");
            proImage.setImageDrawable(Drawable.createFromPath( "sdcard/photoAlbum/"+imageIndex+".jpg"));
            btn_close.setText("Download Again");
        }else{
            System.out.println("Not find file ");
            btn_close.setText("Download");
        }
        //Download Image
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(imageurl);
            }
        });
    }


    private void initi() {

        tv_batchid = (TextView)findViewById(R.id.tv_product_batch_);
        tv_product_name = (TextView)findViewById(R.id.tv_productname_);
        tv_productid = (TextView)findViewById(R.id.tv_product_id_);
        btn_close = (Button)findViewById(R.id.btn_cancel_);
        proImage = (ImageView)findViewById(R.id.iv_promotion);

        productid = getIntent().getExtras().getInt("PRODUCTIDItem");
        batchid = getIntent().getExtras().getInt("BATCHIDItem");
        name = getIntent().getExtras().getString("nameItem");
        cat_type = getIntent().getExtras().getInt("CAT_TYPEItem");

        tv_batchid.setText("Batch ID - "+batchid);
        tv_product_name.setText("Product Name - "+name);
        tv_productid.setText("Product ID - "+productid);

    }

    private void getImageIndex(int pid,int bid) {
        SQLiteDatabase database = db.getWritableDatabase();
        String sql = "SELECT * FROM "+ TABLE_PROMOTION+" WHERE "
                +DbHelper.COL_PROMOTION_ProductId+" = '"+pid+"' AND "
                +DbHelper.COL_PROMOTION_BatchId+" = '"+bid+"' AND "+COL_PROMOTION_CatalogueType+" = '"+cat_type+"'";
        System.out.println("Image query: "+ sql);

        Cursor cursor;

        try{
            System.out.println("Inside try - image");
            cursor = database.rawQuery(sql,null);
            System.out.println("cursor size in image select: "+ cursor.getCount());//not execute
            if(cursor.getCount() > 0){
                System.out.println("Image select");

                while (cursor.moveToNext()){
                    System.out.println("inside while");
                    imageIndex = cursor.getInt(cursor.getColumnIndex(COL_PROMOTION_IMAGE_INDEX));
                    /*byte[] img = cursor.getBlob(cursor.getColumnIndex(DbHelper.COL_PROMOTION_IMAGE_img));
                    System.out.println("Image Array " + img);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(img,0,img.length);
                    proImage.setImageBitmap(bitmap);*/
                }
                System.out.println("Image Index SQL: "+imageIndex);
            }else{
                System.out.println("no data in "+ TABLE_PROMOTION_IMAGE);
            }
        }catch(Exception e){

        }

    }


    class DownloadTask extends AsyncTask<String,Integer,String>{

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewPromotion.this);
            progressDialog.setTitle("Downloading is Progress");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(100);
            progressDialog.setProgress(0);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... voids) {
            String path = voids[0];
            int fileLength = 0;
            try {
                URL url = new URL(path);
                URLConnection urlConnection = url.openConnection();
                urlConnection.connect();
                fileLength = urlConnection.getContentLength();
                File new_folder = new File("sdcard/photoalbum");
                if (!new_folder.exists()){
                    new_folder.mkdir();
                }
                File inputFile =  new File(new_folder,imageIndex+".jpg");
                InputStream inputStream = new BufferedInputStream(url.openStream(),8192);
                byte[] data = new byte[1024];
                int total = 0;
                int count = 0;
                OutputStream outputStream = new FileOutputStream(inputFile);
                while ((count=inputStream.read(data))>1){
                    System.out.println("Progress Count "+count);
                    total+=count;
                    /*if (count<1){
                        count=0;
                    }*/
                    outputStream.write(data,0,count);
                    int progress =(int) total*100/fileLength;
                    publishProgress(progress);
                }
                inputStream.close();
                outputStream.close();

            } catch (MalformedURLException e) {
                System.out.println(" ============ Error At Download IMAGE AsyncTask MalformedURLException ============");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println(" ============ Error At Download IMAGE IOEXEPTION ============");
                e.printStackTrace();
            }
            return "Download Completed...";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.hide();
            Toast.makeText(ViewPromotion.this, result, Toast.LENGTH_SHORT).show();
            String path = "sdcard/photoAlbum/"+imageIndex+".jpg";
            try {
                proImage.setImageDrawable(Drawable.createFromPath(path));
            }catch (Exception e){
                System.out.println("Error at PostExecute");
                e.printStackTrace();
            }
        }
    }

}