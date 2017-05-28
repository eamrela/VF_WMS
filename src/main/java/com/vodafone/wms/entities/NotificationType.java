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
@Table(name = "notification_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NotificationType.findAll", query = "SELECT n FROM NotificationType n")
    , @NamedQuery(name = "NotificationType.findByNotificationName", query = "SELECT n FROM NotificationType n WHERE n.notificationName = :notificationName")
    , @NamedQuery(name = "NotificationType.findByNotificationDescription", query = "SELECT n FROM NotificationType n WHERE n.notificationDescription = :notificationDescription")})
public class NotificationType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "notification_name")
    private String notificationName;
    @Size(max = 2147483647)
    @Column(name = "notification_description")
    private String notificationDescription;
    @OneToMany(mappedBy = "notiType")
    private Collection<Notifications> notificationsCollection;

    public NotificationType() {
    }

    public NotificationType(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public String getNotificationDescription() {
        return notificationDescription;
    }

    public void setNotificationDescription(String notificationDescription) {
        this.notificationDescription = notificationDescription;
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
        hash += (notificationName != null ? notificationName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NotificationType)) {
            return false;
        }
        NotificationType other = (NotificationType) object;
        if ((this.notificationName == null && other.notificationName != null) || (this.notificationName != null && !this.notificationName.equals(other.notificationName))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.NotificationType[ notificationName=" + notificationName + " ]";
    }
    
}
