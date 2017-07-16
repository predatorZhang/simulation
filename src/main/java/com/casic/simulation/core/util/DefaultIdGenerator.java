package com.casic.simulation.core.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by lenovo on 2016/11/18.
 */
public class DefaultIdGenerator {

    private String time;
    private String prefix;
    private String split;
    private AtomicInteger value;
    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyyMMddHHmmss");

    public DefaultIdGenerator() {
        this("default", "-", 1);
    }

    public DefaultIdGenerator(String prefix, String split, int initial){
        this.time = DefaultIdGenerator.FORMATTER.format(new Date());
        this.prefix = prefix;
        this.split = split;
        this.value = new AtomicInteger(initial);
    }

    public String next() {
        StringBuffer sb = new StringBuffer(prefix)
                .append(split).append(time).append(split)
                .append(value.getAndIncrement());
        return sb.toString();
    }
}
