package quests;

import konquestpoints.MainHandler;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Quests;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.util.Filter;
import org.tbot.wrappers.*;

/**
 * Class SheepShearer
 *
 * @author Koen Hendriks
 * @version 0.1 (20-05-2016)
 */
public class SheepShearer extends Quest{

    public static final Area fredArea = new Area(new Tile[]  {new Tile(3185,3277,0),new Tile(3185,3279,0),new Tile(3191,3278,0),new Tile(3191,3276,0),new Tile(3191,3274,0),new Tile(3191,3270,0),new Tile(3189,3271,0),new Tile(3188,3272,0),new Tile(3188,3274,0)});

    public static final String fredString = "Fred the Farmer";
    public static final String questString = "Sheep Shearer";


    public static boolean talkedToFred = true;
    public static boolean completed = false;

    public static int run(){

        if(!completed){
            switch (getState()){
                case "start":
                    if(Quests.isCompleted(questString)){
                        setState("stop");
                    }else{
                        setState("walkToFred");
                    }
                    break;
                case "walkToFred":
                    walkToFred();
                    break;
                case "findFred":
                    findFred();
                    break;
                case "talkToFred":
                    talkToFred();
                    break;
                case "stop":
                    if(Quests.isCompleted(questString)){
                        LogHandler.log("Finished " +questString+" Quest");
                        MainHandler.completedSheepShearer = true;
                    }
                    setState("start");
                    break;
            }
        }

        return Random.nextInt(800,1200);
    }

    private static void talkToFred() {
        if(!isTalking()){
            setState("findFred");
        }else if(clickToContinue.isVisible()){
            clickToContinue.click();
            Time.sleep(800,1300);
        }else if(clickToContinue2.isVisible()){
            clickToContinue2.click();
            Time.sleep(800,1300);
        }else if(clickToContinue3.isVisible()){
            clickToContinue3.click();
            Time.sleep(800,1300);
        }else if(talkOptions.isValid()) {
            WidgetChild talkOptionFred1 = Widgets.getWidgetByTextIncludingGrandChildren("I'm looking for a quest.");
            WidgetChild talkOptionFred2 = Widgets.getWidgetByTextIncludingGrandChildren("Yes okay. I can do that.");
            WidgetChild talkOptionFred3 = Widgets.getWidgetByTextIncludingGrandChildren("Of course");

            if (talkOptionFred1 != null) {
                talkOptionFred1.click();
                Time.sleep(800,1300);
            }

            if(talkOptionFred2 != null){
                talkOptionFred2.click();
                Time.sleep(800,1300);
            }

            if(talkOptionFred3 != null){
                setState("getTool");
            }

        }
    }

    private static void findFred() {
        if(isTalking()){
            setState("talkToFred");
        }else{
            GameObject gate = GameObjects.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    return gameObject.getLocation().getX() == 3189 && gameObject.getLocation().getY() == 3275;
                }
            });

            GameObject door = GameObjects.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    return gameObject.getLocation().getX() == 3188 && gameObject.getLocation().getY() == 3279;
                }
            });

            if(gate != null && gate.hasAction("Open")){
                gate.interact("Open");
                Time.sleep(234,1238);
            }

            if(door != null && door.hasAction("Open")){
                Time.sleep(345,1998);
                door.interact("Open");
            }

            final NPC fred = Npcs.getNearest(fredString);
            if(fred != null && fred.isOnScreen()){
                Camera.turnTo(fred);
                Time.sleep(100,1300);
                fred.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return clickToContinue.isVisible();
                    }
                },Random.nextInt(976,2173));
            }else if(fred != null && !fred.isOnScreen()){
                Tile randomTile = randomTileInArea(fredArea);
                Path path = Walking.findPath(randomTile);
                if(path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return fred.isOnScreen();
                    }
                },Random.nextInt(712,2381));
            }else if(fred == null){
                setState("walkToFred");
            }

        }
    }

    private static void walkToFred() {
        if(!isTalking())
            goToNPC(fredString,fredArea,"findFred");
        else
            setState("talkToFred");
    }
}
