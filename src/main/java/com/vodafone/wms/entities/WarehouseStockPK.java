/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author eamrela
 */
@Embeddable
public class WarehouseStockPK implements Serializable {

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "warehouse_id")
    private String warehouseId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "item_id")
    private String itemId;

    public WarehouseStockPK() {
    }

    public WarehouseStockPK(String warehouseId, String itemId) {
        this.warehouseId = warehouseId;
        this.itemId = itemId;
    }

    public String getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (warehouseId != null ? warehouseId.hashCode() : 0);
        hash += (itemId != null ? itemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof WarehouseStockPK)) {
            return false;
        }
        WarehouseStockPK other = (WarehouseStockPK) object;
        if ((this.warehouseId == null && other.warehouseId != null) || (this.warehouseId != null && !this.warehouseId.equals(other.warehouseId))) {
            return false;
        }
        if ((this.itemId == null && other.itemId != null) || (this.itemId != null && !this.itemId.equals(other.itemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.WarehouseStockPK[ warehouseId=" + warehouseId + ", itemId=" + itemId + " ]";
    }
    
}
