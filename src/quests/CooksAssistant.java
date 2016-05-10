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
import org.tbot.wrappers.*;

import java.util.Objects;


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
    public static final Area windmillArea = new Area(new Tile[]  {new Tile(3166,3308,0)});

    public static final int potId = 1931;
    public static final int potOfFlourId = 1933;
    public static final int bucketId = 1925;
    public static final int bucketOfMilkId = 1927;
    public static final int grainId = 1947;

    public static final WidgetChild gameSetting = Widgets.getWidget(162, 6);
    public static final WidgetChild text = Widgets.getWidget(162, 43);
    public static final WidgetChild lastText = text.getChild(0);

    public static final String eggString = "Egg";
    public static final String wheatString = "Wheat";
    public static final String dairyCowString = "Dairy cow";
    public static final String hopperString = "Hopper";
    public static final String hopperControlsString = "Hopper controls";
    public static final String flourBinString = "Flour bin";
    public static final String ladderString = "ladder";
    public static final String cookString = "Cook";

    public static boolean operatedHopper = false;
    public static boolean usedHopper = false;


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
                    walkToCow();
                    break;
                case "milk":
                    milkCow();
                    break;
                case "walkToWheat":
                    walkToWheat();
                    break;
                case "pickWheat":
                    pickWheat();
                    break;
                case "walkToEgg":
                    walkToEgg();
                    break;
                case "grabEgg":
                    grabEgg();
                    break;
                case "walkToWindMill":
                    walkToWindMill();
                    break;
                case "climbWindMill":
                    climbWindMill();
                    break;
                case "climbDownWindMill":
                    climbDownWindMill();
                    break;
                case "makeFlour":
                    makeFlour();
                    break;
                case "getFlour":
                    getFlour();
                    break;
                case "walkToCook":
                    walkToCook();
                    break;
                case "talkToCook":
                    break;
                case "stop":
                    return -1;
            }
        }

        return Random.nextInt(800,1200);
    }

    private static void getFlour(){

        if(Inventory.contains(potOfFlourId)){
            setState("walkToCook");
        }else{
            final GameObject flourBin = GameObjects.getNearest(flourBinString);

            if(flourBin != null && flourBin.isOnScreen()){
                if(flourBin.hasAction("Empty")){
                    LogHandler.log("Empty Flour bin");
                    flourBin.interact("Empty");

                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return Inventory.contains(potOfFlourId);
                        }
                    }, Random.nextInt(2573,3871));

                    setState("walkToCook");
                }
            } else if(flourBin != null && !flourBin.isOnScreen()){
                Camera.turnTo(flourBin);
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return flourBin.isOnScreen();
                    }
                }, Random.nextInt(2573,3871));
            } else if(flourBin == null){
                setState("walkToWindMill");
            }
        }
    }

    private static void climbDownWindMill(){
        if(getCurrentFloor() == 0) {
            setState("getFlour");
        } else {
            GameObject ladder = GameObjects.getNearest(ladderString);
            if(ladder != null && !ladder.isOnScreen()){
                Camera.turnTo(ladder);
            } else if(ladder != null && ladder.isOnScreen()){
                ladder.interact("Climb-down");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return getCurrentFloor() == 0;
                    }
                }, Random.nextInt(900,1500));
            }
        }
    }

    private static void makeFlour(){
        if(Inventory.contains(potOfFlourId)) {
            setState("walkToCook");
        } else if(!Inventory.contains(grainId) && !operatedHopper && !usedHopper){
            setState("climbDownWindMill");
        } else {

            GameObject hopper = GameObjects.getNearest(hopperString);
            GameObject hopperControls = GameObjects.getNearest(hopperControlsString);
            Item grain = Inventory.getFirst(grainId);

            if(hopper != null && hopperControls != null && grain != null && !usedHopper) {
                LogHandler.log("using grain with hopper!");
                grain.interact("Use");

                Time.sleep(Random.nextInt(100, 500));

                hopper.interact("Use grain -> Hopper");

                Time.sleep(Random.nextInt(3000, 5000));

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        if (lastText.containsText("You put the grain in the hopper.") || lastText.containsText("There is already grain in the hopper.")) {
                            usedHopper = true;
                            return true;
                        }
                        return false;
                    }
                }, Random.nextInt(500,2300));

            } else if(hopper != null && hopperControls != null && usedHopper && !operatedHopper) {

                LogHandler.log("Operating hopper!");

                hopperControls.interact("Operate");

                Time.sleep(Random.nextInt(3000, 5000));

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        if(lastText.containsText("You operate the hopper. The grain slides down the chute.")) {
                            operatedHopper = true;
                            return true;
                        }
                        return false;
                    }
                },Random.nextInt(500,2000));

            } else if(usedHopper && operatedHopper) {
                setState("climbDownWindMill");
            }
        }
    }

    private static void climbWindMill(){
        if(Inventory.contains(potOfFlourId)){
            setState("walkToCook");
        } else {
            if(getCurrentFloor() == 2) {
                setState("makeFlour");
            } else {
                GameObject ladder = GameObjects.getNearest(ladderString);
                if(ladder != null && !ladder.isOnScreen()){
                    Camera.turnTo(ladder);
                } else if(ladder != null && ladder.isOnScreen()){
                    ladder.interact("Climb-up");
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return getCurrentFloor() == 2;
                        }
                    }, Random.nextInt(900,1500));
                }
            }
        }
    }

    private static void pickWheat(){
        if(Inventory.contains(grainId) || Inventory.contains(potOfFlourId)){
            setState("walkToWindMill");
        } else {
            GameObject wheat = GameObjects.getNearest(wheatString);

            wheat.interact("Pick");

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.contains(grainId);
                }
            }, Random.nextInt(1937,3265));
        }
    }

    private static void grabEgg(){
        if(Inventory.contains(eggString)){
            setState("walkToWheat");
        } else {
            GroundItem egg = GroundItems.getNearest(eggString);

            egg.pickUp();

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.contains(eggString);
                }
            }, Random.nextInt(1322,3498));

        }
    }

    private static void milkCow() {
        if(Inventory.contains(bucketOfMilkId)){
            setState("walkToEgg");
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

    private static void walkToCook(){
        if(Inventory.contains(eggString) && Inventory.contains(bucketOfMilkId) && Inventory.contains(potOfFlourId)){
            goToNPC(cookString,cookArea,"talkToCook");
        }
    }

    private static void walkToWindMill(){
        if(!Inventory.contains(potOfFlourId))
            goToGameObject(flourBinString, windmillArea, "climbWindMill");
        else
            setState("climbWindMill");
    }

    private static void walkToWheat(){
        if(!Inventory.contains(grainId) && !Inventory.contains(potOfFlourId))
            goToGameObject(wheatString, wheatArea, "pickWheat");
        else
            setState("pickWheat");
    }

    private static void walkToEgg(){
        if(!Inventory.contains(eggString))
            goToGroundItem(eggString, eggArea, "grabEgg");
        else
            setState("grabEgg");
    }

    private static void walkToCow() {
        if(!Inventory.contains(bucketOfMilkId))
            goToGameObject(dairyCowString,dairyCowArea,"milk");
        else
            setState("walkToEgg");
    }

    private static boolean haveStartItems() {
        return ((Inventory.contains(potId) && Inventory.contains(bucketId)) || (Inventory.contains(potId) && Inventory.contains(bucketOfMilkId)) || (Inventory.contains(potOfFlourId) && Inventory.contains(bucketId)) || (Inventory.contains(potOfFlourId) && Inventory.contains(bucketOfMilkId)));
    }




}
