package com.example.demo1.Code.clock;

import com.example.demo1.Code.Util.Time;
import com.example.demo1.Code.entity.account.StudentAccount;

import java.util.ArrayList;

/**
 * 闹钟交互类
 * 接口：
 * 1.启动闹钟：构造参数里面开始启动闹钟
 * 2.添加闹钟：setClock()
 * 3.删除闹钟：deleteClock()
 */
public class ClockOperation {

    private final StudentAccount studentAccount;

    private ArrayList<EventClock> allClock;

    public ClockOperation(StudentAccount studentAccount) {
        this.studentAccount = studentAccount;
        this.allClock = studentAccount.getM_CaEventClock();
        startClock();
    }

    /**
     * 启动该用户的所有闹钟
     */
    public void startClock() {

        //创建闹钟线程列表并为所有闹钟添加闹钟线程
        ArrayList<Runnable> clockThread = new ArrayList<>();
        for (EventClock ec : allClock) {
            clockThread.add(new ClockThread(ec));
        }

        //创建线程列表并将所有的闹钟线程添加线程
        ArrayList<Thread> Threads = new ArrayList<>();
        for (Runnable ct : clockThread) {
            Threads.add(new Thread(ct));
        }

        //启动所有线程
        for (Thread t : Threads) {
            t.start();
        }

    }

    public StringBuilder setClock(Time time, String name, int type) {
        StringBuilder result = new StringBuilder();
        EventClock eventClock = new EventClock(time, name, type);
        if (studentAccount.addClock(eventClock)) {
            result.append("成功设置闹钟").append(name);
        } else {
            result.append("设置闹钟失败");
        }
        return result;
    }

    public StringBuilder deleteClock(String name) {
        StringBuilder result = new StringBuilder();
        if (studentAccount.decClock(name)) {
            result.append("成功删除闹钟").append(name);
        } else {
            result.append("删除闹钟失败");
        }

        return result;
    }

    public ArrayList<EventClock> LookClock() {
        return allClock;
    }
}
