package com.example.bellvantage.scadd.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bellvantage.scadd.DB.DbHelper;
import com.example.bellvantage.scadd.R;
import com.example.bellvantage.scadd.Target;
import com.example.bellvantage.scadd.Utils.DateManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_PRIMARY_SALES_INVOICE_OrderDate;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_LINE_ITEM_productId;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_TARGET_CATEGORY_LINE_ITEM_targetCategoryId;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRIMARY_SALES_INVOICE;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM;
import static com.example.bellvantage.scadd.DB.DbHelper.TABLE_TARGET_CATEGORY_LINE_ITEM;

/**
 * Created by Sachika on 15/11/2017.
 */

public class AdapterForTarget extends RecyclerView.Adapter<AdapterForTarget.MyViewHolder>{
    //Create Pie Chart
    float[] yData=new float[2];
    String[] xData = new String[2];
    ArrayList<Target> targetArrayList = new ArrayList<>();
    Context context;
    ArrayList<String> categoryNames=new ArrayList<>();
    DbHelper db;
    String firstDate ="",endDate = "";

    public AdapterForTarget(ArrayList<Target> targetArrayList, Context context, String fstDate, String endDate) {
        this.targetArrayList = targetArrayList;
        this.context = context;
        db = new DbHelper(context);
        this.firstDate = fstDate;
        this.endDate = endDate;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_for_targets,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Target target = targetArrayList.get(position);
        System.out.println( "Target Category Name "+target.getTargetCategoryName());
        holder.tvCategoryName.setText("Category Name: "+target.getTargetCategoryName());
        holder.tvTargetQty.setText("Target: "+target.getTargetQuantity());

        double achievement = getProductsLsit(target.getTargetCategoryId());
        System.out.println("Achievement : "+achievement);
        holder.tvAchievement.setText(String.format("Achievement: %.2f ",achievement));

        double percentage = (achievement/target.getTargetQuantity())*100;
        holder.tvPercentage.setText(String.format("Achievement Percentage: %.2f ",percentage)+"%");

        double targetPercentge = 100-percentage;

        //Chart..

        if(percentage>100){
            yData[0] = 0;
//        yData[0] = target.getTargetQuantity();
            System.out.println("yData[0] "+yData[0]);
//        yData[1] = (float) achievement;
//            yData[1] = (float) percentage;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ;
            yData[1] = (float) percentage;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ;
        }else{
            yData[0] = (float) targetPercentge;
//        yData[0] = target.getTargetQuantity();
            System.out.println("yData[0] "+yData[0]);
//        yData[1] = (float) achievement;
            yData[1] = (float) percentage;
        }


        System.out.println("yData[1] "+ yData[1]);

        xData = new String[]{"Target", "Achievement"};

        holder.pieChart.getDescription().setEnabled(false);
        holder.pieChart.setRotationEnabled(true);
        holder.pieChart.setUsePercentValues(true);
        //holder.pieChart.setHoleColor(Color.BLUE);
        //pieChart.setCenterTextColor(Color.BLACK);
        holder.pieChart.setHoleRadius(12f);
        holder.pieChart.setTransparentCircleAlpha(0);
        holder.pieChart.setCenterTextSize(10);
        //pieChart.setDrawEntryLabels(true);
        //pieChart.setEntryLabelTextSize(20);

        addDataSet(holder.pieChart);

    }

    private double getProductsLsit(String targetCategoryId) {
        SQLiteDatabase database = db.getWritableDatabase();
        String sql =    "SELECT * FROM "+ TABLE_TARGET_CATEGORY_LINE_ITEM+ " WHERE "+
                        COL_TARGET_CATEGORY_LINE_ITEM_targetCategoryId + " = '"+targetCategoryId+"'";

        System.out.println("SQL: "+sql);
        Cursor cursor = null;
        double achivement = 0;
        try {
            cursor = database.rawQuery(sql,null);
            if (cursor.getCount()>0){
                while (cursor.moveToNext()){
                    int productId = cursor.getInt(cursor.getColumnIndex(COL_TARGET_CATEGORY_LINE_ITEM_productId));
                    achivement += getSellingQuantityAccordingToProduct(productId);
                }
            }else{

            }
        }catch (Exception e){
            System.out.println("Error at getProductsList in AdapterForTarget ");
            e.printStackTrace();
        }


        return achivement;
    }

