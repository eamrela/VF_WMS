/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "notifications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notifications.findAll", query = "SELECT n FROM Notifications n")
    , @NamedQuery(name = "Notifications.findByNotiId", query = "SELECT n FROM Notifications n WHERE n.notiId = :notiId")
    , @NamedQuery(name = "Notifications.findByNotiTime", query = "SELECT n FROM Notifications n WHERE n.notiTime = :notiTime")
    , @NamedQuery(name = "Notifications.findByNotiDescription", query = "SELECT n FROM Notifications n WHERE n.notiDescription = :notiDescription")})
public class Notifications implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "noti_id")
    private Long notiId;
    @Column(name = "noti_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date notiTime;
    @Size(max = 2147483647)
    @Column(name = "noti_description")
    private String notiDescription;
    @JoinColumn(name = "noti_type", referencedColumnName = "notification_name")
    @ManyToOne
    private NotificationType notiType;
    @JoinColumn(name = "noti_region", referencedColumnName = "region_name")
    @ManyToOne
    private Regions notiRegion;
    @JoinColumn(name = "noti_warehouse", referencedColumnName = "warehouse_name")
    @ManyToOne
    private Warehouses notiWarehouse;

    public Notifications() {
    }

    public Notifications(Long notiId) {
        this.notiId = notiId;
    }

    public Long getNotiId() {
        return notiId;
    }

    public void setNotiId(Long notiId) {
        this.notiId = notiId;
    }

    public Date getNotiTime() {
        return notiTime;
    }

    public void setNotiTime(Date notiTime) {
        this.notiTime = notiTime;
    }

    public String getNotiDescription() {
        return notiDescription;
    }

    public void setNotiDescription(String notiDescription) {
        this.notiDescription = notiDescription;
    }

    public NotificationType getNotiType() {
        return notiType;
    }

    public void setNotiType(NotificationType notiType) {
        this.notiType = notiType;
    }

    public Regions getNotiRegion() {
        return notiRegion;
    }

    public void setNotiRegion(Regions notiRegion) {
        this.notiRegion = notiRegion;
    }

    public Warehouses getNotiWarehouse() {
        return notiWarehouse;
    }

    public void setNotiWarehouse(Warehouses notiWarehouse) {
        this.notiWarehouse = notiWarehouse;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (notiId != null ? notiId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.notiId == null && other.notiId != null) || (this.notiId != null && !this.notiId.equals(other.notiId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Notifications[ notiId=" + notiId + " ]";
    }
    
}
