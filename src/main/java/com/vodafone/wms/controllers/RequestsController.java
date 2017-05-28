package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.Requests;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.RequestsFacade;
import com.vodafone.wms.entities.Items;
import com.vodafone.wms.entities.RequestLineItem;
import com.vodafone.wms.entities.WarehouseStock;
import com.vodafone.wms.entities.WarehouseStockPK;
import com.vodafone.wms.entities.Warehouses;
import java.io.IOException;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import org.primefaces.event.SelectEvent;

@Named("requestsController")
@SessionScoped
public class RequestsController implements Serializable {

    @EJB
    private com.vodafone.wms.beans.RequestsFacade ejbFacade;
    private List<Requests> items = null;
    private List<Requests> inBoundItems = null;
    private List<Requests> transferItems = null;
    private List<Requests> issueItems = null;
    private Requests selected;
    
    @Inject
    private UsersController usersController;
    @Inject
    private RequestTypeController requestTypeController;
    @Inject
    private WarehouseStockController warehouseStockController;
    @Inject 
    private WarehousesController warehousesController;
    @Inject
    private ItemsController itemsController;
    @Inject
    private RequestLineItemStatusController lineItemStatusController;
    @Inject
    private RequestLineItemController requestLineItemController;
    @Inject 
    private NotificationsController notificationController;
    @Inject
    private NotificationTypeController notificationTypeController;
    

    public RequestsController() {
    }

    public Requests getSelected() {
        return selected;
    }

