/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "warehouse_stock")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "WarehouseStock.findAll", query = "SELECT w FROM WarehouseStock w")
    , @NamedQuery(name = "WarehouseStock.findByWarehouseId", query = "SELECT w FROM WarehouseStock w WHERE w.warehouseStockPK.warehouseId = :warehouseId")
    , @NamedQuery(name = "WarehouseStock.findByItemId", query = "SELECT w FROM WarehouseStock w WHERE w.warehouseStockPK.itemId = :itemId")
    , @NamedQuery(name = "WarehouseStock.findByGoodStockQty", query = "SELECT w FROM WarehouseStock w WHERE w.goodStockQty = :goodStockQty")
    , @NamedQuery(name = "WarehouseStock.findByFaultyStockQty", query = "SELECT w FROM WarehouseStock w WHERE w.faultyStockQty = :faultyStockQty")
    , @NamedQuery(name = "WarehouseStock.findByFmStockQty", query = "SELECT w FROM WarehouseStock w WHERE w.fmStockQty = :fmStockQty")
    , @NamedQuery(name = "WarehouseStock.findByTotalStockQty", query = "SELECT w FROM WarehouseStock w WHERE w.totalStockQty = :totalStockQty")
    , @NamedQuery(name = "WarehouseStock.findByLastTransactionTime", query = "SELECT w FROM WarehouseStock w WHERE w.lastTransactionTime = :lastTransactionTime")})
public class WarehouseStock implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected WarehouseStockPK warehouseStockPK;
    @Column(name = "good_stock_qty")
    private BigInteger goodStockQty;
    @Column(name = "faulty_stock_qty")
    private BigInteger faultyStockQty;
    @Column(name = "fm_stock_qty")
    private BigInteger fmStockQty;
    @Column(name = "total_stock_qty")
    private BigInteger totalStockQty;
    @Column(name = "last_transaction_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTransactionTime;
    @JoinColumn(name = "item_id", referencedColumnName = "part_number", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Items items;
    @JoinColumn(name = "warehouse_id", referencedColumnName = "warehouse_name", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Warehouses warehouses;

    public WarehouseStock() {
    }

    public WarehouseStock(WarehouseStockPK warehouseStockPK) {
        this.warehouseStockPK = warehouseStockPK;
    }

    public WarehouseStock(String warehouseId, String itemId) {
        this.warehouseStockPK = new WarehouseStockPK(warehouseId, itemId);
    }

    public WarehouseStockPK getWarehouseStockPK() {
        return warehouseStockPK;
    }

    public void setWarehouseStockPK(WarehouseStockPK warehouseStockPK) {
        this.warehouseStockPK = warehouseStockPK;
    }

    public BigInteger getGoodStockQty() {
        return goodStockQty;
    }

    public void setGoodStockQty(BigInteger goodStockQty) {
        this.goodStockQty = goodStockQty;
    }

    public BigInteger getFaultyStockQty() {
        return faultyStockQty;
    }

    public void setFaultyStockQty(BigInteger faultyStockQty) {
        this.faultyStockQty = faultyStockQty;
    }

    public BigInteger getFmStockQty() {
        return fmStockQty;
    }

    public void setFmStockQty(BigInteger fmStockQty) {
        this.fmStockQty = fmStockQty;
    }

    public BigInteger getTotalStockQty() {
        return totalStockQty;
    }

    public void setTotalStockQty(BigInteger totalStockQty) {
        this.totalStockQty = totalStockQty;
    }

    public Date getLastTransactionTime() {
        return lastTransactionTime;
    }

    public void setLastTransactionTime(Date lastTransactionTime) {
        this.lastTransactionTime = lastTransactionTime;
    }

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public Warehouses getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(Warehouses warehouses) {
        this.warehouses = warehouses;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (warehouseStockPK != null ? warehouseStockPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WarehouseStock)) {
            return false;
        }
        WarehouseStock other = (WarehouseStock) object;
        if ((this.warehouseStockPK == null && other.warehouseStockPK != null) || (this.warehouseStockPK != null && !this.warehouseStockPK.equals(other.warehouseStockPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.WarehouseStock[ warehouseStockPK=" + warehouseStockPK + " ]";
    }
    
}
