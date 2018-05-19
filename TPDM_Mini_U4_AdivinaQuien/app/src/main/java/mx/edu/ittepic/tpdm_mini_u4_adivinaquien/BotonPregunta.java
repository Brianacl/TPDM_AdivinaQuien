package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class BotonPregunta {

    private RectF rectangulo;
    Paint paint;
    Context context;
    int identificador;

    public BotonPregunta(int color, Context context, int identificador){
        paint = new Paint();
        paint.setColor(color);
        rectangulo = new RectF();
        this.identificador = identificador;
        this.context = context;
    }

    public void ponerCoordenadas(int izquierda, int arriba, int derecha, int abajo){
        rectangulo.set(izquierda, arriba, derecha, abajo);
    }//Fin poner coordenadas

    public void dibujar(Canvas canvas){
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(3);
        canvas.drawRect(rectangulo, paint);
    }//Fin dibujar

    public boolean estaEnArea(int xp, int yp){ //PUNTEROS xp y yp
        //Funcion de tipo comportamiento
        int x2 = (int) (rectangulo.left + rectangulo.right);
        int y2 = (int) (rectangulo.top+rectangulo.bottom);

        if(xp >= (int) rectangulo.left && xp <= x2){
            if(yp >= (int) rectangulo.top && yp <= y2){
                return true;
            }
        }
        return false;
    }//Fin estaEnArea


    public int getIdentificador(){
        return identificador;
    }
}//Fin clase
