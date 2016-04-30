package quests;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Bank;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.tabs.Quests;
import org.tbot.util.Condition;
import org.tbot.wrappers.Area;
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
    public static final int dairyCowId = 2691;
    public static final int eggId = 1944;
    public static final int wheatId = 1947;


    public static final boolean completed = false;

    public boolean completed() {
        if(Quests.isCompleted("Cook's Assistant"))
            return true;
        return false;
    }

    public static void run(){
        if(!completed){
            LogHandler.log("Starting cook");

            getItems();
        }
    }

    private static void getItems() {
        LogHandler.log("Item check");
        if (!haveStartItems()) {

            if(Bank.isOpen()){
                LogHandler.log("grab from bank");
                Bank.withdraw(potId,1);
                Bank.withdraw(bucketId,1);
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
            LogHandler.log("i have start items");
        }
    }

    private static boolean haveStartItems() {
        return Inventory.contains(potId) && Inventory.contains(bucketId);
    }


}
