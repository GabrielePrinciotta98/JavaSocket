import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class servizioUtente extends Thread {

    private Socket sockToClient;

    public servizioUtente(Socket toClient) {
        this.sockToClient = toClient;

    }

    @Override
    public void run() {

        byte[] buffer = new byte[100];
        StringBuilder sb = new StringBuilder();
        StringTokenizer st;
        int letti, giudizio, cur_record = -1, count = 0;
        String string_giudizio, id;
        final String id_non_valido = "id non valido";
        final String giudizio_non_valido = "giudizio non valido";
        final String giudizio_valido = "giudizio valido";

        try {
            InputStream fromCl = sockToClient.getInputStream();
            OutputStream toCl = sockToClient.getOutputStream();
            for (int i = 0; i < 3; i++) {
                sb.append(TCPmulti_Srv.main_records[i].getId());
                sb.append(" ");
                sb.append(TCPmulti_Srv.main_records[i].getTempo());
                sb.append(" ");
                sb.append(TCPmulti_Srv.main_records[i].getFeedback());
                sb.append(" ~ ");
            }

            toCl.write(sb.toString().getBytes(), 0, sb.toString().length());

            while (count < 3) {
                letti = fromCl.read(buffer);
                if (letti > 0) {
                    string_giudizio = new String(buffer, 0, letti);
                    st = new StringTokenizer(string_giudizio, " ");
                    id = st.nextToken();
                    if (!id.equals("A") && !id.equals("B") && !id.equals("C")){
                        toCl.write(id_non_valido.getBytes(), 0, id_non_valido.length());
                        continue;
                    }

                    for (int i = 0; i < TCPmulti_Srv.main_records.length; i++) {
                        if (id.equals(TCPmulti_Srv.main_records[i].getId()))
                            cur_record = i;
                    }

                    giudizio = Integer.parseInt(st.nextToken());
                    if (giudizio >=-1 && giudizio <= 1) {
                        TCPmulti_Srv.main_records[cur_record].setFeedback(TCPmulti_Srv.main_records[cur_record].getFeedback() + giudizio);
                        count++;
                        toCl.write(giudizio_valido.getBytes(), 0, giudizio_valido.length());
                    }
                    else{
                        toCl.write(giudizio_non_valido.getBytes(), 0, giudizio_non_valido.length());
                    }
                }
                else{
                    throw new IOException("byte < 0");
                }
            }
            sockToClient.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                sockToClient.close();
            } catch (IOException e) {
                System.err.println("cannot close the socket");
                e.printStackTrace();
            }
        }

    }
}
