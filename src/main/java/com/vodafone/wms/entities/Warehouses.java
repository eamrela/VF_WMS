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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "warehouses")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Warehouses.findAll", query = "SELECT w FROM Warehouses w")
    , @NamedQuery(name = "Warehouses.findByWarehouseName", query = "SELECT w FROM Warehouses w WHERE w.warehouseName = :warehouseName")
    , @NamedQuery(name = "Warehouses.findByWarehouseAddress", query = "SELECT w FROM Warehouses w WHERE w.warehouseAddress = :warehouseAddress")
    , @NamedQuery(name = "Warehouses.findByWarehouseSpoc", query = "SELECT w FROM Warehouses w WHERE w.warehouseSpoc = :warehouseSpoc")})
public class Warehouses implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "warehouse_name")
    private String warehouseName;
    @Size(max = 2147483647)
    @Column(name = "warehouse_address")
    private String warehouseAddress;
    @Size(max = 2147483647)
    @Column(name = "warehouse_spoc")
    private String warehouseSpoc;
    @ManyToMany(mappedBy = "warehousesCollection")
    private Collection<Users> usersCollection;
    @JoinColumn(name = "asp", referencedColumnName = "asp_name")
    @ManyToOne
    private Asp asp;
    @JoinColumn(name = "region", referencedColumnName = "region_name")
    @ManyToOne
    private Regions region;
    @OneToMany(mappedBy = "fromWarehouse")
    private Collection<Requests> requestsCollection;
    @OneToMany(mappedBy = "assignmentGroup")
    private Collection<Requests> requestsCollection1;
    @OneToMany(mappedBy = "toWarehouse")
    private Collection<Requests> requestsCollection2;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "warehouses")
    private Collection<WarehouseStock> warehouseStockCollection;
    @OneToMany(mappedBy = "notiWarehouse")
    private Collection<Notifications> notificationsCollection;

    public Warehouses() {
    }

    public Warehouses(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getWarehouseAddress() {
        return warehouseAddress;
    }

    public void setWarehouseAddress(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }

    public String getWarehouseSpoc() {
        return warehouseSpoc;
    }

    public void setWarehouseSpoc(String warehouseSpoc) {
        this.warehouseSpoc = warehouseSpoc;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public Asp getAsp() {
        return asp;
    }

    public void setAsp(Asp asp) {
        this.asp = asp;
    }

    public Regions getRegion() {
        return region;
    }

    public void setRegion(Regions region) {
        this.region = region;
    }

    @XmlTransient
    public Collection<Requests> getRequestsCollection() {
        return requestsCollection;
    }

    public void setRequestsCollection(Collection<Requests> requestsCollection) {
        this.requestsCollection = requestsCollection;
    }

    @XmlTransient
    public Collection<Requests> getRequestsCollection1() {
        return requestsCollection1;
    }

    public void setRequestsCollection1(Collection<Requests> requestsCollection1) {
        this.requestsCollection1 = requestsCollection1;
    }

    @XmlTransient
    public Collection<Requests> getRequestsCollection2() {
        return requestsCollection2;
    }

    public void setRequestsCollection2(Collection<Requests> requestsCollection2) {
        this.requestsCollection2 = requestsCollection2;
    }

    @XmlTransient
    public Collection<WarehouseStock> getWarehouseStockCollection() {
        return warehouseStockCollection;
    }

    public void setWarehouseStockCollection(Collection<WarehouseStock> warehouseStockCollection) {
        this.warehouseStockCollection = warehouseStockCollection;
    }

    @XmlTransient
    public Collection<Notifications> getNotificationsCollection() {
        return notificationsCollection;
    }

    public void setNotificationsCollection(Collection<Notifications> notificationsCollection) {
        this.notificationsCollection = notificationsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (warehouseName != null ? warehouseName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Warehouses)) {
            return false;
        }
        Warehouses other = (Warehouses) object;
        if ((this.warehouseName == null && other.warehouseName != null) || (this.warehouseName != null && !this.warehouseName.equals(other.warehouseName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Warehouses[ warehouseName=" + warehouseName + " ]";
    }
    
}
