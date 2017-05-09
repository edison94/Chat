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
        } else {
            if(!chatip.getText().equals("")){
                pw.println(chatip.getText());
                chatmsg.append(username+": "+chatip.getText()+"\n");
                chatip.setText(null);
            }
        }
    }

    public static void main(String... args) {
        String SetUserName = JOptionPane.showInputDialog(null, "Introduce el nombre de usuario:", "Luis Edison & Iago",
                JOptionPane.PLAIN_MESSAGE);
        String servername = "192.168.1.100";
        try {
            if (null!= SetUserName) {
               new Cliente(SetUserName, servername);
            }else{
                JOptionPane.showMessageDialog(null, "Introduce un nombre de usuario");
            }
        } catch (Exception ex) {
            out.println("Error: " + ex.getMessage());
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
                }
            } catch (Exception ex) {
            }
        }
    }
}
