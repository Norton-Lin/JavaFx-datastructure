package com.example.demo1.Code.Interactive;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.AccountDatabase;
import com.example.demo1.Code.Mysql.ActivityDatabase;
import com.example.demo1.Code.Mysql.ConstructionDatabase;
import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.*;
import com.example.demo1.Code.entity.account.Account;
import com.example.demo1.Code.entity.account.ManagerAccount;
import com.example.demo1.Code.entity.account.StudentAccount;
import com.example.demo1.Code.entity.account.TeacherAccount;

import java.util.ArrayList;
import java.util.Scanner;

public class MainInterface {
    private ConstructionDatabase constructionDatabase = new ConstructionDatabase();
    private ArrayList<Construction> constructions = new ArrayList<>();
    MainInterface(){
        constructionDatabase.find(constructions);
    }
    public int findConstruction(int n ){
        FindBuilding findBuilding = new FindBuilding();
        return findBuilding.BinaryConstructionSearch(n,constructions);
    }
    public void mainInterface(){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice==1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.用户登录\n");
            System.out.println("2.退出\n");
            choice = in.nextInt();
            if(choice == 1)
            {
                AccountDatabase accountDatabase = new AccountDatabase();
                System.out.println("请输入用户名:\n");
                String username = in.next();
                System.out.println("请输入密码:\n");
                String password = in.next();
                Account account = new Account(username,password);
                if(accountDatabase.findByPassword(account))
                    switch (account.getAuthority())
                    {
                        case Student -> {
                            System.out.println("学生账号登录成功！\n");
                            StudentInterface(account);
                        }
                        case Teacher -> {
                            System.out.println("教师账号登录成功！\n");
                            TeacherInterface(account);
                        }
                        case Manager -> {
                            System.out.println("管理员账号登录成功！\n");
                            ManagerInterface(account);
                        }
                    }
                else
                    System.out.println("登录失败！账号或密码错误\n");
            }
        }
    }

    /**
     * 管理员操作界面
     * @param account 账号信息
     */
    public void ManagerInterface(Account account){
        ManagerAccount managerAccount = new ManagerAccount(account);//调用函数证明账号存在，直接利用构造函数读取数据
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.课程操作\n");
            System.out.println("2.活动操作\n");
            System.out.println("3.导航");
            System.out.println("4.退出登录");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> CourseOperation(managerAccount);
                case 2 -> ActivityOperation(managerAccount);
                default ->  System.out.println("感谢您的使用！");
            }
        }

    }

    /**
     * 教师操作界面
     * @param account 账号信息
     */
    public void TeacherInterface(Account account) {
        TeacherAccount teacherAccount = new TeacherAccount(account);//调用函数证明账号存在，直接利用构造函数读取数据
        ArrayList<Course> allCourses = new ArrayList<>();//所有课程列表
        ArrayList<Activity> allActivities  = new ArrayList<>();//所有活动列表
        CourseDatabase courseDatabase = new CourseDatabase();
        ActivityDatabase activityDatabase =new ActivityDatabase();
        courseDatabase.find(allCourses);
        activityDatabase.find(allActivities,account.getID());
        int choice = 1;
        Scanner in = new Scanner(System.in);
        while (choice <= 3 && choice >= 1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.课程操作\n");
            System.out.println("2.活动操作\n");
            System.out.println("3.导航");
            System.out.println("4.退出登录");
            choice = in.nextInt();
            switch (choice) {
                case 1 -> CourseOperation(teacherAccount,allCourses);
                case 2 -> ActivityOperation(teacherAccount,allActivities);
                default -> System.out.println("感谢您的使用！");
            }
        }
    }

    /**
     * 学生操作界面
     * @param account 账号信息
     */
    public void StudentInterface(Account account){
        StudentAccount studentAccount = new StudentAccount(account);//调用函数证明账号存在，直接利用构造函数读取数据
        ArrayList<Course> allCourses = new ArrayList<>();//所有课程列表
        ArrayList<Activity> allActivities  = new ArrayList<>();//所有活动列表
        CourseDatabase courseDatabase = new CourseDatabase();
        ActivityDatabase activityDatabase =new ActivityDatabase();
        courseDatabase.find(allCourses);
        activityDatabase.find(allActivities,account.getID());
        int choice = 1;
        Scanner in = new Scanner(System.in);
        while (choice <= 3 && choice >= 1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.课程操作\n");
            System.out.println("2.活动操作\n");
            System.out.println("3.导航");
            System.out.println("4.退出登录");
            choice = in.nextInt();
            switch (choice) {
                case 1 -> CourseOperation(studentAccount,allCourses);
                case 2 -> ActivityOperation(studentAccount,allActivities);
                default -> System.out.println("感谢您的使用！");
            }
        }
    }

    /**
     * 课程操作界面（管理员）
     * @param managerAccount 管理员账号信息
     */
    public void CourseOperation(ManagerAccount managerAccount){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=5&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.课程查询\n");
            System.out.println("2.课程添加\n");
            System.out.println("3.课程删除\n");
            System.out.println("4.课程修改\n");
            System.out.println("5.考试时间发布\n");
            System.out.println("6.返回主界面\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findCourse(managerAccount);
                case 2 -> addCourse(managerAccount);
                case 3 -> deleteCourse(managerAccount);
                case 4 -> updateCourse(managerAccount);
                case 5 -> releaseTest();
                default ->  System.out.println("感谢您的使用！");
            }
        }
    }

    /**
     *  课程操作界面（教师）
     * @param teacherAccount 教师账号
     * @param allCourse 所有课程列表
     */
    public void CourseOperation(TeacherAccount teacherAccount,ArrayList<Course> allCourse){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.课程查询\n");
            System.out.println("2.课程添加\n");
            System.out.println("3.课程退出\n");
            System.out.println("4.返回主界面\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findCourse(teacherAccount,allCourse);
                case 2 -> addCourse(teacherAccount,allCourse);
                case 3 -> deleteCourse(teacherAccount);
                default ->  System.out.println("感谢您的使用！");
            }
        }
    }

    /**
     * 课程操作界面（学生）
     * @param studentAccount 学生账号
     * @param allCourse 所有课程列表
     */
    public void CourseOperation(StudentAccount studentAccount,ArrayList<Course> allCourse){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.课程查询\n");
            System.out.println("2.课程添加\n");
            System.out.println("3.课程退出\n");
            System.out.println("4.返回主界面\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findCourse(studentAccount,allCourse);
                case 2 -> addCourse(studentAccount,allCourse);
                case 3 -> deleteCourse(studentAccount);
                default ->  System.out.println("感谢您的使用！");
            }
        }
    }

    /**
     * 活动操作界面（管理员）
     * @param managerAccount 管理员账号信息
     */
    public void ActivityOperation(ManagerAccount managerAccount){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=5&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.活动查询\n");
            System.out.println("2.活动添加\n");
            System.out.println("3.活动修改\n");
            System.out.println("4.活动删除\n");
            System.out.println("5.返回主界面\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findActivity(managerAccount);
                case 2 -> addActivity(managerAccount);
                case 3 -> deleteActivity(managerAccount);
                case 4 -> updateActivity(managerAccount);
                default ->  System.out.println("感谢您的使用！");
            }
        }

    }

    /**
     * 活动操作界面（教师）
     * @param teacherAccount 教师账号信息
     * @param allActivities 所有活动列表
     */
    public void ActivityOperation(TeacherAccount teacherAccount,ArrayList<Activity> allActivities){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.活动查询\n");
            System.out.println("2.活动添加\n");
            System.out.println("3.活动删除\n");
            System.out.println("4.返回主界面\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findActivity(teacherAccount,allActivities);
                case 2 -> addActivity(teacherAccount,allActivities);
                case 3 -> deleteActivity(teacherAccount);
                default ->  System.out.println("感谢您的使用！");
            }
        }
    }

    /**
     * 活动操作界面（学生）
     * @param studentAccount 学生账号
     * @param allActivities 所有活动列表
     */
    public void ActivityOperation(StudentAccount studentAccount,ArrayList<Activity> allActivities){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.活动查询\n");
            System.out.println("2.活动添加\n");
            System.out.println("3.活动删除\n");
            System.out.println("4.返回主界面\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findActivity(studentAccount,allActivities);
                case 2 -> addActivity(studentAccount,allActivities);
                case 3 -> deleteActivity(studentAccount);
                default ->  System.out.println("感谢您的使用！");
            }
        }

    }
    /**
     * 查询课程
     * @param managerAccount 管理员账号
     */
    public void findCourse(ManagerAccount managerAccount) {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.根据课程编号查询\n");//精准查询
            System.out.println("2.根据课程名称查询\n");//模糊查询
            System.out.println("3.查询所有课程\n");
            System.out.println("4.返回上个界面\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("请输入课程编号");
                    int index = managerAccount.getCourseById(in.nextInt());
                    if(index<managerAccount.getCourse().size())
                    {
                        System.out.println("课程信息为:");
                        managerAccount.getCourse().get(index).printCourse();
                    }
                    else
                        System.out.println("课程不存在");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入课程名称");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),managerAccount.getCourse());//模糊查询获得课程
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("无相关课程。\n");
                }
                case 3->{
                    for (Course course : managerAccount.getCourse())
                        course.printCourse();
                }
            }
        }
    }

    /**
     * 查询课程
     * @param teacherAccount 教师账号
     * @param allCourse 所有课程列表
     */
    public void findCourse(TeacherAccount teacherAccount,ArrayList<Course> allCourse) {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=5&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.根据课程编号查询所有课程\n");//精准查询
            System.out.println("2.根据课程名称查询所有课程\n");//模糊查询
            System.out.println("3.根据课程编号查询已选课程\n");//精准查询
            System.out.println("4.根据课程名称查询已选课程\n");//模糊查询
            System.out.println("5.查询所有已选课程\n");
            System.out.println("6.返回上个界面\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("请输入课程编号");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Teacher"+teacherAccount.getID()," 查询课程"+index);
                    index = search.BinaryCourseSearch(index,allCourse);
                    if(index<allCourse.size())
                    {
                        System.out.println("课程信息为:");
                        allCourse.get(index).printCourse();
                    }
                    else
                        System.out.println("课程不存在");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入课程名称");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),allCourse);//模糊查询获得课程
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("无相关课程。\n");
                }
                case 3->{
                    System.out.println("请输入课程编号");
                    int index = teacherAccount.getCourseById(in.nextInt());
                    if(index<teacherAccount.getCourse().size())
                    {
                        System.out.println("课程信息为:");
                        teacherAccount.getCourse().get(index).printCourse();
                    }
                    else
                        System.out.println("课程不存在");
                }
                case 4->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入课程名称");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),teacherAccount.getCourse());//模糊查询获得课程
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("无相关课程。\n");
                }
                case 5->{
                    for (Course course : teacherAccount.getCourse())
                        course.printCourse();
                }
            }
        }
    }

    /**
     * 查询课程
     * @param studentAccount 学生账号
     * @param allCourse 所有课程列表
     */
    public void findCourse(StudentAccount studentAccount,ArrayList<Course> allCourse) {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=5&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.根据课程编号查询所有课程\n");//精准查询
            System.out.println("2.根据课程名称查询所有课程\n");//模糊查询
            System.out.println("3.根据课程编号查询已选课程\n");//精准查询
            System.out.println("4.根据课程名称查询已选课程\n");//模糊查询
            System.out.println("5.查询所有已选课程\n");
            System.out.println("6.返回上个界面\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("请输入课程编号");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Teacher"+studentAccount.getID()," 查询课程"+index);
                    index = search.BinaryCourseSearch(index,allCourse);
                    if(index<allCourse.size())
                    {
                        System.out.println("课程信息为:");
                        allCourse.get(index).printCourse();
                    }
                    else
                        System.out.println("课程不存在");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入课程名称");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),allCourse);//模糊查询获得课程
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("无相关课程。\n");
                }
                case 3->{
                    System.out.println("请输入课程编号");
                    int index = studentAccount.getCourseById(in.nextInt());
                    if(index<studentAccount.getCourse().size())
                    {
                        System.out.println("课程信息为:");
                        studentAccount.getCourse().get(index).printCourse();
                    }
                    else
                        System.out.println("课程不存在");
                }
                case 4->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入课程名称");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),studentAccount.getCourse());//模糊查询获得课程
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("无相关课程。\n");
                }
                case 5->{
                    for (Course course : studentAccount.getCourse())
                        course.printCourse();
                }
            }
        }
    }

    /**
     * 添加课程
     * @param managerAccount 管理员账号
     */
    public void addCourse(ManagerAccount managerAccount){
        Course course = new Course();
        scanCourse(course);
        course.setM_iPle(0);//当前选课人数为0
        course.setM_iNum(managerAccount.getCourse().get(managerAccount.getCourse().size()-1).getM_iNum()+1 );//分配课程编号
        managerAccount.addCourse(course);
    }

    /**
     * 添加课程
     * @param teacherAccount 教师账号
     * @param allCourse 所有课程列表
     */
    public void addCourse(TeacherAccount teacherAccount,ArrayList<Course> allCourse) {
        System.out.println("请输入课程编号：");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allCourse);
        if(index<allCourse.size())
        {
            Course course=allCourse.get(index);
            teacherAccount.addCourse(course);
        }
        else
            System.out.println("课程不存在");
    }

    /**
     * 添加课程
     * @param studentAccount 学生账号
     * @param allCourse 所有课程列表
     */
    public void addCourse(StudentAccount studentAccount,ArrayList<Course> allCourse) {
        System.out.println("请输入课程编号：");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allCourse);
        if(index<allCourse.size())
        {
            Course course=allCourse.get(index);
            studentAccount.addCourse(course);
        }
        else
            System.out.println("课程不存在");
    }

    /**
     * 删除课程
     * @param managerAccount 管理员账号
     */
    public void deleteCourse(ManagerAccount managerAccount){
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入课程编号：");
        int index = managerAccount.getCourseById(in.nextInt());
        if(index<managerAccount.getCourse().size()) {
            course = managerAccount.getCourse().get(index);
            managerAccount.deleteCourse(course);
        }
        else
            System.out.println("课程不存在");
    }

    /**
     * 删除课程
     * @param teacherAccount 教师账号
     */
    public void deleteCourse(TeacherAccount teacherAccount) {
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入课程编号：");
        int index = teacherAccount.getCourseById(in.nextInt());
        if(index<teacherAccount.getCourse().size()) {
            course = teacherAccount.getCourse().get(index);
            teacherAccount.decCourse(course);
        }
        else
            System.out.println("课程不存在");

    }

    /**
     * 删除课程
     * @param studentAccount 学生账号
     */
    public void deleteCourse(StudentAccount studentAccount) {
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入课程编号：");
        int index = studentAccount.getCourseById(in.nextInt());
        if(index<studentAccount.getCourse().size()) {
            course = studentAccount.getCourse().get(index);
            studentAccount.decCourse(course);
        }
        else
            System.out.println("课程不存在");

    }

    /**
     * 更新课程信息
     * @param managerAccount 管理员账号
     */
    public void updateCourse(ManagerAccount managerAccount){
        System.out.println("请输入课程编号：");
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        int index = managerAccount.getCourseById(in.nextInt());
        course= managerAccount.getCourse().get(index);
        scanCourse(course);
        managerAccount.changeCourse(index,course);
    }

    /**
     * 发布考试信息
     */
    public void releaseTest(){

    }

    /**
     * 查询活动
     * @param managerAccount 管理员账号
     */
    public void findActivity(ManagerAccount managerAccount){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.根据活动编号查询\n");//精准查询
            System.out.println("2.根据活动名称查询\n");//模糊查询
            System.out.println("3.查询所有活动\n");
            System.out.println("4.返回上个界面\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("请输入活动编号");
                    int index = managerAccount.getCourseById(in.nextInt());
                    if(index<managerAccount.getCourse().size())
                    {
                        System.out.println("活动信息为:");
                        managerAccount.getActivity().get(index).printActivity();
                    }
                    else
                        System.out.println("活动不存在");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入活动名称");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),managerAccount.getActivity());//模糊查询获得课程
                    if(activities.size()!=0) {
                        for (Activity activity:activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("无相关课程。\n");
                }
                case 3->{
                    for (Activity activity : managerAccount.getActivity())
                        activity.printActivity();
                }

            }
        }
    }

    /**
     * 查询活动
     * @param teacherAccount 教师账号
     * @param allActivity 所有活动列表
     */
    public void findActivity(TeacherAccount teacherAccount,ArrayList<Activity> allActivity)  {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.根据活动编号查询所有活动\n");//精准查询
            System.out.println("2.根据活动名称查询所有活动\n");//模糊查询
            System.out.println("3.根据活动编号查询已选活动\n");//精准查询
            System.out.println("4.根据活动名称查询已选活动\n");//模糊查询
            System.out.println("5.查询所有活动\n");
            System.out.println("6.返回上个界面\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("请输入活动编号");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Teacher"+teacherAccount.getID()," 查询活动"+index);
                    index = search.BinaryCourseSearch(index,allActivity);
                    if(index<allActivity.size())
                    {
                        System.out.println("活动信息为:");
                        allActivity.get(index).printActivity();
                    }
                    else
                        System.out.println("活动不存在");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入活动名称");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),allActivity);//模糊查询获得活动
                    if(activities.size()!=0) {
                        for (Activity activity : activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("无相关活动。\n");
                }
                case 3->{
                    System.out.println("请输入活动编号");
                    int index = teacherAccount.getActivityById(in.nextInt());
                    if(index<teacherAccount.getActivity().size())
                    {
                        System.out.println("活动信息为:");
                        teacherAccount.getActivity().get(index).printActivity();
                    }
                    else
                        System.out.println("活动不存在");
                }
                case 4-> {
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入课程名称");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(), teacherAccount.getActivity());//模糊查询获得课程
                    if (activities.size() != 0) {
                        for (Activity activity : activities)
                            activity.printActivity();
                    } else
                        System.out.println("无相关课程。\n");
                }
                case 5-> {
                    for (Activity activity : teacherAccount.getActivity())
                        activity.printActivity();
                }
            }
        }
    }

    /**
     * 查询活动
     * @param studentAccount 学生账号
     * @param allActivity 所有活动列表
     */
    public void findActivity(StudentAccount studentAccount,ArrayList<Activity> allActivity)  {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("请选择您要执行的操作：\n");
            System.out.println("1.根据活动编号查询所有活动\n");//精准查询
            System.out.println("2.根据活动名称查询所有活动\n");//模糊查询
            System.out.println("3.根据活动编号查询已选活动\n");//精准查询
            System.out.println("4.根据活动名称查询已选活动\n");//模糊查询
            System.out.println("5.查询所有活动\n");
            System.out.println("6.返回上个界面\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("请输入活动编号");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Student"+studentAccount.getID()," 查询活动"+index);
                    index = search.BinaryCourseSearch(index,allActivity);
                    if(index<allActivity.size())
                    {
                        System.out.println("活动信息为:");
                        allActivity.get(index).printActivity();
                    }
                    else
                        System.out.println("活动不存在");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入活动名称");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),allActivity);//模糊查询获得活动
                    if(activities.size()!=0) {
                        for (Activity activity : activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("无相关活动。\n");
                }
                case 3->{
                    System.out.println("请输入活动编号");
                    int index = studentAccount.getActivityById(in.nextInt());
                    if(index<studentAccount.getActivity().size())
                    {
                        System.out.println("活动信息为:");
                        studentAccount.getActivity().get(index).printActivity();
                    }
                    else
                        System.out.println("活动不存在");
                }
                case 4->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("请输入课程名称");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),studentAccount.getActivity());//模糊查询获得课程
                    if(activities.size()!=0) {
                        for (Activity activity:activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("无相关课程。\n");
                }
                case 5->{
                    for (Activity activity : studentAccount.getActivity())
                        activity.printActivity();
                }
            }
        }
    }

    /**
     * 添加活动
     * @param managerAccount 管理员账号
     */
    public void addActivity(ManagerAccount managerAccount){
        Activity activity = new Activity();
        scanActivity(activity);
        activity.setM_iPle(0);//当前活动人数为0
        activity.setM_iNum(2000+managerAccount.getActivity().get(managerAccount.getActivity().size()-1).getM_iNum());//分配课程编号
        managerAccount.addActivity(activity);
    }

    /**
     * 添加活动
     * @param teacherAccount 教师账号
     * @param allActivity 所有活动列表
     */
    public void addActivity(TeacherAccount teacherAccount,ArrayList<Activity> allActivity){
        System.out.println("请输入课程编号：");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allActivity);
        if(index<allActivity.size())
        {
            Activity activity=allActivity.get(index);
            teacherAccount.addActivity(activity);
        }
        else
            System.out.println("课程不存在");
    }

    /**
     * 添加活动
     * @param studentAccount 学生账号
     * @param allActivity 所有活动列表
     */
    public void addActivity(StudentAccount studentAccount,ArrayList<Activity> allActivity){
        System.out.println("请输入课程编号：");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allActivity);
        if(index<allActivity.size())
        {
            Activity activity=allActivity.get(index);
            studentAccount.addActivity(activity);
        }
        else
            System.out.println("课程不存在");
    }

    /**
     * 删除活动
     * @param managerAccount 管理员账号
     */
    public void deleteActivity(ManagerAccount managerAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入课程编号：");
        int index = managerAccount.getActivityById(in.nextInt());
        activity = managerAccount.getActivity().get(index);
        managerAccount.deleteActivity(activity);
    }

    /**
     * 删除活动
     * @param teacherAccount 教师账号
     */
    public void deleteActivity(TeacherAccount teacherAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入课程编号：");
        int index = teacherAccount.getActivityById(in.nextInt());
        activity = teacherAccount.getActivity().get(index);
        teacherAccount.decActivity(activity);
    }

    /**
     * 删除活动
     * @param studentAccount 教师账号
     */
    public void deleteActivity(StudentAccount studentAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入课程编号：");
        int index = studentAccount.getActivityById(in.nextInt());
        activity = studentAccount.getActivity().get(index);
        studentAccount.decActivity(activity);
    }

    /**
     * 更新活动信息
     * @param managerAccount 管理员账号
     */
    public void updateActivity(ManagerAccount managerAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("请输入活动编号：");
        int index = managerAccount.getCourseById(in.nextInt());
        activity = managerAccount.getActivity().get(index);
        scanActivity(activity);
        managerAccount.changeActivity(index,activity);
    }

    /**
     * 输入课程信息
     * @param course 待输入课程
     */
    public void scanCourse(Course course) {
        Scanner in = new Scanner(System.in);
        Time time1 = new Time();
        Time time2 = new Time();
        System.out.println("请输入课程名称：");
        course.setM_sName(in.next());
        System.out.println("请输入上课时间：");
        time1.scanTime();
        course.setM_tTime(time1);
        System.out.println("请输入课程类型（0.理论类，1.实践类，2.体育类）：");
        course.setM_eProperty(in.nextInt());
        System.out.println("请输入授课地点：");
        course.setM_sConstruction(constructions.get(findConstruction(in.nextInt())));
        course.setM_iFloor(in.nextInt());
        course.setM_iRoom(in.nextInt());
        System.out.println("请输入最大参与课程人数：");
        course.setM_iMaxPle(in.nextInt());
        System.out.println("请输入课程群：");
        course.setM_sCurGroup(in.next());
        System.out.println("请输入课程考试时间：");
        time2.scanDateTime();
        System.out.println("请输入课程考试地点：");
        course.setM_cExamConstruction(constructions.get(findConstruction(in.nextInt())));
        System.out.println("请输入课程资料区路径：");
        course.setM_sData(in.next());
        course.setM_iFloor(in.nextInt());
        course.setM_iRoom(in.nextInt());
    }

    /**
     * 输入活动信息
     * @param activity 待输入活动
     */
    public void scanActivity(Activity activity){
        Scanner in = new Scanner(System.in);
        Time time1 = new Time();
        System.out.println("请输入活动名称：");
        activity.setM_sName(in.next());
        System.out.println("请输入活动时间：");
        time1.scanTime();
        activity.setM_tTime(time1);
        System.out.println("请输入活动类型（0.个人，1.集体）：");
        activity.setM_eProperty(in.nextInt());
        System.out.println("请输入活动地点：");
        activity.setM_sConstruction(constructions.get(findConstruction(in.nextInt())));
        activity.setM_iFloor(in.nextInt());
        activity.setM_iRoom(in.nextInt());
        System.out.println("请输入最大参与活动人数：");
        activity.setM_iMaxPle(in.nextInt());
    }

    public static void main(String[] args)
    {
        MainInterface m = new MainInterface();
        m.mainInterface();

    }

}
