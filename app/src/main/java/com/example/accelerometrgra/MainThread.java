package com.example.accelerometrgra;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
/**  Klasa będąca głównym wątkiem i pętlą gry.
 *
 */
public class MainThread extends Thread{
    public static final int MAX_FPS = 30;/**< Maksymalna ilosc FPS w grze*/
    private double averageFPS;/**< Zmienna do wyliczania FPS generowanego przez urzadzenie*/
    private SurfaceHolder surfaceHolder; /**< Interfejs powierzchni ekranu umozliwiajacy edycje*/
    private GamePanel gamePanel; /**< Obiekt klasy GamePanel*/
    private boolean running; /**< Zmienna przechowuje informacje czy program jest uruchomiony*/
    private static Canvas canvas; /**< Plotno do rysowania elementow na ekranie*/

    /** \brief Funkcja ustawiajaca zmienna running na true przy uruchomieniu
     */
    public void setRunning(boolean running){
        this.running = running;
    }
    /** \brief Funkcja uruchamiajaca MainThread
     */
    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel){
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }
    /** \brief Funkcja pętli gry, do rozpoczecia rysowania.
     */
    @Override
    public void run(){
        long startTime;
        long timeMillis = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while(running){
            startTime = System.nanoTime();
            canvas = null;

            try{
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    this.gamePanel.update();
                    this.gamePanel.draw(canvas);
                }
            }catch (Exception e){
                e.printStackTrace();
            } finally {
                if(canvas != null){
                    try{
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    }catch (Exception e){e.printStackTrace();}
                }
            }
            timeMillis = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMillis;
            try {
                if(waitTime > 0)
                    this.sleep(waitTime);
            }catch (Exception e){e.printStackTrace();}

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if(frameCount == MAX_FPS){
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println(averageFPS);
            }
        }
    }
}
