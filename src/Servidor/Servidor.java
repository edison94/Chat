package Servidor;

import java.io.*;
import java.util.*;
import java.net.*;
import static java.lang.System.out;

public class Servidor {

    public static void main(String... args) throws Exception {
        new Servidor().createserver();
    }

    ArrayList<String> users = new ArrayList<>();
    ArrayList<Manageuser> clients = new ArrayList<>();

    public void createserver() throws Exception {
        ServerSocket server = new ServerSocket(1555, 10);
        out.println("El servidor esta arrancado");
        while (true) {
            Socket client = server.accept();
            Manageuser c = new Manageuser(client);
            clients.add(c);
            c.sendMessage(c.getchatusers(), "te has unido al chat.");
            sendtoall(c.getchatusers(), "se ha unido al chat.");
        }
    }

    public void sendtoall(String user, String message) {
        for (Manageuser c : clients) {
            if (!c.getchatusers().equals(user)) {
                c.sendMessage(user, message);
            }
        }
    }

    class Manageuser extends Thread {

        String gotuser = "";
        BufferedReader input;
        PrintWriter output;

        public Manageuser(Socket client) throws Exception {

            input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            output = new PrintWriter(client.getOutputStream(), true);

            gotuser = input.readLine();
            users.add(gotuser);
            start();
        }

        public void sendMessage(String chatuser, String chatmsg) {
            output.println(chatuser + ": " + chatmsg);
        }

        public String getchatusers() {
            return gotuser;
        }

        @Override
        public void run() {
            String line;
            try {
                while (true) {
                    line = input.readLine();
                    if (line.equals("end")) {
                        clients.remove(this);
                        users.remove(gotuser);
                        break;
                    }
                    sendtoall(gotuser, line);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}