    public void setSelected(Requests selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private RequestsFacade getFacade() {
        return ejbFacade;
    }

    public Requests prepareCreate() {
        selected = new Requests();
        initializeEmbeddableKey();
        return selected;
    }

    public Requests prepareCreateInBound(){
        selected = new Requests();
        initializeEmbeddableKey();
        selected.setRequester(usersController.getLoggedInUser());
        selected.setReqTime(new Date());
        selected.setReqType(requestTypeController.getRequestType("In-Bound"));
        return selected;
    }
   
    public Requests prepareCreateTransfer(){
        selected = new Requests();
        initializeEmbeddableKey();
        selected.setRequester(usersController.getLoggedInUser());
        selected.setReqTime(new Date());
        selected.setReqType(requestTypeController.getRequestType("Transfer"));
        return selected;
    }
    
     public Requests prepareCreateIssue(){
        selected = new Requests();
        initializeEmbeddableKey();
        selected.setRequester(usersController.getLoggedInUser());
        selected.setReqTime(new Date());
        selected.setReqType(requestTypeController.getRequestType("Issued-Material"));
        
        return selected;
    }
    
    
            
            
    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("RequestsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            inBoundItems = null;
            transferItems = null;
            issueItems = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("RequestsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("RequestsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            inBoundItems = null;
            transferItems = null;
            issueItems = null;
        }
    }

    public List<Requests> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<Requests> getInBoundItems() {
        if(inBoundItems==null){
            if(usersController.getRole().contains("SPC")){
            inBoundItems = getFacade().findInBoundItemsByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            inBoundItems = getFacade().findInBoundItemsByWarehouse(usersController.getLoggedInUserWarehousesAsString());
            }
        }
        return inBoundItems;
    }

    
    public List<Requests> getTransferItems() {
         if(transferItems==null){
            if(usersController.getRole().contains("SPC")){
            transferItems = getFacade().findTransferItemsByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            transferItems = getFacade().findTransferItemsByWarehouse(usersController.getLoggedInUserWarehousesAsString());
            }
        }
        return transferItems;
    }

    public List<Requests> getIssueItems() {
         if(issueItems==null){
            if(usersController.getRole().contains("SPC")){
            issueItems = getFacade().findIssueItemsByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            issueItems = getFacade().findIssueItemsByWarehouse(usersController.getLoggedInUserWarehousesAsString());
            }
        }
        return issueItems;
    }
    
    private Requests persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    selected = getFacade().merge(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
                return selected;
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
        return null;
    }

    public Requests getRequests(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Requests> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Requests> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Requests.class)
    public static class RequestsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RequestsController controller = (RequestsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "requestsController");
            return controller.getRequests(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Requests) {
                Requests o = (Requests) object;
                return getStringKey(o.getReqId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Requests.class.getName()});
                return null;
            }
        }

    }

    public void addSelectedLineItem(SelectEvent event){
        if(selected!=null){
        Items item = (Items) event.getObject();
        if(selected.getRequestLineItemCollection()==null){
            selected.setRequestLineItemCollection(new ArrayList());
        }
        RequestLineItem lineItem = new RequestLineItem();
        lineItem.setItemId(item);
        lineItem.setReqId(selected);
        lineItem.setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Pending Approval"));
        
        selected.getRequestLineItemCollection().add(lineItem);
        itemsController.setSelected(null);
        JsfUtil.addSuccessMessage("Item added, to remove it just double click on it");
        } 
    }
    
    public void addSelectedTransferLineItem(SelectEvent event){
        if(selected!=null){
            WarehouseStock item = (WarehouseStock) event.getObject();
        if(selected.getRequestLineItemCollection()==null){
            selected.setRequestLineItemCollection(new ArrayList());
        }
        RequestLineItem lineItem = new RequestLineItem();
        lineItem.setItemId(item.getItems());
        lineItem.setReqId(selected);
        lineItem.setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Pending Approval"));
        
        selected.getRequestLineItemCollection().add(lineItem);
        warehouseStockController.setSelected(null);
        JsfUtil.addSuccessMessage("Item added, to remove it just double click on it");
        } 
    }
    
    public void addSelectedIssueLineItem(SelectEvent event){
        if(selected!=null){
            WarehouseStock item = (WarehouseStock) event.getObject();
        if(selected.getRequestLineItemCollection()==null){
            selected.setRequestLineItemCollection(new ArrayList());
        }
        RequestLineItem lineItem = new RequestLineItem();
        lineItem.setItemId(item.getItems());
        lineItem.setReqId(selected);
        lineItem.setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Pending Approval"));
        
        selected.getRequestLineItemCollection().add(lineItem);
        warehouseStockController.setSelected(null);
        JsfUtil.addSuccessMessage("Item added, to remove it just double click on it");
        } 
    }
    
    public void removeFromLineItems(final SelectEvent event){
        RequestLineItem obj = (RequestLineItem) event.getObject();
        if(selected!=null){
            if(!obj.getLineItemStatus().getStatusName().equals("Accepted")){
          if(selected.getRequestLineItemCollection()!=null){
            selected.getRequestLineItemCollection().remove(obj);
            JsfUtil.addSuccessMessage("Item removed!");
            }  
            }
        }
    }
    
    public void removeTransferFromLineItems(final SelectEvent event){
        RequestLineItem obj = (RequestLineItem) event.getObject();
        if(selected!=null){
            if(!obj.getLineItemStatus().getStatusName().equals("Accepted")){
          if(selected.getRequestLineItemCollection()!=null){
            selected.getRequestLineItemCollection().remove(obj);
            JsfUtil.addSuccessMessage("Item removed!");
            }  
            }
        }
    }
     
    public void removeIssueFromLineItems(final SelectEvent event){
        RequestLineItem obj = (RequestLineItem) event.getObject();
        if(selected!=null){
            if(!obj.getLineItemStatus().getStatusName().equals("Accepted")){
          if(selected.getRequestLineItemCollection()!=null){
            selected.getRequestLineItemCollection().remove(obj);
            JsfUtil.addSuccessMessage("Item removed!");
            }  
            }
        }
    }

    public void createInBoundRequest(){
        if(selected!=null){
            if(selected.getRequestLineItemCollection()!=null){
                Object[] lineItems = selected.getRequestLineItemCollection().toArray();
                boolean allGood = true;
                for (Object lineItem : lineItems) {
                    if(((RequestLineItem)lineItem).getQty()!=null){
                        if(((RequestLineItem)lineItem).getQty().compareTo(BigInteger.ZERO)==0){
                            JsfUtil.addErrorMessage("You need to add QTY to the Order Line Item");
                            allGood=false;
                        }
                    }else{
                        allGood = false;
                    }
                }
                if(allGood){
                    selected.setAssignmentGroup(assignmentGroupBrain(selected));
                    selected = persist(PersistAction.CREATE, "Request Created");
                    notificationController.createNotification(selected.getToWarehouse().getRegion(), 
                                                              selected.getToWarehouse(),
                                                              notificationTypeController.getNotificationType("NEW_REQUEST"), 
                                                              "A new IN-BOUND request is created");
                    clearValues();
                    redirect();
                }
            }else{
                JsfUtil.addErrorMessage("You need to add In-Bound Items");
            }
        }
    }
      
    public void createTransferRequest(){
        if(selected!=null){
            if(selected.getRequestLineItemCollection()!=null){
                Object[] lineItems = selected.getRequestLineItemCollection().toArray();
                boolean allGood = true;
                for (Object lineItem : lineItems) {
                    if(((RequestLineItem)lineItem).getQty()!=null){
                        if(((RequestLineItem)lineItem).getQty().compareTo(BigInteger.ZERO)==0){
                            JsfUtil.addErrorMessage("You need to add QTY to the Order Line Item");
                            allGood=false;
                        }
                        if(((RequestLineItem)lineItem).getQty()
                                    .compareTo(warehouseStockController.getGoodStockQty((RequestLineItem) lineItem))>0){
                                
                            JsfUtil.addErrorMessage("You can't transfer more than the current stock qty!");
                            allGood=false;
                        }
                    }else{
                        allGood = false;
                    }
                }
                if(allGood){
                    selected.setAssignmentGroup(assignmentGroupBrain(selected));
                    selected = persist(PersistAction.CREATE, "Request Created");
                     for (Object lineItem : lineItems) {
                         WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                                 new WarehouseStockPK(selected.getFromWarehouse().getWarehouseName(), 
                                         ((RequestLineItem)lineItem).getItemId().getPartNumber()));
                         warehouseStockController.decrementGoodQty(stockItem,((RequestLineItem)lineItem).getQty());
                     }
                    notificationController.createNotification(selected.getFromWarehouse().getRegion(), 
                                                              selected.getFromWarehouse(),
                                                              notificationTypeController.getNotificationType("NEW_REQUEST"), 
                                                              "A new TRANSFER request is created");
                    clearValues();
                    redirect();
                }
            }else{
                JsfUtil.addErrorMessage("You need to add In-Bound Items");
            }
        }
    }
    
    public void createIssueRequest(){
        if(selected!=null){
            if(selected.getRequestLineItemCollection()!=null){
                Object[] lineItems = selected.getRequestLineItemCollection().toArray();
                boolean allGood = true;
                for (Object lineItem : lineItems) {
                    if(((RequestLineItem)lineItem).getQty()!=null){
                        if(((RequestLineItem)lineItem).getQty().compareTo(BigInteger.ZERO)==0){
                            JsfUtil.addErrorMessage("You need to add QTY to the Order Line Item");
                            allGood=false;
                        }
                        if(((RequestLineItem)lineItem).getQty()
                                    .compareTo(warehouseStockController.getGoodStockQty((RequestLineItem) lineItem))>0){
                                
                            JsfUtil.addErrorMessage("You can't set Qty to more than the current stock qty!");
                            allGood=false;
                        }
                    }else{
                        allGood = false;
                    }
                }
                if(allGood){
                    selected.setAssignmentGroup(assignmentGroupBrain(selected));
                    selected = persist(PersistAction.CREATE, "Request Created");
                     for (Object lineItem : lineItems) {
                         WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                                 new WarehouseStockPK(selected.getFromWarehouse().getWarehouseName(), 
                                         ((RequestLineItem)lineItem).getItemId().getPartNumber()));
                         warehouseStockController.decrementGoodQty(stockItem,((RequestLineItem)lineItem).getQty());
                            warehouseStockController.incrementFMQty(stockItem,((RequestLineItem)lineItem).getQty());
                     }
                     notificationController.createNotification(selected.getFromWarehouse().getRegion(), 
                                                              selected.getFromWarehouse(),
                                                              notificationTypeController.getNotificationType("NEW_REQUEST"), 
                                                              "A new ISSUED-MATERIAL request is created");
                    clearValues();
                    redirect();
                }
            }else{
                JsfUtil.addErrorMessage("You need to add In-Bound Items");
            }
        }
    }
      
    
    public Warehouses assignmentGroupBrain(Requests request){
        if(request!=null){
            if(request.getReqType().getReqTypeName().equals("In-Bound")){
                return request.getToWarehouse();
            }
            else if(request.getReqType().getReqTypeName().equals("Transfer")){
                if(usersController.getRole().contains("SPC")){
                    return request.getFromWarehouse();
                }else if(allApproved(request.getRequestLineItemCollection())){
                    return request.getToWarehouse();
                }else{
                    return request.getFromWarehouse();
                }
            }
            else if(request.getReqType().getReqTypeName().equals("Issued-Material")){
                return request.getFromWarehouse();
            }
        }
        return null;
    }
    
    public boolean allApproved(Collection<RequestLineItem> lineItems){
        boolean accepted = true;
        if(lineItems!=null){
            if(lineItems.size()>0){
                Object[] lineItemsArr = lineItems.toArray();
                for (int i = 0; i < lineItemsArr.length; i++) {
                    if(((RequestLineItem) lineItemsArr[i]).getLineItemStatus().getStatusName().contains("Pending")){
                        accepted = false;
                        return false;
                    }
                }
            }
        }
        return accepted;
    }
    
    public void acceptInBoundItem(){
        if(requestLineItemController.getSelected()!=null){
            requestLineItemController.getSelected().setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Approved"));
             WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                                 new WarehouseStockPK(requestLineItemController.getSelected().getReqId().getToWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
            if(stockItem==null){
             stockItem = new WarehouseStock(new WarehouseStockPK(requestLineItemController.getSelected()
                                .getReqId().getToWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
             stockItem.setFaultyStockQty(BigInteger.ZERO);
             stockItem.setFmStockQty(BigInteger.ZERO);
             stockItem.setGoodStockQty(requestLineItemController.getSelected().getQty());
             stockItem.setLastTransactionTime(new Date());
             stockItem.setTotalStockQty(requestLineItemController.getSelected().getQty());
             stockItem.setItems(requestLineItemController.getSelected().getItemId());
             stockItem.setWarehouses(requestLineItemController.getSelected().getReqId().getToWarehouse());
             warehouseStockController.setSelected(stockItem);
             warehouseStockController.create();
            }else{
            warehouseStockController.incrementGoodQty(stockItem, requestLineItemController.getSelected().getQty());
            }
            notificationController.createNotification(selected.getToWarehouse().getRegion(), 
                                                              selected.getToWarehouse(),
                                                              notificationTypeController.getNotificationType("APPROVED_REQUEST"), 
                                                              "IN-BOUND item accepted "+requestLineItemController.getSelected().getQty()
                                                                      +"["+stockItem.getItems().getPartNumber()+"]");
            update();
            clearValues();
        }
    }
    
    public void rejectInBoundItem(){
        if(requestLineItemController.getSelected()!=null){
            requestLineItemController.getSelected().setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Rejected"));
            update();
             notificationController.createNotification(selected.getToWarehouse().getRegion(), 
                                                              selected.getToWarehouse(),
                                                              notificationTypeController.getNotificationType("REJECTED_REQUEST"), 
                                                              "IN-BOUND item accepted "+requestLineItemController.getSelected().getQty()
                                                                      +"["+requestLineItemController.getSelected().getItemId()
                                                                              .getPartNumber()+"]");
            clearValues();
        }
    }
     
    public void acceptTransferItem(){
         if(requestLineItemController.getSelected()!=null){
             if(usersController.getLoggedInUserWarehousesAsString().contains(requestLineItemController.getSelected().getReqId().getFromWarehouse().getWarehouseName())){
            requestLineItemController.getSelected().setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Shipped"));
             }else if(usersController.getLoggedInUserWarehousesAsString().contains(requestLineItemController.getSelected().getReqId().getToWarehouse().getWarehouseName())){
            requestLineItemController.getSelected().setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Approved"));
             
             WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                                 new WarehouseStockPK(requestLineItemController.getSelected().getReqId().getToWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
            if(stockItem==null){
             stockItem = new WarehouseStock(new WarehouseStockPK(requestLineItemController.getSelected()
                                .getReqId().getToWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
             stockItem.setFaultyStockQty(BigInteger.ZERO);
             stockItem.setFmStockQty(BigInteger.ZERO);
             stockItem.setGoodStockQty(requestLineItemController.getSelected().getQty());
             stockItem.setLastTransactionTime(new Date());
             stockItem.setTotalStockQty(requestLineItemController.getSelected().getQty());
             stockItem.setItems(requestLineItemController.getSelected().getItemId());
             stockItem.setWarehouses(requestLineItemController.getSelected().getReqId().getToWarehouse());
             warehouseStockController.setSelected(stockItem);
             warehouseStockController.create();
            }else{
            warehouseStockController.incrementGoodQty(stockItem, requestLineItemController.getSelected().getQty());
            }
            notificationController.createNotification(requestLineItemController.getSelected().getReqId().getFromWarehouse().getRegion(), 
                                                     requestLineItemController.getSelected().getReqId().getFromWarehouse(),
                                                     notificationTypeController.getNotificationType("APPROVED_REQUEST"), 
                                                    "TRANSFER item accepted "+requestLineItemController.getSelected().getQty()
                                                            +"["+requestLineItemController.getSelected().getItemId()
                                                                    .getPartNumber()+"]");
             }
            selected.setAssignmentGroup(assignmentGroupBrain(requestLineItemController.getSelected().getReqId()));
            update();
            clearValues();
        }
    }
    
    public void rejectTransferItem(){
        if(requestLineItemController.getSelected()!=null){
            requestLineItemController.getSelected().setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Rejected"));
             WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                                 new WarehouseStockPK(requestLineItemController.getSelected().getReqId().getFromWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
            if(stockItem==null){
             stockItem = new WarehouseStock(new WarehouseStockPK(requestLineItemController.getSelected()
                                .getReqId().getToWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
             stockItem.setFaultyStockQty(BigInteger.ZERO);
             stockItem.setFmStockQty(BigInteger.ZERO);
             stockItem.setGoodStockQty(requestLineItemController.getSelected().getQty());
             stockItem.setLastTransactionTime(new Date());
             stockItem.setTotalStockQty(requestLineItemController.getSelected().getQty());
             stockItem.setItems(requestLineItemController.getSelected().getItemId());
             stockItem.setWarehouses(requestLineItemController.getSelected().getReqId().getFromWarehouse());
             warehouseStockController.setSelected(stockItem);
             warehouseStockController.create();
            }else{
            warehouseStockController.incrementGoodQty(stockItem, requestLineItemController.getSelected().getQty());
            }
             notificationController.createNotification(requestLineItemController.getSelected().getReqId().getFromWarehouse().getRegion(), 
                                                     requestLineItemController.getSelected().getReqId().getFromWarehouse(),
                                                     notificationTypeController.getNotificationType("REJECTED_REQUEST"), 
                                                    "TRANSFER item rejected "+requestLineItemController.getSelected().getQty()
                                                            +"["+requestLineItemController.getSelected().getItemId()
                                                                    .getPartNumber()+"]");
            update();
            clearValues();
        }
    }
    
    public void deductFMItem(){
        if(selected!=null && requestLineItemController.getSelected()!=null){
            WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                                 new WarehouseStockPK(requestLineItemController.getSelected().getReqId().getFromWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
            if(stockItem!=null){
                warehouseStockController.decrementFMQty(stockItem, requestLineItemController.getSelected().getReturnableQty());
            }
        }
    }
    public void returnItem(){
        if(selected!=null && requestLineItemController.getSelected()!=null){
            requestLineItemController.getSelected().setLineItemStatus(lineItemStatusController.getRequestLineItemStatus("Approved"));
            if(!requestLineItemController.getSelected().getStolen()){
                 WarehouseStock stockItem = warehouseStockController.getWarehouseStock(
                                 new WarehouseStockPK(requestLineItemController.getSelected().getReqId().getFromWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getReturnabeItemId().getPartNumber()));
            if(stockItem==null){
             stockItem = new WarehouseStock(new WarehouseStockPK(requestLineItemController.getSelected()
                                .getReqId().getFromWarehouse().getWarehouseName(), 
                                         requestLineItemController.getSelected().getItemId().getPartNumber()));
             if(requestLineItemController.getSelected().getReturnableStatus().getStatusName().equals("Good")){
                stockItem.setGoodStockQty(requestLineItemController.getSelected().getReturnableQty()); 
                stockItem.setFaultyStockQty(BigInteger.ZERO);
                stockItem.setFmStockQty(BigInteger.ZERO);
             }
             if(requestLineItemController.getSelected().getReturnableStatus().getStatusName().equals("Faulty")){
                stockItem.setFaultyStockQty(requestLineItemController.getSelected().getReturnableQty()); 
                stockItem.setGoodStockQty(BigInteger.ZERO);
                stockItem.setFmStockQty(BigInteger.ZERO);
             }
             if(requestLineItemController.getSelected().getReturnableStatus().getStatusName().equals("FM")){
                stockItem.setFmStockQty(requestLineItemController.getSelected().getReturnableQty()); 
                stockItem.setFaultyStockQty(BigInteger.ZERO);
                stockItem.setGoodStockQty(BigInteger.ZERO);
             }
             stockItem.setLastTransactionTime(new Date());
             stockItem.setTotalStockQty(requestLineItemController.getSelected().getReturnableQty());
             stockItem.setItems(requestLineItemController.getSelected().getItemId());
             stockItem.setWarehouses(requestLineItemController.getSelected().getReqId().getFromWarehouse());
             warehouseStockController.setSelected(stockItem);
             warehouseStockController.create();
            }else{
            if(requestLineItemController.getSelected().getReturnableStatus().getStatusName().equals("Good")){
                warehouseStockController.incrementGoodQty(stockItem, requestLineItemController.getSelected().getReturnableQty());
             }
             if(requestLineItemController.getSelected().getReturnableStatus().getStatusName().equals("Faulty")){
                warehouseStockController.incrementFaultyQty(stockItem, requestLineItemController.getSelected().getReturnableQty());
             }
             if(requestLineItemController.getSelected().getReturnableStatus().getStatusName().equals("FM")){
                warehouseStockController.incrementFMQty(stockItem, requestLineItemController.getSelected().getReturnableQty());
             }
             
             if(requestLineItemController.getSelected().getItemId().getPartNumber().equals(requestLineItemController.getSelected()
                    .getReturnabeItemId().getPartNumber())){
                warehouseStockController.decrementFMQty(stockItem, requestLineItemController.getSelected().getReturnableQty());
             }
            }
             notificationController.createNotification(requestLineItemController.getSelected().getReqId().getFromWarehouse().getRegion(), 
                                                     requestLineItemController.getSelected().getReqId().getFromWarehouse(),
                                                     notificationTypeController.getNotificationType("APPROVED_REQUEST"), 
                                                    "ISSUED item "+requestLineItemController.getSelected().getQty()
                                                            +"["+requestLineItemController.getSelected().getItemId()
                                                                    .getPartNumber()+"]");
            }else{
                // Stolen Item
                requestLineItemController.getSelected().setReturnableSerial("Stolen");
                warehouseStockController.update();
                notificationController.createNotification(requestLineItemController.getSelected().getReqId().getFromWarehouse().getRegion(), 
                                                     requestLineItemController.getSelected().getReqId().getFromWarehouse(),
                                                     notificationTypeController.getNotificationType("PENDING_REQUEST"), 
                                                    "STOLEN item "+requestLineItemController.getSelected().getQty()
                                                            +"["+requestLineItemController.getSelected().getItemId()
                                                                    .getPartNumber()+"]");
            }
            
            update();
            clearValues();
        }
    }
    
    public void redirect(){
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/VF_WMS/app/common/index.xhtml");        
        } catch (IOException ex) {
            Logger.getLogger(RequestsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
     
    public void clearValues(){
        selected = null;
        items = null;
        itemsController.setSelected(null);
        inBoundItems = null;
        transferItems = null;
        issueItems = null;
        itemsController.setSelected(null);
        warehouseStockController.setSelected(null);
        warehousesController.setSelected(null);
        requestLineItemController.setSelected(null);
    }
     
    public void clearRequestLineItems(){
         if(selected!=null){
             selected.setRequestLineItemCollection(null);
         }
     }


}
