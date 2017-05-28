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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
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
@Table(name = "users")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findByUserEmail", query = "SELECT u FROM Users u WHERE u.userEmail = :userEmail")
    , @NamedQuery(name = "Users.findByFullName", query = "SELECT u FROM Users u WHERE u.fullName = :fullName")
    , @NamedQuery(name = "Users.findByPhoneNumber", query = "SELECT u FROM Users u WHERE u.phoneNumber = :phoneNumber")
    , @NamedQuery(name = "Users.findByUserPassword", query = "SELECT u FROM Users u WHERE u.userPassword = :userPassword")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "user_email")
    private String userEmail;
    @Size(max = 2147483647)
    @Column(name = "full_name")
    private String fullName;
    @Size(max = 2147483647)
    @Column(name = "phone_number")
    private String phoneNumber;
    @Size(max = 2147483647)
    @Column(name = "user_password")
    private String userPassword;
    @ManyToMany(mappedBy = "usersCollection")
    private Collection<Roles> rolesCollection;
    @JoinTable(name = "users_warehouse", joinColumns = {
        @JoinColumn(name = "user_id", referencedColumnName = "user_email")}, inverseJoinColumns = {
        @JoinColumn(name = "warehouse_id", referencedColumnName = "warehouse_name")})
    @ManyToMany
    private Collection<Warehouses> warehousesCollection;
    @OneToMany(mappedBy = "requester")
    private Collection<Requests> requestsCollection;
    @JoinColumn(name = "region", referencedColumnName = "region_name")
    @ManyToOne
    private Regions region;

    public Users() {
    }

    public Users(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @XmlTransient
    public Collection<Roles> getRolesCollection() {
        return rolesCollection;
    }

    public void setRolesCollection(Collection<Roles> rolesCollection) {
        this.rolesCollection = rolesCollection;
    }

    @XmlTransient
    public Collection<Warehouses> getWarehousesCollection() {
        return warehousesCollection;
    }

    public void setWarehousesCollection(Collection<Warehouses> warehousesCollection) {
        this.warehousesCollection = warehousesCollection;
    }

    @XmlTransient
    public Collection<Requests> getRequestsCollection() {
        return requestsCollection;
    }

    public void setRequestsCollection(Collection<Requests> requestsCollection) {
        this.requestsCollection = requestsCollection;
    }

    public Regions getRegion() {
        return region;
    }

    public void setRegion(Regions region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userEmail != null ? userEmail.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userEmail == null && other.userEmail != null) || (this.userEmail != null && !this.userEmail.equals(other.userEmail))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Users[ userEmail=" + userEmail + " ]";
    }
    
}
