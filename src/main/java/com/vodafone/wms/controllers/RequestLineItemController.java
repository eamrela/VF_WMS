package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.RequestLineItem;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.RequestLineItemFacade;
import com.vodafone.wms.entities.Items;

import java.io.Serializable;
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

@Named("requestLineItemController")
@SessionScoped
public class RequestLineItemController implements Serializable {

    @EJB
    private com.vodafone.wms.beans.RequestLineItemFacade ejbFacade;
    private List<RequestLineItem> items = null;
    private List<RequestLineItem> issuedMaterialsByRegion = null;
    private List<RequestLineItem> noFaultFoundByRegion = null;
    private List<RequestLineItem> stolenMaterialsByRegion = null;
    private RequestLineItem selected;
    
     @Inject
    private UsersController usersController;
     
     private String filterRegion;

    
    public RequestLineItemController() {
    }

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

    private RequestLineItemFacade getFacade() {
        return ejbFacade;
    }

    public String getFilterRegion() {
        return filterRegion;
    }

    public void setFilterRegion(String filterRegion) {
        this.filterRegion = filterRegion;
    }
    
    public RequestLineItem prepareCreate() {
        selected = new RequestLineItem();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("RequestLineItemCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            issuedMaterialsByRegion = null;
            noFaultFoundByRegion = null;
            stolenMaterialsByRegion = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("RequestLineItemUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("RequestLineItemDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            issuedMaterialsByRegion = null;
            noFaultFoundByRegion = null;
            stolenMaterialsByRegion = null;
        }
    }

    public List<RequestLineItem> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public List<RequestLineItem> getIssuedMaterialsByRegion() {
        if(issuedMaterialsByRegion==null){
            if(filterRegion==null){
            issuedMaterialsByRegion = getFacade().findIssuedByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            issuedMaterialsByRegion = getFacade().findIssuedByRegion(filterRegion);
            }
        }
        return issuedMaterialsByRegion;
    }

    public List<RequestLineItem> getNoFaultFoundByRegion() {
        if(noFaultFoundByRegion==null){
            if(filterRegion==null){
            noFaultFoundByRegion = getFacade().findNFFByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            noFaultFoundByRegion = getFacade().findNFFByRegion(filterRegion);
            }
        }
        return noFaultFoundByRegion;
    }

    public List<RequestLineItem> getStolenMaterialsByRegion() {
        if(stolenMaterialsByRegion==null){
            if(filterRegion==null){
            stolenMaterialsByRegion = getFacade().findStolenByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            stolenMaterialsByRegion = getFacade().findStolenByRegion(filterRegion);
            }
        }
        return stolenMaterialsByRegion;
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

    public RequestLineItem getRequestLineItem(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<RequestLineItem> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<RequestLineItem> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    public List<Object[]> getTop5Items() {
        return getFacade().findTop5Items(usersController.getLoggedInUser().getRegion().getRegionName());
    }
   
    public List<Object[]> getTop5Sites() {
        return getFacade().findTop5Sites(usersController.getLoggedInUser().getRegion().getRegionName());
    }
    
    public List<Object[]> getFaultyCount() {
        return getFacade().findFaultyCount(usersController.getLoggedInUser().getRegion().getRegionName());
    }
    

    @FacesConverter(forClass = RequestLineItem.class)
    public static class RequestLineItemControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RequestLineItemController controller = (RequestLineItemController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "requestLineItemController");
            return controller.getRequestLineItem(getKey(value));
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
            if (object instanceof RequestLineItem) {
                RequestLineItem o = (RequestLineItem) object;
                return getStringKey(o.getLineItemId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), RequestLineItem.class.getName()});
                return null;
            }
        }

    }

    public void updateReturnable(Items item){
        if(selected!=null){
            System.out.println(selected.getLineItemId()+" , "+item.getPartNumber());
            selected.setReturnabeItemId(item);
        }
    }
   
    public void onFilterAreaSelect(){
        issuedMaterialsByRegion = null;
        noFaultFoundByRegion = null;
        stolenMaterialsByRegion = null;
    }
}
