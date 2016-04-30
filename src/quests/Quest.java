package quests;

import org.tbot.internal.handlers.LogHandler;

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

    public static void setState(String newState) {
        LogHandler.log("State changed: "+newState);
        state = newState;
    }
}
