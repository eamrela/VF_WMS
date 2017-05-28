package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.ItemStatus;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.ItemStatusFacade;

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

@Named("itemStatusController")
@SessionScoped
public class ItemStatusController implements Serializable {

    @EJB
    private com.vodafone.wms.beans.ItemStatusFacade ejbFacade;
    private List<ItemStatus> items = null;
    private ItemStatus selected;

    public ItemStatusController() {
    }

    public ItemStatus getSelected() {
        return selected;
    }

    public void setSelected(ItemStatus selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private ItemStatusFacade getFacade() {
        return ejbFacade;
    }

    public ItemStatus prepareCreate() {
        selected = new ItemStatus();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("ItemStatusCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("ItemStatusUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("ItemStatusDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<ItemStatus> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
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

    public ItemStatus getItemStatus(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<ItemStatus> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<ItemStatus> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("ItemStatusControllerConverter")
    public static class ItemStatusControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ItemStatusController controller = (ItemStatusController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "itemStatusController");
            return controller.getItemStatus(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof ItemStatus) {
                ItemStatus o = (ItemStatus) object;
                return getStringKey(o.getStatusName());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), ItemStatus.class.getName()});
                return null;
            }
        }

    }

}
