package com.itheima.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * POI入门案例
 */
public class POITest {

    /**
     * 方式一：读取Excel
     * 1.获取Excel对象
     * 2.获取sheet标签页
     * 3.循环sheet标签页 遍历每一行记录
     * 4.循环行 遍历每一个单元格数据输出
     *
     * xlsx：2007 ==XSSF
     * xls:2003  == HSSF
     */
    //@Test
    public void testRead1() throws IOException {
        //读取C:\working\read.xlsx
        //1.获取Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\working\\read.xlsx");
        //2.获取sheet标签页
        //XSSFSheet sheet1 = xssfWorkbook.getSheet("Sheet1");
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //3.循环sheet标签页 遍历每一行记录
        for (Row row : sheetAt) {
            //row 每一行对象
            //4.循环行 遍历每一个单元格数据输出
            for (Cell cell : row) {
                //cell:单元格
                System.out.println(cell.getStringCellValue());
            }
            System.out.println("*******************************************");
        }
        //4.释放资源
        xssfWorkbook.close();
    }


    /**
     * 方式二：读取Excel
     *   通过行号来循环获取
     */
   // @Test
    public void testRead2() throws IOException {
        //1.获取Excel对象
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("C:\\working\\read.xlsx");
        //2.获取sheet标签页
        //XSSFSheet sheet1 = xssfWorkbook.getSheet("Sheet1");
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);

        int lastRowNum = sheetAt.getLastRowNum();   //行号
        for(int i = 0;i<=lastRowNum;i++){
            //通过行号循环获取每一行记录
            XSSFRow row = sheetAt.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for(int j = 0;j<lastCellNum;j++){
                //循环遍历每一行 每一列单元格数据
                System.out.println(row.getCell(j).getStringCellValue());
            }
            System.out.println("*************************************");
        }
        //释放资源
        xssfWorkbook.close();
    }

    /**
     * 创建excel文件
     * 编号   姓名  年龄
     * 001    老王  18
     *
     * 1.创建excel对象（内存中）
     * 2.创建sheet标签页
     * 3.创建行
     * 4.创建每一行的每一个单元格
     */
    //@Test
    public void testCreate() throws IOException {
        //创建excel对象（内存中）
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        XSSFSheet sheet = xssfWorkbook.createSheet();//创建标签页
        //创建标题行
        XSSFRow titleRow = sheet.createRow(0);//第一行
        titleRow.createCell(0).setCellValue("编号");//第一行第一列
        titleRow.createCell(1).setCellValue("姓名");
        titleRow.createCell(2).setCellValue("年龄");

        //创建数据行
        XSSFRow contentRow = sheet.createRow(1);//第一行
        contentRow.createCell(0).setCellValue("001");//第一行第一列
        contentRow.createCell(1).setCellValue("老王");
        contentRow.createCell(2).setCellValue("18");

        //通过输出流写入磁盘生成excel文件
        OutputStream out = new FileOutputStream("C:\\working\\lao.xlsx");
        xssfWorkbook.write(out);
        out.flush();//写完之后把数据清空
        out.close();
        xssfWorkbook.close();//关闭
    }
}
