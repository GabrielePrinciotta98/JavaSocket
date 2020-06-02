import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UDP_stazione {
    public static void main(String[] args) {

        DatagramSocket sClient;
        InetAddress ia;
        InetSocketAddress isa;

        InputStreamReader tastiera = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(tastiera);
        byte[] buffer;
        String identita, ritardo, ack;

        try {
            sClient = new DatagramSocket();
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia, 60812);

            System.out.println("Inserisci identità:");
            identita = br.readLine();
            buffer = identita.getBytes();
            DatagramPacket dpIn;
            DatagramPacket dpOut = new DatagramPacket(buffer, buffer.length);
            dpOut.setSocketAddress(isa);
            sClient.send(dpOut);

            while (true) {

                System.out.println("Inserisci ritardo: ");
                ritardo = br.readLine();
                buffer = ritardo.getBytes();
                dpOut = new DatagramPacket(buffer, buffer.length);
                dpOut.setSocketAddress(isa);
                sClient.send(dpOut);

                if (ritardo.equals(".")) {
                    break;
                }

                buffer = new byte[100];
                dpIn = new DatagramPacket(buffer, buffer.length);
                sClient.receive(dpIn);
                ack = new String(buffer, 0, dpIn.getLength());
                System.out.println(ack);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
