package com.yynhblog.kafka.utils;

import com.yynhblog.kafka.entity.ResultEntity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * @description: 统一返回工具类
 * @author: yynh
 * @create: 2020-07-27
 **/
public class ResultUtil {
    public static ResultEntity success(Object object) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setResult("0");
        resultEntity.setData(object);
        resultEntity.setSerial_no(UUID.randomUUID().toString());
        return resultEntity;
    }

    public static ResultEntity success() {
        return success("");
    }

    public static ResultEntity error(String msg) {
        ResultEntity resultEntity = new ResultEntity();
        resultEntity.setResult("1");
        resultEntity.setMsg(msg);
        resultEntity.setSerial_no(UUID.randomUUID().toString());
        resultEntity.setData(new ArrayList<>());
        return resultEntity;
    }

}
