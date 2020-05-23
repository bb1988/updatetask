package com.tytzy.network.utils;

/**
 * 作者: 白勃
 * 时间: 2020/5/22 10:53 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 网络库配置文件，预设默认值
 * @param
 */
public class NetConstant {

    //默认地址
    public static String BASE_URL = "http://192.168.10.248:8116";
    //超时时间 单位秒 预设20s
    public static int TIMEOUT = 20;

    //缓存
    //缓存文件地址
    public static String CACHE = "cache_bb_default";
    //缓存大小 单位：M 预设值 10m
    public static int CACHE_SIZE = 10;
    //在线缓存时间 单位秒 预设值 60s
    public static int ONLINE_TIME = 60;
    //离线缓存时间 单位秒 预设值 1day
    public static int OFFLINE_TIME = 60 * 60 * 24;

    //连接池
    //最大数量  预设 20
    public static int CONNECTION_POOL_SIZE = 20;
    //保持时间 单位秒
    public static int POOL_KEEP_LIVE = 15;

    //并发相关
    //最大连接数
    public static int MAX_REQUESTS = 30;
    //单host最大连接数
    public static int MAX_REQUESTS_PER_HOST = 15;

}
