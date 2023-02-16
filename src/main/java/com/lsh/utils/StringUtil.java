package com.lsh.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @Author: LiuShihao
 * @Date: 2023/2/2 20:32
 * @Desc:
 */
@Component
public class StringUtil {

    public String getUUIDString(int index){
        return UUID.randomUUID().toString().substring(0,index+1);
    }
}
