// package main;

// import java.io.*;
// import java.net.*;

// public class Mallory {
//     private boolean mac;
//     private boolean enc;

//     public Mallory(String alicePort, String bobHostname, String bobPort, String config) throws Exception {
//         // Apply configuration
//         if (config.compareTo("noCrypto") == 0) {
//             mac = false;
//             enc = false;
//         } else if (config.compareTo("enc") == 0) {
//             mac = false;
//             enc = true;
//         } else if (config.compareTo("mac") == 0) {
//             mac = true;
//             enc = false;
//         } else if (config.compareTo("EncThenMac") == 0) {
//             mac = true;
//             enc = true;
//         }

//     }

//     public static void main(String[] args) {

//         // if (args.length != 3) {
//         //     System.out.println("Usage: java Mallory <Alice's hostname> <Alice's port> <Bob's hostname>");
//         //     return;
//         // }

//         String aliceHostname = args[0];
//         int alicePort = Integer.parseInt(args[1]);
//         String bobHostname = args[2];
//         String cryptoType = args[3];

//         try (ServerSocket serverSocket = new ServerSocket(alicePort);
//              Socket aliceSocket = serverSocket.accept();
//              PrintWriter toBob = new PrintWriter(new Socket(bobHostname, 4444).getOutputStream(), true);
//              BufferedReader fromAlice = new BufferedReader(new InputStreamReader(aliceSocket.getInputStream()));
//              BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

//             String inputLine;
//             while ((inputLine = fromAlice.readLine()) != null) {
//                 System.out.println("Received from Alice: " + inputLine);
//                 System.out.println("1. Forward as is");
//                 System.out.println("2. Modify and forward");
//                 System.out.println("3. Drop message");
//                 System.out.print("Choose an action: ");
                
//                 int choice = Integer.parseInt(in.readLine());
//                 switch (choice) {
//                     case 1:
//                         toBob.println(inputLine);
//                         break;
//                     case 2:
//                         System.out.print("Enter modified message: ");
//                         String modifiedMessage = in.readLine();
//                         toBob.println(modifiedMessage);
//                         break;
//                     case 3:
//                         System.out.println("Message dropped.");
//                         break;
//                     default:
//                         System.out.println("Invalid choice. Message forwarded as is.");
//                         toBob.println(inputLine);
//                         break;
//                 }
//             }

//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }

//This kinda works

// package main;

// import java.io.*;
// import java.net.*;

// public class Mallory {
//     private boolean mac;
//     private boolean enc;

//     public Mallory(String alicePort, String bobHostname, String bobPort, String config) throws Exception {
//         // Apply configuration
//         if (config.compareTo("noCrypto") == 0) {
//             mac = false;
//             enc = false;
//         } else if (config.compareTo("enc") == 0) {
//             mac = false;
//             enc = true;
//         } else if (config.compareTo("mac") == 0) {
//             mac = true;
//             enc = false;
//         } else if (config.compareTo("EncThenMac") == 0) {
//             mac = true;
//             enc = true;
//         }

//         // Start Mallory's functionality here
//         try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(alicePort));
//              Socket aliceSocket = serverSocket.accept();
//              PrintWriter toBob = new PrintWriter(new Socket(bobHostname, Integer.parseInt(bobPort)).getOutputStream(), true);
//              BufferedReader fromAlice = new BufferedReader(new InputStreamReader(aliceSocket.getInputStream()));
//              BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

//             String inputLine;
//             while ((inputLine = fromAlice.readLine()) != null) {
//                 System.out.println("Received from Alice: " + inputLine);
//                 System.out.println("1. Forward as is");
//                 System.out.println("2. Modify and forward");
//                 System.out.println("3. Drop message");
//                 System.out.print("Choose an action: ");

//                 String choiceStr = in.readLine();
//                 if (choiceStr == null || choiceStr.isEmpty()) {
//                     continue; // Skip if the choice is empty
//                 }
                
