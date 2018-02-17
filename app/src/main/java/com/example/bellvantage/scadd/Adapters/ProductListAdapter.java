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
import com.example.bellvantage.scadd.swf.ProductList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Bellvantage on 29/06/2017.
 */

public class ProductListAdapter extends ArrayAdapter {

    ArrayList<ProductList> productListArrayList = new ArrayList<>();
    ArrayList<ProductList> productListArrayList_filterlist ;

    MyViewHolder myViewHolder;
    String iDate,ddate;
    int categoryId;
    int position;
    double sub_total = 0.00;
    int quantity = 0;
    int free_quantity = 0;
    int returnqty = 0;
    int img;
    double row_free;
    double row_net;
    double row_total;
    int product_isVat;
    double vatrate = 0.00;
    double itemVatAmount = 0.00;


    private static class MyViewHolder{
        TextView tv_name;
        TextView tv_stock;
        TextView tv_unitprice;
        TextView tv_expiredate;
        TextView tv_subtotal;
        TextView tv_qty;
        TextView tv_free_qty;
        ImageView iv_status;

        TextView tv_row_total;
        TextView tv_row_net;
        TextView tv_row_free;

        //not gonna show in row
        TextView tv_batchid;
        TextView tv_productid;
        //TextView tv_unitsellingdiscount;
        //TextView tv_totaldiscount;
        TextView tv_isVat;
        TextView tv_vatrate;
        TextView tv_returnqty;
    }


    public ProductListAdapter(@NonNull Context context, @LayoutRes int resource,
                              @NonNull ArrayList<ProductList> objects) {
        super(context, resource, objects);
        this.productListArrayList = objects;
        this.productListArrayList_filterlist = objects;
    }


    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            myViewHolder = new MyViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.layout_for_sales_items,null);

            myViewHolder.tv_name = (TextView)convertView.findViewById(R.id.tv_orderitem_name);
            myViewHolder.tv_expiredate = (TextView)convertView.findViewById(R.id.tv_orderitem_expiredate);
            myViewHolder.tv_stock = (TextView)convertView.findViewById(R.id.tv_orderitem_stock);
            myViewHolder.tv_unitprice = (TextView)convertView.findViewById(R.id.tv_orderitem_amount);
            myViewHolder.tv_subtotal = (TextView)convertView.findViewById(R.id.tv_orderitem_subtotal);
            myViewHolder.tv_qty = (TextView)convertView.findViewById(R.id.tv_qty_big);
            myViewHolder.tv_free_qty = (TextView)convertView.findViewById(R.id.tv_free_qty_big);
            myViewHolder.iv_status = (ImageView)convertView.findViewById(R.id.iv_is_choose);

            myViewHolder.tv_row_free = (TextView) convertView.findViewById(R.id.tv_freevalue_row);
            myViewHolder.tv_row_net = (TextView)convertView.findViewById(R.id.tv_netvalue_row);
            myViewHolder.tv_row_total = (TextView)convertView.findViewById(R.id.tv_totalvalue_row);

            myViewHolder.tv_batchid = (TextView)convertView.findViewById(R.id.tv_batchid);;
            myViewHolder.tv_productid  = (TextView)convertView.findViewById(R.id.tv_productid);;
            //viewHolder.tv_unitsellingdiscount  = (TextView)convertView.findViewById(R.id.tv_unitsellingdiscount);
            //viewHolder.tv_totaldiscount  = (TextView)convertView.findViewById(R.id.tv_totaldiscount);
            myViewHolder.tv_isVat  = (TextView)convertView.findViewById(R.id.tv_isVat);
            myViewHolder.tv_vatrate  = (TextView)convertView.findViewById(R.id.tv_vatrate);
            myViewHolder.tv_returnqty  = (TextView)convertView.findViewById(R.id.tv_return_qty);

            convertView.setTag(myViewHolder);
        }else{
            myViewHolder = (MyViewHolder)convertView.getTag();
        }

        String edate = productListArrayList.get(position).getExpireDate();
        int start = edate.indexOf("(");
        int end = edate.indexOf(")");
        iDate = edate.substring(start+1,end);
        ddate = getDate(Long.parseLong(iDate), "dd/MM/yyyy");

        myViewHolder.tv_name.setText(productListArrayList.get(position).getName());
        myViewHolder.tv_stock.setText(String.valueOf(productListArrayList.get(position).getStock()));
        myViewHolder.tv_unitprice.setText("Rs: "+String.format("%.2f", productListArrayList.get(position).getUnitPrice()));
        myViewHolder.tv_expiredate.setText(ddate);
        myViewHolder.tv_subtotal.setText("Rs: "+String.format("%.2f", productListArrayList.get(position).getSubTotal()));
        myViewHolder.tv_qty.setText(String.valueOf(productListArrayList.get(position).getQty()));
        myViewHolder.tv_free_qty.setText(String.valueOf(productListArrayList.get(position).getFreeQty()));
        myViewHolder.iv_status.setImageResource(productListArrayList.get(position).getImg());

        myViewHolder.tv_row_free.setText("Rs: "+String.format("%.2f", productListArrayList.get(position).getRow_free()));
        myViewHolder.tv_row_net.setText("Rs: "+String.format("%.2f", productListArrayList.get(position).getRow_net()));
        myViewHolder.tv_row_total.setText("Rs: "+String.format("%.2f", productListArrayList.get(position).getRow_total()));

        myViewHolder.tv_batchid.setText(String.valueOf(productListArrayList.get(position).getBatchid()));
        myViewHolder.tv_productid.setText(String.valueOf(productListArrayList.get(position).getProductid()));
        //viewHolder.tv_unitsellingdiscount.setText(String.valueOf(productListLasts.get(position).getUnitSellingDiscount()));
        //viewHolder.tv_totaldiscount.setText(String.valueOf(productListLasts.get(position).getTotalDiscount()));
        myViewHolder.tv_isVat.setText(String.valueOf(productListArrayList.get(position).getIsVatHas()));
        myViewHolder.tv_vatrate.setText(String.valueOf(productListArrayList.get(position).getVatRate()));
        myViewHolder.tv_returnqty.setText(String.valueOf(productListArrayList.get(position).getReturnqty()));

        return convertView;
    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public void updateList(ArrayList<ProductList> a, double subtotal, int qty,
                           int freeqty, int img, int position,double itemVA,int returnQTY,double rowfree,double rownet,double rowtotal){
        this.productListArrayList = a;
        this.sub_total = subtotal;
        this.quantity = qty;
        this.free_quantity = freeqty;
        this.returnqty = returnQTY;
        this.img = img;
        this.itemVatAmount = itemVA;
        this.row_free = rowfree;
        this.row_net = rownet;
        this.row_total = rowtotal;

        productListArrayList.get(position).setQty(quantity);
        productListArrayList.get(position).setFreeQty(free_quantity);
        productListArrayList.get(position).setSubTotal(sub_total);
        productListArrayList.get(position).setReturnqty(returnqty);
        productListArrayList.get(position).setImg(img);
        productListArrayList.get(position).setRow_free(row_free);
        productListArrayList.get(position).setRow_net(row_net);
        productListArrayList.get(position).setRow_total(row_total);
        notifyDataSetChanged();
    }

    public void setFilter(ArrayList<ProductList> arrayListF){
        System.out.println("arrayListF - "+arrayListF.size());
        productListArrayList = new ArrayList<>();
        productListArrayList.addAll(arrayListF);
        notifyDataSetChanged();
    }



}
