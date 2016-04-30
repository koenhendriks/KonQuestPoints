package quests;

import konquestpoints.AntiBan;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.internal.handlers.RandomHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.tabs.Quests;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.Area;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Tile;


/**
 * Class CooksAssistant
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
public final class CooksAssistant extends Quest {

    public static final Area cookArea = new Area(new Tile[]  {new Tile(3206,3217,0),new Tile(3206,3212,0),new Tile(3211,3213,0),new Tile(3211,3215,0)});
    public static final Area dairyCowArea = new Area(new Tile[]  {new Tile(3254,3281,0),new Tile(3256,3267,0),new Tile(3259,3269,0),new Tile(3257,3280,0)});
    public static final Area eggArea = new Area(new Tile[]  {new Tile(3233,3296,0),new Tile(3227,3295,0),new Tile(3228,3300,0),new Tile(3233,3300,0)});
    public static final Area wheatArea = new Area(new Tile[]  {new Tile(3155,3298,0),new Tile(3160,3297,0),new Tile(3158,3305,0),new Tile(3154,3304,0)});
    public static final Area windmillTopArea = new Area(new Tile[]  {new Tile(3165,3305,2),new Tile(3168,3305,2),new Tile(3168,3308,2),new Tile(3165,3308,2)});
    public static final Area windmillFloorArea = new Area(new Tile[]  {new Tile(3165,3305,0),new Tile(3165,3308,0),new Tile(3168,3308,0),new Tile(3168,3305,0)});

    public static final int cookId = 4626;
    public static final int potId = 1931;
    public static final int potOfFlourId = 1933;
    public static final int bucketId = 1925;
    public static final int bucketOfMilkId = 1927;
    public static final int eggId = 1944;
    public static final int wheatId = 1947;

    public static final String dairyCowString = "Dairy cow";



    public static final boolean completed = false;

    public boolean completed() {
        return Quests.isCompleted("Cook's Assistant");
    }

    public static int run(){

        if(!completed){

            switch (getState()){
                case "start":
                    getItems();
                    break;
                case "walkToCow":
                    goToCow();
                    break;
                case "milk":
                    milkCow();
                    break;
                case "walkToWheat":
                    walkToWheat();
                case "stop":
                    return -1;
            }
        }


        return 500;
    }

    private static void walkToWheat(){

    }

    private static void milkCow() {
        if(Inventory.contains(bucketOfMilkId)){
            setState("walkToWheat");
        } else {
            GameObject dairyCow = GameObjects.getNearest(dairyCowString);

            dairyCow.interact("Milk");

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.contains(bucketOfMilkId);
                }
            }, Random.nextInt(4241,5333));
        }


    }

    private static void goToCow() {

        GameObject dairyCow = GameObjects.getNearest(dairyCowString);
        Path pathToCow = Walking.findPath(dairyCowArea.getCentralTile());

        if((dairyCow != null && !dairyCow.isOnScreen()) || dairyCow == null){
            pathToCow.traverse();
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return GameObjects.getNearest(dairyCowString) != null;
                }
            }, Random.nextInt(800,2109));
        } else if (dairyCow.isOnScreen()){
            setState("milk");
        }
    }

    private static void getItems() {
        if (!haveStartItems()) {

            if(Bank.isOpen()){
                LogHandler.log("grab from bank");

                if(Bank.contains(potId) && Bank.contains(bucketId)) {
                    Bank.withdraw(potId, 1);
                    Bank.withdraw(bucketId, 1);
                } else {
                    LogHandler.log("Don't have the items for the quests.");
                    setState("stop");
                }
            } else {
                LogHandler.log("going and opening bank");
                Bank.openNearestBank();
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return Bank.isOpen();
                    }
                }, Random.nextInt(1236,2482));
            }

        } else {
            setState("walkToCow");
        }
    }

    private static boolean haveStartItems() {
        return Inventory.contains(potId) && Inventory.contains(bucketId);
    }


}
