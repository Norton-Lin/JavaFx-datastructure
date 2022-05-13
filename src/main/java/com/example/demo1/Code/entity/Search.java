package com.example.demo1.Code.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Search {
    /**
     * 以事项编号为基准进行精确查找，采用二分查找算法
     * @param m_iNum 事项编号
     * @param events 具体事项
     */
    public  <T extends Event>int BinaryCourseSearch(int m_iNum, ArrayList<T> events) {
        int low = 0;
        int high = events.size() - 1;
        while (low <= high) {
            int middle = (low + high) >> 1;
            if (m_iNum == events.get(middle).getM_iNum()) {
                return middle;
            }
                if (m_iNum > events.get(middle).getM_iNum()) {
                low = middle + 1;
            }
            if (m_iNum < events.get(middle).getM_iNum()) {
                high = middle - 1;
            }
        }
        return events.size();
    }

    public <T extends Event> void QuickSort(ArrayList<T> events, int low, int high) {
        int i, j;
        T index;
        if (low > high)
            return;
        i = low;
        j = high;
        index = events.get(i);
        while (i != j) {
            while (i < j && events.get(j).getM_iNum() >= index.getM_iNum())
                j--;
            if (i < j) {
                //Collections.swap(events,i,j);
                events.set(i, events.get(j));
                i++;
            }
            while (i < j && events.get(i).getM_iNum() <= index.getM_iNum())
                i++;
            if (i < j) {
               // Collections.swap(events,i,j);
                events.set(j, events.get(i));
                j--;
            }
        }
        //Collections.swap(events,i,low);
        events.set(i, index);
        QuickSort(events, low, i-1);
        QuickSort(events, i + 1, high);
    }


}
