import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Arrays;

public class server2 {

    private static final Character[] permessi = {'A', 'B', 'C', 'D', 'E', 'F'};
    protected static String[] users_name = new String[6];
    protected static InetAddress[] users_address = new InetAddress[6];
    protected static String[] contenuti = {"NIL", "NIL", "NIL"};
    protected static boolean[] in_modifica = {false, false, false};

    public static void main(String[] args) {
        DatagramSocket sSrv = null;
        byte[] buffer = new byte[20];
        SocketAddress saCl;
        String cur_user_id, write_req;
        final String ack = "ack";
        final String nack = "nack";
        StringBuilder sb = new StringBuilder();
        try {
            sSrv = new DatagramSocket();
            System.out.println("Porta server: " + sSrv.getLocalPort());
            while (true) {
                DatagramPacket dpIn = new DatagramPacket(buffer, buffer.length);
                sSrv.receive(dpIn);
                saCl = dpIn.getSocketAddress();
                cur_user_id = new String(buffer, 0, dpIn.getLength());
                System.out.println("Client: il mio nome utente è: " + cur_user_id);

                DatagramPacket dpOut;
                if (Arrays.asList(permessi).contains(cur_user_id.charAt(0)) && !(Arrays.asList(users_name).contains(cur_user_id))) {
                    for (int i = 0; i < users_name.length; i++) {
                        if (users_name[i] == null){
                            users_name[i] = cur_user_id;
                        }
                    }
                    for (InetAddress usersAddress : users_address) {
                        if (usersAddress == null) {
                            usersAddress = dpIn.getAddress();
                        }
                    }
                    buffer = ack.getBytes();
                    dpOut = new DatagramPacket(buffer, ack.length());
                    dpOut.setSocketAddress(saCl);
                    sSrv.send(dpOut);
                } else {
                    buffer = nack.getBytes();
                    dpOut = new DatagramPacket(buffer, nack.length());
                    dpOut.setSocketAddress(saCl);
                    sSrv.send(dpOut);
                }


                buffer = new byte[100];
                dpIn = new DatagramPacket(buffer, buffer.length);
                sSrv.receive(dpIn);
                saCl = dpIn.getSocketAddress();
                write_req = new String(buffer, 0, dpIn.getLength());
                System.out.println("Client: " + write_req);
                switch (write_req.charAt(1)){
                    case 'A':
                    case 'B':
                        buffer = contenuti[0].getBytes();
                        dpOut = new DatagramPacket(buffer,buffer.length);
                        dpOut.setSocketAddress(saCl);
                        sSrv.send(dpOut);
                        in_modifica[0] = true;
                        break;
                    case 'C':
                    case 'D':
                        buffer = contenuti[1].getBytes();
                        dpOut = new DatagramPacket(buffer,buffer.length);
                        dpOut.setSocketAddress(saCl);
                        sSrv.send(dpOut);
                        in_modifica[1] = true;
                        break;
                    case 'E':
                    case 'F':
                        buffer = contenuti[2].getBytes();
                        dpOut = new DatagramPacket(buffer,buffer.length);
                        dpOut.setSocketAddress(saCl);
                        sSrv.send(dpOut);
                        in_modifica[2] = true;
                        break;
                }

                //manca la ricezione del nuovo contenuto

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sSrv != null) {
                sSrv.close();
            }
        }


    }
}
