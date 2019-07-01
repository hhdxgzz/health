package com.itheima.dao;

import com.itheima.pojo.CheckGroup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.Map;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext-dao.xml")
public class DaoTest {
    @Autowired
    private CheckGroupDao checkGroupDao;
    @Test
    public void test(){
        /*CheckGroup checkGroup = new CheckGroup();
        checkGroup.setSex("1");
        checkGroupDao.add(checkGroup);*/
       /* Map<String,Integer> map = new HashMap();
        map.put("checkitemId",29);
        map.put("groupid",2);
        checkGroupDao.setCheckGroupAndCheckItem(map);*/

        /*CheckGroup checkGroup = checkGroupDao.findById(5);
        System.out.println(checkGroup);*/

       /* Integer[] checkItemIdsByCheckGroupId = checkGroupDao.findCheckItemIdsByCheckGroupId(5);
        for (Integer integer : checkItemIdsByCheckGroupId) {
            System.out.println(integer);
        }*/
       /*CheckGroup checkGroup = new CheckGroup();
       checkGroup.setSex("1"); //1:男 0:女 2:不限
       checkGroup.setId(5);
       checkGroupDao.edit(checkGroup);*/

       //checkGroupDao.deleteAssociation(6);
    }
}
