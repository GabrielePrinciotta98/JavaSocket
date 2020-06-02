import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.StringTokenizer;

public class UDP_server {

    private static final Treno[] treni = new Treno[9];

    public static void main(String[] args) {

        for (int i = 0; i < treni.length; i++) {
            treni[i] = new Treno(i+1);
        }

        DatagramSocket sSrv = null;
        byte[] buffer = new byte[100];
        String identita, ritardo;
        StringTokenizer st;
        int cur_id, cur_rit;
        final String ack = "Server: ritardo aggiornato";
        SocketAddress isaCl;
        int num;
        final String invalid_train_number = "Server: numero treno non valido";

        try {
            sSrv = new DatagramSocket(0);
            System.out.println("Porta server: " + sSrv.getLocalPort());
            DatagramPacket dpOut;
            DatagramPacket dpIn;

            while (true) {
                System.out.println("In attesa di client...");
                dpIn = new DatagramPacket(buffer, buffer.length);
                sSrv.receive(dpIn);
                isaCl = dpIn.getSocketAddress();
                identita = new String(buffer, 0, dpIn.getLength());
                System.out.println(identita);
                buffer = new byte[100];

                if (Character.isUpperCase(identita.charAt(0))) {
                    while (true) {
                        dpIn = new DatagramPacket(buffer, buffer.length);
                        sSrv.receive(dpIn);
                        ritardo = new String(buffer, 0, dpIn.getLength());
                        System.out.println("Ritardo: " + ritardo);

                        if (ritardo.equals(".")) {
                            break;
                        }
                        st = new StringTokenizer(ritardo);
                        cur_id = Integer.parseInt(st.nextToken());
                        cur_rit = Integer.parseInt(st.nextToken());

                        treni[cur_id - 1].setIdentita(identita);
                        treni[cur_id - 1].setRitardo(cur_rit);
                        System.out.println("La stazione " + identita + " ha comunicato un ritardo di "
                                + cur_rit + " per il treno " + cur_id);
                        buffer = ack.getBytes();
                        dpOut = new DatagramPacket(buffer, ack.length());
                        dpOut.setSocketAddress(isaCl);
                        sSrv.send(dpOut);
                    }
                } else {
                    try {
                        if (identita.equals("."))
                            continue;
                        num = Integer.parseInt(identita);
                        StringBuilder sb = new StringBuilder();
                        if (num >= 1 && num <= 9){
                            String query = sb.append(treni[num - 1].getRitardo())
                                                .append(", aggiornato dalla stazione ")
                                                .append(treni[num - 1].getIdentita()).toString();
                            buffer = query.getBytes();
                            dpOut = new DatagramPacket(buffer, buffer.length);
                            dpOut.setSocketAddress(isaCl);
                            sSrv.send(dpOut);
                        }
                        else{
                            buffer = invalid_train_number.getBytes();
                            dpOut = new DatagramPacket(buffer, buffer.length);
                            dpOut.setSocketAddress(isaCl);
                            sSrv.send(dpOut);
                        }

                    }catch (NumberFormatException nfe){
                        throw new IllegalHostException("client non identificabile");
                    }

                }




            }
        } catch (IOException | IllegalHostException e) {
            e.printStackTrace();
        } finally {
            assert sSrv != null;
            sSrv.close();
        }
    }

    private static class IllegalHostException extends Throwable {
        public IllegalHostException(String client_non_identificabile) {
        }
    }
}
