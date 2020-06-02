import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Arrays;

public class servizioStazione extends Thread {

    private Socket sockToClient;

    public servizioStazione(Socket toClient) {
        this.sockToClient = toClient;
    }

    @Override
    public void run() {
        byte[] buffer = new byte[100];
        int letti, i;
        String id;
        int cur_station = -1;
        boolean identificato = false;
        final String invalid_id = "id non valido";
        final String invalid_fc = "forecast non valido";
        final String valid_id = "id valido";
        final String valid_fc = "forecast_valido";
        char fc;

        try {
            InputStream fromCl = sockToClient.getInputStream();
            OutputStream toCl = sockToClient.getOutputStream();
            while (!identificato) {
                letti = fromCl.read(buffer);
                id = new String(buffer, 0, letti);
                System.out.println("id client: "+ id);
                for (i = 0; i < TCPmulti_Srv.main_records.length; i++) {
                    if (id.equals(TCPmulti_Srv.main_records[i].getId())) {
                        cur_station = i;
                        identificato = true;
                        toCl.write(valid_id.getBytes(), 0, valid_id.length());
                    }
                }
                if (!identificato){
                    toCl.write(invalid_id.getBytes(), 0, invalid_id.length());
                }
            }
            System.out.println("identita letta");
            while (true) {
                buffer = new byte[100];
                letti = fromCl.read(buffer);
                    if (letti > 0) {
                        fc = new String(buffer, 0, letti).charAt(0);
                        System.out.println("fc: " + fc);
                        if (Arrays.asList(TCPmulti_Srv.fc_validi).contains(fc)) {
                            TCPmulti_Srv.main_records[cur_station].setTempo(fc);
                            toCl.write(valid_fc.getBytes(), 0, valid_fc.length());
                        } else {
                            toCl.write(invalid_fc.getBytes(), 0, invalid_fc.length());
                        }
                    }
                    else {
                        sockToClient.close();
                        System.out.println("client chiuso");
                        return;
                    }
            }

        } catch (IOException e) {
            System.err.println("IO error");
            e.printStackTrace();
        } finally {
            try {
                sockToClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
