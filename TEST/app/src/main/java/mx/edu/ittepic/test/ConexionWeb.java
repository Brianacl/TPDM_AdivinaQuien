package mx.edu.ittepic.test;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ConexionWeb extends AsyncTask<URL, String, String> {

    List<String[]> variables;
    MainActivity punteroMain;

    public ConexionWeb(MainActivity direccionActivity){
        punteroMain = direccionActivity;
        variables = new ArrayList<>();
    }//fin constructor

    public void agregarVariables(String variable, String contenido){
        String [] temp = new String[2];
        temp[0] = variable;
        temp[1] = contenido;
        //temp[2] = respuetaDos;
        //temp[3] = respuestaTres;

        variables.add(temp);
    }//Fin agregarVariables

    @Override
    protected String doInBackground(URL... urls) {
        String POST = "";
        String respuesta = "";

        for(int i=0; i < variables.size(); i++){
            String[] temporal = variables.get(i);
            try{
                POST+= temporal[0]+"="+ URLEncoder.encode(temporal[1], "UTF-8")+" ";
            }catch (Exception e){
                return "ERROR_404_0";
            }
        }//Fin for

        POST = POST.trim();
        POST = POST.replace(" ","&");
        Log.v("POST", POST);
        HttpURLConnection conexion = null;

        try{
            publishProgress("Intentando conectar");
            conexion = (HttpURLConnection) urls[0].openConnection();//Encontro el servidor

            conexion.setDoInput(true);
            conexion.setFixedLengthStreamingMode(POST.length()); //Cantidad de bytes que va a mandar
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "Application/x-www-form-urlencoded");

            publishProgress("Recuperando respuesta del servidor");

            OutputStream flujoSalida = new BufferedOutputStream(conexion.getOutputStream());
            flujoSalida.write(POST.getBytes());

            flujoSalida.flush();
            flujoSalida.close();

            if (conexion.getResponseCode() == 200){
                InputStreamReader input = new InputStreamReader(conexion.getInputStream(), "UTF-8");

                BufferedReader flujoEntrada = new BufferedReader(input); //Lee una lína completa
                String linea = "";

                do{
                    linea = flujoEntrada.readLine();
                    if (linea != null) {
                        respuesta += linea;
                    }
                }while (linea != null);
                flujoEntrada.close();
            }else{
                return "ERROR: 404_1";
            }
        } catch (UnknownHostException unknown){
            return "ERROR: 404_2";
        } catch (IOException ioe){
            return "ERROR: 404_3";
        }finally {
            if (conexion != null){
                conexion.disconnect();
            }
        }
        return respuesta;
    }//fin doInBackGroud

    protected void onProgressUpdate(String... r){

    }

    protected void onPostExecute(String respuesta){
        punteroMain.procesarRespuesta(respuesta);
    }//fin de onPostExecute

}//Fin clase