//                 int choice = Integer.parseInt(in.readLine());
//                 switch (choice) {
//                     case 1:
//                         toBob.println(inputLine);
//                         toBob.flush();
//                         break;
//                     case 2:
//                         System.out.print("Enter modified message: ");
//                         String modifiedMessage = in.readLine();
//                         toBob.println(modifiedMessage);
//                         break;
//                     case 3:
//                         System.out.println("Message dropped.");
//                         break;
//                     default:
//                         System.out.println("Invalid choice. Message forwarded as is.");
//                         toBob.println(inputLine);
//                         break;
//                 }
//             }

//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         if (args.length != 4) {
//             System.out.println("Usage: java Mallory <Alice's port> <Bob's hostname> <Bob's port> <config>");
//             return;
//         }

//         String alicePort = args[0];
//         String bobHostname = args[1];
//         String bobPort = args[2];
//         String config = args[3];

//         try {
//             Mallory mallory = new Mallory(alicePort, bobHostname, bobPort, config);
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
// }


// package main;

// import java.io.*;
// import java.net.*;
// import java.security.*;
// import java.util.Scanner;

// public class Mallory {
    
//     private PublicKey alicePublicKey;
//     private PublicKey bobPublicKey;
//     private boolean mac;
//     private boolean enc;

//     public Mallory(String alicePort, String bobHostname, String bobPort, String config) {
//         //Apply configuration
//         if(config.compareTo("noCrypto") == 0){
//             mac = false;
//             enc = false;
//         } else if(config.compareTo("enc") == 0){
//             mac = false;
//             enc = true;
//         } else if(config.compareTo("mac") == 0){
//             mac = true;
//             enc = false;
//         } else if(config.compareTo("EncThenMac") == 0){
//             mac = true;
//             enc = true;
//         }

//         // Connect to Alice
//         try {
//             ServerSocket aliceServer = new ServerSocket(Integer.parseInt(alicePort));
//             Socket aliceSocket = aliceServer.accept();
//             ObjectInputStream alicePubIn = new ObjectInputStream(aliceSocket.getInputStream());
//             alicePublicKey = (PublicKey) alicePubIn.readObject();
//             alicePubIn.close();
//             aliceSocket.close();
//             aliceServer.close();
//         } catch (IOException | ClassNotFoundException e) {
//             e.printStackTrace();
//         }

//         // Connect to Bob
//         try {
//             Socket bobSocket = new Socket(bobHostname, Integer.parseInt(bobPort));
//             ObjectInputStream bobPubIn = new ObjectInputStream(bobSocket.getInputStream());
//             bobPublicKey = (PublicKey) bobPubIn.readObject();
//             bobPubIn.close();
//             bobSocket.close();
//         } catch (IOException | ClassNotFoundException e) {
//             e.printStackTrace();
//         }
//     }

