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
@Table(name = "item_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ItemStatus.findAll", query = "SELECT i FROM ItemStatus i")
    , @NamedQuery(name = "ItemStatus.findByStatusName", query = "SELECT i FROM ItemStatus i WHERE i.statusName = :statusName")
    , @NamedQuery(name = "ItemStatus.findByStatusDescription", query = "SELECT i FROM ItemStatus i WHERE i.statusDescription = :statusDescription")})
public class ItemStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "status_name")
    private String statusName;
    @Size(max = 2147483647)
    @Column(name = "status_description")
    private String statusDescription;
    @OneToMany(mappedBy = "returnableStatus")
    private Collection<RequestLineItem> requestLineItemCollection;

    public ItemStatus() {
    }

    public ItemStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @XmlTransient
    public Collection<RequestLineItem> getRequestLineItemCollection() {
        return requestLineItemCollection;
    }

    public void setRequestLineItemCollection(Collection<RequestLineItem> requestLineItemCollection) {
        this.requestLineItemCollection = requestLineItemCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statusName != null ? statusName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemStatus)) {
            return false;
        }
        ItemStatus other = (ItemStatus) object;
        if ((this.statusName == null && other.statusName != null) || (this.statusName != null && !this.statusName.equals(other.statusName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.ItemStatus[ statusName=" + statusName + " ]";
    }
    
}
