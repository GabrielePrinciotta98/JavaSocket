import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class servizioServer extends Thread{

    private final Socket socketToClient;

    public servizioServer(Socket toClient) {
        this.socketToClient = toClient;
    }

    @Override
    public void run() {
        int letti, new_dist, new_price;
        byte[] buffer = new byte[100];
        StringBuilder sb = new StringBuilder();
        String cur_couple, new_couple;
        StringTokenizer st;
        final String invalid_couple = "coppia non valida";
        final String valid_couple = "coppia valida";

        try {
            InputStream fromCl = socketToClient.getInputStream();
            OutputStream toCl = socketToClient.getOutputStream();

            cur_couple = sb.append(TCPmulti_Server.coppia.getDistributore()).append(" ")
                            .append(TCPmulti_Server.coppia.getPrezzo()).toString();
            toCl.write(cur_couple.getBytes());

            while (true) {
                letti = fromCl.read(buffer);
                if (letti > 0) {
                    new_couple = new String(buffer, 0, letti);
                    System.out.println("nuova coppia ricevuta: " + new_couple);
                    if (!new_couple.matches("[1-7]\\s[0-9]{1,4}")){
                        System.out.println("coppia non valida");
                        toCl.write(invalid_couple.getBytes());
                    }else {
                        st = new StringTokenizer(new_couple);
                        new_dist = Integer.parseInt(st.nextToken());
                        new_price = Integer.parseInt(st.nextToken());
                        if (TCPmulti_Server.coppia.getPrezzo() > new_price) {
                            TCPmulti_Server.coppia.setDistributore(new_dist);
                            TCPmulti_Server.coppia.setPrezzo(new_price);
                        }
                        toCl.write(valid_couple.getBytes());
                    }
                }else {
                    throw new IOException("byte letti < 0; socket potrebbe essersi chiusa inaspettatamente");
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socketToClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
