package quests;

import konquestpoints.MainHandler;
import org.tbot.internal.event.events.InventoryEvent;
import org.tbot.internal.event.listeners.InventoryListener;
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
 * Class SheepShearer
 *
 * @author Koen Hendriks
 * @version 0.1 (20-05-2016)
 */
public class SheepShearer extends Quest implements InventoryListener {

    private static final Area fredArea = new Area(new Tile[]{new Tile(3185, 3277, 0), new Tile(3185, 3279, 0), new Tile(3191, 3278, 0), new Tile(3191, 3276, 0), new Tile(3191, 3274, 0), new Tile(3191, 3270, 0), new Tile(3189, 3271, 0), new Tile(3188, 3272, 0), new Tile(3188, 3274, 0)});
    private static final Area stileArea = new Area(new Tile[]{new Tile(3198, 3277, 0)});
    private static final Area sheepArea = new Area(new Tile[]{new Tile(3194, 3276, 0), new Tile(3204, 3276, 0), new Tile(3211, 3265, 0), new Tile(3209, 3258, 0), new Tile(3194, 3257, 0), new Tile(3193, 3258, 0), new Tile(3193, 3275, 0)});
    private static final Area spinningArea = new Area(new Tile[]{new Tile(3207, 3214, 1)});

    private static final String fredString = "Fred the Farmer";
    private static final String questString = "Sheep Shearer";
    private static final String stileString = "Stile";
    private static final String shearString = "Shear";
    private static final String spinningWheelString = "Spinning Wheel";

    private static final int shearsId = 1735;
    private static final int spinAnimation = 894;
    private static final int woolId = 1737;
    private static final int ballOfWoolId = 1759;


    private static int woolStocked = 0;

    private static boolean completed = false;

