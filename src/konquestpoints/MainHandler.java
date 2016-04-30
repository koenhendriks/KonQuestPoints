package konquestpoints;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.events.InventoryEvent;
import org.tbot.internal.event.events.MessageEvent;
import org.tbot.internal.event.listeners.InventoryListener;
import org.tbot.internal.event.listeners.MessageListener;
import org.tbot.internal.event.listeners.PaintListener;
import quests.CooksAssistant;

import java.awt.*;
import java.awt.geom.Area;

/**
 * Class konquestpoints.MainHandler
 *
 * @author Koen Hendriks
 * @version 0.1 (29-04-2016)
 */
@Manifest(name = "Kon Quest Points", authors = "Kom Op Nou", version = 1.0, description = "Gets you to 7 Quest points so you can trade and use the Grand Exchange", category = ScriptCategory.OTHER)
public class MainHandler extends AbstractScript implements PaintListener, MessageListener, InventoryListener {


    @Override
    public int loop() {
        return CooksAssistant.run();
    }

    @Override
    public boolean onStart(){
        return true;
    }

    //Inventory Listener
    @Override
    public void itemsRemoved(InventoryEvent inventoryEvent) {

    }

    @Override
    public void itemsAdded(InventoryEvent inventoryEvent) {

    }

    //Message Listener
    @Override
    public void messageReceived(MessageEvent messageEvent) {

    }

    //Paint Listener
    @Override
    public void onRepaint(Graphics graphics) {

    }
}
