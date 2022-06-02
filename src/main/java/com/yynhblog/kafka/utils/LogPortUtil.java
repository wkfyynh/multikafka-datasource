package com.yynhblog.kafka.utils;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @description: 传递log端口
 * @author: yynh
 * @create: 2021-11-05
 **/
public class LogPortUtil {
    public static String[] args;
    public static String port = "8083";

    public static void dealPort(String[] _args) {
        args = _args;
        if (args != null && args.length > 0) {
            Optional<String> first = Arrays.asList(args).stream()
                    .filter(arg -> StringUtils.contains(arg, "--server.port"))
                    .findFirst();
            if (first.isPresent()) {
                port = first.get().substring(first.get().indexOf("=") + 1, first.get().length());

            }
        }
    }

}
