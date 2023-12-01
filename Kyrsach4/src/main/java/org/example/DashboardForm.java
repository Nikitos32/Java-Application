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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;


public class DashboardForm extends JFrame {
    private JPanel dashboardPanel;
    private JLabel lbAdmin;
    private JButton editUsersButton;
    private JButton btnRegister;
    private JButton btnShowAllUsers;
    private JButton deleteUsersButton;
    private JButton workWithClientsButton;
    private JTextArea showTable;
    private JButton btnSortDecs;
    private JButton btnSortAscend;
    private JButton btnFilter;
    private JTextField tfFilter;
    public Socket sock = new Socket("localhost", 1024);
    public String LOGIN_NAME = "";
    public DashboardForm() throws IOException {
        setTitle("Dashboard");
        setContentPane(dashboardPanel);
        setMinimumSize(new Dimension(500, 429));
        setSize(1200, 700);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        boolean hasRegisteredUsers = connectToDatabase();

        if (hasRegisteredUsers) {
            LoginForm loginForm = new LoginForm(this);
            User user = loginForm.user;

            if (user != null) {
                lbAdmin.setText("User: " + user.name);
                LOGIN_NAME = user.name;
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("EXIT");
                    pr.flush();
                    sock.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
            }
        } else {
            RegistrationForm registrationForm = new RegistrationForm(this);
            User user = registrationForm.user;

            if (user != null) {
                lbAdmin.setText("User: " + user.name);
                LOGIN_NAME = user.name;
                setLocationRelativeTo(null);
                setVisible(true);
            } else {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("EXIT");
                    pr.flush();
                    sock.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                dispose();
            }
        }
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("REGISTER_USER");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                RegistrationForm registrationForm = new RegistrationForm(DashboardForm.this);
                User user = registrationForm.user;

                if (user != null) {
                    JOptionPane.showMessageDialog(DashboardForm.this,
                            "New user: " + user.name,
                            "Successful Registration",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        btnShowAllUsers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });

        editUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("EDIT_USERS");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                EditUsersForm editUsersForm = new EditUsersForm(DashboardForm.this, sock);
            }
        });

        deleteUsersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("DELETE_USERS");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                DeleteUsersForm deleteUsersForm = new DeleteUsersForm(DashboardForm.this, sock);
            }
        });

        workWithClientsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("WORK_WITH_CLIENTS");
                    pr.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    ClientsDashboardForm clientsDashboardForm = new ClientsDashboardForm(sock);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        btnSortDecs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("SORT_DESC");
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

        btnSortAscend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("SORT_ASC");
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

        btnFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PrintWriter pr = new PrintWriter(sock.getOutputStream());
                    pr.println("FILTER");
                    pr.flush();
                    InputStreamReader in = new InputStreamReader(sock.getInputStream());
                    BufferedReader bf = new BufferedReader(in);
                    String str;
                    String strFilter = tfFilter.getText();
                    showTable.setText("");
                    while (!Objects.equals(str = bf.readLine(), "END")) {
                        if (str.contains(strFilter)) {
                            showTable.append(str + "\n");
                        }
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    private boolean connectToDatabase() {
        boolean hasRegisteredUsers = false;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3306/";
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";

        try {
            Connection connection = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS users_database");
            stmt.close();
            connection.close();

            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            stmt = connection.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "  id INT( 10 ) NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                    "  name VARCHAR(200) NOT NULL," +
                    "  email VARCHAR(200) NOT NULL UNIQUE," +
                    "  phone VARCHAR(20)," +
                    "  address VARCHAR(200)," +
                    "  password VARCHAR(200) NOT NULL" +
                    ")";
            stmt.executeUpdate(sql);

            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT COUNT(*) FROM users");

            if (resultSet.next()) {
                int numUsers = resultSet.getInt(1);
                if (numUsers > 0) {
                    hasRegisteredUsers = true;
                }
            }

            stmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hasRegisteredUsers;
    }

    public static void main(String[] args) throws IOException {
        DashboardForm myForm = new DashboardForm();
    }
}


