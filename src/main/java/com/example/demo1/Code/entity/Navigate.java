package com.example.demo1.Code.entity;

import com.example.demo1.Code.Mysql.ConstructionDatabase;
import com.example.demo1.Code.Mysql.MapDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

import static com.example.demo1.Code.systemtime.SystemTime.getCurrentTime;

public class Navigate {

    public static int[][] path;//路径矩阵
    public MapDatabase mapDatabase = new MapDatabase();
    public ConstructionDatabase constructionDatabase = new ConstructionDatabase();
    public int constructionNumber;//建筑数量
    public ArrayList<Double> Speed = new ArrayList<>();

    /**
     * 初始化交通工具速度
     */
    public void initSpeed() {

        Speed.add(1.0);//步行速度     traffic_type=0
        Speed.add(3.0);//自行车速度    traffic_type=1
        Speed.add(5.0);//电动车速度    traffic_type=2
        Speed.add(10.0);//汽车速度    traffic_type=3

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
        return constructionNumber + 1;
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
    public double[][] getMatrix(ArrayList<Line> line, int traffic_type, int navigate_type, int currentHour) {

        double[][] matrix = new double[getConstructionNumber()][getConstructionNumber()];

        //初始化邻接矩阵
        for (double[] doubles : matrix) {
            Arrays.fill(doubles, Double.MAX_VALUE);
        }

        int congestion_type;//道路拥挤度类型
        int road_degree = 1;//道路等级

        //时间和道路拥挤度的对应关系，三种时间段对应三个拥挤度
        if (currentHour <= 7 || currentHour >= 20) {
            congestion_type = 0;
        } else if (currentHour <= 9 || currentHour >= 12 && currentHour <= 13 || currentHour >= 18) {
            congestion_type = 1;
        } else {
            congestion_type = 2;
        }

        switch (traffic_type) {
            case 0 -> {
            }
            case 1, 2 -> road_degree = 2;//电动车和自行车共用道路
            case 3 -> road_degree = 3;
        }

        for (Line l : line) {

            double[] congestion = l.getM_d_congestion();//这里的功能不需要修改

            //只读入该交通工具对应的道路
            if (l.getDegree() >= road_degree) {

                switch (navigate_type) {
                    //最短路径算法
                    case 0 -> matrix[l.getM_i_stp()][l.getM_i_enp()] = l.getM_i_length();
                    //最短时间算法
                    case 1 -> matrix[l.getM_i_stp()][l.getM_i_enp()] = l.getM_i_length() / (1 - congestion[congestion_type]);
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

        //混合模式导航
        if (traffic_type == -1) {

            double[][] timeMatrix = getHybridMatrix(line, currentHour);
            floydAlgorithm(timeMatrix);

            if (timeMatrix[start_number][end_number] == Double.MAX_VALUE) {
                Result.append("无有效路径");
            } else {
                ArrayList<Construction> target_road = new ArrayList<>();//保存导航给出的道路

                double min_time = timeMatrix[start_number][end_number];//保存最短时间

                target_road.add(Start);//第一个元素保存起始点

                findPath(start_number, end_number, target_road, Constructions);//保存最短路径的中间结点

                target_road.add(End);//最后一个元素保存终止点

                showHybridWay(target_road, timeMatrix, Result, min_time);//打印路径
            }

        } else {

            //单一交通工具模式导航
            for (int navigate_type = 0; navigate_type < 2; navigate_type++) {

                ArrayList<Construction> target_road = new ArrayList<>();//保存导航给出的道路

                double[][] matrix = getMatrix(line, traffic_type, navigate_type, currentHour);//获得指定当时时间对应的地图

                floydAlgorithm(matrix);//弗洛伊德算法求最短路径

                if (matrix[start_number][end_number] == Double.MAX_VALUE) {
                    Result.append("无有效路径");
                } else {
                    double min_length = matrix[start_number][end_number];//保存最短路径长度

                    target_road.add(Start);//第一个元素保存起始点

                    findPath(start_number, end_number, target_road, Constructions);//保存最短路径的中间结点

                    target_road.add(End);//最后一个元素保存终止点

                    showWay(target_road, min_length, traffic_type, navigate_type, matrix, Result);//打印路径
                }
            }
        }

        return Result;
    }

    public void showHybridWay(ArrayList<Construction> target_road, double[][] matrix, StringBuilder result, double min_time) {

        ArrayList<Line> line = new ArrayList<>();
        mapDatabase.find(line);

        String[][] LoadName = getLoadName(line);

        result.append("\n从").append(target_road.get(0).get_con_name());
        result.append("到");
        result.append(target_road.get(target_road.size() - 1).get_con_name());
        result.append("的最短时间为：").append(min_time).append("秒");

        result.append(".路径导航为：\n");
        result.append("从").append(target_road.get(0).get_con_name()).append("出发，");

        double walkTime = 0.0;//导航所用的时间（单位：分钟）

        for (int i = 0; i < target_road.size() - 1; i++) {

            int currentHour = getCurrentTime().get(Calendar.HOUR_OF_DAY) + (int) walkTime / 60;//当前系统时间（小时数）
            int currentMinute = getCurrentTime().get(Calendar.MINUTE) + (int) (walkTime - 60 * (int) walkTime / 60);//当前系统时间（分钟）

            //这段路是跨校区的，考虑跨校区交通方式，认为公交车、班车速度等于校内汽车速度
            if (LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()].equals("跨校区道路")) {

                //在这几个时间段乘坐班车，此外的时间坐公交车
                if (currentHour == 6 || currentHour == 12 || currentHour == 18) {
                    result.append("乘坐学校班车");
                } else {
                    int waitTime;//等候时间
                    if (currentMinute > 30) {
                        waitTime = 60 - currentMinute;
                    } else {
                        waitTime = 30 - currentMinute;
                    }
                    result.append("等候").append(waitTime).append("分钟，坐公交车，");
                }
                result.append("走").append(matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]).append("秒，");
                result.append("到达").append(target_road.get(i + 1).get_con_name()).append("\n");
            } else {
                walkTime += matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()] / 60;
                result.append("沿").append(LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]);
                result.append("走").append(matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]).append("秒，");
                result.append("到达").append(target_road.get(i + 1).get_con_name()).append("\n");

            }

        }

