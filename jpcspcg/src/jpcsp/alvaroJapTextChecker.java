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
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import jpcsp.HLE.modules150.sceMpeg;

/**
 * This class is the "engine" for analysing the Japanese text.
 * The analysing takes care of when the text is there and not there.
 * It works in 2 phases:
 * 1. Jap Text capture. (Being when he waits for the text to happear)
 * 2. Jap Text release. (Being when he waits for the text do disappear)
 * The second phase is where it signals for the program to advance forward on 
 * the scene's index and display it.
 * 
 * @author Alvaro
 */
public class alvaroJapTextChecker extends Thread {

    // Sometimes in events there are no text there. It requires special 
    // treatment and because of that a variable comes on the stage.
    public static boolean allowEmptyTextTreatment = true;
    
    // To force reset recognition
    private static boolean resetRecognition = false;
    
    // To reset the analysing process with a certain GUI Error 
    private static int resetWithGUIError = 2;
    
    // Ignore the gui error signaling next as fast as possible.
    private static boolean forceIgnoreGUIError = false;
    
    // Alert the program that it was an event
    private static boolean wasEvent = false;
    
    private static boolean hadRun = false;
    
    // Used when in events, to tell the program when a choice has been selected or not.
    private static int[][] firstLetterPixel = null;
    
    // this variable is used to signal the BackgroundGenerator on 
    // alvaroDialogShower to draw.
    public static boolean a_canDraw = false;
    
    // Used when the user was quicker than the analyser and the program proceeds
    // to use the normal analyzation process.
    private boolean notChosen;
    
    // used to tell if text has been already found or not.
    private boolean foundtext;
    
    // used in event analysis
    private Color start2, end2;
    
    // GUIError is the number of times (measured in frames) the program has to check if the interface 
    // is up or not before passing to the next index of the scene.
    private int errorGUI;

