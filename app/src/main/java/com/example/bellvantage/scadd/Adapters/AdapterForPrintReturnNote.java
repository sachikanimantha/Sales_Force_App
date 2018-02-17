package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.ReprintReturnNoteActivity;
import com.example.bellvantage.scadd.swf.ReturnInventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT;
import static com.example.bellvantage.scadd.DB.DbHelper.TBL_MERCHANT_MERCHANT_ID;

/**
 * Created by Sachika on 26/07/2017.
 */

public class AdapterForPrintReturnNote extends RecyclerView.Adapter<AdapterForPrintReturnNote.ReviseViewHolder> {

    ArrayList<ReturnInventory> returnInventoryArrayList;
    Context context;


    public AdapterForPrintReturnNote(Context context, ArrayList<ReturnInventory> returnInventories) {
        this.returnInventoryArrayList = returnInventories;
        this.context = context;
    }

    @Override
    public ReviseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_revise_sales,parent,false);
        return new ReviseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviseViewHolder holder, int position) {
        final ReturnInventory returnInventory = returnInventoryArrayList.get(position);
        holder.tvMerchantName.setText(getMerchantName(returnInventory.getMerchantId()));
        holder.tvSOID.setText(returnInventory.getCreditNoteNo()+"");
        String createdDate = returnInventory.getReturnDate();
        int start = createdDate.indexOf("(");
        int end = createdDate.indexOf(")");
        String iDate = createdDate.substring(start+1,end);
        holder.tvCreateDate.setText(getDate(Long.parseLong(iDate), "dd/MM/yyyy"));

        holder.tvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,ReprintReturnNoteActivity.class);
                intent.putExtra("returnInventory",returnInventory);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    private String getMerchantName(int merchantId) {
        String merchantName = null;
        DbHelper db = new DbHelper(context);
        Cursor cursor = null;
        SQLiteDatabase database = db.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_MERCHANT+ " WHERE " +
                TBL_MERCHANT_MERCHANT_ID+ " = " + merchantId ;

        cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 0) {
            System.out.println(" ============= No data related to the " + merchantId);

        } else {
            while (cursor.moveToNext()) {
                merchantName = cursor.getString(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_MERCHANT_NAME));
            }
        }
        return merchantName;
    }

    @Override
    public int getItemCount() {
        if(returnInventoryArrayList !=null){
            return returnInventoryArrayList.size();
        }
        return 0;
    }


    public static class ReviseViewHolder extends RecyclerView.ViewHolder{

        TextView tvSOID,tvMerchantName,tvView,tvCreateDate;

        public ReviseViewHolder(View itemView) {
            super(itemView);
            tvSOID = (TextView) itemView.findViewById(R.id.tvSOID);
            tvView = (TextView) itemView.findViewById(R.id.tvView);
            tvMerchantName = (TextView) itemView.findViewById(R.id.tvMerchantName);
            tvCreateDate = (TextView) itemView.findViewById(R.id.tvCreateDate);
        }
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
