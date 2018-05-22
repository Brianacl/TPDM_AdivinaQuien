package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.View;

public class Personaje {
    private Bitmap btn, volteado;
    private Paint pincel;

    private String texto;
    private int x, y;
    private int identificador;
    //Sonido
    private SoundPool soundPool;
    private int idVoltearPersonaje;

    private boolean voltear;

    public Personaje(Bitmap boton, int x, int y, String texto, View view, int identificador){
        voltear = false;
        btn = boton;
        pincel = new Paint();
        this.x = x;
        this.y = y;
        this.texto = texto;
        this.identificador = identificador;
        volteado = BitmapFactory.decodeResource(view.getResources(), R.drawable.volteado);
        soundPool = new SoundPool( 5, AudioManager.STREAM_MUSIC , 0);
        idVoltearPersonaje = soundPool.load(view.getContext(), R.raw.voltearcarta, 0);
    }//Fin constructor

    public boolean getVoltear(){
        return voltear;
    }

    public void dibujar(Canvas c) {
        pincel.setColor(Color.WHITE);
        pincel.setTextSize(40);
        c.drawBitmap(btn, x, y, pincel);
        if(voltear){
            c.drawBitmap(volteado, x, y, pincel);
        }
        //c.drawText(texto, left+ btn.getWidth()/3+20, top+btn.getHeight()/2+15, pincel);
    }//Fin de dibujar

    public int getX(){
        return btn.getWidth();
    }

    public int getY(){
        return btn.getHeight();
    }

    public boolean estaEnArea(int xp, int yp){ //PUNTEROS xp y yp
        //Funcion de tipo comportamiento
        int x2 = x+btn.getWidth();
        int y2 = y+btn.getHeight();

        if(xp >= x && xp <= x2){
            if(yp >= y && yp <= y2){
                soundPool.play(idVoltearPersonaje, 1, 1, 1, 0, 1);
                return true;
            }
        }
        return false;
    }//Fin estaEnArea

    public void voltearPersonaje(boolean voltear){
        this.voltear = voltear;
    }//

    public int getIdentificador(){
        return identificador;
    }
}//Fin clase
