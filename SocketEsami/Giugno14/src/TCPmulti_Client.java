import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPmulti_Client {
    public static void main(String[] args) {
        Socket sClient = new Socket();
        InetAddress ia;
        InetSocketAddress isa;
        final String identita = "U";
        byte[] buffer = new byte[100];
        int letti, count = 0;
        String dati, giudizio, ack;

        try {
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia, 22222);
            sClient.connect(isa);

            InputStreamReader tastiera = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(tastiera);

            InputStream fromSrv = sClient.getInputStream();
            OutputStream toSrv = sClient.getOutputStream();

            toSrv.write(identita.getBytes(), 0, identita.length());

            letti = fromSrv.read(buffer);
            if (letti > 0){
                dati = new String(buffer, 0, letti);
                System.out.println("Dati stazioni: " + dati);
            }
            else {
                throw new IOException("byte < 0");
            }
            buffer = new byte[100];
            while (count < 3){
                System.out.println("inserisci giudizio:");
                giudizio = br.readLine();
                toSrv.write(giudizio.getBytes(), 0, giudizio.length());

                letti = fromSrv.read(buffer);
                if (letti > 0){
                    ack = new String(buffer, 0, letti);
                    System.out.println(ack);
                    if (ack.equals("giudizio valido")){
                        count++;
                    }
                }
                else {
                    throw new IOException("byte < 0");
                }
            }
            sClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