    public alvaroJapTextChecker() {
        this.notChosen = false;
        this.foundtext = false;
        this.start2 = new Color(150, 150, 150);
        this.end2 = new Color(255, 255, 255);
        this.errorGUI = 4; // 4 frames to check if gui is up or not.
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException ex) {
                    MainGUI.a_debug.escreveNaConsola("[alvaroJapTextChecker] - Wait interrupted.");
                }
            }
            if (MainGUI.a_dshower != null) {
                /*
                 * If its an event than this frame will only be used to get the 
                 * first letter's pixels for comparision on the next. Otherwise, 
                 * it will proceed to the normal analization.
                 */
                if (MainGUI.a_ep != null && !MainGUI.a_ep.isVisible()) {
                    if (MainGUI.a_dshower.canAnalyseLetters()) {
                        analyseJapaneseLetters();
                    }
                } else if (MainGUI.a_ep.isVisible() && !wasEvent && allowEmptyTextTreatment) {
                    BufferedImage firstLetter = MainGUI.a_es.getJapaneseLetters(-1).getSubimage(0, 0, 17, 17);
                    if (!wasEvent) {
                        wasEvent = !CGSMScene.ImageContainsColorInterval(firstLetter, new Color(180, 180, 180), Color.white);
                        if (!wasEvent) {
                            errorGUI = 2;
                            allowEmptyTextTreatment = false;
                            //MainGUI.a_debug.escreveNaConsola("EmptyTextTreatment = " + allowEmptyTextTreatment);
                        }
                    }
                    firstLetterPixel = CGSMScene.extractNegXAlgPixeis(firstLetter);
                }
            }
        }
    }

    private void analyseJapaneseLetters() {
        if (resetRecognition) {
            resetRecognition = false;
            this.foundtext = false;
            wasEvent = false;
            this.notChosen = false;
            firstLetterPixel = null;
            errorGUI = resetWithGUIError;
        }
        try {
            if (MainGUI.a_dshower.isVisible() || MainGUI.a_dshower.isPaused()) {
                BufferedImage firstLetter, secondLetter, thirdLetter;
                Color start1 = new Color(0, 0, 0);
                Color end1 = new Color(120, 120, 120);
                boolean foundChar2, foundChar3;
                int switchValue = MainGUI.mustSwitch();
                if (wasEvent && notChosen) {
                    // analysis for events
                    foundtext = false;
                    if (firstLetterPixel != null) {
                        // get pixels for the 3 first letters
                        firstLetter = MainGUI.a_es.getJapaneseLetters(-1).getSubimage(0, 0, 51, 17);
                        secondLetter = firstLetter.getSubimage(17, 0, 17, 17);
                        thirdLetter = firstLetter.getSubimage(34, 0, 17, 17);
                        firstLetter = firstLetter.getSubimage(0, 0, 17, 17);
                        // extractNegXAlgPixeis is a algorithm which takes the 
                        // pixels in an X form but since this is the "Neg" 
                        // function it only takes the line from the bottom 
                        // to the upper right corner of the image.
                        // This means he will start from position (0,17) and 
                        // end on (17,0)
                        
                        // It will only advance when the firstLetterPixels 
                        //(In the previous frame) are equal to the the pixels 
                        // from the actual frame.
                        if (!sceMpeg.a_videoPlaying && !alvaroDialogShower.equalPixels(firstLetterPixel, CGSMScene.extractNegXAlgPixeis(firstLetter))) {
                            /* Here I proceed to know if the pixels i got
                             * contain the two color intervals i specified 
                             * which are the following: 
                             * 1. (0,0,0) to (120,120,120) - Trying to get the 
                             * shadow behind the letter
                             * 2. (150,150,150) to (255,255,255) - Trying to get
                             * the white part of the letter.
                             */ 
                            foundChar2 = CGSMScene.imageContainsColorIntervalsXAlg(secondLetter, start1, end1, start2, end2);
                            foundChar3 = CGSMScene.imageContainsColorIntervalsXAlg(thirdLetter, start1, end1, start2, end2);
                            if (!foundChar2 && !foundChar3) {
                                notChosen = false;
                                firstLetterPixel = null;
                                allowEmptyTextTreatment = true;
                                wasEvent = false;
                                MainGUI.a_dshower.next();
                            }
                        }
                    } else {
                        firstLetter = MainGUI.a_es.getJapaneseLetters(-1).getSubimage(0, 0, 51, 17);
                        firstLetterPixel = CGSMScene.extractNegXAlgPixeis(firstLetter);
                        Controller.getInstance().enableGUIHider();
                    }
                } else {
                    // normal analysis
                    boolean isGuiDown = MainGUI.a_es.isGUIDown();
                    if (isGuiDown) {
                        Controller.getInstance().enableGUIHider();
                    }
                    if (!foundtext) {
                        // First Phase.
                        // this will always run until the letters appear.
                        boolean[] chars = MainGUI.a_es.getJapaneseLettersBooleans(false);
                        if (switchValue == -1 || switchValue == 1) {
                            if (!isGuiDown && (chars[1] || chars[2])) {
                                if (!sceMpeg.a_videoPlaying) {
                                    foundtext = true;
                                    a_canDraw = false;
                                    Controller.getInstance().enableGUIHider();
                                }
                            }
                        } else {
                            if (!isGuiDown && !chars[1] && !chars[2]) {
                                if (sceMpeg.a_videoPlaying) {
                                    foundtext = true;
                                    a_canDraw = false;
                                    Controller.getInstance().enableGUIHider();
                                }
                            }
                        }
                    } else {
                        // Second Phase. (Waiting for letters to go away)
                        boolean[] chars = MainGUI.a_es.getJapaneseLettersBooleans(true);
                        if (switchValue == -1) {
                            if (!chars[1] && !chars[2]) {
                                
                                // this function is used when there are special 
                                // treatments (treatments to make the analysis work)
                                // for this particular index of the scene.
                                MainGUI.a_dshower.checkIfHasTreatment();
                                if (MainGUI.isFrameRateLimited() || forceIgnoreGUIError) {
                                    this.errorGUI = 0;
                                    isGuiDown = false;
                                }
                                if (!resetRecognition) {
                                    if (!isGuiDown && this.errorGUI <= 0) {
                                        MainGUI.a_dshower.next();
                                        foundtext = false;
                                        notChosen = true;
                                        allowEmptyTextTreatment = true;
                                        forceIgnoreGUIError = false;
                                        resetWithGUIError = 4;
                                        errorGUI = 4;
                                        a_canDraw = true;
                                        hadRun = false;
                                    } else if (!isGuiDown && this.errorGUI > 0) {
                                        if (MainGUI.a_dshower.isSceneLastIndex()) {
                                            this.errorGUI = 0;
                                        } else {
                                            this.errorGUI--;
                                        }

                                    } else if (isGuiDown) {
                                        System.err.println("gui down");
                                        Controller.getInstance().enableGUIHider();
                                        foundtext = false;
                                        if (!allowEmptyTextTreatment) {
                                            this.errorGUI = 2;
                                        } else {
                                            this.errorGUI = resetWithGUIError;
                                        }
                                    }
                                } else {
                                    foundtext = false;
                                }
                            }
                        } else {
                            if (chars[1] || chars[2]) {
                                if (MainGUI.isFrameRateLimited() ||(!isGuiDown && this.errorGUI <= 0)) {
                                    MainGUI.a_dshower.next();
                                    foundtext = false;
                                    notChosen = true;
                                    allowEmptyTextTreatment = true;
                                    errorGUI = 4;
                                } else if (!isGuiDown && this.errorGUI > 0) {
                                    this.errorGUI--;
                                } else if (isGuiDown) {
                                    Controller.getInstance().enableGUIHider();
                                    foundtext = false;
                                    this.errorGUI = 4;
                                }
                            }
                        }
                    }
                }
            }
            //MainGUI.a_dshower.tryRepaint();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "exception" + e.toString());
        }
    }
    
    public synchronized void checkText() {
        notify();
    }

    public static boolean canDraw() {
        return a_canDraw;
    }

    public static void forceResetRecog(int resetGUIErrorValue) {
        resetRecognition = true;
        if (resetGUIErrorValue < 2) {
            resetWithGUIError = 2;
        } else {
            resetWithGUIError = resetGUIErrorValue;
        }
    }

    public static void ignoreGUIError() {
        forceIgnoreGUIError = true;
    }

    public static void useEventAlgorithm() {
        if (!hadRun) {
            hadRun = true;
            wasEvent = true;
            firstLetterPixel = CGSMScene.extractNegXAlgPixeis(MainGUI.a_es.getJapaneseLetters(-1).getSubimage(0, 0, 17, 17));
        }
    }
}
