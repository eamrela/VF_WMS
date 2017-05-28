/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "regions")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Regions.findAll", query = "SELECT r FROM Regions r")
    , @NamedQuery(name = "Regions.findByRegionName", query = "SELECT r FROM Regions r WHERE r.regionName = :regionName")
    , @NamedQuery(name = "Regions.findByOpManager", query = "SELECT r FROM Regions r WHERE r.opManager = :opManager")
    , @NamedQuery(name = "Regions.findByWhManager", query = "SELECT r FROM Regions r WHERE r.whManager = :whManager")})
public class Regions implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "region_name")
    private String regionName;
    @Size(max = 2147483647)
    @Column(name = "op_manager")
    private String opManager;
    @Size(max = 2147483647)
    @Column(name = "wh_manager")
    private String whManager;
    @OneToMany(mappedBy = "region")
    private Collection<Warehouses> warehousesCollection;
    @OneToMany(mappedBy = "siteRegion")
    private Collection<Sites> sitesCollection;
    @OneToMany(mappedBy = "region")
    private Collection<Users> usersCollection;
    @OneToMany(mappedBy = "notiRegion")
    private Collection<Notifications> notificationsCollection;

    public Regions() {
    }

    public Regions(String regionName) {
        this.regionName = regionName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getOpManager() {
        return opManager;
    }

    public void setOpManager(String opManager) {
        this.opManager = opManager;
    }

    public String getWhManager() {
        return whManager;
    }

    public void setWhManager(String whManager) {
        this.whManager = whManager;
    }

    @XmlTransient
    public Collection<Warehouses> getWarehousesCollection() {
        return warehousesCollection;
    }

    public void setWarehousesCollection(Collection<Warehouses> warehousesCollection) {
        this.warehousesCollection = warehousesCollection;
    }

    @XmlTransient
    public Collection<Sites> getSitesCollection() {
        return sitesCollection;
    }

    public void setSitesCollection(Collection<Sites> sitesCollection) {
        this.sitesCollection = sitesCollection;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
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
        hash += (regionName != null ? regionName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Regions)) {
            return false;
        }
        Regions other = (Regions) object;
        if ((this.regionName == null && other.regionName != null) || (this.regionName != null && !this.regionName.equals(other.regionName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Regions[ regionName=" + regionName + " ]";
    }
    
}
