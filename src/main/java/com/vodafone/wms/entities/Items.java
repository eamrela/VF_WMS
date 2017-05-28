/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "items")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Items.findAll", query = "SELECT i FROM Items i")
    , @NamedQuery(name = "Items.findByMaterialNumber", query = "SELECT i FROM Items i WHERE i.materialNumber = :materialNumber")
    , @NamedQuery(name = "Items.findByPartNumber", query = "SELECT i FROM Items i WHERE i.partNumber = :partNumber")
    , @NamedQuery(name = "Items.findByDescription", query = "SELECT i FROM Items i WHERE i.description = :description")
    , @NamedQuery(name = "Items.findByCategory", query = "SELECT i FROM Items i WHERE i.category = :category")})
public class Items implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "material_number")
    private String materialNumber;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "part_number")
    private String partNumber;
    @Size(max = 2147483647)
    @Column(name = "description")
    private String description;
    @Size(max = 2147483647)
    @Column(name = "category")
    private String category;
    @OneToMany(mappedBy = "itemId")
    private Collection<RequestLineItem> requestLineItemCollection;
    @OneToMany(mappedBy = "returnabeItemId")
    private Collection<RequestLineItem> requestLineItemCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "items")
    private Collection<WarehouseStock> warehouseStockCollection;

    public Items() {
    }

    public Items(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getMaterialNumber() {
        return materialNumber;
    }

    public void setMaterialNumber(String materialNumber) {
        this.materialNumber = materialNumber;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @XmlTransient
    public Collection<RequestLineItem> getRequestLineItemCollection() {
        return requestLineItemCollection;
    }

    public void setRequestLineItemCollection(Collection<RequestLineItem> requestLineItemCollection) {
        this.requestLineItemCollection = requestLineItemCollection;
    }

    @XmlTransient
    public Collection<RequestLineItem> getRequestLineItemCollection1() {
        return requestLineItemCollection1;
    }

    public void setRequestLineItemCollection1(Collection<RequestLineItem> requestLineItemCollection1) {
        this.requestLineItemCollection1 = requestLineItemCollection1;
    }

    @XmlTransient
    public Collection<WarehouseStock> getWarehouseStockCollection() {
        return warehouseStockCollection;
    }

    public void setWarehouseStockCollection(Collection<WarehouseStock> warehouseStockCollection) {
        this.warehouseStockCollection = warehouseStockCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (partNumber != null ? partNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Items)) {
            return false;
        }
        Items other = (Items) object;
        if ((this.partNumber == null && other.partNumber != null) || (this.partNumber != null && !this.partNumber.equals(other.partNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Items[ partNumber=" + partNumber + " ]";
    }
    
}
