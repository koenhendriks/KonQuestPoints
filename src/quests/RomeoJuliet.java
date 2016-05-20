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
import sun.rmi.runtime.Log;

/**
 * Class RomeoJuliet
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
public final class RomeoJuliet extends Quest {

    public static final Area fatherLawrenceArea = new Area(new Tile[]  {new Tile(3252,3482,0),new Tile(3259,3482,0),new Tile(3255,3484,0),new Tile(3255,3487,0),new Tile(3254,3488,0),new Tile(3253,3488,0),new Tile(3252,3487,0)});
    public static final Area cadavaBushArea = new Area(new Tile[]  {new Tile(3271,3369,0)});
    public static final Area romeoArea = new Area(new Tile[]  {new Tile(3210,3415,0),new Tile(3219,3415,0),new Tile(3219,3433,0),new Tile(3207,3433,0)});
    public static final Area julietArea = new Area(new Tile[]  {new Tile(3155,3425,1)});
    public static final Area julietHouse = new Area(new Tile[]  {new Tile(3156,3434,0)});
    public static final Area julietHouseUp = new Area(new Tile[] {new Tile(3155,3435,1)});
    public static final Area apothecaryArea = new Area(new Tile[] {new Tile(3197,3404,0)});


    public static final int cadavaBerryId = 753;
    public static final int messageId = 755;
    public static final int potionId = 756;

    public static int talkCountRomeo = 0;
    public static int talkCountJuliet = 0;
    public static int staircaseUID = 1267032500;
    public static int julietFirstDoorId = 11772;
    public static int julietSecondDoorId = 11773;
    public static int julietFirstDoorUUID = 1266636597;

    public static int julietSecondDoorUUID = 1266636086;
    public static final String cadavaBushString = "Cadava bush";
    public static final String romeoString = "Romeo";
    public static final String julietString = "Juliet";
    public static final String staircaseString = "Staircase";
    public static final String fatherLawrenceString = "Father Lawrence";
    public static final String apothecaryString = "apothecary";

    public static boolean lastRomeoTalk = false;
    public static boolean completed = false;

    public static int run(){

        if(!completed){
            switch (getState()){
                case "start":
                    setState("walkToJulietHouse");
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
                    if(Quests.isCompleted("Romeo and Juliet")){
                        LogHandler.log("Finished Romeo and Juliet Quest");
                        MainHandler.completedRomeoJuliet = true;
                    }
                    setState("start");
                    break;
            }
        }

        return Random.nextInt(800,1200);
    }

    private static void talkToApo() {
        if(Inventory.contains(potionId)){
            setState("walkToJulietHouse");
        }else if(clickToContinue.isVisible()){
            clickToContinue.click();
            Time.sleep(800,1300);
        }else if(clickToContinue2.isVisible()){
            clickToContinue2.click();
            Time.sleep(800,1300);
        }else if(clickToContinue3.isVisible()) {
            clickToContinue3.click();
            Time.sleep(800, 1300);
        }else{
            WidgetChild talk = Widgets.getWidgetByTextIncludingGrandChildren("Click here to continue");
            if(talk != null){
                talk.click();
                Time.sleep(800,1400);
            }else if(Inventory.contains(potionId)){
                setState("walkToJulietHouse");
            } else {
                LogHandler.log("looking for widget");
                WidgetChild endTalk = Widgets.getWidgetByTextIncludingGrandChildren("Ok, thanks.");
                if(endTalk != null && endTalk.isValid() && endTalk.isVisible()){
                    if(!Inventory.contains(cadavaBerryId) && !Inventory.contains(potionId))
                        setState("walkToBush");
                    else if(Inventory.contains(potionId))
                        setState("walkToJulietHouse");
                    else
                        setState("walkToApo");
                }else {
                    if(!Inventory.contains(cadavaBerryId) && !Inventory.contains(potionId))
                        setState("walkToBush");
                    else if(Inventory.contains(potionId))
                        setState("walkToJulietHouse");
                    else{
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
            final NPC apo = Npcs.getNearest(apothecaryString);
            if(apo != null && apo.isOnScreen()){
                Camera.turnTo(apo);
                Time.sleep(100,1300);
                apo.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return isTalking();
                    }
                },Random.nextInt(976,2173));
            }else if(apo != null && !apo.isOnScreen()){
                Tile randomTile = randomTileInArea(apothecaryArea);
                Path path = Walking.findPath(randomTile);
                if(path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return apo.isOnScreen();
                    }
                },Random.nextInt(712,2381));
            }else if(apo == null){
                setState("walkToApo");
            }
        }
    }

    private static void walkToApo() {
        goToNPC(apothecaryString,apothecaryArea,"findApo");
    }

    private static void talkToFatherLawrence() {
        if(clickToContinue.isVisible()){
            LogHandler.log("clicking first");
            clickToContinue.click();
            Time.sleep(800,1300);
        }else if(clickToContinue2.isVisible()){
            LogHandler.log("clicking second");
            clickToContinue2.click();
            Time.sleep(800,1300);
        }else if(clickToContinue3.isVisible()) {
            LogHandler.log("clicking third");
            clickToContinue3.click();
            Time.sleep(800, 1300);
        }else{
            LogHandler.log("Looking for widget");
            WidgetChild talk = Widgets.getWidgetByTextIncludingGrandChildren("Click here to continue");
            if(talk != null){
                LogHandler.log("clicking widget");
                talk.click();
                Time.sleep(800,1400);
            }else{
                setState("walkToApo");
            }
        }
    }

    private static void findFatherLawrence() {
        if(isTalking()){
            setState("talkToFatherLawrence");
        }else{
            final NPC fatherLawrence = Npcs.getNearest(fatherLawrenceString);
            if(fatherLawrence != null && fatherLawrence.isOnScreen()){
                Camera.turnTo(fatherLawrence);
                Time.sleep(100,1300);
                fatherLawrence.interact("Talk-to");
                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return isTalking();
                    }
                },Random.nextInt(976,2173));
            }else if(fatherLawrence != null && !fatherLawrence.isOnScreen()){
                Tile randomTile = randomTileInArea(fatherLawrenceArea);
                Path path = Walking.findPath(randomTile);
                if(path != null)
                    path.traverse();

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        return fatherLawrence.isOnScreen();
                    }
                },Random.nextInt(712,2381));
            }else if(fatherLawrence == null){
                setState("walkToFatherLawrence");
            }
        }
    }

    private static void walkToFatherLawrence() {
        goToNPC(fatherLawrenceString,fatherLawrenceArea,"findFatherLawrence");
    }

    private static void findJuliet() {
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

        if(isTalking()){
            setState("talkToJuliet");
        }else if(juliet != null && juliet.isOnScreen() && firstDoor == null && secondDoor == null){
            LogHandler.log("Looking for julliet");
            juliet.interact("Talk-to");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return clickToContinue.isVisible();
                }
            },Random.nextInt(976,2173));
        }else if(firstDoor != null && firstDoor.hasAction("Open")){
            LogHandler.log("opening first door");
            firstDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return firstDoor.hasAction("Close");
                }
            }, Random.nextInt(900,1400));

        } else if(secondDoor != null && secondDoor.hasAction("Open")){
            LogHandler.log("opening second door");

            secondDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return secondDoor.hasAction("Close");
                }
            }, Random.nextInt(900,1400));
        } else if(secondDoor == null && firstDoor == null){
            Walking.walkTileMM(julietArea.getCentralTile());
            Time.sleep(900,1400);
        }
    }

    private static void climbHouseDown() {
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

        if(getCurrentFloor() == 0){
            setState("walkToRomeo");
        }else if(staircase != null && staircase.isOnScreen() && firstDoor == null && secondDoor == null){
            LogHandler.log("Looking for staircase");
            staircase.interact("Climb-down");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return getCurrentFloor() == 0;
                }
            },Random.nextInt(976,2173));
        }else if(secondDoor != null && secondDoor.hasAction("Open")){
            LogHandler.log("opening second door");

            secondDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return secondDoor.hasAction("Close");
                }
            }, Random.nextInt(900,1400));
        }else if(firstDoor != null && firstDoor.hasAction("Open")){
            LogHandler.log("opening first door");
            firstDoor.interact("Open");
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return firstDoor.hasAction("Close");
                }
            }, Random.nextInt(900,1400));

        }  else if(secondDoor == null && firstDoor == null){
            Walking.walkTileMM(julietHouseUp.getCentralTile());
            Time.sleep(900,1400);
        }


    }

    private static void climbHouseUp() {
        if(getCurrentFloor() == 1){
            setState("findJuliet");
        }else {
            GameObject staircase = GameObjects.getNearest(staircaseString);
            if(staircase != null && staircase.isOnScreen()){
                staircase.interact("Climb-up");

                Time.sleepUntil(new Condition() {
                    @Override
                    public boolean check() {
                        if(getCurrentFloor() == 1){
                            setState("findJuliet");
                            return true;
                        }
                        return false;
                    }
                },Random.nextInt(856,1800));
            }else {
                setState("walkToJulietHouse");
            }
        }
    }

    private static void walkToJulietHouse() {
        GameObject staircase = GameObjects.getNearest(new Filter<GameObject>() {
            @Override
            public boolean accept(GameObject gameObject) {
                return gameObject.getUID() == staircaseUID;
            }
        });

        goToGameObject(staircase,julietHouse,"climbHouseUp");
    }

    private static void talkToJuliet() {
        if(Inventory.contains(messageId)){
            talkCountJuliet = 1;
            setState("climbHouseDown");
        }else if(clickToContinue.isVisible()){
            clickToContinue.click();
            Time.sleep(800,1300);
        }else if(clickToContinue2.isVisible()) {
            clickToContinue2.click();
            Time.sleep(800, 1300);
        }else if(clickToContinue3.isVisible()){
            clickToContinue3.click();
            Time.sleep(800,1300);
        } else if(!isTalking()){
            Time.sleep(800,1300);
            if(!isTalking() && !Inventory.contains(potionId)){
                talkCountJuliet++;
                setState("climbHouseDown");
            }
        }
    }

    private static void talkToRomeo() {
        WidgetChild lastTalk = Widgets.getWidgetByTextIncludingGrandChildren("Ah right, the potion");
        if(lastTalk != null && lastTalk.isValid() && lastTalk.isVisible()){
            lastRomeoTalk = true;
        }

        if(clickToContinue.isVisible()){
            clickToContinue.click();
            Time.sleep(800,1300);
        }else if(clickToContinue2.isVisible()){
            clickToContinue2.click();
            Time.sleep(800,1300);
        }else if(clickToContinue3.isVisible()){
            clickToContinue3.click();
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
                if(Quests.isCompleted("Romeo and Juliet")){
                    setState("stop");
                } else {
                    talkCountRomeo++;
                    if(talkCountRomeo < 2)
                        setState("walkToJulietHouse");
                    else if(talkCountRomeo == 2)
                        setState("walkToFatherLawrence");
                    else if(Quests.isCompleted("Romeo and Juliet")){
                        setState("stop");
                    }
                }
            }

            if(Quests.isCompleted("Romeo and Juliet")) {
                setState("stop");
            }

            Time.sleep(500,1500);
        }else{
            LogHandler.log("all talking failed");
            if(lastRomeoTalk){
                Time.sleep(10000);
            }

            Time.sleep(2000,5000);

            if(Quests.isCompleted("Romeo and Juliet")) {
                setState("stop");
            }

            Widget completed = Widgets.getWidget(277);
            if(completed != null && completed.isValid()){
                setState("stop");
            }else{
                setState("talkToRomeo");
            }

        }
    }

    private static void findRomeo() {
        if(isTalking()){
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
                Tile randomTile = randomTileInArea(romeoArea);
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

            if(lastText != null && lastText.containsText("More berries will grow soon.")){
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
