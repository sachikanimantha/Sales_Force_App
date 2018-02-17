package com.example.bellvantage.scadd.swf;

import android.content.ContentValues;

import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_ACCORDINTO_DATE;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_SALESREP_ID;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_DISTRICT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_INVOICE;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_MERCHANT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_MERCHANT_REASON;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_MERCHANT_STOCK;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_MERCHANT_VISIT_COUNTS;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_PATH;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_PRIMARY_INVOICE_COUNT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_PRODUCT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_PRODUCT_BATCH;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_PRODUCT_CAT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_RETURN_TYPE;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_SALESREP_INVEN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_SALES_ORDER;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_TARGET_CATEGORY_COUNT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_TARGET_COUNT;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_TBL_VEHICLE_INVEN;
import static com.example.bellvantage.scadd.DB.DbHelper.COL_SER_COUNT_UPDATED_DATE_TIME;

/**
 * Created by Bellvantage on 22/09/2017.
 */

public class ServiceCount {

    int salesrepid;
    String date;
    String updatedTime;
    int DistrictCount;
    int ProductCategoryCount ;
    int ProductCount;
    int ProductBatchCount;
    int ReturnTypeCount;
    int PathCount;
    int MerchantCount;
    int SalesOrderCount;
    int VehicleInventoryCount;
    int SalesRepInventoryCount;
    int InvoiceCount;
    int MerchantStockCount;
    int MerchantVisitReasonCount;
    int MerchantVisitDetailsCount;
    int PrimarySalesInvoiceCount;
    int SalesRepTargetCount;
    int DefTargetCategoryCount;


    //PrimarySalesInvoiceCount":3,"SalesRepTargetCount":7,"DefTargetCategoryCount":7

    public ServiceCount(int salesreid,String date,String datetime,int districtCount, int productCategoryCount,
                        int productCount, int productBatchCount, int returnTypeCount, int pathCount, int merchantCount,
                        int salesOrderCount, int vehicleInventoryCount, int salesRepInventoryCount, int invoiceCount,
                        int merchantStockCount,int merchantVisitReasonCount,int merchantVisitDetailsCount,
                        int PrimarySalesInvoice_count,int SalesRepTarget_count,int DefTargetCategory_count) {

        this.setSalesrepid(salesreid);
        this.setDate(date);
        this.setUpdatedTime(datetime);
        this.setDistrictCount(districtCount);
        this.setProductCategoryCount(productCategoryCount);
        this.setProductCount(productCount);
        this.setProductBatchCount(productBatchCount);
        this.setReturnTypeCount(returnTypeCount);
        this.setPathCount(pathCount);
        this.setMerchantCount(merchantCount);
        this.setSalesOrderCount(salesOrderCount);
        this.setVehicleInventoryCount(vehicleInventoryCount);
        this.setSalesRepInventoryCount(salesRepInventoryCount);
        this.setInvoiceCount(invoiceCount);
        this.setMerchantStockCount(merchantStockCount);
        this.setMerchantVisitReasonCount(merchantVisitReasonCount);
        this.setMerchantVisitDetailsCount(merchantVisitDetailsCount);
        this.setPrimarySalesInvoiceCount(PrimarySalesInvoice_count);
        this.setSalesRepTargetCount(SalesRepTarget_count);
        this.setDefTargetCategoryCount(DefTargetCategory_count);
    }

    public int getDistrictCount() {
        return DistrictCount;
    }

    public void setDistrictCount(int districtCount) {
        DistrictCount = districtCount;
    }

    public int getProductCategoryCount() {
        return ProductCategoryCount;
    }

    public void setProductCategoryCount(int productCategoryCount) {
        ProductCategoryCount = productCategoryCount;
    }

    public int getProductCount() {
        return ProductCount;
    }

    public void setProductCount(int productCount) {
        ProductCount = productCount;
    }

    public int getProductBatchCount() {
        return ProductBatchCount;
    }

    public void setProductBatchCount(int productBatchCount) {
        ProductBatchCount = productBatchCount;
    }

    public int getReturnTypeCount() {
        return ReturnTypeCount;
    }

    public void setReturnTypeCount(int returnTypeCount) {
        ReturnTypeCount = returnTypeCount;
    }

    public int getPathCount() {
        return PathCount;
    }

    public void setPathCount(int pathCount) {
        PathCount = pathCount;
    }

    public int getMerchantCount() {
        return MerchantCount;
    }

    public void setMerchantCount(int merchantCount) {
        MerchantCount = merchantCount;
    }

    public int getSalesOrderCount() {
        return SalesOrderCount;
    }

    public void setSalesOrderCount(int salesOrderCount) {
        SalesOrderCount = salesOrderCount;
    }

