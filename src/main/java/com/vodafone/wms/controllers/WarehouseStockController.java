package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.WarehouseStock;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.WarehouseStockFacade;
import com.vodafone.wms.entities.Items;
import com.vodafone.wms.entities.RequestLineItem;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
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

@Named("warehouseStockController")
@SessionScoped
public class WarehouseStockController implements Serializable {

    @EJB
    private com.vodafone.wms.beans.WarehouseStockFacade ejbFacade;
    private List<WarehouseStock> items = null;
    private List<WarehouseStock> itemsByRegion = null;
    private WarehouseStock selected;

    @Inject
    private UsersController usersController;
    @Inject
    private RequestsController requestController;
    
    private String filterRegion;
    
    public WarehouseStockController() {
    }

    public WarehouseStock getSelected() {
        return selected;
    }

    public void setSelected(WarehouseStock selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getWarehouseStockPK().setItemId(selected.getItems().getPartNumber());
        selected.getWarehouseStockPK().setWarehouseId(selected.getWarehouses().getWarehouseName());
    }

    protected void initializeEmbeddableKey() {
        selected.setWarehouseStockPK(new com.vodafone.wms.entities.WarehouseStockPK());
    }

    private WarehouseStockFacade getFacade() {
        return ejbFacade;
    }

    public WarehouseStock prepareCreate() {
        selected = new WarehouseStock();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("WarehouseStockCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            itemsByRegion = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("WarehouseStockUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("WarehouseStockDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            itemsByRegion = null;
        }
    }

    public List<WarehouseStock> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<WarehouseStock> getItemsByRegion() {
        if(itemsByRegion==null){
            if(filterRegion==null){
            itemsByRegion = getFacade().findByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            itemsByRegion = getFacade().findByRegion(filterRegion);
            }
        }
        return itemsByRegion;
    }

    public String getFilterRegion() {
        return filterRegion;
    }

    public void setFilterRegion(String filterRegion) {
        this.filterRegion = filterRegion;
    }

    public void onFilterAreaSelect(){
        itemsByRegion = null;
    }

    public UsersController getUsersController() {
        return usersController;
    }
    
    
    
    
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
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

    public WarehouseStock getWarehouseStock(com.vodafone.wms.entities.WarehouseStockPK id) {
        return getFacade().find(id);
    }

    public List<WarehouseStock> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<WarehouseStock> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public void incrementFaultyQty(WarehouseStock stockItem, BigInteger returnableQty) {
        if(stockItem!=null){
            stockItem.setFaultyStockQty(stockItem.getFaultyStockQty().add(returnableQty));
            setSelected(stockItem);
            update();
        }
    }

    public void decrementFaultyQty(WarehouseStock stockItem, BigInteger qty) {
        if(stockItem!=null){
            stockItem.setFaultyStockQty(stockItem.getFaultyStockQty().subtract(qty));
            setSelected(stockItem);
            update();
        }
    }

    @FacesConverter("WarehouseStockControllerConverter")
    public static class WarehouseStockControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            WarehouseStockController controller = (WarehouseStockController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "warehouseStockController");
            return controller.getWarehouseStock(getKey(value));
        }

        com.vodafone.wms.entities.WarehouseStockPK getKey(String value) {
            com.vodafone.wms.entities.WarehouseStockPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new com.vodafone.wms.entities.WarehouseStockPK();
            key.setWarehouseId(values[0]);
            key.setItemId(values[1]);
            return key;
        }

        String getStringKey(com.vodafone.wms.entities.WarehouseStockPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getWarehouseId());
            sb.append(SEPARATOR);
            sb.append(value.getItemId());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof WarehouseStock) {
                WarehouseStock o = (WarehouseStock) object;
                return getStringKey(o.getWarehouseStockPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), WarehouseStock.class.getName()});
                return null;
            }
        }

    }

    
    public List<WarehouseStock> searchGoodItems(String query) {
        List<WarehouseStock> results = new ArrayList<>();
        List<WarehouseStock> stockItems = new ArrayList<>();
        if(requestController.getSelected()!=null){
            if(requestController.getSelected().getFromWarehouse()!=null){
                System.out.println("Searching "+requestController.getSelected().getFromWarehouse());
                results = getFacade().findWarehouseStockGood(requestController.getSelected().getFromWarehouse().getWarehouseName());
        for (WarehouseStock item : stockItems) {
            if(item.getItems().getPartNumber().toLowerCase().startsWith(query.toLowerCase())){ // bom
                results.add(item);
            }
            if(item.getItems().getDescription()!=null){
            if(item.getItems().getDescription().toLowerCase().startsWith(query.toLowerCase())){ // description
                results.add(item);
            }
            }
            if(item.getItems().getMaterialNumber()!=null){
            if(item.getItems().getMaterialNumber().toLowerCase().startsWith(query.toLowerCase())){ // description
                results.add(item);
            }
            }
        }
            }
        }
        return results;
    }
    
    public void incrementGoodQty(WarehouseStock item,BigInteger qty){
        if(item!=null){
            item.setGoodStockQty(item.getGoodStockQty().add(qty));
            setSelected(item);
            update();
        }
    }
    
    public void decrementGoodQty(WarehouseStock item,BigInteger qty){
        if(item!=null){
            item.setGoodStockQty(item.getGoodStockQty().subtract(qty));
            setSelected(item);
            update();
        }
    }
    
    public void incrementFMQty(WarehouseStock item,BigInteger qty){
        if(item!=null){
            item.setFmStockQty(item.getFmStockQty().add(qty));
            setSelected(item);
            update();
        }
    }
    
    public void decrementFMQty(WarehouseStock item,BigInteger qty){
        if(item!=null){
            item.setFmStockQty(item.getFmStockQty().subtract(qty));
            setSelected(item);
            update();
        }
    }
    
     public BigInteger getGoodStockQty(RequestLineItem lineItem){
        if(lineItem!=null){
            if(lineItem.getReqId().getFromWarehouse()!=null){
                if(lineItem.getItemId()!=null){
                return getFacade().findGoodStockQty(lineItem.getItemId().getPartNumber(),lineItem.getReqId().getFromWarehouse().getWarehouseName());
                }
            }else{
                JsfUtil.addErrorMessage("You need to select a warehouse");
            }
        }
        return BigInteger.ZERO;
    }
}
