package encryptdecrypt;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;

public class FileEncryptorGUI extends JFrame {
    private FileEncryptor encryptor;
    private JTextField inputFileField;
    private JTextField outputFileField;
    private JTextArea previewArea;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton browseInputButton;
    private JButton browseOutputButton;
    
    public FileEncryptorGUI() {
        encryptor = new FileEncryptor();
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("File Encryption/Decryption Tool");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        
        // Create components
        createHeaderPanel();
        createFileSelectionPanel();
        createPreviewPanel();
        createButtonPanel();
        
        pack();
        setMinimumSize(new Dimension(500, 400));
    }
    
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(70, 130, 180));
        
        JLabel titleLabel = new JLabel("File Encryption & Decryption Tool");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel subtitleLabel = new JLabel("Secure your files with encryption");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitleLabel.setForeground(Color.LIGHT_GRAY);
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        add(headerPanel, BorderLayout.NORTH);
    }
    
    private void createFileSelectionPanel() {
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new GridLayout(2, 2, 5, 5));
        filePanel.setBorder(new TitledBorder("File Selection"));
        
        // Input file selection
        JLabel inputLabel = new JLabel("Input File:");
        inputFileField = new JTextField();
        browseInputButton = new JButton("Browse");
        
        // Output file selection
        JLabel outputLabel = new JLabel("Output File:");
        outputFileField = new JTextField();
        browseOutputButton = new JButton("Browse");
        
        // Add components to panel
        filePanel.add(inputLabel);
        filePanel.add(createFileBrowsePanel(inputFileField, browseInputButton));
        filePanel.add(outputLabel);
        filePanel.add(createFileBrowsePanel(outputFileField, browseOutputButton));
        
        add(filePanel, BorderLayout.CENTER);
    }
    
    private JPanel createFileBrowsePanel(JTextField textField, JButton button) {
        JPanel panel = new JPanel(new BorderLayout(5, 0));
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.EAST);
        return panel;
    }
    
    private void createPreviewPanel() {
        JPanel previewPanel = new JPanel(new BorderLayout());
        previewPanel.setBorder(new TitledBorder("File Preview"));
        
        previewArea = new JTextArea(8, 40);
        previewArea.setEditable(false);
        previewArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(previewArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        previewPanel.add(scrollPane, BorderLayout.CENTER);
        
        add(previewPanel, BorderLayout.SOUTH);
    }
    
    private void createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        encryptButton = new JButton("Encrypt File");
        encryptButton.setBackground(new Color(34, 139, 34));
        encryptButton.setForeground(Color.WHITE);
        encryptButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        decryptButton = new JButton("Decrypt File");
        decryptButton.setBackground(new Color(220, 20, 60));
        decryptButton.setForeground(Color.WHITE);
        decryptButton.setFont(new Font("Arial", Font.BOLD, 14));
        
        JButton clearButton = new JButton("Clear");
        JButton exitButton = new JButton("Exit");
        
        // Add action listeners
        browseInputButton.addActionListener(e -> browseInputFile());
        browseOutputButton.addActionListener(e -> browseOutputFile());
        encryptButton.addActionListener(e -> encryptFile());
        decryptButton.addActionListener(e -> decryptFile());
        clearButton.addActionListener(e -> clearFields());
        exitButton.addActionListener(e -> dispose());
        
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void browseInputFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File to Encrypt/Decrypt");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            inputFileField.setText(selectedFile.getAbsolutePath());
            previewFileContent(selectedFile);
        }
    }
    
    private void browseOutputFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Output File Location");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            outputFileField.setText(selectedFile.getAbsolutePath());
        }
    }
    
    private void previewFileContent(File file) {
        try {
            String content = encryptor.readFile(file.getAbsolutePath());
            previewArea.setText(content);
            previewArea.setCaretPosition(0);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage(),
                    "File Read Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void encryptFile() {
        processFile(true);
    }
    
    private void decryptFile() {
        processFile(false);
    }
    
    private void processFile(boolean encrypt) {
        String inputPath = inputFileField.getText();
        String outputPath = outputFileField.getText();
        
        // Validation
        if (inputPath.isEmpty() || outputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select both input and output files!",
                    "Missing Files", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "Input file does not exist!",
                    "File Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            // Perform encryption/decryption
            encryptor.processFile(inputPath, outputPath, encrypt);
            
            String operation = encrypt ? "encrypted" : "decrypted";
            JOptionPane.showMessageDialog(this, 
                    "File successfully " + operation + "!\nOutput: " + outputPath,
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            
            // Preview the processed file
            File outputFile = new File(outputPath);
            if (outputFile.exists()) {
                previewFileContent(outputFile);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(),
                    "Processing Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearFields() {
        inputFileField.setText("");
        outputFileField.setText("");
        previewArea.setText("");
    }
}
