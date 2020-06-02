import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

public class TCPiter_Server {

    public static void main(String[] args) {
        ServerSocket sSrv;
        Socket toClient;
        byte[] buffer;
        int letti;
        final String[] simboli = {"+", "-", "*", "/"};
        String cur_sym = null;
        String s;
        String ris;
        double cur_num = 0, cur_num2 = 1, result = 0;
        final String invalid_operator = "Operatore non valido";
        final String valid_operator = "Operatore valido";
        final String invalid_operand = "Operando non valido";
        final String valid_operand = "Operando valido";
        //final String notifica = "connected";
        boolean operatore_corretto;
        boolean operando_corretto;
        boolean dot;
        try {
            sSrv = new ServerSocket(0, 3);
            System.out.println("Porta server: " + sSrv.getLocalPort());
            while (true) {
            toClient = sSrv.accept();
            System.out.println("Indirizzo nuovo client: " + toClient.getInetAddress() + " ; porta: " + toClient.getPort());

            OutputStream toCl = toClient.getOutputStream();
            InputStream fromCl = toClient.getInputStream();

            //toCl.write(notifica.getBytes(), 0, notifica.length());

                while (true) {
                    System.out.println("------------------------------------");
                    buffer = new byte[100];
                    operatore_corretto = false;
                    operando_corretto = false;
                    dot = false;
                    while (!operatore_corretto) {
                        letti = fromCl.read(buffer);

                        if (letti > 0) {
                            cur_sym = new String(buffer, 0, letti);
                            System.out.println("Simbolo ricevuto: " + cur_sym);

                            if (cur_sym.equals(".")) {
                                dot = true;
                                break;
                            }
                            if (!Arrays.asList(simboli).contains(cur_sym)) {
                                toCl.write(invalid_operator.getBytes(), 0, invalid_operator.length());
                            } else {
                                toCl.write(valid_operator.getBytes(), 0, valid_operator.length());
                                operatore_corretto = true;
                            }
                        }
                        else {
                            toCl.write(invalid_operator.getBytes(), 0, invalid_operator.length());
                        }
                    }
                    if (dot)
                        break;

                    buffer = new byte[100];

                    while (!operando_corretto) {
                        letti = fromCl.read(buffer);

                        if (letti > 0) {
                            s = new String(buffer, 0, letti);
                            if (s.equals(".")) {
                                dot = true;
                                break;
                            }

                            try {
                                cur_num = Double.parseDouble(s);
                                System.out.println("Numero ricevuto: " + cur_num);
                                toCl.write(valid_operand.getBytes(), 0, valid_operand.length());
                                operando_corretto = true;
                            } catch (NumberFormatException nfe) {
                                toCl.write(invalid_operand.getBytes(), 0, invalid_operand.length());
                            }
                        }
                        else{
                            toCl.write(invalid_operand.getBytes(), 0, invalid_operand.length());
                        }
                    }
                    if (dot)
                        break;

                    buffer = new byte[100];

                    operando_corretto = false;
                    while (!operando_corretto) {
                        letti = fromCl.read(buffer);

                        if (letti > 0) {
                            s = new String(buffer, 0, letti);
                            if (s.equals(".")) {
                                dot = true;
                                break;
                            }

                            try {
                                cur_num2 = Double.parseDouble(s);
                                System.out.println("Numero ricevuto: " + cur_num2);
                                toCl.write(valid_operand.getBytes(), 0, valid_operand.length());
                                operando_corretto = true;
                            } catch (NumberFormatException nfe) {
                                toCl.write(invalid_operand.getBytes(), 0, invalid_operand.length());
                            }
                        }
                        else{
                            toCl.write(invalid_operand.getBytes(), 0, invalid_operand.length());
                        }
                    }

                    if (dot)
                        break;

                    switch (cur_sym) {
                        case "+":
                            result = cur_num + cur_num2;
                            break;
                        case "-":
                            result = cur_num - cur_num2;
                            break;
                        case "*":
                            result = cur_num * cur_num2;
                            break;
                        case "/":
                            result = cur_num / cur_num2;
                            break;
                    }
                    ris = Double.toString(result);
                    toCl.write(ris.getBytes(), 0, ris.length());
                }
                toClient.close();
                System.out.println("client chiuso\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
