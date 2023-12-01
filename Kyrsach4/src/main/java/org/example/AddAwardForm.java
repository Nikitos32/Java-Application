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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class AddAwardForm extends JDialog{
    private JTextField tfName;
    private JTextField tfId;
    private JTextField tfAmount;
    private JButton btnRegister;
    private JButton btnCancel;
    private JTextArea showTable;
    private JPanel addAwardPane;

    public AddAwardForm(JFrame parent, Socket sock){
        super(parent);
        setTitle("Adding award");
        setContentPane(addAwardPane);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            PrintWriter pr = new PrintWriter(sock.getOutputStream());
            pr.println("SHOW_CLIENTS_WITH_ID");
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

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerAward();
            }
        });
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void registerAward() {
        String name = tfName.getText();
        String id = tfId.getText();
        String amount = tfAmount.getText();

        if (name.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        award = addClientToDatabase(name, id, amount);
        if (award != null) {
            JOptionPane.showMessageDialog(this,
                    "Successful adding award: ",
                    "Try again",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to add award",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public Award award;

    private Award addClientToDatabase(String name, String id, String amount) {
        Award award = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";
        String date = "" + LocalDate.now();

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO awards (name_user, id_client, adding_date, amount)" +
                    "VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, id);
            preparedStatement.setString(3, date);
            if (amount == null) {
                preparedStatement.setString(4, "-");
            } else preparedStatement.setString(4, amount);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                award = new Award();
                award.nameUser = name;
                award.id = id;
                award.addingDate = date;
                award.amount = amount;
            }

            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return award;
    }
}