    public int getVehicleInventoryCount() {
        return VehicleInventoryCount;
    }

    public void setVehicleInventoryCount(int vehicleInventoryCount) {
        VehicleInventoryCount = vehicleInventoryCount;
    }

    public int getSalesRepInventoryCount() {
        return SalesRepInventoryCount;
    }

    public void setSalesRepInventoryCount(int salesRepInventoryCount) {
        SalesRepInventoryCount = salesRepInventoryCount;
    }

    public int getInvoiceCount() {
        return InvoiceCount;
    }

    public void setInvoiceCount(int invoiceCount) {
        InvoiceCount = invoiceCount;
    }

    public int getSalesrepid() {
        return salesrepid;
    }

    public void setSalesrepid(int salesrepid) {
        this.salesrepid = salesrepid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public int getMerchantStockCount() {
        return MerchantStockCount;
    }

    public void setMerchantStockCount(int merchantStockCount) {
        MerchantStockCount = merchantStockCount;
    }

    public int getMerchantVisitReasonCount() {
        return MerchantVisitReasonCount;
    }

    public void setMerchantVisitReasonCount(int merchantVisitReasonCount) {
        MerchantVisitReasonCount = merchantVisitReasonCount;
    }

    public int getMerchantVisitDetailsCount() {
        return MerchantVisitDetailsCount;
    }

    public void setMerchantVisitDetailsCount(int merchantVisitDetailsCount) {
        MerchantVisitDetailsCount = merchantVisitDetailsCount;
    }

    public int getPrimarySalesInvoiceCount() {
        return PrimarySalesInvoiceCount;
    }

    public void setPrimarySalesInvoiceCount(int primarySalesInvoiceCount) {
        PrimarySalesInvoiceCount = primarySalesInvoiceCount;
    }

    public int getSalesRepTargetCount() {
        return SalesRepTargetCount;
    }

    public void setSalesRepTargetCount(int salesRepTargetCount) {
        SalesRepTargetCount = salesRepTargetCount;
    }

    public int getDefTargetCategoryCount() {
        return DefTargetCategoryCount;
    }

    public void setDefTargetCategoryCount(int defTargetCategoryCount) {
        DefTargetCategoryCount = defTargetCategoryCount;
    }

    public ContentValues getCVServiceCountsValues(){

        ContentValues cv = new ContentValues();
        cv.put(COL_SER_COUNT_SALESREP_ID,getSalesrepid());
        cv.put(COL_SER_COUNT_ACCORDINTO_DATE,getDate());
        cv.put(COL_SER_COUNT_UPDATED_DATE_TIME,getUpdatedTime());
        cv.put(COL_SER_COUNT_TBL_DISTRICT,getDistrictCount());
        cv.put(COL_SER_COUNT_TBL_PRODUCT_CAT,getProductCategoryCount());
        cv.put(COL_SER_COUNT_TBL_PRODUCT,getProductCount());
        cv.put(COL_SER_COUNT_TBL_PRODUCT_BATCH,getProductBatchCount());
        cv.put(COL_SER_COUNT_TBL_RETURN_TYPE,getReturnTypeCount());
        cv.put(COL_SER_COUNT_TBL_PATH,getPathCount());
        cv.put(COL_SER_COUNT_TBL_MERCHANT,getMerchantCount());
        cv.put(COL_SER_COUNT_TBL_SALES_ORDER,getSalesOrderCount());
        cv.put(COL_SER_COUNT_TBL_VEHICLE_INVEN,getVehicleInventoryCount());
        cv.put(COL_SER_COUNT_TBL_SALESREP_INVEN,getSalesRepInventoryCount());
        cv.put(COL_SER_COUNT_TBL_INVOICE,getInvoiceCount());
        cv.put(COL_SER_COUNT_TBL_MERCHANT_STOCK,getMerchantStockCount());
        cv.put(COL_SER_COUNT_TBL_MERCHANT_REASON,getMerchantVisitReasonCount());
        cv.put(COL_SER_COUNT_TBL_MERCHANT_VISIT_COUNTS,getMerchantVisitDetailsCount());

        cv.put(COL_SER_COUNT_TBL_PRIMARY_INVOICE_COUNT,getPrimarySalesInvoiceCount());
        cv.put(COL_SER_COUNT_TBL_TARGET_COUNT,getSalesRepTargetCount());
        cv.put(COL_SER_COUNT_TBL_TARGET_CATEGORY_COUNT,getDefTargetCategoryCount());


        int PrimarySalesInvoiceCount;
        int SalesRepTargetCount;
        int DefTargetCategoryCount;


        return cv;
    }

}
