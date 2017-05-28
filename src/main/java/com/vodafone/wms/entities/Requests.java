/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "requests")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Requests.findAll", query = "SELECT r FROM Requests r")
    , @NamedQuery(name = "Requests.findByReqId", query = "SELECT r FROM Requests r WHERE r.reqId = :reqId")
    , @NamedQuery(name = "Requests.findBySiteId", query = "SELECT r FROM Requests r WHERE r.siteId = :siteId")
    , @NamedQuery(name = "Requests.findByFaultDescription", query = "SELECT r FROM Requests r WHERE r.faultDescription = :faultDescription")
    , @NamedQuery(name = "Requests.findByReqComment", query = "SELECT r FROM Requests r WHERE r.reqComment = :reqComment")
    , @NamedQuery(name = "Requests.findByReqTime", query = "SELECT r FROM Requests r WHERE r.reqTime = :reqTime")
    , @NamedQuery(name = "Requests.findByOfficeLog", query = "SELECT r FROM Requests r WHERE r.officeLog = :officeLog")
    , @NamedQuery(name = "Requests.findByTtId", query = "SELECT r FROM Requests r WHERE r.ttId = :ttId")})
public class Requests implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "req_id")
    private Long reqId;
    @Size(max = 2147483647)
    @Column(name = "site_id")
    private String siteId;
    @Size(max = 2147483647)
    @Column(name = "fault_description")
    private String faultDescription;
    @Size(max = 2147483647)
    @Column(name = "req_comment")
    private String reqComment;
    @Column(name = "req_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reqTime;
    @Size(max = 2147483647)
    @Column(name = "office_log")
    private String officeLog;
    @Size(max = 2147483647)
    @Column(name = "tt_id")
    private String ttId;
    @JoinColumn(name = "engineer", referencedColumnName = "engineer_id")
    @ManyToOne
    private Engineers engineer;
    @JoinColumn(name = "req_type", referencedColumnName = "req_type_name")
    @ManyToOne
    private RequestType reqType;
    @JoinColumn(name = "requester", referencedColumnName = "user_email")
    @ManyToOne
    private Users requester;
    @JoinColumn(name = "from_warehouse", referencedColumnName = "warehouse_name")
    @ManyToOne
    private Warehouses fromWarehouse;
    @JoinColumn(name = "assignment_group", referencedColumnName = "warehouse_name")
    @ManyToOne
    private Warehouses assignmentGroup;
    @JoinColumn(name = "to_warehouse", referencedColumnName = "warehouse_name")
    @ManyToOne
    private Warehouses toWarehouse;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "reqId")
    private Collection<RequestLineItem> requestLineItemCollection;

    public Requests() {
    }

    public Requests(Long reqId) {
        this.reqId = reqId;
    }

    public Long getReqId() {
        return reqId;
    }

    public void setReqId(Long reqId) {
        this.reqId = reqId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public void setFaultDescription(String faultDescription) {
        this.faultDescription = faultDescription;
    }

    public String getReqComment() {
        return reqComment;
    }

    public void setReqComment(String reqComment) {
        this.reqComment = reqComment;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public String getOfficeLog() {
        return officeLog;
    }

    public void setOfficeLog(String officeLog) {
        this.officeLog = officeLog;
    }

    public String getTtId() {
        return ttId;
    }

    public void setTtId(String ttId) {
        this.ttId = ttId;
    }

    public Engineers getEngineer() {
        return engineer;
    }

    public void setEngineer(Engineers engineer) {
        this.engineer = engineer;
    }

    public RequestType getReqType() {
        return reqType;
    }

    public void setReqType(RequestType reqType) {
        this.reqType = reqType;
    }

    public Users getRequester() {
        return requester;
    }

    public void setRequester(Users requester) {
        this.requester = requester;
    }

    public Warehouses getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(Warehouses fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public Warehouses getAssignmentGroup() {
        return assignmentGroup;
    }

    public void setAssignmentGroup(Warehouses assignmentGroup) {
        this.assignmentGroup = assignmentGroup;
    }

    public Warehouses getToWarehouse() {
        return toWarehouse;
    }

    public void setToWarehouse(Warehouses toWarehouse) {
        this.toWarehouse = toWarehouse;
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
        hash += (reqId != null ? reqId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Requests)) {
            return false;
        }
        Requests other = (Requests) object;
        if ((this.reqId == null && other.reqId != null) || (this.reqId != null && !this.reqId.equals(other.reqId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Requests[ reqId=" + reqId + " ]";
    }
    
}
