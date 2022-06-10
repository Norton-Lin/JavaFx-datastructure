package com.example.demo1.Code.entity.account;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.AccountDatabase;
import com.example.demo1.Code.Mysql.ActivityDatabase;
import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.entity.Activity;
import com.example.demo1.Code.entity.Course;
import com.example.demo1.Code.entity.Search;

import java.util.ArrayList;

public class TeacherAccount extends Account{
    private Authority m_eAuthority = Authority.Teacher; //权限等级
    private ArrayList<Course> m_CaCourse = new ArrayList<Course>(); //所授课程
    private ArrayList<Activity> m_CaActivity = new ArrayList<Activity>(); // 指导活动

    /**
     * 构造函数
     * @param Id 账户Id
     * @param password 账户密码
     */
    public TeacherAccount(String Id,String password){
        super(Id,password);
    }
    public TeacherAccount(Account account) {
        super(account.getID(), account.getPassword());
        AccountDatabase accountDatabase = new AccountDatabase();
        accountDatabase.findTeaAccount(this);
        sortCourse();//对课程进行排序
        sortActivity();//对活动进行排序
    }

    /**
     * 获取所授课程 (课表查询）
     * @return 课程名单
     */
    public ArrayList<Course> getCourse(){
        return this.m_CaCourse;
    }

    /**
     * 指定课程查询
     * @param id 指定课程id
     * @return 返回该课程在ArrayList中的位置
     */
    public int getCourseById(int id) {
        Search search = new Search();
        LogFile.info("Student"+getID()," 查询课程"+id);
        return search.BinaryCourseSearch(id,m_CaCourse);
    }

    /**
     * 获取指导活动
     * @return 活动名单
     */
    public ArrayList<Activity> getActivity(){
        return this.m_CaActivity;
    }

    /**
     * 指定活动查询
     * @param id 指定活动id
     * @return 返回该活动在ArrayList中的位置
     */
    public int getActivityById(int id) {
        Search search = new Search();
        LogFile.info("Student"+getID()," 查询活动"+id);
        return search.BinaryCourseSearch(id,m_CaActivity);
    }

    /**
     * 对课程列表进行排序
     */
    public void sortCourse(){
        Search search = new Search();
        search.QuickSort(this.m_CaCourse,0,this.m_CaCourse.size()-1);//快速排序
    }

    /**
     * 对活动列表进行排序
     */
    public void sortActivity(){
        Search search = new Search();
        search.QuickSort(this.m_CaActivity,0,this.m_CaActivity.size()-1);//快速排序
    }

    /**
     * 向已授课程列表中添加课程
     * @param course 待添加课程
     */
    public void addCourse(Course course){
        this.m_CaCourse.add(course);
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.insert(course,this);
        LogFile.info("Teacher "+getID(),"添加课程");
    }

    /**
     * 从已授课程列表中删除课程
     * @param course 待删除课程
     */
    public void decCourse(Course course){
        this.m_CaCourse.remove(course);
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.delete(course,this);
        LogFile.info("Teacher "+getID(),"删除课程");
    }

    /**
     * 向已指导活动列表中添加活动
     * @param activity 待添加活动
     */
    public void addActivity(Activity activity){
        this.m_CaActivity.add(activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
        activityDatabase.insert(activity,this.getID(),0);
        LogFile.info("Teacher "+getID(),"添加活动");
    }

    /**
     * 从已指导活动列表中删除活动
     * @param activity 待删除活动
     */
    public void decActivity(Activity activity){
        this.m_CaActivity.remove(activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
        activityDatabase.delete(activity,this.getID(), 0);
        LogFile.info("Teacher "+getID(),"删除活动");
    }
    public void teaching(int id)
    {
        this.m_CaCourse.get(id).setM_iCurrentClass(this.m_CaCourse.get(id).getM_iCurrentClass()+1);
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.update(this.m_CaCourse.get(id));
        LogFile.info("Teacher "+getID(),this.m_CaCourse.get(id).getM_sName()+" 课程进度加1");
    }

}
