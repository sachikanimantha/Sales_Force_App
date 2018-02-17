package com.example.bellvantage.scadd;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.swf.CategoryDetails;
import com.example.bellvantage.scadd.swf.ProductList;

import java.util.ArrayList;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SALES_ORDER_SalesOrderId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_MERCHANT_STOCK_LINEITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRODUCT;

public class DailySummeryReportActivity extends AppCompatActivity {

    Toolbar mToolbar;
    TextView tv;
    Button btn_product,btn_brand;
    RecyclerView rv;
    int total_salesorder = 0;
    ArrayList<ProductList> productArrayList = new ArrayList<>();
    ArrayList<CategoryDetails> categoryArrayList = new ArrayList<>();
    SQLiteDatabase sd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summery_report);

        mToolbar = (Toolbar) findViewById(R.id.tb_main_3);
        mToolbar.setTitle("Daily Summery Report");

        init();

        btn_brand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }


    private void init(){

        tv = (TextView)findViewById(R.id.tv_count_salesorder);
        btn_brand = (Button)findViewById(R.id.btn_brand);
        btn_product = (Button)findViewById(R.id.btn_product);
        rv = (RecyclerView)findViewById(R.id.rv_list);

        sd = (new DbHelper(getApplicationContext())).getReadableDatabase();
    }

    public void getProductList(){

        String sql = "SELECT * FROM "+TABLE_PRODUCT;
        Cursor cursor = null;

        try{
            cursor = sd.rawQuery(sql,null);
            if(cursor.getCount() > 0 && cursor != null){

                while(cursor.moveToNext()){
                    String name = cursor.getString(cursor.getColumnIndex(DbHelper.COL_PRODUCT_ProductName));
                    int productid = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_PRODUCT_ProductId));
                }
            }
        }catch(Exception e){
            System.out.println("Error - "+e);
        }
    }

    public void getCategoryList(){


    }

    public int getTotalSalesOrders(int salesrepid,String today){
        int i = 0;
        Cursor cursor = null;

        String sql = "SELECT COUNT(*) FROM "+ DbHelper.TABLE_SALES_ORDER+" WHERE "+
                DbHelper.COL_SALES_ORDER_SalesRepId+" = "+salesrepid+" AND "+
                DbHelper.COL_SALES_ORDER_EnteredDate+" = "+today;

        SQLiteDatabase sqLiteDatabase = (new DbHelper(getApplicationContext())).getReadableDatabase();
        try{
            cursor = sqLiteDatabase.rawQuery(sql,null);
            if(cursor != null && cursor.getCount() > 0){
                while(cursor.moveToFirst()){
                    i= cursor.getInt(0);
                    System.out.println("total sales order count - "+i);
                }
            }
        }catch (Exception e){
            System.out.println("cursor null");
            return 0;
        }
        return i;
    }

    public ArrayList<Integer> getSalesOrdersId(int salesrepid, String today){
        ArrayList<Integer> salesorderidlist = new ArrayList<>();
        Cursor cursor = null;
        String sql = "SELECT "+COL_SALES_ORDER_SalesOrderId+" FROM "+
                DbHelper.TABLE_SALES_ORDER+" WHERE "+DbHelper.COL_SALES_ORDER_EnteredDate+" = "+today+" AND "+DbHelper.COL_SALES_ORDER_SalesRepId+" = "+salesrepid;
        SQLiteDatabase sqLiteDatabase = (new DbHelper(getApplicationContext())).getReadableDatabase();
        try{
            cursor = sqLiteDatabase.rawQuery(sql,null);
            if(cursor != null && cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    salesorderidlist.add(new Integer(cursor.getInt(0)));
                }
            }
        }catch (Exception e){
            System.out.println("cursor error");
        }

        return salesorderidlist;
    }

    public ArrayList<Integer> getMerchantStockId(int salesrepid,String today){
        ArrayList<Integer> merchantstockids = new ArrayList<>();
        Cursor cursor = null;
        String sql = "SELECT "+DbHelper.COL_MSTOCK_merchant_stock_id+" FROM "+
                DbHelper.TABLE_MERCHANT_STOCK+" WHERE "+DbHelper.COL_MSTOCK_entered_date+" = "+today+" AND "+DbHelper.COL_MSTOCK_salesrep_id+" = "+salesrepid;
        SQLiteDatabase sqLiteDatabase = (new DbHelper(getApplicationContext())).getReadableDatabase();
        try{
            cursor = sqLiteDatabase.rawQuery(sql,null);
            if(cursor != null && cursor.getCount() > 0){
                while(cursor.moveToNext()){
                    merchantstockids.add(new Integer(cursor.getInt(0)));
                }
            }
        }catch (Exception e){
            System.out.println("cursor error");
        }
        return merchantstockids;
    }

    public int getMercahntStock(ArrayList<Integer> stockid){


        Cursor cursor = null;
        int qty;
        int product_id;
        int totalqty = 0;

        for(int h=0; h < stockid.size() ; h++){

            String sql = "SELECT "+DbHelper.COL_MS_LINEITEM_product_id+","+
                    DbHelper.COL_MS_LINEITEM_quantity+" FROM "+TABLE_MERCHANT_STOCK_LINEITEM+" WHERE "+
                    DbHelper.COL_MS_LINEITEM_merchant_stock_id+" = "+stockid.get(h);

            SQLiteDatabase sqLiteDatabase = (new DbHelper(getApplicationContext())).getReadableDatabase();
            try{
                cursor = sqLiteDatabase.rawQuery(sql,null);
                if(cursor != null && cursor.getCount() > 0){
                    while(cursor.moveToNext()){
                        product_id = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_product_id));
                        qty = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_MS_LINEITEM_quantity));

                        totalqty = totalqty+qty;
                    }
                }
            }catch (Exception e){
                System.out.println("cursor error");
            }
        }

        //int i = 0;

        return totalqty;
    }





}
