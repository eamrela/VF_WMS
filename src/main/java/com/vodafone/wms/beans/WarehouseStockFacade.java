/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.beans;

import com.vodafone.wms.entities.Users;
import com.vodafone.wms.entities.WarehouseStock;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author eamrela
 */
@Stateless
public class WarehouseStockFacade extends AbstractFacade<WarehouseStock> {

    @PersistenceContext(unitName = "com.vodafone_VF_WMS_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WarehouseStockFacade() {
        super(WarehouseStock.class);
    }

    public List<WarehouseStock> findByRegion(String region) {
        return em.createNativeQuery("select * from warehouse_stock where warehouse_id in " +
"(select warehouse_name from warehouses where region = '"+region+"')", WarehouseStock.class).getResultList();
    }

    public List<WarehouseStock> findWarehouseStockGood(String warehouseName) {
        return em.createNativeQuery("select * from warehouse_stock " +
                                    "where warehouse_id = '"+warehouseName+"'" +
                                    "and good_stock_qty > 0", WarehouseStock.class).getResultList();
    }

    public BigInteger findGoodStockQty(String partNumber, String warehouseName) {
        List<WarehouseStock> result= em.createNativeQuery("select * from warehouse_stock " +
                                    "where warehouse_id = '"+warehouseName+"'" +
                                    "and item_id = '"+partNumber+"'", WarehouseStock.class).getResultList();
        if(result.size()>0){
            return result.get(0).getGoodStockQty();
        }
        return BigInteger.ZERO;
    }
    
}
