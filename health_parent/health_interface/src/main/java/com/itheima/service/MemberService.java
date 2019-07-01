package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

/**
 * 会员接口服务
 */
public interface MemberService {
    /**
     * 根据手机号码查询会员信息
     * @param telephone
     * @return
     */
    public Member findByTelephone(String telephone);

    /**
     * 注册
     * @param member
     */
    public void add(Member member);

    /**
     * 循环查询每个月之前会员总数
     * @param monthsList
     * @return
     */
    List<Integer> findMemberCountBeforeDate(List<String> monthsList);
}
