package com.lsh.service;

import com.lsh.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author: LiuShihao
 * @Date: 2023/2/2 20:08
 * @Desc:
 */
@Service
public class HelloServiceImpl implements HelloService{

    @Autowired
    StringUtil stringUtil;

    @Override
    public String getString(String name) {
        return name+":"+ stringUtil.getUUIDString(10);
    }
}
