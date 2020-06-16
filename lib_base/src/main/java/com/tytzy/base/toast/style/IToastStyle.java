package com.tytzy.base.toast.style;

/**
 * 作者: 白勃
 * 时间: 2020/6/16 9:39 AM
 * 版权: Copyright © 2020 BB Inc. All Rights Reserved
 * 描述: 默认样式接口
 */
public interface IToastStyle {


    /**
     * 吐司的重心
     */
    int getGravity();

    /**
     * X轴偏移
     */
    int getXOffset();

    /**
     * Y轴偏移
     */
    int getYOffset();

    /**
     * 吐司 Z 轴坐标
     */
    int getZ();

    /**
     * 圆角大小
     */
    int getCornerRadius();

    /**
     * 背景颜色
     */
    int getBackgroundColor();

    /**
     * 文本颜色
     */
    int getTextColor();

    /**
     * 文本大小
     */
    float getTextSize();

    /**
     * 最大行数
     */
    int getMaxLines();

    /**
     * 开始内边距
     */
    int getPaddingStart();

    /**
     * 顶部内边距
     */
    int getPaddingTop();

    /**
     * 结束内边距
     */
    int getPaddingEnd();

    /**
     * 底部内边距
     */
    int getPaddingBottom();
}