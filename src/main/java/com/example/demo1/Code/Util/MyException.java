package com.example.demo1.Code.Util;

public class MyException extends Exception{
    String message;

    public MyException(int i) {
        switch (i) {
            case 0 -> message = "建筑名非法！";
            case 1 -> message = "时间非法！";
            case 2 -> message = "楼层或房间号非法！";
        }
    }

    public String getMessage() {
        return this.message;
    }
}
