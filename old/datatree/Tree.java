package datatree;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicholas on 01/03/15.
 */
public class Tree {
    /**
     * root : represents the current node of focus
     */
    private Node root;

    /**
     * Tree Constructor:
     *      constructs and tree with only a root node.
     */
    public Tree() {
        this.root = new Node(0);
    }

    /**
     * getRoot():
     *      gets the current Node in focus.
     * @return An object of type Node
     */
    public Node getRoot() {
        return root;
    }

    /**
     * setRoot():
     *      sets the root to the new Node in focus.
     * @param node An object of type  Node
     */
    public void setRoot(Node node) {
        root = node;
    }

    public static void main(String[] args) {
        // This data will need to be generated externally.
        Map<String, String> data = new HashMap<String, String>();

        // Create a new tree
        Tree tree = new Tree();
        tree.root.setChild(new Node(1, data));

        System.out.println(tree.root.getId());
        System.out.println(tree.root.getSizeChildren());

        for (int i = 1; i < tree.root.getSizeChildren(); i++) {
            System.out.println(tree.root.getChild(i).getId());
        }

        tree.setRoot(tree.getRoot().getChild(1));

        System.out.println(tree.root.getId());
        System.out.println(tree.root.getSizeChildren());

    }
}
