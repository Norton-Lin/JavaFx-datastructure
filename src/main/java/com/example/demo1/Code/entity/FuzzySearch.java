package com.example.demo1.Code.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class FuzzySearch {

    /**
     * 模糊查找名字与关键字匹配的事项并排序
     *
     * @param A      目标关键字
     * @param events 所有事项
     * @return 模糊查找后的排好序的所有事项
     */
    public <T extends Event> ArrayList<T> get_FS_result(String A, ArrayList<T> events) {

        ArrayList<T> FS_result = new ArrayList<>();//保存模糊查找结果

        try {

            //int end_index;
            int maxLength;//重复的最大子串长度

            String s;

            //遍历所有的事项
            for (T e : events) {

                s = e.getM_sName();
                //end_index = 0;
                maxLength = 0;
                int[][] dp = new int[s.length()][A.length()];//两个字符串匹配的矩阵

                //初始化dp矩阵
                for (int i = 0; i < s.length(); i++) {
                    for (int j = 0; j < A.length(); j++) {
                        dp[i][j] = 0;
                    }
                }
                //遍历dp矩阵求最大对角线长度
                for (int i = 0; i < s.length(); i++) {
                    for (int j = 0; j < A.length(); j++) {
                        if (s.charAt(i) == A.charAt(j)) {
                            //开始重复
                            if (i == 0 || j == 0) {
                                dp[i][j] = 1;
                            } else {
                                dp[i][j] = dp[i - 1][j - 1] + 1;
                            }

                            //更新最长对角线长度
                            if (dp[i][j] > maxLength) {
                                maxLength = dp[i][j];
                                //end_index = j;
                            }
                        }
                    }
                }
                //end_index += 1;

                e.setM_iWeight(maxLength);//保存查询结果的权重，为排序做准备

                if (maxLength > 0) {
                    FS_result.add(e);//将名字和目标字符串含有重复子串的事项保存起来
                }
                //System.out.println(s.substring(end_index - maxLength, end_index));

            }

        } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {

            System.out.println("查询出错，请重试...");
            System.exit(0);

        }

        if (FS_result.size() == 0) {
            return null;
        } else {
            return WeightSort(FS_result);//调用排序算法对模糊查询结果进行排序
        }

    }

    /**
     * 对模糊查找结果进行排序
     *
     * @param events 待排序课程
     */
    public <T extends Event> ArrayList<T> WeightSort(ArrayList<T> events) {

        //对课程按照排序权重进行排序
        events.sort(Comparator.comparing(T::getM_iWeight));

        Collections.reverse(events);//由于sort方法是升序排序，而要求为按权重降序排序

        return events;

    }

    //main方法仅作测试使用
    public static void main(String[] args) {

        ArrayList<Event> Events = new ArrayList<>();

        Event e1 = new Event();
        e1.setM_sName("计算机");
        Event e2 = new Event();
        e2.setM_sName("自动机");

        Events.add(e1);
        Events.add(e2);

        FuzzySearch search = new FuzzySearch();
        Scanner sc = new Scanner(System.in);

        String A = sc.next();

        System.out.println("查询结果为：");
        search.get_FS_result(A, Events);
    }

}
