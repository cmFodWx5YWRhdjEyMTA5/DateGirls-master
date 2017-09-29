package com.amsu.dategirls.util;

import com.amsu.dategirls.bean.LastMessage;

import java.util.Comparator;


/**
 * Created by Administrator on 2016/7/14.
 */
public class MessageComparator implements Comparator {
    @Override
    public int compare(Object lhs, Object rhs) {
        LastMessage lastMessage1 = (LastMessage) lhs;
        LastMessage lastMessage2 = (LastMessage) rhs;

        return lastMessage2.getLastTime().compareTo(lastMessage1.getLastTime());
    }
}
