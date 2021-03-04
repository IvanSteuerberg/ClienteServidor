import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Hilos extends Thread{

    private Socket newSocket;

    public Hilos(Socket newSocket) {
        this.newSocket = newSocket;
    }

    public static void consultarTiempo(Socket newSocket) {
        try {

            //InputStream is = newSocket.getInputStream();
            OutputStream os = newSocket.getOutputStream();

            Document doc = Jsoup.connect("https://weather.com/es-ES/tiempo/hoy/l/SPXX0084:1:SP").userAgent("Mozilla").get();
            Elements elementoTemperatura = doc.getElementsByClass("CurrentConditions--tempValue--3KcTQ");
            String temperatura = elementoTemperatura.text();
            Elements elementoTiempo = doc.getElementsByClass("CurrentConditions--phraseValue--2xXSr");
            Elements elementoTiempo2 = doc.getElementsByClass("CurrentConditions--precipValue--RBVJT");
            String tiempo2 = elementoTiempo2.text();
            String tiempo = elementoTiempo.text();
            Elements elementoVigo = doc.getElementsByClass("CurrentConditions--location--1Ayv3");
            String vigo = elementoVigo.text();
            String mensajeTiempo = vigo + "\n" + temperatura + "\n" + tiempo + "\n" + tiempo2 + "\nDatos extraídos de " + doc.title();
            os.write(mensajeTiempo.getBytes());
            System.out.println("Mensaje de tiempo enviado");


        } catch (IOException e) {
        }
    }

    public static void chatear(Socket newSocket) {
        try {
            InputStream is = newSocket.getInputStream();
            OutputStream os = newSocket.getOutputStream();
            String contestacion;
            System.out.println("Escribe 'aaa' para terminar el chat!");
            do {
                byte[] mensaje = new byte[20];
                is.read(mensaje);
                String mensajeRecibido = new String(mensaje);
                System.out.println("Mensaje recibido: " + mensajeRecibido);
                contestacion = JOptionPane.showInputDialog(mensajeRecibido);
                while (contestacion.length() > 20) {
                    System.out.println("EL LIMITE DE CARACTERES ES 20!");
                    contestacion = JOptionPane.showInputDialog(mensajeRecibido);
                }
                while (contestacion.length() < 1) {
                    System.out.println("No puedes enviar mensajes en blanco!");
                    contestacion = JOptionPane.showInputDialog(mensajeRecibido);
                }
                System.out.println("Enviado: " + contestacion);
                if (contestacion.equalsIgnoreCase("aaa")) {
                    contestacion = "aaaaaaaaaaaaaaaaaaaa";
                }
                os.write(contestacion.getBytes());
            } while (!contestacion.equalsIgnoreCase("aaaaaaaaaaaaaaaaaaaa"));

        } catch (IOException e) {
        }

    }

    public static void hacerOperacion(Socket newSocket) {
        try {

            InputStream is = newSocket.getInputStream();
            OutputStream os = newSocket.getOutputStream();
            int opcion;
            do {
                String mensaje_ = "Mandame el primer operando:";
                os.write(mensaje_.getBytes());
                byte[] mensaje1 = new byte[10];
                is.read(mensaje1);
                int operando1 = mensaje1[0];
                System.out.println("Recibido " + operando1);
                String mensaje2_ = "Mandame el segundo operando:";
                os.write(mensaje2_.getBytes());
                byte[] mensaje2 = new byte[10];
                is.read(mensaje2);
                int operando2 = mensaje2[0];
                System.out.println("Recibido " + operando2);

                String mensajeMenu = "   MENÚ  \n1-->Sumar\n2-->Restar \n3-->Multiplicar"
                        + "\n4-->Dividir \n0-->Salir";
                os.write(mensajeMenu.getBytes());

                byte[] mensaje3 = new byte[10];
                is.read(mensaje3);
                opcion = mensaje3[0];
                int resultado;
                switch (opcion) {
                    case 1:
                        System.out.println("Seleccionado SUMA");
                        resultado = operando1 + operando2;
                        String resultado2;
                        if (resultado>=100) {
                            resultado2 = "Resultado " + resultado + "   ";
                        }
                        else {
                            resultado2 = "Resultado " + resultado + "    ";
                        }
                        os.write(resultado2.getBytes());
                        break;
                    case 2:
                        System.out.println("Seleccionado RESTA");
                        resultado = operando1 - operando2;
                        resultado2 = "Resultado " + resultado + "    ";
                        os.write(resultado2.getBytes());
                        break;
                    case 3:
                        System.out.println("Seleccionado MULTIPLICACION");
                        resultado = operando1 * operando2;
                        resultado2 = "Resultado " + resultado + "    ";
                        System.out.println(resultado2);
                        os.write(resultado2.getBytes());
                        break;
                    case 4:
                        System.out.println("Seleccionado DIVISION");
                        resultado = operando1 / operando2;
                        resultado2 = "Resultado " + resultado + "    ";
                        os.write(resultado2.getBytes());
                        break;
                    case 0:
                        System.out.println("Saliendo... ");
                        os.write("Saliendo...    ".getBytes());
                }
            } while (opcion != 0);
        } catch (IOException e) {
        }

    }

    public void run(){
        try {
        InputStream is = newSocket.getInputStream();
        OutputStream os = newSocket.getOutputStream();
        int opcionSwitch;
        do {
            String mensaje = "   Que quieres hacer?  \n1-->Operaciones\n2-->Consultar el tiempo\n3-->Chatear \n0-->Salir";
            os.write(mensaje.getBytes());
            byte[] mensajeCliente = new byte[10];
            is.read(mensajeCliente);
            opcionSwitch = mensajeCliente[0];
            switch (opcionSwitch) {
                case 1:
                    hacerOperacion(newSocket);
                    break;
                case 2:
                    consultarTiempo(newSocket);
                    break;
                case 3:
                    chatear(newSocket);
                    break;
            }
        } while (opcionSwitch != 0);
        System.out.println("Cerrando el nuevo socket");
        newSocket.close();
    } catch (IOException e) {
    }


    }


}
