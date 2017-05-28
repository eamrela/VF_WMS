/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "sites")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sites.findAll", query = "SELECT s FROM Sites s")
    , @NamedQuery(name = "Sites.findBySiteId", query = "SELECT s FROM Sites s WHERE s.siteId = :siteId")})
public class Sites implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "site_id")
    private String siteId;
    @JoinColumn(name = "site_region", referencedColumnName = "region_name")
    @ManyToOne
    private Regions siteRegion;

    public Sites() {
    }

    public Sites(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public Regions getSiteRegion() {
        return siteRegion;
    }

    public void setSiteRegion(Regions siteRegion) {
        this.siteRegion = siteRegion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siteId != null ? siteId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Sites)) {
            return false;
        }
        Sites other = (Sites) object;
        if ((this.siteId == null && other.siteId != null) || (this.siteId != null && !this.siteId.equals(other.siteId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Sites[ siteId=" + siteId + " ]";
    }
    
}