//     public void intercept() {
//         try {
//             // Implement interception logic here
//             // This method will be similar to the previous implementation
//             // You can refer to the previous implementation and adapt it as needed
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         if (args.length != 4) {
//             System.out.println("Usage: java Mallory <alicePort> <bobHostname> <bobPort> <config>");
//             return;
//         }

//         String alicePort = args[0];
//         String bobHostname = args[1];
//         String bobPort = args[2];
//         String config = args[3];

//         Mallory mallory = new Mallory(alicePort, bobHostname, bobPort, config);
//         mallory.intercept();
//     }
// }

// package main;

// import java.io.*;
// import java.net.*;
// import java.util.Scanner;

// public class Mallory {

//     private boolean mac;
//     private boolean enc;

//     public Mallory(int malloryPort, String bobHostname, int bobPort, String config) {
//         // Apply configuration
//         if (config.compareTo("noCrypto") == 0) {
//             mac = false;
//             enc = false;
//         } else if (config.compareTo("enc") == 0) {
//             mac = false;
//             enc = true;
//         } else if (config.compareTo("mac") == 0) {
//             mac = true;
//             enc = false;
//         } else if (config.compareTo("EncThenMac") == 0) {
//             mac = true;
//             enc = true;
//         }

//         try {
//             // Start Mallory's server
//             ServerSocket serverSocket = new ServerSocket(malloryPort);
//             System.out.println("Mallory is listening for Alice on port " + malloryPort);

//             // Connect to Bob
//             Socket bobSocket = new Socket(bobHostname, bobPort);
//             DataOutputStream toBob = new DataOutputStream(bobSocket.getOutputStream());
//             BufferedReader fromBob = new BufferedReader(new InputStreamReader(bobSocket.getInputStream()));

//             System.out.println("Connected to Bob");

//             while (true) {
//                 // Accept connection from Alice
//                 Socket aliceSocket = serverSocket.accept();
//                 DataInputStream fromAlice = new DataInputStream(aliceSocket.getInputStream());
//                 PrintWriter toAlice = new PrintWriter(aliceSocket.getOutputStream(), true);

//                 // Intercept messages from Alice and forward them to Bob
//                 BufferedReader reader = new BufferedReader(new InputStreamReader(fromAlice));
//                 PrintWriter writer = new PrintWriter(toBob, true);

//                 Scanner scanner = new Scanner(System.in);
//                 String message;
//                 while ((message = reader.readLine()) != null) {
//                     System.out.println("Received from Alice: " + message);
//                     System.out.println("1. Forward as is");
//                     System.out.println("2. Modify message");
//                     System.out.println("3. Drop message");
//                     System.out.print("Choose an action: ");
//                     int choice = scanner.nextInt();

//                     switch (choice) {
//                         case 1:
//                             // Forward message to Bob
//                             writer.println(message);
//                             writer.flush();
//                             System.out.println("Forwarded to Bob");
//                             break;
//                         case 2:
//                             // Modify message (example: append ' (modified)' to the message)
//                             String modifiedMessage = message + " (modified)";
//                             writer.println(modifiedMessage);
//                             writer.flush();
//                             System.out.println("Modified message and forwarded to Bob");
//                             break;
//                         default:
//                             // Drop message
//                             System.out.println("Message dropped");
//                             break;
//                     }
//                 }

//                 // Close connections with Alice
//                 aliceSocket.close();
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         if (args.length != 4) {
//             System.out.println("Usage: java Mallory <malloryPort> <bobHostname> <bobPort> <config>");
//             return;
//         }

//         int malloryPort = Integer.parseInt(args[0]);
//         String bobHostname = args[1];
//         int bobPort = Integer.parseInt(args[2]);
//         String config = args[3];

//         Mallory mallory = new Mallory(malloryPort, bobHostname, bobPort, config);
//     }
// }

package main;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Mallory {

    public Mallory(int malloryPort, String bobHostname, int bobPort, String config) {
        // Apply configuration
        if (config.compareTo("noCrypto") == 0) {
            // Implement your logic for handling messages without cryptography
        } else if (config.compareTo("enc") == 0) {
            // Implement your logic for handling messages with symmetric encryption only
        } else if (config.compareTo("mac") == 0) {
            // Implement your logic for handling messages with MACs only
        } else if (config.compareTo("EncThenMac") == 0) {
            // Implement your logic for handling messages with Enc-then-MAC
        }

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
            String message;
            while ((message = reader.readLine()) != null) {
                System.out.println("Received from Alice: " + message);
                System.out.println("1. Forward as is");
                System.out.println("2. Modify message");
                System.out.println("3. Drop message");
                System.out.print("Choose an action: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    default:
                        // Forward message to Bob
                        writer.println(message);
                        writer.flush();
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
                }
            }

            // Close connections
            aliceSocket.close();
            bobSocket.close();
            serverSocket.close();
            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
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

        Mallory mallory = new Mallory(malloryPort, bobHostname, bobPort, config);
    }
}
