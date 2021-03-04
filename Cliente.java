import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Cliente {
    public static void consultarTiempo(Socket clienteSocket) {
        try {
            InputStream is = clienteSocket.getInputStream();
            //OutputStream os = clienteSocket.getOutputStream();

            byte[] mensajeTiempo = new byte[200];
            is.read(mensajeTiempo);
            System.out.println("Mensaje recibido: " + new String(mensajeTiempo));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chatear(Socket clienteSocket) {
        try {
            InputStream is = clienteSocket.getInputStream();
            OutputStream os = clienteSocket.getOutputStream();
            String mensajeRecibido="Escribe un mensaje";
            do {
                String enviarMensaje = JOptionPane.showInputDialog(mensajeRecibido);
                while (enviarMensaje.length()>20){
                    System.out.println("EL LIMITE DE CARACTERES ES 20");
                    enviarMensaje = JOptionPane.showInputDialog(mensajeRecibido);
                }
                while (enviarMensaje.length()<1){
                    System.out.println("No puedes enviar mensajes en blanco!");
                    enviarMensaje = JOptionPane.showInputDialog(mensajeRecibido);
                }
                System.out.println("Envidado: "+enviarMensaje);
                os.write(enviarMensaje.getBytes());
                byte[] mensaje = new byte[20];
                is.read(mensaje);
                mensajeRecibido = new String(mensaje);
                System.out.println("Mensaje recibido: " + mensajeRecibido);

            } while (!mensajeRecibido.equalsIgnoreCase("aaaaaaaaaaaaaaaaaaaa"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void hacerOperacion(Socket clienteSocket) {
        try {
            InputStream is = clienteSocket.getInputStream();
            OutputStream os = clienteSocket.getOutputStream();
            int opcion;
            do {
                byte[] mensaje = new byte[35];
                byte[] mensaje2 = new byte[30];
                is.read(mensaje);
                //System.out.println("Mensaje recibido: " + new String(mensaje));
                //Scanner sc = new Scanner(System.in);
                //int numero1 = sc.nextInt();
                int numero1 = Integer.parseInt(JOptionPane.showInputDialog(new String(mensaje)));
                while (numero1 >= 128 || numero1 < -128) {
                    //System.out.println(new String(mensaje));
                    //numero1 = sc.nextInt();
                    numero1 = Integer.parseInt(JOptionPane.showInputDialog(new String(mensaje)));
                }
                os.write(numero1);
                is.read(mensaje2);
                //System.out.println("Mensaje recibido: " + new String(mensaje2));
                //int numero2 = sc.nextInt();
                int numero2 = Integer.parseInt(JOptionPane.showInputDialog(new String(mensaje2)));
                while (numero2 >= 128 || numero2 < -128) {
                    //System.out.println(new String(mensaje2));
                    //numero2 = sc.nextInt();
                    numero2 = Integer.parseInt(JOptionPane.showInputDialog(new String(mensaje2)));
                }
                os.write(numero2);
                byte[] mensaje3 = new byte[100];
                is.read(mensaje3);
                opcion = Integer.parseInt(JOptionPane.showInputDialog(new String(mensaje3)));
                os.write(opcion);
                byte[] resultado = new byte[15];
                is.read(resultado);
                System.out.println("Mensaje recibido: " + new String(resultado));
            } while (opcion != 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("Creando socket cliente");
            Socket clienteSocket = new Socket();
            System.out.println("Estableciendo la conexión");
            InetSocketAddress addr = new InetSocketAddress("192.168.1.141", 6666);
            clienteSocket.connect(addr);
            InputStream is = clienteSocket.getInputStream();
            OutputStream os = clienteSocket.getOutputStream();
            int opcionSwitch;
            do {
                byte[] mensajeInicial = new byte[86];
                is.read(mensajeInicial);
                opcionSwitch = Integer.parseInt(JOptionPane.showInputDialog(new String(mensajeInicial)));
                os.write(opcionSwitch);
                switch (opcionSwitch) {
                    case 1:
                        hacerOperacion(clienteSocket);
                        break;
                    case 2:
                        consultarTiempo(clienteSocket);
                        break;
                    case 3:
                        chatear(clienteSocket);
                        break;
                }
            } while (opcionSwitch != 0);
            System.out.println("Cerrando el socket cliente");
            clienteSocket.close();
            System.out.println("Terminado");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}