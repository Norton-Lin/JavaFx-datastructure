package com.example.demo1.Code.entity;

/*校区类*/
public class Campus {

    private Construction construction;//校区的建筑物
    private Line line;//校区的道路

    Campus(Construction construction,Line line){
        this.construction=construction;
        this.line=line;
    }

    /**
     * 返回该校区的建筑物
     * @return construction
     */
    private Construction get_campus_con(){
        return construction;
    }

    /**
     * 返回该校区的道路
     * @return road
     */
    private Line get_campus_road(){
        return line;
    }

    /**
     * 设置该校区的建筑物
     * @param construction 建筑
     */
    private void set_campus_con(Construction construction){
        this.construction=construction;
    }

    /**
     * 设置该校区的道路
     * @param line 道路
     */
    private void set_campus_road(Line line){
        this.line=line;
    }
}
