package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.view.MotionEvent;
import android.view.View;

public class Lienzo extends View {

    private int maxX, maxY;
    //Imagenes
    private Bitmap interrogacion, datosJugador, datosOponente, vs;
    private Bitmap preguntar, resolver;

    private boolean direccion;
    private Paint pincelLienzo;

    //Instanciacion de clases
    private Fondo signos[];
    private ComponentesExtra []componentesExtra;
    private Botones btnPreguntar, btnResolver;

    public Lienzo(Context context){
        super(context);
        pincelLienzo = new Paint();

        //Vectores de clases
        componentesExtra = new ComponentesExtra[2];
        signos = new Fondo[15];

        //Imagenes
        interrogacion = BitmapFactory.decodeResource(getResources(), R.drawable.interrogacion);
        datosJugador = BitmapFactory.decodeResource(getResources(), R.drawable.jugador);
        datosOponente = BitmapFactory.decodeResource(getResources(), R.drawable.oponente);
        preguntar = BitmapFactory.decodeResource(getResources(), R.drawable.btnpreguntar);
        resolver = BitmapFactory.decodeResource(getResources(), R.drawable.btnresolver);
        vs = BitmapFactory.decodeResource(getResources(), R.drawable.vs);

        //Inicializacion de clases (no vectores)
        btnPreguntar = new Botones(preguntar, 2195,500, "Preguntar");
        btnResolver = new Botones(resolver, 2195, 700,"Resolver");

        direccion = true;
        for(int i = 0; i < signos.length; i++){
            signos[i] = new Fondo(interrogacion, direccion);
        }
    }//Fin constructor

    @Override
    protected void onSizeChanged(int w, int h, int viejoW, int viejoH){
        super.onSizeChanged(w,h,viejoW, viejoH);
        maxX = w;
        maxY = h;
        for(int i=0; i < signos.length; i++)
            signos[i].setMax(maxX, maxY);
    }//Fin onSizeChanged

    protected void onDraw(Canvas c){
        componentesExtra[0] = new ComponentesExtra(datosJugador, c.getWidth() - datosJugador.getWidth() - 10,50);
        componentesExtra[1] = new ComponentesExtra(datosOponente, c.getWidth() - datosJugador.getWidth() - 10,datosJugador.getHeight()+70);

        pincelLienzo.setShader(//Todo: Para el gradiente
                new RadialGradient(c.getWidth()/2, c.getHeight()/2,
                        c.getHeight()/4, Color.rgb(247, 220, 111),
                        Color.rgb(  227, 169, 59), Shader.TileMode.MIRROR));
        c.drawRect(0,0, c.getWidth(), c.getHeight(), pincelLienzo);
        pincelLienzo.reset();

        //todo: Dibujar los circulos con el signo de interrogacion
        for(int i = 0; i < signos.length; i++){
            signos[i].dibujar(c);
        }

        pincelLienzo.setAlpha(50);//TODO Rectangulo con degradado
        c.drawRect(c.getWidth()/4+c.getWidth()/4+c.getWidth()/4+c.getWidth()/12, 0, c.getWidth(), c.getHeight(), pincelLienzo);
        pincelLienzo.reset();

        componentesExtra[0].dibujar(c);//TODO Rectangulo rojo
        componentesExtra[1].dibujar(c);
        c.drawBitmap(vs, c.getWidth()- c.getWidth()/8+20, datosJugador.getHeight(), pincelLienzo);
        btnPreguntar.dibujar(c);
        btnResolver.dibujar(c);
        invalidate();
    }//Fin onDraw


    public boolean onTouchEvent(MotionEvent e){
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                //programas el estado "PRESIONADO"
                    if (btnPreguntar.estaEnArea((int) e.getX(), (int) e.getY())) {
                        //break; cualquiera de las dos opciones está bien
                    }
                    if (btnResolver.estaEnArea((int) e.getX(), (int) e.getY())) {
                    //break; cualquiera de las dos opciones está bien
                    }
                break;
            case MotionEvent.ACTION_MOVE:
                //Programa el estado "ARRASTRE"
                break;
            case MotionEvent.ACTION_UP:
                //Programa el estado "SOLTAR"
                break;
        }//fin switch
        return true;
    }
}//Fin clase
