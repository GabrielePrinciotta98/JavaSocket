import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPmulti_Client {

    public static void main(String[] args) {
        Socket sClient = new Socket();
        InetAddress ia;
        InetSocketAddress isa;
        byte[] buffer = new byte[100];
        InputStreamReader tastiera = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(tastiera);
        int letti;
        String cur_couple, new_couple, ack;

        try {
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia, 55555);
            sClient.connect(isa);

            InputStream fromSrv = sClient.getInputStream();
            OutputStream toSrv = sClient.getOutputStream();

            System.out.println("lettura coppia corrente...");
            letti = fromSrv.read(buffer);
            if (letti > 0){
                cur_couple = new String(buffer, 0, letti);
                System.out.println("coppia corrente: "+ cur_couple);
            }
            else
                throw new IOException("byte letti < 0");
            buffer = new byte[100];
            while (true){
                System.out.println("Inserisci nuova coppia:");
                new_couple = br.readLine();

                if (new_couple.equals("."))
                    break;

                toSrv.write(new_couple.getBytes());

                letti = fromSrv.read(buffer);
                if (letti > 0){
                    ack = new String(buffer, 0, letti);
                    if (ack.equals("coppia non valida"))
                        System.out.println("coppia inserita non valida! riprovare");
                }

            }
            sClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                sClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
