package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.Utils.ImageBase64;

import java.util.ArrayList;

/**
 * Created by Bellvantage on 13/09/2017.
 */

public class ImageAdapter extends  RecyclerView.Adapter<ImageAdapter.MyViewHolder>{

    Context context;
   ArrayList<Img> arrayList = new ArrayList<>();

    public ImageAdapter(Context context, ArrayList<Img> arrayList) {
        System.out.println("Adapter============================");
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_image,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        System.out.println("Adapter Holder =======================");
        Img img = arrayList.get(position);
        System.out.println(img.getImage().toString());
        byte[] image = img.getImage();
       // Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);
        try {
            /*ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(image);
            Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
            holder.ivPhoto.setImageBitmap(bitmap);*/
            Bitmap bitmap = ImageBase64.decode(image.toString());
            holder.ivPhoto.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("Image Adapter", "<loadImageFromDB> Error : " + e.getLocalizedMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (arrayList.size()!=0){
            return arrayList.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPhoto;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }
}
