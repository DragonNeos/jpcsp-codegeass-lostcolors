/*
This file is part of jpcspCG.

JPCSPCG is and addition to the jpcsp software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published 
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU General Public License
along with Jpcsp.  If not, see <http://www.gnu.org/licenses/>.
 */
package jpcsp;

import cgscenesl.CGSMScene;
import cgscenesl.Choice;
import cgscenesl.Event;
import customscriptdev.CGSMScript;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import jpcsp.HLE.modules150.sceDisplay;

/**
 * This panel is where the treatment for events occur
 * @author Alvaro
 */
public class alvaroEventPanel extends JPanel {
     private Image backgroundImage, timerImage;
    private Event currentEvent;
    private boolean eventReady, eventIsComing, eventTreated;
    private static BufferedImage[] choiceImage;
    
    private final long tLimit = 10000; // 10 sec time limit to recognize
    private static Font font = MainGUI.a_dshower.getCustomFont().deriveFont(Font.BOLD);
    
    public alvaroEventPanel(int x, int y, int w, int h) {
        super(null, true);
        super.setIgnoreRepaint(true);
        super.setOpaque(true);
        try {
            choiceImage = new BufferedImage[12]; // 12 (8,9,10 & 11 are x2 image buttons square and circle)
            for (int k = 0; k < choiceImage.length; k++) {
                choiceImage[k] = ImageIO.read(new java.io.File("CodeGeassSpeech/Background/EventButton" + (k + 1) +".png"));
            }
        } catch (java.io.IOException e) {
            MainGUI.a_debug.escreveNaConsola("EventPanel:"+ e.fillInStackTrace());
        }
        super.setVisible(false);
        super.setBounds(x, y, w, h);
        this.backgroundImage = null;
        this.timerImage = null;
        this.eventReady = false;
        this.eventIsComing = false;
        this.eventTreated = true;
        /*Timer refresher = new Timer(37, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                repaint();
            }
        });
        refresher.start();*/
    }
    
    public static void adjustFont() {
        font = MainGUI.a_dshower.getCustomFont().deriveFont(Font.BOLD);
    }
    
    public static boolean triangleChoiceNotEqual(BufferedImage currentbi) {
        for (int k = 0; k < 2; k++) {
            if (CGSMScene.equalImages(currentbi, choiceImage[k])) {
                return false;
            }
        }
        return true;
    }
    /**
     * Tries to retrieve the choice of an event that was clicked
     * @return The choice clicked or -1 if none was clicked.
     */
    public int getChoiceClicked() {
        int r = -1;
        BufferedImage[] allButtons = new BufferedImage[currentEvent.size()];
        allButtons[0] = MainGUI.a_es.extractEventButtonTriangle();
        // check if triangle is chosen.
        if (!CGSMScene.equalImages(allButtons[0], choiceImage[0]) && !CGSMScene.equalImages(allButtons[0], choiceImage[1]))  {
            r = 0;
        }
        if (r < 0) {
            switch (currentEvent.size()) {
                case 2:
                    allButtons[1] = MainGUI.a_es.extractEventButtonCross();
                    if (!CGSMScene.equalImages(allButtons[1], choiceImage[2]) && 
                            !CGSMScene.equalImages(allButtons[1], choiceImage[3]))  {
                        r = 1;
                    }
                    break;
                default:
                    allButtons[1] = MainGUI.a_es.extractEventButtonSquare();
                    if (!CGSMScene.equalImages(allButtons[1], choiceImage[6]) && 
                            !CGSMScene.equalImages(allButtons[1], choiceImage[7]) &&
                            !CGSMScene.equalImages(allButtons[1], choiceImage[10]) &&
                            !CGSMScene.equalImages(allButtons[1], choiceImage[11]))  {
                        r = 1;
                    }
                    allButtons[2] = MainGUI.a_es.extractEventButtonCircle();
                    if (r < 0 && !CGSMScene.equalImages(allButtons[2], choiceImage[4]) && 
                            !CGSMScene.equalImages(allButtons[2], choiceImage[5]) &&
                            !CGSMScene.equalImages(allButtons[1], choiceImage[8]) &&
                            !CGSMScene.equalImages(allButtons[1], choiceImage[9]))  {
                        r = 2;
                    }
                    break;
            }
        }
        return r;
    }
    
