import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class utente2 {
    public static void main(String[] args) {
        DatagramSocket sClient = null;
        byte[] buffer;
        InetAddress ia;
        InetSocketAddress isa;
        String username = null, ack, write_req_confirm, cur_content, new_content;
        final String write_req = "Posso modificare i contenuti?";
        boolean confermato = false;
        StringBuilder sb = new StringBuilder();

        try {
            sClient = new DatagramSocket();
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia, 52587);

            InputStreamReader tastiera = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(tastiera);
            while(!confermato) {
                System.out.println("Inserisci username: ");
                username = br.readLine();
                buffer = username.getBytes();
                DatagramPacket dpOut = new DatagramPacket(buffer, username.length());
                dpOut.setSocketAddress(isa);
                sClient.send(dpOut);

                buffer = new byte[20];
                DatagramPacket dpIn = new DatagramPacket(buffer, buffer.length);
                sClient.receive(dpIn);
                ack = new String(buffer, 0, dpIn.getLength());
                System.out.println("Server: " + ack);
                if (ack.equals("ack")) {
                    confermato = true;
                }
                else
                    System.err.println("Autenticazione fallita");
            }
            System.out.println("Autenticazione avvenuta");
            while (true){
                System.out.println("Modificare il contenuto della propria coppia?(y/n)");
                write_req_confirm = br.readLine();
                if (write_req_confirm.equals("n"))
                    continue;
                buffer = sb.append("(").append(username).append("): ").append(write_req).toString().getBytes();
                DatagramPacket dpOut = new DatagramPacket(buffer, buffer.length);
                dpOut.setSocketAddress(isa);
                sClient.send(dpOut);

                buffer = new byte[20];
                DatagramPacket dpIn = new DatagramPacket(buffer, buffer.length);
                sClient.receive(dpIn);
                cur_content = new String(buffer, 0, dpIn.getLength());
                System.out.println("Server: " + cur_content);

                System.out.println("Inserisci nuovo contenuto:");
                new_content = br.readLine();
                buffer = new_content.getBytes();
                dpOut = new DatagramPacket(buffer, buffer.length);
                dpOut.setSocketAddress(isa);
                sClient.send(dpOut);

                //manca la ricezione della risposta del server riguardo il nuovo contenuto
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sClient != null) {
                sClient.close();
            }
        }
    }
}
