package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class AddClientForm extends JDialog {
    private JPanel addClientPanel;
    private JTextField tfName;
    private JTextField tfSurname;
    private JTextField tfAge;
    private JTextField tfPost;
    private JButton btnRegister;
    private JButton btnCancel;

    public AddClientForm(JFrame parent, Socket sock) {
        super(parent);
        setTitle("Add new client");
        setContentPane(addClientPanel);
        setMinimumSize(new Dimension(450, 474));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerClient();
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


    private void registerClient() {
        String name = tfName.getText();
        String surname = tfSurname.getText();
        String age = tfAge.getText();
        String post = tfPost.getText();

        if (name.isEmpty() || surname.isEmpty() || age.isEmpty() || post.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter all fields",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        client = addClientToDatabase(name, surname, age, post);
        if (client != null) {
            JOptionPane.showMessageDialog(this,
                    "Successful adding of: " + client.name,
                    "Try again",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Failed to adding new client",
                    "try again",
                    JOptionPane.ERROR_MESSAGE);
        }

    }

    public Client client;

    private Client addClientToDatabase(String name, String surname, String age, String post) {
        Client client = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";


        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO clients (name, surname, age, post)" +
                    "VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, surname);
            preparedStatement.setString(3, age);
            preparedStatement.setString(4, post);

            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                client = new Client();
                client.name = name;
                client.surname = surname;
                client.age = age;
                client.post = post;
            }

            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }
}
