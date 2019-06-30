package com.example.accelerometrgra;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
/**  Klasa zarządzająca logiką odczytów akcelerometru i magnetometru
 *
 */

public class OrientationData implements SensorEventListener {
    private SensorManager manager; /**< Manager sensorow*/
    private Sensor accelerometer; /**< Obiekt akcelerometru*/
    private Sensor magnometer; /**< Obiekt magnetometru*/

    private float[] accelOutput; /**< Tablica wynikow odczytu akcelerometru*/
    private float[] magOutput; /**< Tablica wynikow odczytu magnetometru*/

    private float[] orientation = new float[3]; /**< Tablica okreslajaca orientacje telefonu*/

    public float[] getOrientation(){
        return orientation;
    }

    private float[] startOrientation = null;
    public float[] getStartOrientation(){
        return startOrientation;
    }

    /** \brief Funkcja przywracajaca poczatkowa orientacje podczas wlaczenia nowej gry
     */
    public void newGame(){
        startOrientation = null;
    }

    /** \brief Funkcja przypisujaca zmienne sensorow do ich odpowiednikow w menagerze sensorow
     */
    public OrientationData(){
        manager = (SensorManager)Constants.CURRENT_CONTEXT.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnometer = manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }
    /** \brief Funkcja pobierajaca dane z sensorow
     */
    public void register(){
        manager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
        manager.registerListener(this,magnometer,SensorManager.SENSOR_DELAY_GAME);

    }
    /** \brief Funkcja pauzujaca odczyt z sensorow
     */
    public void pause(){
        manager.unregisterListener(this);
    }

    /** \brief Funkcja sprawdzajaca orientacje telefonu
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            accelOutput = event.values;
        else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                magOutput = event.values;
        if(accelOutput != null && magOutput != null){
            float[] R = new float[9];
            float[] I = new float[9];

            boolean success = SensorManager.getRotationMatrix(R, I, accelOutput, magOutput);
            if(success){
                SensorManager.getOrientation(R, orientation);
                if(startOrientation == null){
                    startOrientation = new float[orientation.length];
                    System.arraycopy(orientation, 0 , startOrientation, 0, orientation.length);
                }
            }
        }


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