    public static int run() {

        if (!completed) {

            setActiveQuest(questString);

            switch (getState()) {
                case "start":
                    if (Quests.isCompleted(questString)) {
                        setState("stop");
                    } else {
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
                case "getTool":
                    getTool();
                    break;
                case "walkToStile":
                    walkToStile();
                    break;
                case "climbStile":
                    climbStile();
                    break;
                case "shearSheeps":
                    shearSheeps();
                    break;
                case "walkToSpinner":
                    walkToSpinner();
                    break;
                case "spinWool":
                    spinWool();
                    break;
                case "stop":
                    if (Quests.isCompleted(questString)) {
                        LogHandler.log("Finished " + questString + " Quest");
                        MainHandler.completedSheepShearer = true;
                    }
                    setState("start");
                    break;
            }
        }

        return Random.nextInt(800, 1200);
    }

    private static void spinWool() {
        setAction("Spinning 20 wool");
        GameObject door = GameObjects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getLocation().getX() == 3207 && gameObject.getLocation().getY() == 3214 && gameObject.hasAction("Open");
            }
        });

        if (door != null) {
            door.interact("Open");
            Time.sleep(678, 1263);
        }

        GameObject spinningWheel = GameObjects.getNearest(spinningWheelString);

        if (spinningWheel != null) {
            Item wool = Inventory.getFirst(woolId);
            if (wool == null) {
                if (Inventory.getCount(ballOfWoolId) >= 20) {
                    setState("walkToFred");
                } else {
                    setState("walkToStile");
                }
            } else {
                wool.interact("Use");
                Time.sleep(1200, 1500);
                spinningWheel.interact("Use wool -> Spinning wheel");
                Time.sleep(1400, 1800);
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return Players.getLocal().getAnimation() != spinAnimation;
                    }
                }, Random.nextInt(3548,4785));
            }

        }
    }

    private static void walkToSpinner() {
        if (Inventory.getCount(woolId) + Inventory.getCount(ballOfWoolId) < 20) {
            setState("walkToFred");
        } else {
            setAction("Walking to spinning wheel");
            GameObject door = GameObjects.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    return (gameObject.getLocation().getX() == 3207 || gameObject.getLocation().getX() == 3208) && gameObject.getLocation().getY() == 3214;
                }
            });

            goToGameObject(door, spinningArea, "spinWool");
        }
    }

    private static void shearSheeps() {
        if (!Inventory.contains(shearsId)) {
            setState("walkToFred");
        } else if (Inventory.getCount(woolId) + Inventory.getCount(ballOfWoolId) >= 20) {
            setState("walkToSpinner");
        } else if (!sheepArea.contains(Players.getLocal())) {
            setState("walkToStile");
        } else if (Players.getLocal().getAnimation() == -1) {
            setAction("Shearing sheeps!");
            NPC sheep = Npcs.getNearest(new Filter<NPC>() {
                @Override
                public boolean accept(NPC npc) {
                    return npc.hasAction(shearString) && !npc.hasAction("Talk-to");
                }
            });

            if (sheep == null || !sheep.isOnScreen()) {
                Walking.walkTileMM(sheepArea.getCentralTile());
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        NPC sheep = Npcs.getNearest(new Filter<NPC>() {
                            @Override
                            public boolean accept(NPC npc) {
                                return npc.hasAction(shearString) && !npc.hasAction("Talk-to") && sheepArea.contains(npc);
                            }
                        });
                        return sheep != null;
                    }
                }, Random.nextInt(400, 1200));
            } else {
                sheep.interact(shearString);
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return Inventory.getCount(woolId) == (woolStocked + 1);
                    }
                }, Random.nextInt(3231, 4321));
            }
        }
    }

    private static void climbStile() {
        GameObject stile = GameObjects.getNearest(stileString);
        if (stile == null) {
            setState("walkToStile");
        } else {
            setAction("Climbing over stile");
            stile.interact("Climb-over");
            Time.sleep(3211,5632);
            Walking.walkTileMM(sheepArea.getCentralTile());
            setState("shearSheeps");
        }
    }

    private static void walkToStile() {
        if (!Inventory.contains(shearsId)) {
            setState("walkToFred");
        } else {
            setAction("Walking to Stile");
            goToGameObject(stileString, stileArea, "climbStile");
        }
    }

    private static void talkToFred() {
        if (!isTalking()) {
            setState("findFred");
        } else if (Quests.isCompleted(questString)) {
            completed = true;
            setState("stop");
        } else if (clickToContinue.isVisible()) {
            setAction("Talking to Fred");
            WidgetChild talkOptionFred5 = Widgets.getWidgetByTextIncludingGrandChildren("How are you doing getting those balls of wool?");

            if (talkOptionFred5 != null) {
                if (Inventory.getCount(ballOfWoolId) >= 20) {
                    clickToContinue.click();
                } else {
                    Time.sleep(500, 1200);
                    setState("getTool");
                }
            } else {
                clickToContinue.click();
            }
            Time.sleep(800, 1300);
        } else if (clickToContinue2.isVisible()) {
            setAction("Talking to Fred");
            clickToContinue2.click();
            Time.sleep(800, 1300);
        } else if (clickToContinue3.isVisible()) {
            setAction("Talking to Fred");
            clickToContinue3.click();
            Time.sleep(800, 1300);
        } else if (talkOptions.isValid()) {
            setAction("Talking to Fred");
            WidgetChild talkOptionFred1 = Widgets.getWidgetByTextIncludingGrandChildren("I'm looking for a quest.");
            WidgetChild talkOptionFred2 = Widgets.getWidgetByTextIncludingGrandChildren("Yes okay. I can do that.");
            WidgetChild talkOptionFred3 = Widgets.getWidgetByTextIncludingGrandChildren("Of course!");
            WidgetChild talkOptionFred4 = Widgets.getWidgetByTextIncludingGrandChildren("I'm something of an expert actually!");
            WidgetChild talkOptionFred5 = Widgets.getWidgetByTextIncludingGrandChildren("I'm back!");

            if (talkOptionFred1 != null) {
                talkOptionFred1.click();
                Time.sleep(800, 1000);
            }

            if (talkOptionFred2 != null) {
                talkOptionFred2.click();
                Time.sleep(600, 1300);
            }

            if (talkOptionFred3 != null) {
                talkOptionFred3.click();
                Time.sleep(300, 800);
            }

            if (talkOptionFred4 != null) {
                talkOptionFred4.click();
                Time.sleep(800, 1900);
            }

            if (talkOptionFred5 != null) {
                talkOptionFred5.click();
                Time.sleep(800, 1900);
            }
        } else {
            setState("stop");
        }
    }

    private static void findFred() {
        if (isTalking()) {
            setState("talkToFred");
        } else {
            setAction("Looking for Fred");
            checkDoors();


            final NPC fred = Npcs.getNearest(fredString);
            if (fred != null && fred.isOnScreen()) {
                Camera.turnTo(fred);
                Time.sleep(100, 1300);
                fred.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return clickToContinue.isVisible();
                    }
                }, Random.nextInt(976, 2173));
            } else if (fred != null && !fred.isOnScreen()) {
                Tile randomTile = randomTileInArea(fredArea);
                Path path = Walking.findPath(randomTile);
                if (path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return fred.isOnScreen();
                    }
                }, Random.nextInt(712, 2381));
            } else if (fred == null) {
                setState("walkToFred");
            }

        }
    }

    private static void checkDoors() {
        if (Quests.isCompleted(questString)) {
            setState("stop");
        } else {
            setAction("Checking if doors are open to Fred");
            GameObject gate = GameObjects.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    return gameObject.getLocation().getX() == 3188 && gameObject.getLocation().getY() == 3279;
                }
            });

            GameObject door = GameObjects.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    return gameObject.getLocation().getX() == 3189 && gameObject.getLocation().getY() == 3275;
                }
            });

            GameObject door2 = GameObjects.getNearest(new Filter<GameObject>() {
                @Override
                public boolean accept(GameObject gameObject) {
                    return gameObject.getLocation().getX() == 3188 && gameObject.getLocation().getY() == 3272;
                }
            });

            if (gate != null && door != null && door2 != null && (door.hasAction("Open") || door2.hasAction("Open") || gate.hasAction("Open"))) {
                if (door.distance() < gate.distance()) {

                    if (door.distance() < door2.distance()) {

                        if (door.hasAction("Open"))
                            door.interact("Open");

                        Time.sleep(345, 1998);

                        if (door2.hasAction("Open"))
                            door2.interact("Open");

                        Time.sleep(345, 1998);

                    } else {

                        if (door2.hasAction("Open"))
                            door2.interact("Open");

                        Time.sleep(345, 1998);

                        if (door.hasAction("Open"))
                            door.interact("Open");

                        Time.sleep(345, 1998);

                    }


                    if (gate.hasAction("Open")) {
                        gate.interact("Open");
                        Time.sleep(234, 1238);
                    }

                } else {

                    if (gate.hasAction("Open"))
                        gate.interact("Open");

                    Time.sleep(234, 1238);

                    if (door.hasAction("Open"))
                        door.interact("Open");

                    Time.sleep(234, 1238);

                    if (door2.hasAction("Open"))
                        door2.interact("Open");

                    Time.sleep(345, 1998);
                }
                LogHandler.log("Re-open doors and gate");

                checkDoors();
            } else if (gate != null) {
                if (gate.hasAction("Open")) {
                    gate.interact("Open");
                    Time.sleep(234, 1238);
                }
            } else if (door != null) {
                if (door.hasAction("Open")) {
                    door.interact("Open");
                    Time.sleep(345, 1998);
                }
            }
            LogHandler.log("Doors and gate are open");
        }
    }


    private static void walkToFred() {
        if (!isTalking()) {
            setAction("Walking to Fred");
            goToNPC(fredString, fredArea, "findFred");
        } else {
            setState("talkToFred");
        }
    }

    private static void getTool() {
        if (Inventory.contains(shearsId)) {
            checkDoors();
            setState("walkToStile");
        }else{
            checkDoors();
            setAction("Looking for shear tool");
            GroundItem shears = GroundItems.getNearest("Shears");
            if (shears != null) {
                shears.interact("Take");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return Inventory.contains(shearsId);
                    }
                }, Random.nextInt(4000, 6000));
            } else {
                LogHandler.log("No tool... hopping world");
                setAction("Swapping worlds for shear tool");
                hopWorld();
            }
        }
    }

    @Override
    public void itemsRemoved(InventoryEvent inventoryEvent) {

    }

    @Override
    public void itemsAdded(InventoryEvent inventoryEvent) {
        if(!Inventory.isOpen()){
            Inventory.openTab();
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.isOpen();
                }
            }, Random.nextInt(2345,5321));
        }
        if (inventoryEvent.getItem().getID() == woolId) {
            woolStocked++;
        }
    }
}
