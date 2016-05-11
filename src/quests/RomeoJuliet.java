package quests;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.*;
import sun.rmi.runtime.Log;

/**
 * Class RomeoJuliet
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
public final class RomeoJuliet extends Quest {

    public static final Area cadavaBushArea = new Area(new Tile[]  {new Tile(3271,3369,0)});
    public static final Area romeoArea = new Area(new Tile[]  {new Tile(3210,3415,0),new Tile(3219,3415,0),new Tile(3219,3433,0),new Tile(3207,3433,0)});
    public static final Area julietArea = new Area(new Tile[]  {new Tile(3155,3425,1)});

    public static final int cadavaBerryId = 753;
    public static int talkCountRomeo = 0;
    public static int talkCountJuliet = 0;
    public static int talkCountChemist = 0;

    public static final String cadavaBushString = "Cadava bush";
    public static final String romeoString = "Romeo";
    public static final String julietString = "Juliet";

    public static boolean completed = false;

    public static int run(){

        if(!completed){

            switch (getState()){
                case "start":
                    setState("walkToBush");
                    break;
                case "walkToBush":
                    walkToBush();
                    break;
                case "pickBerry":
                    pickBerry();
                    break;
                case "walkToRomeo":
                    walkToRomeo();
                    break;
                case "findRomeo":
                    findRomeo();
                    break;
                case "talkToRomeo":
                    talkToRomeo();
                    break;
                case "walkToJuliet":
                    walkToJuliet();
                    break;
                case "findJuliet":
                    break;
                case "stop":
                    return -1;
            }
        }

        return Random.nextInt(800,1200);
    }

    private static void walkToJuliet() {
        goToNPC(julietString,julietArea,"findJuliet");
    }

    private static void talkToRomeo() {
        if(clickToContinue.isVisible()){
            clickToContinue.click();
            Time.sleep(800,1300);
        }else if(clickToContinue2.isVisible()){
            clickToContinue2.click();
            Time.sleep(800,1300);
        }else if(talkOptions.isValid()){
            WidgetChild talkOptionRomeo1 = Widgets.getWidgetByTextIncludingGrandChildren("Yes, I have seen her actually!");
            WidgetChild talkOptionRomeo2 = Widgets.getWidgetByTextIncludingGrandChildren("Yes, ok, I'll let her know.");
            WidgetChild talkOptionRomeo3 = Widgets.getWidgetByTextIncludingGrandChildren("Ok, thanks.");

            if(talkOptionRomeo1 != null)
                talkOptionRomeo1.click();

            if(talkOptionRomeo2 != null)
                talkOptionRomeo2.click();

            if(talkOptionRomeo3 != null){
                talkCountRomeo++;
                setState("walkToJuliet");
            }

            Time.sleep(500,1500);
        }else{
            LogHandler.log("all talking failed");
            setState("findRomeo");
        }
    }

    private static void findRomeo() {
        if(talkCountRomeo > 1 && talkCountJuliet == 0){
            setState("walkToJuliet");
        }else if(clickToContinue.isVisible() || clickToContinue2.isVisible() || talkOptions.isValid()){
            setState("talkToRomeo");
        }else{
            final NPC romeo = Npcs.getNearest(romeoString);
            if(romeo != null && romeo.isOnScreen()){
                Camera.turnTo(romeo);
                Time.sleep(100,1300);
                romeo.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return clickToContinue.isVisible();
                    }
                },Random.nextInt(976,2173));
            }else if(romeo != null && !romeo.isOnScreen()){
                Tile randomTile = romeoArea.getTileArray()[Random.nextInt(0,romeoArea.getTileArray().length)];
                Path path = Walking.findPath(randomTile);
                if(path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return romeo.isOnScreen();
                    }
                },Random.nextInt(712,2381));
            }else if(romeo == null){
                setState("walkToRomeo");
            }
        }
    }

    private static void pickBerry() {
        final GameObject bush = GameObjects.getNearest(cadavaBushString);
        if(bush != null && bush.isOnScreen()){
            bush.interact("Pick-from");

            if(lastText.containsText("More berries will grow soon.")){
                LogHandler.log("No berries... Swapping world");
                Game.instaHopNextF2P();
                LogHandler.log("Swapping world sleep");
                Time.sleep(Random.nextInt(10000,15000));
            }

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.contains(cadavaBerryId);
                }
            }, Random.nextInt(1273,3476));

            if(Inventory.contains(cadavaBerryId))
                setState("walkToRomeo");
            else
                Time.sleep(Random.nextInt(100,900));

        }else if(bush != null && !bush.isOnScreen()){
            Camera.turnTo(bush);

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return bush.isOnScreen();
                }
            }, Random.nextInt(1273,3476));
        } else{
            setState("walkToBush");
        }
    }

    public static void walkToRomeo(){
        goToNPC(romeoString,romeoArea,"findRomeo");
    }

    public static void walkToBush(){
        if(Inventory.contains(cadavaBerryId)){
            setState("walkToRomeo");
        } else{
            goToGameObject(cadavaBushString,cadavaBushArea,"pickBerry");
        }
    }
}
