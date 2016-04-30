package konquestpoints;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Camera;
import org.tbot.methods.Random;

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

    private static boolean oneIn(int change){
        int rand = Random.nextInt(1, change);
        return rand < change;
    }
}
