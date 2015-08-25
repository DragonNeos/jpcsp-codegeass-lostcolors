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

import cgscenesl.*;
import customscriptdev.CGSMScript;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import jpcsp.HLE.modules150.sceDisplay;
import jpcsp.HLE.modules150.sceMpeg;

/**
 *
 * @author LelouchZero
 */
public class alvaroDialogShower extends JPanel {

    // Changing Vars
    private CGSMScene scene;
    private int sceneIndex;
    private Object current;
    private java.awt.Image background, background02;
    private LinkedList<String> sceneText;
    private CGSMScript currentActiveScript;
    private boolean isMenuScene;
    private boolean canSkip, paused;
    private boolean canAnalyseLetters = true;
    private Semaphore eventWaiter;
    private boolean acquired;
    private boolean isPaiting = false;
    private boolean blockPaint = false;
    // CONST
    private static final int fontSizeNormal = 13;
    private static final int fontType = Font.PLAIN;
    // NON-CONST STATIC
    private static String sv_lastValue = "-1";
    private static Font f;
    private boolean waitForGame, stcT, wTS;
    private static BufferedImage[] choiceBackgroundImages;
    private Thread textRecognThread;

    public boolean isPaused() {
        return paused;
    }

    // takes care of the english text appearing letter by letter over time.
    private class AnimatedDialog implements Runnable {

        private LinkedList<String> dialog;
        private int showingLength;
        private boolean running;
        private java.util.concurrent.Semaphore sync;
        private static final long DELAY = 37;

        public AnimatedDialog() {
            this(new LinkedList<String>());
        }

        public AnimatedDialog(LinkedList<String> dialog) {
            this.dialog = dialog;
            this.showingLength = 0;
            this.running = false;
            this.sync = new Semaphore(1);
        }

        public void setDialog(LinkedList<String> dialog) {
            if (dialog == null) {
                this.dialog = new LinkedList<String>();
            } else {
                this.dialog = dialog;
            }
        }

        public LinkedList<String> getCurrentShowingText() {
            int currentLength = 0;
            boolean error;
            LinkedList<String> result = new LinkedList<String>();
            do {
                error = false;
                try {
                    this.sync.acquire();
                    currentLength = this.showingLength;
                    this.sync.release();
                } catch (InterruptedException e) {
                    error = true;
                    e.fillInStackTrace();
                }
            } while (error);
            java.util.Iterator<String> i = this.dialog.iterator();
            String currentLine;
            while (i.hasNext() && currentLength > 0) {
                currentLine = i.next();
                if (currentLine.length() <= currentLength) {
                    currentLength -= currentLine.length();
                    result.add(currentLine);
                } else {
                    result.add(currentLine.substring(0, currentLength));
                    break;
                }
            }
            return result;
        }

        public void skip() {
            if (this.running) {
                this.running = false;
                this.showingLength = this.getTotalLength();
                if (isVisible()) {
                    repaint();
                }
            }
        }

        public void stop() {
            if (isRunning()) {
                this.running = false;
            }
            this.showingLength = 0;
            this.setDialog(null);
        }

        public boolean isRunning() {
            return this.running;
        }

        private int getTotalLength() {
            int result = 0;
            java.util.Iterator<String> it = this.dialog.iterator();
            while (it.hasNext()) {
                result += it.next().length();
            }
            return result;
        }

        public String getFirstString() {
            return this.dialog.peek();
        }

        public void setStartIndex(int index) {
            this.showingLength = index;
        }

        @Override
        public void run() {
            this.running = true;
            while (this.showingLength < this.getTotalLength() && this.running) {
                try {
                    this.sync.acquire();
                    this.showingLength++;
                    this.sync.release();
                    if (isVisible() && this.running && !isPaiting) {
                        repaint();
                    }
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    e.fillInStackTrace();
                }
            }
            this.running = false;
        }
    }
    private AnimatedDialog animDialog;

    // takes care of painting the background whenever it is signaled for.
    private class BackgroundGenerator extends Thread {

