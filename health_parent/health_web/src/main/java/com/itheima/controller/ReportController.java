package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import com.itheima.utils.DateUtils;
import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报表控制层管理
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    //运营统计报表处理业务服务
    @Reference
    private ReportService reportService;

    /**
     * 获取每个月会员增长数量 折线图数据图标展示
     * @return
     */
    @RequestMapping(value = "/getMemberReport", method = RequestMethod.GET)
    public Result getMemberReport() {

        //1.往前12个月时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);  //先减12个月

        //定一个list集合返回页面需要年月集合
        List<String> monthsList = new ArrayList<>();
        //通过循环的方式 +1 得到下个月年月 2018-01 2018-02
        for(int i = 1;i<=12;i++){
            calendar.add(Calendar.MONTH,1);
            monthsList.add(new SimpleDateFormat("yyyy-MM").format(calendar.getTime()));
        }
        //2.每个月之前会员总数  select count(*) from t_member where regTime < '2018-09-31'

        List<Integer> countMembers = memberService.findMemberCountBeforeDate(monthsList);

        //定一个map封装页面需要的数据
        Map<String,Object> map = new HashMap<>();
        map.put("months",monthsList);
        map.put("memberCount",countMembers);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,map);
    }

    /**
     * 套餐预约占比饼形图
     */
    @RequestMapping(value = "/getSetmealReport", method = RequestMethod.GET)
    public Result getSetmealReport() {
        //1.获取每个套餐预约总数 List<Map>
        List<Map> setmealCount =setmealService.findSetmealCount();


        List<String> setmealNames = new ArrayList<>();
        //2.获取套餐名称 List<String>
        if(setmealCount != null && setmealCount.size()>0){
            for (Map map : setmealCount) {
                setmealNames.add((String)map.get("name"));
            }
        }
        //返回Map返回结果
        Map<String,Object> map =new HashMap<>();
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",setmealCount);
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }

    /**
     * 运营数据统计
     */
    @RequestMapping(value = "/getBusinessReportData", method = RequestMethod.GET)
    public Result getBusinessReportData() {
        //调用服务 得到结果返回页面即可
        Map<String,Object> result = reportService.getBusinessReportData();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,result);
    }

    /**
     * 导出运营数据报表 exportBusinessReport
     * @throws Exception
     */
   /* @RequestMapping(value = "/exportBusinessReport", method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        //1.获取导出报表的数据
        Map<String,Object> result = reportService.getBusinessReportData();
        //2.创建Excele对象 ，将数据写入
        String filePath = request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //获取第一个sheet
            XSSFSheet sheetAt = workbook.getSheetAt(0);
            //获取第三行 下标是2
            XSSFRow row = sheetAt.getRow(2);
            //获取第六列 下标是5
            row.getCell(5).setCellValue((String)result.get("reportDate"));
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");

            row = sheetAt.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheetAt.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheetAt.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日到诊数

            row = sheetAt.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周到诊数

            row = sheetAt.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月到诊数

            ///中间省略。。。
            //循环热门套餐
            int rowNum = 12;
            List<Map> hotSetmeal =(List<Map>)result.get("hotSetmeal");
            //校验
            if(hotSetmeal!=null && hotSetmeal.size()>0){
                for (Map map : hotSetmeal) {
                    String name =(String)map.get("name");//套餐名称
                    Integer  setmeal_count =  Integer.parseInt(map.get("setmeal_count").toString()); //预约总数
                    BigDecimal proportion =(BigDecimal)map.get("proportion");//预约占比
                    String remark =(String)map.get("remark");//预约占比
                    XSSFRow hotRow = sheetAt.getRow(rowNum);//从13行开始循环
                    hotRow.getCell(4).setCellValue(name);
                    hotRow.getCell(5).setCellValue(setmeal_count);
                    hotRow.getCell(6).setCellValue(proportion.doubleValue());
                    hotRow.getCell(7).setCellValue(remark);
                    rowNum++;
                }
            }
            //输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //写入response之前设置文件类型 和 文件名
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();//清空
            outputStream.close();//关闭输出流
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }*/

    @RequestMapping(value = "/exportBusinessReport", method = RequestMethod.GET)
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        //1.获取导出报表的数据
        Map<String,Object> beans = reportService.getBusinessReportData();
        //2.创建Excele对象 ，将数据写入
        String filePath = request.getSession().getServletContext().getRealPath("template")+File.separator+"report_template.xlsx";
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(filePath)));
            //获取第一个sheet
            XSSFSheet sheetAt = workbook.getSheetAt(0);

            //获取第三行 下标是2
            XSSFRow row = sheetAt.getRow(2);
            //提供两个参数 第一个参数模板  第二参数模板对应的数据
            //xls模板方法  将map对象解析设置到excel中
            XLSTransformer transformer = new XLSTransformer();
            transformer.transformWorkbook(workbook, beans);
            //输出流
            ServletOutputStream outputStream = response.getOutputStream();
            //写入response之前设置文件类型 和 文件名
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();//清空
            outputStream.close();//关闭输出流
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }


    public static void main(String[] args) throws Exception {

        /*Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        System.out.println(calendar.getTime());*/

        /*String reportDate = DateUtils.parseDate2String(new Date(), "yyyy-MM-dd");

        System.out.println(reportDate);*/


        System.out.println(DateUtils.parseDate2String(DateUtils.getThisWeekMonday(new Date())));
    }

}
