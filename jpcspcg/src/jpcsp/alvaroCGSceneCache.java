/*
This file is part of jpcspCG.

JPCSPCG is an addition to the jpcsp software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published 
by the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

You should have received a copy of the GNU General Public License
along with Jpcsp.  If not, see <http://www.gnu.org/licenses/>.
 */
package jpcsp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import cgscenesl.*;
import customscriptdev.CGSMScript;
import java.io.*;

/**
 * Test Commit
 * @author Alvaro
 */
public class alvaroCGSceneCache {
    private Stack<LinkedList<CGSMScene>> cache;
    private int numberOfScenes;
    private LinkedList<CGSMScene> matchingLists;
    private LinkedList<CGSMScript> scripts;
    
    public alvaroCGSceneCache() {
        cache = new Stack<LinkedList<CGSMScene>>();
        this.numberOfScenes = 0;
        this.matchingLists = null;
        this.scripts = new LinkedList<CGSMScript>();
    }
    
    /**
     * Adds a scene to the cache including a script.
     * @param scene The scene to be added
     * @param script The script to be attached.
     * @throws NullPointerException  If the scene is a null pointer.
     */
    public void addScene(CGSMScene scene, String scriptPath) throws NullPointerException {
        if (scene == null) {
            throw new NullPointerException();
        }
        if (this.numberOfScenes % 100 == 0) {
            this.cache.push(new LinkedList<CGSMScene>());
        }
        this.cache.peek().add(scene);
        if (scriptPath != null) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(new File(scriptPath)));
                CGSMScript script = CGSMScript.class.cast(ois.readObject());
                if (script != null) {
                    script.includeObjectInScript(scene, "scene");
                    script.includeObjectInScript(MainGUI.a_debug, "debug");
                    this.scripts.add(script);
                }
            } catch (IOException e) {
                MainGUI.a_debug.escreveNaConsola("Error while reading script.\n" + e.fillInStackTrace());
            } catch (ClassNotFoundException e) {
                MainGUI.a_debug.escreveNaConsola("Script Class corrupted.\n" + e.fillInStackTrace());
            }
        }
        this.numberOfScenes++;
    }
    
    public int getTotalScenes() {
        return this.numberOfScenes;
    }
    
    /**
     * Retrieves a scene by giving its index.
     * @param k The index of the scene.
     * @return The scene. If scenes does not exist it is returned a null.
     */
    public CGSMScene getSceneByIndex(int k) {
        Iterator<LinkedList<CGSMScene>> i = this.cache.iterator();
        LinkedList<CGSMScene> scenes = i.next();
        while (i.hasNext() && k > 100) {
            k = k - 100;
            scenes = i.next();
        }
        try {
            return scenes.get(k);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }
    
    /**
     * Attempts to retrieve a script giving its attached scene.
     * @param scene The Scene
     * @return The Script attached or null if script does not exist.
     */
    public CGSMScript retrieveScript(CGSMScene scene) {
        Iterator<CGSMScript> i = this.scripts.iterator();
        CGSMScript script;
        while(i.hasNext()) {
            script = i.next();
            if (script.getObjects().next().equals(scene)) {
                return script;
            }
        }
        return null;
    }
    
    /**
     * Attempts to extract scenes matching a image file. <br>
     * Some times it will return more than one which means another image will <br>
     * have to be compared to properly extract the correct scene.
     * @param imageFile The image file to be compared
     * @param indexImage The index image of the scenes (0, 1)
     * @return A list of scenes or null if the extracting process failed.
     */
    public LinkedList<CGSMScene> getScenes(File imageFile, int indexImage) {
        this.matchingLists = new LinkedList<CGSMScene>();
        int numberOfThreads = this.cache.size();
        Iterator<LinkedList<CGSMScene>> i = this.cache.iterator();
        Thread[] searchingThread = new Thread[numberOfThreads];
        for (int k = 0; k < searchingThread.length; k++) {
            searchingThread[k] = new Thread(this.initiateSearchThread(i.next(), imageFile, indexImage));
            searchingThread[k].start();
        }
        try {
            for (int k1 = 0; k1 < searchingThread.length; k1++) {
                searchingThread[k1].join();
            }
            return this.matchingLists;
        } catch (InterruptedException e) {
            return null;
        }
        
        
    }
    
    /**
     * Attempts to extract scenes matching a image Buffer. <br>
     * Some times it will return more than one which means another image will <br>
     * have to be compared to properly extract the correct scene.
     * @param imageFile The image file to be compared
     * @param indexImage The index image of the scenes (0, 1)
     * @return A list of scenes or null if the extracting process failed.
     */
    public LinkedList<CGSMScene> getScenes(BufferedImage imageFile, int indexImage) {
        
        this.matchingLists = new LinkedList<CGSMScene>();
        int numberOfThreads = this.cache.size();
        Iterator<LinkedList<CGSMScene>> i = this.cache.iterator();
        Thread[] searchingThread = new Thread[numberOfThreads];
        for (int k = 0; k < searchingThread.length; k++) {
            searchingThread[k] = new Thread(this.initiateSearchThread(i.next(), imageFile, indexImage));
            searchingThread[k].start();
        }
        try {
            for (int k1 = 0; k1 < searchingThread.length; k1++) {
                searchingThread[k1].join();
            }
            //MainGUI.a_debug.escreveNaConsola("CACHE: Matching list with: " + this.matchingLists.size() + " scenes.");
            return this.matchingLists;
        } catch (InterruptedException e) {
            return null;
        }
        
        
    }
    /**
     * Attempts to provide the scene index in the cache.
     * @param scene The scene to look for
     * @return The index number
     */
    public int getSceneIndex(CGSMScene scene) {
        Iterator<LinkedList<CGSMScene>> lists = this.cache.iterator();
        Iterator<CGSMScene> scenes;
        int r = -1;
        while(lists.hasNext()) {
            scenes = lists.next().iterator();
            while(scenes.hasNext()) {
                r++;
                if (scenes.next().equals(scene)) {
                    return r;
                }
            }
        }
        return -1;
    }
    
    
    private Runnable initiateSearchThread(final LinkedList<CGSMScene> list, final File compareImage, final int indexImage) {
        return new Runnable() {
            @Override
            public void run() {
                Iterator<CGSMScene> ownList = list.iterator();
                CGSMScene current;
                while(ownList.hasNext()) {
                    current = ownList.next();
                    try {
                        if (current.isEqual(compareImage, indexImage) || 
                                current.isEqual(compareImage, indexImage + 2) || 
                                current.isEqual(compareImage, indexImage + 4)) {
                            matchingLists.add(current);
                        }
                        Thread.sleep(3);
                    } catch (IOException e) {
                        break;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
    }
    
    private Runnable initiateSearchThread(final LinkedList<CGSMScene> list, final BufferedImage compareImage, final int indexImage) {
        return new Runnable() {
            @Override
            public void run() {
                Iterator<CGSMScene> ownList = list.iterator();
                CGSMScene current;
                while(ownList.hasNext()) {
                    current = ownList.next();
                    try {
                        if (current.isEqual(compareImage, indexImage) || 
                                current.isEqual(compareImage, indexImage + 2) || 
                                current.isEqual(compareImage, indexImage + 4)) {
                            matchingLists.add(current);
                        }
                        Thread.sleep(3);
                    } catch (IOException e) {
                        break;
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        };
    }
    
    /**
     * Attempts to extract from the list argument a matching comparing image file.<p>
     * Note that this function is purely used to just compare the second scene 
     * text image in case the full-search first image failed.
     * @param list The list of scenes to compare
     * @param compareImage The compare image file.
     * @return The matching scene or null if no such scene matches.
     * @throws IOException If an error ocours while reading the compareImage file.
     */
    public static CGSMScene searchInScenesList(final LinkedList<CGSMScene> list,
            final File compareImage) throws IOException {
        Iterator<CGSMScene> i = list.iterator();
        CGSMScene scene;
        while(i.hasNext()) {
            scene = i.next();
            if (scene.isEqual(compareImage, 1) || 
                    scene.isEqual(compareImage, 3) ||
                    scene.isEqual(compareImage, 5)) {
                return scene;
            }
        }
        return null;
    }
    /**
     * Attempts to extract from the list argument a matching comparing a Buffered Image.<p>
     * Note that this function is purely used to just compare the second scene 
     * text image in case the full-search first image failed.
     * @param list The list of scenes to compare
     * @param compareImage The compare buffered image.
     * @return The matching scene or null if no such scene matches.
     * @throws IOException If an error ocours while reading the compareImage file.
     */
    public static CGSMScene searchInScenesList(final LinkedList<CGSMScene> list,
            final BufferedImage compareImage) throws IOException {
        Iterator<CGSMScene> i = list.iterator();
        CGSMScene scene;
        while(i.hasNext()) {
            scene = i.next();
            if (scene.isEqual(compareImage, 1) || 
                    scene.isEqual(compareImage, 3) ||
                    scene.isEqual(compareImage, 5)) {
                return scene;
            }
        }
        return null;
    }
}
