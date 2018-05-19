package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class Lienzo extends View {

    private int maxX, maxY, ximg, yimg;
    boolean voltear, miTurno;
    private int miPersonaje;
    //Imagenes
    private Bitmap interrogacion, datosJugador, datosOponente, vs;
    private Bitmap preguntar, resolver, imgMiPersonaje, contestar, verRespuesta;

    private boolean direccion;
    private boolean preguntara = false, resolvera = true;
    private Paint pincelLienzo;

    //Instanciacion de clases
    private Fondo signos[];
    private ComponentesExtra []componentesExtra;
    private Botones btnPreguntar, btnResolver, btnContestar, btnVerRespuesta;
    private Personaje personajes[], apuntaMiPersonaje;
    private MainActivity punteroMain;
    private String indicacion;
    private boolean ganador = false, error = false;
    private BotonPregunta btnPreguntas[];
    private Personaje puntero;

    public Lienzo(Context context, String[] datos, MainActivity punteroMain,
                  int []personajesSeleccionados, int miPersonaje){
        super(context);
        this.punteroMain = punteroMain;
        punteroMain.insertarPersonaje(miPersonaje);
        this.miPersonaje = miPersonaje;
        voltear = false;
        indicacion="";

        pincelLienzo = new Paint();

        //personajesSeleccionados = cargarPersonajes();
        ximg = 50;
        yimg = 300;

        //Vectores de clases
        componentesExtra = new ComponentesExtra[2];
        signos = new Fondo[15];
        this.personajes = new Personaje[24];

        //Imagenes
        interrogacion = BitmapFactory.decodeResource(getResources(), R.drawable.interrogacion);
        datosJugador = BitmapFactory.decodeResource(getResources(), R.drawable.jugador);
        datosOponente = BitmapFactory.decodeResource(getResources(), R.drawable.oponente);
        preguntar = BitmapFactory.decodeResource(getResources(), R.drawable.btnpreguntar);
        resolver = BitmapFactory.decodeResource(getResources(), R.drawable.btnresolver);
        vs = BitmapFactory.decodeResource(getResources(), R.drawable.vs);
        contestar = BitmapFactory.decodeResource(getResources(), R.drawable.btncontestar);
        verRespuesta = BitmapFactory.decodeResource(getResources(), R.drawable.btnverrespuesta);

        //Inicializacion de clases (no vectores)
        btnPreguntar = new Botones(preguntar, "Preguntar", this, R.drawable.btnpreguntar1);
        btnResolver = new Botones(resolver,"Resolver", this, R.drawable.btnresolver1);
        btnContestar = new Botones(contestar, "Contestar", this, R.drawable.btnresolver1);
        btnVerRespuesta = new Botones(verRespuesta, "Ver resp.", this, R.drawable.btnresolver1);


        direccion = true;
        for(int i = 0; i < signos.length; i++){
            signos[i] = new Fondo(interrogacion, direccion);
        }

        int []imgvolteado = new int[24];
        for(int i = 0; i < 24; i++){
            imgvolteado[i] = R.drawable.volteado;
        }

        imgMiPersonaje = BitmapFactory.decodeResource(getResources(), this.miPersonaje);
        //apuntaMiPersonaje = new Personaje(imgMiPersonaje, 2195,300,"", this);

        //Llenar arreglo de personajes
        int puntero = 0;
        for(int i=0; i < personajes.length; i++){

            personajes[i] = new Personaje(
                    BitmapFactory.decodeResource(getResources(), personajesSeleccionados[i]),
                    ximg, yimg, "Texto", this, personajesSeleccionados[i]);

            ximg += personajes[puntero].getX() + 50;

            if(i == 5 || i == 11 || i == 17) {
                puntero = 0;
                ximg = 50;
                yimg += personajes[puntero].getY() + 50;
            }
            puntero++;
        }//Fin del for para llenar arreglo con personajes
    }//Fin constructor

    @Override
    protected void onSizeChanged(int w, int h, int viejoW, int viejoH){
        super.onSizeChanged(w,h,viejoW, viejoH);
            maxX = w;
            maxY = h;
            for (int i = 0; i < signos.length; i++)
                signos[i].setMax(maxX, maxY);
    }//Fin onSizeChanged

    int x=50, y=200;

    protected void onDraw(Canvas c){
        //Esto es para el fondo
        Paint paint = new Paint();
        paint.setShader(new RadialGradient(c.getWidth()/2, c.getHeight()/2,
                c.getHeight()/4, Color.rgb(247, 220, 111),
                Color.rgb(  227, 169, 59), Shader.TileMode.MIRROR));
        c.drawRect(0, 0, c.getWidth(), c.getHeight(), paint);
        //Fin de para el fondo

            componentesExtra[0] = new ComponentesExtra(datosJugador, c.getWidth() - datosJugador.getWidth() - 10, 50);
            componentesExtra[1] = new ComponentesExtra(datosOponente, c.getWidth() - datosJugador.getWidth() - 10, datosJugador.getHeight() + 70);

            pincelLienzo.setShader(//Todo: Para el gradiente
                    new RadialGradient(c.getWidth() / 2, c.getHeight() / 2,
                            c.getHeight() / 4, Color.rgb(247, 220, 111),
                            Color.rgb(227, 169, 59), Shader.TileMode.MIRROR));
            c.drawRect(0, 0, c.getWidth(), c.getHeight(), pincelLienzo);
            pincelLienzo.reset();

            //todo: Dibujar los circulos con el signo de interrogacion
            for (int i = 0; i < signos.length; i++) {
                signos[i].dibujar(c);
            }

            pincelLienzo.setAlpha(50);//TODO Rectangulo con degradado
            c.drawRect(c.getWidth() / 4 + c.getWidth() / 4 + c.getWidth() / 4 + c.getWidth() / 12, 0, c.getWidth(), c.getHeight(), pincelLienzo);
            pincelLienzo.reset();

            componentesExtra[0].dibujar(c);//TODO Rectangulo rojo
            componentesExtra[1].dibujar(c);

            c.drawBitmap(vs, c.getWidth() - c.getWidth() / 8 + 20, datosJugador.getHeight(), pincelLienzo);

            btnPreguntar.pasarCoordenadas(2195, 500);
            btnResolver.pasarCoordenadas(2195, 700);
            btnVerRespuesta.pasarCoordenadas(2195, 1100);
            btnContestar.pasarCoordenadas(2195, 1300);

            btnPreguntar.dibujar(c);
            btnResolver.dibujar(c);
            btnVerRespuesta.dibujar(c);
            btnContestar.dibujar(c);

            c.drawBitmap(imgMiPersonaje, 2200,100, paint);

            for (int i = 0; i < personajes.length; i++) {
                personajes[i].dibujar(c);
            }

            Paint paintTexto = new Paint();
            paintTexto.setTextSize(100);
            if(miTurno){
                c.drawText("Es tu turno!", 100, 100, paintTexto);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   }
            else {
                c.drawText("Espera, por favor", 100, 100, paintTexto);
                c.drawRect(c.getWidth() / 4 + c.getWidth() / 4 + c.getWidth() / 4 + c.getWidth() / 12, 300, c.getWidth(), c.getHeight()/2                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           , pincelLienzo);
            }

            c.drawText(indicacion, c.getWidth()/2 - c.getWidth()/10, 100, paintTexto);
            if(ganador){
                indicacion = "HAS GANADO!";
            }

            if(error){
                indicacion = "RESPUESTA EQUIVOCADA";
                error = false;
            }
        invalidate();
    }//Fin onDraw


    public boolean onTouchEvent(MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                //programas el estado "PRESIONADO"
                voltear=!voltear;
                    if (btnResolver.estaEnArea((int) e.getX(), (int) e.getY())) {
                    //break; cualquiera de las dos opciones está bien
                        indicacion = "Seleccione su solución";
                        resolvera = true;
                    }
                if (btnContestar.estaEnArea((int) e.getX(), (int) e.getY())) {
                    //break; cualquiera de las dos opciones está bien
                    punteroMain.contestar();
                }
                if (btnVerRespuesta.estaEnArea((int) e.getX(), (int) e.getY())) {
                    //break; cualquiera de las dos opciones está bien
                    punteroMain.verRespuesta();

                }
                    for (int i = 0; i < personajes.length; i++) {
                        if (personajes[i].estaEnArea((int) e.getX(), (int) e.getY())) {
                            personajes[i].voltearPersonaje(voltear);
                            if(resolvera) {
                                puntero = personajes[i];
                                punteroMain.resolver(puntero.getIdentificador());
                                indicacion = "";
                                resolvera = false;
                            }
                            i = personajes.length;
                                //break; cualquiera de las dos opciones está bien
                        }
                    }//Fin for

                if (btnPreguntar.estaEnArea((int) e.getX(), (int) e.getY())) {
                        punteroMain.mostrarPreguntas();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //Programa el estado "ARRASTRE"
                break;
            case MotionEvent.ACTION_UP:
                //Programa el estado "SOLTAR"
                //btnResolver.cambiarColor(false);
                //btnPreguntar.cambiarColor(false);
                puntero = null;
                break;
        }//fin switch
        return true;
    }//Fin onTouch

    public void turno(boolean turno){
        miTurno = turno;
    }

    public void ganador(boolean ganador){
        this.ganador = ganador;
    }

    public void error(boolean error){
        this.error = error;
    }
}//Fin clase
