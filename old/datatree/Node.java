package datatree;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nicholas on 01/03/15.
 */
public class Node {

    /**
    id : represents the a store on the board
        Range : between 1 and 6
                exception root.id() = 0
    */
    private int id;

    /**
    data : represents a dictionary of data that the
           node holds. (eg. data['prob_of_win'] = 97%)
        *leaves room for extra data elements without
         the need for expansion of the base data structure.
     */

    private Map<String, String> data;

    /**
    children : represents the decision branches.
        Size : can vary between 1 and 6.
               dependent upon game state.
     */
    private Map<Integer, Node> children;

    /**
     * Empty Node Constructor:
     *      sets the id of the node and creates empty
     *      HashMaps for the remaining attributes.
     * @param id An integer the represents the nodes id.
     */
    public Node(int id) {
        this.id = id;
        this.data = new HashMap<String, String>();
        this.children = new HashMap<Integer, Node>();
    }

    /**
     * Node Constructor:
     *      sets the id, data and size of children to the
     *      number of children the Node has.
     * @param id    An integer representing the id of the store
     * @param data  An dictionary of data elements
     */
    public Node(int id, Map<String, String> data) {
        this.id = id;
        this.data = data;
        this.children = new HashMap<Integer, Node>();
    }

    /**
     * getId():
     *      gets the id of the node
     * @return An integer
     */
    public int getId() {
        return id;
    }

    /**
     * getData():
     *      gets the data dictionary of the Node.
     * @return A HashMap of type (String, String)
     */
    public Map<String, String> getData() {
        return data;
    }

    /**
     * setChild(Node node):
     *      sets a child branch for the node.
     * @param node An object of type Node
     */
    public void setChild(Node node) {
        children.put(node.getId(), node);
    }

    /**
     * getChild(int id):
     *      gets the child node from the Node's map.
     * @param id An integer representing the Nodes id
     * @return A Node
     */
    public Node getChild(int id) {
        return children.get(id);
    }

    public int getSizeChildren() {
        return children.size();
    }
}
