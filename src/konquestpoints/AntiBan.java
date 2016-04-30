package konquestpoints;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Camera;
import org.tbot.methods.Random;
import org.tbot.methods.walking.Walking;

import static org.tbot.methods.Random.random;

/**
 * Class AntiBan
 *
 * @author Koen Hendriks
 * @version 0.1 (30-04-2016)
 */
public final class AntiBan {

    public static void lookAround(){
        if(oneIn(Random.nextInt(100,300))){
            LogHandler.log("Looking around");
            Camera.rotateAndTiltRandomly();
        }
    }

    /**
     * Enable running if the player has enough energy
     * and is not already running.
     */
    static void handleRun(){
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

    private static boolean oneIn(int change){
        int rand = Random.nextInt(1, change);
        return rand < change;
    }
}