    private void addDataSet(PieChart pieChart) {

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();

        for(int i = 0; i < yData.length; i++){
            yEntrys.add(new PieEntry(yData[i] , i));
        }

        for(int i = 1; i < xData.length; i++){
            xEntrys.add(xData[i]);
        }

        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();

        colors.add(Color.RED);
        colors.add(Color.GREEN);

        ArrayList<String> data = new ArrayList<>();
        data.add("Target");
        data.add("Achievement");

        pieDataSet.setColors(colors);
        //add legend to chart
        Legend legend = pieChart.getLegend();
        pieChart.getLegend().setEnabled(false);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);


        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private double getSellingQuantityAccordingToProduct(int productId) {
        DateManager dateManager = new DateManager();
        long todayMil = dateManager.todayMillsecWithDateFormat("EEE, MMM d, yyyy");
        System.out.println("EEE, MMM d, yyyy: "+ todayMil);

        String fstDate,endDate;

        Date startDate = dateManager.getFirstDateOfMonth();
        Date lastDate = dateManager.getLastDateOfMonth();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        fstDate=sdf.format(startDate);
        endDate=sdf.format(lastDate);

        System.out.println("First Date: "+ fstDate);
        System.out.println("End Date: "+ endDate);
        System.out.println("FirstDate Mil: "+ dateManager.getMilSecAccordingToDate(fstDate));
        System.out.println("End Date Mil: "+ dateManager.getMilSecAccordingToDate(endDate));

        long fdateMil = dateManager.getMilSecAccordingToDate(fstDate);
        long edateMil = dateManager.getMilSecAccordingToDate(endDate);

        long searchFirstDate = dateManager.getMilSecAccordingToDate(firstDate);
        long searchaEndDate = dateManager.getMilSecAccordingToDate(endDate);

        System.out.println("[Adapter] Search First Date "+searchFirstDate);

        SQLiteDatabase database = db.getWritableDatabase();
        String sql = "";
        if (firstDate.isEmpty() || endDate.isEmpty()){
            sql =   "SELECT " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty+
                    " , " +TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit+
                    " FROM " +TABLE_PRIMARY_SALES_INVOICE+
                    " INNER JOIN "+ TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM +
                    " ON "+TABLE_PRIMARY_SALES_INVOICE+"."+COL_PRIMARY_SALES_INVOICE_InvoiceNumber +
                    " = " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber +
                    " WHERE "+ TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId+" = "+productId
                    + " AND substr("+COL_PRIMARY_SALES_INVOICE_OrderDate+ " , 7,13) >= '"+fdateMil +"'"
                    + " AND substr("+COL_PRIMARY_SALES_INVOICE_OrderDate+ " , 7,13) <= '"+todayMil +"'" ;
        }else{
            sql =   "SELECT " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty+
                    " , " +TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit+
                    " FROM " +TABLE_PRIMARY_SALES_INVOICE+
                    " INNER JOIN "+ TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM +
                    " ON "+TABLE_PRIMARY_SALES_INVOICE+"."+COL_PRIMARY_SALES_INVOICE_InvoiceNumber +
                    " = " + TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber +
                    " WHERE "+ TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+"."+COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId+" = "+productId
                    + " AND substr("+COL_PRIMARY_SALES_INVOICE_OrderDate+ " , 7,13) >= '"+searchFirstDate +"'"
                    + " AND substr("+COL_PRIMARY_SALES_INVOICE_OrderDate+ " , 7,13) <= '"+searchaEndDate +"'" ;
        }


        System.out.println("SQL "+ sql);

        Cursor cursor = null;
        double sellingQuantity = 0;
        try {
            cursor = database.rawQuery(sql,null);
            if (cursor.getCount()>0){
                System.out.println("Cursor Count: "+cursor.getCount());

                while (cursor.moveToNext()){
                    int orderQty = cursor.getInt(0);
                    double sizeOfUnit = cursor.getDouble(1);
                    sellingQuantity += orderQty*sizeOfUnit;
                    System.out.println("orderQty "+ orderQty+ " sizeOfUnit " + sizeOfUnit+ " sellingQuantity " + sellingQuantity);
                }
            }else{
                System.out.println("No Data related to the Selling Quantity");
            }
        }catch (Exception e){
            System.out.println("Error at getSellingquantityAccordingToProduct in AdapterForTarget ");
            e.printStackTrace();
        }

        System.out.println(" sellingQuantity " + sellingQuantity);

        return sellingQuantity;
    }



    @Override
    public int getItemCount() {
        if (targetArrayList.size()!=0){
            return targetArrayList.size();
        }
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvCategoryName, tvTargetQty,tvAchievement,tvPercentage;
        PieChart pieChart;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = (TextView) itemView.findViewById(R.id.tvCategoryName);
            tvTargetQty= (TextView) itemView.findViewById(R.id.tvTargetQty);
            tvAchievement= (TextView) itemView.findViewById(R.id.tvAchievement);
            tvPercentage = (TextView) itemView.findViewById(R.id.tvPercentage);
            pieChart = (PieChart) itemView.findViewById(R.id.pieChart);
        }
    }
}
