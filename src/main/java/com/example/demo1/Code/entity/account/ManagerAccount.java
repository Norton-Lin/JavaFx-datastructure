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

public class ManagerAccount extends Account{
    private Authority m_eAuthority = Authority.Manager; //权限等级
    private ArrayList<Course> m_CaCourse = new ArrayList<Course>(); // 所有课程
    private ArrayList<Activity> m_CaActivity = new ArrayList<Activity>(); // 所有活动

    public ManagerAccount(String Id, String password) {
        super(Id,password);
        CourseDatabase courseDatabase = new CourseDatabase();
        ActivityDatabase activityDatabase = new ActivityDatabase();
        courseDatabase.find(this.m_CaCourse);//从数据库内读取所有课程信息
        activityDatabase.find(this.m_CaActivity);//从数据库内读取所有活动信息
    }

    public ManagerAccount(Account account) {
        super(account.getID(), account.getPassword());
        AccountDatabase accountDatabase = new AccountDatabase();
        accountDatabase.findManAccount(this);
       // CourseDatabase courseDatabase = new CourseDatabase();
      //  ActivityDatabase activityDatabase = new ActivityDatabase();
     //   courseDatabase.find(this.m_CaCourse);//从数据库内读取所有课程信息
      //  activityDatabase.find(this.m_CaActivity);//从数据库内读取所有活动信息
        sortCourse();//对课程进行排序
        sortActivity();//对活动进行排序
    }

    /**
     * 获取所有课程
     * @return 所有课程信息
     */
    public ArrayList<Course> getCourse() {
        return m_CaCourse;
    }

    /**
     * 获取所有活动
     * @return 所有活动信息
     */
    public ArrayList<Activity> getActivity() {
        return m_CaActivity;
    }

    /**
     * 指定课程查询
     * @param id 指定课程id
     * @return 返回该课程在ArrayList中的位置
     */
    public int getCourseById(int id) {
        Search search = new Search();
        LogFile.info("Manager"+getID()," 查询课程"+id);
        return search.BinaryCourseSearch(id,m_CaCourse);
    }

    /**
     * 指定活动查询
     * @param id 指定活动id
     * @return 返回该活动在ArrayList中的位置
     */
    public int getActivityById(int id) {
        Search search = new Search();
        LogFile.info("Manager"+getID()," 查询活动"+id);
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
     * 增加课程
     * @param course 指定课程
     */
    public void addCourse(Course course) {
        this.m_CaCourse.add(course);
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.insert(course);
        LogFile.info("Manager"+getID(),"管理员添加课程 "+course.getM_sName());
    }

    /**
     * 增加活动
     * @param activity 指定活动
     */
    public void addActivity(Activity activity){
        this.m_CaActivity.add(activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
      //  activityDatabase.insert(activity);
        LogFile.info("Manager"+getID(),"管理员添加活动 "+activity.getM_sName());
    }

    /**
     * 删除课程
     * @param course 指定课程
     */
    public void deleteCourse(Course course) {
        this.m_CaCourse.remove(course);
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.delete(course);
        LogFile.info("Manager"+getID(),"管理员删除课程 "+course.getM_sName());
    }

    /**
     * 删除活动
     * @param activity 待删除活动
     */
    public void deleteActivity(Activity activity){
        this.m_CaActivity.remove(activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
        activityDatabase.delete(activity,this.getID(),0);
        LogFile.info("Manager"+getID(),"管理员删除活动 "+activity.getM_sName());
    }

    /**
     * 修改课程内容
     * @param location 待修改课程位置
     * @param course 修改后的新课程内容
     */
    public void changeCourse(int location,Course course){
        this.m_CaCourse.set(location,course);//修改课程内容
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.update(course);
        LogFile.info("Manager"+getID(),"管理员修改课程 "+course.getM_sName());
    }

    /**
     * 修改活动内容
     * @param location 待修改活动的位置
     * @param activity 修改后的新活动内容
     */
    public void changeActivity(int location,Activity activity){
        this.m_CaActivity.set(location,activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
        //activityDatabase.update(activity);
        LogFile.info("Manager"+getID(),"管理员修改课程 "+activity.getM_sName());
    }

    /**
     * 修改课程列表
     * @param course 新课程列表
     */
    public void setCourse(ArrayList<Course> course){
        this.m_CaCourse = course;
    }

    /**
     * 修改活动列表
     * @param activity 新活动列表
     */
    public void setActivity(ArrayList<Activity> activity) {
        this.m_CaActivity = activity;
    }
    /**
    * 管理员为学生添加课程
     */
    public void addCourse(Course course,String ID)
    {
        AccountDatabase accountDatabase = new AccountDatabase();
        CourseDatabase courseDatabase = new CourseDatabase();
        if(accountDatabase.findStuAccount(ID))//用户存在
           courseDatabase.insert(course,ID,1);//为单个用户添加课程
    }

    /**
     * 为班级添加活动
     * @param activity 活动
     * @param ID 班级编号
     * @return boolean
     */
    public boolean addClassActivity(Activity activity,String ID) {
        boolean result = false;
        AccountDatabase accountDatabase = new AccountDatabase();
        ActivityDatabase activityDatabase = new ActivityDatabase();
        if(accountDatabase.findClass(ID))//班级存在
        {
            activityDatabase.insert(activity, ID, 1);//为单个用户添加活动
            result = true;
        }
        return result;
    }

    /**
     * 删除班级活动
     * @param activity 活动
     * @param ID 班级ID
     */
    public void deleteClassActivity(Activity activity,String ID){
        this.m_CaActivity.remove(activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
        activityDatabase.delete(activity,ID,1);
        LogFile.info("Manager"+getID(),"管理员删除活动 "+activity.getM_sName());
    }
}
