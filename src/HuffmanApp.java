import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HuffmanApp {

    public static void main(String[] args) {
        // Set up the GUI
        JFrame frame = new JFrame("Huffman Coding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLayout(new BorderLayout());

        // Input area
        JTextArea inputArea = new JTextArea(" ");
        JScrollPane inputScroll = new JScrollPane(inputArea);
        inputScroll.setPreferredSize(new Dimension(780, 100));

        // Output area
        JTextArea outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputArea);
        outputScroll.setPreferredSize(new Dimension(780, 100));

        // Encode and Decode Buttons
        JButton encodeButton = new JButton("Encode");
        JButton decodeButton = new JButton("Decode");

        // Panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(encodeButton);
        buttonPanel.add(decodeButton);

        // Add components to the frame
        frame.add(inputScroll, BorderLayout.NORTH);
        frame.add(buttonPanel, BorderLayout.CENTER);
        frame.add(outputScroll, BorderLayout.SOUTH);

        // Loads the Huffman Tree from Frequency Table
        List<TreeNode> nodes = createInitialNodes("LetterCount.txt");
        if (nodes == null || nodes.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Failed to load frequency table or table is empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Build the Huffman Tree from nodes
        HuffmanTreeBuilder builder = new HuffmanTreeBuilder();
        TreeNode root = builder.buildHuffmanTree(nodes);
        List<CharacterPair> codes = builder.generateCodes(root);

        // Encode button action
        encodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String inputText = inputArea.getText().toUpperCase();
                StringBuilder encodedMessage = new StringBuilder();

                // Encode the input text by looping through characters
                for (char c : inputText.toCharArray()) {
                    boolean found = false;
                    for (CharacterPair pair : codes) {
                        if (pair.character == c) {
                            encodedMessage.append(pair.code);
                            found = true;
                            break;
                        }
                    }
                }

                // Calculate compression ratio
                int originalSize = inputText.length() * 7; // Original size in bits (7 bits per character)
                int compressedSize = encodedMessage.length(); // Compressed size in bits
                double compressionRatio = (double) originalSize / compressedSize;

                // Display the encoded message and compression ratio
                outputArea.setText("Encoded Message: " + encodedMessage.toString() +
                        "\nOriginal Size: " + originalSize + " bits" +
                        "\nCompressed Size: " + compressedSize + " bits" +
                        "\nCompression Ratio: " + String.format("%.2f:1", compressionRatio));
            }
        });

        // Decode button action
        decodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String encodedText = inputArea.getText().trim();
                String decodedMessage = decodeMessage(root, encodedText);
                if (decodedMessage.isEmpty()) {
                    outputArea.setText("Error: Invalid binary string or decoding failed.");
                } else {
                    outputArea.setText("Decoded Message: " + decodedMessage);
                }
            }
        });

        // Show the frame
        frame.setVisible(true);
    }

    // Read frequency data from the file and create initial nodes
    private static List<TreeNode> createInitialNodes(String filename) {
        List<TreeNode> nodes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t"); // Split by tab
                if (parts.length == 2) {
                    char character = parts[0].charAt(0); // Character
                    int frequency = Integer.parseInt(parts[1]); // Frequency
                    nodes.add(new TreeNode(character, frequency)); // Adds tree node to list
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the frequency file: " + e.getMessage());
        }
        return nodes;
    }

    // Decode the binary string using the Huffman tree
    private static String decodeMessage(TreeNode root, String binaryString) {
        StringBuilder decodedMessage = new StringBuilder();
        TreeNode currentNode = root;

        // traverse the tree based on binary digits
        for (char bit : binaryString.toCharArray()) {
            currentNode = (bit == '0') ? currentNode.left : currentNode.right;

            // If leaf node, append the character to decoded message
            if (currentNode.left == null && currentNode.right == null) {
                decodedMessage.append(currentNode.character);
                currentNode = root; // Goes back to root to continue looking for next character
            }
        }

        return decodedMessage.toString();
    }
}
