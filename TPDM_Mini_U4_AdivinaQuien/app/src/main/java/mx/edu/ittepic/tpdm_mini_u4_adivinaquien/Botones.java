package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Botones {
    private Bitmap btn;
    private Paint pincel;

    private int left, top;
    private String texto;

    public Botones(Bitmap boton, int x, int y, String texto){
        btn = boton;
        pincel = new Paint();
        this.left = x;
        this.top = y;
        this.texto = texto;
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
                Log.v("-->","Está en área!");
                return true;
            }
        }
        return false;
    }//Fin estaEnArea
}//Fin clase
