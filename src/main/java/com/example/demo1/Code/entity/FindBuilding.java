package com.example.demo1.Code.entity;

import java.util.ArrayList;

public class FindBuilding {

    /**
     * ���ݽ�����Ŷ��ֲ��ҽ���
     *
     * @param con_number    ������
     * @param constructions ���н�����
     * @return ����
     */
    public int BinaryConstructionSearch(int con_number, ArrayList<Construction> constructions) {

        int low = 0;
        int high = constructions.size() - 1;

        while (low <= high) {

            int middle = (low + high) / 2;
            if (con_number == constructions.get(middle).get_con_number()) {
                return middle;
            } else if (con_number > constructions.get(middle).get_con_number()) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }

        }

        return constructions.size();

    }

}
