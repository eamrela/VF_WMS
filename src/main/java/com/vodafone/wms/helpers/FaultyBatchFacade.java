/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.helpers;

import com.vodafone.wms.beans.AbstractFacade;
import com.vodafone.wms.entities.Configuration;
import com.vodafone.wms.entities.RequestLineItem;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class FaultyBatchFacade  extends AbstractFacade<RequestLineItem>{
    
     @PersistenceContext(unitName = "com.vodafone_VF_WMS_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FaultyBatchFacade() {
        super(RequestLineItem.class);
    }
    
    public List<RequestLineItem> findFaultyBatch(String regionName) {
       return em.createNativeQuery(" select * " +
                                    " from request_line_item " +
                                    " where req_id in  " +
                                    "(select req_id from requests  " +
                                    " where req_type='Issued-Material' " +
                                    " and from_warehouse in (select warehouse_name from warehouses where region='"+regionName+"'))"
                                  + " and (returnable_status = 'Faulty' or stolen is true)"
                                  + " and faulty_batch_date is null ", 
                                    RequestLineItem.class).getResultList();
    }

    public String findFaultyBatchDirectory(String regionName) {
        List<Configuration> conf = em.createNativeQuery(
                "select * from configuration where conf_name='FAULTY_BACTCH_DIR' and conf_enviroment='DEV'", 
                Configuration.class).getResultList();
        if(conf.size()>0){
            return conf.get(0).getConfValue();
        }
        return null;
    }
}
