package com.example.demo1.Code.entity;

public interface IGraph<E> {

    int getNumOfVertex();//��ȡ����ĸ���

    boolean insertVex(E v);//���붥��

    boolean deleteVex(E v);//ɾ������

    int indexOfVex(E v);//��λ�����λ��

    E valueOfVex(int v);// ��λָ��λ�õĶ���

    boolean insertEdge(int v1, int v2, int weight);//�����

    boolean deleteEdge(int v1, int v2);//ɾ����

    int getEdge(int v1, int v2);//���ұ�

    int[] dijkstra(int v);//����Դ�㵽���������·��

}
