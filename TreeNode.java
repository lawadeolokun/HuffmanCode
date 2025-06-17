public class TreeNode {
    char character; // The character stored in the node
    int frequency;  // The frequency of the character
    TreeNode left;  // Left child
    TreeNode right; // Right child

    // Constructor for leaf nodes
    public TreeNode(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    // Constructor for internal nodes
    public TreeNode(int frequency, TreeNode left, TreeNode right) {
        this.character = '\0'; // Null character for internal nodes
        this.frequency = frequency; // child nodes
        this.left = left;
        this.right = right;
    }
}