        @Override
        public void run() {
            BufferedImage bi, backgI;
            while (true) {
                synchronized (this) {
                    try {
                        //Thread.sleep(MainGUI.getFPSV());
                        wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (!MainGUI.a_ep.isVisible()) {
                    if (!blockPaint && !MainGUI.a_es.isFirstLetterShown()) {
                        bi = MainGUI.a_es.getBackgroundBI();
                        if (isMenuScene) {
                            backgI = bi.getSubimage(71, 0, 78, 17);
                        } else {
                            backgI = bi.getSubimage(14, 0, 78, 20);
                        }
                        setBackground(backgI);
                        background02 = bi;
                        if (isVisible()) {
                            repaint();
                        }
                    } else {
                        if (!blockPaint && !MainGUI.a_es.isFourthLetterShown()
                                && !MainGUI.a_es.isSecondLineFirstLetterShown()) {
                            bi = MainGUI.a_es.getBackgroundBI();
                            background02 = bi;
                            if (isVisible()) {
                                repaint();
                            }
                        } else {
                            if (sceneIndex == 148 && MainGUI.a_es.isGUIDown()) {
                                background02 = MainGUI.a_es.getBackgroundBI();
                                if (isVisible()) {
                                    repaint();
                                }
                            }
                            blockPaint = true;
                        }
                        if (blockPaint) {
                            blockPaint = MainGUI.a_es.checkFourthJapaneseLetter();
                        }
                    }
                }

            }
        }

        public synchronized void attempDrawBackground() {
            notify();
        }
    }
    private static BackgroundGenerator backgroundGenerator;

    public alvaroDialogShower(int x, int y, int width, int height) {
        super(null, true);
        super.setVisible(false);
        super.setBounds(x, y, width, height);
        super.setBackground(null);
        super.setIgnoreRepaint(true);
        this.eventWaiter = new Semaphore(0);
        this.waitForGame = false;
        this.sceneText = null;
        this.current = null;
        this.initBacks();
        this.paused = false;
        this.stcT = false;
        this.wTS = false;
        this.acquired = false;
        this.isMenuScene = false;
        this.sceneIndex = -1;
        this.textRecognThread = this.constructTextRecognThread();
        this.animDialog = new AnimatedDialog();
        backgroundGenerator = new BackgroundGenerator();

        initChoiceBacks();
        //backgroundThread = this.generateBackgroundThread();
        try {
            f = Font.createFont(Font.TRUETYPE_FONT, new java.io.File("CodeGeassSpeech/fonts/font01/SciFly-Sans.ttf")).deriveFont((float) fontSizeNormal).deriveFont((int) fontType);
        } catch (Exception e) {
            MainGUI.a_debug.escreveNaConsola("ERROR WHILE LOADING FONT: " + e.toString());
        }
        System.gc(); // force freeing up memory
    }

    public static void adjustToViewPort() {
        f = f.deriveFont((float) (fontSizeNormal * MainGUI.currentViewPortNr));
        alvaroEventPanel.adjustFont();
        alvaroExtracter.adjustColors();
    }

    private static void initChoiceBacks() {
        LinkedList<BufferedImage> images = new LinkedList<BufferedImage>();
        File[] files = new File("CodeGeassSpeech/Background/").listFiles();
        for (int k = 0; k < files.length; k++) {
            if (files[k].getName().startsWith("ChoiceBackground")) {
                try {
                    images.add(ImageIO.read(files[k]));
                } catch (IOException e) {
                    MainGUI.a_debug.escreveNaConsola("\"" + files[k].getAbsolutePath() + "\" could not be readed.");
                }
            }
        }
        choiceBackgroundImages = new BufferedImage[images.size()];
        for (int k = 0; !images.isEmpty(); k++) {
            choiceBackgroundImages[k] = images.pop();
        }
        MainGUI.a_debug.escreveNaConsola("ChoiceBackgrounds Readed");
    }

    private void initBacks() {
        this.background = Toolkit.getDefaultToolkit().getImage("CodeGeassSpeech/Background/background1x.png");
        this.background02 = Toolkit.getDefaultToolkit().getImage("CodeGeassSpeech/Background/background1x02.png");
    }

    public Font getCustomFont() {
        return f;
    }

    public void attemptDrawBackground() {
        backgroundGenerator.attempDrawBackground();
    }

    public void setDialog(CGSMScene scene) {
        sceDisplay.activate();
        initBacks();
        this.stcT = false;
        this.wTS = false;
        switch (MainGUI.a_cache.getSceneIndex(scene)) {
            case 56:
                this.isMenuScene = true;
                break;
            case 41:
                this.isMenuScene = true;
                break;
            case 131:
                this.isMenuScene = true;
                break;
            case 135:
                this.isMenuScene = true;
                break;
            default:
                this.isMenuScene = false;
                break;
        }
        MainGUI.a_debug.escreveNaConsola("Dialog Starting...");
        this.scene = scene;
        this.sceneIndex = this.getCurrentSceneIndex();
        this.currentActiveScript = MainGUI.a_cache.retrieveScript(scene);

        if (this.currentActiveScript != null) {
            MainGUI.a_debug.escreveNaConsola("Script starting...");
            Thread t = new Thread(this.currentActiveScript);
            t.setName("Current Active Script Thread");
            t.start();
        }
    }

    public void cannotSkip() {
        this.canSkip = false;
    }

    public void eventContinue() {
        if (acquired) {
            this.eventWaiter.release();
            acquired = false;
        }
    }

    public static boolean equalPixels(int[][] pixel1, int[][] pixel2) {
        int min = Math.min(pixel1.length, pixel2.length);
        for (int k = 0; k < min; k++) {
            if (pixel1[k][0] != pixel2[k][0] || pixel1[k][1] != pixel2[k][1] || pixel1[k][2] != pixel2[k][2]) {
                return false;
            }
        }
        return true;
    }

    public synchronized boolean canSkip() {
        return this.canSkip;
    }

    public CGSMScript getScript() {
        return this.currentActiveScript;
    }

    public void start() {
        if (isVisible()) {
            this.stop();
        }
        if (!backgroundGenerator.isAlive()) {
            backgroundGenerator.start();
        }
        this.setVisible(true);
        sceDisplay.activate();
        this.nextProcess();
        MainGUI.a_debug.escreveNaConsola("Starting...");

    }

    public void setBackground(File f) {
        this.background = Toolkit.getDefaultToolkit().getImage(f.getAbsolutePath());
    }

    public void setBackground(BufferedImage bi) {
        this.background = bi;
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if (b) {
            this.setOpaque(true);
        } else {
            this.setOpaque(false);
        }
    }
    /*
     * Pauses the dialog without restarting the graphics.
     */

    public void pause() {
        if (this.isVisible()) {
            this.paused = true;
            super.setVisible(false);
            this.setOpaque(true);
        }
    }

    /*
     * Unpauses the dialog.
     */
    public void unpause() {
        if (!this.isVisible()) {
            super.setVisible(true);
            this.setOpaque(false);
            this.paused = false;
        }
    }

    public void forceReset() {
        if (this.scene != null) {
            this.scene.reset();
        }
    }

    private void waitUntilSkipper() {
        // DISABLE USER INTERACTION UNTIL RECOGNITION
        Controller.disable();
        Controller.getInstance().releaseEverything();
        Controller.getInstance().stopAutoSkip();

        if (MainGUI.currentViewPortNr != 1) {
            MainGUI.instance.resizeView(1);
        }

        do {
            try {
                Thread.sleep(100);
                this.background02 = MainGUI.a_es.getBackgroundBI();
                getGraphics().drawImage(this.background02, 0, 0, this.background02.getWidth(this), this.background02.getHeight(this), this);

            } catch (InterruptedException e) {
                e.fillInStackTrace();
            }
        } while (!CGSMScene.ImageContainsColors(MainGUI.a_es.extractSceneBI(), MainGUI.a_skippingColors) && alvaroEventPanel.triangleChoiceNotEqual(MainGUI.a_es.extractEventButtonTriangle()));

        // ENABLE USER INTERACTION
        Controller.enable();
    }

    public boolean wasTrickyScene() {
        return this.wTS;
    }

    public void skipTextAnimation() {
        if (isVisible()) {
            this.animDialog.skip();
        }
    }

    public void next() {
        if (isVisible()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    nextProcess();
                }
            }).start();
        }
    }

    public void nextProcess() {
        switch (this.sceneIndex) {
            case 76:
                if (this.scene.getIndex() == 82) {
                    alvaroJapTextChecker.useEventAlgorithm();
                }
                break;
            case 199:
                if (this.scene.getIndex() == 30) {
                    alvaroJapTextChecker.useEventAlgorithm();
                }
                break;
            default:
                break;
        }
        Controller.getInstance().disableGUIHider();
        //MainGUI.a_debug.escreveNaConsola(this.eventWaiter.availablePermits());
        this.animDialog.stop();
        this.sceneText = null;
        //this.sceneText = null;
        this.canSkip = false;
        this.waitForGame = true;
        while (!MainGUI.a_ep.isEventTreated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        try {
            if (!this.stcT) {
                this.canAnalyseLetters = false;
                int currentSceneIndex = MainGUI.a_cache.getSceneIndex(scene);
                String trickySceneInfo = MainGUI.getCorrespondentTrickyScene(currentSceneIndex);
                //MainGUI.a_debug.escreveNaConsola("Treating Tricky Scene...");
                if (nrSeperators(trickySceneInfo) > 1) {
                    int cScene = Integer.parseInt(trickySceneInfo.substring(0, trickySceneInfo.indexOf("-"))),
                            index = Integer.parseInt(trickySceneInfo.substring(trickySceneInfo.indexOf("-") + 1, trickySceneInfo.lastIndexOf("-"))),
                            startIndex = analyseSI(trickySceneInfo.substring(trickySceneInfo.lastIndexOf("-") + 1));
                    //MainGUI.a_debug.escreveNaConsola("[1]Special Tricky Scene...");
                    if (this.scene.getIndex() == index) {
                        this.waitUntilSkipper();
                        try {
                            BufferedImage bi = MainGUI.a_es.extract(), specialBI = ImageIO.read(new File("CodeGeassSpeech/SceneIdentificator/S" + cScene + "I03.png"));
                            if (CGSMScene.equalImages(bi, specialBI)) {
                                MainGUI.a_es.setTrickySceneTreated();
                                this.setDialog(MainGUI.a_cache.getSceneByIndex(cScene));
                                this.scene.setIndex(startIndex + 1);
                            }
                            bi = null;
                            specialBI = null;
                            this.stcT = true;
                            this.initBacks();
                            this.wTS = true;
                            if (MainGUI.currentViewPortNr != 1) {
                                MainGUI.instance.resizeView(MainGUI.currentViewPortNr);
                            }
                        } catch (java.io.IOException e) {
                            MainGUI.a_debug.escreveNaConsola("AlvaroDialogShower - ERROR[TrickySceneTreator]: " + e);
                        }
                    }
                } else if (this.getScript() != null) {
                    if (this.scene.getIndex() == analyseSI(trickySceneInfo.substring(trickySceneInfo.indexOf("-") + 1))) {

                        // DISABLE USER INTERACTION UNTIL RECOGNITION
                        Controller.disable();
                        Controller.getInstance().releaseEverything();
                        Controller.getInstance().stopAutoSkip();

                        //MainGUI.a_debug.escreveNaConsola("Special Tricky Scene...");
                        java.awt.Image prevI = this.background02.getScaledInstance(this.background02.getWidth(this), this.background02.getHeight(this), Image.SCALE_DEFAULT);
                        switch (currentSceneIndex) {
                            case 227:
                                try {
                                    Thread.sleep(500);
                                    BufferedImage bi = MainGUI.a_es.extract();
                                    if (!CGSMScene.ImageContainsColor(bi, -1)) {
                                        this.currentActiveScript.interpretNativeCommand("sv = " + trickySceneInfo.substring(0, trickySceneInfo.indexOf("/")) + ";", null);
                                        sv_lastValue = trickySceneInfo.substring(0, trickySceneInfo.indexOf("/"));
                                    } else {
                                        this.currentActiveScript.interpretNativeCommand("sv = " + trickySceneInfo.substring(trickySceneInfo.indexOf("/") + 1, trickySceneInfo.indexOf("-")) + ";", null);
                                        sv_lastValue = trickySceneInfo.substring(trickySceneInfo.indexOf("/") + 1, trickySceneInfo.indexOf("-"));
                                    }
                                    while (Integer.parseInt(this.currentActiveScript.getVar("sv")) != -1) {
                                        Thread.sleep(100);
                                        //MainGUI.a_debug.escreveNaConsola("HERE2(special)");
                                    }
                                    MainGUI.a_es.setTrickySceneTreated();
                                    bi = null;
                                } catch (InterruptedException e) {
                                    //MainGUI.a_debug.escreveNaConsola("AlvaroDialogShower - ERROR[TrickySceneTreator]: " + e);
                                }
                                break;
                            default:
                                treatNSTS(trickySceneInfo, currentSceneIndex);
                                break;
                        }
                        this.stcT = true;
                        this.background02 = prevI;
                        this.wTS = true;
                        // ENABLE USER INTERACTION
                        Controller.enable();
                    }

                }
            }
        } catch (NullPointerException e) {
            if (this.scene.getIndex() == 0) {
                if (MainGUI.a_es.isTrickyScene()) {
                    //MainGUI.a_debug.escreveNaConsola("[catch]:Treating Tricky Scene...");
                    this.waitUntilSkipper();
                    //MainGUI.a_debug.escreveNaConsola("Analysing...");
                    MainGUI.a_es.analyse(MainGUI.a_es.extract());
                    this.wTS = true;
                } else {
                    this.stcT = true; // because there is no stc if the conditions were all passed.
                }
            }
        }
        repaint();
        canAnalyseLetters = true;
        try {
            this.current = this.scene.getNext();
            if (current instanceof Speech) {
                Speech currentSpeech = (Speech) current;
                String who = currentSpeech.getWho();
                if (who.contains("posInd")) { // force the emulator to ignore guiError when the user is out of energy.
                    alvaroJapTextChecker.ignoreGUIError();
                }
                if (who.contains("[")) {
                    int indexOfBracet = who.indexOf("[");
                    if (indexOfBracet != -1) {
                        who = who.substring(0, indexOfBracet);
                    }
                }
                //this.sceneText =  this.transformString(((Speech) current).getDialog());
                if (!who.endsWith("-") && !who.equals("YOU") && !who.contains("posInd") && !who.toLowerCase().contains("enemydamage") && !who.toLowerCase().contains("mydamage")) {
                    this.animDialog.setDialog(this.transformString(who + ": " + currentSpeech.getDialog()));
                    this.animDialog.setStartIndex(this.animDialog.getFirstString().indexOf(":"));
                } else {
                    this.animDialog.setDialog(this.transformString(currentSpeech.getDialog()));
                }
                this.sceneText = this.animDialog.getCurrentShowingText();
            } else {
                this.animDialog.setDialog(null);
                this.sceneText = null;
                if (current instanceof Event) {
                    MainGUI.a_ep.setEvent(Event.class.cast(current));
                    //MainGUI.a_debug.escreveNaConsola("Event Starting...");
                }
            }
            if (this.scene.getIndex() + 1 < this.scene.getNumberOfSpeeches() + this.scene.getNumberOfEvents()) {
                if (this.scene.getByIndex(this.scene.getIndex() + 1) instanceof Event) {
                    //MainGUI.a_debug.escreveNaConsola("Event Starting...");
                    MainGUI.a_ep.setEvent(Event.class.cast(this.scene.getNext()));
                }
            }
            MainGUI.a_debug.escreveNaConsola("Scene Current Index: " + this.scene.getIndex());

            //MainGUI.a_debug.escreveNaConsola("Next...");
        } catch (NoSuchElementException e) {
            System.err.println("Scene ended by default. [ " + this.scene.getIndex() + " ]");
            if (MainGUI.soundHelper) {
                MainGUI.setPlayListWithIndex(-1);
            }
            sceDisplay.deactivate(); //deactivate for the processor to rest.
            stop();
        }
        /*
         if (backgroundThread != null && !backgroundThread.isAlive()) {
         backgroundThread = this.generateBackgroundThread();
         backgroundThread.start();
         }*/
        if (this.textRecognThread != null && this.textRecognThread.isAlive()) {
            this.textRecognThread.interrupt();
            this.textRecognThread = null;
        }
        textRecognThread = this.constructTextRecognThread();
        textRecognThread.setName("Next - Thread(alvaroDialog)");
        textRecognThread.start();
    }

    private Thread constructTextRecognThread() {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                    boolean[] chars;
                    do {
                        chars = MainGUI.a_es.getJapaneseLettersBooleans(false);
                        Thread.sleep(75);
                    } while ((!chars[0] && !chars[1] && !chars[2]));
                    waitForGame = false;
                    //MainGUI.a_debug.escreveNaConsola("waitForGame: " + waitForGame);
                    Thread animTh = new Thread(animDialog);
                    animTh.start();
                    repaint();
                } catch (InterruptedException e) {
                    MainGUI.a_debug.escreveNaConsola("DIALOGSHOWER:" + e);
                }
            }
        });
    }

    /**
     * This function treats normaly a special tricky scene
     *
     * @param trickySceneInfo
     */
    private void treatNSTS(String trickySceneInfo, int currentSceneIndex) {
        this.waitUntilSkipper();
        try {
            BufferedImage bi = MainGUI.a_es.extract(), specialBI = ImageIO.read(new File("CodeGeassSpeech/SceneIdentificator/S" + currentSceneIndex + "I03.png"));
            if (CGSMScene.equalImages(bi, specialBI)) {
                this.currentActiveScript.interpretNativeCommand("sv = " + trickySceneInfo.substring(0, trickySceneInfo.indexOf("/")) + ";", null);
            } else {
                this.currentActiveScript.interpretNativeCommand("sv = " + trickySceneInfo.substring(trickySceneInfo.indexOf("/") + 1, trickySceneInfo.indexOf("-")) + ";", null);
                //MainGUI.a_debug.escreveNaConsola("HERE");
            }
            while (Integer.parseInt(this.currentActiveScript.getVar("sv")) != -1) {
                Thread.sleep(100);
                //MainGUI.a_debug.escreveNaConsola("HERE2");
            }
            MainGUI.a_es.setTrickySceneTreated();
            bi = null;
            specialBI = null;
            if (MainGUI.currentViewPortNr != 1) {
                MainGUI.instance.resizeView(MainGUI.currentViewPortNr);
            }
        } catch (java.io.IOException e) {
            MainGUI.a_debug.escreveNaConsola("AlvaroDialogShower - ERROR[TrickySceneTreator]: " + e);
        } catch (InterruptedException e) {
            MainGUI.a_debug.escreveNaConsola("AlvaroDialogShower - ERROR[TrickySceneTreator]: " + e);
        }
    }

    private int analyseSI(String si) {
        if (si.contains("neg")) {
            return Integer.parseInt(si.substring(3)) * -1;
        } else {
            return Integer.parseInt(si);
        }
    }

    private static int nrSeperators(String info) {
        int r = 0;
        for (int k = 0; k < info.length(); k++) {
            if (info.charAt(k) == '-') {
                r++;
            }
        }
        return r;
    }

    public void stop() {
        Controller.enable();
        Controller.getInstance().turnOnGUIHiderFunct();
        Controller.getInstance().enableGUIHider();
        Controller.getInstance().stopAutoSkip();
        Controller.getInstance().releaseEverything();
        alvaroDebugger.activateLoadingDebugging();
        sceDisplay.deactivate();
        this.setVisible(false);
        if (this.scene != null) {
            this.scene.reset();
        }
        MainGUI.a_es.recognizeScene();
        this.current = null;
        System.gc(); // release memory
    }

    @Override
    public void paint(Graphics g) {
        isPaiting = true;
        super.paint(g);
        Graphics2D g2D = (Graphics2D) g;
        //g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        // background (block japanese text)
        if (MainGUI.currentViewPortNr == 1) {
            g2D.drawImage(this.background02, 0, 0, 347, super.getHeight(), this);
        } else {
            g2D.drawImage(this.background02, 0, 0, 694, super.getHeight(), this);
        }
        if (isMenuScene) {
            g2D.drawImage(this.background, 71 * MainGUI.currentViewPortNr,
                    0, 78 * MainGUI.currentViewPortNr,
                    this.background.getHeight(this) * MainGUI.currentViewPortNr,
                    this);
        } else {
            g2D.drawImage(this.background, 14 * MainGUI.currentViewPortNr, 0,
                    78 * MainGUI.currentViewPortNr,
                    this.background.getHeight(this) * MainGUI.currentViewPortNr,
                    this);
        }
        paintText(g2D);
        isPaiting = false;
    }

    private void paintText(Graphics2D g2D) {
        // text
        //g2D.setFont(new Font(fontName, fontType, fontSize));
        g2D.setPaint(Color.white);
        //g2D.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        g2D.setFont(f);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (!waitForGame) {
            if (this.current != null && this.animDialog != null) {
                this.sceneText = this.animDialog.getCurrentShowingText();
                if (this.sceneText != null) {
                    for (int k = 0; this.sceneText != null && k < this.sceneText.size(); k++) {
                        g2D.drawString(this.sceneText.get(k), 5, (12 * MainGUI.currentViewPortNr) * (k + 1));
                    }
                }
            }
        }
    }

    private LinkedList<String> transformString(String information) {
        information = prepareString(information);
        String[] splited = information.split("[ ]");
        LinkedList<String> r = new LinkedList<String>();
        String addInfo = "";
        boolean containsLn = false;
        if (getAppropriateSize(information) <= 240) {
            for (int k = 0; k < splited.length; k++) {
                containsLn = (splited[k].contains("<b>"));
                if (containsLn) {
                    splited[k] = splited[k].replaceAll("<b>", "");
                    addInfo += splited[k];
                }
                if (containsLn || (getPixelLengthOfString(addInfo) + getPixelLengthOfString(splited[k]) >= 365)) {
                    r.add(addInfo);
                    addInfo = "";
                }
                if (!containsLn) {
                    addInfo += splited[k];
                    if (k != (splited.length - 1)) {
                        addInfo += " ";
                    } else {
                        r.add(addInfo);
                    }
                }
            }
            return r;
        } else {
            throw new IllegalArgumentException();
        }
    }

    private String prepareString(String i) {
        i = i.replaceAll("\n", " ");
        if (i.length() != 0 && ("" + i.charAt(i.length() - 1)).equals(" ")) {
            return i.substring(0, i.length() - 1);
        } else {
            return i;
        }
    }

    private int getAppropriateSize(String i) {
        return i.replaceAll("<b>", "").length();
    }

    private int getPixelLengthOfString(String s) {
        return getPixelLengthOfStringAux(s, 0);
    }

    private int getPixelLengthOfStringAux(String s, int index) {
        if (index < s.length()) {
            return (1 + charPixelLength(s.charAt(index))) + getPixelLengthOfStringAux(s, index + 1);
        } else {
            return 0;
        }
    }

    private int charPixelLength(char c) {
        return getGraphics().getFontMetrics().charWidth(c);
    }

    public boolean isMenuScene() {
        return this.isMenuScene;
    }

    public boolean canAnalyseLetters() {
        return this.canAnalyseLetters;
    }

    public int getSceneCurrentIndex() {
        return this.scene.getIndex();
    }

    private int getCurrentSceneIndex() {
        return MainGUI.a_cache.getSceneIndex(scene);
    }

    public int getIndexOfScene() {
        return this.sceneIndex;
    }

    public int getNumberOfObjectsInScene() {
        return this.scene.getNumberOfEvents() + this.scene.getNumberOfSpeeches();
    }

    public boolean isSceneLastIndex() {
        if (!isVisible()) {
            return false;
        }
        return this.scene.getNumberOfEvents() + this.scene.getNumberOfSpeeches() - 1 == this.scene.getIndex();
    }
    /**
     * This variable is used to pass information from a script to another script
     * (This is needed on scene 367)
     */
    private static String[] AUX_SCENE_SCRIPT_INFO;

    /**
     * Check if a particular scene in a particular index has a treatment for the
     * Japanese Text Checker.
     */
    public void checkIfHasTreatment() {
        switch (this.sceneIndex) {
            case 7:
                if (this.scene.getIndex() == 8) {
                    forceSkipOne();
                }
                break;
            case 31:
                if (this.scene.getIndex() == 16) {
                    Controller.disable();
                    Controller.getInstance().stopAutoSkip();
                    Controller.getInstance().releaseEverything();
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                }
                break;
            case 32:
                if (this.scene.getIndex() == 5) {
                    forceSkipOne();
                }
                break;
            case 38:
                if (this.scene.getIndex() == 8) {
                    forceSkipOne();
                }
                break;
            case 53:
                if (this.scene.getIndex() == 44) {
                    forceSkipOne();
                }
                break;
            case 57:
                if (this.scene.getIndex() == 3) {
                    forceSkipOne();
                }
                break;
            case 58:
                if (this.scene.getIndex() == 64 || this.scene.getIndex() == 105) {
                    waitForVideoToPass();
                }
                if (this.scene.getIndex() == 107) {
                    forceSkipOne();
                }
                break;
            case 59:
                if (this.scene.getIndex() == 206) {
                    alvaroDebugger.deactivateLoadingDebugging();
                } else if (this.scene.getIndex() == 207) {
                    stopEverythingUntilLoadingIsOver(0, 4);
                } else if (this.scene.getIndex() == 208) {
                    alvaroDebugger.activateLoadingDebugging();
                }
                break;
            case 76:
                if (this.scene.getIndex() == 27) {
                    forceSkipOne();
                }
                break;
            case 83:
                if (scene.getIndex() == 19 || scene.getIndex() == 21
                        || scene.getIndex() == 30 || scene.getIndex() == 32
                        || scene.getIndex() == 33 || scene.getIndex() == 40
                        || scene.getIndex() == 41 || scene.getIndex() == 42) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            // 22 e 23
            case 96:
                if (this.scene.getIndex() == 22 || this.scene.getIndex() == 23) {
                    forceSkipOne();
                } else {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 98:
                if (this.scene.getIndex() == 3 || this.scene.getIndex() == 4 || this.scene.getIndex() == 5) {
                    forceSkipOne();
                }
                break;
            case 99:
                if (this.scene.getIndex() == 93 || this.scene.getIndex() == 100
                        || this.scene.getIndex() == 120 || this.scene.getIndex() == 136
                        || this.scene.getIndex() == 137) {
                    forceSkipOne();
                }
                break;
            case 101:
                if (this.scene.getIndex() == 2) {
                    Controller.disable();
                    Controller.getInstance().stopAutoSkip();
                    Controller.getInstance().releaseEverything();
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                }
                break;
            case 107:
                if (this.scene.getIndex() == 43) {
                    Controller.disable();
                    Controller.getInstance().stopAutoSkip();
                    Controller.getInstance().releaseEverything();
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                }
                break;
            case 127:
                if (this.scene.getIndex() == 18) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 128:
                if (this.scene.getIndex() == 18) {
                    forceSkipOne();
                }
                break;
            case 140:
                if (this.scene.getIndex() == 30 || this.scene.getIndex() == 34) {
                    forceSkipOne();
                }
                break;
            case 144:
                if (this.scene.getIndex() == 36) {
                    waitForVideoToPass();
                }
                if (this.scene.getIndex() == 48) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                if (scene.getIndex() == 65) {
                    waitForVideoToPass();
                }
                if (this.scene.getIndex() == 78) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                if (this.scene.getIndex() == 233) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 146:
                if (scene.getIndex() == 14) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 147:
                if (scene.getIndex() == 14) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 154:
                if (this.scene.getIndex() == 3) {
                    Controller.getInstance().stopAutoSkip();
                }
                if (this.scene.getIndex() == 4) {
                    waitForVideoToPass();
                }
                break;
            case 160:
                if (this.scene.getIndex() == 69) {
                    alvaroDebugger.deactivateLoadingDebugging();
                    Controller.getInstance().stopAutoSkip();
                }
                if (this.scene.getIndex() == 70) {
                    stopEverythingUntilLoadingIsOver(2, -1);
                } else if (this.scene.getIndex() == 71) {
                    alvaroDebugger.activateLoadingDebugging();
                } else if (this.scene.getIndex() > 71) {
                    if (this.scene.getIndex() == 90
                            || this.scene.getIndex() == 92
                            || this.scene.getIndex() == 96
                            || this.scene.getIndex() == 101
                            || this.scene.getIndex() == 104
                            || this.scene.getIndex() == 111
                            || this.scene.getIndex() == 112
                            || this.scene.getIndex() == 113) {
                        alvaroJapTextChecker.ignoreGUIError();
                    } else {
                        if (sv_lastValue.equals("0")) {
                            if (this.scene.getIndex() == 146) {
                                Controller.getInstance().stopAutoSkip();
                            } else if (this.scene.getIndex() == 148) {
                                waitForVideoToPass();
                            }
                        } else {
                            if (this.scene.getIndex() == 145) {
                                Controller.getInstance().stopAutoSkip();
                            } else if (this.scene.getIndex() == 146) {
                                waitForVideoToPass();
                            }
                        }
                    }
                }
                break;
            case 167:
                if (this.scene.getIndex() == 33) {
                    Controller.getInstance().stopAutoSkip();
                } else if (this.scene.getIndex() == 34) {
                    stopEverythingUntilLoadingIsOver(2, -1);
                } else if (this.scene.getIndex() == 46
                        || this.scene.getIndex() == 57
                        || this.scene.getIndex() == 58
                        || this.scene.getIndex() == 72) {
                    alvaroJapTextChecker.ignoreGUIError();
                } else if (this.scene.getIndex() == 68) {
                    Controller.getInstance().stopAutoSkip();
                } else if (this.scene.getIndex() == 69) {
                    waitForVideoToPass();
                }
                break;
            case 168:
                if (this.scene.getIndex() == 62) {
                    alvaroJapTextChecker.ignoreGUIError();
                } else if (this.scene.getIndex() == 68) {
                    Controller.getInstance().stopAutoSkip();
                } else if (this.scene.getIndex() == 69) {
                    waitForVideoToPass();
                } else if (this.scene.getIndex() == 108) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;

            case 176:
                if (this.scene.getIndex() == 6 && this.stcT) {
                    boolean[] chars = MainGUI.a_es.getJapaneseLettersBooleans(false);
                    while ((!chars[0] && !chars[1] && !chars[2]) && alvaroEventPanel.triangleChoiceNotEqual(MainGUI.a_es.extractEventButtonTriangle())) {
                        try {
                            Thread.sleep(17);
                            chars = MainGUI.a_es.getJapaneseLettersBooleans(false);
                        } catch (InterruptedException e) {
                            System.err.println("Treatment sleep interrupted.");
                        }
                    }
                    if (chars[0] || chars[1] || chars[2]) {
                        this.currentActiveScript.interpretNativeCommand("sv = 0;", null);
                        while (Integer.parseInt(this.currentActiveScript.getVar("sv")) != -1) {
                            try {
                                Thread.sleep(100);
                                chars = MainGUI.a_es.getJapaneseLettersBooleans(false);
                            } catch (InterruptedException e) {
                                System.err.println("Treatment sleep interrupted.");
                            }
                        }
                    } else {
                        this.currentActiveScript.interpretNativeCommand("sv = 1;", null);
                    }
                    this.next();
                } else if (this.scene.getIndex() == 200) {
                    Controller.getInstance().stopAutoSkip();
                } else if (this.scene.getIndex() == 201) {
                    waitForVideoToPass();
                }
                break;
            case 179:
                if (this.scene.getIndex() == 104) {
                    Controller.getInstance().stopAutoSkip();
                } else if (this.scene.getIndex() == 105) {
                    waitForVideoToPass();
                } else if (this.scene.getIndex() == 84
                        || this.scene.getIndex() == 112
                        || this.scene.getIndex() == 131
                        || this.scene.getIndex() == 132) {
                    alvaroJapTextChecker.ignoreGUIError();
                } else if (this.scene.getIndex() == 135) {
                    Controller.getInstance().stopAutoSkip();
                } else if (this.scene.getIndex() == 136) {
                    waitForVideoToPass();
                }
                break;
            case 196:
                if (this.scene.getIndex() == 27) {
                    forceSkipOne();
                } else if (this.scene.getIndex() == 29) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 214:
                if (this.scene.getIndex() == 31) {
                    stopEverythingUntilLoadingIsOver(1, -1);
                } else if (this.scene.getIndex() == 271) {
                    stopEverythingUntilLoadingIsOver(0, 4);

                }
                break;
            case 218:
                if (this.scene.getIndex() == 62) {
                    stopEverythingUntilLoadingIsOver(1, -1);
                } else if (this.scene.getIndex() == 112) {
                    stopEverythingUntilLoadingIsOver(0, 4);
                }
                break;
            case 222:
                if (this.scene.getIndex() == 9) {
                    stopEverythingUntilLoadingIsOver(1, -1);
                }
                break;
            case 223:
                if (this.scene.getIndex() == 18 || this.scene.getIndex() == 52) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                if (this.scene.getIndex() == 79) {
                    stopEverythingUntilLoadingIsOver(0, 4);
                }
                break;
            case 226:
                if (this.scene.getIndex() == 51
                        || this.scene.getIndex() == 100
                        || this.scene.getIndex() == 103
                        || this.scene.getIndex() == 107) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                if (this.scene.getIndex() == 30) {
                    Controller.disable();
                    Controller.getInstance().stopAutoSkip();
                    Controller.getInstance().releaseEverything();
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                }
                if (this.scene.getIndex() == 49) {
                    boolean scriptHadAlreadyRunned = Boolean.parseBoolean(this.getScript().getVar("alreadyRunned"));
                    if (scriptHadAlreadyRunned) {
                        stopEverythingUntilLoadingIsOver(1, -1);
                    } else {
                        sceDisplay.deactivate();
                        alvaroJapTextChecker.forceResetRecog(-1);
                        initBacks(); // reset the backgrounds
                        pause();

                        // catch loading
                        boolean[] japLetters;
                        boolean isLoading = true;

                        Color initColor = MainGUI.a_es.getSpecifiedPixelColor(13, 135);
                        Color currentColor;
                        boolean stopCheckingText = false;
                        int svInfo;
                        try {
                            do {
                                Thread.sleep(17);
                                japLetters = MainGUI.a_es.getJapaneseLettersBooleans(false);
                                currentColor = MainGUI.a_es.getSpecifiedPixelColor(13, 135);
                                if (!stopCheckingText && (currentColor.getRGB() != initColor.getRGB())) {
                                    stopCheckingText = true;
                                    isLoading = true;
                                    this.getScript().interpretNativeCommand("sv = 0;", null);
                                    do {
                                        svInfo = Integer.parseInt(this.getScript().getVar("sv"));
                                        Thread.sleep(100);
                                    } while (svInfo != -1);
                                }
                                if (!stopCheckingText && (japLetters[1] || japLetters[2])) {
                                    System.err.println("here");
                                    isLoading = false;
                                    this.getScript().interpretNativeCommand("sv = 1;", null);
                                    do {
                                        svInfo = Integer.parseInt(this.getScript().getVar("sv"));
                                        Thread.sleep(100);
                                    } while (svInfo != -1);
                                    break;
                                }
                            } while (!MainGUI.a_es.isLoadingColor(false));

                            if (isLoading) {
                                MainGUI.a_debug.escreveNaConsola("Loading Screen Captured.");
                                // wait 'till vanish.
                                do {
                                    Thread.sleep(17);
                                } while (MainGUI.a_es.isLoadingColor(false));
                                Thread.sleep(100);
                                MainGUI.a_debug.escreveNaConsola("Loading screen over. Waiting for text to happear...");
                                waitForText();
                                Thread.sleep(100);
                            }
                        } catch (InterruptedException e) {
                            System.err.println(e.fillInStackTrace());
                        }

                        unpause();

                        if (isLoading) {
                            MainGUI.a_es.setTypeOfInterface(1);
                        }
                        next();
                        sceDisplay.activate();
                    }
                } else if (this.scene.getIndex() == 90) {
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                }
                break;
            case 227:
                if (this.scene.getIndex() == 176) {
                    stopEverythingUntilLoadingIsOver(0, 4);
                } else if (this.scene.getIndex() == 295
                        || this.scene.getIndex() == 301
                        || this.scene.getIndex() == 304
                        || this.scene.getIndex() == 320
                        || this.scene.getIndex() == 322
                        || this.scene.getIndex() == 323
                        || this.scene.getIndex() == 325
                        || this.scene.getIndex() == 329) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 240:
                if (this.scene.getIndex() == 24) {
                    stopEverythingUntilLoadingIsOver(1, 4);
                } else if (this.scene.getIndex() == 35) {
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                } else if (this.scene.getIndex() == 44) {
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                } else if (this.scene.getIndex() == 205) {
                    alvaroJapTextChecker.ignoreGUIError();
                } else if (this.scene.getIndex() == 333) {
                    stopEverythingUntilLoadingIsOver(0, 4);
                }
                break;
            case 242:
                if (scene.getIndex() == 63) {
                    stopEverythingUntilLoadingIsOver(1, 4);
                } else if (scene.getIndex() == 67) {
                    Controller.disable();
                    Controller.getInstance().stopAutoSkip();
                    Controller.getInstance().releaseEverything();
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                }
                break;
            case 245:
                if (scene.getIndex() == 103) {
                    stopEverythingUntilLoadingIsOver(1, 4);
                } else if (scene.getIndex() == 114) {
                    try {
                        // custom treatment: we wait until the fading is over and then restart.
                        sceDisplay.deactivate();
                        Color currentColor;
                        do {
                            currentColor = MainGUI.a_es.getSpecifiedPixelColor(13, 135);
                            Thread.sleep(MainGUI.getFPSV());
                        } while (currentColor.getRed() > 60 || currentColor.getBlue() > 60 || currentColor.getGreen() > 60);

                        MainGUI.a_debug.escreveNaConsola("Waiting for text to come up.");
                        waitForText();
                        MainGUI.a_debug.escreveNaConsola("Waiting for gui to come up.");
                        while (MainGUI.a_es.isGUIDown()) {
                            Thread.sleep(MainGUI.getFPSV());
                        }
                        alvaroJapTextChecker.forceResetRecog(4);
                        next();
                        sceDisplay.activate();
                    } catch (InterruptedException e) {
                    }
                } else if (this.scene.getIndex() == 118 || this.scene.getIndex() == 151) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 246:
                if (scene.getIndex() == 52) {
                    Controller.disable();
                    Controller.getInstance().stopAutoSkip();
                    Controller.getInstance().releaseEverything();
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                }
                break;
            case 248:
                if (this.scene.getIndex() == 14 || this.scene.getIndex() == 16) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                if (scene.getIndex() == 141) {
                    stopEverythingUntilLoadingIsOver(0, 6);
                }
                break;
            // 103
            case 249:
                if (this.scene.getIndex() == 232) {
                    Controller.disable();
                    Controller.getInstance().stopAutoSkip();
                    Controller.getInstance().releaseEverything();
                    sceDisplay.deactivate();
                    alvaroJapTextChecker.forceResetRecog(4);
                    next();
                    waitTillMovieEnds();
                    sceDisplay.activate();
                }
                break;
            case 251:
                // This scene needs another image comparation due to the text 
                // on index 31 being different from PurebredFaction's path.
                if (this.scene.getIndex() == 31) {
                    try {
                        this.compareSpecialImagesAndSendSV(new String[] { "CodeGeassSpeech/SceneIdentificator/S251SI01.png" });
                    } catch (IOException e) {
                        MainGUI.a_debug.escreveNaConsola("[IO EXCEPTION] - Error while treating scene 251's special case.");
                    } catch (InterruptedException e) {
                        MainGUI.a_debug.escreveNaConsola("[INTERRUPTED EXCEPTION] - Error while treating scene 251's special case.");
                    }
                }
            case 379:
                if (this.scene.getIndex() == 94) {
                    Controller.getInstance().stopAutoSkip();
                } else if (this.scene.getIndex() == 95) {
                    waitForVideoToPass();
                }
                break;
            case 419:
                if (this.scene.getIndex() == 35 || this.scene.getIndex() == 41
                        || this.scene.getIndex() == 42 || this.scene.getIndex() == 45
                        || this.scene.getIndex() == 46 || this.scene.getIndex() == 49
                        || this.scene.getIndex() == 51 || this.scene.getIndex() == 52
                        || this.scene.getIndex() == 53) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 421:
                if (this.scene.getIndex() == 21) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 435:
                if (this.scene.getIndex() == 20) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 447:
                if (this.scene.getIndex() == 81) {
                    stopEverythingUntilLoadingIsOver(1, 4);
                } else if (this.scene.getIndex() == 88
                        || this.scene.getIndex() == 92
                        || this.scene.getIndex() == 93
                        || this.scene.getIndex() == 101
                        || this.scene.getIndex() == 214
                        || this.scene.getIndex() == 234
                        || this.scene.getIndex() == 237
                        || this.scene.getIndex() == 241) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 461:
                if (this.scene.getIndex() == 119) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                if (this.scene.getIndex() == 200) {
                    waitForVideoToPass();
                }
                break;
            case 464:
                // This scene needs another image comparation due to the text 
                // on index 88 being different on two distinct ends.
                if (this.scene.getIndex() == 88) {
                    try {
                        this.compareSpecialImagesAndSendSV(new String[]{"CodeGeassSpeech/SceneIdentificator/S464SI01.png"});
                    } catch (IOException e) {
                        MainGUI.a_debug.escreveNaConsola("[IO EXCEPTION] - Error while treating scene 464's special case.");
                    } catch (InterruptedException e) {
                        MainGUI.a_debug.escreveNaConsola("[INTERRUPTED EXCEPTION] - Error while treating scene 464's special case.");
                    }

                }
            case 467:
                if (this.scene.getIndex() == 132) {
                    AUX_SCENE_SCRIPT_INFO = new String[3];
                    AUX_SCENE_SCRIPT_INFO[0] = this.getScript().getVar("ohgi");
                    AUX_SCENE_SCRIPT_INFO[1] = this.getScript().getVar("tamaki");
                    AUX_SCENE_SCRIPT_INFO[2] = this.getScript().getVar("myDamage");
                } else if (this.scene.getIndex() == 200) {
                    alvaroJapTextChecker.forceResetRecog(-1);
                    stop();
                    while (!this.isVisible()) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.err.println(e.fillInStackTrace());
                        }
                    }
                    while (this.getScript().getVar("enemyB") == null) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            System.err.println(e.fillInStackTrace());
                        }
                    }
                    boolean concurrencyError = false;
                    do {
                        try {
                            this.getScript().interpretNativeCommand("enemyA = " + AUX_SCENE_SCRIPT_INFO[0] + ";", null);
                            this.getScript().interpretNativeCommand("enemyB = " + AUX_SCENE_SCRIPT_INFO[1] + ";", null);
                            this.getScript().interpretNativeCommand("myDamage = " + AUX_SCENE_SCRIPT_INFO[2] + ";", null);
                        } catch (ConcurrentModificationException e) {
                            concurrencyError = true;
                        }
                    } while (concurrencyError);
                } else if (this.scene.getIndex() == 8
                        || this.scene.getIndex() == 20
                        || this.scene.getIndex() == 30
                        || this.scene.getIndex() == 130
                        || this.scene.getIndex() == 179
                        || this.scene.getIndex() == 182
                        || this.scene.getIndex() == 186) {
                    alvaroJapTextChecker.ignoreGUIError();
                } else if (this.scene.getIndex() == 169) {
                    waitForVideoToPass();
                }
                break;

            case 483:
                if (this.scene.getIndex() == 5) {
                    waitForVideoToPass();
                } else if (this.scene.getIndex() == 14 || this.scene.getIndex() == 16) {
                    alvaroJapTextChecker.ignoreGUIError();
                }
                break;
            case 488:
                if (this.scene.getIndex() == 26) {
                    waitForVideoToPass();
                }
                break;
            default:
                break;
        }
    }

    private void sendCommandToScript(String command) {
        CGSMScript script = this.getScript();
        if (script != null && script.isRunning()) {
            boolean concurrentError;
            do {
                concurrentError = false;
                try {
                    script.interpretNativeCommand(command, null);
                } catch (ConcurrentModificationException e) {
                    concurrentError = true;
                }
            } while (concurrentError);
        }
    }

    /**
     * This function attempts to compare special images (S-X-SI-N, with X being 
     * the scene and N the Number of the special image) and then updates the 
     * current script's SV value based on the matching image. (0 if all images 
     * failed to match, 1 if first image matched, 2 if second matched and so 
     * on...)
     * 
     * @param imagesPath The images to be compared.
     * @throws IOException In case there was an error reading the images
     * @throws InterruptedException In case the Sleeping process was stopped.
     */
    private void compareSpecialImagesAndSendSV(String[] imagesPath) 
            throws IOException, InterruptedException {
        // deactivate display
        sceDisplay.deactivate();
        // reset the recognition procedure
        alvaroJapTextChecker.forceResetRecog(4);

        // Wait until the text is fully shown (waits for the red arrow to appear)
        this.waitUntilSkipper();
        
        // get images for comparation and compare each them.
        BufferedImage currentEmulatorImage = MainGUI.a_es.extract(),
                currentSpecialImageCase;
        int svValue = 0;
        for (int k = 0; k < imagesPath.length; k++) {
            currentSpecialImageCase = ImageIO.read(new File(imagesPath[k]));
            if (CGSMScene.equalImages(currentEmulatorImage, currentSpecialImageCase)) {
                svValue = k+1;
                break;
            }
        }
        
        // Tell sv value to the script.
        this.sendCommandToScript("sv = " + svValue + ";");
        
        // we wait now until the sv is back to -1
        // SV == -1 proves that all changes are finished within 
        // the script and we are safe to call next().
        while (Integer.parseInt(this.currentActiveScript.getVar("sv")) != -1) {
            Thread.sleep(100);
        }
        // reset view port
        if (MainGUI.currentViewPortNr != 1) {
            MainGUI.instance.resizeView(MainGUI.currentViewPortNr);
        }
        sceDisplay.activate();
        // we call for the next and reset the background.
        this.initBacks();
        next();
    }

    /*
     * // special case. on 114 it recognizes text when he shouldn't recognize.
     sceDisplay.deactivate();
     alvaroJapTextChecker.forceResetRecog(6);
     next();
     try {
     waitForText(); // wait for ghost text
     Thread.sleep(300);
     waitForText(); // recognize real text.
     } catch (InterruptedException ex) {
     Logger.getLogger(alvaroDialogShower.class.getName()).log(Level.SEVERE, null, ex);
     }
     sceDisplay.activate();
     * 
     * 
     */
    private void forceSkipOne() {
        alvaroJapTextChecker.forceResetRecog(-1);
        next();
    }

    private void stopEverythingUntilLoadingIsOver(int nextInterface, int nrGUIError) {
        sceDisplay.deactivate();
        alvaroJapTextChecker.forceResetRecog(nrGUIError);
        initBacks(); // reset the backgrounds
        pause();
        waitForLoading();
        unpause();
        MainGUI.a_es.setTypeOfInterface(nextInterface);
        next();
        sceDisplay.activate();
    }

    public void waitTillMovieEnds() {
        try {
            while (!sceMpeg.a_videoPlaying) {
                Thread.sleep(100);
            }
            initBacks();
            MainGUI.a_debug.escreveNaConsola("video captured");
            Controller.enableVideoSkipping();
            while (sceMpeg.a_videoPlaying) {
                Thread.sleep(100);
            }
            MainGUI.a_debug.escreveNaConsola("video ended. waiting for text to happear.");
            Thread.sleep(100);
            waitForText();
            Controller.enable();
            MainGUI.a_debug.escreveNaConsola("text found.");
        } catch (InterruptedException e) {
            MainGUI.a_debug.escreveNaConsola(e);
        }
    }

    private void waitForLoading() {
        try {
            // catch loading
            do {
                Thread.sleep(17);
            } while (!MainGUI.a_es.isLoadingColor(false));
            MainGUI.a_debug.escreveNaConsola("Loading Screen Captured.");
            // wait 'till vanish.
            do {
                Thread.sleep(17);
            } while (MainGUI.a_es.isLoadingColor(false));
            Thread.sleep(100);
            MainGUI.a_debug.escreveNaConsola("Loading screen over. Waiting for text to happear...");
            waitForText();
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
    }

    private void waitForVideoToPass() {
        Controller.disable();
        Controller.getInstance().stopAutoSkip();
        Controller.getInstance().releaseEverything();
        sceDisplay.deactivate();
        alvaroJapTextChecker.forceResetRecog(4);
        next();
        waitTillMovieEnds();
        sceDisplay.activate();
    }

    private void waitForText() throws InterruptedException {
        boolean[] japLetters;
        do {
            Thread.sleep(17);
            japLetters = MainGUI.a_es.getJapaneseLettersBooleans(false);
        } while (!japLetters[0] && !japLetters[1] && !japLetters[2]);
    }
}
