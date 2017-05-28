/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.beans;

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
public class RequestLineItemFacade extends AbstractFacade<RequestLineItem> {

    @PersistenceContext(unitName = "com.vodafone_VF_WMS_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RequestLineItemFacade() {
        super(RequestLineItem.class);
    }

    public List<RequestLineItem> findIssuedByRegion(String regionName) {
        return em.createNativeQuery(" select * " +
                                    " from request_line_item " +
                                    " where req_id in  " +
                                    "(select req_id from requests  " +
                                    " where req_type='Issued-Material' " +
                                    " and from_warehouse in (select warehouse_name from warehouses where region='"+regionName+"')) ", 
                                    RequestLineItem.class).getResultList();
    }

    public List<RequestLineItem> findNFFByRegion(String filterRegion) {
        return em.createNativeQuery(" select * " +
                                    " from request_line_item " +
                                    " where req_id in  " +
                                    "(select req_id from requests  " +
                                    " where req_type='Issued-Material' " +
                                    " and from_warehouse in (select warehouse_name from warehouses where region='"+filterRegion+"')) "
                                            + " and returnable_status != 'Faulty' ", 
                                    RequestLineItem.class).getResultList();
    }

    public List<RequestLineItem> findStolenByRegion(String filterRegion) {
        return em.createNativeQuery(" select * " +
                                    " from request_line_item " +
                                    " where req_id in  " +
                                    "(select req_id from requests  " +
                                    " where req_type='Issued-Material' " +
                                    " and from_warehouse in (select warehouse_name from warehouses where region='"+filterRegion+"')) "
                                + " and stolen is true ",
                RequestLineItem.class).getResultList();
    }

    public List<Object[]> findTop5Items(String regionName) {
        return em.createNativeQuery("select item_id,count(item_id) " +
                                    "from request_line_item  " +
                                    "where req_id in  " +
                                    "(select req_id from requests  " +
                                    "where req_type='Issued-Material' " +
                                    "and from_warehouse in (select warehouse_name from warehouses where region='"+regionName+"')) " +
                                    "group by item_id " +
                                    "order by count(item_id) desc " +
                                    "limit 10").getResultList();
    }
   
    public List<Object[]> findTop5Sites(String regionName) {
        return em.createNativeQuery(" select site_id,count(site_id) " +
                                    " from requests " +
                                    " where  " +
                                    " req_type='Issued-Material' " +
                                    " and " +
                                    " from_warehouse in (select warehouse_name from warehouses where region='"+regionName+"') " +
                                    " group by site_id " +
                                    " order by count(site_id) desc " +
                                    " limit 10 ").getResultList();
    }
    
    public List<Object[]> findFaultyCount(String regionName) {
        return em.createNativeQuery(" select req_date,sum(qty) " +
                                    " from ( " +
                                    " select req_time::date req_date,(select sum(qty) "
                                   +" from request_line_item where req_id=r.req_id group by req_id ) qty " +
                                    " from requests r " +
                                    " where " +
                                    " req_type='Issued-Material'  " +
                                    " and " +
                                    " from_warehouse in (select warehouse_name from warehouses where region='"+regionName+"') "
                                     + " and req_time::date between (now()::date-'7 days'::interval)::date and (now()+'1 day'::interval)::date " +
                                    " ) a " +
                                    " group by req_date " +
                                    " order by req_date asc " +
                                    " limit 5 ").getResultList();
    }
    
}
