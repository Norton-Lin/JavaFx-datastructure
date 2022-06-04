package com.example.demo1.Code.entity;

import com.example.demo1.Code.Mysql.ConstructionDatabase;
import com.example.demo1.Code.Mysql.MapDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Objects;

public class Navigate {

    public static int[][] path;//路径矩阵
    public MapDatabase mapDatabase = new MapDatabase();
    public ConstructionDatabase constructionDatabase = new ConstructionDatabase();
<<<<<<< HEAD
    public int constructionNumber;//建筑数量
    public ArrayList<Double> Speed;
=======
>>>>>>> 61729f7a11ed97f3c18aafabd14161235b8af2ea

    public void setSpeed(ArrayList<Integer> Speed) {

        Speed.add(1.0);//步行速度     traffic_type=0
        Speed.add(3.0);//自行车速度    traffic_type=1
        Speed.add(5.0);//电动车速度    traffic_type=2
        Speed.add(10.0);//汽车速度    traffic_type=3

    }

    /**
     * 获得给定交通工具对应的地图
     * navigate_type=0:最短路径导航
     * navigate_type=1:最短时间导航
     *
     * @param traffic_type  交通工具类型
     * @param line          保存所有道路类
     * @param navigate_type 导航类型
     * @return 该交通工具的地图
     */
    public double[][] getMatrix(int traffic_type, ArrayList<Line> line, int navigate_type) {

        double[][] matrix = new double[line.size()][line.size()];
        int road_type;

        //建立交通工具类型和道路类型的对应关系，汽车有四种，道路有三种
        switch (traffic_type) {
            case 1, 2 -> road_type = 1;//电动车和自行车共用道路
            case 3 -> road_type = 2;
            default -> road_type = 0;
        }

        for (Line l : line) {

            double[] congestion = l.getM_d_congestion();

            if (l.getDegree() == road_type) {
                switch (navigate_type) {
                    //最短路径算法
                    case 0 -> matrix[l.getM_i_stp()][l.getM_i_enp()] = l.getM_i_length();
                    //最短时间算法
                    case 1 -> matrix[l.getM_i_stp()][l.getM_i_enp()] = l.getM_i_length() * congestion[road_type];
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

        String[][] LoadName = new String[line.size()][line.size()];
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
    public ArrayList<String> toNavigate(int traffic_type, String start_name, String end_name) {

        ArrayList<Line> line = new ArrayList<>();
        ArrayList<Construction> Constructions = new ArrayList<>();
        ArrayList<Construction> target_road = new ArrayList<>();//保存导航给出的道路

        ArrayList<String> Result = new ArrayList<>();

        Construction Start = getConstruction(start_name);
        Construction End = getConstruction(end_name);

        mapDatabase.find(line);//从地图数据库获取所有道路类
        constructionDatabase.find(Constructions);//从建筑数据库获得所有建筑类

        int start_number = 0, end_number = 0;

        //得到起止建筑在地图上的编号
        try {

            start_number = Start.get_con_number();
            end_number = End.get_con_number();

        } catch (NullPointerException e) {

            Result.add("建筑物不存在，请重试...");
            System.exit(0);

        }

        for (int navigate_type = 0; navigate_type < 2; navigate_type++) {

            double[][] matrix = getMatrix(traffic_type, line, navigate_type);//获得指定交通工具对应的地图

            floydAlgorithm(matrix);//弗洛伊德算法求最短路径

            double min_length = matrix[start_number][end_number];//保存最短路径长度

            target_road.add(Start);//第一个元素保存起始点

            findPath(start_number, end_number, target_road, Constructions);//保存最短路径的中间结点

            target_road.add(End);//最后一个元素保存终止点

            Result = showWay(target_road, min_length, traffic_type, navigate_type, matrix);//打印路径
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

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                path[i][j] = -1;
            }
        }

        //初计算路径数组
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

        int m = path[i][j];

        if (m == -1) {
            return;
        }

        findPath(i, m, target_road, Constructions);

        Construction Transit = getNameBS(m, Constructions);//找到最短路径的中间结点对应的建筑物

        target_road.add(Transit);//保存中间结点对应的建筑物结点

        findPath(m, j, target_road, Constructions);
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
     * 通过建筑物的名字查找该建筑物的名字
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
    public ArrayList<String> showWay(ArrayList<Construction> target_road, double min_length, int traffic_type, int navigate_type, double[][] matrix) {

        ArrayList<Integer> Speed = new ArrayList<>();//交通工具的速度
        ArrayList<Line> line = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();

        setSpeed(Speed);//设置四种交通工具的速度
        mapDatabase.find(line);

        String[][] LoadName = getLoadName(line);

        result.add("\n从" + target_road.get(0).get_con_name());
        result.add("到");
        result.add(target_road.get(target_road.size() - 1).get_con_name());

        switch (navigate_type) {

            case 0 -> result.add("的最短路径长度是：" + min_length);

            case 1 -> {

                NumberFormat nf = NumberFormat.getNumberInstance();
                nf.setMaximumFractionDigits(1);//输入小数点后一位
                double t = min_length / Speed.get(traffic_type);
                result.add("的最短时间是：" + nf.format(t));

            }
        }

        result.add("路径导航为：");
        result.add("从" + target_road.get(0).get_con_name() + "出发");

        for (int i = 0; i < target_road.size() - 2; i++) {

            result.add("沿" + LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]);
            result.add("走" + matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()] + "米");
            result.add("到达" + target_road.get(i + 1).get_con_name());

        }

        result.add("此次导航结束...\n");

        return result;
    }

}