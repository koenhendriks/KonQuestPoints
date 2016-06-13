package konquestpoints;

import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.Timer;

/**
 * Class AntiBan
 *
 * @author Koen Hendriks
 * @version 0.1 (30-04-2016)
 */
public final class AntiBan {

    static Timer lookAroundTimer = new Timer(Random.nextInt(Random.nextInt(8000, 15000), Random.nextInt(50000, 70000)));
    static Timer runningTimer = new Timer(Random.nextInt(Random.nextInt(28000, 30000), Random.nextInt(30000, 34000)));
    static Timer tabsTimer = new Timer(Random.nextInt(Random.nextInt(60000, 65000), Random.nextInt(175000, 195000)));
    static Timer mouseTimer = new Timer(Random.nextInt(Random.nextInt(28000, 30000),  Random.nextInt(175000, 195000)));

    static void checkTimers() {
        if (lookAroundTimer.getRemaining() <= 0) {
            lookAround();
        }

        if (runningTimer.getRemaining() <= 0) {
            handleRun();
        }

        if(tabsTimer.getRemaining() <= 0) {
            openingTabs();
        }

        if(mouseTimer.getRemaining() <= 0){
            randomMouse();
        }
    }

    private static void lookAround() {
        lookAroundTimer = new Timer(Random.nextInt(Random.nextInt(8000, 15000), Random.nextInt(50000, 70000)));

        if (oneIn(2)) {
            Camera.setAngle(Camera.getAngleTo(Camera.getYaw() + Random.nextInt(150, 300)));
            LogHandler.log("[AB] Looking around far");
        } else {
            LogHandler.log("[AB] Looking around");

            extraMouseMove();

            Camera.rotateAndTiltRandomly();
            if (oneIn(3)) {
                Time.sleep(500, 900);
                extraMouseMove();
                LogHandler.log("[AB] Looking around");
                Camera.rotateAndTiltRandomly();
                if (oneIn(3)) {
                    Time.sleep(356, 1025);
                    extraMouseMove();
                    LogHandler.log("[AB] Looking around");
                    Camera.rotateAndTiltRandomly();
                    if (oneIn(3)) {
                        Time.sleep(587, 621);
                        extraMouseMove();
                        LogHandler.log("[AB] Looking around");
                        Camera.rotateAndTiltRandomly();
                    }
                }
            }
        }
    }

    private static void randomMouse(){
        mouseTimer = new Timer(Random.nextInt(Random.nextInt(28000, 30000),  Random.nextInt(175000, 195000)));
        LogHandler.log("[AB] Moving Mouse");
        Mouse.moveRandomly();
        extraMouseMove();
    }

    private static void extraMouseMove() {
        if (oneIn(2)) {
            Mouse.moveRandomly();
            LogHandler.log("[AB] Moving Mouse Extra");
        }
    }

    private static void openingTabs() {
        tabsTimer = new Timer(Random.nextInt(Random.nextInt(60000, 65000), Random.nextInt(175000, 195000)));

        extraMouseMove();


        if (!Inventory.isOpen()) {
            LogHandler.log("[AB] Opening Inventory");
            Inventory.openTab();
            Time.sleepUntil(new Condition() {
                @Override
                public boolean check() {
                    return Inventory.isOpen();
                }
            }, Random.nextInt(1200, 1800));
        } else {
            int sRandom = Random.nextInt(1, 3);
            if (sRandom == 1 && !Widgets.isTabOpen(Widgets.TAB_STATS)) {
                LogHandler.log("[AB] Opening Stats");
                Widgets.openTab(Widgets.TAB_STATS);
                extraMouseMove();
            } else if (sRandom == 2 && !Widgets.isTabOpen(Widgets.TAB_EQUIPMENT)) {
                LogHandler.log("[AB] Opening Equipment");
                Widgets.openTab(Widgets.TAB_EQUIPMENT);
                extraMouseMove();
            } else if (sRandom == 3 && !Widgets.isTabOpen(Widgets.TAB_COMBAT)) {
                LogHandler.log("[AB] Opening Combat");
                Widgets.openTab(Widgets.TAB_COMBAT);
                extraMouseMove();
            }
        }

    }

    /**
     * Enable running if the player has enough energy
     * and is not already running.
     */
    private static void handleRun() {
        runningTimer = new Timer(Random.nextInt(Random.nextInt(28000, 30000), Random.nextInt(30000, 34000)));
        if (!Walking.isRunEnabled()) {
            if (Walking.getRunEnergy() >= Random.nextInt(30, 100)) {
                LogHandler.log("[AB] Enabling Running");
                Walking.setRun(true);
                if (oneIn(2)) {
                    LogHandler.log("[AB] Moving Mouse");
                    Mouse.moveRandomly();
                }
            }
        } else if (Walking.getRunEnergy() < Random.nextInt(0, 20)) {
            LogHandler.log("[AB] Stop Running");
            Walking.setRun(false);
            extraMouseMove();
        }
    }

    public static boolean oneIn(int change) {
        int rand = Random.nextInt(1, change);
        return rand == change;
    }
}
