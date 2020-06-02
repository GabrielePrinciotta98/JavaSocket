import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDP_utente {
    public static void main(String[] args) {
        DatagramSocket sClient;
        InetSocketAddress isa;
        InetAddress ia;
        String query, dati;
        byte[] buffer;
        InputStreamReader tastiera = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(tastiera);
        DatagramPacket dpOut;
        DatagramPacket dpIn;

        try {
            sClient = new DatagramSocket();
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia, 60812);

            while (true) {
                System.out.println("Inserisci numero treno:");
                query = br.readLine();
                if (query.equals(".")){
                    break;
                }
                buffer = query.getBytes();
                dpOut = new DatagramPacket(buffer, buffer.length);
                dpOut.setSocketAddress(isa);
                sClient.send(dpOut);

                buffer = new byte[100];
                dpIn = new DatagramPacket(buffer, buffer.length);
                sClient.receive(dpIn);
                dati = new String(buffer, 0, dpIn.getLength());
                if (!dati.equals("Server: numero treno non valido"))
                    System.out.println("risultato: " + dati);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
