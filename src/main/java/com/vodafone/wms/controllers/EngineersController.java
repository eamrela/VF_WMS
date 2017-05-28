package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.Engineers;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.EngineersFacade;

import java.io.Serializable;
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

@Named("engineersController")
@SessionScoped
public class EngineersController implements Serializable {

    @EJB
    private com.vodafone.wms.beans.EngineersFacade ejbFacade;
    private List<Engineers> items = null;
    private Engineers selected;

    public EngineersController() {
    }

    public Engineers getSelected() {
        return selected;
    }

    public void setSelected(Engineers selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private EngineersFacade getFacade() {
        return ejbFacade;
    }

    public Engineers prepareCreate() {
        selected = new Engineers();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EngineersCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EngineersUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EngineersDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Engineers> getItems() {
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

    public Engineers getEngineers(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Engineers> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Engineers> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter("EngineersControllerConverter")
    public static class EngineersControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EngineersController controller = (EngineersController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "engineersController");
            return controller.getEngineers(getKey(value));
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
            if (object instanceof Engineers) {
                Engineers o = (Engineers) object;
                return getStringKey(o.getEngineerId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Engineers.class.getName()});
                return null;
            }
        }

    }

    public List<Engineers> searchEngineer(String query_sr){
        getItems();
        List<Engineers> result = new ArrayList<>();
        for (Engineers item : items) {
            if(item.getEngineerId().toLowerCase().contains(query_sr.toLowerCase())){
                result.add(item);
            }
            if(item.getEngineerName()!=null){
            if(item.getEngineerName().toLowerCase().contains(query_sr.toLowerCase())){
                result.add(item);
            }
            }
            if(item.getEngineerPhone()!=null){
            if(item.getEngineerPhone().toLowerCase().contains(query_sr.toLowerCase())){
                result.add(item);
            }
            }
        }
        
        return result;
    }
}
