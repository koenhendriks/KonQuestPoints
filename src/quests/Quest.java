package quests;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Random;
import org.tbot.methods.walking.Walking;

/**
 * Class Quest
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
abstract class Quest {

    private static String state = "start";

    static String getState() {
        return state;
    }

    static void setState(String newState) {
        LogHandler.log("State changed: "+newState);
        state = newState;
    }

    static void enableRun(){
        if(!Walking.isRunEnabled()){
            if(Walking.getRunEnergy() >= Random.nextInt(30,100)){
                LogHandler.log("Enabling running");
                Walking.setRun(true);
            }
        } else if(Walking.getRunEnergy() < Random.nextInt(0,20)){
            LogHandler.log("Stop running");
            Walking.setRun(false);
        }
    }
}
