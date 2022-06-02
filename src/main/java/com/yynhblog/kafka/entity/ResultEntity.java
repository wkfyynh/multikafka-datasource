package com.yynhblog.kafka.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @description: 统一返回实体类
 * @author: yynh
 * @create: 2021-07-23
 **/
@Data
public class ResultEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    //流水号
    private String serial_no;

    //返回结果 0 正常 1 异常
    private String result;

    // 错误信息
    private String msg = "";

    /**
     * 具体的内容.
     */
    private T data;
}
