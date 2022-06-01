package com.example.demo1.Code.entity;

import java.util.ArrayList;

/*建筑类*/
public class Construction {

    private String con_name;//建筑物名称
    private ArrayList<ArrayList<Integer>> con_room = new ArrayList<>();//建筑物的房间信息：楼层数+房间编号
    private int con_number;//建筑物在地图上的编号
    public int getCampus() {
        return campus;
    }

    public void setCampus(int campus) {
        this.campus = campus;
    }

    private int campus ;//校区

    public Construction() {
        super();
    }



    /**
     * 返回建筑物的名称
     *
     * @return con_name
     */
    public String get_con_name() {
        return this.con_name;
    }

    /**
     * 返回建筑物的房间的信息
     *
     * @return con_room
     */
    public ArrayList<ArrayList<Integer>> get_con_room() {
        return this.con_room;
    }

    /**
     * 返回建筑物在地图上的编号
     *
     * @return con_number
     */
    public int get_con_number() {
        return this.con_number;
    }

    /**
     * 设置建筑物的名称
     *
     * @param name;
     */
    public void set_con_name(String name) {
        this.con_name = name;
    }

    /**
     * 设置建筑物的房间的信息
     *
     * @param room 房间
     */
    public void set_con_room(ArrayList<ArrayList<Integer>> room) {
        this.con_room = room;
    }
    public void set_con_room(int floor, int[] num){

        for(int i =0;i<floor;i++){
            ArrayList<Integer> temp = new ArrayList<>();
            for(int j =0;j<num[i];j++){
                temp.add((i+1)*100+j+1);
            }
            this.con_room.add(temp);
        }
    }

    /**
     * 设置建筑物在地图上编号
     *
     * @param number 编号
     */
    public void set_con_number(int number) {
        this.con_number = number;
    }
    public void scanPlace()
    {}

    @Override
    public String toString() {
        String Result;
        Result = "校区：" + this.campus + "\n" + "教学楼：" + this.con_name;
        return Result;
    }
}