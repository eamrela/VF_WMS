/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.beans;

import com.vodafone.wms.entities.Notifications;
import com.vodafone.wms.entities.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class NotificationsFacade extends AbstractFacade<Notifications> {

    @PersistenceContext(unitName = "com.vodafone_VF_WMS_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NotificationsFacade() {
        super(Notifications.class);
    }

    public List<Notifications> findByUser(Users loggedInUser) {
        return em.createNativeQuery(" select * " +
                                    " from notifications " +
                                    " where noti_region = '"+loggedInUser.getRegion().getRegionName()+"' " +
                                    " and noti_warehouse like '%%' " +
                                    " order by noti_time desc " +
                                    " limit 5 ", Notifications.class).getResultList();
    }

    public List<Notifications> findByRegion(String filterRegion) {
          return em.createNativeQuery(" select * " +
                                    " from notifications " +
                                    " where noti_region = '"+filterRegion+"' " +
                                    " order by noti_time desc " +
                                    " limit 1000 ", Notifications.class).getResultList();
    }
    
}
