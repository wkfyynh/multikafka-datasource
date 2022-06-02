package com.yynhblog.kafka.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.yynhblog.kafka.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @description:
 * @author: yynh
 * @create: 2022-05-11
 **/
@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    @Transactional
    @DS("one")
    public Map test() {
        return testMapper.test();
    }
}
