package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Event;

public class TickBroadcast implements Broadcast {

    boolean finish;

    public TickBroadcast(){
        this.finish = false;
    }

    public void setFinish(boolean finish) {
        this.finish = finish;
    }

    public boolean isFinish() {
        return finish;
    }

}
