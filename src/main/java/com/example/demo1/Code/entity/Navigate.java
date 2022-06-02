package com.example.demo1.Code.entity;

import com.example.demo1.Code.Mysql.ConstructionDatabase;
import com.example.demo1.Code.Mysql.MapDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Navigate {

    public static int[][] path;//路径矩阵
    public MapDatabase mapDatabase = new MapDatabase();
    public ConstructionDatabase constructionDatabase = new ConstructionDatabase();
    public int constructionNumber;//建筑数量
    public ArrayList<double> Speed;

    /**
     * 初始化交通工具速度
     */
    public void initSpeed() {

        Speed.add(1);//步行速度     traffic_type=0
        Speed.add(3);//自行车速度    traffic_type=1
        Speed.add(5);//电动车速度    traffic_type=2
        Speed.add(10);//汽车速度    traffic_type=3

    }

    public double getSpeed(int i) {
        return Speed.get(i);
    }

    /**
     * 保存所有建筑物数量
     *
     * @param constructionNumber 数量
     */
    public void setConstructionNumber(int constructionNumber) {
        this.constructionNumber = constructionNumber;
    }

    /**
     * 获取所有建筑物数量
     *
     * @return 数量
     */
    public int getConstructionNumber() {
        return constructionNumber;
    }

    /**
     * 获得给定交通工具对应的地图
     * navigate_type=0:最短路径导航
     * navigate_type=1:最短时间导航
     *
     * @param line          保存所有道路类
     * @param navigate_type 导航类型
     * @return 该交通工具的地图
     */
    public double[][] getMatrix(ArrayList<Line> line, int navigate_type, int currentHour) {

        double[][] matrix = new double[getConstructionNumber()][getConstructionNumber()];

        //初始化邻接矩阵
        for (double[] doubles : matrix) {
            Arrays.fill(doubles, 10000.0);
        }
        int road_type;

        //时间和道路拥挤度的对应关系，三个时间段对应三个拥挤度
        if (currentHour <= 7 || currentHour >= 20) {
            road_type = 1;
        } else if (currentHour <= 9 || currentHour >= 12 && currentHour <= 13 || currentHour >= 18) {
            road_type = 2;
        } else {
            road_type = 3;
        }
        /*switch (traffic_type) {
            case 1, 2 -> road_type = 1;//电动车和自行车共用道路
            case 3 -> road_type = 2;
            default -> road_type = 0;
        }*/

        for (Line l : line) {

            double[] congestion = l.getM_d_congestion();//这里的功能需要修改

            if (l.getDegree() == road_type) {
                switch (navigate_type) {
                    //最短路径算法
                    case 0 -> matrix[l.getM_i_stp()][l.getM_i_enp()] = l.getM_i_length();
                    //最短时间算法
                    case 1 -> matrix[l.getM_i_stp()][l.getM_i_enp()] = l.getM_i_length() / (1 - congestion[road_type]);
                }

            }

        }

        return matrix;
    }

    /**
     * 获得所有道路的名字
     *
     * @param line 保存所有道路类
     * @return 保存所有道路名字的数组
     */
    public String[][] getLoadName(ArrayList<Line> line) {

        String[][] LoadName = new String[getConstructionNumber()][getConstructionNumber()];
        for (Line l : line) {
            LoadName[l.getM_i_stp()][l.getM_i_enp()] = l.getM_s_name();
        }
        return LoadName;
    }

    /**
     * 求最短路径
     *
     * @param traffic_type 交通工具类型
     * @param start_name   起点建筑物名字
     * @param end_name     终点建筑物名字
     * @return Result 导航结果
     */
    public StringBuilder toNavigate(int traffic_type, String start_name, String end_name, int currentHour) {

        ArrayList<Line> line = new ArrayList<>();
        ArrayList<Construction> Constructions = new ArrayList<>();
        //ArrayList<Construction> target_road = new ArrayList<>();//保存导航给出的道路

        initSpeed();

        StringBuilder Result = new StringBuilder();

        Construction Start = getConstruction(start_name);
        Construction End = getConstruction(end_name);

        mapDatabase.find(line);//从地图数据库获取所有道路类
        constructionDatabase.find(Constructions);//从建筑数据库获得所有建筑类
        setConstructionNumber(Constructions.size());

        int start_number, end_number;

        //得到起止建筑在地图上的编号
        try {

            start_number = Start.get_con_number();
            end_number = End.get_con_number();

        } catch (NullPointerException e) {

            Result.append("建筑物不存在，请重试...");
            return Result;
        }

        for (int navigate_type = 0; navigate_type < 2; navigate_type++) {

            ArrayList<Construction> target_road = new ArrayList<>();//保存导航给出的道路

            double[][] matrix = getMatrix(line, navigate_type, currentHour);//获得指定当时时间对应的地图

            floydAlgorithm(matrix);//弗洛伊德算法求最短路径

            double min_length = matrix[start_number][end_number];//保存最短路径长度

            target_road.add(Start);//第一个元素保存起始点

            findPath(start_number, end_number, target_road, Constructions);//保存最短路径的中间结点

            target_road.add(End);//最后一个元素保存终止点

            showWay(target_road, min_length, traffic_type, navigate_type, matrix, Result);//打印路径
        }
        return Result;
    }

    /**
     * 弗洛伊德算法求最短路径
     *
     * @param matrix 地图
     */
    public void floydAlgorithm(double[][] matrix) {

        path = new int[matrix.length][matrix.length];//路径数组

        //初始化路径数组
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                path[i][j] = -1;
            }
        }

        //更新计算路径数组
        for (int m = 0; m < matrix.length; m++) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix.length; j++) {
                    if (matrix[i][m] + matrix[m][j] < matrix[i][j]) {
                        matrix[i][j] = matrix[i][m] + matrix[m][j];
                        //记录经由哪个点到达
                        path[i][j] = m;
                    }
                }
            }
        }
    }

    /**
     * 寻求两个结点之间的最短路径
     *
     * @param i             当前结点编号
     * @param j             下一结点编号
     * @param target_road   保存最短路径经过的建筑物
     * @param Constructions 保存所有的建筑物
     */
    public void findPath(int i, int j, ArrayList<Construction> target_road, ArrayList<Construction> Constructions) {

        if (i != j) {
            int m = path[i][j];

            if (m == -1) {
                return;
            }

            findPath(i, m, target_road, Constructions);

            Construction Transit = getNameBS(m, Constructions);//找到最短路径的中间结点对应的建筑物

            target_road.add(Transit);//保存中间结点对应的建筑物结点

            findPath(m, j, target_road, Constructions);
        }

    }

    /**
     * 通过建筑物的结点编号二分查找该建筑的名字
     *
     * @param m             目标结点编号
     * @param Constructions 保存所有建筑物
     * @return 目标建筑物
     */
    public Construction getNameBS(int m, ArrayList<Construction> Constructions) {

        int left = 0;
        int right = Constructions.size() - 1;
        int mid;

        while (left <= right) {

            mid = (left + right) / 2;

            if (Constructions.get(mid).get_con_number() < m) {
                left = mid + 1;
            } else if (Constructions.get(mid).get_con_number() > m) {
                right = mid - 1;
            } else {
                return Constructions.get(mid);
            }
        }
        return null;
    }

    /**
     * 通过建筑物的名字查找该建筑物
     *
     * @param name 目标建筑物的名字
     * @return 目标建筑物
     */
    public Construction getConstruction(String name) {

        ArrayList<Construction> constructions = new ArrayList<>();
        constructionDatabase.find(constructions);

        for (Construction c : constructions) {
            if (Objects.equals(c.get_con_name(), name)) {
                return c;
            }
        }
        return null;
    }

    /**
     * 打印导航结果
     *
     * @param target_road   导航路径
     * @param min_length    最短路径长度
     * @param traffic_type  指定的交通工具
     * @param navigate_type 指定的导航类型
     */
    public void showWay(ArrayList<Construction> target_road, double min_length, int traffic_type, int navigate_type, double[][] matrix, StringBuilder result) {

        ArrayList<Line> line = new ArrayList<>();
        mapDatabase.find(line);

        String[][] LoadName = getLoadName(line);

        result.append("\n从").append(target_road.get(0).get_con_name());
        result.append("到");
        result.append(target_road.get(target_road.size() - 1).get_con_name());

        switch (navigate_type) {

            case 0 -> result.append("的最短路径长度是：").append(min_length).append("米");

            case 1 -> {

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(1);//输入小数点后一位
                double t = min_length / Speed.get(traffic_type);
                result.append("的最短时间是：").append(nf.format(t)).append("秒");

            }
        }

        result.append(".路径导航为：\n");
        result.append("从").append(target_road.get(0).get_con_name()).append("出发，");

        switch (navigate_type){
            case 0 ->{
                for (int i = 0; i < target_road.size() - 1; i++) {

                    result.append("沿").append(LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]);
                    result.append("走").append(matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]).append("米，");
                    result.append("到达").append(target_road.get(i + 1).get_con_name()).append("\n");

                }
            }
            case 1 ->{
                for (int i = 0; i < target_road.size() - 1; i++) {

                    result.append("沿").append(LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]);
                    result.append("走").append(matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]/getSpeed(traffic_type)).append("秒，");
                    result.append("到达").append(target_road.get(i + 1).get_con_name()).append("\n");

                }
            }
        }



        //result.add(target_road.get(target_road.size()-1).get_con_name());
        result.append("\n此次导航结束...\n");

        //return result;
    }

}