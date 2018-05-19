package mx.edu.ittepic.tpdm_mini_u4_adivinaquien;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btnIngresar, btnRegistrar;
    private EditText txtUsuario, txtContrasena;
    private ConexionWeb conexionWeb;
    private AlertDialog.Builder alerta;
    private String datosUsuario[], respuesta;
    private CountDownTimer miTimer, timerVerificarTurno;
    private boolean miTurno;
    private String njuego;
    private Portada portada;
    private AlertDialog.Builder alertaPreguntas, alertaververRespuesta, alertaResponder;
    private Button btnPreguntas[];
    private String []preguntas;

    int i;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnRegistrar = findViewById(R.id.btnRegistrar);
        btnIngresar = findViewById(R.id.btnIngresar);

        alerta = new AlertDialog.Builder(this);
        alertaResponder = new AlertDialog.Builder(this);
        alertaververRespuesta = new AlertDialog.Builder(this);

        respuesta="";

        recuperarPreguntas();

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    conexionWeb = new ConexionWeb(MainActivity.this);
                    conexionWeb.agregarVariables("USUARIO", txtUsuario.getText().toString());
                    conexionWeb.agregarVariables("CONTRASENA", txtContrasena.getText().toString());

                    //Ejecución
                    URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/singup.php");
                    conexionWeb.execute(direccion);
                } catch (MalformedURLException malformed) {
                    Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
                }
            }
        });

        }//Fin onCreate

    private void login(){
        try {

            conexionWeb = new ConexionWeb(MainActivity.this);
            conexionWeb.agregarVariables("USUARIO", txtUsuario.getText().toString());
            conexionWeb.agregarVariables("CONTRASENA", txtContrasena.getText().toString());

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/singin.php");
            conexionWeb.execute(direccion);
        } catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }//Ffin login

    protected void enviarPregunta(String pregunta){
        try {

            conexionWeb = new ConexionWeb(MainActivity.this);
            conexionWeb.agregarVariables("IDJUGADOR", datosUsuario[1]);
            conexionWeb.agregarVariables("NJUEGO", njuego);
            conexionWeb.agregarVariables("PREGUNTA", pregunta);

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/enviarpregunta.php");
            conexionWeb.execute(direccion);
        } catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }

    public void mostrarPreguntas(){
        alertaPreguntas.setTitle("Preguntas disponibles: ").show();
    }

    public void procesarRespuesta(String respuesta){
        if(respuesta.equals("ERROR: 404_1")){
            respuesta = "Error: Flujo Entrada/Salida no funciona";
            Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
            return;
        }
        if(respuesta.equals("ERROR: 404_2")){
            respuesta = "Error: Servidor caido o dirección incorrecta";
            Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
            return;
        }

        if(respuesta.equals("NO_REGISTRADO")){
            alerta.setTitle("ERROR").setMessage("¿Ya está registrado?").show();
            return;
        }

        if(respuesta.equals("NO_ACCESO")){
            alerta.setTitle("ERROR").setMessage("Usuario o contraseña incorrecto.").show();
            return;
        }

        if(respuesta.equals("YA_REGISTRADO")){
            alerta.setTitle("AVISO!").setMessage("Nombre de usuario ya existente.\n Intente con otro nombre, por favor").show();
            return;
        }

        if(respuesta.equals(txtUsuario.getText())){
            login();
            return;
        }

        if(respuesta.equals("1")){
            this.respuesta = respuesta;
            portada.seEncontroPartida(true);
            return;
        }

        if(respuesta.equals("0")){
            portada.mostrarAvance("Buscando jugador");
            return;
        }

        if(respuesta.equals("SALIO_ROOM")) {
            portada.mostrarAvance("Intenta de nuevo");
            return;
        }

        if(respuesta.length()>2 && respuesta.substring(2).equals("TURNO")){
            portada.miTurno(true);
            njuego= respuesta.substring(0, 1);
            //Log.v("NJUEGO", njuego);
            return;
        }

        if(respuesta.length()>2 && respuesta.substring(2).equals("ESPERA")){
            portada.miTurno(false);
            njuego= respuesta.substring(0, 1);
            //Log.v("NJUEGO", njuego);
            return;
        }

        if(respuesta.equals("GANADOR")){
            portada.ganador(true);
            return;
        }

        if(respuesta.equals("EQUIVOCADO")){
            portada.error(true);
            return;
        }

        if(respuesta.length() > 1 && respuesta.substring(0,1).equals("¿")){
            preguntas = respuesta.split("-");
            pasarPreguntasBoton();
            return;
        }

        Log.v("RESPUESTA", respuesta);
        if(respuesta.length() > 6 && respuesta.substring(0, 6).equals("ACCESO")) {
            datosUsuario = respuesta.split("-");
            Toast.makeText(this, "LE DAMOS LA BIEVENIDA! " + datosUsuario[2], Toast.LENGTH_SHORT).show();
            portada = new Portada(this, datosUsuario, this);
            setContentView(portada);
            return;
        }
    }//Fin procesar respuesta


    public void pasarPreguntasBoton(){
        btnPreguntas = new Button[preguntas.length];

        alertaPreguntas = new AlertDialog.Builder(this);
        recuperarPreguntas();

        for(i=0 ; i < btnPreguntas.length; i++) {
            btnPreguntas[i] = new Button(this);
            btnPreguntas[i].setText(preguntas[i]);
            btnPreguntas[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Button b = (Button) view;
                    enviarPregunta(b.getText().toString());
                }
            });
        }

        ScrollView scrollView = new ScrollView(this);

        LinearLayout ll=new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setPadding(100,10,100,10);

        for(int i =0; i < btnPreguntas.length; i++)
            ll.addView(btnPreguntas[i]);

        scrollView.addView(ll);
        alertaPreguntas.setView(scrollView);
    }

    public void buscarPartida(){
        try {

            conexionWeb = new ConexionWeb(MainActivity.this);
            conexionWeb.agregarVariables("IDUSUARIO", datosUsuario[1]);

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/room.php");
            conexionWeb.execute(direccion);
            buscarOponente();

        } catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }//Fin buscarPartida


    protected void buscarOponente(){
        miTimer = new CountDownTimer(60000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(respuesta.equals("1")){
                    miTimer.cancel();
                    portada.mostrarAvance("Partida encontrada!");
                    checarTurno();
                    miTimer.onFinish();
                }
                try {
                    conexionWeb = new ConexionWeb(MainActivity.this);
                    conexionWeb.agregarVariables("IDUSUARIO", datosUsuario[1]);

                    //Ejecución
                    URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/buscandoOponente.php");
                    conexionWeb.execute(direccion);
                }catch (MalformedURLException mal){
                    mal.getMessage();
                }
            }//Fin de onTick

            @Override
            public void onFinish() {
                if(respuesta.equals("1")){
                    traerDatos();
                }
                else {
                    Toast.makeText(MainActivity.this, "No se encontró partida", Toast.LENGTH_SHORT).show();
                    noEncontroPartida();
                }
            }//Fin de onFinish
        };
        miTimer.start();
    }

    protected void traerDatos(){

        try {
            conexionWeb = new ConexionWeb(MainActivity.this);
            conexionWeb.agregarVariables("IDUSUARIO", datosUsuario[1]);

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/traerDatos.php");
            conexionWeb.execute(direccion);
            }catch (MalformedURLException mal){
            mal.getMessage();
            }
    }//Fin traer datos


    protected void checarTurno(){
                    timerVerificarTurno = new CountDownTimer(30000, 3000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            traerDatos();
                        }

                        @Override
                        public void onFinish() {
                            checarTurno();
                        }
                    };
                    timerVerificarTurno.start();

    }

    private void noEncontroPartida(){
        try {

            conexionWeb = new ConexionWeb(MainActivity.this);
            conexionWeb.agregarVariables("IDUSUARIO", datosUsuario[1]);

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/noEncontroPartida.php");
            conexionWeb.execute(direccion);

        } catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }//Fin noEncontroPartida

    private void recuperarPreguntas(){
        try {

            conexionWeb = new ConexionWeb(MainActivity.this);

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/recuperarpreguntas.php");
            conexionWeb.execute(direccion);

        } catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }//Fin noEncontroPartida


    public void insertarPersonaje(int personaje){
        try {
            conexionWeb = new ConexionWeb(MainActivity.this);
            //conexionWeb.agregarVariables("NJUEGO", datosUsuario[1]);
            conexionWeb.agregarVariables("IDJUGADOR", datosUsuario[1]);
            conexionWeb.agregarVariables("PERSONAJE", personaje+"");

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/insertarPersonaje.php");
            conexionWeb.execute(direccion);

        }catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }//


    public void resolver(int identificador){
        try {
            conexionWeb = new ConexionWeb(MainActivity.this);
            //conexionWeb.agregarVariables("NJUEGO", datosUsuario[1]);
            conexionWeb.agregarVariables("IDJUGADOR", datosUsuario[1]);
            conexionWeb.agregarVariables("NJUEGO", njuego);
            conexionWeb.agregarVariables("PERSONAJE", identificador+"");

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/resolver.php");
            conexionWeb.execute(direccion);

        }catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }

    public void contestar(){
        alertaResponder.setTitle("PREGUNTA").show();
    }

    public void verRespuesta(){
        alertaververRespuesta.setTitle("RESPUESTA").show();
    }
}//Fin MainActivity
