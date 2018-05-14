package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;

public class Fondo {

    private int izquierdoTopX;
    private int izquierdoTopY;
    private Paint pincelFondo;
    private int radio;
    private Bitmap interrogacion;
    private RectF cuadrado;

    private int velocidadX;
    private int velocidadY;

    private int maxX;
    private int maxY;

    private boolean direccion;

    public Fondo(Bitmap signoInterrogacion, boolean direccion){
        pincelFondo = new Paint();
        cuadrado = new RectF();
        interrogacion = signoInterrogacion;
        radio = 100;
        this.direccion = direccion;
        setSpeed(2, 2);

        ponerCoordenadas((int) (Math.random()* 2450)+ 10,(int) (Math.random()* 1550)+ 10);
    }//Fin constructor

    public void ponerCoordenadas(int x, int y){
        izquierdoTopX = x;
        izquierdoTopY = y;
        cuadrado.set(izquierdoTopX, izquierdoTopY,
                izquierdoTopX+this.radio*2, izquierdoTopY+this.radio*2);
    }

    public void setMax(int maxX, int maxY){
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void setSpeed(int velocidadX, int velocidadY){
        this.velocidadX = velocidadX;
        this.velocidadY = velocidadY;
    }//

    public void dibujar(Canvas c) {
        pincelFondo.setColor(Color.rgb(93, 109, 126));//Circulo más grande
        c.drawOval(cuadrado, pincelFondo);
        pincelFondo.setColor(Color.rgb(   127, 179, 213));//Circulo más pequeño
        c.drawOval(cuadrado.left+20, cuadrado.top+20, cuadrado.right-20, cuadrado.bottom-20, pincelFondo);
        pincelFondo.setColor(Color.RED);//Signo de interrogación
        c.drawBitmap(interrogacion, cuadrado.left+50, cuadrado.top+50, pincelFondo);
        pincelFondo.reset();
        redibujar();
    }

    public void redibujar(){
            if (izquierdoTopX + 2 * radio > maxX) {
                velocidadX = velocidadX * -1;
            } else if (izquierdoTopX < 0) {
                velocidadX = velocidadX * -1;
            }

            if (izquierdoTopY + 2 * radio > maxY) {
                velocidadY = velocidadY * -1;
            } else if (izquierdoTopY < 0) {
                velocidadY = velocidadY * -1;
            }

        izquierdoTopX = izquierdoTopX + velocidadX;
        izquierdoTopY = izquierdoTopY + velocidadY;

        cuadrado.set(izquierdoTopX, izquierdoTopY,
                izquierdoTopX+2 * radio, izquierdoTopY+2*radio);
    }//Fin redibujar

}//Fin clase
