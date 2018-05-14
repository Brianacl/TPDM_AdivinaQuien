package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

public class ComponentesExtra {

    private Bitmap img;
    private Paint pincel;

    private int left, top;

    public ComponentesExtra(Bitmap imagen, int left, int top){
        pincel = new Paint();
        img = imagen;
        this.left = left;
        this.top = top;
    }//Fin constructor

    public void dibujar(Canvas c) {
        pincel.setColor(Color.RED);
        c.drawBitmap(img, left, top, pincel);
    }//Fin de dibujar
}//Fin clase
