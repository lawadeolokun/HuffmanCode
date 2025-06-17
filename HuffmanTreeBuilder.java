import java.util.ArrayList;
import java.util.List;

public class HuffmanTreeBuilder {

    // Build the Huffman tree from a list of TreeNodes
    // Each node represents a character and its frequency
    public TreeNode buildHuffmanTree(List<TreeNode> nodes) {
        while (nodes.size() > 1) {
            // Sort nodes by frequency (ascending order)
            nodes.sort((a, b) -> a.frequency - b.frequency);

            // Remove the two smallest nodes from left and right child
            TreeNode left = nodes.remove(0);
            TreeNode right = nodes.remove(0);

            // Create a parent node with combined frequency
            TreeNode parent = new TreeNode(left.frequency + right.frequency, left, right);

            // Add the parent node back into the list
            nodes.add(parent);
        }

        // Return the root of the tree
        return nodes.get(0);
    }

    // Generate Huffman codes for each character
    public List<CharacterPair> generateCodes(TreeNode root) {
        List<CharacterPair> codeList = new ArrayList<>();
        generateCodesHelper(root, "", codeList);
        return codeList;
    }

    // Helper method to traverse the tree and generate codes
    private void generateCodesHelper(TreeNode node, String code, List<CharacterPair> codeList) {
        if (node == null) return;

        // If it's a leaf node, save the code
        if (node.left == null && node.right == null) {
            codeList.add(new CharacterPair(node.character, code));
        }

        // Traverse left and right
        generateCodesHelper(node.left, code + "0", codeList);
        generateCodesHelper(node.right, code + "1", codeList);
    }
}
