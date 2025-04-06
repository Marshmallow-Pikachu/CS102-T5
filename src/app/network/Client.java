package app.network;

import java.net.Socket;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;

public class Client {
    
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private boolean ready;
    private boolean gameEnd;

    public Client(Socket socket, String username) {

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.ready = false;
            this.gameEnd = false;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public void sendMessage(Scanner sc) {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            while (socket.isConnected() && !gameEnd) {
                String message = sc.nextLine();
                if (ready) {
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                    ready = false;
                }
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String message;

                while (socket.isConnected()) {
                    try {
                        message = bufferedReader.readLine();
                        if (message == null) {
                            System.out.println("Something went wrong when connecting to the server...");
                            gameEnd = true;
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            break;
                        }
                        if (message.equals("Thank you for playing!")) {
                            System.out.println(message);
                            gameEnd = true;
                            closeEverything(socket, bufferedReader, bufferedWriter);
                            break;
                        }

                        if (message.equals("Something went wrong during the game. Ask the host to try again...")) {
                            System.out.println(message);
                            gameEnd = true;
                            closeEverything(socket, bufferedReader, bufferedWriter);
                        }
                        if (message.equals("Your turn")) {
                            ready = true;
                            continue;
                        }
                        System.out.println(message);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    } catch (NullPointerException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    } catch (Exception e) {
                        System.out.println("Something went wrong when trying to shutdown client");
                    } 

                }

                System.out.println("enter anything to continue!");
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        // prevent NullPointerException
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            
        }
    }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println(
                "Usage: java Client <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int port = Integer.parseInt(args[1]);

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();

 
        Socket socket = new Socket(hostName, port);
        Client client = new Client(socket, username);

        client.listenForMessage();
        client.sendMessage(scanner);
    }
}
