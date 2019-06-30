package com.example.accelerometrgra;

import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
/**  Klasa zarządzająca wszystkimi scenami w grze.
 *
 */
public class SceneManager {
    private ArrayList<Scene> scenes = new ArrayList<>();
    public static int ACTIVE_SCENE;

    public SceneManager(){
        ACTIVE_SCENE = 0;
        scenes.add(new GameplayScene());
    }
    /** \brief Funkcja przekazujaca dane od gracza i przekazuje do sceny
     */
    public void receiveTouch(MotionEvent event){
        scenes.get(ACTIVE_SCENE).receiveTouch(event);
    }
    public void update(){
        scenes.get(ACTIVE_SCENE).update();
    }

    public void draw (Canvas canvas){
        scenes.get(ACTIVE_SCENE).draw(canvas);
    }

}