        result.append("\n此次导航结束...\n");

    }


    public double[][] getHybridMatrix(ArrayList<Line> line, int currentHour) {

        double[][] timeMatrix = new double[getConstructionNumber()][getConstructionNumber()];

        //初始化邻接矩阵
        for (double[] doubles : timeMatrix) {
            Arrays.fill(doubles, Double.MAX_VALUE);
        }

        int congestion_type;//道路拥挤度类型

        //时间和道路拥挤度的对应关系，三种时间段对应三个拥挤度
        if (currentHour <= 7 || currentHour >= 20) {
            congestion_type = 0;
        } else if (currentHour <= 9 || currentHour >= 12 && currentHour <= 13 || currentHour >= 18) {
            congestion_type = 1;
        } else {
            congestion_type = 2;
        }

        for (Line l : line) {

            double[] congestion = l.getM_d_congestion();
            double len = l.getM_i_length() / (1 - congestion[congestion_type]);
            double speed = 0;
            switch (l.getDegree()) {
                case 1 -> speed = Speed.get(0);//道路等级为仅人行，最大速度为步行速度
                case 2 -> speed = Speed.get(2);//道路等级为人行+自行车+电动车，最大速度为电动车速度
                case 3 -> speed = Speed.get(3);//道路等级为支持所有交通工具，最大速度为汽车速度
            }
            timeMatrix[l.getM_i_stp()][l.getM_i_enp()] = len / speed;

        }

        return timeMatrix;
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

        switch (navigate_type) {
            case 0 -> {
                for (int i = 0; i < target_road.size() - 1; i++) {

                    result.append("沿").append(LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]);
                    result.append("走").append(matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]).append("米，");
                    result.append("到达").append(target_road.get(i + 1).get_con_name()).append("\n");

                }
            }
            case 1 -> {

                double walkTime = 0.0;//当前导航所用的时间（单位：分钟）

                for (int i = 0; i < target_road.size() - 1; i++) {

                    int currentHour = getCurrentTime().get(Calendar.HOUR_OF_DAY) + (int) walkTime / 60;//当前系统时间（小时数）
                    int currentMinute = getCurrentTime().get(Calendar.MINUTE) + (int) (walkTime - 60 * (int) walkTime / 60);//当前系统时间（分钟）

                    //这段路是跨校区的，考虑跨校区交通方式，认为公交车、班车是校内汽车速度的二倍
                    if (LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()].equals("跨校区道路")) {

                        //在这几个时间段乘坐班车，此外的时间坐公交车
                        if (currentHour == 6 || currentHour == 12 || currentHour == 18) {
                            result.append("乘坐学校班车");
                        } else {
                            int waitTime;//等候时间
                            if (currentMinute > 30) {
                                waitTime = 60 - currentMinute;
                            } else {
                                waitTime = 30 - currentMinute;
                            }
                            result.append("等候").append(waitTime).append("分钟，坐公交车，");
                        }
                        result.append("走").append(matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()] / (getSpeed(3))).append("秒，");
                        result.append("到达").append(target_road.get(i + 1).get_con_name()).append("\n");
                    } else {

                        //更新导航所用的时间（单位：分钟）
                        walkTime += (matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()] / (getSpeed(traffic_type)) * 60);

                        result.append("沿").append(LoadName[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()]);
                        result.append("走").append(matrix[target_road.get(i).get_con_number()][target_road.get(i + 1).get_con_number()] / getSpeed(traffic_type)).append("秒，");
                        result.append("到达").append(target_road.get(i + 1).get_con_name()).append("\n");

                    }
                }
            }
        }


        //result.add(target_road.get(target_road.size()-1).get_con_name());
        result.append("\n此次导航结束...\n");

        //return result;
    }

}