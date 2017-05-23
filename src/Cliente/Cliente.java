package Cliente;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.lang.System.out;

public final class Cliente extends JFrame implements ActionListener {

    String username;
    PrintWriter pw;
    BufferedReader br;
    JTextArea chatmsg;
    JTextField chatip;
    JButton send, clean;
    Socket chatusers;

    public Cliente(String uname, String servername) throws Exception {
        super(uname);
        this.username = uname;
        chatusers = new Socket(servername, 1555);
        br = new BufferedReader(new InputStreamReader(chatusers.getInputStream()));
        pw = new PrintWriter(chatusers.getOutputStream(), true);
        pw.println(uname);
        buildInterface();
        leerFichero();
        new MessagesThread().start();
    }

    public void buildInterface() {
        send = new JButton("Enviar");
        clean = new JButton("Limpiar");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(50);
        chatmsg.setEditable(false);
        chatip = new JTextField(50);
        JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp, "Center");
        JPanel bp = new JPanel(new FlowLayout());
        bp.add(chatip);

        bp.add(send);
        bp.add(clean);
        bp.setBackground(Color.LIGHT_GRAY);
        bp.setName("Luis Edison & Iago");
        add(bp, "North");
        send.addActionListener(this);
        clean.addActionListener(this);
        setSize(500, 300);
        setVisible(true);
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == clean) {
            chatmsg.setText("");
            File f = new File("copia.txt");
            f.delete();
        } else {
            if(!chatip.getText().equals("")){
                pw.println(chatip.getText());
                chatmsg.append(username+": "+chatip.getText()+"\n");
                escribirFichero(username+": "+chatip.getText());
                chatip.setText(null);
            }
        }
    }

    public static void main(String... args) {
        String SetUserName = JOptionPane.showInputDialog(null, "Introduce el nombre de usuario:", "Luis Edison & Iago",
                JOptionPane.PLAIN_MESSAGE);
        String servername = "192.168.0.109";
        try {
            if (null!= SetUserName) {
               new Cliente(SetUserName, servername);
            }else{
                JOptionPane.showMessageDialog(null, "Introduce un nombre de usuario");
            }
        } catch (Exception ex) {
            out.println("Error:" + ex.getMessage());
        }

    }

    private void escribirFichero(String msg){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try
        {
            fichero = new FileWriter("copia.txt",true);
            pw = new PrintWriter(fichero);
            pw.println(msg);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {
           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
    }
    private void leerFichero() {
        try{
            File f1 = new File("copia.txt");
            if (f1.exists()){
                String cadena;
                FileReader f = new FileReader(f1);
                BufferedReader b = new BufferedReader(f);
                while((cadena = b.readLine())!=null) {
                    chatmsg.append(cadena + "\n");
                }
                b.close();
            }else{
                f1.createNewFile();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    class MessagesThread extends Thread {

        @Override
        public void run() {
            String line;
            try {
                while (true) {
                    line = br.readLine();
                    chatmsg.append(line + "\n");
                    escribirFichero(line);
                }
            } catch (Exception ex) {
            }
        }
    }
}
