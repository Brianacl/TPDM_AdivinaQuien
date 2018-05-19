package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;

public class Portada extends View{

    private Bitmap logoAdivinaQuien, comenzar, preguntar;
    private Botones btnComenzar;
    private Botones btnPreguntar;
    private Context context;
    private MainActivity punteroMain;
    private String []datosUsuario;
    private String avance;
    private CountDownTimer timerCambioColor;
    private Paint paintAviso;
    private boolean seEncontroPartida;
    private int miPersonaje;
    private Lienzo lienzo;
    private RectF rectanguloPreguntas;
    private int []personajesDisponibles  =
            {
                R.drawable.uno,
                R.drawable.dos,
                R.drawable.tres,
                R.drawable.cuatro,
                R.drawable.cinco,
                R.drawable.seis,
                R.drawable.siete,
                R.drawable.ocho,
                R.drawable.nueve,
                R.drawable.diez,
                R.drawable.once,
                R.drawable.doce,
                R.drawable.trece,
                R.drawable.catorce,
                R.drawable.quince,
                R.drawable.dieciseis,
                R.drawable.diecisiete,
                R.drawable.dieciocho,
                R.drawable.diecinueve,
                R.drawable.veinte,
                R.drawable.veintiuno,
                R.drawable.veintidos,
                R.drawable.veintitres,
                R.drawable.veinticuatro
            };

    public Portada(Context context, String datosUsuario[], MainActivity punteroMain){
        super(context);

        preguntar = BitmapFactory.decodeResource(getResources(), R.drawable.btnpreguntar);
        btnPreguntar = new Botones(preguntar, "Preguntar", this, R.drawable.btnpreguntar1);

        seEncontroPartida = false;
        avance="";
        this.context = context;
        paintAviso = new Paint();
        this.punteroMain = punteroMain;
        this.datosUsuario = datosUsuario;
        comenzar = BitmapFactory.decodeResource(getResources(), R.drawable.comenzar1);
        logoAdivinaQuien = BitmapFactory.decodeResource(getResources(), R.drawable.portada);
        btnComenzar = new Botones(comenzar,"", this, R.drawable.comenzar2);
    }//Fin constructor

    protected void onDraw(Canvas c){
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(c.getWidth()/2, c.getHeight()/2,
                c.getHeight()/4, Color.rgb(247, 220, 111),
                Color.rgb(  227, 169, 59), Shader.TileMode.MIRROR));
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), paint);

        btnComenzar.pasarCoordenadas(c.getWidth() / 2 - btnComenzar.getLargo() / 2, c.getHeight() - btnComenzar.getAncho() * 2);
        btnComenzar.dibujar(c);

        paintAviso.setTextSize(100);
        c.drawText(avance, c.getWidth() - c.getWidth()/2 - c.getWidth()/7, c.getHeight() - c.getHeight()/9, paintAviso);

        c.drawBitmap(logoAdivinaQuien, c.getWidth() / 4, c.getHeight() / 4, new Paint());

        if(seEncontroPartida){
            lienzo = new Lienzo(context, datosUsuario, punteroMain, cargarPersonajes(), miPersonaje());
            punteroMain.setContentView(lienzo);
        }
        invalidate();
    }//Fin onDraw

    public void seEncontroPartida(boolean encontro){
        seEncontroPartida = encontro;
    }

    private boolean preguntara = false;

    public boolean onTouchEvent(MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                //programas el estado "PRESIONADO"
                if (btnComenzar.estaEnArea((int) e.getX(), (int) e.getY())) {
                    btnComenzar.cambiarColor(true);
                    punteroMain.buscarPartida();
                    timer();
                    //punteroMain.setContentView(new Lienzo(punteroMain, datosUsuario, punteroMain));
                }
            case MotionEvent.ACTION_MOVE:
                //Programa el estado "ARRASTRE"
                break;
            case MotionEvent.ACTION_UP:
                //Programa el estado "SOLTAR"
                btnComenzar.cambiarColor(false);
                break;
        }//fin switch
        return true;
    }//Fin onTouchEvent

    public void mostrarAvance(String respuesta){
        avance = respuesta;
    }

    private void timer(){
        timerCambioColor = new CountDownTimer(60000, 1000) {
            boolean cambio = false;
            @Override
            public void onTick(long l) {
                cambio = !cambio;
                if(cambio)
                    paintAviso.setColor(Color.BLUE);
                else
                    paintAviso.setColor(Color.WHITE);
            }

            @Override
            public void onFinish() {
                paintAviso.setColor(Color.WHITE);
            }
        };
        timerCambioColor.start();
    }//Fin timer

    private int[] cargarPersonajes(){

        int cantidad=24, rango=24;
        int arreglo[] = new int[cantidad];

        for(int i = 0; i < cantidad; i++){
            arreglo[i]=(int)(Math.random()*rango);
            for(int j=0; j<i; j++){
                if(arreglo[i]==arreglo[j]){
                    i--;
                }
            }
        }

        int []personajesSeleccinados = new int[24];

        for(int i = 0; i < personajesSeleccinados.length; i++){
            personajesSeleccinados[i] = personajesDisponibles[arreglo[i]];
        }

        return personajesSeleccinados;
    }//Fin cargar personajes

    private int miPersonaje(){
        int cantidad=1, rango=24;
        int arreglo[] = new int[cantidad];

        for(int i = 0; i < cantidad; i++){
            arreglo[i]=(int)(Math.random()*rango);
            for(int j=0; j<i; j++){
                if(arreglo[i]==arreglo[j]){
                    i--;
                }
            }
        }
        miPersonaje = personajesDisponibles[arreglo[0]];

        return miPersonaje;
    }

    public void ganador(boolean ganador){
        lienzo.ganador(ganador);
    }

    public void error(boolean error){
        lienzo.error(error);
    }

    public void miTurno(boolean turno){
        lienzo.turno(turno);
    }

}//Fin clase
