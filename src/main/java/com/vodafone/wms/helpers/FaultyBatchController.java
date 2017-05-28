/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.helpers;

import com.vodafone.wms.controllers.UsersController;
import com.vodafone.wms.controllers.WarehouseStockController;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.entities.RequestLineItem;
import com.vodafone.wms.entities.WarehouseStock;
import com.vodafone.wms.entities.WarehouseStockPK;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author eamrela
 */
@Named("faultyBatchController")
@SessionScoped
public class FaultyBatchController implements Serializable{
    
   
    @EJB
    private FaultyBatchFacade ejbFacade;
    private List<RequestLineItem> items = null;
    private List<RequestLineItem> selectedItems = null;
    private RequestLineItem selected;
    
    @Inject
    private UsersController usersController;
    @Inject
    private WarehouseStockController warehouseStockController;
     
     private String filterRegion;
     
     public RequestLineItem getSelected() {
        return selected;
    }

    public void setSelected(RequestLineItem selected) {
        this.selected = selected;
    }
    
    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private FaultyBatchFacade getFacade() {
        return ejbFacade;
    }

    public String getFilterRegion() {
        return filterRegion;
    }

    public void setFilterRegion(String filterRegion) {
        this.filterRegion = filterRegion;
    }
    
    public List<RequestLineItem> getItems() {
       if(items==null){
            if(filterRegion==null){
            items = getFacade().findFaultyBatch(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            items = getFacade().findFaultyBatch(filterRegion);
            }
        }
        return items;
    }
    
    public String getFBDirectory(){
         String FBDir = null;
        if(filterRegion==null){
            FBDir = getFacade().findFaultyBatchDirectory(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            FBDir = getFacade().findFaultyBatchDirectory(filterRegion);
            }
        return FBDir;
    }
    public void onFilterAreaSelect(){
        items = null;
        selectedItems = null;
    }

    public void update() {
        persist(JsfUtil.PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("RequestLineItemUpdated"));
    }
     
    private void persist(JsfUtil.PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != JsfUtil.PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public List<RequestLineItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems(List<RequestLineItem> selectedItems) {
        this.selectedItems = selectedItems;
    }
    
    public void deductStock(RequestLineItem item){
        if(item!=null){
        if(!item.getStolen()){
            WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                            new WarehouseStockPK(item.getReqId().getFromWarehouse().getWarehouseName(), 
                                    item.getReturnabeItemId().getPartNumber()));
            if(stockItem!=null){
                warehouseStockController.decrementFaultyQty(stockItem, item.getReturnableQty());
            }
        }
        
        }
    }
    
    public void exportFaultyBatch(){
        PrintWriter pw = null;
        String faulty_batch_dir = getFBDirectory();
        if(selectedItems!=null && faulty_batch_dir!=null){
            String fileName = (filterRegion!=null?filterRegion:usersController.getLoggedInUser().getRegion().getRegionName());
            fileName = "Faulty_Batch_"+fileName+"_"+
                    (java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime())).replaceAll(":", "")+".csv";
            File FB = new File(faulty_batch_dir+"\\"+fileName);
        try {
            pw = new PrintWriter(FB);
            pw.println("Material Number,Part Number,Description,Serial Number,Site ID,Engineer Name,New Installed Serial,"
                    + "Fault Description,Fault Date,TT#,Office,Vendor,Return Status");
            for (RequestLineItem selectedItem : selectedItems) {
               if(selectedItem.getReturnabeItemId()!=null){
               pw.print(selectedItem.getReturnabeItemId().getMaterialNumber()+",");
               pw.print(selectedItem.getReturnabeItemId().getPartNumber()+",");
               pw.print(selectedItem.getReturnabeItemId().getDescription()+",");
               }else{
               pw.print("Stolen,Stolen,Stolen,");
               }
               pw.print(selectedItem.getReturnableSerial()!=null?selectedItem.getReturnableSerial():"Stolen"+",");
               pw.print(selectedItem.getReqId().getSiteId()+",");
               pw.print(selectedItem.getReqId().getEngineer().getEngineerName()+",");
               pw.print(selectedItem.getSerialNumber()+",");
               pw.print(selectedItem.getReqId().getFaultDescription()+",");
               pw.print(selectedItem.getReqId().getReqTime()+",");
               pw.print(selectedItem.getReqId().getTtId()+",");
               pw.print(selectedItem.getReqId().getFromWarehouse().getWarehouseName()+",");
               pw.print(selectedItem.getVendor()+",");
               pw.print(selectedItem.getReturnableStatus()!=null?selectedItem.getReturnableStatus().getStatusName():"Stolen"+",");
               pw.print("\n");
               selectedItem.setFaultyBatchDate(new Date());
               setSelected(selectedItem);
               update();
               deductStock(selectedItem);
                
            }
        
        
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FaultyBatchController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            pw.close();
        }
            
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/csv");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\""+FB.getName()+""+"\"");
            FileInputStream fin = new FileInputStream(FB);
            byte[] data;
                data = new byte[fin.available()];
            fin.read(data);
            externalContext.getResponseOutputStream().write(data);
            externalContext.getResponseOutputStream().flush();
            externalContext.getResponseOutputStream().close();
            } catch (IOException ex) {
                Logger.getLogger(FaultyBatchController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            items=null;
            selectedItems=null;
            selected = null;
        
        }
    }
    
    
    
   
    
}
