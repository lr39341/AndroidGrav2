package com.example.accelerometrgra;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
/**  Klasa reprezentująca scenę z grą. Zawiera logikę i rysowanie obiektów.
 *
 */
public class GameplayScene implements Scene{

    private Rect r = new Rect(); /**< Obiekt rectangle do wypisywania tekstu */

    private RectPlayer player; /**< Obiekt gracza*/
    private Point playerPoint; /**< Wspolrzedna gracza na ekranie*/
    private ObstacleManager obstacleManager; /**< Obiekt menadzera przeszkod*/

    private boolean movingPlayer = false; /**< Zmienna okreslajaca mozliwosc poruszania sie gracza*/
    private boolean gameOver = false; /**< Zmienna okreslajaca koniec gry*/
    private long gameOverTime; /**< Zmienna okreslajaca czas ile gra jest w stanie konca gry.*/

    private OrientationData orientationData; /**< Obiekt okreslajacy orientacje telefonu*/
    private long frameTime; /**< Zmienna do okreslenia czasu jednej klatki*/

    /** \brief Konstruktor budujacy scene.
     *
     */
    public GameplayScene(){
        player = new RectPlayer(new Rect(100,100,200,200), Color.rgb(255,128,0));
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(250, 500,75,Color.WHITE);

        orientationData = new OrientationData();
        orientationData.register();
        frameTime = System.currentTimeMillis();
    }
    /** \brief Funkcja resetujaca gre podczas rozpoczecia ponownej gry
     */
    public void reset(){
        playerPoint = new Point(Constants.SCREEN_WIDTH/2, 3*Constants.SCREEN_HEIGHT/4);
        player.update(playerPoint);

        obstacleManager = new ObstacleManager(250, 500,75,Color.WHITE);
        movingPlayer = false;
    }

    /** \brief Funkcja sprawdza kolizje gracza z przeszkodami, sprawdza czy jest koniec gry i aktualizuje pozycje gracza za pomoca odczytow z akcelerometru.
     */
    @Override
    public void update() {

        if(!gameOver) {
            if(frameTime < Constants.INIT_TIME)
                frameTime = Constants.INIT_TIME;
            int elapsedTime = (int)(System.currentTimeMillis() - frameTime);
            frameTime = System.currentTimeMillis();
            if(orientationData.getOrientation() != null && orientationData.getStartOrientation() != null){
                float pitch = orientationData.getOrientation()[1] - orientationData.getStartOrientation()[1];
                float roll = orientationData.getOrientation()[2] - orientationData.getStartOrientation()[2];

                float xSpeed = 2* roll * Constants.SCREEN_WIDTH/1000.0f;
                float ySpeed = pitch *Constants.SCREEN_HEIGHT/1000.0f;

                playerPoint.x += Math.abs(xSpeed*elapsedTime) > 5 ? xSpeed*elapsedTime : 0;
                playerPoint.y -= Math.abs(ySpeed*elapsedTime) > 5 ? ySpeed*elapsedTime : 0;

            }

            if(playerPoint.x < 0)
                playerPoint.x = 0;
            else if(playerPoint.x > Constants.SCREEN_WIDTH)
                playerPoint.x = Constants.SCREEN_WIDTH;

            if(playerPoint.y < 0)
                playerPoint.y = 0;
            else if(playerPoint.y > Constants.SCREEN_HEIGHT)
                playerPoint.y = Constants.SCREEN_HEIGHT;

            player.update(playerPoint);
            obstacleManager.update();
            if(obstacleManager.playerCollide(player)){
                gameOver = true;
                gameOverTime = System.currentTimeMillis();
            }
        }
    }
    /** \brief Funkcja rysuje tlo, gracza, przeszkody i napis konca gry.
     */
    @Override
    public void draw(Canvas canvas) {

        canvas.drawColor(Color.BLACK);



        player.draw(canvas);
        obstacleManager.draw(canvas);


        if(gameOver){
            Paint paint = new Paint();
            paint.setTextSize(48);
            paint.setColor(Color.rgb(255,128,0));
            drawCenterText(canvas, paint, "Kliknij by rozpocząć ponownie!");
        }
    }

    @Override
    public void terminate() {
        SceneManager.ACTIVE_SCENE = 0;

    }
    /** \brief Funkcja sczytuje dotkniecie gracza po koncu gry by rozpoczac ponownie
     */
    @Override
    public void receiveTouch(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(!gameOver && player.getRectangle().contains((int)event.getX(), (int)event.getY()))
                    movingPlayer = true;
                if(gameOver && System.currentTimeMillis() - gameOverTime >= 1500){
                    reset();
                    gameOver = false;
                    orientationData.newGame();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(!gameOver && movingPlayer)
                    playerPoint.set((int)event.getX(), (int)event.getY());
                break;
            case MotionEvent.ACTION_UP:
                movingPlayer = false;
                break;

        }
    }
    /** \brief Funkcja do wyznaczania srodka ekranu i wypisania tekstu
     */
    private void drawCenterText(Canvas canvas, Paint paint, String text) {
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.getClipBounds(r);
        int cHeight = r.height();
        int cWidth = r.width();
        paint.getTextBounds(text, 0, text.length(), r);
        float x = cWidth / 2f - r.width() / 2f - r.left;
        float y = cHeight / 2f + r.height() / 2f - r.bottom;
        canvas.drawText(text, x, y, paint);

    }
}
