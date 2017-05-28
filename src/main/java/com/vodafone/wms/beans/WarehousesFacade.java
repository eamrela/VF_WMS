/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.beans;

import com.vodafone.wms.entities.Warehouses;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class WarehousesFacade extends AbstractFacade<Warehouses> {

    @PersistenceContext(unitName = "com.vodafone_VF_WMS_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WarehousesFacade() {
        super(Warehouses.class);
    }

    public List<Warehouses> findItemsByRegion(String region) {
        return em.createNativeQuery("select * from warehouses where region ='"+region+"'",Warehouses.class).getResultList();
    }

   
    
}
