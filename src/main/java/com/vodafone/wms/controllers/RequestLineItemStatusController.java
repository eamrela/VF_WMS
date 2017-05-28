package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.RequestLineItemStatus;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.RequestLineItemStatusFacade;

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

@Named("requestLineItemStatusController")
@SessionScoped
public class RequestLineItemStatusController implements Serializable {

    @EJB
    private com.vodafone.wms.beans.RequestLineItemStatusFacade ejbFacade;
    private List<RequestLineItemStatus> items = null;
    private RequestLineItemStatus selected;

    public RequestLineItemStatusController() {
    }

    public RequestLineItemStatus getSelected() {
        return selected;
    }

    public void setSelected(RequestLineItemStatus selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private RequestLineItemStatusFacade getFacade() {
        return ejbFacade;
    }

    public RequestLineItemStatus prepareCreate() {
        selected = new RequestLineItemStatus();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle2").getString("RequestLineItemStatusCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle2").getString("RequestLineItemStatusUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle2").getString("RequestLineItemStatusDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<RequestLineItemStatus> getItems() {
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle2").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle2").getString("PersistenceErrorOccured"));
            }
        }
    }

    public RequestLineItemStatus getRequestLineItemStatus(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<RequestLineItemStatus> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<RequestLineItemStatus> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("RequestLineItemStatusControllerConverter")
    public static class RequestLineItemStatusControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            RequestLineItemStatusController controller = (RequestLineItemStatusController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "requestLineItemStatusController");
            return controller.getRequestLineItemStatus(getKey(value));
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
            if (object instanceof RequestLineItemStatus) {
                RequestLineItemStatus o = (RequestLineItemStatus) object;
                return getStringKey(o.getStatusName());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), RequestLineItemStatus.class.getName()});
                return null;
            }
        }

    }

}
