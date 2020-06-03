import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPmulti_Server {

    protected static DistrPrezzo coppia = new DistrPrezzo(1, 9999);

    public static void main(String[] args) {
        ServerSocket sSrv;
        Socket toClient;
        Thread t;
        try {
            sSrv = new ServerSocket(55555);

            while (true) {
                toClient = sSrv.accept();
                t = new servizioServer(toClient);
                t.start();

                System.out.println("Coppia corrente: " + coppia.getDistributore() +"; "+ coppia.getPrezzo());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
