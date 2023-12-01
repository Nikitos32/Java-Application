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

public class AwardsDashboardForm extends JFrame {
    private JTextArea showTable;
    private JButton showAllButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton addButton;
    private JPanel workingWithAwardsPane;
    private JButton calcAwardButton;

    public AwardsDashboardForm (Socket sock) throws IOException {
        setTitle("Awards");
        setContentPane(workingWithAwardsPane);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("SHOW_AWARDS");
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

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("EDIT_AWARD");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                EditAwardsForm editAwardsForm = new EditAwardsForm(AwardsDashboardForm.this, sock);
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("ADD_AWARD");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                AddAwardForm addAwardForm = new AddAwardForm(AwardsDashboardForm.this, sock);
                Award award = addAwardForm.award;
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("DELETE_AWARD");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                DeleteAwardForm deleteAwardForm = new DeleteAwardForm(AwardsDashboardForm.this, sock);
            }
        });


        calcAwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("CALC_AWARD");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                CalculateAwardForm calculateAwardForm = new CalculateAwardForm(AwardsDashboardForm.this, sock);
            }
        });

        setVisible(true);
    }
}
