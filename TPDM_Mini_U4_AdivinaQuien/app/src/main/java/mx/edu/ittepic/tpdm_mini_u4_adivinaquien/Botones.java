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

public class Botones {
    private Bitmap btn;
    private Paint pincel;

    private int left, top;
    private String texto;
    private Bitmap temporal;
    private View puntero;
    private int imagen;
    private boolean parpadea;
    //Sonido
    private SoundPool soundPool;
    private int idPresionarBtn;

    public Botones(Bitmap boton, String texto, View view, int imagen){
        btn = boton;
        pincel = new Paint();
        this.texto = texto;
        puntero = view;
        this.imagen = imagen;
        soundPool = new SoundPool( 5, AudioManager.STREAM_MUSIC , 0);
        idPresionarBtn = soundPool.load(view.getContext(), R.raw.presionarboton, 0);
    }//Fin constructor

    public void dibujar(Canvas c) {
        pincel.setColor(Color.WHITE);
        pincel.setTextSize(40);
        c.drawBitmap(btn, left, top, pincel);
        c.drawText(texto, left+ btn.getWidth()/3+20, top+btn.getHeight()/2+15, pincel);
    }//Fin de dibujar


    public boolean estaEnArea(int xp, int yp){ //PUNTEROS xp y yp
        //Funcion de tipo comportamiento
        int x2 = left+btn.getWidth();
        int y2 = top+btn.getHeight();

        if(xp >= left && xp <= x2){
            if(yp >= top && yp <= y2){
                soundPool.play(idPresionarBtn, 1, 1, 1, 0, 1);
                return true;
            }
        }
        return false;
    }//Fin estaEnArea

    public void pasarCoordenadas(int x, int y){
        this.left = x;
        this.top = y;
    }

    public int getLargo(){
        return btn.getWidth();
    }

    public int getAncho(){
        return btn.getHeight();
    }

    public void cambiarColor(boolean cambio){
        if(cambio) {
            temporal = btn;
            btn = BitmapFactory.decodeResource(puntero.getResources(), imagen);
        }
        else
            btn = temporal;
    }

    public void parpadear(boolean parpadea){
        this.parpadea = parpadea;
    }
}//Fin clase
