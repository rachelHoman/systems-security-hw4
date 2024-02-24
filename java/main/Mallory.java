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

package main;

import java.io.*;
import java.net.*;

public class Mallory {
    private boolean mac;
    private boolean enc;

    public Mallory(String alicePort, String bobHostname, String bobPort, String config) throws Exception {
        // Apply configuration
        if (config.compareTo("noCrypto") == 0) {
            mac = false;
            enc = false;
        } else if (config.compareTo("enc") == 0) {
            mac = false;
            enc = true;
        } else if (config.compareTo("mac") == 0) {
            mac = true;
            enc = false;
        } else if (config.compareTo("EncThenMac") == 0) {
            mac = true;
            enc = true;
        }

        // Start Mallory's functionality here...
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(alicePort));
             Socket aliceSocket = serverSocket.accept();
             PrintWriter toBob = new PrintWriter(new Socket(bobHostname, Integer.parseInt(bobPort)).getOutputStream(), true);
             BufferedReader fromAlice = new BufferedReader(new InputStreamReader(aliceSocket.getInputStream()));
             BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {

            String inputLine;
            while ((inputLine = fromAlice.readLine()) != null) {
                System.out.println("Received from Alice: " + inputLine);
                System.out.println("1. Forward as is");
                System.out.println("2. Modify and forward");
                System.out.println("3. Drop message");
                System.out.print("Choose an action: ");
                
                int choice = Integer.parseInt(in.readLine());
                switch (choice) {
                    case 1:
                        toBob.println(inputLine);
                        break;
                    case 2:
                        System.out.print("Enter modified message: ");
                        String modifiedMessage = in.readLine();
                        toBob.println(modifiedMessage);
                        break;
                    case 3:
                        System.out.println("Message dropped.");
                        break;
                    default:
                        System.out.println("Invalid choice. Message forwarded as is.");
                        toBob.println(inputLine);
                        break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java Mallory <Alice's port> <Bob's hostname> <Bob's port> <config>");
            return;
        }

        String alicePort = args[0];
        String bobHostname = args[1];
        String bobPort = args[2];
        String config = args[3];

        try {
            Mallory mallory = new Mallory(alicePort, bobHostname, bobPort, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
