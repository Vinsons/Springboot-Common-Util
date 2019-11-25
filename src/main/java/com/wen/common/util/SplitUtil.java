package com.wen.common.util;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @ClassName SplitUtil
 * @Description
 * @Author 黄文聪
 * @Date 2019-11-25 13:42
 * @Version 1.0
 **/
public class SplitUtil {

    /**
     * list分组
     * @param list 原始数据
     * @param groupSize 每组要有多少数据
     * @return
     */
    private List<List<String>> splitList(List<String> list, int groupSize) {
        List<List<String>> newList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)){
            int length = list.size();
            // 计算可以分成多少组
            int num = (length + groupSize - 1) / groupSize;
            newList = new ArrayList<>(num);
            for (int i = 0; i < num; i++) {
                // 开始位置
                int fromIndex = i * groupSize;
                // 结束位置
                int toIndex = (i + 1) * groupSize < length ? (i + 1) * groupSize : length;
                newList.add(list.subList(fromIndex, toIndex));
            }
        }
        return newList;
    }
}
