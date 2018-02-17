package com.example.bellvantage.scadd.Utils;

/**
 * Created by Sachika on 19/09/2017.
 */

public class UtilityManager  {

    public  static int genarateInvoiceId(int id,int salesrepId){
        int invoiceId = 0;
        try{
            invoiceId =  Integer.parseInt((1000+salesrepId)+""+id);
        }catch (Exception e){
            System.out.println("Error at genarateInvoiceId of UtilityManager: "+ e.getMessage());
        }
        return invoiceId;
    }

    public  static String truncateInvoiceId(int id){
        String truncatedID = null;
        try{
            truncatedID = Integer.toString(id);
            truncatedID = truncatedID.substring(4,truncatedID.length());
        }catch (Exception e){
            System.out.println("Error at genarateInvoiceId of UtilityManager: "+ e.getMessage());
        }
        return truncatedID;
    }

}
