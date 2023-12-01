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

public class CalculateAwardForm extends JDialog {
    private JPanel calculateAwardPane;
    private JTextArea showTable;
    private JButton btnCalculate;
    private JTextField tfEnterID;
    private JTextField tfCoef;
    private JTextField tfSalary;
    private JTextField tfHours;

    public CalculateAwardForm(JFrame parent, Socket sock) {
        super(parent);
        setTitle("Calculate awards");
        setContentPane(calculateAwardPane);
        setMinimumSize(new Dimension(500, 500));
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

        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAward();
                dispose();
            }
        });

        setVisible(true);
    }

    private void calculateAward() {
        String id = tfEnterID.getText();
        String hours = tfHours.getText();
        String coef = tfCoef.getText();
        String salary = tfSalary.getText();
        double amount;
        amount = Double.parseDouble(hours)*Double.parseDouble(coef)*0.01 + Double.parseDouble(salary)*0.01;

        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";


        try {
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();

            String sql = "UPDATE awards SET amount = ? WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, Double.toString(amount));
            preparedStatement.setString(2, id);

            preparedStatement.executeUpdate();

            stmt.close();
            connection.close();

            JOptionPane.showMessageDialog(this,
                    "Data successfully calculated",
                    "Ok",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
