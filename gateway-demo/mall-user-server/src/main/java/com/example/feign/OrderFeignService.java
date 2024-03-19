package com.example.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Fox
 *
 *
 */

@FeignClient(value = "mall-order",path = "/order")
public interface OrderFeignService {

    /**
     *  1. openfeign传递restful参数
     *  方法要求：
     *    返回值： 要对应
     *    方法名：随意
     *    参数： 要对应的注解
     *  方法上添加springmvc
     * @param userId
     * @return
     */
    @RequestMapping("/findOrderByUserId/{userIdsxxx}")
    Object findOrderByUserIdxxxx(@PathVariable("userIdsxxx") Integer userId);



}

