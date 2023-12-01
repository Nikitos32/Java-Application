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

public class EditClientsForm extends JDialog {
    private JTextField tfName;
    private JTextField tfAge;
    private JTextField tfPost;
    private JTextField tfSurname;
    private JButton btnUpdate;
    private JTextField enterNameField;
    private JTextArea showTable;
    private JButton btnSendName;
    private JPanel editClientsPane;
    private JTextField tfSurnameEnter;

    public EditClientsForm (JFrame parent, Socket sock) {
        super(parent);
        setTitle("Edit Clients");
        setContentPane(editClientsPane);
        setMinimumSize(new Dimension(1000, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

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

        btnSendName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillJTextAreas();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateClient();
            }
        });

        setVisible(true);
    }

    private void updateClient(){
        String name = enterNameField.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";
        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();

            String sql = "UPDATE clients SET name = ?, surname = ?, age =?, post =? WHERE name = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, tfName.getText());
            preparedStatement.setString(2, tfSurname.getText());
            preparedStatement.setString(3, tfAge.getText());
            preparedStatement.setString(4, tfPost.getText());
            preparedStatement.setString(5, name);

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
        String name = enterNameField.getText();
        String surname = tfSurnameEnter.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM clients WHERE name = ? AND surname = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tfName.setText(resultSet.getString("name"));
                tfSurname.setText(resultSet.getString("surname"));
                tfAge.setText(resultSet.getString("age"));
                tfPost.setText(resultSet.getString("post"));
            }

            stmt.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
