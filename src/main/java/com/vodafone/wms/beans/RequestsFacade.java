/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.beans;

import com.vodafone.wms.entities.Requests;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class RequestsFacade extends AbstractFacade<Requests> {

    @PersistenceContext(unitName = "com.vodafone_VF_WMS_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RequestsFacade() {
        super(Requests.class);
    }

    public Requests merge(Requests selected) {
        return em.merge(selected);
    }

    public List<Requests> findInBoundItemsByRegion(String regionName) {
        return em.createNativeQuery("select * from requests r where " +
                                    "req_type = 'In-Bound' and " +
                                    "assignment_group in  " +
                                    "(select warehouse_name from warehouses where region = '"+regionName+"') " +
                                    "and (select count(*) " +
                                    "     from request_line_item " +
                                    "     where req_id = r.req_id " +
                                    "     and line_item_status = 'Pending Approval')>0 ", Requests.class).getResultList();
    }

    public List<Requests> findTransferItemsByRegion(String regionName) {
        return em.createNativeQuery("select * from requests r where " +
                                    "req_type = 'Transfer' and " +
                                    "assignment_group in  " +
                                    "(select warehouse_name from warehouses where region = '"+regionName+"') " +
                                    "and (select count(*) " +
                                    "     from request_line_item " +
                                    "     where req_id = r.req_id " +
                                    "     and line_item_status in ('Pending Approval','Shipped'))>0 ", Requests.class).getResultList();
    }

    public List<Requests> findIssueItemsByRegion(String regionName) {
        return em.createNativeQuery("select * from requests r where " +
                                    "req_type = 'Issued-Material' and " +
                                    "assignment_group in  " +
                                    "(select warehouse_name from warehouses where region = '"+regionName+"') " +
                                    "and (select count(*) " +
                                    "     from request_line_item " +
                                    "     where req_id = r.req_id " +
                                    "     and line_item_status in ('Pending Approval','Pending Return'))>0 ", Requests.class).getResultList();
    }

    public List<Requests> findInBoundItemsByWarehouse(String loggedInUserWarehousesAsString) {
        return em.createNativeQuery("select * from requests r where " +
                                    "req_type = 'In-Bound' and " +
                                    "assignment_group in  " +
                                    "( "+loggedInUserWarehousesAsString+") " +
                                    "and (select count(*) " +
                                    "     from request_line_item " +
                                    "     where req_id = r.req_id " +
                                    "     and line_item_status = 'Pending Approval')>0 ", Requests.class).getResultList();
    }

    public List<Requests> findTransferItemsByWarehouse(String loggedInUserWarehousesAsString) {
        System.out.println("Getting Transfer Items for "+loggedInUserWarehousesAsString);
        return em.createNativeQuery("select * from requests r where " +
                                    "req_type = 'Transfer' and " +
                                    "assignment_group in  " +
                                    "( "+loggedInUserWarehousesAsString+") " +
                                    "and (select count(*) " +
                                    "     from request_line_item " +
                                    "     where req_id = r.req_id " +
                                    "     and line_item_status in ('Pending Approval','Shipped') )>0 ", Requests.class).getResultList();
    }

    public List<Requests> findIssueItemsByWarehouse(String loggedInUserWarehousesAsString) {
         return em.createNativeQuery("select * from requests r where " +
                                    "req_type = 'Issued-Material' and " +
                                    "assignment_group in  " +
                                    "( "+loggedInUserWarehousesAsString+") " +
                                    "and (select count(*) " +
                                    "     from request_line_item " +
                                    "     where req_id = r.req_id " +
                                    "     and line_item_status in ('Pending Approval','Pending Return'))>0 ", Requests.class).getResultList();
    }
    
}
