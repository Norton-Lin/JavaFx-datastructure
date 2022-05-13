package com.example.demo1.Code.Interactive;

import com.example.demo1.Code.LogUtil.LogFile;
import com.example.demo1.Code.Mysql.AccountDatabase;
import com.example.demo1.Code.Mysql.ActivityDatabase;
import com.example.demo1.Code.Mysql.ConstructionDatabase;
import com.example.demo1.Code.Mysql.CourseDatabase;
import com.example.demo1.Code.Util.Authority;
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
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�û���¼\n");
            System.out.println("2.�˳�\n");
            choice = in.nextInt();
            if(choice == 1)
            {
                AccountDatabase accountDatabase = new AccountDatabase();
                System.out.println("�������û���:\n");
                String username = in.next();
                System.out.println("����������:\n");
                String password = in.next();
                Account account = new Account(username,password);
                if(accountDatabase.findByPassword(account))
                switch (account.getAuthority())
                {
                    case Student -> {
                        System.out.println("ѧ���˺ŵ�¼�ɹ���\n");
                        StudentInterface(account);
                    }
                    case Teacher -> {
                        System.out.println("��ʦ�˺ŵ�¼�ɹ���\n");
                        TeacherInterface(account);
                    }
                    case Manager -> {
                        System.out.println("����Ա�˺ŵ�¼�ɹ���\n");
                        ManagerInterface(account);
                    }
                }
                else
                    System.out.println("��¼ʧ�ܣ��˺Ż��������\n");
            }
        }
    }

    /**
     * ����Ա��������
     * @param account �˺���Ϣ
     */
    public void ManagerInterface(Account account){
        ManagerAccount managerAccount = new ManagerAccount(account);//���ú���֤���˺Ŵ��ڣ�ֱ�����ù��캯����ȡ����
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲���\n");
            System.out.println("2.�����\n");
            System.out.println("3.����");
            System.out.println("4.�˳���¼");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> CourseOperation(managerAccount);
                case 2 -> ActivityOperation(managerAccount);
                default ->  System.out.println("��л����ʹ�ã�");
            }
        }

    }

    /**
     * ��ʦ��������
     * @param account �˺���Ϣ
     */
    public void TeacherInterface(Account account) {
        TeacherAccount teacherAccount = new TeacherAccount(account);//���ú���֤���˺Ŵ��ڣ�ֱ�����ù��캯����ȡ����
        ArrayList<Course> allCourses = new ArrayList<>();//���пγ��б�
        ArrayList<Activity> allActivities  = new ArrayList<>();//���л�б�
        CourseDatabase courseDatabase = new CourseDatabase();
        ActivityDatabase activityDatabase =new ActivityDatabase();
        courseDatabase.find(allCourses);
        activityDatabase.find(allActivities);
        int choice = 1;
        Scanner in = new Scanner(System.in);
        while (choice <= 3 && choice >= 1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲���\n");
            System.out.println("2.�����\n");
            System.out.println("3.����");
            System.out.println("4.�˳���¼");
            choice = in.nextInt();
            switch (choice) {
                case 1 -> CourseOperation(teacherAccount,allCourses);
                case 2 -> ActivityOperation(teacherAccount,allActivities);
                default -> System.out.println("��л����ʹ�ã�");
            }
        }
    }

    /**
     * ѧ����������
     * @param account �˺���Ϣ
     */
    public void StudentInterface(Account account){
        StudentAccount studentAccount = new StudentAccount(account);//���ú���֤���˺Ŵ��ڣ�ֱ�����ù��캯����ȡ����
        ArrayList<Course> allCourses = new ArrayList<>();//���пγ��б�
        ArrayList<Activity> allActivities  = new ArrayList<>();//���л�б�
        CourseDatabase courseDatabase = new CourseDatabase();
        ActivityDatabase activityDatabase =new ActivityDatabase();
        courseDatabase.find(allCourses);
        activityDatabase.find(allActivities);
        int choice = 1;
        Scanner in = new Scanner(System.in);
        while (choice <= 3 && choice >= 1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲���\n");
            System.out.println("2.�����\n");
            System.out.println("3.����");
            System.out.println("4.�˳���¼");
            choice = in.nextInt();
            switch (choice) {
                case 1 -> CourseOperation(studentAccount,allCourses);
                case 2 -> ActivityOperation(studentAccount,allActivities);
                default -> System.out.println("��л����ʹ�ã�");
            }
        }
    }

    /**
     * �γ̲������棨����Ա��
     * @param managerAccount ����Ա�˺���Ϣ
     */
    public void CourseOperation(ManagerAccount managerAccount){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=5&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲�ѯ\n");
            System.out.println("2.�γ����\n");
            System.out.println("3.�γ�ɾ��\n");
            System.out.println("4.�γ��޸�\n");
            System.out.println("5.����ʱ�䷢��\n");
            System.out.println("6.����������\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findCourse(managerAccount);
                case 2 -> addCourse(managerAccount);
                case 3 -> deleteCourse(managerAccount);
                case 4 -> updateCourse(managerAccount);
                case 5 -> releaseTest();
                default ->  System.out.println("��л����ʹ�ã�");
            }
        }
    }

    /**
     *  �γ̲������棨��ʦ��
     * @param teacherAccount ��ʦ�˺�
     * @param allCourse ���пγ��б�
     */
    public void CourseOperation(TeacherAccount teacherAccount,ArrayList<Course> allCourse){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲�ѯ\n");
            System.out.println("2.�γ����\n");
            System.out.println("3.�γ��˳�\n");
            System.out.println("4.����������\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findCourse(teacherAccount,allCourse);
                case 2 -> addCourse(teacherAccount,allCourse);
                case 3 -> deleteCourse(teacherAccount);
                default ->  System.out.println("��л����ʹ�ã�");
            }
        }
    }

    /**
     * �γ̲������棨ѧ����
     * @param studentAccount ѧ���˺�
     * @param allCourse ���пγ��б�
     */
    public void CourseOperation(StudentAccount studentAccount,ArrayList<Course> allCourse){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲�ѯ\n");
            System.out.println("2.�γ����\n");
            System.out.println("3.�γ��˳�\n");
            System.out.println("4.����������\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findCourse(studentAccount,allCourse);
                case 2 -> addCourse(studentAccount,allCourse);
                case 3 -> deleteCourse(studentAccount);
                default ->  System.out.println("��л����ʹ�ã�");
            }
        }
    }

    /**
     * ��������棨����Ա��
     * @param managerAccount ����Ա�˺���Ϣ
     */
    public void ActivityOperation(ManagerAccount managerAccount){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=5&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.���ѯ\n");
            System.out.println("2.����\n");
            System.out.println("3.��޸�\n");
            System.out.println("4.�ɾ��\n");
            System.out.println("5.����������\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findActivity(managerAccount);
                case 2 -> addActivity(managerAccount);
                case 3 -> deleteActivity(managerAccount);
                case 4 -> updateActivity(managerAccount);
                default ->  System.out.println("��л����ʹ�ã�");
            }
        }

    }

    /**
     * ��������棨��ʦ��
     * @param teacherAccount ��ʦ�˺���Ϣ
     * @param allActivities ���л�б�
     */
    public void ActivityOperation(TeacherAccount teacherAccount,ArrayList<Activity> allActivities){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲�ѯ\n");
            System.out.println("2.�γ����\n");
            System.out.println("3.�γ��˳�\n");
            System.out.println("4.����������\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findActivity(teacherAccount,allActivities);
                case 2 -> addActivity(teacherAccount,allActivities);
                case 3 -> deleteActivity(teacherAccount);
                default ->  System.out.println("��л����ʹ�ã�");
            }
        }
    }

    /**
     * ��������棨ѧ����
     * @param studentAccount ѧ���˺�
     * @param allActivities ���л�б�
     */
    public void ActivityOperation(StudentAccount studentAccount,ArrayList<Activity> allActivities){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=3&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.�γ̲�ѯ\n");
            System.out.println("2.�γ����\n");
            System.out.println("3.�γ��˳�\n");
            System.out.println("4.����������\n");
            choice = in.nextInt();
            switch (choice)
            {
                case 1 -> findActivity(studentAccount,allActivities);
                case 2 -> addActivity(studentAccount,allActivities);
                case 3 -> deleteActivity(studentAccount);
                default ->  System.out.println("��л����ʹ�ã�");
            }
        }

    }
    /**
     * ��ѯ�γ�
     * @param managerAccount ����Ա�˺�
     */
    public void findCourse(ManagerAccount managerAccount) {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.���ݿγ̱�Ų�ѯ\n");//��׼��ѯ
            System.out.println("2.���ݿγ����Ʋ�ѯ\n");//ģ����ѯ
            System.out.println("3.�����ϸ�����\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("������γ̱��");
                    int index = managerAccount.getCourseById(in.nextInt());
                    if(index<managerAccount.getCourse().size())
                    {
                        System.out.println("�γ���ϢΪ:");
                        managerAccount.getCourse().get(index).printCourse();
                    }
                    else
                        System.out.println("�γ̲�����");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("������γ�����");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),managerAccount.getCourse());//ģ����ѯ��ÿγ�
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }

            }
        }
    }

    /**
     * ��ѯ�γ�
     * @param teacherAccount ��ʦ�˺�
     * @param allCourse ���пγ��б�
     */
    public void findCourse(TeacherAccount teacherAccount,ArrayList<Course> allCourse) {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.���ݿγ̱�Ų�ѯ���пγ�\n");//��׼��ѯ
            System.out.println("2.���ݿγ����Ʋ�ѯ���пγ�\n");//ģ����ѯ
            System.out.println("3.���ݿγ̱�Ų�ѯ��ѡ�γ�\n");//��׼��ѯ
            System.out.println("4.���ݿγ����Ʋ�ѯ��ѡ�γ�\n");//ģ����ѯ
            System.out.println("5.�����ϸ�����\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("������γ̱��");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Teacher"+teacherAccount.getID()," ��ѯ�γ�"+index);
                    index = search.BinaryCourseSearch(index,allCourse);
                    if(index<allCourse.size())
                    {
                        System.out.println("�γ���ϢΪ:");
                        allCourse.get(index).printCourse();
                    }
                    else
                        System.out.println("�γ̲�����");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("������γ�����");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),allCourse);//ģ����ѯ��ÿγ�
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }
                case 3->{
                    System.out.println("������γ̱��");
                    int index = teacherAccount.getCourseById(in.nextInt());
                    if(index<teacherAccount.getCourse().size())
                    {
                        System.out.println("�γ���ϢΪ:");
                        teacherAccount.getCourse().get(index).printCourse();
                    }
                    else
                        System.out.println("�γ̲�����");
                }
                case 4->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("������γ�����");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),teacherAccount.getCourse());//ģ����ѯ��ÿγ�
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }
            }
        }
    }

    /**
     * ��ѯ�γ�
     * @param studentAccount ѧ���˺�
     * @param allCourse ���пγ��б�
     */
    public void findCourse(StudentAccount studentAccount,ArrayList<Course> allCourse) {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.���ݿγ̱�Ų�ѯ���пγ�\n");//��׼��ѯ
            System.out.println("2.���ݿγ����Ʋ�ѯ���пγ�\n");//ģ����ѯ
            System.out.println("3.���ݿγ̱�Ų�ѯ��ѡ�γ�\n");//��׼��ѯ
            System.out.println("4.���ݿγ����Ʋ�ѯ��ѡ�γ�\n");//ģ����ѯ
            System.out.println("5.�����ϸ�����\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("������γ̱��");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Teacher"+studentAccount.getID()," ��ѯ�γ�"+index);
                    index = search.BinaryCourseSearch(index,allCourse);
                    if(index<allCourse.size())
                    {
                        System.out.println("�γ���ϢΪ:");
                        allCourse.get(index).printCourse();
                    }
                    else
                        System.out.println("�γ̲�����");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("������γ�����");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),allCourse);//ģ����ѯ��ÿγ�
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }
                case 3->{
                    System.out.println("������γ̱��");
                    int index = studentAccount.getCourseById(in.nextInt());
                    if(index<studentAccount.getCourse().size())
                    {
                        System.out.println("�γ���ϢΪ:");
                        studentAccount.getCourse().get(index).printCourse();
                    }
                    else
                        System.out.println("�γ̲�����");
                }
                case 4->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("������γ�����");
                    ArrayList<Course> courses = fuzzySearch.get_FS_result(in.next(),studentAccount.getCourse());//ģ����ѯ��ÿγ�
                    if(courses.size()!=0) {
                        for (Course course : courses)
                            course.printCourse();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }
            }
        }
    }

    /**
     * ��ӿγ�
     * @param managerAccount ����Ա�˺�
     */
    public void addCourse(ManagerAccount managerAccount){
        Course course = new Course();
        scanCourse(course);
        course.setM_iPle(0);//��ǰѡ������Ϊ0
        course.setM_iNum(managerAccount.getCourse().get(managerAccount.getCourse().size()-1).getM_iNum()+1 );//����γ̱��
        managerAccount.addCourse(course);
    }

    /**
     * ��ӿγ�
     * @param teacherAccount ��ʦ�˺�
     * @param allCourse ���пγ��б�
     */
    public void addCourse(TeacherAccount teacherAccount,ArrayList<Course> allCourse) {
        System.out.println("������γ̱�ţ�");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allCourse);
        if(index<allCourse.size())
        {
            Course course=allCourse.get(index);
            teacherAccount.addCourse(course);
        }
        else
            System.out.println("�γ̲�����");
    }

    /**
     * ��ӿγ�
     * @param studentAccount ѧ���˺�
     * @param allCourse ���пγ��б�
     */
    public void addCourse(StudentAccount studentAccount,ArrayList<Course> allCourse) {
        System.out.println("������γ̱�ţ�");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allCourse);
        if(index<allCourse.size())
        {
            Course course=allCourse.get(index);
            studentAccount.addCourse(course);
        }
        else
            System.out.println("�γ̲�����");
    }

    /**
     * ɾ���γ�
     * @param managerAccount ����Ա�˺�
     */
    public void deleteCourse(ManagerAccount managerAccount){
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        System.out.println("������γ̱�ţ�");
        int index = managerAccount.getCourseById(in.nextInt());
        if(index<managerAccount.getCourse().size()) {
            course = managerAccount.getCourse().get(index);
            managerAccount.deleteCourse(course);
        }
        else
            System.out.println("�γ̲�����");
    }

    /**
     * ɾ���γ�
     * @param teacherAccount ��ʦ�˺�
     */
    public void deleteCourse(TeacherAccount teacherAccount) {
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        System.out.println("������γ̱�ţ�");
        int index = teacherAccount.getCourseById(in.nextInt());
        if(index<teacherAccount.getCourse().size()) {
            course = teacherAccount.getCourse().get(index);
            teacherAccount.decCourse(course);
        }
        else
            System.out.println("�γ̲�����");

    }

    /**
     * ɾ���γ�
     * @param studentAccount ѧ���˺�
     */
    public void deleteCourse(StudentAccount studentAccount) {
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        System.out.println("������γ̱�ţ�");
        int index = studentAccount.getCourseById(in.nextInt());
        if(index<studentAccount.getCourse().size()) {
            course = studentAccount.getCourse().get(index);
            studentAccount.decCourse(course);
        }
        else
            System.out.println("�γ̲�����");

    }

    /**
     * ���¿γ���Ϣ
     * @param managerAccount ����Ա�˺�
     */
    public void updateCourse(ManagerAccount managerAccount){
        System.out.println("������γ̱�ţ�");
        Course course = new Course();
        Scanner in = new Scanner(System.in);
        int index = managerAccount.getCourseById(in.nextInt());
        scanCourse(course);
        managerAccount.changeCourse(index,course);
    }

    /**
     * ����������Ϣ
     */
    public void releaseTest(){

    }

    /**
     * ��ѯ�
     * @param managerAccount ����Ա�˺�
     */
    public void findActivity(ManagerAccount managerAccount){
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.���ݻ��Ų�ѯ\n");//��׼��ѯ
            System.out.println("2.���ݻ���Ʋ�ѯ\n");//ģ����ѯ
            System.out.println("3.�����ϸ�����\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("���������");
                    int index = managerAccount.getCourseById(in.nextInt());
                    if(index<managerAccount.getCourse().size())
                    {
                        System.out.println("���ϢΪ:");
                        managerAccount.getActivity().get(index).printActivity();
                    }
                    else
                        System.out.println("�������");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("����������");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),managerAccount.getActivity());//ģ����ѯ��ÿγ�
                    if(activities.size()!=0) {
                        for (Activity activity:activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }

            }
        }
    }

    /**
     * ��ѯ�
     * @param teacherAccount ��ʦ�˺�
     * @param allActivity ���л�б�
     */
    public void findActivity(TeacherAccount teacherAccount,ArrayList<Activity> allActivity)  {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.���ݻ��Ų�ѯ���л\n");//��׼��ѯ
            System.out.println("2.���ݻ���Ʋ�ѯ���л\n");//ģ����ѯ
            System.out.println("3.���ݻ��Ų�ѯ��ѡ�\n");//��׼��ѯ
            System.out.println("4.���ݻ���Ʋ�ѯ��ѡ�\n");//ģ����ѯ
            System.out.println("5.�����ϸ�����\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("���������");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Teacher"+teacherAccount.getID()," ��ѯ�"+index);
                    index = search.BinaryCourseSearch(index,allActivity);
                    if(index<allActivity.size())
                    {
                        System.out.println("���ϢΪ:");
                        allActivity.get(index).printActivity();
                    }
                    else
                        System.out.println("�������");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("����������");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),allActivity);//ģ����ѯ��û
                    if(activities.size()!=0) {
                        for (Activity activity : activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("����ػ��\n");
                }
                case 3->{
                    System.out.println("���������");
                    int index = teacherAccount.getActivityById(in.nextInt());
                    if(index<teacherAccount.getActivity().size())
                    {
                        System.out.println("���ϢΪ:");
                        teacherAccount.getActivity().get(index).printActivity();
                    }
                    else
                        System.out.println("�������");
                }
                case 4->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("������γ�����");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),teacherAccount.getActivity());//ģ����ѯ��ÿγ�
                    if(activities.size()!=0) {
                        for (Activity activity:activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }
            }
        }
    }

    /**
     * ��ѯ�
     * @param studentAccount ѧ���˺�
     * @param allActivity ���л�б�
     */
    public void findActivity(StudentAccount studentAccount,ArrayList<Activity> allActivity)  {
        int choice=1;
        Scanner in = new Scanner(System.in);
        while(choice<=2&&choice>=1) {
            System.out.println("��ѡ����Ҫִ�еĲ�����\n");
            System.out.println("1.���ݻ��Ų�ѯ���л\n");//��׼��ѯ
            System.out.println("2.���ݻ���Ʋ�ѯ���л\n");//ģ����ѯ
            System.out.println("3.���ݻ��Ų�ѯ��ѡ�\n");//��׼��ѯ
            System.out.println("4.���ݻ���Ʋ�ѯ��ѡ�\n");//ģ����ѯ
            System.out.println("5.�����ϸ�����\n");
            choice = in.nextInt();
            switch (choice) {
                case 1->{
                    System.out.println("���������");
                    Search search = new Search();
                    int index = in.nextInt();
                    LogFile.info("Student"+studentAccount.getID()," ��ѯ�"+index);
                    index = search.BinaryCourseSearch(index,allActivity);
                    if(index<allActivity.size())
                    {
                        System.out.println("���ϢΪ:");
                        allActivity.get(index).printActivity();
                    }
                    else
                        System.out.println("�������");
                }
                case 2->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("����������");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),allActivity);//ģ����ѯ��û
                    if(activities.size()!=0) {
                        for (Activity activity : activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("����ػ��\n");
                }
                case 3->{
                    System.out.println("���������");
                    int index = studentAccount.getActivityById(in.nextInt());
                    if(index<studentAccount.getActivity().size())
                    {
                        System.out.println("���ϢΪ:");
                        studentAccount.getActivity().get(index).printActivity();
                    }
                    else
                        System.out.println("�������");
                }
                case 4->{
                    FuzzySearch fuzzySearch = new FuzzySearch();
                    System.out.println("������γ�����");
                    ArrayList<Activity> activities = fuzzySearch.get_FS_result(in.next(),studentAccount.getActivity());//ģ����ѯ��ÿγ�
                    if(activities.size()!=0) {
                        for (Activity activity:activities)
                            activity.printActivity();
                    }
                    else
                        System.out.println("����ؿγ̡�\n");
                }
            }
        }
    }

    /**
     * ��ӻ
     * @param managerAccount ����Ա�˺�
     */
    public void addActivity(ManagerAccount managerAccount){
        Activity activity = new Activity();
        scanActivity(activity);
        activity.setM_iPle(0);//��ǰ�����Ϊ0
        activity.setM_iNum(2000+managerAccount.getActivity().get(managerAccount.getActivity().size()-1).getM_iNum());//����γ̱��
        managerAccount.addActivity(activity);
    }

    /**
     * ��ӻ
     * @param teacherAccount ��ʦ�˺�
     * @param allActivity ���л�б�
     */
    public void addActivity(TeacherAccount teacherAccount,ArrayList<Activity> allActivity){
        System.out.println("������γ̱�ţ�");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allActivity);
        if(index<allActivity.size())
        {
            Activity activity=allActivity.get(index);
            teacherAccount.addActivity(activity);
        }
        else
            System.out.println("�γ̲�����");
    }

    /**
     * ��ӻ
     * @param studentAccount ѧ���˺�
     * @param allActivity
     */
    public void addActivity(StudentAccount studentAccount,ArrayList<Activity> allActivity){
        System.out.println("������γ̱�ţ�");
        Scanner in = new Scanner(System.in);
        Search search = new Search();
        int index = search.BinaryCourseSearch(in.nextInt(),allActivity);
        if(index<allActivity.size())
        {
            Activity activity=allActivity.get(index);
            studentAccount.addActivity(activity);
        }
        else
            System.out.println("�γ̲�����");
    }


    /**
     * ɾ���
     * @param managerAccount ����Ա�˺�
     */
    public void deleteActivity(ManagerAccount managerAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("������γ̱�ţ�");
        int index = managerAccount.getActivityById(in.nextInt());
        activity = managerAccount.getActivity().get(index);
        managerAccount.deleteActivity(activity);
    }

    /**
     * ɾ���
     * @param teacherAccount ��ʦ�˺�
     */
    public void deleteActivity(TeacherAccount teacherAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("������γ̱�ţ�");
        int index = teacherAccount.getActivityById(in.nextInt());
        activity = teacherAccount.getActivity().get(index);
        teacherAccount.decActivity(activity);
    }

    /**
     * ɾ���
     * @param studentAccount ��ʦ�˺�
     */
    public void deleteActivity(StudentAccount studentAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("������γ̱�ţ�");
        int index = studentAccount.getActivityById(in.nextInt());
        activity = studentAccount.getActivity().get(index);
        studentAccount.decActivity(activity);
    }

    /**
     * ���»��Ϣ
     * @param managerAccount ����Ա�˺�
     */
    public void updateActivity(ManagerAccount managerAccount){
        Activity activity = new Activity();
        Scanner in = new Scanner(System.in);
        System.out.println("��������ţ�");
        int index = managerAccount.getCourseById(in.nextInt());
        scanActivity(activity);
        managerAccount.changeActivity(index,activity);
    }

    /**
     * ����γ���Ϣ
     * @param course ������γ�
     */
    public void scanCourse(Course course) {
        Scanner in = new Scanner(System.in);
        Time time1 = new Time();
        Time time2 = new Time();
        Construction construction = new Construction();
        System.out.println("������γ����ƣ�");
        course.setM_sName(in.next());
        System.out.println("�������Ͽ�ʱ�䣺");
        time1.scanTime();
        course.setM_tTime(time1);
        System.out.println("������γ����ͣ�0.�����࣬1.ʵ���࣬2.�����ࣩ��");
        course.setM_eProperty(in.nextInt());
        System.out.println("�������ڿεص㣺");
        course.setM_sConstruction(constructions.get(findConstruction(in.nextInt())));
        course.setM_iFloor(in.nextInt());
        course.setM_iRoom(in.nextInt());
        System.out.println("������������γ�������");
        course.setM_iMaxPle(in.nextInt());
        System.out.println("������γ�Ⱥ��");
        course.setM_sCurGroup(in.next());
        System.out.println("������γ̿���ʱ�䣺");
        time2.scanDateTime();
        System.out.println("������γ̿��Եص㣺");
        course.setM_cExamConstruction(constructions.get(findConstruction(in.nextInt())));
        course.setM_iFloor(in.nextInt());
        course.setM_iRoom(in.nextInt());

    }

    /**
     * ������Ϣ
     * @param activity ������
     */
    public void scanActivity(Activity activity){
        Scanner in = new Scanner(System.in);
        Time time1 = new Time();
        Construction construction = new Construction();
        System.out.println("���������ƣ�");
        activity.setM_sName(in.next());
        System.out.println("������ʱ�䣺");
        time1.scanTime();
        activity.setM_tTime(time1);
        System.out.println("���������ͣ�0.���ˣ�1.���壩��");
        activity.setM_eProperty(in.nextInt());
        System.out.println("�������ص㣺");
        activity.setM_sConstruction(constructions.get(findConstruction(in.nextInt())));
        activity.setM_iFloor(in.nextInt());
        activity.setM_iRoom(in.nextInt());
        System.out.println("������������������");
        activity.setM_iMaxPle(in.nextInt());
    }

    public static void main(String args[])
    {
        MainInterface m = new MainInterface();
        m.mainInterface();
    }

}
