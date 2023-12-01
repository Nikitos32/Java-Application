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

public class EditAwardsForm extends JDialog{
    private JTextField tfName;
    private JTextField tfDate;
    private JTextField tfAmount;
    private JTextField tfId;
    private JButton btnUpdate;
    private JTextField enterIdField;
    private JTextArea showTable;
    private JButton btnSendName;
    private JPanel editAwardsPane;

    public EditAwardsForm (JFrame parent, Socket sock){
        super(parent);
        setTitle("Edit Awards");
        setContentPane(editAwardsPane);
        setMinimumSize(new Dimension(1000, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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

        btnSendName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fillJTextAreas();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAward();
            }
        });

        setVisible(true);
    }

    private void updateAward(){
        String id = enterIdField.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";
        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();

            String sql = "UPDATE awards SET name_user = ?, id_client = ?, amount = ?, adding_date = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, tfName.getText());
            preparedStatement.setString(2, tfId.getText());
            preparedStatement.setString(3, tfAmount.getText());
            preparedStatement.setString(4, tfDate.getText());
            preparedStatement.setString(5, id);

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
        String id = enterIdField.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";

        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();
            String sql = "SELECT * FROM awards WHERE id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                tfName.setText(resultSet.getString("name_user"));
                tfId.setText(resultSet.getString("id_client"));
                tfAmount.setText(resultSet.getString("amount"));
                tfDate.setText(resultSet.getString("adding_date"));
            }

            stmt.close();
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
