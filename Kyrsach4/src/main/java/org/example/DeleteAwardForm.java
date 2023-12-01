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

public class DeleteAwardForm extends JDialog {
    private JTextArea showTable;
    private JButton btnDelete;
    private JTextField tfEnterID;
    private JPanel deleteAwardPane;

    public DeleteAwardForm(JFrame parent, Socket sock) {
        super(parent);
        setTitle("Delete awards");
        setContentPane(deleteAwardPane);
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

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(
                        null,
                        "Точно хотите удалить премию?",
                        "Подтверждение",
                        JOptionPane.YES_NO_OPTION);
                if (result == 0) deleteAward();
            }
        });

        setVisible(true);
    }

    private void deleteAward(){
        String id = tfEnterID.getText();
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";

        try{
            Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();

            String sql = "DELETE FROM awards WHERE id = ?";

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, id);

            preparedStatement.execute();

            stmt.close();
            connection.close();

            JOptionPane.showMessageDialog(this,
                    "Award successfully deleted",
                    "Ok",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
