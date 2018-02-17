package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.swf.ProductList;
import com.example.bellvantage.scadd.swf.ReturnInventoryLineItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_PROMOTION_CatalogueType;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PROMOTION_IMAGE;

/**
 * Created by Bellvantage on 08/10/2017.
 */

public class ProductListRV extends RecyclerView.Adapter<ProductListRV.MyViewHolder> implements Filterable{

    //String iDate,ddate;
    ArrayList<ProductList> productListArrayList = new ArrayList<>();
    ArrayList<ProductList> productListArrayList_filter = new ArrayList<>();
    ArrayList<ReturnInventoryLineItem> returnInventoryLineItems = new ArrayList<>();
    Context context;
    static DbHelper db;
    private OnItemClickListener listener;
    int posi;
    double itemVatAmount = 0.00;


    public interface OnItemClickListener{
        void onItemClick(ProductList productList,int position);
    }


    public ProductListRV(ArrayList<ProductList> productListArrayList,
                         ArrayList<ProductList> productListArrayList_filter,
                         ArrayList<ReturnInventoryLineItem> returnInventoryLineItems,
                         Context context,
                        OnItemClickListener itemClickListener) {
        this.productListArrayList = productListArrayList;
        this.productListArrayList_filter = productListArrayList_filter;
        this.returnInventoryLineItems = returnInventoryLineItems;
        this.context = context;
        this.listener = itemClickListener;
        db = new DbHelper(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_sales_items,parent,false);
        return new MyViewHolder(view);
    }


