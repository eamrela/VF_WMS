package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.Users;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.UsersFacade;
import com.vodafone.wms.entities.Roles;
import com.vodafone.wms.entities.Warehouses;

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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Named("usersController")
@SessionScoped
public class UsersController implements Serializable {

    private String role;
    @EJB
    private com.vodafone.wms.beans.UsersFacade ejbFacade;
    private List<Users> items = null;
    private Users selected;
    private Users loggedInUser;

    public UsersController() {
    }

    public Users getSelected() {
        return selected;
    }

    public Users getLoggedInUser() {
        if(loggedInUser==null){
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if(auth.getName()!=null){
               loggedInUser=getUsers(auth.getName());
            }
            
        }
        return loggedInUser;
    }

    
    public void setSelected(Users selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private UsersFacade getFacade() {
        return ejbFacade;
    }

    public Users prepareCreate() {
        selected = new Users();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("UsersCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("UsersUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("UsersDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Users> getItems() {
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

    public Users getUsers(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<Users> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Users> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Users.class)
    public static class UsersControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            UsersController controller = (UsersController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "usersController");
            return controller.getUsers(getKey(value));
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
            if (object instanceof Users) {
                Users o = (Users) object;
                return getStringKey(o.getUserEmail());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Users.class.getName()});
                return null;
            }
        }

    }

    public String getRole() {
        if(loggedInUser!=null){
            role = ((Roles)loggedInUser.getRolesCollection().toArray()[0]).getRoleName();
        }
        return role;
    }
    
    public boolean isTransfer(){
        if(getRole().equals("ROLE_SPC") ){
            return true;
        }
        return false;
    }
    public boolean isFaultyBatch(){
        if(getRole().equals("ROLE_SPC") ){
            return true;
        }
        return false;
    }
    public boolean isInBound(){
        if(getRole().equals("ROLE_SPC") ){
            return true;
        }
        return false;
    }
    public boolean isSPC(){
        if(getRole().equals("ROLE_SPC") ){
            return true;
        }
        return false;
    }
    
    public String getLoggedInUserWarehousesAsString(){
        String warehouseCollection = "";
        if(loggedInUser!=null){
            if(loggedInUser.getWarehousesCollection()!=null){
                Object[] warehouses = loggedInUser.getWarehousesCollection().toArray();
                for (Object warehouse : warehouses) {
                    warehouseCollection += "'" + ((Warehouses) warehouse).getWarehouseName() + "',";
                }
                if(warehouseCollection.length()>1){
                warehouseCollection = warehouseCollection.substring(0, warehouseCollection.length()-1);
                return warehouseCollection;
                }
            }
        }
        return warehouseCollection;
    }
}
