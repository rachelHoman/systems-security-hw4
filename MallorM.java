package main;
import java.io.*;
import java.net.*;
import java.util.*;

public class MallorM {

    private List<String> receivedMessages; // Store received messages from Alice
    private boolean mac;
    private boolean enc;

    public MallorM(int malloryPort, String bobHostname, int bobPort, String config) {
        // Initialize the list of received messages
        receivedMessages = new ArrayList<>();

        // Apply configuration
        applyConfiguration(config);

        try {
            // Start Mallory's server
            ServerSocket serverSocket = new ServerSocket(malloryPort);

            // Accept connection from Alice
            Socket aliceSocket = serverSocket.accept();
            DataInputStream fromAlice = new DataInputStream(aliceSocket.getInputStream());
            PrintWriter toAlice = new PrintWriter(aliceSocket.getOutputStream(), true);

            // Connect to Bob
            Socket bobSocket = new Socket(bobHostname, bobPort);
            DataOutputStream toBob = new DataOutputStream(bobSocket.getOutputStream());
            BufferedReader fromBob = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));

            System.out.println("Connected to Alice and Bob");

            // Intercept messages from Alice and forward them to Bob
            BufferedReader reader = new BufferedReader(new InputStreamReader(fromAlice));
            PrintWriter writer = new PrintWriter(toBob, true);

            Scanner scanner = new Scanner(System.in);
            boolean finished = false;
            while (!finished) {
                try {
                    // replay message whenever
                    System.out.println("Choose a message to replay:");
                    System.out.println(0 + ". " + "[skip replay]");
                    for (int i = 0; i < receivedMessages.size(); i++) {
                        System.out.println((i + 1) + ". " + receivedMessages.get(i));
                    }
                    System.out.print("Enter message number to replay: ");
                    if (scanner.hasNextInt()) {
                        int messageIndex = scanner.nextInt();
                        if (messageIndex > 0 && messageIndex <= receivedMessages.size()) {
                            // Get the message to replay
                            String replayMessage = receivedMessages.get(messageIndex - 1);
                            // Forward the replayed message to Bob
                            toBob.writeUTF(replayMessage);
                            toBob.flush();
                            System.out.println("Replayed message sent to Bob: " + replayMessage);
                        } 
                        else if (messageIndex == 0) {
                            System.out.println("Skipping replay.");
                        }
                        else {
                            System.out.println("Invalid message number.");
                        }
                    }

                    String message = fromAlice.readUTF();
                    System.out.println("Received from Alice: " + message);
                    receivedMessages.add(message); // Store the received message

                    System.out.println("1. Forward as is");
                    System.out.println("2. Modify message");
                    System.out.println("3. Drop message");
                    System.out.println("4. Replay message");
                    System.out.print("Choose an action: ");
                    int choice = scanner.nextInt();

                    if (message.equals("done")) {
                        finished = true;
                        aliceSocket.close();
                        bobSocket.close();
                        serverSocket.close();
                        scanner.close();
                    }

                    switch (choice) {
                        default:
                            // Forward message to Bob
                            toBob.writeUTF(message);
                            toBob.flush();
                            System.out.println("Forwarded to Bob");
                            break;
                        case 2:
                            System.out.print("Enter modified message: ");
                            Scanner modifiedScanner = new Scanner(System.in);
                            String modifiedMessage = modifiedScanner.nextLine(); // Read the modified message input

                            // Print the modified message for debugging
                            System.out.println("Modified message: " + modifiedMessage);

                            // Send the modified message to Bob
                            toBob.writeUTF(modifiedMessage);
                            toBob.flush();
                            System.out.println("Modified message sent to Bob");

                            // Close the scanner after sending the modified message
                            modifiedScanner.close();
                            break;
                        case 3:
                            // Drop message
                            System.out.println("Message dropped");
                            break;
                        case 4:
                            System.out.println("Choose a message to replay:");
                            for (int i = 0; i < receivedMessages.size(); i++) {
                                System.out.println((i + 1) + ". " + receivedMessages.get(i));
                            }
                            System.out.print("Enter message number to replay: ");
                            int messageIndex2 = scanner.nextInt();
                            if (messageIndex2 > 0 && messageIndex2 <= receivedMessages.size()) {
                                // Get the message to replay
                                String replayMessage = receivedMessages.get(messageIndex2 - 1);
                                // Forward the replayed message to Bob
                                toBob.writeUTF(replayMessage);
                                toBob.flush();
                                System.out.println("Replayed message sent to Bob: " + replayMessage);
                            } else {
                                System.out.println("Invalid message number.");
                            }
                            break;
                    }
                    
                } catch (IOException ioe) {
                    finished = true; // Disconnect if there is an error reading the input
                }
            }

            // Close connections
            aliceSocket.close();
            bobSocket.close();
            serverSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyConfiguration(String config) {
        // Apply configuration
        if (config.compareTo("noCrypto") == 0) {
            // Implement your logic for handling messages without cryptography
            mac = false;
            enc = false;
        } else if (config.compareTo("enc") == 0) {
            // Implement your logic for handling messages with symmetric encryption only
            mac = false;
            enc = true;
        } else if (config.compareTo("mac") == 0) {
            // Implement your logic for handling messages with MACs only
            mac = true;
            enc = false;
        } else if (config.compareTo("EncThenMac") == 0) {
            // Implement your logic for handling messages with Enc-then-MAC
            mac = true;
            enc = true;
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Mallory <malloryPort> <bobHostname> <bobPort> <config>");
            return;
        }

        int malloryPort = Integer.parseInt(args[0]);
        String bobHostname = args[1];
        int bobPort = Integer.parseInt(args[2]);
        String config = args[3];

        MallorM mallory = new MallorM(malloryPort, bobHostname, bobPort, config);
    }
}