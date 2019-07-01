package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.pojo.Setmeal;
import com.itheima.service.CheckGroupService;
import com.itheima.service.SetmealService;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.UUID;

/**
 * 套餐控制层管理
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    /**
     * 注入检查组服务接口
     */
    @Reference
    private SetmealService setmealService;

    //Jedis操作redis客户端对象
    @Autowired
    private JedisPool jedisPool;

    /**
     * 图片上传
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result upload(@RequestParam MultipartFile imgFile) {
        try {
            //1.文件名处理 xxxxx.jpg===>uuid.jpg
            String originalFilename = imgFile.getOriginalFilename();//获取原始的文件名
            int index = originalFilename.lastIndexOf(".");
            String suffix = originalFilename.substring(index);//后缀
            String newFileName = UUID.randomUUID().toString() + suffix;
            //System.out.println(newFileName);
            //2.调用七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),newFileName);

            //3.将上传七牛云后图片名称保存到set1
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,newFileName);

            return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,newFileName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    public static void main(String[] args) {
        String originalFilename = "11111.jpg";//获取原始的文件名
        int index = originalFilename.lastIndexOf(".");
        String suffix = originalFilename.substring(index);//后缀
        String newFileName = UUID.randomUUID().toString() + suffix;
        System.out.println(newFileName);
    }

    /**
     * 新增套餐方法
     * @return 返回结果 xxx
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Result add(Integer[] checkgroupIds,@RequestBody Setmeal setmeal) {
        try {
            setmealService.add(checkgroupIds,setmeal);

            //3.将套餐保存数据库后 将图片名称保存set2集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_SETMEAL_FAIL);
        }
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @RequestMapping(value = "/findPage", method = RequestMethod.POST)
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult pageResult = setmealService.pageQuery(queryPageBean.getCurrentPage(), queryPageBean.getPageSize(), queryPageBean.getQueryString());
        return pageResult;
    }

}
