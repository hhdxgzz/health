package com.itheima.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.pojo.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 会员业务逻辑处理服务
 */
@Service(interfaceClass = MemberService.class) //发布服务
@Transactional
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberDao memberDao;

    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    /**
     * 循环查询每个月之前会员总数
     * @param monthsList
     * @return
     */
    @Override
    public List<Integer> findMemberCountBeforeDate(List<String> monthsList) {
        List<Integer> countMembers = new ArrayList<>();
        if(monthsList!= null && monthsList.size()>0){
            for (String monthDate : monthsList) {
                Integer memberCountBeforeDate = memberDao.findMemberCountBeforeDate(monthDate + "-32");
                countMembers.add(memberCountBeforeDate);
            }
        }
        return countMembers;
    }
}
