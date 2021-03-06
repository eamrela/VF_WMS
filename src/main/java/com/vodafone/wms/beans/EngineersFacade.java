/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.beans;

import com.vodafone.wms.entities.Engineers;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class EngineersFacade extends AbstractFacade<Engineers> {

    @PersistenceContext(unitName = "com.vodafone_VF_WMS_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EngineersFacade() {
        super(Engineers.class);
    }
    
}
