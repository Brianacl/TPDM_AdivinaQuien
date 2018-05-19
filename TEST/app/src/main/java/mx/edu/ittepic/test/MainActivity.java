package mx.edu.ittepic.test;

import android.app.ProgressDialog;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

/*
UPDATE `ROOM` SET `Turno`= 1 WHERE `ID_JUGADOR` != 2 AND `NJuego` = 1
UPDATE `ROOM` SET `Turno`= 0 WHERE `ID_JUGADOR` = 2 AND `NJuego` = 1
*/
public class MainActivity extends AppCompatActivity {

    Button btnBuscarPartida, btnEnviarRespuesta;
    EditText lblRespuesta;
    ConexionWeb conexionWeb;
    CountDownTimer miTimer, timerVerificarTurno;
    String respuesta="", ID="2", njuego="";
    TextView lblTurno;
    boolean miTurno = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBuscarPartida = findViewById(R.id.btnBuscarPartida);
        lblRespuesta = findViewById(R.id.lblRespuesta);
        lblTurno = findViewById(R.id.lblTurno);
        btnEnviarRespuesta = findViewById(R.id.btnEnviarRespuesta);

        btnEnviarRespuesta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    conexionWeb = new ConexionWeb(MainActivity.this);
                    conexionWeb.agregarVariables("IDUSUARIO", ID);
                    conexionWeb.agregarVariables("NJUEGO", njuego);

                    //Ejecución
                    URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/cambioTurno.php");
                    conexionWeb.execute(direccion);

                    btnBuscarPartida.setEnabled(false);
                    buscandoOponente();

                } catch (MalformedURLException malformed) {
                    Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnBuscarPartida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    conexionWeb = new ConexionWeb(MainActivity.this);
                    conexionWeb.agregarVariables("IDUSUARIO", ID);

                    //Ejecución
                    URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/room.php");
                    conexionWeb.execute(direccion);

                    btnBuscarPartida.setEnabled(false);
                    buscandoOponente();

                } catch (MalformedURLException malformed) {
                    Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
                }
            }
        });
    }//Fin onCreate


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

        if(respuesta.equals("1")){
            this.respuesta = respuesta;
            return;
        }

        if(respuesta.equals("0")){
            Toast.makeText(this, "Buscando jugador", Toast.LENGTH_SHORT).show();
            return;
        }

        if(respuesta.equals("SALIO_ROOM")) {
            Toast.makeText(this, "Intenta de nuevo", Toast.LENGTH_SHORT).show();
            return;
        }

        if(respuesta.length()>2 && respuesta.substring(2).equals("TURNO")){
            miTurno = true;
            njuego= respuesta.substring(0, 1);
            //Log.v("NJUEGO", njuego);
            lblTurno.setText("TU TURNO");
            lblRespuesta.setEnabled(true);
            btnEnviarRespuesta.setEnabled(true);
            return;
        }

        if(respuesta.length()>2 && respuesta.substring(2).equals("ESPERA")){
            miTurno = false;
            njuego= respuesta.substring(0, 1);
            //Log.v("NJUEGO", njuego);
            lblTurno.setText("ESPERA, POR FAVOR");
            lblRespuesta.setEnabled(false);
            btnEnviarRespuesta.setEnabled(false);
            return;
        }

        Toast.makeText(this, respuesta, Toast.LENGTH_SHORT).show();
    }//Fin procesar respuesta

    protected void buscandoOponente(){
        miTimer = new CountDownTimer(60000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(respuesta.equals("1")){
                    miTimer.cancel();
                    Toast.makeText(MainActivity.this, "Partida encontrada", Toast.LENGTH_SHORT).show();
                    checharTurno();
                    miTimer.onFinish();
                }
                try {
                    conexionWeb = new ConexionWeb(MainActivity.this);
                    conexionWeb.agregarVariables("IDUSUARIO", ID);

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
                btnBuscarPartida.setEnabled(true);
            }//Fin de onFinish
        };
        miTimer.start();
    }//Fin buscando oponente


    private void traerDatos(){
        try {
            conexionWeb = new ConexionWeb(MainActivity.this);
            conexionWeb.agregarVariables("IDUSUARIO", ID);

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/traerDatos.php");
            conexionWeb.execute(direccion);
        }catch (MalformedURLException mal){
            mal.getMessage();
        }
    }//Fin de traer datos

    private void checharTurno(){
        timerVerificarTurno = new CountDownTimer(30000, 3000) {
            @Override
            public void onTick(long millisUntilFinished) {
                traerDatos();
            }

            @Override
            public void onFinish() {
                checharTurno();
            }
        };
        timerVerificarTurno.start();
    }//Fin checarTurno

    private void noEncontroPartida(){
        try {

            conexionWeb = new ConexionWeb(MainActivity.this);
            conexionWeb.agregarVariables("IDUSUARIO", ID);

            //Ejecución
            URL direccion = new URL("https://tpdm-brian.000webhostapp.com/AdivinaQuien/noEncontroPartida.php");
            conexionWeb.execute(direccion);

        } catch (MalformedURLException malformed) {
            Toast.makeText(MainActivity.this, "No se pudo contectar con el servidor", Toast.LENGTH_LONG).show();
        }
    }//Fin noEncontroPartida
}//Fin clase
