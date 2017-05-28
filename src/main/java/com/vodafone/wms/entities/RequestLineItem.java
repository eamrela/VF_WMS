/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import java.math.BigInteger;
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
@Table(name = "request_line_item")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "RequestLineItem.findAll", query = "SELECT r FROM RequestLineItem r")
    , @NamedQuery(name = "RequestLineItem.findByLineItemId", query = "SELECT r FROM RequestLineItem r WHERE r.lineItemId = :lineItemId")
    , @NamedQuery(name = "RequestLineItem.findByVendor", query = "SELECT r FROM RequestLineItem r WHERE r.vendor = :vendor")
    , @NamedQuery(name = "RequestLineItem.findByLineItemComment", query = "SELECT r FROM RequestLineItem r WHERE r.lineItemComment = :lineItemComment")
    , @NamedQuery(name = "RequestLineItem.findByQty", query = "SELECT r FROM RequestLineItem r WHERE r.qty = :qty")
    , @NamedQuery(name = "RequestLineItem.findByCartonAccepted", query = "SELECT r FROM RequestLineItem r WHERE r.cartonAccepted = :cartonAccepted")
    , @NamedQuery(name = "RequestLineItem.findByAntistaticAccepted", query = "SELECT r FROM RequestLineItem r WHERE r.antistaticAccepted = :antistaticAccepted")
    , @NamedQuery(name = "RequestLineItem.findByFoamAccepted", query = "SELECT r FROM RequestLineItem r WHERE r.foamAccepted = :foamAccepted")
    , @NamedQuery(name = "RequestLineItem.findBySerialNumber", query = "SELECT r FROM RequestLineItem r WHERE r.serialNumber = :serialNumber")
    , @NamedQuery(name = "RequestLineItem.findByReturnableSerial", query = "SELECT r FROM RequestLineItem r WHERE r.returnableSerial = :returnableSerial")
    , @NamedQuery(name = "RequestLineItem.findByStolen", query = "SELECT r FROM RequestLineItem r WHERE r.stolen = :stolen")
    , @NamedQuery(name = "RequestLineItem.findByReturnableQty", query = "SELECT r FROM RequestLineItem r WHERE r.returnableQty = :returnableQty")})
public class RequestLineItem implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "line_item_id")
    private Long lineItemId;
    @Size(max = 2147483647)
    @Column(name = "vendor")
    private String vendor;
    @Size(max = 2147483647)
    @Column(name = "line_item_comment")
    private String lineItemComment;
    @Column(name = "qty")
    private BigInteger qty;
    @Column(name = "carton_accepted")
    private Boolean cartonAccepted;
    @Column(name = "antistatic_accepted")
    private Boolean antistaticAccepted;
    @Column(name = "foam_accepted")
    private Boolean foamAccepted;
    @Size(max = 2147483647)
    @Column(name = "serial_number")
    private String serialNumber;
    @Size(max = 2147483647)
    @Column(name = "returnable_serial")
    private String returnableSerial;
    @Column(name = "stolen")
    private Boolean stolen;
    @Column(name = "returnable_qty")
    private BigInteger returnableQty;
    @JoinColumn(name = "returnable_status", referencedColumnName = "status_name")
    @ManyToOne
    private ItemStatus returnableStatus;
    @JoinColumn(name = "item_id", referencedColumnName = "part_number")
    @ManyToOne
    private Items itemId;
    @JoinColumn(name = "returnabe_item_id", referencedColumnName = "part_number")
    @ManyToOne
    private Items returnabeItemId;
    @JoinColumn(name = "line_item_status", referencedColumnName = "status_name")
    @ManyToOne
    private RequestLineItemStatus lineItemStatus;
    @JoinColumn(name = "req_id", referencedColumnName = "req_id")
    @ManyToOne(optional = false)
    private Requests reqId;
    @Column(name = "faulty_batch_date")
    @Temporal(TemporalType.DATE)
    private Date faultyBatchDate;

    public RequestLineItem() {
    }

    public RequestLineItem(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Long getLineItemId() {
        return lineItemId;
    }

    public Date getFaultyBatchDate() {
        return faultyBatchDate;
    }

    public void setFaultyBatchDate(Date faultyBatchDate) {
        this.faultyBatchDate = faultyBatchDate;
    }

    
    public void setLineItemId(Long lineItemId) {
        this.lineItemId = lineItemId;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getLineItemComment() {
        return lineItemComment;
    }

    public void setLineItemComment(String lineItemComment) {
        this.lineItemComment = lineItemComment;
    }

    public BigInteger getQty() {
        return qty;
    }

    public void setQty(BigInteger qty) {
        this.qty = qty;
    }

    public Boolean getCartonAccepted() {
        return cartonAccepted;
    }

    public void setCartonAccepted(Boolean cartonAccepted) {
        this.cartonAccepted = cartonAccepted;
    }

    public Boolean getAntistaticAccepted() {
        return antistaticAccepted;
    }

    public void setAntistaticAccepted(Boolean antistaticAccepted) {
        this.antistaticAccepted = antistaticAccepted;
    }

    public Boolean getFoamAccepted() {
        return foamAccepted;
    }

    public void setFoamAccepted(Boolean foamAccepted) {
        this.foamAccepted = foamAccepted;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getReturnableSerial() {
        return returnableSerial;
    }

    public void setReturnableSerial(String returnableSerial) {
        this.returnableSerial = returnableSerial;
    }

    public Boolean getStolen() {
        return stolen;
    }

    public void setStolen(Boolean stolen) {
        this.stolen = stolen;
    }

    public BigInteger getReturnableQty() {
        return returnableQty;
    }

    public void setReturnableQty(BigInteger returnableQty) {
        this.returnableQty = returnableQty;
    }

    public ItemStatus getReturnableStatus() {
        return returnableStatus;
    }

    public void setReturnableStatus(ItemStatus returnableStatus) {
        this.returnableStatus = returnableStatus;
    }

    public Items getItemId() {
        return itemId;
    }

    public void setItemId(Items itemId) {
        this.itemId = itemId;
    }

    public Items getReturnabeItemId() {
        return returnabeItemId;
    }

    public void setReturnabeItemId(Items returnabeItemId) {
        this.returnabeItemId = returnabeItemId;
    }

    public RequestLineItemStatus getLineItemStatus() {
        return lineItemStatus;
    }

    public void setLineItemStatus(RequestLineItemStatus lineItemStatus) {
        this.lineItemStatus = lineItemStatus;
    }

    public Requests getReqId() {
        return reqId;
    }

    public void setReqId(Requests reqId) {
        this.reqId = reqId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lineItemId != null ? lineItemId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestLineItem)) {
            return false;
        }
        RequestLineItem other = (RequestLineItem) object;
        if ((this.lineItemId == null && other.lineItemId != null) || (this.lineItemId != null && !this.lineItemId.equals(other.lineItemId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.RequestLineItem[ lineItemId=" + lineItemId + " ]";
    }
    
}
