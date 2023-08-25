package com.example.smartscheduling.ModelClass;

public class MessageModelClass {

    public String msg="",name="";

    int uid, sid,rid;

    public MessageModelClass(String msg, String name, int uid, int sid, int rid) {
        this.msg = msg;
        this.name = name;
        this.uid = uid;
        this.sid = sid;
        this.rid = rid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }
}
