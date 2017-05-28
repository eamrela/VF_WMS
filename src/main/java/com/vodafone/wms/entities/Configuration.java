/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author eamrela
 */
@Entity
@Table(name = "configuration")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Configuration.findAll", query = "SELECT c FROM Configuration c")
    , @NamedQuery(name = "Configuration.findByConfName", query = "SELECT c FROM Configuration c WHERE c.configurationPK.confName = :confName")
    , @NamedQuery(name = "Configuration.findByConfValue", query = "SELECT c FROM Configuration c WHERE c.confValue = :confValue")
    , @NamedQuery(name = "Configuration.findByConfEnviroment", query = "SELECT c FROM Configuration c WHERE c.configurationPK.confEnviroment = :confEnviroment")})
public class Configuration implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ConfigurationPK configurationPK;
    @Size(max = 2147483647)
    @Column(name = "conf_value")
    private String confValue;

    public Configuration() {
    }

    public Configuration(ConfigurationPK configurationPK) {
        this.configurationPK = configurationPK;
    }

    public Configuration(String confName, String confEnviroment) {
        this.configurationPK = new ConfigurationPK(confName, confEnviroment);
    }

    public ConfigurationPK getConfigurationPK() {
        return configurationPK;
    }

    public void setConfigurationPK(ConfigurationPK configurationPK) {
        this.configurationPK = configurationPK;
    }

    public String getConfValue() {
        return confValue;
    }

    public void setConfValue(String confValue) {
        this.confValue = confValue;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (configurationPK != null ? configurationPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Configuration)) {
            return false;
        }
        Configuration other = (Configuration) object;
        if ((this.configurationPK == null && other.configurationPK != null) || (this.configurationPK != null && !this.configurationPK.equals(other.configurationPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.vodafone.wms.entities.Configuration[ configurationPK=" + configurationPK + " ]";
    }
    
}
