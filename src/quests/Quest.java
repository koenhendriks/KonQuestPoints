package quests;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.*;
import sun.rmi.runtime.Log;

/**
 * Class Quest
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
abstract class Quest{

    /**
     * Widgets for quest interactions with NPC's
     */
    public static final WidgetChild gameSetting = Widgets.getWidget(162, 6);
    public static final WidgetChild text = Widgets.getWidget(162, 43);
    public static WidgetChild lastText = text.getChild(0);
    public static final WidgetChild clickToContinue = Widgets.getWidget(231,2);
    public static final WidgetChild clickToContinue2 = Widgets.getWidget(217,2);
    public static final WidgetChild clickToContinue3 = Widgets.getWidget(193,2);
    public static final Widget talkOptions = Widgets.getWidget(219);

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
     * Go to a certain NPC in an area, when found
     * it then changes the state of the quest.
     *
     * @param NPC String of the NPC to go to
     * @param NPCArea Area where the NPC might be
     * @param nextState String to which the state will change when the NPC has been found
     */
    static void goToNPC(final String NPC, Area NPCArea, final String nextState){
        NPC npcObject = Npcs.getNearest(NPC);
        Tile npcTile = null;

        if(npcObject != null){
            npcTile = npcObject.getLocation();
        }

        Path pathToGo = null;

        if(npcTile != null){
            pathToGo = Walking.findPath(npcTile);
        }else {
            pathToGo = Walking.findPath(NPCArea.getCentralTile());
        }


        if(((npcObject != null && !npcObject.isOnScreen()) || npcObject == null) && pathToGo != null){
            pathToGo.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Npcs.getNearest(NPC) != null;
                }
            }, Random.nextInt(1009,2376));

        } else if (npcObject != null && npcObject.distance() > 3 && pathToGo != null && pathToGo.getCost() < 66){
            if(npcTile != null){
                pathToGo = Walking.findPath(npcTile);
            }else {
                Tile randomTile = randomTileInArea(NPCArea);
                pathToGo = Walking.findPath(randomTile);
            }

            if(pathToGo != null)
                pathToGo.traverse();


            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    if(Npcs.getNearest(NPC).distance() < 3){
                        setState(nextState);
                        return true;
                    }
                    return false;
                }
            }, Random.nextInt(1009,2376));
        } else if (npcObject != null && npcObject.distance() < 3 && pathToGo != null && pathToGo.getCost() < 66){
            setState(nextState);
        }
    }

    /**
     * Go to a certain game object in an area, when found
     * it then changes the state of the quest.
     *
     * @param gameObject String of the game object to go to
     * @param gameObjectArea Area where the game object might be
     * @param nextState String to which the state will change when the game object has been found
     */
    static void goToGameObject(final String gameObject, Area gameObjectArea, final String nextState){
        GameObject go = GameObjects.getNearest(gameObject);
        final Path pathToGo = Walking.findPath(gameObjectArea.getCentralTile());

        if(((go != null && !go.isOnScreen()) || go == null) && pathToGo != null){

            pathToGo.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                     return GameObjects.getNearest(gameObject) != null;
                }
            }, Random.nextInt(1009,2376));

        } else if (go != null && go.distance() > 3 && pathToGo != null && pathToGo.getCost() < 66){
            Tile randomTile = randomTileInArea(gameObjectArea);
            Path path = Walking.findPath(randomTile);
            if(path != null)
                path.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    if(GameObjects.getNearest(gameObject).distance() < 3){
                        setState(nextState);
                        return true;
                    }
                    return false;
                }
            }, Random.nextInt(1009,2376));
        } else if (go != null && go.distance() < 3 && pathToGo != null && pathToGo.getCost() < 66){
            setState(nextState);
        }
    }

    /**
     * Go to a certain game object in an area, when found
     * it then changes the state of the quest.
     *
     * @param go String of the game object to go to
     * @param gameObjectArea Area where the game object might be
     * @param nextState String to which the state will change when the game object has been found
     */
    static void goToGameObject(final GameObject go, Area gameObjectArea, final String nextState){
        final Path pathToGo = Walking.findPath(gameObjectArea.getCentralTile());

        if(go != null && go.isOnScreen() && go.distance() < 3){
            setState(nextState);
        }else if(((go != null && !go.isOnScreen()) || go == null) && pathToGo != null){
            pathToGo.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return go != null;
                }
            }, Random.nextInt(1009,2376));

        } else if (go != null && go.distance() > 3 && pathToGo != null && pathToGo.getCost() < 66){
            Tile randomTile = randomTileInArea(gameObjectArea);
            Path path = Walking.findPath(randomTile);
            if(path != null)
                path.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    if(go.distance() < 3){
                        setState(nextState);
                        return true;
                    }
                    return false;
                }
            }, Random.nextInt(1009,2376));
        } else if (go != null && go.distance() < 3 && pathToGo != null && pathToGo.getCost() < 66){
            setState(nextState);
        } else {
            Path path  = Walking.findPath(gameObjectArea.getCentralTile());
            if(path != null)
                path.traverse();
        }

    }

    /**s
     * Go to a certain ground item in an area, when found
     * it then changes the state of the quest.
     *
     * @param groundItem String of the ground item to go to
     * @param groundItemArea Area where the ground item might be
     * @param nextState String to which the state will change when the ground item has been found
     */
    static void goToGroundItem(final String groundItem, Area groundItemArea, final String nextState){
        GroundItem gi = GroundItems.getNearest(groundItem);
        Path pathToGi = Walking.findPath(groundItemArea.getCentralTile());

        if(((gi != null && !gi.isOnScreen()) || gi == null) && pathToGi != null && pathToGi.getCost() < 66){

            pathToGi.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return GroundItems.getNearest(groundItem) != null;
                }
            }, Random.nextInt(1009,2376));

        } else if (gi != null && gi.distance() > 3 && pathToGi != null && pathToGi.getCost() < 66) {
            Tile randomTile = randomTileInArea(groundItemArea);
            Path path = Walking.findPath(randomTile);
            if(path != null)
                path.traverse();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    if(GroundItems.getNearest(groundItem).distance() < 3){
                        setState(nextState);
                        return true;
                    }
                    return false;
                }
            }, Random.nextInt(1009,2376));
        } else if (gi != null && gi.distance() < 3 && pathToGi != null && pathToGi.getCost() < 66){
            setState(nextState);
        }
    }

    static Tile randomTileInArea(Area area) {
        try{
            return area.getTileArray()[Random.nextInt(0,area.getTiles().length)];
        } catch (Exception ArrayIndexOutOfBoundsException){
            return area.getCentralTile();
        }
    }

    static void hopWorld(){
        Game.instaHopNextF2P();
        LogHandler.log("Swapping world sleep");
        Time.sleep(Random.nextInt(10000,15000));
    }

    /**
     * Get the floor that the player is currently on
     *
     * @return
     */
    static int getCurrentFloor(){
        String location = Players.getLocal().getLocation().toString();
        String floor = location.substring(location.length()-2,location.length()-1);
        return Integer.valueOf(floor);
    }

    static boolean isTalking(){
        return clickToContinue.isVisible() || clickToContinue2.isVisible() || clickToContinue3.isVisible() || talkOptions.isValid();
    }
}
