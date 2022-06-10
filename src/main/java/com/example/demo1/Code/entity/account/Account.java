package com.example.demo1.Code.entity.account;

import com.example.demo1.Code.Util.Authority;

/**
 * 账户类
 * 含用户账户相关数据与方法
 */
public class Account {
    private String m_sID; //账号码
    private String m_sPassword; //密码
    private Authority m_eAuthority ; //权限等级
    private String m_iClassID = "";//所在班级
    public Account(String Id,String password) {
        this.m_sID=Id;
        this.m_sPassword=password;
    }

    public Account() {}

    /**
     * 获取账号
     * @return 账号
     */
    public String getID(){
        return this.m_sID;
    }

    /**
     * 获取密码
     * @return 密码
     */
    public String getPassword(){
        return this.m_sPassword;
    }

    /**
     * 获取账号权限等级
     * @return 权限等级
     */
    public Authority getAuthority(){
        return this.m_eAuthority;
    }

    /**
     * 修改账号密码
     * @param password 新密码
     */
    public void setPassword(String password) {
        this.m_sPassword=password;
    }

    /**
     * 修改ID
     * @param m_sID 新ID
     */
    public void setM_sID(String m_sID) {
        this.m_sID = m_sID;
    }

    /**
     * 修改权限
     * @param m_eAuthority 新权限
     */
    public void setM_eAuthority(Authority m_eAuthority) {
        this.m_eAuthority = m_eAuthority;
    }

    public void setM_eAuthority(int num){
        switch (num) {
            case 0 -> this.m_eAuthority = Authority.Student;
            case 1 -> this.m_eAuthority = Authority.Teacher;
            case 2 -> this.m_eAuthority = Authority.Manager;
        }

    }

    /**
     * 读取班级信息
     * @return 班级编号
     */
    public String getClassID() {
        return m_iClassID;
    }

    /**
     * 修改班级信息
     * @param m_iClassID 新班级
     */
    public void setClassID(String m_iClassID) {
        this.m_iClassID = m_iClassID;
    }
}
