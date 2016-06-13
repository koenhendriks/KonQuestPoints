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
        gr.setFont(new Font("Sans Serif", Font.BOLD, 20));
        gr.drawString("Konbot Quest Points", 15, 311);
        gr.setFont(new Font("Arial", Font.PLAIN, 11));
        gr.drawString("Runtime : " + runTimer.getTimeRunningString(), 350, 311);
//        gr.drawString("Antiban lookaround : " + AntiBan.lookAroundTimer.getRemaining(), 0, 15);
//        gr.drawString("Antiban running : " + AntiBan.runningTimer.getRemaining(), 300, 15);
//        gr.drawString("Antiban tabs : " + AntiBan.tabsTimer.getRemaining(), 0, 28);
//        gr.drawString("Antiban mouse : " + AntiBan.mouseTimer.getRemaining(), 300, 28);
        gr.drawString("Current Quest : " + Quest.getActiveQuest(), 15, 330);
        gr.drawString("Doing : " + Quest.getAction(), 270, 330);

        mt.draw(gr);
    }
}
