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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class DeleteUsersForm extends JDialog {
    private JTextArea showTable;
    private JButton btnDelete;
    private JTextField tfEnterEmail;
    private JPanel deleteUsersPane;

    public DeleteUsersForm(JFrame parent, Socket sock){
        super(parent);
        setTitle("Delete users");
        setContentPane(deleteUsersPane);
        setMinimumSize(new Dimension(500, 500));
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

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Точно хотите удалить пользователя?",
                        "Подтверждение",
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) deleteUser();
            }
        });

        setVisible(true);
    }


    private void deleteUser(){
        String email = tfEnterEmail.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";

        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();

            String sql = "DELETE FROM users WHERE email = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, email);

            preparedStatement.execute();

            stmt.close();
            connection.close();

            JOptionPane.showMessageDialog(this,
                    "User successfully deleted",
                    "Ok",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
