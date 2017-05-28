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
@Table(name = "request_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestType.findAll", query = "SELECT r FROM RequestType r")
    , @NamedQuery(name = "RequestType.findByReqTypeName", query = "SELECT r FROM RequestType r WHERE r.reqTypeName = :reqTypeName")
    , @NamedQuery(name = "RequestType.findByReqTypeDescription", query = "SELECT r FROM RequestType r WHERE r.reqTypeDescription = :reqTypeDescription")})
public class RequestType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "req_type_name")
    private String reqTypeName;
    @Size(max = 2147483647)
    @Column(name = "req_type_description")
    private String reqTypeDescription;
    @OneToMany(mappedBy = "reqType")
    private Collection<Requests> requestsCollection;

    public RequestType() {
    }

    public RequestType(String reqTypeName) {
        this.reqTypeName = reqTypeName;
    }

    public String getReqTypeName() {
        return reqTypeName;
    }

    public void setReqTypeName(String reqTypeName) {
        this.reqTypeName = reqTypeName;
    }

    public String getReqTypeDescription() {
        return reqTypeDescription;
    }

    public void setReqTypeDescription(String reqTypeDescription) {
        this.reqTypeDescription = reqTypeDescription;
    }

    @XmlTransient
    public Collection<Requests> getRequestsCollection() {
        return requestsCollection;
    }

    public void setRequestsCollection(Collection<Requests> requestsCollection) {
        this.requestsCollection = requestsCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (reqTypeName != null ? reqTypeName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestType)) {
            return false;
        }
        RequestType other = (RequestType) object;
        if ((this.reqTypeName == null && other.reqTypeName != null) || (this.reqTypeName != null && !this.reqTypeName.equals(other.reqTypeName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.RequestType[ reqTypeName=" + reqTypeName + " ]";
    }
    
}
