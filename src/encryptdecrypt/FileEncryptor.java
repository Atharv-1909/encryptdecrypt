package encryptdecrypt;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class FileEncryptor {
    private static final int SHIFT_KEY = 3; // Caesar cipher shift value
    
    // Encryption algorithm (Caesar cipher)
    public String encrypt(String text) {
        StringBuilder encrypted = new StringBuilder();
        
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isLowerCase(character) ? 'a' : 'A';
                char encryptedChar = (char) ((character - base + SHIFT_KEY) % 26 + base);
                encrypted.append(encryptedChar);
            } else if (Character.isDigit(character)) {
                char encryptedChar = (char) ((character - '0' + SHIFT_KEY) % 10 + '0');
                encrypted.append(encryptedChar);
            } else {
                encrypted.append(character);
            }
        }
        
        return encrypted.toString();
    }
    
    // Decryption algorithm
    public String decrypt(String text) {
        StringBuilder decrypted = new StringBuilder();
        
        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isLowerCase(character) ? 'a' : 'A';
                char decryptedChar = (char) ((character - base - SHIFT_KEY + 26) % 26 + base);
                decrypted.append(decryptedChar);
            } else if (Character.isDigit(character)) {
                char decryptedChar = (char) ((character - '0' - SHIFT_KEY + 10) % 10 + '0');
                decrypted.append(decryptedChar);
            } else {
                decrypted.append(character);
            }
        }
        
        return decrypted.toString();
    }
    
    // Read file content
    public String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
    
    // Write content to file
    public void writeFile(String filePath, String content) throws IOException {
        Files.write(Paths.get(filePath), content.getBytes());
    }
    
    // Process file (encrypt/decrypt)
    public void processFile(String inputPath, String outputPath, boolean encrypt) throws IOException {
        String content = readFile(inputPath);
        String processedContent;
        
        if (encrypt) {
            processedContent = encrypt(content);
            System.out.println("File encrypted successfully!");
        } else {
            processedContent = decrypt(content);
            System.out.println("File decrypted successfully!");
        }
        
        writeFile(outputPath, processedContent);
    }
    
    // Console interface
    public void startConsoleInterface() {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            try {
                System.out.println("\n=== File Encryption/Decryption ===");
                System.out.println("1. Encrypt File");
                System.out.println("2. Decrypt File");
                System.out.println("3. Launch GUI Interface");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                
                switch (choice) {
                    case 1:
                        processFileOperation(scanner, true);
                        break;
                    case 2:
                        processFileOperation(scanner, false);
                        break;
                    case 3:
                        launchGUI();
                        break;
                    case 4:
                        System.out.println("Goodbye!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option! Please try again.");
                }
                
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                scanner.nextLine(); // Clear invalid input
            }
        }
    }
    
    private void processFileOperation(Scanner scanner, boolean encrypt) {
        try {
            System.out.print("Enter input file path: ");
            String inputPath = scanner.nextLine();
            
            System.out.print("Enter output file path: ");
            String outputPath = scanner.nextLine();
            
            // Check if input file exists
            File inputFile = new File(inputPath);
            if (!inputFile.exists()) {
                System.out.println("Input file does not exist!");
                return;
            }
            
            processFile(inputPath, outputPath, encrypt);
            
        } catch (IOException e) {
            System.out.println("File operation failed: " + e.getMessage());
        }
    }
    
    private void launchGUI() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            FileEncryptorGUI gui = new FileEncryptorGUI();
            gui.setVisible(true);
        });
    }
    
    public static void main(String[] args) {
        FileEncryptor encryptor = new FileEncryptor();
        encryptor.startConsoleInterface();
    }
}
