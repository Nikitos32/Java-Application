package org.example.Server;

import org.example.Award;
import org.example.Client;
import org.example.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Server {
    public static int amountClient = 0;

    public static void main(String[] args) throws IOException {

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost:3306/";
        final String DB_URL = "jdbc:mysql://localhost:3306/users_database";
        final String USERNAME = "root";
        final String PASSWORD = "Dimach321";

        ServerSocket sock = new ServerSocket(1024);
        Socket socket = sock.accept();
        amountClient++;
        while (amountClient != 0) {
            System.out.println("User " + amountClient + " connected");
            while (true) {
                InputStreamReader in = new InputStreamReader(socket.getInputStream());
                BufferedReader bf = new BufferedReader(in);
                String str = bf.readLine();
                System.out.println("User " + amountClient + " : " + str);
                if (Objects.equals(str, "EXIT")) {
                    amountClient--;
                    break;
                } else if (Objects.equals(str, "SHOW_USERS")) {
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    try {
                        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT * FROM users");
                        List<User> userList = new ArrayList<>();
                        while (resultSet.next()) {
                            User user = new User();
                            user.name = resultSet.getString("name");
                            user.email = resultSet.getString("email");
                            user.phone = resultSet.getString("phone");
                            user.address = resultSet.getString("address");
                            user.password = resultSet.getString("password");
                            userList.add(user);
                        }
                        for (int i = 0; i < userList.size(); i++) {
                            pr.println(userList.get(i).name + " | " + userList.get(i).email + " | " + userList.get(i).phone + " | " + userList.get(i).address + " | " + userList.get(i).password);
                        }
                        pr.println("END");
                        pr.flush();
                        stmt.close();
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Objects.equals(str, "SHOW_CLIENTS")) {
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    try {
                        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT * FROM clients");
                        List<Client> clientList = new ArrayList<>();
                        while (resultSet.next()) {
                            Client client = new Client();
                            client.name = resultSet.getString("name");
                            client.surname = resultSet.getString("surname");
                            client.age = resultSet.getString("age");
                            client.post = resultSet.getString("post");
                            clientList.add(client);
                        }
                        for (int i = 0; i < clientList.size(); i++) {
                            pr.println(clientList.get(i).name + " | " + clientList.get(i).surname + " | " + clientList.get(i).age + " | " + clientList.get(i).post);
                        }
                        pr.println("END");
                        pr.flush();
                        stmt.close();
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Objects.equals(str, "SORT_DESC")) {
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    try {
                        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT * FROM users ORDER BY name DESC");
                        List<User> userList = new ArrayList<>();
                        while (resultSet.next()) {
                            User user = new User();
                            user.name = resultSet.getString("name");
                            user.email = resultSet.getString("email");
                            user.phone = resultSet.getString("phone");
                            user.address = resultSet.getString("address");
                            user.password = resultSet.getString("password");
                            userList.add(user);
                        }
                        for (int i = 0; i < userList.size(); i++) {
                            pr.println(userList.get(i).name + " | " + userList.get(i).email + " | " + userList.get(i).phone + " | " + userList.get(i).address + " | " + userList.get(i).password);
                        }
                        pr.println("END");
                        pr.flush();
                        stmt.close();
                        connection.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Objects.equals(str, "SORT_ASC")) {
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    try {
                        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT * FROM users ORDER BY name ASC");
                        List<User> userList = new ArrayList<>();
                        while (resultSet.next()) {
                            User user = new User();
                            user.name = resultSet.getString("name");
                            user.email = resultSet.getString("email");
                            user.phone = resultSet.getString("phone");
                            user.address = resultSet.getString("address");
                            user.password = resultSet.getString("password");
                            userList.add(user);
                        }
                        for (int i = 0; i < userList.size(); i++) {
                            pr.println(userList.get(i).name + " | " + userList.get(i).email + " | " + userList.get(i).phone + " | " + userList.get(i).address + " | " + userList.get(i).password);
                        }
                        pr.println("END");
                        pr.flush();
                        stmt.close();
                        connection.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }  else if (Objects.equals(str, "FILTER")) {
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    try {
                        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT * FROM users");
                        List<User> userList = new ArrayList<>();
                        while (resultSet.next()) {
                            User user = new User();
                            user.name = resultSet.getString("name");
                            user.email = resultSet.getString("email");
                            user.phone = resultSet.getString("phone");
                            user.address = resultSet.getString("address");
                            user.password = resultSet.getString("password");
                            userList.add(user);
                        }
                        for (int i = 0; i < userList.size(); i++) {
                            pr.println(userList.get(i).name + " | " + userList.get(i).email + " | " + userList.get(i).phone + " | " + userList.get(i).address + " | " + userList.get(i).password);
                        }
                        pr.println("END");
                        pr.flush();
                        stmt.close();
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Objects.equals(str, "SHOW_AWARDS")) {
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    try {
                        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT * FROM awards");
                        List<Award> awardList = new ArrayList<>();
                        while (resultSet.next()) {
                            Award award = new Award();
                            award.id = resultSet.getString("id");
                            award.nameUser = resultSet.getString("name_user");
                            award.idClient = resultSet.getString("id_client");
                            award.addingDate = resultSet.getString("adding_date");
                            award.amount = resultSet.getString("amount");
                            awardList.add(award);
                        }
                        for (int i = 0; i < awardList.size(); i++) {
                            pr.println(awardList.get(i).id + " | " + awardList.get(i).nameUser + " | " + awardList.get(i).idClient + " | " + awardList.get(i).addingDate + " | " + awardList.get(i).amount);
                        }
                        pr.println("END");
                        pr.flush();
                        stmt.close();
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (Objects.equals(str, "SHOW_CLIENTS_WITH_ID")) {
                    PrintWriter pr = new PrintWriter(socket.getOutputStream());
                    try {
                        Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                        Statement stmt = connection.createStatement();
                        ResultSet resultSet = stmt.executeQuery("SELECT * FROM clients");
                        List<Client> clientList = new ArrayList<>();
                        while (resultSet.next()) {
                            Client client = new Client();
                            client.id = resultSet.getString("id");
                            client.name = resultSet.getString("name");
                            client.surname = resultSet.getString("surname");
                            client.age = resultSet.getString("age");
                            client.post = resultSet.getString("post");
                            clientList.add(client);
                        }
                        for (int i = 0; i < clientList.size(); i++) {
                            pr.println(clientList.get(i).id + " | " + clientList.get(i).name + " | " + clientList.get(i).surname + " | " + clientList.get(i).age + " | " + clientList.get(i).post);
                        }
                        pr.println("END");
                        pr.flush();
                        stmt.close();
                        connection.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        sock.close();
    }
}