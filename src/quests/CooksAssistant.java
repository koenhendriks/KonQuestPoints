package quests;

import konquestpoints.AntiBan;
import konquestpoints.MainHandler;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.tabs.Quests;
import org.tbot.util.Condition;
import org.tbot.wrappers.*;


/**
 * Class CooksAssistant
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
public final class CooksAssistant extends Quest {

    private static final Area cookArea = new Area(new Tile[]{new Tile(3206, 3217, 0), new Tile(3206, 3212, 0), new Tile(3211, 3213, 0), new Tile(3211, 3215, 0)});
    private static final Area dairyCowArea = new Area(new Tile[]{new Tile(3254, 3281, 0), new Tile(3256, 3267, 0), new Tile(3259, 3269, 0), new Tile(3257, 3280, 0)});
    private static final Area eggArea = new Area(new Tile[]{new Tile(3233, 3296, 0), new Tile(3227, 3295, 0), new Tile(3228, 3300, 0), new Tile(3233, 3300, 0)});
    private static final Area wheatArea = new Area(new Tile[]{new Tile(3155, 3298, 0), new Tile(3160, 3297, 0), new Tile(3158, 3305, 0), new Tile(3154, 3304, 0)});
    private static final Area windmillArea = new Area(new Tile[]{new Tile(3166, 3308, 0)});

    private static final int potId = 1931;
    private static final int potOfFlourId = 1933;
    private static final int bucketId = 1925;
    private static final int bucketOfMilkId = 1927;
    private static final int grainId = 1947;

    private static final String eggString = "Egg";
    private static final String wheatString = "Wheat";
    private static final String dairyCowString = "Dairy cow";
    private static final String hopperString = "Hopper";
    private static final String hopperControlsString = "Hopper controls";
    private static final String flourBinString = "Flour bin";
    private static final String ladderString = "ladder";
    private static final String cookString = "Cook";
    private static final String questString = "Cook's Assistant";

    private static boolean operatedHopper = false;
    private static boolean usedHopper = false;


    private static boolean completed = false;

    public static int run() {
        if (!completed) {

            setActiveQuest(questString);

            switch (getState()) {
                case "start":
                    if (Quests.isCompleted(questString)) {
                        setState("stop");
                    } else {
                        getItems();
                    }
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
                    talkToCook();
                    break;
                case "stop":
                    if (Quests.isCompleted(questString)) {
                        LogHandler.log("Finished Cooks Assistant Quest");
                        MainHandler.completedCooksAssistant = true;
                    }
                    setState("start");
                    break;
            }
        } else {
            LogHandler.log("Finished Cooks Assistant Quest");
            MainHandler.completedCooksAssistant = true;
        }

        return Random.nextInt(800, 1200);
    }

    private static void talkToCook() {
        if (Quests.isCompleted(questString)) {
            completed = true;
            setState("stop");
        } else {

            setAction("Talking to cook and finishing quest.");

            WidgetChild talk1 = Widgets.getWidgetByTextIncludingGrandChildren("What's wrong?");
            WidgetChild talk2 = Widgets.getWidgetByTextIncludingGrandChildren("I'm always happy to help a cook in distress.");

            if (clickToContinue.containsText("Click here to continue")) {
                clickToContinue.click();
                Time.sleep(1000, 3000);
            } else if (clickToContinue2.containsText("Click here to continue")) {
                clickToContinue2.click();
                Time.sleep(1000, 3000);
            } else if (talk2 != null) {
                talk2.click();
                Time.sleep(1000, 3000);
            } else if (talk1 != null) {
                talk1.click();
                Time.sleep(1000, 3000);
            } else {

                final NPC cook = Npcs.getNearest(cookString);
                if (cook != null && cook.isOnScreen()) {
                    cook.interact("Talk-to");


                } else if (cook != null && !cook.isOnScreen()) {
                    Camera.turnTo(cook);
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return cook.isOnScreen();
                        }
                    }, Random.nextInt(1200, 2400));
                } else if (cook == null) {
                    setState("walkToCook");
                }
            }
        }

    }

    private static void getFlour() {

        if (Inventory.contains(potOfFlourId)) {
            setState("walkToCook");
        } else {
            setAction("Getting Flour");
            final GameObject flourBin = GameObjects.getNearest(flourBinString);

            if (flourBin != null && flourBin.isOnScreen()) {
                if (flourBin.hasAction("Empty")) {
                    LogHandler.log("Empty Flour bin");
                    flourBin.interact("Empty");

                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return Inventory.contains(potOfFlourId);
                        }
                    }, Random.nextInt(2573, 3871));

                    setState("walkToCook");
                }
            } else if (flourBin != null && !flourBin.isOnScreen()) {
                Camera.turnTo(flourBin);
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return flourBin.isOnScreen();
                    }
                }, Random.nextInt(2573, 3871));
            } else if (flourBin == null) {
                setState("walkToWindMill");
            }
        }
    }

    private static void climbDownWindMill() {
        if (getCurrentFloor() == 0) {
            setState("getFlour");
        } else {
            setAction("Climbing down windmill");
            GameObject ladder = GameObjects.getNearest(ladderString);
            if (ladder != null && !ladder.isOnScreen()) {
                Camera.turnTo(ladder);
            } else if (ladder != null && ladder.isOnScreen()) {
                ladder.interact("Climb-down");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return getCurrentFloor() == 0;
                    }
                }, Random.nextInt(900, 1500));
            }
        }
    }

    private static void makeFlour() {
        if (Inventory.contains(potOfFlourId)) {
            setState("walkToCook");
        } else if (!Inventory.contains(grainId) && !operatedHopper && !usedHopper) {
            setState("climbDownWindMill");
        } else {
            setAction("Making flour");
            GameObject hopper = GameObjects.getNearest(hopperString);
            GameObject hopperControls = GameObjects.getNearest(hopperControlsString);
            Item grain = Inventory.getFirst(grainId);

            if (hopper != null && hopperControls != null && grain != null && !usedHopper) {
                LogHandler.log("using grain with hopper!");
                grain.interact("Use");

                Time.sleep(Random.nextInt(100, 500));

                hopper.interact("Use grain -> Hopper");

                Time.sleep(Random.nextInt(3000, 5000));

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        if (lastText == null) {
                            WidgetChild text = Widgets.getWidget(162, 43);
                            lastText = text.getChild(0);
                        }

                        if (lastText.containsText("You put the grain in the hopper.") || lastText.containsText("There is already grain in the hopper.")) {
                            usedHopper = true;
                            return true;
                        }
                        return false;
                    }
                }, Random.nextInt(500, 2300));

            } else if (hopper != null && hopperControls != null && usedHopper && !operatedHopper) {

                LogHandler.log("Operating hopper!");

                hopperControls.interact("Operate");

                Time.sleep(Random.nextInt(3000, 5000));

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        if (lastText.containsText("You operate the hopper. The grain slides down the chute.")) {
                            operatedHopper = true;
                            return true;
                        }
                        return false;
                    }
                }, Random.nextInt(500, 2000));

            } else if (usedHopper && operatedHopper) {
                setState("climbDownWindMill");
            }
        }
    }

    private static void climbWindMill() {
        if (Inventory.contains(potOfFlourId)) {
            setState("walkToCook");
        } else {
            setAction("Climbing the windmill");
            if (getCurrentFloor() == 2) {
                setState("makeFlour");
            } else {
                GameObject ladder = GameObjects.getNearest(ladderString);
                if (ladder != null && !ladder.isOnScreen()) {
                    Camera.turnTo(ladder);
                } else if (ladder != null && ladder.isOnScreen()) {
                    ladder.interact("Climb-up");
                    Time.sleepUntil(new Condition() {
                        @Override
                        public boolean check() {
                            return getCurrentFloor() == 2;
                        }
                    }, Random.nextInt(900, 1500));
                }
            }
        }
    }

    private static void pickWheat() {
        if (Inventory.contains(grainId) || Inventory.contains(potOfFlourId)) {
            setState("walkToWindMill");
        } else {
            setAction("Pick wheat from the field");
            GameObject wheat = GameObjects.getNearest(wheatString);

            wheat.interact("Pick");

            if(AntiBan.oneIn(2)){
                if(!Inventory.isOpen()){
                    Inventory.openTab();
                    Time.sleep(800,1200);
                }
            }

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.contains(grainId);
                }
            }, Random.nextInt(1937, 3265));
        }
    }

    private static void grabEgg() {
        if (Inventory.contains(eggString)) {
            setState("walkToWheat");
        } else {
            setAction("Getting an egg.");
            GroundItem egg = GroundItems.getNearest(eggString);
            if (egg != null) {
                egg.pickUp();

                if(AntiBan.oneIn(2)){
                    if(!Inventory.isOpen()){
                        Inventory.openTab();
                        Time.sleep(800,1200);
                    }
                }
            }else {
                setState("walkToEgg");
            }

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.contains(eggString);
                }
            }, Random.nextInt(1322, 3498));

        }
    }

    private static void milkCow() {
        if (Inventory.contains(bucketOfMilkId)) {
            setState("walkToEgg");
        } else {
            setAction("Milking a cow");
            GameObject dairyCow = GameObjects.getNearest(dairyCowString);

            dairyCow.interact("Milk");

            if(AntiBan.oneIn(2)){
                if(!Inventory.isOpen()){
                    Inventory.openTab();
                    Time.sleep(800,1200);
                }
            }

            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.contains(bucketOfMilkId);
                }
            }, Random.nextInt(4241, 5333));
        }
    }

    private static void getItems() {

        setAction("Getting items for the quest");
        if (Bank.isOpen()) {

            Bank.depositAll();

            Time.sleep(Random.nextInt(200, 500), Random.nextInt(1200, 1900));

            LogHandler.log("grab from bank");

            if (Bank.contains(potId) && Bank.contains(bucketId)) {
                Bank.withdraw(potId, 1);
                Bank.withdraw(bucketId, 1);
                setState("walkToCow");
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
            }, Random.nextInt(1236, 2482));
        }
    }

    private static void walkToCook() {
        if (Inventory.contains(eggString) && Inventory.contains(bucketOfMilkId) && Inventory.contains(potOfFlourId)) {
            setAction("Walking to the cook");
            goToNPC(cookString, cookArea, "talkToCook");
        }
    }

    private static void walkToWindMill() {
        if (!Inventory.contains(potOfFlourId)) {
            setAction("Walking to the windmill");
            goToGameObject(flourBinString, windmillArea, "climbWindMill");
        } else {
            setState("climbWindMill");
        }
    }

    private static void walkToWheat() {
        if (!Inventory.contains(grainId) && !Inventory.contains(potOfFlourId)) {
            setAction("Climbing");
            goToGameObject(wheatString, wheatArea, "pickWheat");
        } else {
            setState("pickWheat");
        }
    }

    private static void walkToEgg() {
        if (!Inventory.contains(eggString)) {
            setAction("Walking to the egg");
            goToGroundItem(eggString, eggArea, "grabEgg");
        } else {
            setState("grabEgg");
        }
    }

    private static void walkToCow() {
        if (!Inventory.contains(bucketOfMilkId)) {
            setAction("Walking to dairy cow");
            goToGameObject(dairyCowString, dairyCowArea, "milk");
        } else {
            setState("walkToEgg");
        }
    }
}
