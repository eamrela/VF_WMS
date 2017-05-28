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
@Table(name = "asp")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Asp.findAll", query = "SELECT a FROM Asp a")
    , @NamedQuery(name = "Asp.findByAspName", query = "SELECT a FROM Asp a WHERE a.aspName = :aspName")
    , @NamedQuery(name = "Asp.findByAspSpoc", query = "SELECT a FROM Asp a WHERE a.aspSpoc = :aspSpoc")})
public class Asp implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "asp_name")
    private String aspName;
    @Size(max = 2147483647)
    @Column(name = "asp_spoc")
    private String aspSpoc;
    @OneToMany(mappedBy = "asp")
    private Collection<Warehouses> warehousesCollection;

    public Asp() {
    }

    public Asp(String aspName) {
        this.aspName = aspName;
    }

    public String getAspName() {
        return aspName;
    }

    public void setAspName(String aspName) {
        this.aspName = aspName;
    }

    public String getAspSpoc() {
        return aspSpoc;
    }

    public void setAspSpoc(String aspSpoc) {
        this.aspSpoc = aspSpoc;
    }

    @XmlTransient
    public Collection<Warehouses> getWarehousesCollection() {
        return warehousesCollection;
    }

    public void setWarehousesCollection(Collection<Warehouses> warehousesCollection) {
        this.warehousesCollection = warehousesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (aspName != null ? aspName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Asp)) {
            return false;
        }
        Asp other = (Asp) object;
        if ((this.aspName == null && other.aspName != null) || (this.aspName != null && !this.aspName.equals(other.aspName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Asp[ aspName=" + aspName + " ]";
    }
    
}
