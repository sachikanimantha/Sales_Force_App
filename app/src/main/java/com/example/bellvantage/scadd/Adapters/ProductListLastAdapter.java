package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.ProductListLast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bellvantage on 21/06/2017.
 */

public class ProductListLastAdapter extends ArrayAdapter {

    ArrayList<ProductListLast> productListLasts = new ArrayList<>();
    ViewHolder viewHolder;
    String date_,date;
    int quantity;
    int free_quantity;
    int productId;

    public static class ViewHolder{
        TextView tv_name;
        TextView tv_stock;
        TextView tv_unitprice;
        TextView tv_expiredate;
        TextView tv_subtotal;
        TextView tv_qty;
        TextView tv_free_qty;
        ImageView iv_status;

        //not gonna show in row
        TextView tv_batchid;
        TextView tv_productid;
        //TextView tv_unitsellingdiscount;
        //TextView tv_totaldiscount;
        TextView tv_isVat;
        TextView tv_vatrate;
        TextView tv_line_grandtotal;
        TextView tv_line_freetotal;
        TextView tv_line_nettotal;

    }

    public ProductListLastAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<ProductListLast> objects) {
        super(context, resource, objects);
        this.productListLasts = objects;
        System.out.println("productListLasts inside adapter size - "+ productListLasts.size());
    }

    @Override
    public int getCount() {
        return super.getCount();

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_for_sales_items,null);

            viewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_orderitem_name);
            viewHolder.tv_stock = (TextView)convertView.findViewById(R.id.tv_orderitem_stock);
            viewHolder.tv_unitprice = (TextView)convertView.findViewById(R.id.tv_orderitem_amount);
            viewHolder.tv_expiredate = (TextView)convertView.findViewById(R.id.tv_orderitem_expiredate);
            viewHolder.tv_subtotal = (TextView)convertView.findViewById(R.id.tv_orderitem_subtotal);
            viewHolder.tv_qty = (TextView)convertView.findViewById(R.id.tv_qty_big);
            viewHolder.tv_free_qty = (TextView)convertView.findViewById(R.id.tv_free_qty_big);
            viewHolder.iv_status = (ImageView)convertView.findViewById(R.id.iv_is_choose);

            viewHolder.tv_batchid = (TextView)convertView.findViewById(R.id.tv_batchid);;
            viewHolder.tv_productid  = (TextView)convertView.findViewById(R.id.tv_productid);;
            //viewHolder.tv_unitsellingdiscount  = (TextView)convertView.findViewById(R.id.tv_unitsellingdiscount);
            //viewHolder.tv_totaldiscount  = (TextView)convertView.findViewById(R.id.tv_totaldiscount);
            viewHolder.tv_isVat  = (TextView)convertView.findViewById(R.id.tv_isVat);
            viewHolder.tv_vatrate  = (TextView)convertView.findViewById(R.id.tv_vatrate);

            viewHolder.tv_line_grandtotal = (TextView)convertView.findViewById(R.id.tv_totalvalue_row);
            viewHolder.tv_line_freetotal = (TextView)convertView.findViewById(R.id.tv_freevalue_row);
            viewHolder.tv_line_nettotal = (TextView)convertView.findViewById(R.id.tv_netvalue_row);

            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder)convertView.getTag();
        }


        String edate = productListLasts.get(position).getExpiredate();
        int start = edate.indexOf("(");
        int end = edate.indexOf(")");
        date_ = edate.substring(start + 1, end);
        date = getDate(Long.parseLong(date_), "dd/MM/yyyy");

        viewHolder.tv_name.setText(productListLasts.get(position).getName());
        viewHolder.tv_expiredate.setText(date);
        viewHolder.tv_stock.setText(String.valueOf(productListLasts.get(position).getStock()));
        viewHolder.tv_unitprice.setText("Rs : "+ String.format("%.2f", productListLasts.get(position).getUnitSellingPrice()));
        viewHolder.tv_subtotal.setText("Rs : "+ String.format("%.2f", productListLasts.get(position).getLineTotal()));
        viewHolder.tv_qty.setText(String.valueOf(productListLasts.get(position).getQuantity()));
        viewHolder.tv_free_qty.setText(String.valueOf(productListLasts.get(position).getFreeQuantity()));
        viewHolder.iv_status.setImageResource(productListLasts.get(position).getImg());

        viewHolder.tv_batchid.setText(String.valueOf(productListLasts.get(position).getBatchID()));
        viewHolder.tv_productid.setText(String.valueOf(productListLasts.get(position).getProductID()));
        //viewHolder.tv_unitsellingdiscount.setText(String.valueOf(productListLasts.get(position).getUnitSellingDiscount()));
        //viewHolder.tv_totaldiscount.setText(String.valueOf(productListLasts.get(position).getTotalDiscount()));
        viewHolder.tv_isVat.setText(String.valueOf(productListLasts.get(position).getIsVat()));
        viewHolder.tv_vatrate.setText(String.valueOf(productListLasts.get(position).getVatRate()));


        viewHolder.tv_line_grandtotal.setText("Rs : "+ String.format("%.2f", productListLasts.get(position).getLineTotal()));
        viewHolder.tv_line_freetotal.setText("Rs : "+ String.format("%.2f", productListLasts.get(position).getFreeTotal()));
        viewHolder.tv_line_nettotal.setText("Rs : "+ String.format("%.2f", productListLasts.get(position).getGrossTotal()));

        return convertView;
    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void updateItemAdd(int productID,int qty,int freeqty,int position){

        this.quantity = qty;
        this.free_quantity = freeqty;
        this.productId = productID;

        productListLasts.get(position).setQuantity(quantity);
        productListLasts.get(position).setFreeQuantity(free_quantity);
        productListLasts.get(position).setProductID(productId);
        notifyDataSetChanged();

    }


}
