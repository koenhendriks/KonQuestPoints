package konquestpoints;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.InventoryEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Random;
import quests.CooksAssistant;
import quests.RomeoJuliet;
import quests.SheepShearer;

import java.awt.*;
import java.awt.geom.Area;

/**
 * Class konquestpoints.MainHandler
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
@Manifest(name = "Kon Quest Points", authors = "Kom Op Nou", version = 1.0, description = "Gets you to 7 Quest points so you can trade and use the Grand Exchange", category = ScriptCategory.OTHER)
public class MainHandler extends AbstractScript implements PaintListener, InventoryListener {

    public static boolean completedCooksAssistant = false;
    public static boolean completedRomeoJuliet = false;
    public static boolean completedSheepShearer = false;

    @Override
    public int loop() {
        if(!completedCooksAssistant)
            return CooksAssistant.run();
        else if(!completedRomeoJuliet)
            return RomeoJuliet.run();
        else if(!completedSheepShearer)
            return SheepShearer.run();
        else
            return -1;


//        return Random.nextInt(500,2000);
    }

    @Override
    public boolean onStart(){
        LogHandler.log("                                                ");
        LogHandler.log("                 Kon's Quest Points                 ");
        LogHandler.log("        Please report any bugs in the topic         ");
        LogHandler.log("                                                ");
        return true;
    }

    //Inventory Listener
    @Override
    public void itemsRemoved(InventoryEvent inventoryEvent) {

    }

    @Override
    public void itemsAdded(InventoryEvent inventoryEvent) {

    }

    //Paint Listener
    @Override
    public void onRepaint(Graphics graphics) {

    }
}
