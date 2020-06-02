import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPmulti_Srv {

    public static Record[] main_records = new Record[3];
    protected static final Character[] fc_validi = {'s', 'v', 'c', 'p', 'n', '-'};

    public static void main(String[] args) {
        ServerSocket sSrv;
        Socket toClient;
        int letti;
        byte[] buffer = new byte[100];
        String identita;
        Thread t = null;

        main_records[0] = new Record("A");
        main_records[1] = new Record("B");
        main_records[2] = new Record("C");

        try {
            sSrv = new ServerSocket(22222);
            while (true){
                toClient = sSrv.accept();
                System.out.println("Indirizzo nuovo client: " + toClient.getInetAddress() + " ; porta: " + toClient.getPort());
                InputStream fromCl = toClient.getInputStream();
                letti = fromCl.read(buffer);
                identita = new String(buffer, 0, letti);

                if (identita.equals("S")) {
                    t = new servizioStazione(toClient);
                }
                else if (identita.equals("U")) {
                    t = new servizioUtente(toClient);
                }
                else {
                    System.out.println("client non riconosciuto");
                }
                if (t != null) {
                    t.start();
                }
                System.out.println("ciao");
                for (Record main_record : main_records) {
                    System.out.print(main_record.getTempo() + " ");
                    System.out.println(main_record.getFeedback());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
