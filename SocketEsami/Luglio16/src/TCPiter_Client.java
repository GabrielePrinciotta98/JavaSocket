import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class TCPiter_Client {
    public static void main(String[] args) {

        Socket sClient = new Socket();
        InetAddress ia;
        InetSocketAddress isa;
        boolean confermato;
        String cur_symbol, ack, ris, notifica;
        byte[] buffer;
        int letti;
        boolean dot = false;

        try {
            ia = InetAddress.getLocalHost();
            isa = new InetSocketAddress(ia, 64217);

            sClient.connect(isa);
            InputStreamReader tastiera = new InputStreamReader(System.in);
            BufferedReader br = new BufferedReader(tastiera);

            InputStream fromSrv = sClient.getInputStream();
            OutputStream toSrv = sClient.getOutputStream();

            /*System.out.println("in attesa di connessione...");
            letti = fromSrv.read(buffer);
            if (letti > 0){
                notifica = new String(buffer, 0, letti);
                if (!notifica.equals("connected")){
                    throw new IOException();
                }
            }
            System.out.println("connessione avvenuta");*/

            while (true) {
                System.out.println("Nuova operazione: ");
                confermato = false;
                buffer = new byte[100];
                while (!confermato) {
                    System.out.println("Inserisci operatore: ");
                    cur_symbol = br.readLine();
                    toSrv.write(cur_symbol.getBytes(), 0, cur_symbol.length());
                    if (cur_symbol.equals(".")) {
                        dot = true;
                        break;
                    }
                    letti = fromSrv.read(buffer);
                    if (letti > 0) {
                        ack = new String(buffer, 0, letti);
                        if (ack.equals("Operatore valido")) {
                            confermato = true;
                        } else
                            System.out.println("errore");
                    }

                }
                if (dot)
                    break;
                buffer = new byte[100];
                confermato = false;

                while (!confermato) {
                    System.out.println("Inserisci operando 1:");
                    cur_symbol = br.readLine();
                    toSrv.write(cur_symbol.getBytes(), 0, cur_symbol.length());
                    if (cur_symbol.equals(".")) {
                        dot = true;
                        break;
                    }
                    letti = fromSrv.read(buffer);
                    if (letti > 0) {
                        ack = new String(buffer, 0, letti);
                        if (ack.equals("Operando valido")) {
                            confermato = true;
                        } else
                            System.out.println("errore");
                    }
                }

                if (dot)
                    break;
                buffer = new byte[100];
                confermato = false;

                while (!confermato) {
                    System.out.println("Inserisci operando 2:");
                    cur_symbol = br.readLine();
                    toSrv.write(cur_symbol.getBytes(), 0, cur_symbol.length());
                    if (cur_symbol.equals(".")) {
                        dot = true;
                        break;
                    }
                    letti = fromSrv.read(buffer);
                    if (letti > 0) {
                        ack = new String(buffer, 0, letti);
                        if (ack.equals("Operando valido")) {
                            confermato = true;
                        } else
                            System.out.println("errore");
                    }
                }
                if (dot)
                    break;

                buffer = new byte[100];
                letti = fromSrv.read(buffer);
                if (letti > 0){
                    ris = new String(buffer, 0, buffer.length);
                    System.out.println("Il risulato è: " + ris);
                }
            }
            sClient.close();
            System.out.println("Chiusura...");

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
