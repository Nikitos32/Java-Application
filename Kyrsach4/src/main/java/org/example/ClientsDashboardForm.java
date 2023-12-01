package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Objects;

public class ClientsDashboardForm extends JFrame {
    private JTextArea showTable;
    private JButton showAllButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton addButton;
    private JPanel workingWithClientPane;
    private JButton workWithAwardsButton;

    public ClientsDashboardForm(Socket sock) throws IOException{
        setTitle("Clients");
        setContentPane(workingWithClientPane);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("SHOW_CLIENTS");
                    pr.flush();
                    InputStreamReader in = new InputStreamReader(sock.getInputStream());
                    BufferedReader bf = new BufferedReader(in);
                    String str;
                    showTable.setText("");
                    while (!Objects.equals(str = bf.readLine(), "END")) {
                        showTable.append(str + "\n");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("ADD_CLIENT");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                AddClientForm addClientForm = new AddClientForm(ClientsDashboardForm.this, sock);
                Client client1 = addClientForm.client;
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("EDIT_CLIENT");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                EditClientsForm editClientsForm = new EditClientsForm(ClientsDashboardForm.this, sock);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("DELETE_CLIENT");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                DeleteClientsForm deleteClientsForm = new DeleteClientsForm(ClientsDashboardForm.this, sock);
            }
        });

        workWithAwardsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("WORK_WITH_AWARDS");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    AwardsDashboardForm awardsDashboardForm = new AwardsDashboardForm(sock);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        setVisible(true);
    }
}
