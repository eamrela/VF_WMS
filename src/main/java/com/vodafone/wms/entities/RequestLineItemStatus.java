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
@Table(name = "request_line_item_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestLineItemStatus.findAll", query = "SELECT r FROM RequestLineItemStatus r")
    , @NamedQuery(name = "RequestLineItemStatus.findByStatusName", query = "SELECT r FROM RequestLineItemStatus r WHERE r.statusName = :statusName")
    , @NamedQuery(name = "RequestLineItemStatus.findByStatusDescription", query = "SELECT r FROM RequestLineItemStatus r WHERE r.statusDescription = :statusDescription")})
public class RequestLineItemStatus implements Serializable {

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
    @OneToMany(mappedBy = "lineItemStatus")
    private Collection<RequestLineItem> requestLineItemCollection;

    public RequestLineItemStatus() {
    }

    public RequestLineItemStatus(String statusName) {
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
        if (!(object instanceof RequestLineItemStatus)) {
            return false;
        }
        RequestLineItemStatus other = (RequestLineItemStatus) object;
        if ((this.statusName == null && other.statusName != null) || (this.statusName != null && !this.statusName.equals(other.statusName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.RequestLineItemStatus[ statusName=" + statusName + " ]";
    }
    
}
