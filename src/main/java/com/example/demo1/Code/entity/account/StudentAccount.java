package com.example.demo1.Code.entity.account;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.AccountDatabase;
import com.example.demo1.Code.Mysql.ActivityDatabase;
import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.Authority;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.clock.EventClock;
import com.example.demo1.Code.entity.*;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 学生账户类
 */
public class StudentAccount extends Account{

   // private Authority m_eAuthority = Authority.Student; //权限等级
    private ArrayList<Course> m_CaCourse = new ArrayList<Course>(); //已选课程
    private ArrayList<Activity> m_CaActivity = new ArrayList<Activity>(); // 已选活动
    private ArrayList<EventClock> m_CaEventClock = new ArrayList<>();//已设置闹钟

    /**
     * 构造函数
     * @param Id 账户ID
     * @param password 账户密码
     */
    public StudentAccount(String Id, String password) {
           super(Id,password);
           setM_eAuthority(Authority.Student);
    }
    public StudentAccount(Account account) {
        super(account.getID(), account.getPassword());
        AccountDatabase accountDatabase = new AccountDatabase();
        accountDatabase.findStuAccount(this);
        sortCourse();//对课程进行排序
        sortActivity();//对活动进行排序
    }
    /**
     * 获取已选课程 (课表查询）
     * @return 课程名单
     */
    public ArrayList<Course> getCourse(){
        return this.m_CaCourse;
    }
    public void setCourse(ArrayList<Course> m_CaCourse){
        this.m_CaCourse = m_CaCourse;
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
     * 获取已选活动
     * @return 活动名单
     */
    public ArrayList<Activity> getActivity(){
        return this.m_CaActivity;
    }
    public void setActivity(ArrayList<Activity>  m_CaActivity){
        this.m_CaActivity = m_CaActivity;
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

    public ArrayList<EventClock> getM_CaEventClock() {
        return m_CaEventClock;
    }

    public void setM_CaEventClock(ArrayList<EventClock> m_CaEventClock) {
        this.m_CaEventClock = m_CaEventClock;
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
     * 检查课程是否存在时间冲突
     * @param course 待检测课程
     * @return  存在冲突则为false
     */
    public boolean checkTime(Course course){
        boolean result = true;
        for (Course c : m_CaCourse) {//foreach 对已选的每门课进行检查
            Time time = c.getM_tTime();
            if(!time.checkTime(course.getM_tTime())) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 检查活动是否存在时间冲突
     * @param activity 待检测活动
     * @return 存在冲突则为false
     */
    public boolean checkTime(Activity activity){
        boolean result = true;
        for (Course c : m_CaCourse) {//foreach 对每门课进行检查
            Time time = c.getM_tTime();
            if(!time.checkTime(activity.getM_tTime()))
                result=false;
        }
        for (Activity c : m_CaActivity) {//foreach 对每个活动进行检查
            Time time = c.getM_tTime();
            if(!time.checkTime(activity.getM_tTime())) {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * 报名参加课程
     * @param course 目标课程
     * @return 报名结果 成功/失败
     */
    public String registerCourse(Course course){
        String message = null;
        if(!checkTime(course)){
            message="报名失败,存在时间冲突";
        }
        else {
            if (course.joinCourse()) {
                message = "报名成功！";
                addCourse(course);
            }
        }
        return message;
    }

    /**
     * 退出课程
     * @param course 课程名
     * @return 退出结果 成功/失败
     */
    public String exitCourse(Course course){
        String message=null;
        int location=getCourseById(course.getM_iNum());
        if(location==m_CaCourse.size())
            message="退出失败，未参与该课程";
        else {
           if( course.delM_iPle())//退出课程，课程当前人数-1
            message="退出成功";
        }
        if(Objects.equals(message, "退出成功"))
             decCourse(course);
        return message;
    }

    /**
     * 报名参加活动
     * @param activity 活动名
     * @return 报名结果 成功/识别
     */
    public String registerActivity(Activity activity){
        String message="报名失败,存在时间冲突";
        if(checkTime(activity)&&checkActivityName(activity)){
            message="报名成功！";
            addActivity(activity);
        }
        return message;
    }
    /**
     * 检查是否有同名活动
     */
    public boolean checkActivityName(Activity activity) {
        for(Activity a:m_CaActivity) {
            if (Objects.equals(a.getM_sName(), activity.getM_sName()))
                return false;
        }
        return true;
    }

    /**
     * 退出活动
     * @param activity 活动名
     * @return 退出成功/失败
     */
    public String exitActivity(Activity activity){
        String message=null;
        int location=getCourseById(activity.getM_iNum());
        if(location==m_CaActivity.size())
            message="退出失败，未参与该活动";
        else {
                message="退出成功";
        }
        if(Objects.equals(message, "退出成功"))
            decActivity(activity);
        return message;
    }

    /**
     * 向已选课程列表中添加课程
     * @param course 待添加课程
     */
    public void addCourse(Course course){
        this.m_CaCourse.add(course);
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.insert(course,this);
        courseDatabase.update(course);
        LogFile.info("Student "+getID(),"添加课程");
   }

    /**
     * 从已选课程列表中删除课程
     * @param course 待删除课程
     */
    public void decCourse(Course course){
        this.m_CaCourse.remove(course);
        CourseDatabase courseDatabase = new CourseDatabase();
        courseDatabase.delete(course,this);
        courseDatabase.update(course);
        LogFile.info("Student "+getID(),"删除课程");
    }

    /**
     * 向已选活动列表中添加活动
     * @param activity 待添加活动
     */
    public void addActivity(Activity activity){
        this.m_CaActivity.add(activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
        activityDatabase.insert(activity,this.getID(),0);
        LogFile.info("Student "+getID(),"添加活动");
   }

    /**
     * 从已选课程列表中删除活动
     * @param activity 待删除活动
     */
    public void decActivity(Activity activity){
        this.m_CaActivity.remove(activity);
        ActivityDatabase activityDatabase = new ActivityDatabase();
        activityDatabase.delete(activity,this.getID());
        LogFile.info("Student "+getID(),"删除活动");
   }

    /**
     * 添加闹钟
     * @param clock 新闹钟
     * @return 返回是否添加成功
     */
    public boolean addClock(EventClock clock){
        boolean result = true;
        for(EventClock c:m_CaEventClock)
        {
            if(Objects.equals(c.getClockName(), clock.getClockName())) {
                result = false;
                break;
            }
        }
        if(result)
        {
            AccountDatabase accountDatabase = new AccountDatabase();
            accountDatabase.insertClock(this,clock);
        }
        return result;
    }

    /**
     * 删除闹钟
     * @param clock 闹钟
     */
    public boolean decClock(String clock)
    {
        boolean result = false;
        for(int i =0;i<m_CaEventClock.size();i++)
        {
            if(Objects.equals(m_CaEventClock.get(i).getClockName(), clock)) {
                result = true;
                m_CaEventClock.remove(i);//删除闹钟
                break;
            }
        }
        if(result)
        {
            AccountDatabase accountDatabase = new AccountDatabase();
            accountDatabase.deleteClock(this,clock);
        }
        return result;
    }
}
