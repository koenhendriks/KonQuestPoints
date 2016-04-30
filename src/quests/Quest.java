package quests;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.GameObjects;
import org.tbot.methods.GroundItems;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.GroundItem;

/**
 * Class Quest
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
abstract class Quest {

    private static String state = "start";

    /**
     * Get the current state of the quest
     *
     * @return String state of the quest
     */
    static String getState() {
        return state;
    }

    /**
     * Set a new state for the quest
     *
     * @param newState String of the new state
     */
    static void setState(String newState) {
        LogHandler.log("State changed: "+newState);
        state = newState;
    }

    /**
     * Go to a certain game object in an area, when found
     * it then changes the state of the quest.
     *
     * @param gameObject String of the game object to go to
     * @param gameObjectArea Area where the game object might be
     * @param nextState String to which the state will change when the game object has been found
     */
    static void goToGameObject(final String gameObject, Area gameObjectArea, String nextState){
        GameObject go = GameObjects.getNearest(gameObject);
        Path pathToGo = Walking.findPath(gameObjectArea.getCentralTile());

        if((go != null && !go.isOnScreen()) || go == null){

            if(pathToGo != null)
                pathToGo.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return GameObjects.getNearest(gameObject) != null;
                }
            }, Random.nextInt(800,2109));

        } else if (go.isOnScreen()){
            setState(nextState);
        }
    }

    /**
     * Go to a certain ground item in an area, when found
     * it then changes the state of the quest.
     *
     * @param groundItem String of the ground item to go to
     * @param groundItemArea Area where the ground item might be
     * @param nextState String to which the state will change when the ground item has been found
     */
    static void goToGroundItem(final String groundItem, Area groundItemArea, String nextState){
        GroundItem gi = GroundItems.getNearest(groundItem);
        Path pathToGi = Walking.findPath(groundItemArea.getCentralTile());

        if((gi != null && !gi.isOnScreen()) || gi == null){

            if(pathToGi != null)
                pathToGi.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return GroundItems.getNearest(groundItem) != null;
                }
            }, Random.nextInt(800,2109));

        } else if (gi.isOnScreen()){
            setState(nextState);
        }
    }
}
