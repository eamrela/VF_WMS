/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vodafone.wms.helpers;

import com.vodafone.wms.controllers.WarehouseStockController;
import com.vodafone.wms.controllers.WarehousesController;
import com.vodafone.wms.entities.WarehouseStock;
import com.vodafone.wms.entities.Warehouses;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author eamrela
 */
@Named("exportManager")
@SessionScoped
public class ExportManager implements Serializable{
    
    private Map<String, CellStyle> styles = new TreeMap<>();
    @Inject
    private WarehouseStockController warehouseStockController;
    @Inject
    private WarehousesController warehousesController;
    
    
    
     public void exportSohByRegion(){
      
        String region = warehouseStockController.getFilterRegion()!=null?warehouseStockController.getFilterRegion():
                warehouseStockController.getUsersController().getLoggedInUser().getRegion().getRegionName();
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet(region+"_SOH");
        TreeMap<String,Integer> warehouseIndex = new TreeMap<>();
        TreeMap<String,WarehouseStock> uniqueItems = new TreeMap<>();
        TreeMap<String,TreeMap<String,WarehouseStock>> stockItems = new TreeMap<>();
        List<Warehouses> warehouses = warehousesController.getItemByRegion(region);
        List<WarehouseStock> items = warehouseStockController.getItemsByRegion();

        //<editor-fold defaultstate="collapsed" desc="Headers">
        int num = warehouses.size();
        initStyles(workbook,num);
        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("Material");
        row.getCell(0).setCellStyle(styles.get("default"));
        row.createCell(1).setCellValue("Part Number");
        row.getCell(1).setCellStyle(styles.get("default"));
        row.createCell(2).setCellValue("Description");
        row.getCell(2).setCellStyle(styles.get("default"));
        row.createCell(3).setCellValue("Category");
        row.getCell(3).setCellStyle(styles.get("default"));
        int i=4;
        for (Warehouses warehouse : warehouses) {
            warehouseIndex.put(warehouse.getWarehouseName(), i);
            row.createCell(i).setCellValue(warehouse.getWarehouseName()+" Good");
            row.getCell(i).setCellStyle(styles.get(""+num));
            i++;
            row.createCell(i).setCellValue(warehouse.getWarehouseName()+" Faulty");
            row.getCell(i).setCellStyle(styles.get(""+num));
            i++;
            row.createCell(i).setCellValue(warehouse.getWarehouseName()+" On-Call");
            row.getCell(i).setCellStyle(styles.get(""+num));
            i++;
            row.createCell(i).setCellValue(warehouse.getWarehouseName()+" Total");
            row.getCell(i).setCellStyle(styles.get(""+num));
            i++;
            num--;
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Splitting WH Items">

        for (WarehouseStock item : items) {
            uniqueItems.put(item.getItems().getPartNumber(), item);
            if(stockItems.containsKey(item.getWarehouses().getWarehouseName())){
                stockItems.get(item.getWarehouses().getWarehouseName()).put(item.getItems().getPartNumber(), item);
            }else{
                stockItems.put(item.getWarehouses().getWarehouseName(),new TreeMap<String, WarehouseStock>());
                stockItems.get(item.getWarehouses().getWarehouseName()).put(item.getItems().getPartNumber(), item);
            }
        }
//</editor-fold>
        //<editor-fold defaultstate="collapsed" desc="Writing Data">
        int whIndex = 0;
        int j=1;
        for (Map.Entry<String,WarehouseStock> entry : uniqueItems.entrySet()) {
            row = sheet.createRow(j);
            row.createCell(0).setCellValue(entry.getValue().getItems().getMaterialNumber());
            row.getCell(0).setCellStyle(styles.get("border"));
            row.createCell(1).setCellValue(entry.getValue().getItems().getPartNumber());
            row.getCell(1).setCellStyle(styles.get("border"));
            row.createCell(2).setCellValue(entry.getValue().getItems().getDescription());
            row.getCell(2).setCellStyle(styles.get("border"));
            row.createCell(3).setCellValue(entry.getValue().getItems().getCategory());
            row.getCell(3).setCellStyle(styles.get("border"));
            for (Map.Entry<String, Integer> entry1 : warehouseIndex.entrySet()) {
                whIndex = entry1.getValue();
                if(stockItems.containsKey(entry1.getKey())){
                    if(stockItems.get(entry1.getKey()).containsKey(entry.getValue().getItems().getPartNumber())){
                        
                row.createCell(whIndex).setCellValue(
                        stockItems.get(entry1.getKey()).get(entry.getValue().getItems().getPartNumber())
                                .getGoodStockQty().intValue());
                row.getCell(whIndex).setCellStyle(styles.get("border"));
                whIndex++;
                row.createCell(whIndex).setCellValue(
                        stockItems.get(entry1.getKey()).get(entry.getValue().getItems().getPartNumber())
                                .getFaultyStockQty().intValue());
                row.getCell(whIndex).setCellStyle(styles.get("border"));
                whIndex++;
                row.createCell(whIndex).setCellValue(
                        stockItems.get(entry1.getKey()).get(entry.getValue().getItems().getPartNumber())
                                .getFmStockQty().intValue());
                row.getCell(whIndex).setCellStyle(styles.get("border"));
                whIndex++;
                row.createCell(whIndex).setCellValue(
                        stockItems.get(entry1.getKey()).get(entry.getValue().getItems().getPartNumber())
                                .getTotalStockQty().intValue());
                row.getCell(whIndex).setCellStyle(styles.get("border"));
                    }
                }
            }

            j++;
        }
 //</editor-fold>       
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.setResponseContentType("application/vnd.ms-excel");
        externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\""+region+"_SOH.xlsx"+"\"");

        try {
            workbook.write(externalContext.getResponseOutputStream());
            externalContext.getResponseOutputStream().flush();
            externalContext.getResponseOutputStream().close();
        } catch (IOException ex) {
            Logger.getLogger(ExportManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void exportIssuedMaterialsByRegion(){
        
    }
    
    public void exportNFFByRegion(){
        
    }
    
    public void exportStolenMaterialsByRegion(){
        
    }
    
    private void initStyles(Workbook wb, int num){
        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 12);
        
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(headerFont);
        style.setWrapText(true);
        styles.put("default", style);
        
        style = createBorderedStyle(wb);
        styles.put("border",style);
        
        for (int i = 0; i < num; i++) {
        style = wb.createCellStyle();
        style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor((short) ThreadLocalRandom.current().nextInt(10, 30));
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setFont(headerFont);
        styles.put(""+(i+1), style);
        }

    }
    
    private static CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }
}