    public static String getDate(long milliSeconds, String dateFormat)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        ProductList productList = productListArrayList_filter.get(position);
        holder.bind(productList,listener,position);
        this.posi = position;
    }

    @Override
    public int getItemCount() {
        return productListArrayList_filter.size();
    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String s = constraint.toString();
                if(s.isEmpty()){
                    productListArrayList_filter = productListArrayList;
                    System.out.println("empty list");
                }else{
                    ArrayList<ProductList> filteredList = new ArrayList<>();
                    for(ProductList productList: productListArrayList){
                        if(productList.getName().toLowerCase().contains(s)){
                            filteredList.add(productList);
                        }
                    }
                    System.out.println("filteredList - "+filteredList.size());
                    productListArrayList_filter = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = productListArrayList_filter;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                productListArrayList_filter = (ArrayList<ProductList>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_stock,tv_unitprice,tv_expiredate,tv_subtotal,tv_qty,tv_free_qty,tvPromotion;
        ImageView iv_status;
        TextView tv_row_total,tv_row_net,tv_row_free;
        //not gonna show in row
        TextView tv_batchid,tv_productid,tv_isVat,tv_vatrate,tv_returnqty;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv_name = (TextView)itemView.findViewById(R.id.tv_orderitem_name);
            tv_expiredate = (TextView)itemView.findViewById(R.id.tv_orderitem_expiredate);
            tv_stock = (TextView)itemView.findViewById(R.id.tv_orderitem_stock);
            tv_unitprice = (TextView)itemView.findViewById(R.id.tv_orderitem_amount);
            tv_subtotal = (TextView)itemView.findViewById(R.id.tv_orderitem_subtotal);
            tv_qty = (TextView)itemView.findViewById(R.id.tv_qty_big);
            tv_free_qty = (TextView)itemView.findViewById(R.id.tv_free_qty_big);
            iv_status = (ImageView)itemView.findViewById(R.id.iv_is_choose);

            tv_row_free = (TextView) itemView.findViewById(R.id.tv_freevalue_row);
            tv_row_net = (TextView)itemView.findViewById(R.id.tv_netvalue_row);
            tv_row_total = (TextView)itemView.findViewById(R.id.tv_totalvalue_row);

            tv_batchid = (TextView)itemView.findViewById(R.id.tv_batchid);
            tv_productid  = (TextView)itemView.findViewById(R.id.tv_productid);
            tv_isVat  = (TextView)itemView.findViewById(R.id.tv_isVat);
            tv_vatrate  = (TextView)itemView.findViewById(R.id.tv_vatrate);
            tv_returnqty  = (TextView)itemView.findViewById(R.id.tv_return_qty);
            tvPromotion  = (TextView)itemView.findViewById(R.id.tvPromotion);

        }


        public void bind(final ProductList productList, final OnItemClickListener itemClickListener, final int position){

            String iDate,ddate;
            String edate = productList.getExpireDate();
            int start = edate.indexOf("(");
            int end = edate.indexOf(")");
            iDate = edate.substring(start+1,end);
            ddate = getDate(Long.parseLong(iDate), "dd/MM/yyyy");

            tv_name.setText(productList.getName());
            tv_stock.setText(String.valueOf(productList.getStock()));
            tv_unitprice.setText("Rs: "+String.format("%.2f", productList.getUnitPrice()));
            tv_expiredate.setText(ddate);
            tv_subtotal.setText("Rs: "+String.format("%.2f",productList.getSubTotal()));
            tv_qty.setText(String.valueOf(productList.getQty()));
            tv_free_qty.setText(String.valueOf(productList.getFreeQty()));
            iv_status.setImageResource(productList.getImg());

            tv_row_free.setText("Rs: "+String.format("%.2f", productList.getRow_free()));
            tv_row_net.setText("Rs: "+String.format("%.2f", productList.getRow_net()));
            tv_row_total.setText("Rs: "+String.format("%.2f", productList.getRow_total()));

            tv_batchid.setText(String.valueOf(productList.getBatchid()));
            tv_productid.setText(String.valueOf(productList.getProductid()));
            tv_isVat.setText(String.valueOf(productList.getIsVatHas()));
            tv_vatrate.setText(String.valueOf(productList.getVatRate()));
            tv_returnqty.setText(String.valueOf(productList.getReturnqty()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(productList,position);
                }
            });

            /*int catelogType = getCatelogType(productList.getProductid(),productList.getBatchid());
            System.out.println("catalog type _ : "+ catelogType);*/
            if (getCatelogType(productList.getProductid(),productList.getBatchid())){
                tvPromotion.setVisibility(View.VISIBLE);
            }else{
                tvPromotion.setVisibility(View.GONE);
            }
        }

        private boolean getCatelogType(int productid, int batchid) {
            SQLiteDatabase database = db.getWritableDatabase();
            int catelogType = 0;
            boolean isAvailable = false;
            String sql = "SELECT * FROM "+ TABLE_PROMOTION+" WHERE "
                    + DbHelper.COL_PROMOTION_ProductId+" = '"+productid+"' AND "
                    +DbHelper.COL_PROMOTION_BatchId+" = '"+batchid+"' AND "+COL_PROMOTION_CatalogueType + " = 1" ;
            System.out.println("Image query: "+ sql);

            Cursor cursor;

            try{
                System.out.println("Inside try - catalog");
                cursor = database.rawQuery(sql,null);
                System.out.println("cursor size in image select: "+ cursor.getCount());//not execute
                if(cursor.getCount() > 0){
                    System.out.println("catalog select");

                    isAvailable = true;

                   /* while (cursor.moveToNext()){
                        System.out.println("inside catalog while");
                        catelogType = cursor.getInt(cursor.getColumnIndex(COL_PROMOTION_CatalogueType));
                    }
                    System.out.println("catalog Type SQL: "+catelogType);*/
                }else{
                    System.out.println("no data in "+ TABLE_PROMOTION_IMAGE);
                }
            }catch(Exception e){
                System.out.println("Error at ProductListRv");
                e.printStackTrace();
            }
            return isAvailable;
        }
    }


    public void update(ArrayList<ProductList> list,int position,double vatamount){

        productListArrayList_filter = list;
        this.itemVatAmount = vatamount;
        notifyDataSetChanged();
    }
}
