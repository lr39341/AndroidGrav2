package com.example.accelerometrgra;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
/**  Klasa reprezentująca pojedynczą przeszkodę do ominięcia
 *
 */
public class Obstacle implements GameObject{

    private Rect rectangle;/**< Lewa czesc przeszkody*/
    private Rect rectangle2;/**< Prawa czesc przeszkody*/
    private int color;/**< Kolor przeszkody*/
    //private int startX;
    //private int playerGap;

    /** \brief Funkcja zwracajaca prostokat
     *
     * @return rectangle
     */
    public Rect getRectangle(){
        return rectangle;
    }

    /** \brief Funkcja przesuwajaca przeszkody w dol
     */
    public void incrementY(float y){
        rectangle.top += y;
        rectangle.bottom += y;
        rectangle2.top += y;
        rectangle2.bottom += y;
    }
    /** \brief Funkcja generujaca 2 prostokaty tworzace przeszkode .
     *
     * @param rectHeight Wysokosc prostokatu w pixelach
     * @param color Kolor prostokatu
     * @param startX Wspołrzedna X
     * @param startY Współrzędna Y
     * @param playerGap Odstęp pomiędzy prostokątami
     */
    public Obstacle(int rectHeight,int color, int startX, int startY, int playerGap){
        this.color = color;
        rectangle = new Rect(0,startY, startX, startY + rectHeight);
        rectangle2 = new Rect(startX + playerGap, startY, Constants.SCREEN_WIDTH, startY + rectHeight);


    }

    /** \brief Funkcja sprawdzająca kolizję gracza z przeszkoda
     *
     * @param player obiekt gracza
     */
    public boolean playerCollide(RectPlayer player){

        return Rect.intersects(rectangle, player.getRectangle()) || Rect.intersects(rectangle2, player.getRectangle());
       /*
        if(rectangle.contains(player.getRectangle().left, player.getRectangle().top)
        || rectangle.contains(player.getRectangle().right, player.getRectangle().top)
        || rectangle.contains(player.getRectangle().left, player.getRectangle().bottom)
        || rectangle.contains(player.getRectangle().right, player.getRectangle().bottom))
            return true;
        return false;
        */
    }

    @Override
    public void draw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawRect(rectangle, paint);
        canvas.drawRect(rectangle2, paint);
    }

    @Override
    public void update() {

    }
}
