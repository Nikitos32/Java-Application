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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class EditUsersForm extends JDialog {
    private JTextField enterNameField;
    private JTextArea showTable;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JTextField tfPassword;
    private JButton btnSendName;

    private JPanel editUsersPane;
    private JButton btnUpdate;

    public EditUsersForm (JFrame parent, Socket sock) {
        super(parent);
        setTitle("Edit Users");
        setContentPane(editUsersPane);
        setMinimumSize(new Dimension(1000, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        try {
            PrintWriter pr = new PrintWriter(sock.getOutputStream());
            pr.println("SHOW_USERS");
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

        btnSendName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillJTextAreas();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUser();
            }
        });

        setVisible(true);
    }

    private void updateUser(){
        String email = enterNameField.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";
        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();

            String sql = "UPDATE users SET name = ?, address = ?, email =?,phone =?,password =? WHERE email = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, tfName.getText());
            preparedStatement.setString(2, tfAddress.getText());
            preparedStatement.setString(3, tfEmail.getText());
            preparedStatement.setString(4, tfPhone.getText());
            preparedStatement.setString(5, tfPassword.getText());
            preparedStatement.setString(6, email);

            preparedStatement.executeUpdate();

            stmt.close();
            connection.close();

            JOptionPane.showMessageDialog(this,
                    "Data successfully updated",
                    "Ok",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void fillJTextAreas(){
        String email = enterNameField.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM users WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tfName.setText(resultSet.getString("name"));
                tfAddress.setText(resultSet.getString("address"));
                tfEmail.setText(resultSet.getString("email"));
                tfPhone.setText(resultSet.getString("phone"));
                tfPassword.setText(resultSet.getString("password"));
            }

            stmt.close();
            connection.close();
        }
             catch (Exception e){
                e.printStackTrace();
            }
    }
}
