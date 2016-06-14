package quests;

import konquestpoints.MainHandler;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.tabs.Quests;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.util.Filter;
import org.tbot.wrappers.*;

/**
 * Class RomeoJuliet
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
public final class RomeoJuliet extends Quest {

    private static final Area fatherLawrenceArea = new Area(new Tile[]{new Tile(3252, 3482, 0), new Tile(3259, 3482, 0), new Tile(3255, 3484, 0), new Tile(3255, 3487, 0), new Tile(3254, 3488, 0), new Tile(3253, 3488, 0), new Tile(3252, 3487, 0)});
    private static final Area cadavaBushArea = new Area(new Tile[]{new Tile(3271, 3369, 0)});
    private static final Area romeoArea = new Area(new Tile[]{new Tile(3210, 3415, 0), new Tile(3219, 3415, 0), new Tile(3219, 3433, 0), new Tile(3207, 3433, 0)});
    private static final Area julietArea = new Area(new Tile[]{new Tile(3155, 3425, 1)});
    private static final Area julietHouse = new Area(new Tile[]{new Tile(3156, 3434, 0)});
    private static final Area julietHouseUp = new Area(new Tile[]{new Tile(3155, 3435, 1)});
    private static final Area apothecaryArea = new Area(new Tile[]{new Tile(3197, 3404, 0)});


    private static final int cadavaBerryId = 753;
    private static final int messageId = 755;
    private static final int potionId = 756;
    private static final String cadavaBushString = "Cadava bush";
    private static final String romeoString = "Romeo";
    private static final String julietString = "Juliet";
    private static final String staircaseString = "Staircase";
    private static final String fatherLawrenceString = "Father Lawrence";
    private static final String apothecaryString = "apothecary";
    private static final String questString = "Romeo & Juliet";
    private static int talkCountRomeo = 0;
    private static int talkCountJuliet = 0;
    private static boolean lastRomeoTalk = false;

    public static int run() {

        if (!MainHandler.completedRomeoJuliet) {

            setActiveQuest(questString);

            switch (getState()) {
                case "start":
                    if (Quests.isCompleted(questString)) {
                        setState("stop");
                    } else {
                        setState("walkToApo");
                    }
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
                case "walkToJulietHouse":
                    walkToJulietHouse();
                    break;
                case "climbHouseUp":
                    climbHouseUp();
                    break;
                case "climbHouseDown":
                    climbHouseDown();
                    break;
                case "findJuliet":
                    findJuliet();
                    break;
                case "talkToJuliet":
                    talkToJuliet();
                    break;
                case "walkToFatherLawrence":
                    walkToFatherLawrence();
                    break;
                case "findFatherLawrence":
                    findFatherLawrence();
                    break;
                case "talkToFatherLawrence":
                    talkToFatherLawrence();
                    break;
                case "walkToApo":
                    walkToApo();
                    break;
                case "findApo":
                    findApo();
                    break;
                case "talkToApo":
                    talkToApo();
                    break;
                case "stop":
                    if (Quests.isCompleted(questString)) {
                        LogHandler.log("Finished Romeo & Juliet Quest");
                        MainHandler.completedRomeoJuliet = true;
                    }
                    setState("start");
                    break;
            }
        }

        return Random.nextInt(800, 1200);
    }

    private static void talkToApo() {
        if (Inventory.contains(potionId)) {
            setState("walkToJulietHouse");
        } else if (clickToContinue.isVisible()) {
            setAction("Talking with Apo for potion");
            clickToContinue.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue2.isVisible()) {
            setAction("Talking with Apo for potion");
            clickToContinue2.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue3.isVisible()) {
            setAction("Talking with Apo for potion");
            clickToContinue3.click();
            Time.sleep(800, 1300);
        } else {
            setAction("Talking with Apo for potion");
            WidgetChild talk = Widgets.getWidgetByTextIncludingGrandChildren("Click here to continue");
            if (talk != null) {
                talk.click();
                Time.sleep(800, 1400);
            } else if (Inventory.contains(potionId)) {
                setState("walkToJulietHouse");
            } else {
                LogHandler.log("looking for widget");
                WidgetChild endTalk = Widgets.getWidgetByTextIncludingGrandChildren("Ok, thanks.");
                if (endTalk != null && endTalk.isValid() && endTalk.isVisible()) {
                    endTalk.click();
                    Time.sleep(1200,3420);
                    if (!Inventory.contains(cadavaBerryId) && !Inventory.contains(potionId))
                        setState("walkToBush");
                    else if (Inventory.contains(potionId))
                        setState("walkToJulietHouse");
                    else
                        setState("walkToApo");
                } else {
                    if (!Inventory.contains(cadavaBerryId) && !Inventory.contains(potionId))
                        setState("walkToBush");
                    else if (Inventory.contains(potionId))
                        setState("walkToJulietHouse");
                    else {
                        Time.sleep(2200,4300);
                        setState("walkToApo");
                    }
                }
            }
        }
    }

    private static void findApo() {
        if(isTalking()){
            setState("talkToApo");
        }else{
            setAction("Looking for Apo");
            final NPC apo = Npcs.getNearest(apothecaryString);
            if (apo != null && apo.isOnScreen()) {
                Camera.turnTo(apo);
                Time.sleep(100, 1300);
                apo.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        if (isTalking()) {
                            setState("talkToApo");
                            return true;
                        }
                        return false;
                    }
                }, Random.nextInt(976, 2173));
            } else if (apo != null && !apo.isOnScreen()) {
                Tile randomTile = randomTileInArea(apothecaryArea);
                Path path = Walking.findPath(randomTile);
                if (path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return apo.isOnScreen();
                    }
                }, Random.nextInt(712, 2381));
            } else if (apo == null) {
                setState("walkToApo");
            }
        }
    }

    private static void walkToApo() {
        setAction("Walking to Apo");
        goToNPC(apothecaryString, apothecaryArea, "findApo");
    }

    private static void talkToFatherLawrence() {
        setAction("Talk to Father Lawrence");
        if (clickToContinue.isVisible()) {
            clickToContinue.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue2.isVisible()) {
            clickToContinue2.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue3.isVisible()) {
            clickToContinue3.click();
            Time.sleep(800, 1300);
        } else {
            LogHandler.log("Looking for widget");
            WidgetChild talk = Widgets.getWidgetByTextIncludingGrandChildren("Click here to continue");
            if (talk != null) {
                talk.click();
                Time.sleep(800, 1400);
            } else {
                setState("walkToApo");
            }
        }
    }

    private static void findFatherLawrence() {
        if (isTalking()) {
            setState("talkToFatherLawrence");
        } else {
            setAction("Looking for Father Lawrence");
            final NPC fatherLawrence = Npcs.getNearest(fatherLawrenceString);
            if (fatherLawrence != null && fatherLawrence.isOnScreen()) {
                Camera.turnTo(fatherLawrence);
                Time.sleep(100, 1300);
                fatherLawrence.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return isTalking();
                    }
                }, Random.nextInt(976, 2173));
            } else if (fatherLawrence != null && !fatherLawrence.isOnScreen()) {
                Tile randomTile = randomTileInArea(fatherLawrenceArea);
                Path path = Walking.findPath(randomTile);
                if (path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return fatherLawrence.isOnScreen();
                    }
                }, Random.nextInt(712, 2381));
            } else if (fatherLawrence == null) {
                setState("walkToFatherLawrence");
            }
        }
    }

    private static void walkToFatherLawrence() {
        setAction("Walking to Father Lawrence");
        goToNPC(fatherLawrenceString, fatherLawrenceArea, "findFatherLawrence");
    }

    private static void findJuliet() {
        setAction("Looking for Juliet");
        final GameObject firstDoor = GameObjects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getLocation().getX() == 3157 && gameObject.getLocation().getY() == 3430;
            }
        });

        final GameObject secondDoor = GameObjects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getLocation().getX() == 3158 && gameObject.getLocation().getY() == 3426;
            }
        });

        final NPC juliet = Npcs.getNearest(julietString);

        if (isTalking()) {
            setState("talkToJuliet");
        } else if (juliet != null && juliet.isOnScreen() && firstDoor == null && secondDoor == null) {
            LogHandler.log("Looking for julliet");
            juliet.interact("Talk-to");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return clickToContinue.isVisible();
                }
            }, Random.nextInt(976, 2173));
        } else if (firstDoor != null && firstDoor.hasAction("Open")) {
            Camera.turnTo(firstDoor);
            LogHandler.log("opening first door");
            firstDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return firstDoor.hasAction("Close");
                }
            }, Random.nextInt(900, 1400));

        } else if (secondDoor != null && secondDoor.hasAction("Open")) {
            Camera.turnTo(secondDoor);
            if (!secondDoor.isOnScreen()) {
                LogHandler.log("Walking to door");
                Path path = Walking.findPath(secondDoor.getLocation());
                if (path != null)
                    path.traverse();
            }
            LogHandler.log("opening second door");

            secondDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return secondDoor.hasAction("Close");
                }
            }, Random.nextInt(900, 1400));
        } else if (secondDoor == null && firstDoor == null) {
            Walking.walkTileMM(julietArea.getCentralTile());
            Time.sleep(900, 1400);
        }
    }

    private static void climbHouseDown() {
        setAction("Going down Juliets house");
        final GameObject firstDoor = GameObjects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getLocation().getX() == 3157 && gameObject.getLocation().getY() == 3430;
            }
        });

        final GameObject secondDoor = GameObjects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getLocation().getX() == 3158 && gameObject.getLocation().getY() == 3426;
            }
        });

        GameObject staircase = GameObjects.getNearest(staircaseString);

        if (getCurrentFloor() == 0) {
            setState("walkToRomeo");
        } else if (staircase != null && staircase.isOnScreen() && firstDoor == null && secondDoor == null) {
            LogHandler.log("Looking for staircase");
            staircase.interact("Climb-down");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return getCurrentFloor() == 0;
                }
            }, Random.nextInt(976, 2173));
        } else if (secondDoor != null && secondDoor.hasAction("Open")) {
            LogHandler.log("opening second door");
            Camera.turnTo(secondDoor);
            secondDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return secondDoor.hasAction("Close");
                }
            }, Random.nextInt(900, 1400));
        } else if (firstDoor != null && firstDoor.hasAction("Open")) {
            if (!firstDoor.isOnScreen()) {
                LogHandler.log("Walking to door");
                Path path = Walking.findPath(firstDoor.getLocation());
                if (path != null)
                    path.traverse();
            }

            LogHandler.log("opening first door");
            firstDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return firstDoor.hasAction("Close");
                }
            }, Random.nextInt(900, 1400));

        } else if (secondDoor == null && firstDoor == null) {
            Walking.walkTileMM(julietHouseUp.getCentralTile());
            Time.sleep(900, 1400);
        }


    }

    private static void climbHouseUp() {
        setAction("Climbing up Julliets house");
        if (getCurrentFloor() == 1) {
            setState("findJuliet");
        } else {
            GameObject staircase = GameObjects.getNearest(staircaseString);
            if (staircase != null && staircase.isOnScreen()) {
                staircase.interact("Climb-up");

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        if (getCurrentFloor() == 1) {
                            setState("findJuliet");
                            return true;
                        }
                        return false;
                    }
                }, Random.nextInt(856, 1800));
            } else {
                setState("walkToJulietHouse");
            }
        }
    }

    private static void walkToJulietHouse() {
        setAction("Walking to Juliets House");
        GameObject staircase = GameObjects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getLocation().getX() == 3157 && gameObject.getLocation().getY() == 3435;

            }
        });

        goToGameObject(staircase, julietHouse, "climbHouseUp");
    }

    private static void talkToJuliet() {
        setAction("Talking to Juliet");
        if (Inventory.contains(messageId)) {
            talkCountJuliet = 1;
            setState("climbHouseDown");
        } else if (clickToContinue.isVisible()) {
            clickToContinue.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue2.isVisible()) {
            clickToContinue2.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue3.isVisible()) {
            clickToContinue3.click();
            Time.sleep(800, 1300);
        } else if (!isTalking()) {
            Time.sleep(800, 1300);
            if (!isTalking() && !Inventory.contains(potionId)) {
                talkCountJuliet++;
                setState("climbHouseDown");
            }
        }
    }

    private static void talkToRomeo() {
        setAction("Talking to Romeo");
        WidgetChild lastTalk = Widgets.getWidgetByTextIncludingGrandChildren("Ah right, the potion");
        if (lastTalk != null && lastTalk.isValid() && lastTalk.isVisible()) {
            LogHandler.log("now waiting for ending....");
            lastRomeoTalk = true;
        }

        WidgetChild lawrence = Widgets.getWidgetByTextIncludingGrandChildren("Oh hello, have you seen Lather Fawrence?");
        if (lawrence != null && lawrence.isValid() && lawrence.isVisible()) {
            setState("walkToFatherLawrence");
        } else if (clickToContinue.isVisible()) {
            clickToContinue.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue2.isVisible()) {
            clickToContinue2.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue3.isVisible()) {
            clickToContinue3.click();
            Time.sleep(800, 1300);
        } else if (talkOptions.isValid()) {
            WidgetChild talkOptionRomeo1 = Widgets.getWidgetByTextIncludingGrandChildren("Yes, I have seen her actually!");
            WidgetChild talkOptionRomeo2 = Widgets.getWidgetByTextIncludingGrandChildren("Yes, ok, I'll let her know.");
            WidgetChild talkOptionRomeo3 = Widgets.getWidgetByTextIncludingGrandChildren("Ok, thanks.");

            if (talkOptionRomeo1 != null)
                talkOptionRomeo1.click();

            if (talkOptionRomeo2 != null)
                talkOptionRomeo2.click();

            if (talkOptionRomeo3 != null) {
                if (Quests.isCompleted(questString)) {
                    setState("stop");
                } else {
                    talkCountRomeo++;
                    if (talkCountRomeo < 2)
                        setState("walkToJulietHouse");
                    else if (talkCountRomeo == 2)
                        setState("walkToFatherLawrence");
                    else if (Quests.isCompleted(questString)) {
                        setState("stop");
                    }
                }
            }

            if (Quests.isCompleted(questString)) {
                setState("stop");
            }

            Time.sleep(500, 1500);
        } else {
            LogHandler.log("all talking failed");
            if (lastRomeoTalk) {
                LogHandler.log("waiting scene...");
                Time.sleep(10000);
                if (Quests.isCompleted(questString)) {
                    setState("stop");
                }
            } else {
                setState("findRomeo");
            }

            Time.sleep(2000, 5000);


            Widget completed = Widgets.getWidget(277);
            if (completed != null && completed.isValid()) {
                setState("stop");
            } else {
                setState("talkToRomeo");
            }


        }
    }

    private static void findRomeo() {
        if (isTalking()) {
            setState("talkToRomeo");
        } else {
            setAction("Looking for Romeo");
            final NPC romeo = Npcs.getNearest(romeoString);
            if (romeo != null && romeo.isOnScreen()) {
                Camera.turnTo(romeo);
                Time.sleep(100, 1300);
                romeo.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return clickToContinue.isVisible();
                    }
                }, Random.nextInt(976, 2173));
            } else if (romeo != null && !romeo.isOnScreen()) {
                Tile randomTile = randomTileInArea(romeoArea);
                Path path = Walking.findPath(randomTile);
                if (path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return romeo.isOnScreen();
                    }
                }, Random.nextInt(712, 2381));
            } else if (romeo == null) {
                setState("walkToRomeo");
            }
        }
    }

    private static void pickBerry() {
        if(Inventory.contains(cadavaBerryId)){
            setState("walkToRomeo");
        }else{
            setAction("Picking Berry");
            final GameObject bush = GameObjects.getNearest(cadavaBushString);
            if (bush != null && bush.isOnScreen()) {
                bush.interact("Pick-from");

                if (lastText != null && lastText.containsText("More berries will grow soon.")) {
                    LogHandler.log("No berries... Swapping world");
                    setAction("Swapping worlds for berries");
                    hopWorld();
                }

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return Inventory.contains(cadavaBerryId);
                    }
                }, Random.nextInt(1273, 3476));

                if (Inventory.contains(cadavaBerryId))
                    setState("walkToRomeo");
                else
                    Time.sleep(Random.nextInt(100, 900));

            } else if (bush != null && !bush.isOnScreen()) {
                Camera.turnTo(bush);

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return bush.isOnScreen();
                    }
                }, Random.nextInt(1273, 3476));
            } else {
                setState("walkToBush");
            }
        }
    }

    private static void walkToRomeo() {
        setAction("Walking to Romeo");
        goToNPC(romeoString, romeoArea, "findRomeo");
    }

    private static void walkToBush() {
        if (Inventory.contains(cadavaBerryId)) {
            setState("walkToRomeo");
        } else {
            setAction("Walking to cadava bush");
            goToGameObject(cadavaBushString, cadavaBushArea, "pickBerry");
        }
    }
}
