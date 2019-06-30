package com.example.accelerometrgra;

import android.graphics.Canvas;
/**  Klasa Bazowa każdego obiektu w grze
 *
 */
public interface GameObject {
    public void draw(Canvas canvas);
    public void update();
}
