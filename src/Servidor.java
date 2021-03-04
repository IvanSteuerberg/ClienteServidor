import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;

public class Servidor {

    public static void main(String[] args) throws IOException, InterruptedException {

        System.out.println("Creando socket servidor");

        ServerSocket serverSocket = new ServerSocket();

        System.out.println("Realizando el bind");

        InetSocketAddress addr = new InetSocketAddress("192.168.1.141", 6666);
        serverSocket.bind(addr);

        System.out.println("Aceptando conexiones");
while (true) {
    Socket newSocket = serverSocket.accept();
    System.out.println("Conexión recibida");
    new Hilos(newSocket).start();

}

    }
    }






