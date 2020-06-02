import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPmulti_stazione {
    public static void main(String[] args) {

        Socket sClient = new Socket();
        InetAddress ia;
        InetSocketAddress isa;
        String cur_fc, id, ack;
        final String identita = "S";
        byte[] buffer = new byte[100];
        int letti;
        boolean confermato = false;

        try {
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia, 22222);
            sClient.connect(isa);

            InputStreamReader tastiera = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(tastiera);

            InputStream fromSrv = sClient.getInputStream();
            OutputStream toSrv = sClient.getOutputStream();

            toSrv.write(identita.getBytes(), 0, identita.length());
            buffer = new byte[100];

            while (!confermato) {
                System.out.println("Inserisci id:");
                id = br.readLine();
                toSrv.write(id.getBytes(), 0, id.length());

                letti = fromSrv.read(buffer);
                ack = new String(buffer, 0, letti);
                System.out.println("Server: " + ack);
                if (ack.equals("id valido"))
                    confermato = true;
            }
            buffer = new byte[100];

            while (true){
                System.out.println("Inserisci previsione");
                cur_fc = br.readLine();
                toSrv.write(cur_fc.getBytes(), 0, cur_fc.length());

                if (cur_fc.equals("."))
                    break;


                letti = fromSrv.read(buffer);
                ack = new String(buffer, 0, letti);
                if (ack.equals("forecast non valido")){
                    System.out.println("Server: " + ack);
                }
            }

            sClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
