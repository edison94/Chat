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
    JTextArea  chatmsg;
    JTextField chatip;
    JButton send,exit;
    Socket chatusers;
    
    public Cliente(String uname,String servername) throws Exception {
        super(uname); 
        this.username = uname;
        chatusers  = new Socket(servername, 1555);
        br = new BufferedReader( new InputStreamReader( chatusers.getInputStream()) ) ;
        pw = new PrintWriter(chatusers.getOutputStream(),true);
        pw.println(uname);
        buildInterface();
        new MessagesThread().start(); 
    }
    
    public void buildInterface() {
        send = new JButton("Send");
        exit = new JButton("Exit");
        chatmsg = new JTextArea();
        chatmsg.setRows(30);
        chatmsg.setColumns(50);
        chatmsg.setEditable(false);
        chatip  = new JTextField(50);
        JScrollPane sp = new JScrollPane(chatmsg, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(sp,"Center");
        JPanel bp = new JPanel( new FlowLayout());
        bp.add(chatip);
        
        bp.add(send);
        bp.add(exit);
        bp.setBackground(Color.LIGHT_GRAY);
        bp.setName("Kstark Chat Application Using Socket");
        add(bp,"North");
        send.addActionListener(this);
        exit.addActionListener(this);
        setSize(500,300);
        setVisible(true);
        pack();
    }
    
    @Override
    public void actionPerformed(ActionEvent evt) {
        if ( evt.getSource() == exit ) {
            pw.println("end"); 
            System.exit(0);
        } else {
            
            pw.println(chatip.getText());
            chatip.setText(null);
        }
    }
    
    public static void main(String ... args) {
    
        
        String SetUserName = JOptionPane.showInputDialog(null,"Please enter your name :", "Kstark Chat Application",
             JOptionPane.PLAIN_MESSAGE);
        String servername = "192.168.1.100";  
        try {
            new Cliente( SetUserName ,servername);
        } catch(Exception ex) {
            out.println( "Error:" + ex.getMessage());
        }
        
    } 
    class  MessagesThread extends Thread {
        @Override
        public void run() {
            String line;
            try {
                while(true) {
                    line = br.readLine();
                    chatmsg.append(line + "\n");
                }
            } catch(Exception ex) {}
        }
    }
} 