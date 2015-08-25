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
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import jpcsp.HLE.Modules;
import jpcsp.HLE.modules150.sceMpeg;
import jpcsp.graphics.GeCommands;
import jpcsp.memory.IMemoryReader;
import jpcsp.memory.ImageReader;

/**
 * Attempts to extract the text in image format from the game.
 * @author Alvaro
 */
public class alvaroExtracter implements Runnable {
    // This part was extracted from the ImageViewer.java file. 
    // No right infrigment intended. All credits to the creators.
    private int startAddress = MemoryMap.START_VRAM;
    private int bufferWidth = 512;
    private int imageWidth = 480;
    private int imageHeight = 272;
    private boolean imageSwizzle = false;
    private boolean useAlpha = false;
    private int pixelFormat = GeCommands.TPSM_PIXEL_STORAGE_MODE_32BIT_ABGR8888;
    private int clutAddress = 0;
    private int clutNumberBlocks = 0;
    private int clutFormat = GeCommands.CMODE_FORMAT_32BIT_ABGR8888;
    private int clutStart = 0;
    private int clutShift = 0;
    private int clutMask = 0xFF;
    
    private class BreakingLineElement {
        private Integer mode;
        private Integer skipValue;
        private boolean onlyOnAR;
        
        private BreakingLineElement(Integer mode, boolean onlyOnAR, Integer skipValue) {
            this.mode = mode;
            this.onlyOnAR = onlyOnAR;
            this.skipValue = skipValue;
        }
        
        protected Integer getMode() {
            return this.mode;
        }
        
        protected boolean getOnlyOnAR() {
            return this.onlyOnAR;
        }
        
        protected Integer getSkipValue() {
            return this.skipValue;
        }
    }
    
    // ALVARO
    private boolean isPaused, trickySet, sceneIdentified, relax;
    private LinkedList<CGSMScene> firstSearch;
    private HashMap<Integer, BreakingLineElement> trickyCaseLineBreak;
    private int interfaceType;
    
    private final Color[] loadingColors1x1 = {
        new Color(30, 49, 55), new Color(200, 202, 200), new Color(123, 77, 148),
        new Color(156, 166, 170), new Color(35, 102, 19), new Color(11, 11, 16),
        new Color(88, 105, 112), new Color(173, 162, 140), new Color(63, 124, 198),
        new Color(74, 56, 57), new Color(118, 111, 134), new Color(156, 117, 90),
        new Color(162, 49, 11), new Color(148, 203, 198), new Color(189, 190, 189),
        new Color(187, 193, 198) 
    }, loadingColors2x1 = {
        new Color(24, 70, 93), new Color(107, 154, 74), new Color(90, 36, 90),
        new Color(85, 108, 159), new Color(38, 109, 19), new Color(156, 60, 140),
        new Color(60, 76, 85), new Color(49, 48, 33), new Color(71, 125, 198),
        new Color(167, 179, 181), new Color(101, 103, 132), new Color(85, 64, 126),
        new Color(178, 78, 11), new Color(200, 153, 195), new Color(192, 191, 189),
        new Color(181, 186, 181)
    }, loadingColors1x2 = {
        new Color(31, 49, 55), new Color(200, 202, 200), new Color(126, 80, 150),
        new Color(156, 165, 166), new Color(34, 100, 18), new Color(11, 11, 16),
        new Color(88, 107, 115), new Color(171, 158, 136), new Color(63, 124, 198),
        new Color(74, 56, 57), new Color(120, 113, 137), new Color(135, 103, 80),
        new Color(162, 51, 11), new Color(146, 198, 199), new Color(185, 186, 183),
        new Color(186, 192, 198)
    }, loadingColors2x2 = {
        new Color(24, 70, 93), new Color(105, 153, 72), new Color(86, 34, 84), 
        new Color(86, 110, 164), new Color(36, 106, 19), new Color(156, 61, 141), 
        new Color(61, 76, 86), new Color(50, 50, 34), new Color(70, 125, 198), 
        new Color(158, 169, 174), new Color(101, 101, 130), new Color(85, 64, 126), 
        new Color(178, 79, 12), new Color(202, 159, 193), new Color(192, 191, 189), 
        new Color(180, 186, 182)
    };
    
    private static Color letterShownStartColor = new Color(150, 150, 150);
    
    
    
    public alvaroExtracter() {
        //imagenumber = 0;
        this.isPaused = false;
        this.trickySet = false;
        this.firstSearch = new LinkedList<CGSMScene>();
        this.trickyCaseLineBreak = new HashMap<Integer, BreakingLineElement>();
        this.interfaceType = -1;
    }
    
    @Override
    public void run() {
        while(true) {
            while(!this.isPaused) {
                try {
                    if (!this.sceneIdentified && !sceMpeg.a_videoPlaying&& CGSMScene.ImageContainsColors(extractSceneBI(), MainGUI.a_skippingColors)) { // 
                        if (MainGUI.currentViewPortNr != 1) {
                            MainGUI.instance.resizeView(1);
                        }
                        analyse(extract());
                    } else {
                        switch (MainGUI.getAnalisSpeed()) {
                            case 1:
                                Thread.sleep(500); // 2 analises per second
                                break;
                            case 2:
                                Thread.sleep(250);// 4 analises per second
                                break;
                            case 3:
                                Thread.sleep(100); // 10 analysis per second
                                break;
                            default:
                                Thread.sleep(1000);// 1 analyse per second
                                break;
                        }
                    }
                } catch (InterruptedException e) {
                    MainGUI.a_debug.escreveNaConsola(e.fillInStackTrace());
                }
            }
        }
    }
    
