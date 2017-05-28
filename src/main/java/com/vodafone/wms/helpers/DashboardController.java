/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.helpers;

import com.vodafone.wms.controllers.RequestLineItemController;
import com.vodafone.wms.controllers.RequestsController;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.joda.time.DateTime;


/**
 *
 * @author eamrela
 */
@Named("dashboardController")
@SessionScoped
public class DashboardController implements Serializable{

    private Integer faultyRateAverage;
    private List<Object[]> topSites;
    private List<Object[]> topItems;
    private String faultyCountChart;
    
    @Inject
    private RequestLineItemController requestLineItemController;
    @Inject
    private RequestsController requestsController;

    public Integer getInBoundCount() {
        return requestsController.getInBoundItems().size();
    }

    public Integer getIssueCount() {
        return requestsController.getIssueItems().size();
    }

    

    public Integer getTransferCount() {
        return requestsController.getTransferItems().size();
    }

    public Integer getFaultyRateAverage() {
        return faultyRateAverage;
    }

    public List<Object[]> getTopItems() {
        return requestLineItemController.getTop5Items();
    }

    public void setTopItems(List<Object[]> topItems) {
        this.topItems = topItems;
    }

    public List<Object[]> getTopSites() {
        return requestLineItemController.getTop5Sites();
    }

    public void setTopSites(List<Object[]> topSites) {
        this.topSites = topSites;
    }

    public String getFaultyCountChart() {
        List<Object[]> faultyCount = requestLineItemController.getFaultyCount();
        String labels="";
        DateTime now = DateTime.now().plusDays(1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        TreeMap<String,String> series = new TreeMap<>();
        for (int i = 7; i > 0; i--) {
            if(i!=1){
            series.put(sdf.format(now.minusDays(i).toDate()), "");
            labels+="\""+sdf.format(now.minusDays(i).toDate())+"\",";
            }else{
            series.put(sdf.format(now.minusDays(i).toDate()), "");
            labels+="\""+sdf.format(now.minusDays(i).toDate())+"\"";
            }
        }
        String values="";
        for (int i = 0; i < faultyCount.size(); i++) {
           if(series.containsKey(sdf.format((Date)faultyCount.get(i)[0]))){
               series.put(sdf.format((Date)faultyCount.get(i)[0]) , faultyCount.get(i)[1]+"");
           }
        }
        for (Map.Entry<String, String> entry : series.entrySet()) {
           values+=entry.getValue()+",";
            
        }
        values=values.substring(0,values.length()-1);
        faultyCountChart = 
         "{"+
	"labels:["+labels+"],"+
	"series:[["+values+"]]"+
        "}";
        
        return faultyCountChart;
    }
    
    
}
