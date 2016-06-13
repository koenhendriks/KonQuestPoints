package konquestpoints;

import org.tbot.graphics.MouseTrail;
import org.tbot.wrappers.Timer;
import quests.Quest;

import java.awt.*;

/**
 * Class Gui
 *
 * @author Koen Hendriks
 * @version 0.1 (13-06-2016)
 */
 final class Gui {

    private static MouseTrail mt = new MouseTrail();
    private static Timer runTimer = new Timer();

    static void draw(Graphics g){
        Graphics2D gr = (Graphics2D) g;

        gr.setColor(Color.WHITE);
        gr.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        gr.drawString("Konbot Quest Points", 15, 311);
        gr.setFont(new Font("Arial", 1, 11));
        gr.drawString("Runtime : " + runTimer.getTimeRunningString(), 350, 311);
        gr.drawString("Current Quest : " + Quest.getActiveQuest(), 15, 330);
        gr.drawString("Doing : " + Quest.getAction(), 300, 330);

        mt.draw(gr);
    }
}
