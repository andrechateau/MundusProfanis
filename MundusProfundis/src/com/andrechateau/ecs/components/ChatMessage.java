/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andrechateau.ecs.components;

import com.artemis.Component;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Andre Chateaubriand
 */
public class ChatMessage extends Component {

    private LinkedList<String> msg;
    private LinkedList<Long> times;
    private String name;
    public final long NEW_TIME = 5000;
    private long time;

    public ChatMessage(String msg, String name) {
        time = NEW_TIME;
        this.msg = new LinkedList<>();
        this.times = new LinkedList<>();
        this.msg.add(msg);
        this.times.add(NEW_TIME);
        this.name = name;
    }

    public void decreaseTime(int t) {
        int howMany = 0;
        for (int i = 0; i < times.size(); i++) {
            times.set(i, times.get(i) - t);
            if (times.get(i) <= 0) {
                howMany++;
            }
        }
        for (int i = 0; i < howMany; i++) {
            times.removeFirst();
            this.msg.removeFirst();
        }
        time -= t;
    }

    /**
     * @return the msg
     */
    public LinkedList<String> getMsg() {
        return msg;
    }

    /**
     * @param msg the msg to set
     */
    public void setMsg(LinkedList<String> msg) {
        this.msg = msg;
    }

    public void addMsg(String msg) {
        time = NEW_TIME;
        this.times.add(NEW_TIME);
        this.msg.add(msg);
    }

    /**
     * @return the time
     */
    public long getTime() {
        return time;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
