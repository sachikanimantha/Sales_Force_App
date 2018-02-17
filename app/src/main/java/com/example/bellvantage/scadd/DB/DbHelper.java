package com.example.bellvantage.scadd.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.bellvantage.scadd.Utils.DateManager;

import static com.example.bellvantage.scadd.Utils.SyncManager.All;
import static com.example.bellvantage.scadd.Utils.SyncManager.Category;
import static com.example.bellvantage.scadd.Utils.SyncManager.Distributor;
import static com.example.bellvantage.scadd.Utils.SyncManager.District;
import static com.example.bellvantage.scadd.Utils.SyncManager.Invoice;
import static com.example.bellvantage.scadd.Utils.SyncManager.Merchant;
import static com.example.bellvantage.scadd.Utils.SyncManager.MerchantReason;
import static com.example.bellvantage.scadd.Utils.SyncManager.MerchantStock;
import static com.example.bellvantage.scadd.Utils.SyncManager.MerchantVisitCounts;
import static com.example.bellvantage.scadd.Utils.SyncManager.Path;
import static com.example.bellvantage.scadd.Utils.SyncManager.PrimaryInvoice;
import static com.example.bellvantage.scadd.Utils.SyncManager.Product;
import static com.example.bellvantage.scadd.Utils.SyncManager.Product_batch;
import static com.example.bellvantage.scadd.Utils.SyncManager.Return_type;
import static com.example.bellvantage.scadd.Utils.SyncManager.Sales_order;
import static com.example.bellvantage.scadd.Utils.SyncManager.Salesrep;
import static com.example.bellvantage.scadd.Utils.SyncManager.Salesrep_inventory;
import static com.example.bellvantage.scadd.Utils.SyncManager.Target;
import static com.example.bellvantage.scadd.Utils.SyncManager.TargetCategory;
import static com.example.bellvantage.scadd.Utils.SyncManager.Vehicle_inventory;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "scadd.db";

    //meta
    public static  final int SYNC_STATUS_OK = 1;
    public static  final int SYNC_STATUS_FAIL = 0;
    public static  final int TABLE_SALES_ORDER_ID = 1;
    public static  final int TABLE_RETURN_INVENTORY_ID = 2;
    public static  final int TABLE_SALES_INVOICE_ID = 3;

    //Tables
    public static final String TABLE_NAME = "attendance";
    public static final String TBL25 = "ACTUAL_PATH";
    public static final String TABLE_AREA = "area";
    public static final String TABLE_DISTRICT = "district";
    public static final String TABLE_PATH = "path";
    public static final String TABLE_MERCHANT = "DEF_MERCHANT";
    public static final String TABLE_DELIVERY_LOCATIONS = "delivery_locations";
    public static final String TABLE_SALES_REP = "DEF_SALES_REP";
    public static final String TABLE_SALES_ORDER = "DEF_SALES_ORDER";
    public static final String TABLE_LINE_ITEM = "DEF_LINE_ITEM";
    public static final String TABLE_PRODUCT= "DEF_PRODUCT";
    public static final String TABLE_PRODUCT_BATCH= "PRODUCT_BATCH";
    public static final String TABLE_VEHICALE_INVENTORY= "VEHICLE_INVENTORY";
    public static final String TABLE_SALESORDERS_SYNC= "SALESORDERS_SYNC";
    public static final String TABLE_DISTRIBUTOR= "DEF_DISTRIBUTOR";
    public static final String TABLE_CATEGORY = "DEF_CATEGORY";
    public static final String TABLE_REVISE_ORDER= "REVISE_ORDER";
    public static final String TABLE_REVISE_ORDER_JSON= "REVISE_ORDER_JSON";
    public static final String TABLE_INVOICE_JSON= "INVOICE_JSON";
    public static final String TABLE_SALES_ORDER_JSON= "SALES_ORDER_JSON";
    public static final String TABLE_RETURN_INVENTORY_JSON= "RETURN_INVENTORY_JSON";
    public static final String TABLE_SALESREP_INVENTORY= "SALESREP_INVENTORY";
    public static final String TABLE_RETURN_TYPE= "RETURN_TYPE";
    public static final String TABLE_RETURN_INVENTORY= "RETURN_INVENTORY";
    public static final String TABLE_RETURN_INVENTORY_LINEITEM= "RETURN_INVENTORY_LINEITEM";
    public static final String TABLE_SYNC= "SYNC_TABLES";
    public static final String TABLE_INVOICE_ITEM = "DEF_INVOICE_ITEM";
    public static final String TABLE_INVOICE_LINE_ITEM = "DEF_INVOICE_LINE_ITEM";
    public static final String TABLE_MERCHANT_CLASS = "MERCHANT_CLASS";
    public static final String TABLE_MERCHANT_TYPE = "MERCHANT_TYPE";
    public static final String TABLE_INVOICE_UTILIZATION = "INVOICE_UTILIZATION";
    public static final String TABLE_SYNC_ID = "SYNC_ID";
    public static final String TABLE_SERVICES_COUNTS = "SERVICE_COUNTS";
    public static final String TABLE_MERCHANT_STOCK = "Merchant_stock_table";
    public static final String TABLE_MERCHANT_STOCK_LINEITEM = "Merchant_Stock_Lineitem_table";
    public static final String TABLE_SYNC_ATTENDANCE = "SYNC_ATTENDANCE";
    public static final String TABLE_MVISIT_REASON = "Merchant_visit_reason_table";
    public static final String TABLE_MVISIT = "Merchant_visit_table";
    public static final String TABLE_TARGET_CATEGORY = "TargetCategory";
    public static final String TABLE_TARGET_CATEGORY_LINE_ITEM = "TargetCategoryLineItem";
    public static final String TABLE_SALESREP_TARGET = "SalesRepTarget";
    public static final String TABLE_PRIMARY_SALES_INVOICE = "PrimarySalesInvoice";
    public static final String TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM = "PrimarySalesInvoiceLineItem";
    public static final String TABLE_CREDIT_TYPE = "TableCreditType";
    public static final String TABLE_MILEAGE = "Mileage";
    public static final String TABLE_SYNC_MILEAGE = "SYNC_MILEAGE";
    public static final String TABLE_PROMOTION = "PROMOTION";
    public static final String TABLE_PROMOTION_IMAGE = "PROMOTION_IMAGE";

    //columns of actual path tables
    public static final String COL25_SEQUENCE_ID = "SEQUENCE_ID";
    public static final String COL25_ROUTE_DATE = "ROUTE_DATE";
    public static final String COL25_DISTRIBUTER_ID = "DISTRIBUTER_ID";
    public static final String COL25_SALES_REP_ID = "SALES_REP_ID";
    public static final String COL_25_USER_NAME = "USER_NAME";
    public static final String COL25_LOCATION_TIME = "LOCATION_TIME";
    public static final String COL25_LONGITUDE = "LONGITUDE";
    public static final String COL25_LATITUDE = "LATITUDE";
    public static final String COL25_IS_SYNC = "IS_SYNC";
    public static final String COL25_SYNC_DATE = "SYNC_DATE";
    public static final String COL25_ACCURACY = "ACCURACY";

    //table ccolumns of Area
    public  static final String TABLE_AREA_AREA_CODE = "areaCode";
    public  static final String TABLE_AREA_AREA_NAME = "areaName";

    //table ccolumns of District
    public  static final String TABLE_DISTRICT_DISTRICT_NAME = "districtName";
    public  static final String TABLE_AREA_DISTRICT_CODE= "districtCode";

    //table columns of Path
    public  static final String TABLE_PATH_DELIVERYPATHID = "DeliveryPathId";
    public  static final String TABLE_PATH_PATHNAME= "PathName";

    //table ccolumns of Delivery Locations
    public  static final String TABLE_DELIVERY_LOCATION_MERCHANT_ID = "MerchantID";
    public  static final String TABLE_DELIVERY_LOCATION_DELIVERY_PATH_ID = "DeliveryPathID";

    //Merchant Table columns
    public static final String TBL_MERCHANT_SEQUENCE_ID = "SequenceId";
    public static final String TBL_MERCHANT_MERCHANT_ID = "MerchantId";
    public static final String TBL_MERCHANT_MERCHANT_NAME = "MerchantName";
    public static final String TBL_MERCHANT_CONTACT_NO1 = "ContactNo1";
    public static final String TBL_MERCHANT_CONTACT_NO2 = "ContactNo2";
    public static final String TBL_MERCHANT_LONGITUDE = "Longitude";
    public static final String TBL_MERCHANT_LATITUDE = "Latitude";
    public static final String TBL_MERCHANT_IS_ACTIVE = "IsActive";
    public static final String TBL_MERCHANT_ENTERED_DATE = "EnteredDate";
    public static final String TBL_MERCHANT_ENTERED_USER = "EnteredUser";
    public static final String TBL_MERCHANT_DISCOUNT_RATE = "DiscountRate";
    public static final String TBL_MERCHANT_IS_SYNC = "IsSync";
    public static final String TBL_MERCHANT_SYNC_DATE = "SyncDate";
    public static final String TBL_MERCHANT_BUILDING_NO = "BuildingNo";
    public static final String TBL_MERCHANT_ADDRESS_1 = "Address1";
    public static final String TBL_MERCHANT_ADDRESS_2 = "Address2";
    public static final String TBL_MERCHANT_CITY = "City";
    public static final String TBL_MERCHANT_CONTACT_PERSON = "ContactPerson";
    public static final String TBL_MERCHANT_AREA_CODE = "AreaCode";
    public static final String TBL_MERCHANT_MERCHANT_TYPE = "MerchantType";
    public static final String TBL_MERCHANT_MERCHANT_CLASS = "MerchantClass";
    public static final String TBL_MERCHANT_DISTRICT_CODE = "DistrictCode";
    public static final String TBL_MERCHANT_UPDATED_USER = "UpdatedUser";
    public static final String TBL_MERCHANT_UPDATED_DATE = "UpdatedDate";
    public static final String TBL_MERCHANT_REFERENCE_ID = "ReferenceID";
    public static final String TBL_MERCHANT_ISVAT = "IsVat";
    public static final String TBL_MERCHANT_VAT_NO = "VatNo";
    public static final String TBL_MERCHANT_PATH_CODE = "pathCode";
    public static final String TBL_MERCHANT_SYNC_ID = "SyncId";
    public static final String TBL_MERCHANT_IS_CREDIT = "IsCredit";
    public static final String TBL_MERCHANT_CREDIT_DAYS = "CreditDays";
    public static final String TBL_INVOICE_ITEM = "DEF_INVOICE_ITEM";
    public static final String TBL_INVOICE_LINE_ITEM = "DEF_INVOICE_LINE_ITEM";

    //Columns of SalesRep table
    public static final String COL_SALES_REP_SalesRepId = "SalesRepId";
    public static final String COL_SALES_REP_DistributorId = "DistributorId";
    public static final String COL_SALES_REP_SalesRepName = "SalesRepName";
    public static final String COL_SALES_REP_EpfNumber = "EpfNumber";
    public static final String COL_SALES_REP_IsActive = "IsActive";
    public static final String COL_SALES_REP_EnteredUser = "EnteredUser";
    public static final String COL_SALES_REP_EnteredDate = "EnteredDate";
    public static final String COL_SALES_REP_PresentAddress = "PresentAddress";
    public static final String COL_SALES_REP_PermanentAddress = "PermanentAddress";
    public static final String COL_SALES_REP_ContactNo = "ContactNo";
    public static final String COL_SALES_REP_UpdatedUser = "UpdatedUser";
    public static final String COL_SALES_REP_UpdatedDate = "UpdatedDate";
    public static final String COL_SALES_REP_ReferenceID= "ReferenceID";
    public static final String COL_SALES_REP_NextCreditNoteNo= "NextCreditNoteNo";
    public static final String COL_SALES_REP_NextSalesOrderNo= "NextSalesOrderNo";
    public static final String COL_SALES_REP_NextInvoiceNo= "NextInvoiceNo";
    public static final String COL_SALES_REP_SalesRepType= "SalesRepType";

    //Columns of DEF_SALES_ORDER table
    public static final String COL_SALES_ORDER_InvoiceNumber = "InvoiceNumber";
    public static final String COL_SALES_ORDER_EstimateDeliveryDate= "EstimateDeliveryDate";
    public static final String COL_SALES_ORDER_TotalDiscount = "TotalDiscount";
    public static final String COL_SALES_ORDER_TotalAmount = "TotalAmount";
    public static final String COL_SALES_ORDER_SyncDate = "SyncDate";
    public static final String COL_SALES_ORDER_IsSync = "IsSync";
    public static final String COL_SALES_ORDER_SaleStatus = "SaleStatus";
    public static final String COL_SALES_ORDER_EnteredUser = "EnteredUser";
    public static final String COL_SALES_ORDER_EnteredDate = "EnteredDate";
    public static final String COL_SALES_ORDER_UpdatedUser = "UpdatedUser";
    public static final String COL_SALES_ORDER_UpdatedDate = "UpdatedDate";
    public static final String COL_SALES_ORDER_SaleDate = "SaleDate";
    public static final String COL_SALES_ORDER_SaleTypeId= "SaleTypeId";
    public static final String COL_SALES_ORDER_SalesRepId= "SalesRepId";
    public static final String COL_SALES_ORDER_DitributorId= "DitributorId";
    public static final String COL_SALES_ORDER_MerchantId= "MerchantId";
    public static final String COL_SALES_ORDER_SalesOrderId= "SalesOrderId";
    public static final String COL_SALES_ORDER_TotalVAT= "TotalVAT";
    public static final String COL_SALES_ORDER_IsVat= "IsVat";
    public static final String COL_SALES_ORDER_IsReturn= "IsReturn";
    public static final String COL_SALES_ORDER_MerchntName= "MerchntName";
    public static final String COL_SALES_ORDER_CreditType= "CreditType";
    public static final String COL_SALES_ORDER_CreditDate= "CreditDate";



    //Columns of Line Items Tables
    public static final String COL_LINE_ITEM_SalesOrderID= "SalesOrderID";
    public static final String COL_LINE_ITEM_BatchID= "BatchID";
    public static final String COL_LINE_ITEM_ProductID= "ProductID";
    public static final String COL_LINE_ITEM_CurrentStock= "Stock";
    public static final String COL_LINE_ITEM_Quantity= "Quantity";
    public static final String COL_LINE_ITEM_FreeQuantity= "FreeQuantity";
    public static final String COL_LINE_ITEM_UnitSellingPrice= "UnitSellingPrice";
    public static final String COL_LINE_ITEM_UnitSellingDiscount= "UnitSellingDiscount";
    public static final String COL_LINE_ITEM_TotalAmount= "TotalAmount";
    public static final String COL_LINE_ITEM_TotalDiscount= "TotalDiscount";
    public static final String COL_LINE_ITEM_IsSync= "IsSync";
    public static final String COL_LINE_ITEM_SyncDate= "SyncDate";

    //Columns of Product table
    public static final String COL_PRODUCT_ProductId= "ProductId";
    public static final String COL_PRODUCT_PackingTypeId= "PackingTypeId";
    public static final String COL_PRODUCT_SellingCategoryId= "SellingCategoryId";
    public static final String COL_PRODUCT_CompanyId= "CompanyId";
    public static final String COL_PRODUCT_CategoryId= "CategoryId";
    public static final String COL_PRODUCT_ProductName= "ProductName";
    public static final String COL_PRODUCT_IsActive= "IsActive";
    public static final String COL_PRODUCT_EnteredDate= "EnteredDate";
    public static final String COL_PRODUCT_EnteredUser= "EnteredUser";
    public static final String COL_PRODUCT_UnitSellingPrice= "UnitSellingPrice";
    public static final String COL_PRODUCT_UnitDistributorPrice= "UnitDistributorPrice";
    public static final String COL_PRODUCT_ReferenceID= "ReferenceID";
    public static final String COL_PRODUCT_IsVat= "IsVat";
    public static final String COL_PRODUCT_VatRate= "VatRate";
    public static final String COL_PRODUCT_SizeOfUnit= "SizeofUnit";
    public static final String COL_PRODUCT_AddToCard= "AddtoCard";


    //Columns of Vehicle Inventory Table
    public static final String COL_VEHICLE_INVENTORY_ProductId= "ProductId";
    public static final String COL_VEHICLE_INVENTORY_BatchID= "BatchID";
    public static final String COL_VEHICLE_INVENTORY_DistributorID= "DistributorID";
    public static final String COL_VEHICLE_INVENTORY_SalesRepID= "SalesRepID";
    public static final String COL_VEHICLE_INVENTORY_LoadQuantity= "LoadQuantity";
    public static final String COL_VEHICLE_INVENTORY_OutstandingQuantity= "OutstandingQuantity";
    public static final String COL_VEHICLE_INVENTORY_DiscountRule= "DiscountRule";
    public static final String COL_VEHICLE_INVENTORY_FreeIssueRule= "FreeIssueRule";
    public static final String COL_VEHICLE_INVENTORY_UnitSellingPrice= "UnitSellingPrice";
    public static final String COL_VEHICLE_INVENTORY_InventoryDate= "InventoryDate";

    //Columns of Product Batch Table
    public static final String COL_PRODUCT_BATCH_ProductId= "ProductId";
    public static final String COL_PRODUCT_BATCH_BatchID= "BatchID";
    public static final String COL_PRODUCT_BATCH_Quantity= "Quantity";
    public static final String COL_PRODUCT_BATCH_BlockQuantity= "BlockQuantity";
    public static final String COL_PRODUCT_BATCH_UnitDealrePrice= "UnitDealrePrice";
    public static final String COL_PRODUCT_BATCH_UnitSellingPrice= "UnitSellingPrice";
    public static final String COL_PRODUCT_BATCH_IsAllSold= "IsAllSold";
    public static final String COL_PRODUCT_BATCH_ExpiryDate= "ExpiryDate";
    public static final String COL_PRODUCT_BATCH_EnteredUser= "EnteredUser";
    public static final String COL_PRODUCT_BATCH_EnteredDate= "EnteredDate";

    //Columns of Revise Sales Order table
    public static final String COL_REVISE_SALES_ORDER_ProductId= "ProductId";
    public static final String COL_REVISE_SALES_ORDER_Batch_Id= "BatchID";
    public static final String COL_REVISE_SALES_UnitSellingPrice= "UnitSellingPrice";
    public static final String COL_REVISE_SALES_ExpiryDate= "ExpiryDate";
    public static final String COL_REVISE_SALES_ProductName= "ProductName";
    public static final String COL_REVISE_SALES_CategoryId= "CategoryId";
    public static final String COL_REVISE_SALES_IsVat= "IsVat";
    public static final String COL_REVISE_SALES_VatRate= "VatRate";
    public static final String COL_REVISE_STOCK_R= "StockR";
    public static final String COL_REVISE_STOCK_P= "StockP";

    //columns of distributor
    public static final String COL_DISTRIBUTOR_DISTRIBUTOR_ID = "DISTRIBUTOR_ID";
    public static final String COL_DISTRIBUTOR_REGION_ID = "REGION_ID" ;
    public static final String COL_DISTRIBUTOR_AREA_CODE = "AREA_CODE";
    public static final String COL_DISTRIBUTOR_TERITORY_ID = "TERITORY_ID";
    public static final String COL_DISTRIBUTOR_DISTRIBUTOR_NAME = "DISTRIBUTOR_NAME";
    public static final String COL_DISTRIBUTOR_CONTACT_NUMBER = "CONTACT_NUMBER";
    public static final String COL_DISTRIBUTOR_ADDRESS_1 = "ADDRESS_1";
    public static final String COL_DISTRIBUTOR_ADDRESS_2 = "ADDRESS_2";
    public static final String COL_DISTRIBUTOR_ADDRESS_3 = "ADDRESS_3";
    public static final String COL_DISTRIBUTOR_ADDRESS_4 = "ADDRESS_4";
    public static final String COL_DISTRIBUTOR_EMAIL = "EMAIL";
    public static final String COL_DISTRIBUTOR_PARTNER_ID = "PARTNER_ID";
    public static final String COL_DISTRIBUTOR_IS_SYNC = "IS_SYNC";
    public static final String COL_DISTRIBUTOR_SYNC_DATE = "SYNC_DATE";
    public static final String COL_DISTRIBUTOR_REFERENCE_ID = "REFERENCE_ID";
    public static final String COL_DISTRIBUTOR_IS_VAT = "IS_VAT";
    public static final String COL_DISTRIBUTOR_VAT_NO = "VAT_NO";
    public static final String COL_DISTRIBUTOR_UPDATED_USER = "UPDATED_USER";
    public static final String COL_DISTRIBUTOR_UPDATED_DATE = "UPDATED_DATE";
    public static final String COL_DISTRIBUTOR_ENTERED_USER = "ENTERED_USER";
    public static final String COL_DISTRIBUTOR_ENTERED_DATE = "ENTERED_DATE";
    public static final String COL_DISTRIBUTOR_DISTRIBUTOR_TYPE = "DISTRIBUTOR_TYPE" ;

    public static final String COL_CATEGORY_CATEGORY_ID = "CATEGORY_ID";
    public static final String COL_CATEGORY_CATEGORY_NAME = "CATEGORY_NAME";
    public static final String COL_CATEGORY_CATEGORY_SNAME = "CATEGORY_SNAME";
    public static final String COL_CATEGORY_CATEGORY_ISACTIVE = "CATEGORY_ISACTIVE";
    public static final String COL_CATEGORY_CATEGORY_ENTERED_USER = "CATEGORY_ENTERED_USER";
    public static final String COL_CATEGORY_CATEGORY_ENTERED_DATE = "CATEGORY_ENTERED_DATE";

    //Columns of Revise SalesOrderJson table
    public static final String COL_REVISE_SALES_ORDER_JSON_InvoiceNumber= "InvoiceNumber";
    public static final String COL_REVISE_SALES_ORDER_JSON_JsonString= "JsonString";
    public static final String COL_REVISE_SALES_JSON_IsSync= "IsSync";

    //Columns of Invoice_json table
    public static final String COL_INVOICE_JSON_InvoiceNumber= "InvoiceNumber";
    public static final String COL_INVOICE_JSON_JsonString= "JsonString";
    public static final String COL_INVOICE_JSON_IsSync= "IsSync";


    //Columns of SalesOrderJson table
    public static final String COL_SALES_ORDER_JSON_InvoiceNumber= "InvoiceNumber";
    public static final String COL_SALES_ORDER_JSON_SalesOrderId= "SalesOrderId";
    public static final String COL_SALES_ORDER_JSON_JsonString= "JsonString";
    public static final String COL_SALES_ORDER_JSON_IsSync= "IsSync";

    //Columns of Return Inventory JSon table
    public static final String COL_RETURN_INVENTORY_JSON_creditNoteNo= "creditNoteNo";
    public static final String COL_RETURN_INVENTORY_JSON_JsonString= "JsonString";
    public static final String COL_RETURN_INVENTORY_JSON_IsSync= "IsSync";


    //column of Salesrep Inventory
    public static final String COL_SRI_PRODUCT_ID= "product_id";
    public static final String COL_SRI_BATCH_ID= "batch_id";
    public static final String COL_SRI_DISTRIBUTOR_ID= "distributor_id";
    public static final String COL_SRI_SALESREP_ID= "salesrep_id";
    public static final String COL_SRI_QTY_INHAND= "quantity_inhand";
    public static final String COL_SRI_REORDER_LEVEL= "reorder_level";
    public static final String COL_SRI_LAST_INVENTORYUPDATE= "inventory_update";

    //column of SALES ORDER SYNC Table
    public static final String COL_SALESORDER_SYNC_SalesOrderID= "SalesOrderID";
    public static final String COL_SALESORDER_SYNC_SaleStatus= "SaleStatus";
    public static final String COL_SALESORDER_SYNC_IsSync= "IsSync";

    //column of Return Inventory Line Item  Table
    public static final String COL_RETURN_INVENTORY_LINEITEM_creditNoteNo= "creditNoteNo";
    public static final String COL_RETURN_INVENTORY_LINEITEM_productId= "productId";
    public static final String COL_RETURN_INVENTORY_LINEITEM_batchId= "batchId";
    public static final String COL_RETURN_INVENTORY_LINEITEM_quantity= "quantity";
    public static final String COL_RETURN_INVENTORY_LINEITEM_totalAmount= "totalAmount";
    public static final String COL_RETURN_INVENTORY_LINEITEM_returnType= "returnType";
    public static final String COL_RETURN_INVENTORY_LINEITEM_isSellable= "isSellable";
    public static final String COL_RETURN_INVENTORY_LINEITEM_discountedPrice= "discountedPrice";
    public static final String COL_RETURN_INVENTORY_LINEITEM_productName= "productName";
    public static final String COL_RETURN_INVENTORY_LINEITEM_isSync= "isSync";
    public static final String COL_RETURN_INVENTORY_LINEITEM_unitPrice= "unitPrice";

    //column of Return Inventory Table
    public static final String COL_RETURN_INVENTORY_creditNoteNo= "creditNoteNo";
    public static final String COL_RETURN_INVENTORY_returnDate= "returnDate";
    public static final String COL_RETURN_INVENTORY_totalAmount= "totalAmount";
    public static final String COL_RETURN_INVENTORY_totalOutstanding= "totalOutstanding";
    public static final String COL_RETURN_INVENTORY_merchantId= "merchantId";
    public static final String COL_RETURN_INVENTORY_SalesRepId= "SalesRepId";
    public static final String COL_RETURN_INVENTORY_distributorId= "distributorId";
    public static final String COL_RETURN_INVENTORY_isSync= "isSync";
    public static final String COL_RETURN_INVENTORY_syncDate= "syncDate";


    //column of  Return Type table
    public static final String COL_RETURN_TYPE_ReturnType= "ReturnType";
    public static final String COL_RETURN_TYPE_ReturnTypeName= "ReturnTypeName";

    //Columns of INVOICE Items Tables
    public static final String COL_INVOICE_ITEM_VehicleID= "VehicleId";
    public static final String COL_INVOICE_ITEM_VehicleNumber= "VehicleNumber";
    public static final String COL_INVOICE_ITEM_DeliveryDate= "DeliveryDate";
    public static final String COL_INVOICE_ITEM_isDelivered= "IsDelivered";
    public static final String COL_INVOICE_ITEM_OusAmount= "OusAmount";
    public static final String COL_INVOICE_ITEM_InvoiceAmount= "InvoiceAmount";
    public static final String COL_INVOICE_ITEM_SyncDate= "SyncDate";
    public static final String COL_INVOICE_ITEM_IsSync= "IsSync";
    public static final String COL_INVOICE_ITEM_SalesStatus= "SaleStatus";
    public static final String COL_INVOICE_ITEM_EnteredUser= "EnteredUser";
    public static final String COL_INVOICE_ITEM_EnteredDate= "EnteredDate";
    public static final String COL_INVOICE_ITEM_InvoiceDate= "InvoiceDate";//
    public static final String COL_INVOICE_ITEM_SaleTypeId= "SaleTypeId";
    public static final String COL_INVOICE_ITEM_SalesRepId= "SalesRepId";
    public static final String COL_INVOICE_ITEM_DistributorId= "DistributorId";
    public static final String COL_INVOICE_ITEM_MerchantId= "MerchantId";
    public static final String COL_INVOICE_ITEM_InvoiceId= "InvoiceId";
    public static final String COL_INVOICE_ITEM_TotalVAT= "TotalVAT";
    public static final String COL_INVOICE_ITEM_salesRepName= "SalesRepName";
    public static final String COL_INVOICE_ITEM_salesRepContactNo= "SalesRepContactNo";
    public static final String COL_INVOICE_ITEM_merchantName= "MerchantName";
    public static final String COL_INVOICE_ITEM_buildingNo= "BuildingNo";
    public static final String COL_INVOICE_ITEM_address1= "Address1";
    public static final String COL_INVOICE_ITEM_address2= "Address2";
    public static final String COL_INVOICE_ITEM_city= "City";
    public static final String COL_INVOICE_ITEM_IsCredit= "IsCredit";
    public static final String COL_INVOICE_ITEM_CreditDays= "CreditDays";
    public static final String COL_INVOICE_ITEM_merchantContactNo= "MerchantContactNo";


    //columns of invloce line items
    public static final String COL_INVOICE_LINE_ITEM_batchID = "batchID";
    public static final String COL_INVOICE_LINE_ITEM_productID= "productID";
    public static final String COL_INVOICE_LINE_ITEM_invoiceID = "invoiceID";
    public static final String COL_INVOICE_LINE_ITEM_qty= "qty";
    public static final String COL_INVOICE_LINE_ITEM_freeQuantity= "freeQuantity";
    public static final String COL_INVOICE_LINE_ITEM_price= "price";
    public static final String COL_INVOICE_LINE_ITEM_lineTotal= "lineTotal";
    public static final String COL_INVOICE_LINE_ITEM_issync = "issync";
    public static final String COL_INVOICE_LINE_ITEM_syncDate = "syncDate";
    public static final String COL_INVOICE_LINE_ITEM_itemName= "itemName";


    //sync_status_tables
    public static final String COL_SYNC_ID= "Id";
    public static final String COL_SYNC_TABLE_NAME = "SyncTable";
    public static final String COL_SYNC_TABLE_TIME = "SyncTime";
    public static final String COL_SYNC_ROWCOUNT = "SyncRowCount";
    public static final String COL_SYNC_ROWCOUNT_NEW = "SyncRowCount_new";
    public static final String COL_SYNC_SALESREPID = "SalesRepId";
    public static final String COL_SYNC_LASTLOG_TIME =  "LastLogTime";
    public static final String COL_SYNC_STATUS= "SyncStatus";

    //Columns of Merchaan Class Table
    public static final String COL_MERCHANT_CLASS_ClassID= "ClassID";
    public static final String COL_MERCHANT_CLASS_ClassDescription= "ClassDescription";


    //Columns of  MERCHANT_TYPE Table
    public static final String COL_MERCHANT_TYPE_TypeCode= "TypeCode";
    public static final String COL_MERCHANT_TYPE_TypeDescription= "TypeDescription";

    //Columns table Utilization
    public static final String COL_TABLE_INVOICE_UTILIZATION_InvoiceId= "InvoiceId";
    public static final String COL_TABLE_INVOICE_UTILIZATION_CreditNoteNo= "CreditNoteNo";
    public static final String COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount= "UtilizedAmount";
    public static final String COL_TABLE_INVOICE_UTILIZATION_EnteredUser= "EnteredUser";
    public static final String COL_TABLE_INVOICE_UTILIZATION_EnteredDate= "EnteredDate";
    public static final String COL_TABLE_INVOICE_UTILIZATION_IsSync= "IsSync";

    //Columns table SYMC_ID
    public static final String COL_SYNC_ID_TABLE_ID= "TABLE_ID";
    public static final String COL_SYNC_ID_NEXT_ID= "NEXT_ID";

    //columns table service count
    public static final String COL_SER_COUNT_SALESREP_ID = "count_salesrep_id";
    public static final String COL_SER_COUNT_TBL_DISTRICT= "count_tbl_district";
    public static final String COL_SER_COUNT_TBL_PRODUCT_CAT= "count_tbl_productcat";
    public static final String COL_SER_COUNT_TBL_PRODUCT= "count_tbl_product";
    public static final String COL_SER_COUNT_TBL_PRODUCT_BATCH= "count_tbl_productbatch";
    public static final String COL_SER_COUNT_TBL_RETURN_TYPE= "count_tbl_returntype";
    public static final String COL_SER_COUNT_TBL_PATH= "count_tbl_path";
    public static final String COL_SER_COUNT_TBL_MERCHANT= "count_tbl_merchant";
    public static final String COL_SER_COUNT_TBL_SALES_ORDER= "count_tbl_salesorder";
    public static final String COL_SER_COUNT_TBL_VEHICLE_INVEN= "count_tbl_vehicleinven";
    public static final String COL_SER_COUNT_TBL_SALESREP_INVEN= "count_tbl_salesrepinven";
    public static final String COL_SER_COUNT_TBL_INVOICE= "count_tbl_invoice";
    public static final String COL_SER_COUNT_TBL_MERCHANT_STOCK= "count_tbl_merchantstock";
    public static final String COL_SER_COUNT_TBL_MERCHANT_REASON= "count_tbl_merchantreason";
    public static final String COL_SER_COUNT_TBL_MERCHANT_VISIT_COUNTS= "count_tbl_merchant_visitcounts";

    public static final String COL_SER_COUNT_TBL_PRIMARY_INVOICE_COUNT= "count_tbl_primary_invoice_count";
    public static final String COL_SER_COUNT_TBL_TARGET_COUNT= "count_tbl_target_count";
    public static final String COL_SER_COUNT_TBL_TARGET_CATEGORY_COUNT= "count_tbl_target_category_count";
    public static final String COL_SER_COUNT_ACCORDINTO_DATE = "count_accordingto_date";
    public static final String COL_SER_COUNT_UPDATED_DATE_TIME = "count_updated_time";


    //Columns table SYMC_ID
    public static final String TABLE_IMAGE= "MERCHANT_IMAGE";
    public static final String COL_IMAGE1= "IMAGE1";
    public static final String COL_IMAGE2= "IMAGE2";
    public static final String COL_IMAGE_MERCHANT_ID= "MERCHANT_ID";
    public static final String COL_IMAGE_SEQUENCE_ID= "SEQUENCE_ID";
    public static final String COL_IMAGE_ISSYNC= "ISSYNC";

    //merchant stock
    public static final String COL_MSTOCK_merchant_stock_id = "Merchant_stockid";
    public static final String COL_MSTOCK_merchant_id = "Merchant_id";
    public static final String COL_MSTOCK_distributor_id = "Distributor_id";
    public static final String COL_MSTOCK_salesrep_id = "Salesrep_id";
    public static final String COL_MSTOCK_entered_date = "Entered_date";
    public static final String COL_MSTOCK_entered_user = "Entered_user";
    public static final String COL_MSTOCK_sales_status = "Sales_status";
    public static final String COL_MSTOCK_is_sync = "Is_sync";
    public static final String COL_MSTOCK_sync_date = "Sync_date";
    public static final String COL_MSTOCK_updated_user= "Updated_user";
    public static final String COL_MSTOCK_updated_date= "Updated_date";

    //merchant stock line item
    public static final String COL_MS_LINEITEM_merchant_stock_id = "Merchant_stock_id";
    public static final String COL_MS_LINEITEM_product_name = "Product_name";
    public static final String COL_MS_LINEITEM_product_id = "Product_id";
    public static final String COL_MS_LINEITEM_quantity = "Quantity";
    public static final String COL_MS_LINEITEM_is_sync = "Is_sync";
    public static final String COL_MS_LINEITEM_sync_date = "Sync_date";


    //Columns of TABLE SYNC_ATTENDANCE
    public static final String COL_SYNC_ATTENDANCE_ID = "id";
    public static final String COL_SYNC_ATTENDANCE_DATE = "attendance_date";
    public static final String COL_SYNC_ATTENDANCE_DISTRIBUTR_ID = "distributer_id";
    public static final String COL_SYNC_ATTENDANCE_SALESREP_ID = "salesrep_id";
    public static final String COL_SYNC_ATTENDANCE_TIME_IN= "time_in";
    public static final String COL_SYNC_ATTENDANCE_TIME_OUT= "time_out";
    public static final String COL_SYNC_ATTENDANCE_BATTERY_IN= "battery_in";
    public static final String COL_SYNC_ATTENDANCE_BATTERY_OUT= "battery_out";
    public static final String COL_SYNC_ATTENDANCE_LONGITUDE_IN= "longitude_in";
    public static final String COL_SYNC_ATTENDANCE_LATITUDE_IN= "latitude_in";
    public static final String COL_SYNC_ATTENDANCE_LONGITUDE_OUT= "longitude_out";
    public static final String COL_SYNC_ATTENDANCE_LATITUDE_OUT= "latitude_out";
    public static final String COL_SYNC_ATTENDANCE_IS_SYNC_IN= "is_sync_in";
    public static final String COL_SYNC_ATTENDANCE_IS_SYNC_OUT= "is_sync_out";

    //columns of merchant visit reason
    public static final String COL_MVISIT_REASON_REASON_ID = "reason_id";
    public static final String COL_MVISIT_REASON_DESCRIPTION = "description";
    public static final String COL_MVISIT_REASON_ALLOWSTOCK = "allow_stock";

    //columns of merchant visit
    public static final String COL_MVISIT_MERCHANT_ID = "merchant_id";
    public static final String COL_MVISIT_REASON_ID = "reason_id";
    public static final String COL_MVISIT_SALESREP_ID = "salesrep_id";
    public static final String COL_MVISIT_DISTRIBUTOR_ID = "distributor_id";
    public static final String COL_MVISIT_DELIVERYPATH_ID = "delivery_pathid";
    public static final String COL_MVISIT_ENTERED_DATE = "entered_date";
    public static final String COL_MVISIT_ISSYNC = "is_sync";
    public static final String COL_MVISIT_ENTERED_USER = "entered_user";


    //columns of TABLE_TARGET_CATEGORY
    public static final String COL_TARGET_CATEGORY_targetCategoryId= "targetCategoryId";
    public static final String COL_TARGET_CATEGORY_targetCategoryName= "targetCategoryName";
    public static final String COL_TARGET_CATEGORY_unitOfMeasurement= "unitOfMeasurement";
    public static final String COL_TARGET_CATEGORY_enteredDate= "enteredDate";
    public static final String COL_TARGET_CATEGORY_enterdUser= "enterdUser";


    //columns of TABLE_TARGET_CATEGORY_LINE_ITEM
    public static final String COL_TARGET_CATEGORY_LINE_ITEM_targetCategoryId= "targetCategoryId";
    public static final String COL_TARGET_CATEGORY_LINE_ITEM_productId= "productId";
    public static final String COL_TARGET_CATEGORY_LINE_ITEM_unitPerQuantity= "unitPerQuantity";
    public static final String COL_TARGET_CATEGORY_LINE_ITEM_enteredDate= "enteredDate";
    public static final String COL_TARGET_CATEGORY_LINE_ITEM_enterdUser= "enterdUser";


    //columns of TABLE_SALESREP_TARGET

    public static final String COL_SALESREP_TARGET_salesRepId= "salesRepId";
    public static final String COL_SALESREP_TARGET_targetCategoryId= "targetCategoryId";
    public static final String COL_SALESREP_TARGET_targetQty = "targetQty";
    public static final String COL_SALESREP_TARGET_year= "year";
    public static final String COL_SALESREP_TARGET_month= "month";


    //Columns of TABLE_PRIMARY_SALES_INVOICE
    public static final String COL_PRIMARY_SALES_INVOICE_InvoiceNumber= "InvoiceNumber";
    public static final String COL_PRIMARY_SALES_INVOICE_OrderDate= "OrderDate";
    public static final String COL_PRIMARY_SALES_INVOICE_StatusId= "StatusId";
    public static final String COL_PRIMARY_SALES_INVOICE_EstimatedDeliveryDate= "EstimatedDeliveryDate";
    public static final String COL_PRIMARY_SALES_INVOICE_ExpectedDeliveryDate= "ExpectedDeliveryDate";
    public static final String COL_PRIMARY_SALES_INVOICE_DeliveredDate= "DeliveredDate";
    public static final String COL_PRIMARY_SALES_INVOICE_SalesInvoiceNumber= "SalesInvoiceNumber";
    public static final String COL_PRIMARY_SALES_INVOICE_SalesRepID= "SalesRepID";


    //Columns of TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM
    public static final String COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId= "ProductId";
    public static final String COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber= "InvoiceNumber";
    public static final String COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty= "OrderQty";
    public static final String COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit= "SizeOfUnit";


    //columns of credit type
    public static final String COL_CT_ID= "credittype_id";
    public static final String COL_CT_NAME= "credittype_name";



    //columns of Table Mileage
    public static final String COL_TABLE_MILEAGE_id= "id";
    public static final String COL_TABLE_MILEAGE_markedTime= "markedTime";
    public static final String COL_TABLE_MILEAGE_type= "type";
    public static final String COL_TABLE_MILEAGE_salesRepId= "salesRepId";


    //columns of Table sync  Mileage
    public static final String COL_TABLE_SYNC_MILEAGE_mileageDate= "mileageDate";
    public static final String COL_TABLE_SYNC_MILEAGE_salesRepId= "salesRepId";
    public static final String COL_TABLE_SYNC_MILEAGE_mileageInTime= "mileageInTime";
    public static final String COL_TABLE_SYNC_MILEAGE_mileageIn= "mileageIn";
    public static final String COL_TABLE_SYNC_MILEAGE_mileageOutTime= "mileageOutTime";
    public static final String COL_TABLE_SYNC_MILEAGE_mileageOut= "mileageOut";
    public static final String COL_TABLE_SYNC_MILEAGE_isSyncIn= "isSyncIn";
    public static final String COL_TABLE_SYNC_MILEAGE_isSyncOut= "isSyncOut";
    public static final String COL_TABLE_SYNC_MILEAGE_syncDate= "syncDate";
    public static final String COL_TABLE_SYNC_MILEAGE_enteredUser= "enteredUser";

    //columns of Table PROMOTION
    public static final String COL_PROMOTION_ProductId = "ProductId";
    public static final String COL_PROMOTION_BatchId = "BatchId";
    public static final String COL_PROMOTION_CatalogueType = "CatalogueType";
    public static final String COL_PROMOTION_StartDate = "StartDate";
    public static final String COL_PROMOTION_EndDate = "EndDate";
    public static final String COL_PROMOTION_EnteredUser = "EnteredUser";
    public static final String COL_PROMOTION_EnteredDate = "EnteredDate";
    public static final String COL_PROMOTION_IMAGE_INDEX = "ImageIndex";

    //column of table PROMOTION IMAGE
    public static final String COL_PROMOTION_IMAGE_img = "img_image";
    public static final String COL_PROMOTION_IMAGE_productid = "img_productid";
    public static final String COL_PROMOTION_IMAGE_batchid = "img_batchid";
    public static final String COL_PROMOTION_IMAGE_catalogue_type = "img_cat_type";


    String time = (new DateManager()).getDateWithTime();

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql_user_attendace = "create table attendance(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userId TEXT," +
                "attendace_date INTEGER," +
                "type INTEGER," +
                "sequence_id INTEGER," +
                "lat TEXT," +
                "lon TEXT," +
                "is_synced INTEGER," +
                "created_at INTEGER," +
                "venue TEXT," +
                "venueCode INTEGER)";
        System.out.println("sql_user_attendace - "+sql_user_attendace);
        db.execSQL(sql_user_attendace);

        String sql25_actual_path = "CREATE TABLE " + TBL25 + " (" +
                COL25_SEQUENCE_ID + " INTEGER  NOT NULL PRIMARY KEY    AUTOINCREMENT," +
                COL25_ROUTE_DATE + " TEXT  NOT NULL  ," +
                COL25_DISTRIBUTER_ID + " INTEGER  NOT NULL  ," +
                COL25_SALES_REP_ID + " INTEGER  NOT NULL  ," +
                COL_25_USER_NAME + " TEXT NOT NULL ,"+
                COL25_LOCATION_TIME + " TEXT  NULL  ," +
                COL25_LONGITUDE + " REAL  NULL  ," +
                COL25_LATITUDE + " REAL  NULL  ," +
                COL25_IS_SYNC + " INTEGER  NULL  ," +
                COL25_SYNC_DATE + " TEXT  NULL  ," +
                COL25_ACCURACY + " REAL  NULL);";
        System.out.println("sql25_actual_path - "+sql25_actual_path);
        db.execSQL(sql25_actual_path);


        String sql_area = "CREATE TABLE " + TABLE_AREA + " (" +
                TABLE_AREA_AREA_CODE + " TEXT," +
                TABLE_AREA_AREA_NAME + " TEXT  NULL);";
        System.out.println("sql_area - "+sql_area);
        db.execSQL(sql_area);

        String sql_district = "CREATE TABLE " + TABLE_DISTRICT + " (" +
                TABLE_AREA_DISTRICT_CODE + " TEXT," +
                TABLE_DISTRICT_DISTRICT_NAME + " TEXT  NULL);";
        System.out.println("sql_district - "+sql_district);
        db.execSQL(sql_district);

        String sql_path = "CREATE TABLE " + TABLE_PATH + " (" +
                TABLE_PATH_DELIVERYPATHID + " INTEGER," +
                TABLE_PATH_PATHNAME + " TEXT  NULL);";
        System.out.println("sql_path - "+sql_path);
        db.execSQL(sql_path);

        //
        String sql_sync_id = "CREATE TABLE " + TABLE_SYNC_ID + " (" +
                COL_SYNC_ID_TABLE_ID + " INTEGER," +
                COL_SYNC_ID_NEXT_ID + " INTEGER);";
        System.out.println("sql_sync_id - "+sql_sync_id);
        db.execSQL(sql_sync_id);


        String sql_def_merchant = "CREATE TABLE " + TABLE_MERCHANT + " (" +
                TBL_MERCHANT_SEQUENCE_ID + " INTEGER  NOT NULL PRIMARY KEY    AUTOINCREMENT," +
                TBL_MERCHANT_MERCHANT_ID + " INTEGER ," +
                TBL_MERCHANT_MERCHANT_NAME + " TEXT ," +
                TBL_MERCHANT_CONTACT_NO1 + " TEXT ," +
                TBL_MERCHANT_CONTACT_NO2 + " TEXT ,"+
                TBL_MERCHANT_LONGITUDE + " TEXT ," +
                TBL_MERCHANT_LATITUDE + " TEXT ," +
                TBL_MERCHANT_IS_ACTIVE + " INTEGER ," +
                TBL_MERCHANT_ENTERED_DATE + " TEXT ," +
                TBL_MERCHANT_ENTERED_USER + " INTEGER ," +
                TBL_MERCHANT_DISCOUNT_RATE + " INTEGER ," +
                TBL_MERCHANT_IS_SYNC + " INTEGER ," +
                TBL_MERCHANT_SYNC_DATE + " TEXT ," +
                TBL_MERCHANT_BUILDING_NO + " TEXT ," +
                TBL_MERCHANT_ADDRESS_1 + " TEXT ," +
                TBL_MERCHANT_ADDRESS_2 + " TEXT  ," +
                TBL_MERCHANT_CITY + " TEXT ," +
                TBL_MERCHANT_CONTACT_PERSON + " TEXT ," +
                TBL_MERCHANT_AREA_CODE + " INTEGER ," +
                TBL_MERCHANT_PATH_CODE + " INTEGER ," +
                TBL_MERCHANT_MERCHANT_TYPE + " TEXT ," +
                TBL_MERCHANT_MERCHANT_CLASS + " TEXT ," +
                TBL_MERCHANT_DISTRICT_CODE + " INTEGER ," +
                TBL_MERCHANT_UPDATED_USER + " TEXT ," +
                TBL_MERCHANT_UPDATED_DATE + " TEXT ," +
                TBL_MERCHANT_REFERENCE_ID + " TEXT ," +
                TBL_MERCHANT_ISVAT + " INTEGER  ," +
                TBL_MERCHANT_VAT_NO + " TEXT ,"+
                TBL_MERCHANT_SYNC_ID  + " TEXT ,"+
                TBL_MERCHANT_IS_CREDIT  + " INTEGER ,"+
                TBL_MERCHANT_CREDIT_DAYS + " TEXT );";
        System.out.println("sql_def_merchant - "+sql_def_merchant);
        db.execSQL(sql_def_merchant);


        String sql_delivery_locations = "CREATE TABLE " + TABLE_DELIVERY_LOCATIONS + " (" +
                TABLE_DELIVERY_LOCATION_MERCHANT_ID + " INTEGER," +
                TABLE_DELIVERY_LOCATION_DELIVERY_PATH_ID + " INTEGER  NULL);";
        System.out.println("sql_delivery_locations - "+sql_delivery_locations);
        db.execSQL(sql_delivery_locations);


        String sql_def_sales_rep = "CREATE TABLE " + TABLE_SALES_REP + " (" +
                COL_SALES_REP_SalesRepId+ " INTEGER ," +
                COL_SALES_REP_DistributorId + " INTEGER ," +
                COL_SALES_REP_SalesRepName + " TEXT ," +
                COL_SALES_REP_EpfNumber + " INTEGER ,"+
                COL_SALES_REP_IsActive + " INTEGER ," +
                COL_SALES_REP_EnteredUser + " TEXT ," +
                COL_SALES_REP_EnteredDate + " TEXT ," +
                COL_SALES_REP_UpdatedUser + " TEXT ," +
                COL_SALES_REP_UpdatedDate + " TEXT ," +
                COL_SALES_REP_ReferenceID + " TEXT ," +
                COL_SALES_REP_NextCreditNoteNo + " INTEGER ," +
                COL_SALES_REP_NextSalesOrderNo + " INTEGER ," +
                COL_SALES_REP_NextInvoiceNo + " INTEGER  ," +
                COL_SALES_REP_SalesRepType + " TEXT ,"+
                COL_SALES_REP_ContactNo + " TEXT );";
        System.out.println("sql_def_sales_rep - "+sql_def_sales_rep);
        db.execSQL(sql_def_sales_rep);


        String sql_def_sales_order = "CREATE TABLE " + TABLE_SALES_ORDER + " (" +
                COL_SALES_ORDER_InvoiceNumber+ " INTEGER ," +
                COL_SALES_ORDER_EstimateDeliveryDate + " TEXT ," +
                COL_SALES_ORDER_TotalDiscount + " REAL ," +
                COL_SALES_ORDER_TotalAmount + " REAL ,"+
                COL_SALES_ORDER_SyncDate + " TEXT ," +
                COL_SALES_ORDER_IsSync + " INTEGER ," +
                COL_SALES_ORDER_SaleStatus + " INTEGER ," +
                COL_SALES_ORDER_EnteredUser + " TEXT ," +
                COL_SALES_ORDER_EnteredDate + " TEXT ," +
                COL_SALES_ORDER_UpdatedUser + " TEXT ," +
                COL_SALES_ORDER_UpdatedDate + " TEXT ," +
                COL_SALES_ORDER_SaleDate + " TEXT ," +
                COL_SALES_ORDER_SaleTypeId + " TEXT ," +
                COL_SALES_ORDER_SalesRepId + " INTEGER ," +
                COL_SALES_ORDER_DitributorId + " INTEGER  ," +
                COL_SALES_ORDER_MerchantId + " INTEGER  ," +
                COL_SALES_ORDER_SalesOrderId + " INTEGER  ," +
                COL_SALES_ORDER_TotalVAT + " REAL  ," +
                COL_SALES_ORDER_IsVat + " INTEGER  ," +
                COL_SALES_ORDER_IsReturn + " INTEGER  ," +
                COL_SALES_ORDER_CreditType + " INTEGER  ," +
                COL_SALES_ORDER_CreditDate + " INTEGER  ," +
                COL_SALES_ORDER_MerchntName + " TEXT );";
        System.out.println("sql_def_sales_order - "+sql_def_sales_order);
        db.execSQL(sql_def_sales_order);


        String sql_line_item = "CREATE TABLE " + TABLE_LINE_ITEM + " (" +
                COL_LINE_ITEM_SalesOrderID+ " INTEGER ," +
                COL_LINE_ITEM_BatchID + " INTEGER ," +
                COL_LINE_ITEM_ProductID + " INTEGER ," +
                COL_LINE_ITEM_CurrentStock + " INTEGER ," +
                COL_LINE_ITEM_Quantity + " INTEGER ,"+
                COL_LINE_ITEM_FreeQuantity + " INTEGER ," +
                COL_LINE_ITEM_UnitSellingPrice + " REAL ," +
                COL_LINE_ITEM_UnitSellingDiscount + " REAL ," +
                COL_LINE_ITEM_TotalAmount + " REAL ," +
                COL_LINE_ITEM_TotalDiscount + " REAL ," +
                COL_LINE_ITEM_IsSync + " INTEGER ," +
                COL_LINE_ITEM_SyncDate + " TEXT );";

        System.out.println("sql line item - "+sql_line_item);
        db.execSQL(sql_line_item);


        String sql_product = "CREATE TABLE " + TABLE_PRODUCT + " (" +
                COL_PRODUCT_ProductId+ " INTEGER ," +
                COL_PRODUCT_PackingTypeId+ " INTEGER ," +
                COL_PRODUCT_SellingCategoryId + " INTEGER ," +
                COL_PRODUCT_CompanyId + " INTEGER ,"+
                COL_PRODUCT_CategoryId + " INTEGER ," +
                COL_PRODUCT_ProductName + " TEXT ," +
                COL_PRODUCT_IsActive + " INTEGER ," +
                COL_PRODUCT_EnteredDate + " TEXT ," +
                COL_PRODUCT_EnteredUser + " TEXT ," +
                COL_PRODUCT_UnitSellingPrice + " REAL ," +
                COL_PRODUCT_UnitDistributorPrice + " REAL ," +
                COL_PRODUCT_ReferenceID + " TEXT ," +
                COL_PRODUCT_IsVat + " INTEGER ," +
                COL_PRODUCT_VatRate + " INTEGER ," +
                COL_PRODUCT_SizeOfUnit + " INTEGER ," +
                COL_PRODUCT_AddToCard + " INTEGER );";
        System.out.println("sql product - "+sql_product);
        db.execSQL(sql_product);

        String sql_product_batch = "CREATE TABLE " + TABLE_PRODUCT_BATCH + " (" +
                COL_PRODUCT_BATCH_ProductId+ " INTEGER ," +
                COL_PRODUCT_BATCH_BatchID + " INTEGER ," +
                COL_PRODUCT_BATCH_Quantity + " INTEGER ," +
                COL_PRODUCT_BATCH_BlockQuantity + " INTEGER ,"+
                COL_PRODUCT_BATCH_UnitDealrePrice + " REAL ," +
                COL_PRODUCT_BATCH_UnitSellingPrice + " REAL ," +
                COL_PRODUCT_BATCH_IsAllSold + " INTEGER ," +
                COL_PRODUCT_BATCH_ExpiryDate + " TEXT ," +
                COL_PRODUCT_BATCH_EnteredUser + " TEXT ," +
                COL_PRODUCT_BATCH_EnteredDate + " TEXT );";

        System.out.println("sql batch - "+sql_product_batch);
        db.execSQL(sql_product_batch);

        String sql_vehicle_inventory = "CREATE TABLE " + TABLE_VEHICALE_INVENTORY + " (" +
                COL_VEHICLE_INVENTORY_ProductId+ " INTEGER ," +
                COL_VEHICLE_INVENTORY_BatchID + " INTEGER ," +
                COL_VEHICLE_INVENTORY_DistributorID + " INTEGER ," +
                COL_VEHICLE_INVENTORY_SalesRepID + " INTEGER ,"+
                COL_VEHICLE_INVENTORY_LoadQuantity + " INTEGER ," +
                COL_VEHICLE_INVENTORY_OutstandingQuantity + " INTEGER ," +
                COL_VEHICLE_INVENTORY_DiscountRule + " TEXT ," +
                COL_VEHICLE_INVENTORY_FreeIssueRule + " TEXT ," +
                COL_VEHICLE_INVENTORY_UnitSellingPrice + " REAL ," +
                COL_VEHICLE_INVENTORY_InventoryDate + " TEXT );";

        System.out.println("sql inventory - "+sql_vehicle_inventory);
        db.execSQL(sql_vehicle_inventory);

        String sql_distributor = "CREATE TABLE "+TABLE_DISTRIBUTOR+" (" +
                COL_DISTRIBUTOR_DISTRIBUTOR_ID+ " INTEGER  AUTO_INCREMENT," +
                COL_DISTRIBUTOR_REGION_ID+ " TEXT NULL ," +
                COL_DISTRIBUTOR_AREA_CODE+ " TEXT NULL ," +
                COL_DISTRIBUTOR_TERITORY_ID+ " INTEGER NULL ," +
                COL_DISTRIBUTOR_DISTRIBUTOR_NAME+ " TEXT  ," +
                COL_DISTRIBUTOR_CONTACT_NUMBER+ " TEXT NULL ," +
                COL_DISTRIBUTOR_ADDRESS_1+ " TEXT  NULL," +
                COL_DISTRIBUTOR_ADDRESS_2+ " TEXT NULL ," +
                COL_DISTRIBUTOR_ADDRESS_3+ " TEXT NULL ," +
                COL_DISTRIBUTOR_ADDRESS_4+ " TEXT NULL ," +
                COL_DISTRIBUTOR_EMAIL+ " TEXT NULL ," +
                COL_DISTRIBUTOR_PARTNER_ID+ " INTEGER  NULL," +
                COL_DISTRIBUTOR_IS_SYNC+ " INTEGER  ," +
                COL_DISTRIBUTOR_SYNC_DATE+ " TEXT  ," +
                COL_DISTRIBUTOR_REFERENCE_ID+ " TEXT  ," +
                COL_DISTRIBUTOR_IS_VAT+ " INTEGER  ," +
                COL_DISTRIBUTOR_VAT_NO+ " TEXT  ," +
                COL_DISTRIBUTOR_UPDATED_USER+ " TEXT  ," +
                COL_DISTRIBUTOR_UPDATED_DATE+ " TEXT  ," +
                COL_DISTRIBUTOR_ENTERED_USER+ " TEXT  ," +
                COL_DISTRIBUTOR_ENTERED_DATE+ " TEXT  ," +
                COL_DISTRIBUTOR_DISTRIBUTOR_TYPE+ " TEXT  ," +
                "PRIMARY KEY ( "+COL_DISTRIBUTOR_DISTRIBUTOR_ID+" ) );";

        System.out.println("sql dist "+sql_distributor);
        db.execSQL(sql_distributor);

        String sql_category  = "CREATE TABLE "+TABLE_CATEGORY+" (" +
                COL_CATEGORY_CATEGORY_ID+ " INTEGER  AUTO_INCREMENT," +
                COL_CATEGORY_CATEGORY_NAME+ " TEXT NULL ," +
                COL_CATEGORY_CATEGORY_SNAME+ " TEXT NULL ," +
                COL_CATEGORY_CATEGORY_ISACTIVE+ " INTEGER NULL ," +
                COL_CATEGORY_CATEGORY_ENTERED_DATE+ " TEXT NULL ," +
                COL_CATEGORY_CATEGORY_ENTERED_USER+ " TEXT NULL ," +
                "PRIMARY KEY ( "+COL_CATEGORY_CATEGORY_ID+" ) );";
        System.out.println("sql_category - "+sql_category);
        db.execSQL(sql_category);

        String sql_revise_order = "CREATE TABLE " + TABLE_REVISE_ORDER + " (" +
                COL_REVISE_SALES_ORDER_ProductId+ " INTEGER ," +
                COL_REVISE_SALES_ORDER_Batch_Id + " INTEGER ," +
                COL_REVISE_SALES_UnitSellingPrice + " REAL ," +
                COL_REVISE_SALES_ExpiryDate + " TEXT ,"+
                COL_REVISE_SALES_ProductName + " TEXT ," +
                COL_REVISE_SALES_CategoryId + " INTEGER ," +
                COL_REVISE_SALES_IsVat + " INTEGER ," +
                COL_REVISE_SALES_VatRate + " REAL ," +
                COL_REVISE_STOCK_R + " INTEGER ," +
                COL_REVISE_STOCK_P + " INTEGER );";
        System.out.println("sql_revise_order - "+sql_revise_order);
        db.execSQL(sql_revise_order);

        String sql_revise_order_json = "CREATE TABLE " + TABLE_REVISE_ORDER_JSON + " (" +
                COL_REVISE_SALES_ORDER_JSON_InvoiceNumber+ " INTEGER ," +
                COL_REVISE_SALES_ORDER_JSON_JsonString + " TEXT ," +
                COL_REVISE_SALES_JSON_IsSync + " INTEGER );";
        System.out.println("sql_revise_order_json - "+sql_revise_order_json);
        db.execSQL(sql_revise_order_json);


        //Invoice json table
        String sql_invoice_json = "CREATE TABLE " + TABLE_INVOICE_JSON + " (" +
                COL_INVOICE_JSON_InvoiceNumber+ " INTEGER ," +
                COL_INVOICE_JSON_JsonString + " TEXT ," +
                COL_INVOICE_JSON_IsSync + " INTEGER );";
        System.out.println("sql_invoice_json - "+sql_invoice_json);
        db.execSQL(sql_invoice_json);

        String sql_sales_order_json = "CREATE TABLE " + TABLE_SALES_ORDER_JSON + " (" +
                COL_SALES_ORDER_JSON_InvoiceNumber+ " INTEGER ," +
                COL_SALES_ORDER_JSON_SalesOrderId+ " INTEGER ," +
                COL_SALES_ORDER_JSON_JsonString + " TEXT ," +
                COL_SALES_ORDER_JSON_IsSync + " INTEGER );";
        System.out.println("sql_sales_order_json - "+sql_sales_order_json);
        db.execSQL(sql_sales_order_json);


        String sql_return_inventory_json = "CREATE TABLE " + TABLE_RETURN_INVENTORY_JSON + " (" +
                COL_RETURN_INVENTORY_JSON_creditNoteNo+ " INTEGER ," +
                COL_RETURN_INVENTORY_JSON_JsonString + " TEXT ," +
                COL_RETURN_INVENTORY_JSON_IsSync + " INTEGER );";
        System.out.println("sql_return_inventory_json - "+sql_return_inventory_json);
        db.execSQL(sql_return_inventory_json);

        String sql_salesrep_inventory = "CREATE TABLE "+TABLE_SALESREP_INVENTORY+" ( "+
                COL_SRI_PRODUCT_ID + " INTEGER , "+
                COL_SRI_BATCH_ID + " INTEGER , "+
                COL_SRI_DISTRIBUTOR_ID + " INTEGER , "+
                COL_SRI_SALESREP_ID + " INTEGER , "+
                COL_SRI_QTY_INHAND + " INTEGER , "+
                COL_SRI_REORDER_LEVEL + " INTEGER , "+
                COL_SRI_LAST_INVENTORYUPDATE + " TEXT );";
        System.out.println("sql_salesrep_inventory - "+sql_salesrep_inventory);
        db.execSQL(sql_salesrep_inventory);

        String sql_sales_orders_sync= "CREATE TABLE "+TABLE_SALESORDERS_SYNC+" ( "+
                COL_SALESORDER_SYNC_SalesOrderID + " INTEGER , "+
                COL_SALESORDER_SYNC_SaleStatus+ " INTEGER , "+
                COL_SALESORDER_SYNC_IsSync+ " INTEGER );";
        System.out.println("sql_sales_orders_sync - "+sql_sales_orders_sync);
        db.execSQL(sql_sales_orders_sync);


        String sql_return_type= "CREATE TABLE "+TABLE_RETURN_TYPE+" ( "+
               COL_RETURN_TYPE_ReturnType + " INTEGER , "+
               COL_RETURN_TYPE_ReturnTypeName+ " TEXT );";
        System.out.println("sql_return_type - "+sql_return_type);
        db.execSQL(sql_return_type);


        String sql_return_inventory= "CREATE TABLE "+ TABLE_RETURN_INVENTORY +" ( "+
                COL_RETURN_INVENTORY_creditNoteNo + " INTEGER , "+
                COL_RETURN_INVENTORY_returnDate + " TEXT , "+
                COL_RETURN_INVENTORY_totalAmount + " REAL , "+
                COL_RETURN_INVENTORY_totalOutstanding + " REAL , "+
                COL_RETURN_INVENTORY_merchantId + " INTEGER , "+
                COL_RETURN_INVENTORY_SalesRepId + " INTEGER , "+
                COL_RETURN_INVENTORY_distributorId + " INTEGER , "+
                COL_RETURN_INVENTORY_isSync + " INTEGER , "+
                COL_RETURN_INVENTORY_syncDate+ " TEXT );";
        System.out.println("sql_return_inventory - " + sql_return_inventory);
        db.execSQL(sql_return_inventory);

        //Merchant Class Table
        String sql_merchant_class= "CREATE TABLE "+ TABLE_MERCHANT_CLASS +" ( "+
                COL_MERCHANT_CLASS_ClassID + " INTEGER , "+
                COL_MERCHANT_CLASS_ClassDescription+ " TEXT );";
        System.out.println("sql_merchant_class - " + sql_merchant_class);
        db.execSQL(sql_merchant_class);


        //Merchant Type Table
        String sql_merchant_type= "CREATE TABLE "+ TABLE_MERCHANT_TYPE +" ( "+
                COL_MERCHANT_TYPE_TypeCode + " INTEGER , "+
                COL_MERCHANT_TYPE_TypeDescription+ " TEXT );";
        System.out.println("sql_merchant_type - " + sql_merchant_type);
        db.execSQL(sql_merchant_type);

        String sql_return_inventory_lineitem= "CREATE TABLE "+TABLE_RETURN_INVENTORY_LINEITEM+" ( "+
                COL_RETURN_INVENTORY_LINEITEM_creditNoteNo + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_productId + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_batchId + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_quantity + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_totalAmount + " REAL , "+
                COL_RETURN_INVENTORY_LINEITEM_returnType + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_isSellable + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_discountedPrice + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_productName + " TEXT , "+
                COL_RETURN_INVENTORY_LINEITEM_isSync + " INTEGER , "+
                COL_RETURN_INVENTORY_LINEITEM_unitPrice+ " TEXT );";
        System.out.println("sql_return_inventory_lineitem - "+sql_return_inventory_lineitem);
        db.execSQL(sql_return_inventory_lineitem);


        String sql_invoice_item = "CREATE TABLE " + TBL_INVOICE_ITEM + " (" +
                COL_INVOICE_ITEM_VehicleID+ " INTEGER ," +
                COL_INVOICE_ITEM_VehicleNumber + " TEXT ," +
                COL_INVOICE_ITEM_DeliveryDate + " TEXT ," +
                COL_INVOICE_ITEM_isDelivered + " INTEGER ,"+
                COL_INVOICE_ITEM_OusAmount + " REAL ," +
                COL_INVOICE_ITEM_InvoiceAmount + " REAL ," +
                COL_INVOICE_ITEM_SyncDate + " TEXT ," +
                COL_INVOICE_ITEM_IsSync + " INTEGER ," +
                COL_INVOICE_ITEM_SalesStatus + " INTEGER ," +
                COL_INVOICE_ITEM_EnteredUser + " TEXT ," +   //this field is text
                COL_INVOICE_ITEM_EnteredDate + " TEXT ," +
                COL_INVOICE_ITEM_InvoiceDate + " TEXT ," +
                COL_INVOICE_ITEM_SaleTypeId + " INTEGER ," +
                COL_INVOICE_ITEM_SalesRepId + " INTEGER ," +
                COL_INVOICE_ITEM_DistributorId + " INTEGER ," +
                COL_INVOICE_ITEM_MerchantId + " INTEGER ," +
                COL_INVOICE_ITEM_InvoiceId + " INTEGER ," +
                COL_INVOICE_ITEM_TotalVAT + " REAL ," +
                COL_INVOICE_ITEM_salesRepName + " TEXT ," +
                COL_INVOICE_ITEM_salesRepContactNo + " TEXT ," +
                COL_INVOICE_ITEM_merchantName + " TEXT ," +
                COL_INVOICE_ITEM_buildingNo + " TEXT ," +
                COL_INVOICE_ITEM_address1 + " TEXT ," +
                COL_INVOICE_ITEM_address2 + " TEXT ," +
                COL_INVOICE_ITEM_city + " TEXT ," +
                COL_INVOICE_ITEM_IsCredit + " INTEGER ," +
                COL_INVOICE_ITEM_CreditDays + " TEXT ," +
                COL_INVOICE_ITEM_merchantContactNo + " TEXT " +
                ");";

        System.out.println("sql INVOICE line item - "+sql_invoice_item);
        db.execSQL(sql_invoice_item);

        String sql_invoice_line_item = "CREATE TABLE " + TBL_INVOICE_LINE_ITEM + " (" +
                COL_INVOICE_LINE_ITEM_batchID+ " INTEGER ," +
                COL_INVOICE_LINE_ITEM_productID+ " INTEGER ," +
                COL_INVOICE_LINE_ITEM_invoiceID+ " INTEGER ," +
                COL_INVOICE_LINE_ITEM_qty + " INTEGER ," +
                COL_INVOICE_LINE_ITEM_freeQuantity + " INTEGER,"+
                COL_INVOICE_LINE_ITEM_price + " REAL ,"+
                COL_INVOICE_LINE_ITEM_lineTotal + " REAL ," +
                COL_INVOICE_LINE_ITEM_issync + " INTEGER ,"+
                COL_INVOICE_LINE_ITEM_syncDate + " TEXT ," +
                COL_INVOICE_LINE_ITEM_itemName + " TEXT " +
                " );";

        System.out.println("sql INVOICE line item - "+sql_invoice_line_item);
        db.execSQL(sql_invoice_line_item);


        String sql_sync_tables= "CREATE TABLE "+TABLE_SYNC+" ( "+
                COL_SYNC_ID + " INTEGER , "+
                COL_SYNC_TABLE_NAME + " TEXT , "+
                COL_SYNC_TABLE_TIME + " TEXT , "+
                COL_SYNC_ROWCOUNT + " INTEGER , "+
                COL_SYNC_ROWCOUNT_NEW + " INTEGER , "+
                COL_SYNC_SALESREPID + " INTEGER , "+
                COL_SYNC_LASTLOG_TIME + " TEXT , "+
                COL_SYNC_STATUS+ " INTEGER );";
        System.out.println("sql_sync_tables - "+sql_sync_tables);
        db.execSQL(sql_sync_tables);


        //Crete Invoice_Utilization
        String sql_invoice_utilization_tables= "CREATE TABLE "+TABLE_INVOICE_UTILIZATION+" ( "+
                COL_TABLE_INVOICE_UTILIZATION_InvoiceId + " INTEGER , "+
                COL_TABLE_INVOICE_UTILIZATION_CreditNoteNo + " INTEGER , "+
                COL_TABLE_INVOICE_UTILIZATION_UtilizedAmount + " REAL , "+
                COL_TABLE_INVOICE_UTILIZATION_EnteredUser + " TEXT , "+
                COL_TABLE_INVOICE_UTILIZATION_EnteredDate + " TEXT , "+
                COL_TABLE_INVOICE_UTILIZATION_IsSync+ " INTEGER );";
        System.out.println("sql_invoice_utilization_tables - "+sql_invoice_utilization_tables);
        db.execSQL(sql_invoice_utilization_tables);




        //Crete TABLE_PRIMARY_SALES_INVOICE
        String SQL_TABLE_PRIMARY_SALES_INVOICE= "CREATE TABLE "+TABLE_PRIMARY_SALES_INVOICE+" ( "+
                COL_PRIMARY_SALES_INVOICE_InvoiceNumber + " TEXT , "+
                COL_PRIMARY_SALES_INVOICE_OrderDate + " TEXT , "+
                COL_PRIMARY_SALES_INVOICE_StatusId + " INTEGER , "+
                COL_PRIMARY_SALES_INVOICE_EstimatedDeliveryDate + " TEXT , "+
                COL_PRIMARY_SALES_INVOICE_ExpectedDeliveryDate + " TEXT , "+
                COL_PRIMARY_SALES_INVOICE_DeliveredDate + " TEXT , "+
                COL_PRIMARY_SALES_INVOICE_SalesInvoiceNumber + " INTEGER , "+
                COL_PRIMARY_SALES_INVOICE_SalesRepID+ " INTEGER );";
        System.out.println("SQL_TABLE_PRIMARY_SALES_INVOICE - "+SQL_TABLE_PRIMARY_SALES_INVOICE);
        db.execSQL(SQL_TABLE_PRIMARY_SALES_INVOICE);


        //Crete TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM
        String SQL_TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM= "CREATE TABLE "+TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM+" ( "+
                COL_PRIMARY_SALES_INVOICE_LINE_ITEM_ProductId + " INTEGER , "+
                COL_PRIMARY_SALES_INVOICE_LINE_ITEM_InvoiceNumber + " TEXT , "+
                COL_PRIMARY_SALES_INVOICE_LINE_ITEM_OrderQty + " INTEGER , "+
                COL_PRIMARY_SALES_INVOICE_LINE_ITEM_SizeOfUnit+ " REAL );";
        System.out.println("SQL_TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM - "+SQL_TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);
        db.execSQL(SQL_TABLE_PRIMARY_SALES_INVOICE_LINE_ITEM);


        /*
        INSERT INTO TABLE_NAME [(column1, column2, column3,...columnN)]
VALUES (value1, value2, value3,...valueN);
         */
        for(int i = 0; i < 20 ; i++){

            String[] string_table = {All, District, Category, Product, Product_batch, Return_type, Path,
                    Distributor,Salesrep,Merchant, Sales_order, Vehicle_inventory,Salesrep_inventory,
                    Invoice,MerchantStock,MerchantReason,MerchantVisitCounts,PrimaryInvoice,Target,TargetCategory};

            String s1 = "INSERT INTO "+TABLE_SYNC+
                    "("+COL_SYNC_ID+
                    ","+COL_SYNC_TABLE_NAME+
                    ","+COL_SYNC_TABLE_TIME+
                    ","+COL_SYNC_ROWCOUNT+
                    ","+COL_SYNC_ROWCOUNT_NEW+
                    ","+COL_SYNC_SALESREPID+
                    ","+COL_SYNC_LASTLOG_TIME+
                    ","+COL_SYNC_STATUS+
                    ") VALUES ( "+i+",\""+string_table[i]+"\",\""+time+"\",0,0,0,\"\",0)";
            System.out.println(s1);
            db.execSQL(s1);
        }

        //Insert Data to TABLE_SYNC_ID
        for(int i = 0; i < 3 ; i++){

         int[] string_table_id = {TABLE_SALES_ORDER_ID, TABLE_RETURN_INVENTORY_ID , TABLE_SALES_INVOICE_ID};

            String s1 = "INSERT INTO "+TABLE_SYNC_ID+ "( "+COL_SYNC_ID_TABLE_ID +" ) VALUES ( "+string_table_id[i]+" )";
            System.out.println(s1);
            db.execSQL(s1);
        }


        String sql_service_count = "CREATE TABLE "+TABLE_SERVICES_COUNTS+" ( "+
                COL_SER_COUNT_SALESREP_ID+" INTEGER, "+
                COL_SER_COUNT_ACCORDINTO_DATE+" TEXT, "+
                COL_SER_COUNT_UPDATED_DATE_TIME+" TEXT, "+
                COL_SER_COUNT_TBL_DISTRICT+" INTEGER, "+
                COL_SER_COUNT_TBL_PRODUCT_CAT+" INTEGER, "+
                COL_SER_COUNT_TBL_PRODUCT+" INTEGER, "+
                COL_SER_COUNT_TBL_PRODUCT_BATCH+" INTEGER, "+
                COL_SER_COUNT_TBL_RETURN_TYPE+" INTEGER, "+
                COL_SER_COUNT_TBL_PATH+" INTEGER, "+
                COL_SER_COUNT_TBL_MERCHANT+" INTEGER, "+
                COL_SER_COUNT_TBL_SALES_ORDER+" INTEGER, "+
                COL_SER_COUNT_TBL_VEHICLE_INVEN+" INTEGER, "+
                COL_SER_COUNT_TBL_SALESREP_INVEN+" INTEGER, "+
                COL_SER_COUNT_TBL_INVOICE+" INTEGER ,"+
                COL_SER_COUNT_TBL_MERCHANT_STOCK+" INTEGER ,"+
                COL_SER_COUNT_TBL_MERCHANT_REASON+" INTEGER ,"+
                COL_SER_COUNT_TBL_MERCHANT_VISIT_COUNTS+" INTEGER ,"+
                COL_SER_COUNT_TBL_PRIMARY_INVOICE_COUNT+" INTEGER ,"+
                COL_SER_COUNT_TBL_TARGET_COUNT+" INTEGER ,"+
                COL_SER_COUNT_TBL_TARGET_CATEGORY_COUNT+" INTEGER "+
                ");";
        System.out.println("sql_service_count - "+sql_service_count);
        db.execSQL(sql_service_count);

        String sql_image = "CREATE TABLE "+TABLE_IMAGE+" ( "+
                COL_IMAGE_SEQUENCE_ID+" TEXT , "+
                COL_IMAGE_MERCHANT_ID+" INTEGER , "+
                COL_IMAGE1+" BLOB , "+
                COL_IMAGE2+" BLOB , "+
                COL_IMAGE_ISSYNC+" INTEGER );";
        System.out.println("sql_image - "+sql_image);
        db.execSQL(sql_image);

        String sql_merchant_stock = "CREATE TABLE " + TABLE_MERCHANT_STOCK + " (" +
                COL_MSTOCK_merchant_stock_id+ " INTEGER ," +
                COL_MSTOCK_merchant_id + " INTEGER ," +
                COL_MSTOCK_distributor_id + " INTEGER ," +
                COL_MSTOCK_salesrep_id + " INTEGER ,"+
                COL_MSTOCK_entered_date + " TEXT ," +
                COL_MSTOCK_entered_user + " TEXT ," +
                COL_MSTOCK_sales_status + " INTEGER ," +
                COL_MSTOCK_is_sync + " INTEGER ," +
                COL_MSTOCK_sync_date + " TEXT ," +
                COL_MSTOCK_updated_user + " TEXT ," +
                COL_MSTOCK_updated_date + " TEXT );";

        System.out.println("sql_merchant_stock - "+sql_merchant_stock);
        db.execSQL(sql_merchant_stock);

        String sql_merchant_stock_lineitem = "CREATE TABLE " + TABLE_MERCHANT_STOCK_LINEITEM + " (" +
                COL_MS_LINEITEM_merchant_stock_id+ " INTEGER ," +
                COL_MS_LINEITEM_product_id + " INTEGER ," +
                COL_MS_LINEITEM_product_name + " TEXT ," +
                COL_MS_LINEITEM_quantity + " INTEGER ," +
                COL_MS_LINEITEM_is_sync + " INTEGER ,"+
                COL_MS_LINEITEM_sync_date + " TEXT );";

        System.out.println("sql_merchant_stock_lineitem - "+sql_merchant_stock_lineitem);
        db.execSQL(sql_merchant_stock_lineitem);



        String sql_sync_attendace = "CREATE TABLE "+TABLE_SYNC_ATTENDANCE+"(" +
                COL_SYNC_ATTENDANCE_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_SYNC_ATTENDANCE_DATE+" TEXT," +
                COL_SYNC_ATTENDANCE_DISTRIBUTR_ID+" TEXT," +
                COL_SYNC_ATTENDANCE_SALESREP_ID+" TEXT," +
                COL_SYNC_ATTENDANCE_TIME_IN+" TEXT," +
                COL_SYNC_ATTENDANCE_TIME_OUT+" TEXT," +
                COL_SYNC_ATTENDANCE_BATTERY_IN+" TEXT," +
                COL_SYNC_ATTENDANCE_BATTERY_OUT+" TEXT," +
                COL_SYNC_ATTENDANCE_LONGITUDE_IN+" REAL," +
                COL_SYNC_ATTENDANCE_LATITUDE_IN+" REAL," +
                COL_SYNC_ATTENDANCE_LONGITUDE_OUT+" REAL," +
                COL_SYNC_ATTENDANCE_LATITUDE_OUT+" REAL," +
                COL_SYNC_ATTENDANCE_IS_SYNC_IN+" INTEGER," +
                COL_SYNC_ATTENDANCE_IS_SYNC_OUT+" INTEGER)";
        System.out.println("sql_sync_attendace - "+sql_sync_attendace);
        db.execSQL(sql_sync_attendace);

        String sql_merchant_visit_reason = "CREATE TABLE "+TABLE_MVISIT_REASON+" ("+
                COL_MVISIT_REASON_REASON_ID+" INTEGER, "+
                COL_MVISIT_REASON_DESCRIPTION+" TEXT, "+
                COL_MVISIT_REASON_ALLOWSTOCK+" INTEGER )";
        System.out.println("sql_merchant_visit_reason - "+sql_merchant_visit_reason);
        db.execSQL(sql_merchant_visit_reason);

        String sql_merchant_visit = "CREATE TABLE "+TABLE_MVISIT+" ("+
                COL_MVISIT_MERCHANT_ID+" INTEGER, "+
                COL_MVISIT_REASON_ID+" INTEGER,"+
                COL_MVISIT_SALESREP_ID+" INTEGER,"+
                COL_MVISIT_DISTRIBUTOR_ID+" INTEGER,"+
                COL_MVISIT_DELIVERYPATH_ID+" INTEGER,"+
                COL_MVISIT_ENTERED_DATE+" TEXT,"+
                COL_MVISIT_ISSYNC+" INTEGER,"+
                COL_MVISIT_ENTERED_USER+" TEXT )";
        System.out.println("sql_merchant_visit - "+sql_merchant_visit);
        db.execSQL(sql_merchant_visit);


        //Create TARGET_CATEGORY Table
        String sql_target_category = "CREATE TABLE "+TABLE_TARGET_CATEGORY+" ("+
                COL_TARGET_CATEGORY_targetCategoryId+" TEXT, "+
                COL_TARGET_CATEGORY_targetCategoryName+" TEXT,"+
                COL_TARGET_CATEGORY_unitOfMeasurement+" TEXT,"+
                COL_TARGET_CATEGORY_enteredDate+" TEXT,"+
                COL_TARGET_CATEGORY_enterdUser+" TEXT )";
        System.out.println("sql_target_category - "+sql_target_category);
        db.execSQL(sql_target_category);


        //Create TARGET_CATEGORY_LINE_ITEM Table
        String sql_target_category_line_item = "CREATE TABLE "+TABLE_TARGET_CATEGORY_LINE_ITEM+" ("+
                COL_TARGET_CATEGORY_LINE_ITEM_targetCategoryId+" TEXT, "+
                COL_TARGET_CATEGORY_LINE_ITEM_productId+" INTEGER,"+
                COL_TARGET_CATEGORY_LINE_ITEM_unitPerQuantity+" INTEGER,"+
                COL_TARGET_CATEGORY_LINE_ITEM_enteredDate+" TEXT,"+
                COL_TARGET_CATEGORY_LINE_ITEM_enterdUser+" TEXT )";
        System.out.println("sql_target_category_line_item - "+sql_target_category_line_item);
        db.execSQL(sql_target_category_line_item);

        //Create TABLE_SALESREP_TARGET
        String sql_salesrep_target = "CREATE TABLE "+TABLE_SALESREP_TARGET+" ("+
                COL_SALESREP_TARGET_salesRepId+" INTEGER, "+
                COL_SALESREP_TARGET_targetCategoryId+" INTEGER,"+
                COL_SALESREP_TARGET_targetQty +" INTEGER,"+
                COL_SALESREP_TARGET_year+" INTEGER,"+
                COL_SALESREP_TARGET_month+" INTEGER )";
        System.out.println("sql_salesrep_target - "+sql_salesrep_target);
        db.execSQL(sql_salesrep_target);


        //Create Mileage Table
        String sql_mileage = "CREATE TABLE "+TABLE_MILEAGE+" ("+
                COL_TABLE_MILEAGE_id+" INTEGER, "+
                COL_TABLE_MILEAGE_markedTime+" INTEGER,"+
                COL_TABLE_MILEAGE_type+" INTEGER,"+
                COL_TABLE_MILEAGE_salesRepId+" INTEGER )";
        System.out.println("sql_mileage - "+sql_mileage);
        db.execSQL(sql_mileage);

        //Create Sync Mileage Table
        String sql_sync_mileage = "CREATE TABLE "+TABLE_SYNC_MILEAGE+" ("+
                COL_TABLE_SYNC_MILEAGE_mileageDate+" TEXT, "+
                COL_TABLE_SYNC_MILEAGE_salesRepId+" INTEGER,"+
                COL_TABLE_SYNC_MILEAGE_mileageInTime+" TEXT,"+
                COL_TABLE_SYNC_MILEAGE_mileageIn+" TEXT,"+
                COL_TABLE_SYNC_MILEAGE_mileageOutTime+" TEXT,"+
                COL_TABLE_SYNC_MILEAGE_mileageOut+" TEXT,"+
                COL_TABLE_SYNC_MILEAGE_isSyncIn+" INTEGER,"+
                COL_TABLE_SYNC_MILEAGE_isSyncOut+" INTEGER,"+
                COL_TABLE_SYNC_MILEAGE_syncDate+" TEXT,"+
                COL_TABLE_SYNC_MILEAGE_enteredUser+" TEXT )";
        System.out.println("sql_sync_mileage - "+sql_sync_mileage);
        db.execSQL(sql_sync_mileage);

        // Salesorder promotion

        String sql_salesorder_promption = "CREATE TABLE "+TABLE_PROMOTION+" ("+
                COL_PROMOTION_ProductId+" INTEGER, "+
                COL_PROMOTION_BatchId+" INTEGER, "+
                COL_PROMOTION_CatalogueType+" INTEGER, "+
                COL_PROMOTION_StartDate+" TEXT, "+
                COL_PROMOTION_EndDate+" TEXT, "+
                COL_PROMOTION_EnteredUser+" INTEGER, "+
                COL_PROMOTION_IMAGE_INDEX+" INTEGER, "+
                COL_PROMOTION_EnteredDate+" TEXT) ";

        System.out.println("sql_PROMOTION - "+sql_salesorder_promption);
        db.execSQL(sql_salesorder_promption);


        //credittype
        String sql_credittype = "CREATE TABLE "+TABLE_CREDIT_TYPE+" ("+
                COL_CT_ID+" INTEGER, "+
                COL_CT_NAME+" TEXT )";
        System.out.println("sql_credittype - "+sql_credittype);
        db.execSQL(sql_credittype);



        String s0 = "INSERT INTO "+TABLE_CREDIT_TYPE+ " ( "+COL_CT_ID +","+COL_CT_NAME+" ) VALUES ( 0, \"Cash\" )";
        String s1 = "INSERT INTO "+TABLE_CREDIT_TYPE+ " ( "+COL_CT_ID +","+COL_CT_NAME+" ) VALUES ( 1, \"Credit\" )";
        System.out.println(s0+"\n"+s1);
        db.execSQL(s0);
        db.execSQL(s1);


        String sql_promotion_image = "CREATE TABLE "+TABLE_PROMOTION_IMAGE+" ("+
                COL_PROMOTION_IMAGE_productid+" INTEGER, "+
                COL_PROMOTION_IMAGE_batchid+" INTEGER, "+
                COL_PROMOTION_IMAGE_catalogue_type+" INTEGER, "+
                COL_PROMOTION_IMAGE_img+" BLOB )";
        db.execSQL(sql_promotion_image);
        System.out.println("sql_promotion_image - "+sql_promotion_image);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql_upgrade = "drop table "+TABLE_NAME+ " if exists;" +
                "drop table "+TBL25+" if exists"+
        "drop table "+TABLE_CATEGORY+ " if exists;" +
                "drop table "+TBL25+" if exists;" ;
        db.execSQL(sql_upgrade);
    }

    public boolean insertData(ContentValues cv){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(TABLE_NAME,null,cv);

        if (result>0){
            return true;
        }else{
            return false;
        }
    }

    public void updateData(String sql){

        Cursor cursor = null;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery(sql,null);
//
//        if (result.getCount() > 0){
//            return cursor;
//        }else{
//            return null;
//        }
    }

    public boolean hasData(String sql){

        boolean hasdata = false;

        SQLiteDatabase database = this.getReadableDatabase();
        try{

            Cursor cursor = database.rawQuery(sql,null);
            if(cursor.getCount() > 0 & cursor != null){
                hasdata = true;
            }
        }catch(Exception e){
            System.out.println("e - "+e.toString());
            hasdata = false;
        }

        return hasdata;

    }


    public boolean insertDataAll(ContentValues cv,String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.insert(tableName,null,cv);

        if (result>0){
            return true;
        }else{
            return false;
        }
    }

    public int insertDataWithLastID(ContentValues cv,String tableName){
        Cursor cursor;
        int SequenceId = 0;
        SQLiteDatabase db = this.getWritableDatabase();

        long result = db.insert(tableName,null,cv);

        if (result>0) {
            cursor = getAllData(tableName);
            if (cursor.moveToLast()) {
                SequenceId = cursor.getInt(cursor.getColumnIndex(DbHelper.TBL_MERCHANT_SEQUENCE_ID));
            }
        }
        return SequenceId;
    }
    
    public Cursor getAllData(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+tableName,null);
        return  cursor;
    }

    public Cursor getSingleData(String url){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(url,null);
        return  cursor;
    }

    public int getDistributorId(String url){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(url,null);

        int dis = 0;
        while (cursor.moveToNext()) {
            dis = cursor.getInt(cursor.getColumnIndex(DbHelper.COL_SALES_REP_DistributorId));
        }
        return dis;
    }

    public boolean updateActualPath(String id, int sync){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv= new ContentValues();
        cv.put(COL25_IS_SYNC,sync);
        db.update(TBL25,cv,COL25_SEQUENCE_ID+"= ?",new String[] {id});
        return  true;
    }

    /*db.update(TABLE_NAME, contentValues, NAME + " = ? AND " + LASTNAME + " = ?", new String[]{"Manas", "Bajaj"});*/

    public boolean updateTable(String id, ContentValues cv, String whereClause, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isUpdated = false;
        int afectedRows = db.update(tableName,cv,whereClause,new String[] {id});
        if (afectedRows>0){
            isUpdated = true;
            System.out.println("======== Updated Row Count: " +afectedRows + " ================= "+tableName);
        }
        return isUpdated;
    }

    public boolean deleteAllData(String tableName) {
        boolean success = false;
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            db.execSQL("DELETE FROM " + tableName);
            success = true;

        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        return success;
    }


    public int totalROWS(String tablename){
        int value = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
//        if(tablename.equalsIgnoreCase(All)){
//            cursor.getCount() = 0;
//        }else{
//
//        }
        cursor = db.rawQuery("SELECT COUNT (*) FROM "+tablename,null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                value = cursor.getInt(0);
            }
        }else{
            value = 0;
        }

        return value;
    }

    public int totalROWS_where_Id(String tablename,String distriid_column_name,int disId){
        int value = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM "+tablename+" WHERE \""+distriid_column_name+"\" = "+disId,null);
        if(cursor.getCount() != 0){
            while (cursor.moveToNext()) {
                value = cursor.getInt(0);
            }
        }else{
            value = 0;
        }
        return value;
    }

    public void deleteFromAnyTable(String tablename,String wherePart){

        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor cursor = db.rawQuery("delete from "+tablename+" where "+wherePart,null);

        String sql = "delete from "+tablename+" where "+wherePart ;
        db.execSQL(sql);
    }

}