    // 80% code extracted from ImageViewer.java
    /**
     * Extracts the image to be compared with the ones in every detected scene.
     * @return The image to be compared.
     */
    public BufferedImage extract() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        //MainGUI.a_debug.escreveNaConsola(this.getClass().getName() + ": shot taken");
        BufferedImage ioutput = new BufferedImage((int) (imageWidth * 0.72), (int) (imageHeight * 0.20), BufferedImage.TYPE_INT_RGB);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < minWidth; x++) {
		int colorABGR = imageReader.readNext();
		int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                outputG.setColor(new Color(colorARGB)); // ALVARO
                if (y >= ((int) (imageHeight * 0.80)) && x <= (int) (imageWidth * 0.72)) { // ALVARO 
                    drawPixel(outputG, x, y - ((int) (imageHeight * 0.80))); // ALVARO
                } // ALVARO 
            }
	}
        try { // ALVARO
            //ImageIO.write(ioutput, "PNG", new java.io.File("CodeGeassSpeach\\cgextractedImage" + imagenumber++ + ".png")); // ALVARO
            // tirar a linha de baixo para guardar as imagens.
            //ImageIO.write(ioutput, "PNG", new java.io.File("CodeGeassSpeach/ExtractedSpeach.png")); // ALVARO
            return ioutput;
        } catch (Exception e) { // ALVARO
            MainGUI.a_debug.escreveNaConsola("Extracter2:" + e);
            return null;
        } // ALVARO
    }
    
    // 80% code extracted from ImageViewer.java
    /**
     * Attempts to extract the skipping arrow to signal the program when to extract and recognize.
     * @return An image containing the skipping arrow.
     */
    public BufferedImage extractSceneBI() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        BufferedImage ioutput = new BufferedImage((int) (imageWidth * 0.10), (int) (imageHeight * 0.15), BufferedImage.TYPE_INT_ARGB);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        imageReader.skip(242255); // skips to the BI
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < minWidth; x++) {
                if (x < (int) (imageWidth * 0.10)) {
                    int colorABGR = imageReader.readNext();
                    int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                    outputG.setColor(new Color(colorARGB, useAlpha));
                    drawPixel(outputG, x, y);
                } else {
                    imageReader.skip(imageWidth - ((int) (imageWidth * 0.10)));
                    break;
                }
            }
            if (y > (0.15 * imageHeight)) {
                break;
            }
	}
        return ioutput;
    }
    
    /**
     * Extracts the background image to block the japanese text.
     * @return The background image to block the japanese text.
     */
    public BufferedImage getBackgroundBI() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int widthAux = (int) (imageWidth * 0.723), heightAux = (int) (imageHeight * 0.20) + 1;
        BufferedImage ioutput = new BufferedImage(widthAux, heightAux, BufferedImage.TYPE_INT_ARGB);
        //MainGUI.a_debug.escreveNaConsola(widthAux + ", " + heightAux);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        imageReader.skip(235200);
        for (int y = 0; y < imageHeight; y++) {
            imageReader.skip(8);
            for (int x = 0; x < minWidth; x++) {
                if (x <= 347) { // ALVARO 1px error fix
                    int colorABGR = imageReader.readNext();
                    int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                    Color c = new Color(colorARGB, useAlpha);
                    outputG.setColor(c); // ALVARO
                    drawPixel(outputG, x, y);
                } else {
                    imageReader.skip(minWidth - 356);
                    break;
                }
            }
            if (y > heightAux) {
                break;
            }
	}
        return ioutput;
    }
    
    /**
     * Attempts in analysing the dialog image to provide an answer to 
     * whether the first letter has been shown or not.
     * @return True if it has happeared or false if otherwise.
     */
    public boolean isFirstLetterShown() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int heightAux = (int) (imageHeight * 0.20) - 39;
        imageReader.skip(235200 + (imageWidth * 4));
        int skipper = 0;
        if (MainGUI.a_dshower.isMenuScene()) {
            skipper = 4*17;
        }
        for (int y = 0; y < imageHeight; y++) {
            imageReader.skip(8+17+skipper);
            for (int x = 0; x < minWidth; x++) {
                if (x <= 16) { // ALVARO 1px error fix
                    int colorABGR = imageReader.readNext();
                    int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                    Color c = new Color(colorARGB, useAlpha);
                    if (colorIsWithinInterval(c, letterShownStartColor, Color.white)) {
                        return true;
                    }
                } else {
                    imageReader.skip(minWidth - 42);
                    break;
                }
            }
            if (y > heightAux) {
                break;
            }
	}
        return false;
    }
    
    /**
     * Attempts in analysing the dialog image to provide an answer to 
     * whether the fourth letter has been shown or not.
     * @return True if it has happeared or false if otherwise.
     */
    public boolean isFourthLetterShown() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int heightAux = (int) (imageHeight * 0.20) - 39;
        imageReader.skip(235200 + (imageWidth * 4));
        int skipper = 0;
        if (MainGUI.a_dshower.isMenuScene()) {
            skipper = 4*17;
        }
        for (int y = 0; y < imageHeight; y++) {
            imageReader.skip(8+51+skipper);
            for (int x = 0; x < minWidth; x++) {
                if (x <= 20) { // ALVARO 1px error fix
                    int colorABGR = imageReader.readNext();
                    int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                    Color c = new Color(colorARGB, useAlpha);
                    if (colorIsWithinInterval(c, letterShownStartColor, Color.white)) {
                        return true;
                    }
                } else {
                    imageReader.skip(minWidth - 80);
                    break;
                }
            }
            if (y > heightAux) {
                break;
            }
	}
        return false;
    }
    
    /**
     * Attempts in analysing the dialog image to provide an answer to 
     * whether the first letter of the second line has been shown or not.
     * @return True if it has happeared or false if otherwise.
     */
    public boolean isSecondLineFirstLetterShown() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int heightAux = (int) (imageHeight * 0.20) - 39;
        imageReader.skip(235200 + (imageWidth * 24));
        for (int y = 0; y < imageHeight; y++) {
            imageReader.skip(8+17);
            for (int x = 0; x < minWidth; x++) {
                if (x <= 16) { // ALVARO 1px error fix
                    int colorABGR = imageReader.readNext();
                    int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                    Color c = new Color(colorARGB, useAlpha);
                    if (colorIsWithinInterval(c, letterShownStartColor, Color.white)) {
                        return true;
                    }
                } else {
                    imageReader.skip(minWidth - 42);
                    break;
                }
            }
            if (y > heightAux) {
                break;
            }
	}
        return false;
    }
    
    private BufferedImage extractEventButton(int skiplength) {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress,
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        //MainGUI.a_debug.escreveNaConsola(this.getClass().getName() + ": shot taken");
        BufferedImage ioutput = new BufferedImage((int) (imageWidth * 0.01), (int) (imageHeight * 0.02), BufferedImage.TYPE_INT_RGB);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        imageReader.skip(skiplength);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < minWidth; x++) {
		int colorABGR = imageReader.readNext();
		int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                outputG.setColor(new Color(colorARGB)); // ALVARO
                drawPixel(outputG, x, y); 
            }
            if (y > (int) (imageHeight * 0.02)) {
                break;
            }
	}
        try { 
            return ioutput;
        } catch (Exception e) { // ALVARO
            MainGUI.a_debug.escreveNaConsola("Extracter3:" + e);
            return null;
        } 
        // ALVARO
    }
    
    /**
     * Extracts the image of a Triangle button from an event.
     * @return The image of a Triangle button from an event.
     */
    public BufferedImage extractEventButtonTriangle() {
        return this.extractEventButton(38544);
        // ALVARO
    }
    
    /**
     * Extracts the image of a Cross button from an event.
     * @return The image of a Cross button from an event.
     */
    public BufferedImage extractEventButtonCross() {
        return this.extractEventButton(75025);
        // ALVARO
    }
    
    /**
     * Extracts the image of a Square button from an event.
     * @return The image of a Square button from an event.
     */
    public BufferedImage extractEventButtonSquare() {
        return this.extractEventButton(74908);
        // ALVARO
    }
    
    /**
     * Extracts the image of a Circle button from an event.
     * @return The image of a Circle button from an event.
     */
    public BufferedImage extractEventButtonCircle() {
        return this.extractEventButton(75142);
        // ALVARO
    }
    
    /**
     * Extracts the background image from an event.
     * @return The background image from an event.
     */
    public BufferedImage extractEventBackgroundBI() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        //MainGUI.a_debug.escreveNaConsola(this.getClass().getName() + ": shot taken");
        BufferedImage ioutput = new BufferedImage(imageWidth, (int) (imageHeight * 0.50), BufferedImage.TYPE_INT_RGB);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < minWidth; x++) {
		int colorABGR = imageReader.readNext();
		int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                outputG.setColor(new Color(colorARGB)); // ALVARO
                if (y >= imageHeight * 0.25) { // ALVARO 
                    drawPixel(outputG, x, y - ((int) (imageHeight * 0.25))); // ALVARO
                } // ALVARO 
            }
	}
        try {
            return ioutput;
        } catch (Exception e) { // ALVARO
            MainGUI.a_debug.escreveNaConsola("Extracter4:" + e);
            return null;
        } 
        // ALVARO
    }
    
    /**
     * Attempts at extracting an image of the timer in an event.
     * @return An image of the timer in an event.
     */
    public BufferedImage extractTimerEventBackgroundBI() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        //MainGUI.a_debug.escreveNaConsola(this.getClass().getName() + ": shot taken");
        BufferedImage ioutput = new BufferedImage(imageWidth, (int) (imageHeight * 0.17), BufferedImage.TYPE_INT_RGB);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        imageReader.skip(46080);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < minWidth; x++) {
		int colorABGR = imageReader.readNext();
		int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                outputG.setColor(new Color(colorARGB)); // ALVARO
                drawPixel(outputG, x, y);
            }
            if (y > (int) (imageHeight * 0.17)) {
                break;
            }
	}
        try { // ALVARO
            return ioutput;
        } catch (Exception e) { // ALVARO
            MainGUI.a_debug.escreveNaConsola("Extracter4:" + e);
            return null;
        } 
    }
    
    /**
     * Provides an image contaning the tree first japanese letters.
     * @return An image contaning the tree first japanese letters.
     */
    public BufferedImage getJapaneseLetters(int forceMode) {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int widthAux = 51, heightAux = 17;
        BufferedImage ioutput = new BufferedImage(widthAux, heightAux, BufferedImage.TYPE_INT_ARGB);
        //MainGUI.a_debug.escreveNaConsola(widthAux + ", " + heightAux);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        if (!this.trickyCaseLineBreak.isEmpty() || forceMode != -1) {
            if (forceMode == -1) {
                Integer sceneIndex = new Integer(MainGUI.a_dshower.getSceneCurrentIndex());
                BreakingLineElement mode = this.trickyCaseLineBreak.get(sceneIndex);
                if (mode != null) {
                    if (mode.getMode().intValue() < 3) {
                        imageReader.skip(236186 + (479 * 2));
                        imageReader.skip((236665 + minWidth - 1) + (minWidth * (17 * mode.getMode().intValue())));
                    } else {
                        imageReader.skip(236186 + (479 * 2));
                        imageReader.skip(30);
                    }
                } else {
                   imageReader.skip(236186 + (479 * 2));
                }
            } else {
                if (forceMode < 3) {
                    imageReader.skip((236665 + minWidth - 1) + (minWidth * (16 * forceMode)));
                } else {
                    imageReader.skip(236186 + (479 * 2));
                    imageReader.skip(30);
                }
            }
        } else {
            if (forceMode < 3 && forceMode != -1) {
                imageReader.skip((236665 + minWidth - 1) + (minWidth * (16 * forceMode)));
            } else {
                imageReader.skip(236186 + (479 * 2));
                imageReader.skip(30);
            }
        }
        if (!MainGUI.a_dshower.isMenuScene()) {
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < minWidth; x++) {
                    int colorABGR = imageReader.readNext();
                    int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                    outputG.setColor(new Color(colorARGB, useAlpha)); // ALVARO
                    if (x < 51) { // ALVARO 1px error fix
                        drawPixel(outputG, x, y);
                    } else {
                        imageReader.skip(minWidth - 52);
                        break;
                    }
                }
                if (y > heightAux) {
                    break;
                }
            }
        } else {
            for (int y = 0; y < imageHeight; y++) {
                for (int x = 0; x < minWidth; x++) {
                    int colorABGR = imageReader.readNext();
                    int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                    outputG.setColor(new Color(colorARGB, useAlpha)); // ALVARO
                    if (x > 51 && x < 103) { 
                        drawPixel(outputG, x - 52, y);
                    } else if (x < 52) {
                        imageReader.skip(51);
                        x = 51;
                    } else if (x > 102) {
                        imageReader.skip(minWidth - 104);
                        break;
                    }
                }
                if (y > heightAux) {
                    break;
                }
            }
        }
        return ioutput;
    }
    
    /**
     * Tells whether if the three japanese letters have happeared or not using the X alg.
     * @param isAr Tells whether this function is beeing accessed after the jap text have been already catched.
     * @return True if had happeared, false otherwise.
     */
    public boolean[] getJapaneseLettersBooleans(boolean isAr) {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int heightAux = 17;
        
        if (!this.trickyCaseLineBreak.isEmpty()) {
            Integer sceneIndex = new Integer(MainGUI.a_dshower.getSceneCurrentIndex());
            BreakingLineElement mode = this.trickyCaseLineBreak.get(sceneIndex);
            if (mode != null) {
                    if (mode.getMode().intValue() < 3) {
                        if (!mode.onlyOnAR || (mode.onlyOnAR == isAr)) {
                            imageReader.skip((236665 + minWidth - 1) + (minWidth * (16 * mode.getMode().intValue())));
                        } else {
                            imageReader.skip(236665 + minWidth - 1);
                        }
                    } else {
                        switch(mode.getMode().intValue()) {
                            case 3:
                                imageReader.skip(30);
                                imageReader.skip(236665 + minWidth - 1);
                                break;
                            case 4:
                                imageReader.skip(236665 + minWidth - 6);
                                break;
                            case 5:
                                imageReader.skip(236665 + minWidth - 10);
                                break;
                            case 6:
                                imageReader.skip(236665 + minWidth - 4);
                            default:
                                break;
                        }
                    }
            } else {
                imageReader.skip(236665 + minWidth - 1);
            }
        } else {
            imageReader.skip(236665 + minWidth - 1);
        }
        int initialSkip = 15; // we will skip every 15 
        boolean[] threeLetterBooleans = { false, false, false, false, false, false };
        // Color[] interval = new Color[] { new Color(0, 0, 0), new Color(120, 120, 120), new Color(150, 150, 150), Color.white }; previous
        Color[] interval = new Color[] { new Color(0, 0, 0), new Color(120, 120, 120), letterShownStartColor, Color.white };
        if (!MainGUI.a_dshower.isMenuScene()) {
            for (int y = 0; y < imageHeight; y++) { // y works as our skip between letters
                for (int k = 0; k < 3; k++) { // 3 because we need to read 3 space letters
                    //checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, k, outputG, k == 0);
                    checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, k);
                }
                imageReader.skip(minWidth - 51);
                
                if (y != 8) {
                    initialSkip -= 2;
                }
                if (y >= heightAux - 1 || 
                        (threeLetterBooleans[2] && threeLetterBooleans[3]) || 
                        (threeLetterBooleans[4] && threeLetterBooleans[5])) { // 
                    break;
                }
            }
        } else {
            imageReader.skip(51);
            for (int y = 0; y < imageHeight; y++) {
                for (int k = 0; k < 3; k++) { // 3 because we need to read 3 space letters
                    //checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, k, outputG, k == 0);
                    checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, k);
                }
                imageReader.skip(minWidth - 51);
                
                if (y != 8) {
                    initialSkip -= 2;
                }
                // || (threeLetterBooleans[2] && threeLetterBooleans[3]) || (threeLetterBooleans[4] && threeLetterBooleans[5])
                if (y >= heightAux - 1 || 
                        (threeLetterBooleans[2] && threeLetterBooleans[3]) || 
                        (threeLetterBooleans[4] && threeLetterBooleans[5])) { 
                    break;
                }
            }
        }
        //System.err.println(isARON);
        return new boolean[] { (threeLetterBooleans[0] && threeLetterBooleans[1]),
            (threeLetterBooleans[2] && threeLetterBooleans[3]),
            (threeLetterBooleans[4] && threeLetterBooleans[5])};
    }
    
    public BufferedImage getJapaneseLettersBooleansImage(boolean isAr) {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int heightAux = 17;
        BufferedImage bi = new BufferedImage(51, 17, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = bi.createGraphics();
        if (!this.trickyCaseLineBreak.isEmpty()) {
            Integer sceneIndex = new Integer(MainGUI.a_dshower.getSceneCurrentIndex());
            BreakingLineElement mode = this.trickyCaseLineBreak.get(sceneIndex);
            if (mode != null) {
                    if (mode.getMode().intValue() < 3) {
                        if (!mode.onlyOnAR || (mode.onlyOnAR == isAr)) {
                            imageReader.skip((236665 + minWidth - 1) + (minWidth * (16 * mode.getMode().intValue())));
                        } else {
                            imageReader.skip(236665 + minWidth - 1);
                        }
                    } else {
                        switch(mode.getMode().intValue()) {
                            case 3:
                                imageReader.skip(30);
                                imageReader.skip(236665 + minWidth - 1);
                                break;
                            case 4:
                                imageReader.skip(236665 + minWidth - 6);
                                break;
                            case 5:
                                imageReader.skip(236665 + minWidth - 10);
                                break;
                            case 6:
                                imageReader.skip(236665 + minWidth - 4);
                            default:
                                break;
                        }
                    }
            } else {
                imageReader.skip(236665 + minWidth - 1);
            }
        } else {
            imageReader.skip(236665 + minWidth - 1);
        }
        int initialSkip = 15; // we will skip every 15 
        boolean[] threeLetterBooleans = { false, false, false, false, false, false };
        // Color[] interval = new Color[] { new Color(0, 0, 0), new Color(120, 120, 120), new Color(150, 150, 150), Color.white }; previous
        Color[] interval = new Color[] { new Color(0, 0, 0), new Color(120, 120, 120), letterShownStartColor, Color.white };
        if (!MainGUI.a_dshower.isMenuScene()) {
            for (int y = 0; y < imageHeight; y++) { // y works as our skip between letters
                for (int k = 0; k < 3; k++) { // 3 because we need to read 3 space letters
                    //checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, k, outputG, k == 0);
                    checkLetterRowDrawing(imageReader, interval, initialSkip, y, threeLetterBooleans, k, g2d);
                }
                imageReader.skip(minWidth - 51);
                
                if (y != 8) {
                    initialSkip -= 2;
                }
                if (y >= heightAux - 1 || 
                        (threeLetterBooleans[2] && threeLetterBooleans[3]) || 
                        (threeLetterBooleans[4] && threeLetterBooleans[5])) {
                    System.err.println("breaked out");// 
                    break;
                }
            }
        } else {
            imageReader.skip(51);
            for (int y = 0; y < imageHeight; y++) {
                for (int k = 0; k < 3; k++) { // 3 because we need to read 3 space letters
                    //checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, k, outputG, k == 0);
                    checkLetterRowDrawing(imageReader, interval, initialSkip, y, threeLetterBooleans, k, g2d);
                }
                imageReader.skip(minWidth - 51);
                
                if (y != 8) {
                    initialSkip -= 2;
                }
                // || (threeLetterBooleans[2] && threeLetterBooleans[3]) || (threeLetterBooleans[4] && threeLetterBooleans[5])
                if (y >= heightAux - 1 || 
                        (threeLetterBooleans[2] && threeLetterBooleans[3]) || 
                        (threeLetterBooleans[4] && threeLetterBooleans[5])) { 
                    break;
                }
            }
        }
        return bi;
    }
    
    /**
     * Checks whether the fourth japanese letter have happeared.
     * @return True if had happeared, false if otherwise.
     */
    public boolean checkFourthJapaneseLetter() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift, clutMask, null, null);
        int heightAux = 17;
        imageReader.skip(236665 + (minWidth - 1) + 51);
        if (!this.trickyCaseLineBreak.isEmpty()) {
            Integer sceneIndex = new Integer(MainGUI.a_dshower.getSceneCurrentIndex());
            BreakingLineElement mode = this.trickyCaseLineBreak.get(sceneIndex);
            if (mode != null) {
                if (mode.getMode().intValue() < 3) {
                    imageReader.skip(minWidth * (17 * mode.getMode().intValue()) + (10 * (mode.getMode().intValue() - 1)));
                } else {
                    imageReader.skip(30);
                }
            }
        }
        int initialSkip = 15; // we will skip every 15 
        boolean[] threeLetterBooleans = { false, false };
        // Color[] interval = new Color[] { new Color(0, 0, 0), new Color(120, 120, 120), new Color(150, 150, 150), Color.white }; previous
        Color[] interval = new Color[] { new Color(0, 0, 0), new Color(120, 120, 120), letterShownStartColor, Color.white };
        if (!MainGUI.a_dshower.isMenuScene()) {
            for (int y = 0; y < imageHeight; y++) { // y works as our skip between letters
                checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, 0);
                imageReader.skip(minWidth - 17);
                if (y != 8) {
                    initialSkip -= 2;
                }
                // || (threeLetterBooleans[2] && threeLetterBooleans[3]) || (threeLetterBooleans[4] && threeLetterBooleans[5])
                if (y >= heightAux - 1 || (threeLetterBooleans[0] && threeLetterBooleans[1])) { 
                    break;
                }
            }
        } else {
            imageReader.skip(51);
            for (int y = 0; y < imageHeight; y++) {
                checkLetterRow(imageReader, interval, initialSkip, y, threeLetterBooleans, 0);
                imageReader.skip(minWidth - 17);
                if (y != 8) {
                    initialSkip -= 2;
                }
                // || (threeLetterBooleans[2] && threeLetterBooleans[3]) || (threeLetterBooleans[4] && threeLetterBooleans[5])
                if (y >= heightAux - 1 || (threeLetterBooleans[0] && threeLetterBooleans[1])) { 
                    break;
                }
            }
        }
        return threeLetterBooleans[0] && threeLetterBooleans[1];
    }
    
    private void checkLetterRow(IMemoryReader imageReader, Color[] interval, int skipBetweenPixel, int y, boolean[] letterBooleans, int letterNr) {
        boolean noNeedToCheck = false;
        if (letterBooleans[letterNr * 2] && letterBooleans[(letterNr * 2) + 1]) {
            noNeedToCheck = true;
        }
        
        Color currentColor;
        skipBetweenPixel = Math.abs(skipBetweenPixel);
        //int virginY = y;
        if (y > 8) {
            y = 16 - y;
        }
        if (noNeedToCheck) {
            imageReader.skip(17);
        } else {
            imageReader.skip(y);
            currentColor = new Color(ImageReader.colorABGRtoARGB(imageReader.readNext()), useAlpha);
            checkCurrentColor(currentColor, interval, letterBooleans, letterNr);
            if (y != 8) {
                imageReader.skip(skipBetweenPixel);

                currentColor = new Color(ImageReader.colorABGRtoARGB(imageReader.readNext()), useAlpha);
                checkCurrentColor(currentColor, interval, letterBooleans, letterNr);
                /*if (draw) {
                    g2d.setPaint(currentColor);
                    System.err.println("drawing pixel on: " + (16 - y) + ", " + y + ", with color = " + currentColor);
                    drawPixel(g2d, 16 - y, virginY);
                    System.err.println();
                }*/
            }
            imageReader.skip(y);
        }
        
        
        /*if (draw) {
            g2d.setPaint(currentColor);
            System.err.println("drawing pixel on: " + y + ", " + y + ", with color = " + currentColor);
            drawPixel(g2d, y, virginY);
        }*/
        
        
        
    }
    
    private void checkLetterRowDrawing(IMemoryReader imageReader, Color[] interval, int skipBetweenPixel, int y, boolean[] letterBooleans, int letterNr, Graphics2D g2d) {
        boolean noNeedToCheck = false;
        
        if (letterBooleans[letterNr * 2] && letterBooleans[(letterNr * 2) + 1]) {
            noNeedToCheck = true;
        }
        
        Color currentColor;
        skipBetweenPixel = Math.abs(skipBetweenPixel);

        int virginY = y;

        if (y > 8) {
            y = 16 - y;
        }
        
        if (noNeedToCheck) {
            imageReader.skip(17);
        } else {
            imageReader.skip(y);
            currentColor = new Color(ImageReader.colorABGRtoARGB(imageReader.readNext()), useAlpha);
            checkCurrentColor(currentColor, interval, letterBooleans, letterNr);

            g2d.setPaint(currentColor);
            System.err.println("drawing pixel first pixel on: " + (y + (16 * letterNr)) + ", " + y + ", with color = " + currentColor);
            drawPixel(g2d, y + (16 * letterNr), virginY);
            System.err.println();

            if (y != 8) {
                imageReader.skip(skipBetweenPixel);

                currentColor = new Color(ImageReader.colorABGRtoARGB(imageReader.readNext()), useAlpha);
                checkCurrentColor(currentColor, interval, letterBooleans, letterNr);

                g2d.setPaint(currentColor);
                System.err.println("drawing second pixel on: " + (16 - y) * (letterNr + 1) + ", " + y + ", with color = " + currentColor);
                drawPixel(g2d, (16  * (letterNr + 1) - y), virginY);
                System.err.println();

            }
            imageReader.skip(y);
        }


    }
    
    private void checkCurrentColor(Color c, Color[] interval, boolean[] letterBooleans, int letterNr) {
        if (!letterBooleans[letterNr *2 ] && colorIsWithinInterval(c, interval[0], interval[1])) {
            letterBooleans[letterNr * 2] = true;
        }
        if (!letterBooleans[(letterNr * 2) + 1] && colorIsWithinInterval(c, interval[2], interval[3])) {
            letterBooleans[(letterNr * 2) + 1] = true;
        }
    }
    
    private boolean colorIsWithinInterval(Color c, Color start, Color end) {
        if (start.getRed() <= c.getRed() && c.getRed() <= end.getRed() && 
                start.getGreen() <= c.getGreen() && c.getGreen() <= end.getGreen() && 
                start.getBlue() <= c.getBlue() && c.getBlue() <= end.getBlue()) {
            return true;
        }
        return false;
    }
    
    /**
    * Checks if the GUI is down or not (L1 button) with 0 additionalSkip.
    * @return True if is down, false if is not.
    */
    public boolean isGUIDown() {
        return isGUIDown(0, null, true);
                
    }
    
   /**
    * Checks if the GUI is down or not (L1 button) with more specific details.
    * @return True if is down, false if is not.
    * @throws IllegalArgumentException If the additionalSkip gets out of bounds [-50, imageWidth-150].
    */
    public boolean isGUIDown(int additionalSkip, Color raiseBlackColor, boolean useSecondColor) throws IllegalArgumentException {
        if (additionalSkip < -50 || additionalSkip > (imageWidth - 150)) {
            throw new IllegalArgumentException();
        }
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        switch (this.interfaceType) {
            case 1:  
                imageReader.skip((233800 - imageWidth) + additionalSkip);
                break;
            default:
                if (MainGUI.a_dshower.isMenuScene()) {
                    imageReader.skip((233800 + imageWidth) + additionalSkip);
                } else {
                    imageReader.skip(233800 + additionalSkip);
                }
                //imageReader.skip(233800);
                
                break;
        }
	int colorARGB = ImageReader.colorABGRtoARGB(imageReader.readNext());
        imageReader.skip(100);
        int colorARGB2 = ImageReader.colorABGRtoARGB(imageReader.readNext());
        Color color2Start, color2End;
        switch (this.interfaceType){
            case 1:
                if (raiseBlackColor == null){
                    color2Start = new Color(20, 20, 20);
                } else {
                    color2Start = new Color(20 + raiseBlackColor.getRed(), 20 + raiseBlackColor.getGreen(), 20 + raiseBlackColor.getBlue());
                }
                color2End = new Color(140,140,140);
                return !isColorWithinInterval(new Color(colorARGB, useAlpha), color2Start, color2End) || (useSecondColor && !isColorWithinInterval(new Color(colorARGB2, useAlpha), color2Start, color2End));
            case 2:
                if (raiseBlackColor == null){
                    color2Start = new Color(20, 20, 20);
                } else {
                    color2Start = new Color(20 + raiseBlackColor.getRed(), 20 + raiseBlackColor.getGreen(), 20 + raiseBlackColor.getBlue());
                }
                color2End = new Color(140,140,140);
                return !isColorWithinInterval(new Color(colorARGB, useAlpha), color2Start, color2End) || (useSecondColor && !isColorWithinInterval(new Color(colorARGB2, useAlpha), color2Start, color2End));
            default:
                Color color1 = new Color(225, 225, 225);
                return !isColorWithinInterval(new Color(colorARGB, useAlpha), color1, Color.white) || (useSecondColor && !isColorWithinInterval(new Color(colorARGB2, useAlpha), color1, Color.white));
        }
                
    }
    
    /**
     * Contrary to the "isGuiDown" this provides the image itself.
     * @return The image used in the analysis of the GUI.
     */
    public BufferedImage getGUIDownImage() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress,
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        //MainGUI.a_debug.escreveNaConsola(this.getClass().getName() + ": shot taken");
        BufferedImage ioutput = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        imageReader.skip(233800 - imageWidth);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < minWidth; x++) {
		int colorABGR = imageReader.readNext();
		int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                outputG.setColor(new Color(colorARGB)); // ALVARO
                if (x < 50) {
                    drawPixel(outputG, x, y);
                }
            }
            if (y > 50) {
                break;
            }
	}
        try { // ALVARO
            return ioutput;
        } catch (Exception e) { // ALVARO
            MainGUI.a_debug.escreveNaConsola("Extracter4:" + e);
            return null;
        } 
    }
    
    /**
     * Checks if the loading screen is on or off.
     * @param debug True enables the debug, false otherwise.
     * @return True if the loading screen is on, false otherwise.
     */
    public boolean isLoadingColor(boolean debug) {
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress,
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        imageReader.skip(imageWidth * (imageHeight / 2));
        imageReader.skip((imageWidth / 2));
        int colorABGR = imageReader.readNext();
	int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
        Color target = new Color(colorARGB);
        if (debug) {
            MainGUI.a_debug.escreveNaConsola("** x" + MainGUI.currentViewPortNr + " ** Loading Color: " + target);
        }
        for (int k = 0; k < loadingColors1x1.length; k++) {
            if (debug) {
                MainGUI.a_debug.escreveNaConsola("** x" + MainGUI.currentViewPortNr + " ** Comparing with... " + loadingColors1x1[k]);
            }
            if (MainGUI.currentViewPortNr == 1) {
                if (isLoadingColorAux(target, loadingColors1x1[k])) {
                    if (debug) {
                        MainGUI.a_debug.escreveNaConsola("** x" + MainGUI.currentViewPortNr + " ** Matched!");
                    }
                    return isLoadingColorAux(getNextLoadColor(imageReader), loadingColors2x1[k]);
                }
            } else {
                if (isLoadingColorAux(target, loadingColors1x2[k])) {
                    if (debug) {
                        MainGUI.a_debug.escreveNaConsola("** x" + MainGUI.currentViewPortNr + " ** Matched!");
                    }
                    return isLoadingColorAux(getNextLoadColor(imageReader), loadingColors2x2[k]);
                }
            }
        }
        return false;
    }
    
    private Color getNextLoadColor(IMemoryReader imageReader) {
        imageReader.skip(48);
        int colorABGR = imageReader.readNext();
        int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
        return new Color(colorARGB);
    }
    
    private boolean isLoadingColorAux(Color target, Color loadingColor) {
        final int limit = 3;
        if (Math.abs(target.getRed() - loadingColor.getRed()) <= limit && 
                Math.abs(target.getGreen() - loadingColor.getGreen()) <= limit && 
                Math.abs(target.getBlue() - loadingColor.getBlue()) <= limit) {
            return true;
        }
        return false;
    }
    
    /**
     * Provides the image used in the analysis of the loading screen.
     * @return The image used in the analysis of the loading screen.
     */
    public BufferedImage getLoadingImage() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        //MainGUI.a_debug.escreveNaConsola(this.getClass().getName() + ": shot taken");
        BufferedImage ioutput = new BufferedImage(50, 50, BufferedImage.TYPE_INT_RGB);
	Graphics2D outputG = ioutput.createGraphics(); // ALVARO
        imageReader.skip(imageWidth * (imageHeight / 2));
        imageReader.skip(imageWidth / 2);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < minWidth; x++) {
		int colorABGR = imageReader.readNext();
		int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                outputG.setColor(new Color(colorARGB)); // ALVARO
                if (x < 50) {
                    drawPixel(outputG, x, y);
                }
            }
            if (y > 50) {
                break;
            }
	}
        try { // ALVARO
            return ioutput;
        } catch (Exception e) { // ALVARO
            MainGUI.a_debug.escreveNaConsola("Extracter4:" + e);
            return null;
        }
    }
    
    /**
     * Attemps to retrieve a especified pixel's color giving its position.
     * @param x The x position
     * @param y The y position
     * @return The color of the pixel at the desired position.
     */
    public Color getSpecifiedPixelColor(int x, int y) {
        int minWidth = Math.min(imageWidth, bufferWidth);
        if ( x < 0 || x >= minWidth || y >= imageHeight || y < 0) {
            return null;
        }
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        imageReader.skip((minWidth * y) + x);
        return new Color(ImageReader.colorABGRtoARGB(imageReader.readNext()));
    }
    
    /**
     * Tells if the game's autoskip event is enabled.
     * @return True if is on, false if otherwise.
     */
    public boolean isAutoSkipping() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        int finalSkip = (int)(minWidth * 0.75);
        int heightSkip;
        if (this.interfaceType != 0) {
            heightSkip = imageHeight - 16;
            finalSkip += 6;
            if (this.interfaceType == 1) {
                finalSkip += 1;
            }
        } else {
            heightSkip = imageHeight - 13;
        }
        imageReader.skip((minWidth * heightSkip) + finalSkip);
        Color readedColor = new Color(ImageReader.colorABGRtoARGB(imageReader.readNext()), useAlpha);
        return isColorWithinInterval(readedColor, new Color(235, 235, 235), Color.white);
    }
    
    public BufferedImage getAutoSkippingImage() {
        int minWidth = Math.min(imageWidth, bufferWidth);
        IMemoryReader imageReader = ImageReader.getImageReader(startAddress, 
                imageWidth, imageHeight, bufferWidth, pixelFormat, imageSwizzle,
                clutAddress, clutFormat, clutNumberBlocks, clutStart, clutShift,
                clutMask, null, null);
        
        BufferedImage bi = new BufferedImage(50, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D outputG = (Graphics2D) bi.getGraphics();
        int finalSkip = (int)(minWidth * 0.75);
        int heightSkip;
        if (this.interfaceType != 0) {
            heightSkip = imageHeight - 16;
            finalSkip += 7;
        } else {
            heightSkip = imageHeight - 13;
        }
        imageReader.skip((minWidth * heightSkip));
        for (int y = 0; y < imageHeight; y++) {
            imageReader.skip(finalSkip);
            for (int x = 0; x < minWidth; x++) {
		int colorABGR = imageReader.readNext();
		int colorARGB = ImageReader.colorABGRtoARGB(colorABGR);
                outputG.setColor(new Color(colorARGB)); // ALVARO
                if (x < 50) {
                    drawPixel(outputG, x, y);
                } else {
                    imageReader.skip(imageWidth - (finalSkip + 51));
                    break;
                }
            }
            if (y > 50) {
                break;
            }
	}
        try { // ALVARO
            return bi;
        } catch (Exception e) { // ALVARO
            MainGUI.a_debug.escreveNaConsola("Extracter4:" + e);
            return null;
        }
    }
    
    /**
     * Checks if a color is within and interval(start-end)
     * @param target The color to be checked.
     * @param start The start of the interval
     * @param end The end of the interval
     * @return True if is between start and end, false if otherwise.
     */
    public static boolean isColorWithinInterval(Color target, Color start, Color end) {
        if (start == null || end == null) {
            return true;
        }
        if (target.getRed() < start.getRed() || target.getGreen() < start.getGreen() || target.getBlue() < start.getBlue()) {
            return false;
        } else {
            if (target.getRed() > end.getRed() || target.getGreen() > end.getGreen() || target.getBlue() > end.getBlue()) {
                return false;
            }
            return true;
        }
    }
    
    // Extracted From ImageViewer, I hold nothing.
    private void drawPixel(Graphics g, int x, int y) {
        
        g.drawLine(x, y, x, y);
    }
    
    /**
     * Tells if the currently identified scene is a tricky scene.
     * @return True if it is, false if otherwise.
     */
    public boolean isTrickyScene() {
        return this.trickySet;
    }
    
    /**
     * Forces a scene to be unidentified, re-enabling the analysing process again.
     */
    public void setUnIdentified() {
        this.sceneIdentified = false;
    }
    
    /**
     * Signals the program that the scene has been treated.
     */
    public void setTrickySceneTreated() {
        this.trickySet = false;
        this.sceneIdentified = true;
    }
    /*
     * When there is more than one scene that has a first image equal then the 
     * job is for the DialogShower to get the correct.
     */
    public void analyse(BufferedImage f) {
        // Analyse images -- ALVARO
        // once the scene is identified the its the control's job to get the next speach.
        if (!MainGUI.a_dshower.isVisible() || trickySet) {
            if (f != null) {
                if (!trickySet) {
                    this.firstSearch = MainGUI.a_cache.getScenes(f, 0);
                    if (this.firstSearch.size() > 0) {
                        // FOUND
                        this.sceneIdentified = true;
                        CGSMScene sceneaux;
                        if (this.firstSearch.size() == 1) {
                            // normal (awesome)
                            sceneaux = this.firstSearch.pop();
                            this.initScene(sceneaux);
                            this.interfaceType = this.getTypeOfInterface(MainGUI.a_cache.getSceneIndex(sceneaux));
                            if (interfaceType != 0) {
                                Controller.getInstance().disableGUIHider();
                                Controller.getInstance().turnOffGUIHiderFunct();
                            } else {
                                Controller.getInstance().turnOnGUIHiderFunct();
                                Controller.getInstance().enableGUIHider();
                            }
                            try {
                                String caseInfo = MainGUI.getCorrespondentTrickyLineBreak(MainGUI.a_cache.getSceneIndex(sceneaux));
                                this.trickyCaseLineBreak.clear();
                                String[] leftPart = caseInfo.substring(0, caseInfo.indexOf("/")).split(","), rightPart = caseInfo.substring(caseInfo.indexOf("/") + 1).split(",");
                                Object[] currentLineBreakInfo;
                                for (int k = 0; k < leftPart.length; k++) {
                                    currentLineBreakInfo = this.getLineBreakInfo(rightPart[k]);
                                    this.trickyCaseLineBreak.put(Integer.parseInt(leftPart[k]), 
                                            new BreakingLineElement(Integer.class.cast(currentLineBreakInfo[0]), 
                                            Boolean.class.cast(currentLineBreakInfo[1]), 
                                            Integer.class.cast(currentLineBreakInfo[2])));
                                }
                            } catch (NoSuchElementException e) {
                                // do nothing
                                this.trickyCaseLineBreak.clear();
                            }
                            if (MainGUI.currentViewPortNr != 1) {
                                MainGUI.instance.resizeView(MainGUI.currentViewPortNr);
                            }
                            MainGUI.a_debug.escreveNaConsola("HERE");
                        } else {
                            //Tricky
                            this.trickySet = true;
                            MainGUI.a_debug.escreveNaConsola("TrickyScene - TrickySet: " + trickySet);
                            sceneaux = this.firstSearch.peek();
                            this.initScene(sceneaux); // just show the first line
                            if (MainGUI.currentViewPortNr != 1) {
                                MainGUI.instance.resizeView(MainGUI.currentViewPortNr);
                            }
                        }
                    }
                } else {
                    // DialogShower part
                    this.trickySet = false;
                    try {
                        MainGUI.a_dshower.stop();
                        CGSMScene scene = alvaroCGSceneCache.searchInScenesList(this.firstSearch, f);
                        
                        if (scene != null) {
                            scene.setIndex(-1); // second dialog (-1 equals the first)
                            initScene(scene);
                            MainGUI.a_debug.escreveNaConsola("HERE2");
                            this.firstSearch = new LinkedList<CGSMScene>();
                        }
                    } catch (IOException e) {
                        MainGUI.a_debug.escreveNaConsola("AExtractor:" + e);
                    }
                    if (MainGUI.currentViewPortNr != 1) {
                        MainGUI.instance.resizeView(MainGUI.currentViewPortNr);
                    }
                }
            } else {
                MainGUI.a_dshower.stop();
                
            }
        }
    }
    
    private Object[] getLineBreakInfo(String leftPart) {
        int arIndex = leftPart.indexOf("ar");
        if (arIndex != -1) {
            int number = Integer.parseInt(leftPart.substring(0, arIndex));
            int skipValue = Integer.parseInt(leftPart.substring(arIndex + 2));
            return new Object[] { number , true, skipValue };
        } else {
            return new Object[] { Integer.parseInt(leftPart), false, 0 };
        }
    }
    
    private void initScene(CGSMScene scene) {
        MainGUI.a_dshower.setDialog(scene);
        if (MainGUI.soundHelper) {
            MainGUI.setPlayListWithIndex(MainGUI.a_cache.getSceneIndex(scene));
        }
        MainGUI.a_dshower.start();
    }
    
    private int getTypeOfInterface(int sceneIndex) {
        for (int k = 0; k < MainGUI.a_blueInterfaceScenes.length; k++) {
            if (sceneIndex == MainGUI.a_blueInterfaceScenes[k]) {
                return 1;
            }
        }
        for (int k = 0; k < MainGUI.a_redInterfaceScenes.length; k++) {
            if (sceneIndex == MainGUI.a_redInterfaceScenes[k]) {
                return 2;
            }
        }
        return 0;
    }
    
    /**
     * Set the type of an interface manually.
     * @param type - 0- Normal interface, 1- Blue interface, 2- Red interface.
     */
    public void setTypeOfInterface(int type) {
        if (type != 0) {
            Controller.getInstance().disableGUIHider();
            Controller.getInstance().turnOffGUIHiderFunct();
        } else {
            Controller.getInstance().turnOnGUIHiderFunct();
            Controller.getInstance().enableGUIHider();
        }
        this.interfaceType = type;
    }
    
    /**
     * Provides the general image's max width.
     * @return The image width.
     */
    public int getImageWidth() {
        return imageWidth;
    }
    
    /**
     * Pauses the recognition process.
     */
    public void pause() {
        this.isPaused = true;
    }
    
    /**
     * Unpauses the recognition process.
     */
    public void continueI() {
        this.isPaused = false;
    }
    
    public boolean allowInput() {
        return !this.relax;
    }
    
    public void relax(boolean b) {
        this.relax = b;
    }
    
    /**
     * Forcibilty orders the program to recognize a scene.
     */
    public void recognizeScene() {
        this.sceneIdentified = false;
    }
    
    /**
     * Adjusts the colors with the port view.
     */
    public static void adjustColors() {
        letterShownStartColor = new Color(150 - (10 * (MainGUI.currentViewPortNr - 1)),
                150 - (10 * (MainGUI.currentViewPortNr - 1)), 
                150 - (10 * (MainGUI.currentViewPortNr - 1)));
    }
    
}
