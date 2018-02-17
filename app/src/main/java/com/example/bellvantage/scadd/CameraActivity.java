package com.example.bellvantage.scadd;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.DB.MySingleton;
import com.example.bellvantage.scadd.Utils.Cam;
import com.example.bellvantage.scadd.Utils.ImageBase64;
import com.example.bellvantage.scadd.Utils.ImageLoader;
import com.example.bellvantage.scadd.Utils.NetworkConnection;
import com.example.bellvantage.scadd.swf.MerchantImage;
import com.example.bellvantage.scadd.web.HTTPPaths;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_IMAGE;

public class CameraActivity extends AppCompatActivity {

    Cam cameraPhoto;
    Button btnLaunchCamera,btnUpload,btnGetJson;
    ImageView ivCameraImage;
    final  int CAMERA_REQUEST= 13323;
    String selectedPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        btnLaunchCamera = (Button) findViewById(R.id.btnLaunchCamera);
        btnGetJson = (Button) findViewById(R.id.btnGetJson);
        btnUpload= (Button) findViewById(R.id.btnUplaod);
        ivCameraImage = (ImageView) findViewById(R.id.ivCameraImage);

        cameraPhoto =new Cam(CameraActivity.this);


        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhoto.takePhotoIntent(),CAMERA_REQUEST);
                } catch (IOException e) {
                    System.out.println("===============================================================");
                    System.out.println("Error while taking photo");
                    e.printStackTrace();
                }
            }
        });

        btnGetJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String url= HTTPPaths.seriveUrl+"";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }

                );
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);*/

               /* DbHelper dbHelper = new DbHelper(getApplicationContext());
                Cursor cursor = dbHelper.getAllData(TABLE_IMAGE);
                Bitmap bmp = null;
                byte[] image = new byte[0];
                if (cursor.getCount()>0){
                    System.out.println("Image Cursor: "+cursor.getCount());
                    while (cursor.moveToNext()){
                        image = cursor.getBlob(cursor.getColumnIndex(COL_IMAGE));
//                    Bitmap bitmap = BitmapFactory.decodeByteArray(image,100,image.length);

                    }
                }

                try {

//                    Bitmap bitmap = BitmapFactory.decodeByteArray(image,100,image.length);
//                    ivCameraImage.setImageBitmap(bitmap);

                    Bitmap bitmap = ImageLoader.init().from(image.toString()).requestSize(512,512).getBitmap();
                    ivCameraImage.setImageBitmap(bitmap);



                } catch (Exception e) {
                    Log.e("Image Adapter", "<loadImageFromDB> Error : " + e.getLocalizedMessage());
                }
*/
                Intent intent = new Intent(getApplicationContext(),ImageViewActivity.class);
                startActivity(intent);



            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                     if (selectedPhoto.equals("")){
                         Toast.makeText(CameraActivity.this, "Please capture image", Toast.LENGTH_SHORT).show();
                         return;
                     }

                    Bitmap bitmap = ImageLoader.init().from(selectedPhoto).requestSize(70,70).getBitmap();
                    String encodedImage = ImageBase64.encode(bitmap);
                    System.out.println("================================");
                    //System.out.println("Selected photo BITMAP: "+ encodedImage);

                   MerchantImage merchantImage = new MerchantImage();

                    DbHelper db = new DbHelper(getApplicationContext());
                    ContentValues cv = new ContentValues();
                    cv.put(DbHelper.COL_IMAGE1,encodedImage);
                    boolean s = db.insertDataAll(cv, TABLE_IMAGE);

                    if (s){
                        System.out.println("Image is save to database");
                    }else {
                        System.out.println("Image is not save to database");
                    }

                    Gson gson = new Gson();
                    //String jsonString = gson.toJson(merchantImage,MerchantImage.class);
                    String jsonString = gson.toJson(merchantImage,MerchantImage.class);
                    System.out.println("\n\n\nImage Json: "+jsonString+" \n\n");

                    String url = HTTPPaths.seriveUrl+"AddMerchantImage";
                   // String url = "http://sampletemp.96.lt/jsonImage.php";
//                    jsonObj
                    JSONObject jsonObj = null;
                    try {
                        jsonObj = new JSONObject(jsonString);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        System.out.println("Json Error in (Sync Image): " + e.getMessage());
                    }

                    if (NetworkConnection.checkNetworkConnection(getApplicationContext())==true){
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        System.out.println("=========response - "+response.toString());
                                        JsonObject object = Json.parse(String.valueOf(response)).asObject();
                                        int id = object.get("ID").asInt();

                                        if (id==200) {
                                            System.out.println("Image Sync successful");
                                        }

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        System.out.println("@@@@@ (Image Sync)  error - "+error.toString());
                                    }
                                });
                        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                    }
                } catch (FileNotFoundException e) {
                    System.out.println("Error while uploading photo");
                    e.printStackTrace();
                }finally {

                }
            }
        });


    }

    //Methods


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (requestCode==CAMERA_REQUEST){
                String photoPath = cameraPhoto.getPhotoPath();
                selectedPhoto = photoPath;
                System.out.println("Photo path "+photoPath);

                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                     ivCameraImage.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    System.out.println("===============================================================");
                    System.out.println("Error while setting photo");
                    e.printStackTrace();

                }
            }
        }
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
