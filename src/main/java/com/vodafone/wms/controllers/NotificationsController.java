package com.vodafone.wms.controllers;

import com.vodafone.wms.entities.Notifications;
import com.vodafone.wms.controllers.util.JsfUtil;
import com.vodafone.wms.controllers.util.JsfUtil.PersistAction;
import com.vodafone.wms.beans.NotificationsFacade;
import com.vodafone.wms.entities.NotificationType;
import com.vodafone.wms.entities.Regions;
import com.vodafone.wms.entities.Warehouses;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("notificationsController")
@SessionScoped
public class NotificationsController implements Serializable {

    @EJB
    private com.vodafone.wms.beans.NotificationsFacade ejbFacade;
    private List<Notifications> items = null;
    private List<Notifications> notificationPerRegion = null;
    private List<Notifications> activitiesByRegion = null;
    private Notifications selected;
    
    @Inject
    private UsersController usersController;
     
    private Integer numberOfNotifications;
    private String filterRegion;
    
    public NotificationsController() {
    }

    public Notifications getSelected() {
        return selected;
    }

    public void setSelected(Notifications selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private NotificationsFacade getFacade() {
        return ejbFacade;
    }

    public Notifications prepareCreate() {
        selected = new Notifications();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle5").getString("NotificationsCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
            notificationPerRegion = null;
            activitiesByRegion = null;
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle5").getString("NotificationsUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle5").getString("NotificationsDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
            notificationPerRegion = null;
            activitiesByRegion = null;
        }
    }

    public List<Notifications> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    public void setNumberOfNotifications(Integer numberOfNotifications) {
        this.numberOfNotifications = numberOfNotifications;
    }
    
    
    
 

    public List<Notifications> getNotificationPerRegion() {
        if (notificationPerRegion == null) {
            notificationPerRegion = getFacade().findByUser(usersController.getLoggedInUser());
            setNumberOfNotifications(notificationPerRegion.size());
        }
        return notificationPerRegion;
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
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle5").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle5").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Notifications getNotifications(java.lang.Long id) {
        return getFacade().find(id);
    }

    public List<Notifications> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Notifications> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Notifications.class)
    public static class NotificationsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NotificationsController controller = (NotificationsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "notificationsController");
            return controller.getNotifications(getKey(value));
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
            if (object instanceof Notifications) {
                Notifications o = (Notifications) object;
                return getStringKey(o.getNotiId());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Notifications.class.getName()});
                return null;
            }
        }

    }

    public Integer getNumberOfNotifications() {
        if(numberOfNotifications==null){
            return 0;
        }
        return numberOfNotifications;
    }

    public void createNotification(Regions region,Warehouses warehouse,NotificationType notiType,String description){
        prepareCreate();
        selected.setNotiDescription(description);
        selected.setNotiWarehouse(warehouse);
        selected.setNotiRegion(region);
        selected.setNotiType(notiType);
        selected.setNotiTime(new Date());
        create();
        
    }
    
    public void updateNotification(){
        notificationPerRegion = null;
        getNotificationPerRegion();
//        System.out.println("Updating Notifications");
    }

    public String getFilterRegion() {
        return filterRegion;
    }

    public void setFilterRegion(String filterRegion) {
        this.filterRegion = filterRegion;
    }

    public void onFilterAreaSelect(){
        activitiesByRegion = null;
    }
    public List<Notifications> getActivitiesByRegion() {
         if(activitiesByRegion==null){
            if(filterRegion==null){
            activitiesByRegion = getFacade().findByRegion(usersController.getLoggedInUser().getRegion().getRegionName());
            }else{
            activitiesByRegion = getFacade().findByRegion(filterRegion);
            }
        }
        return activitiesByRegion;
    }
    
    
}
