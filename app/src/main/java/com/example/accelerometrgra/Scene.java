package com.example.accelerometrgra;

import android.graphics.Canvas;
import android.view.MotionEvent;
/**  Klasa bazowa, reprezentująca pojedynczą scenę
 *
 */
public interface Scene {
    public void update();
    public void draw(Canvas canvas);
    public void terminate();
    public void receiveTouch(MotionEvent event);
}