    /**
     * Used for special event treatments.
     * @param nrC Number of choices it was
     * @return True if is still up or false if not.
     */
    private boolean isPanelStillUp(int nrC) {
        BufferedImage currentButton = MainGUI.a_es.extractEventButtonTriangle();
        if (CGSMScene.equalImages(currentButton, choiceImage[0]) || CGSMScene.equalImages(currentButton, choiceImage[1])) {
            return true;
        } else {
            switch(nrC) {
                case 2:
                    currentButton = MainGUI.a_es.extractEventButtonCross();
                    if (CGSMScene.equalImages(currentButton, choiceImage[2]) || CGSMScene.equalImages(currentButton, choiceImage[3])) {
                        return true;
                    }
                    break;
                default:
                    currentButton = MainGUI.a_es.extractEventButtonSquare();
                    if (CGSMScene.equalImages(currentButton, choiceImage[6]) || 
                            CGSMScene.equalImages(currentButton, choiceImage[7]) || 
                            CGSMScene.equalImages(currentButton, choiceImage[10])|| 
                            CGSMScene.equalImages(currentButton, choiceImage[11])) {
                        return true;
                    } else {
                        currentButton = MainGUI.a_es.extractEventButtonCircle();
                        if (CGSMScene.equalImages(currentButton, choiceImage[4]) ||
                                CGSMScene.equalImages(currentButton, choiceImage[5]) || 
                            CGSMScene.equalImages(currentButton, choiceImage[8])|| 
                            CGSMScene.equalImages(currentButton, choiceImage[9])) {
                            return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }
    
    /**
     * Sets the frame visible setting an event.
     * @param e The event.
     */
    public void setEvent(Event e) {
        this.currentEvent = e;
        this.eventIsComing = true;
        this.eventTreated = true;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedImage currentbi, backgroundImageBI, timerbi;
                long cTime = 0;
                try {
                    //MainGUI.a_debug.escreveNaConsola("trying to match...");
                    do {
                        currentbi = MainGUI.a_es.extractEventButtonTriangle();
                        Thread.sleep(MainGUI.getFPSV());
                        cTime += MainGUI.getFPSV();
                    } while (triangleChoiceNotEqual(currentbi) && cTime < tLimit);
                    if (cTime < tLimit) {
                        Thread.sleep(37);
                        //MainGUI.a_debug.escreveNaConsola("matched...");
                        backgroundImageBI = MainGUI.a_es.extractEventBackgroundBI();
                        backgroundImage = backgroundImageBI.getScaledInstance(backgroundImageBI.getWidth(),
                                backgroundImageBI.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        timerbi = MainGUI.a_es.extractTimerEventBackgroundBI();
                        timerImage = timerbi.getScaledInstance(timerbi.getWidth(), 
                        timerbi.getHeight(), BufferedImage.TYPE_INT_ARGB);
                        eventReady = true;
                        setVisible(true);
                        repaint();
                    } else {
                        setVisible(false);
                        //MainGUI.a_debug.escreveNaConsola("Event Skipped");
                        if (!MainGUI.a_dshower.wasTrickyScene()) {
                            MainGUI.a_dshower.next();
                        }
                    }
                } catch (InterruptedException e) {
                    
                }
            }
        });
        t.setName("Setting Event Thread");
        t.start();
    }
    /**
     * -2 = Not Defined, -1 = current, x = scene
     * @param index
     * @return 
     */
    public int getNextByChoiceIndex(int index) {
        return this.currentEvent.getChoice(index).getNextSceneID();
    }
    
    public int getNumberOfChoices() {
        return this.currentEvent.size();
    }
    
    public boolean isEventComing() {
        return this.eventIsComing;
    }
    
    private void eventChoice(final int choice) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                CGSMScript script = MainGUI.a_dshower.getScript();
                if (script != null && script.isRunning()) {
                    script.interpretNativeCommand("choiceButton = " + choice + ";", null);
                    String output;
                    do {
                        try {
                            output = script.getVar("choiceButton");
                        } catch (java.util.ConcurrentModificationException e) {
                            output = script.getVar("choiceButton");
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.err.println(e.fillInStackTrace());
                        }
                    } while(Integer.parseInt(output) != -1);
                }
                int nextScene = MainGUI.a_ep.getNextByChoiceIndex(choice);
                //MainGUI.a_debug.escreveNaConsola("HELLO2");
                if (nextScene == -1) {
                    //MainGUI.a_debug.escreveNaConsola("HELLO");
                    //MainGUI.a_dshower.next();
                    MainGUI.a_es.relax(true);
                } else if (nextScene == -2) {
                    // not yet defined meaning to send a message to the user.
                    // if scene contains a script then the choice is treated.
                    if(script == null) {
                        JOptionPane.showMessageDialog(null, 
                            "This scene choice is still not translated.", 
                            "Scene not translated", 
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        if (MainGUI.isSpecialTrickyEvent()) {
                            int script_currentIndex = Integer.parseInt(script.getVar("currentIndex"));
                            MainGUI.a_debug.escreveNaConsola(script_currentIndex);
                            int nrChoice = MainGUI.getSpecialTrickyEvent(script_currentIndex);
                            if (nrChoice != -1) {
                                try {
                                    while (isPanelStillUp(nrChoice)) {
                                        Thread.sleep(MainGUI.getFPSV());
                                    }
                                    if (MainGUI.a_dshower.getIndexOfScene() == 99) {
                                        sceDisplay.deactivate();
                                        alvaroJapTextChecker.forceResetRecog(4);
                                        MainGUI.a_dshower.next();
                                        MainGUI.a_dshower.waitTillMovieEnds();
                                        sceDisplay.activate();
                                    } else {
                                        alvaroJapTextChecker.forceResetRecog(-1);
                                        MainGUI.a_dshower.next();
                                    }
                                } catch (InterruptedException e) {
                                    System.err.println(e.fillInStackTrace());
                                }
                            }
                        }
                        //MainGUI.a_dshower.next();
                        MainGUI.a_es.relax(true);
                    }
                } else {
                    // set next scene
                    CGSMScene nScene = MainGUI.a_cache.getSceneByIndex(nextScene);
                    MainGUI.a_dshower.forceReset();
                    MainGUI.a_dshower.setDialog(nScene);
                    if (MainGUI.soundHelper) {
                        MainGUI.setPlayListWithIndex(MainGUI.a_cache.getSceneIndex(nScene));
                    }
                    MainGUI.a_dshower.start();
                }
                eventTreated = true;
            }
        });
        t.start();
    }
    
    /**
     * Use this to close the event panel when the choice is chosen.
     * @param b true to close it.
     */
    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            this.setOpaque(true);
            Thread autoCloseThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedImage timerbi;
                    try {
                        Thread.sleep(200);
                        int choiceButton = -1;
                        eventTreated = false;
                        do {
                            timerbi = MainGUI.a_es.extractTimerEventBackgroundBI();
                            timerImage = timerbi.getScaledInstance(timerbi.getWidth(), 
                                    timerbi.getHeight(), BufferedImage.TYPE_INT_ARGB);
                            Graphics2D.class.cast(getGraphics()).drawImage(timerImage, 0, 
                                    28 * MainGUI.currentViewPortNr, 
                                    timerImage.getWidth(null) * MainGUI.currentViewPortNr, 
                                timerImage.getHeight(null) * MainGUI.currentViewPortNr, null);
                            Thread.sleep(MainGUI.getFPSV());
                            choiceButton = getChoiceClicked();
                            //MainGUI.a_debug.escreveNaConsola("Comparing Event Button");
                        } while(choiceButton < 0 && isVisible()); // !choicesNotEqual(bi) && isVisible() ((!choicesNotEqual(bi) || !CGSMScene.equalImages(bi, choiceImage[2])) && isVisible())
                        //MainGUI.a_debug.escreveNaConsola("Choice Button = " + choiceButton);
                        eventChoice(choiceButton);
                        MainGUI.a_es.relax(false);
                        if (isVisible()) {
                            setVisible(false);
                            repaint();
                        }
                    } catch (InterruptedException e) {
                        MainGUI.a_debug.escreveNaConsola("Exception occurred in alvaroEventPanel.");
                    }
                }
            });
            autoCloseThread.start();
        } else {
            MainGUI.a_dshower.eventContinue();
            this.backgroundImage = null;
            this.eventReady = false;
            this.setOpaque(false);
            this.eventIsComing = false;
        }
    }
    
    
    public boolean isEventTreated() {
        return this.eventTreated;
    }
    
    @Override
    public void paint(Graphics g) {
        //super.paint(g);
        Graphics2D g2D = Graphics2D.class.cast(g);
        if (this.backgroundImage != null) {
            g2D.drawImage(this.backgroundImage, 0,0, 
                    backgroundImage.getWidth(this) * MainGUI.currentViewPortNr, 
                    backgroundImage.getHeight(this) * MainGUI.currentViewPortNr, this);
        }
        // TODO re-arrange the image drawing position to match background.
        if (this.timerImage != null) {
            //g2D.drawImage(this.timerImage, 168, 28, this.timerImage.getWidth(this), 
            //        this.timerImage.getHeight(this), this);
            
                g2D.drawImage(this.timerImage, 0, 28 * MainGUI.currentViewPortNr, 
                        this.timerImage.getWidth(this) * MainGUI.currentViewPortNr, 
                        this.timerImage.getHeight(this) * MainGUI.currentViewPortNr, this);
            /*if (MainGUI.currentViewPortNr == 1) {
                g2D.drawImage(this.timerImage, 0, 28, 
                        this.timerImage.getWidth(this), 
                        this.timerImage.getHeight(this), this);
            } else {
                g2D.drawImage(this.timerImage, 0, 76, 
                        this.timerImage.getWidth(null), 
                        this.timerImage.getHeight(null), this);
            }*/
        }
        g2D.setColor(java.awt.Color.white);
        if (this.eventReady && this.currentEvent != null) {
            g2D.setFont(font);
            String text;
            g2D.setRenderingHint( RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON );
            Choice aux;
            for (int k = 0; k < this.currentEvent.size(); k++) {
                aux = this.currentEvent.getChoice(k);
                text = aux.getChoiceText();
                if (aux.getNextSceneID() == -2 && (MainGUI.a_dshower.getScript() == null)) {
                    text = text + " <Not Translated>";
                }
                if (k == 0) {
                    drawOutline(g2D, 155* MainGUI.currentViewPortNr, 14* MainGUI.currentViewPortNr, text);
                    g2D.drawString(text, 155* MainGUI.currentViewPortNr, 14* MainGUI.currentViewPortNr);
                } else {
                    switch(this.currentEvent.size()) {
                        case 2:
                            switch (k) {
                                case 1:
                                    drawOutline(g2D, 155* MainGUI.currentViewPortNr, 89* MainGUI.currentViewPortNr, text);
                                    g2D.drawString(text, 155* MainGUI.currentViewPortNr, 89* MainGUI.currentViewPortNr);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case 3:
                            switch (k) {
                                case 1:
                                    drawOutline(g2D, 40* MainGUI.currentViewPortNr, 89* MainGUI.currentViewPortNr, text);
                                    g2D.drawString(text, 40* MainGUI.currentViewPortNr, 89* MainGUI.currentViewPortNr);
                                    break;
                                case 2:
                                    drawOutline(g2D, 274* MainGUI.currentViewPortNr, 89* MainGUI.currentViewPortNr, text);
                                    g2D.drawString(text, 274* MainGUI.currentViewPortNr, 89* MainGUI.currentViewPortNr);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
    private void drawOutline(Graphics2D g2D, int posX, int posY, String s) {
        GlyphVector gv = font.createGlyphVector(g2D.getFontRenderContext(), s);
        Color lastC = g2D.getColor();
        g2D.setPaint(Color.black);
        g2D.setStroke(new BasicStroke(1.3f));
        g2D.translate(posX + 1, posY + 1);
        java.awt.Shape sh = gv.getOutline();
        sh.getBounds().setSize(sh.getBounds().width + 2, sh.getBounds().height + 2);
        g2D.draw(sh);    
        g2D.translate(-posX - 1, -posY - 1);
        g2D.setStroke(new BasicStroke(1.0f));
        g2D.setPaint(lastC);
    }
}
