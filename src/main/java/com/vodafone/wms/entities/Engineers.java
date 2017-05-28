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
@Table(name = "engineers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Engineers.findAll", query = "SELECT e FROM Engineers e")
    , @NamedQuery(name = "Engineers.findByEngineerName", query = "SELECT e FROM Engineers e WHERE e.engineerName = :engineerName")
    , @NamedQuery(name = "Engineers.findByEngineerPhone", query = "SELECT e FROM Engineers e WHERE e.engineerPhone = :engineerPhone")
    , @NamedQuery(name = "Engineers.findByEngineerId", query = "SELECT e FROM Engineers e WHERE e.engineerId = :engineerId")})
public class Engineers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 2147483647)
    @Column(name = "engineer_name")
    private String engineerName;
    @Size(max = 2147483647)
    @Column(name = "engineer_phone")
    private String engineerPhone;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "engineer_id")
    private String engineerId;
    @OneToMany(mappedBy = "engineer")
    private Collection<Requests> requestsCollection;

    public Engineers() {
    }

    public Engineers(String engineerId) {
        this.engineerId = engineerId;
    }

    public String getEngineerName() {
        return engineerName;
    }

    public void setEngineerName(String engineerName) {
        this.engineerName = engineerName;
    }

    public String getEngineerPhone() {
        return engineerPhone;
    }

    public void setEngineerPhone(String engineerPhone) {
        this.engineerPhone = engineerPhone;
    }

    public String getEngineerId() {
        return engineerId;
    }

    public void setEngineerId(String engineerId) {
        this.engineerId = engineerId;
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
        hash += (engineerId != null ? engineerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Engineers)) {
            return false;
        }
        Engineers other = (Engineers) object;
        if ((this.engineerId == null && other.engineerId != null) || (this.engineerId != null && !this.engineerId.equals(other.engineerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Engineers[ engineerId=" + engineerId + " ]";
    }
    
}
