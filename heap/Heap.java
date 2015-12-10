package heap;
import java.util.ArrayList;
import java.util.Arrays;



public class Heap {
  // Root element.
  Node root;

  /**
   * Adds an element to the heap.
   * @param element Node to add to the heap.
   */
  public void add(Node element) {
    if (root == null) {
      root = element;
      root.level = 0;
    } else {
      Node el = getFirstFreeParent();
      el.addChild(element);
      heapify(element);
    }
  }


  /**
   * Gets the node in a tree by it's index.
   * @param index Index of the node to search.
   * @return The node with the index or null.
   */
  public Node findByIndex(int index) {
    if (root == null) {
      return null;
    }
    ArrayList<Node> queue = new ArrayList();
    queue.add(root);
    while (queue.size() > 0) {
      Node node = queue.remove(0);
      if (node.index == index) {
        return node;
      }
      queue.addAll(node.getChildren());
    }
    return null;
  }


  /**
   * Gets the max node (root) and removes it from the heap.
   * @return The node with max value or null.
   */
  public Node pullMax() {
    remove(root.index);
    return root;
  }


  /**
   * Removes elements by index.
   * @param index Index of the node to remove.
   */
  public void remove(int index) {
    Node elem = findByIndex(index);
    if (elem == null) return;
    Node last = getLastElement();
    if (elem == last) {
      elem.parent.removeChild(elem);
      return;
    }
    Node lastParent = last.parent;
    if (elem.parent != null) {
      Node p = elem.parent;
      p.removeChild(elem);
      p.addChild(last);
    } else {
      root = last;
    }
    for (int i = 0; i < elem.children.length; i++) {
      if (elem.children[i] != null) {
        elem.children[i].parent = last;
      }
    }
    Node[] children = elem.children;
    last.children = children;
    last.level = elem.level;
    lastParent.removeChild(last);

    ArrayList<Node> queue = new ArrayList();
    queue.add(last);
    while (queue.size() > 0) {
      Node node = queue.remove(0);
      Node maxChild = node.getMaxChild();
      if (maxChild != null) {
        if (maxChild.value > node.value) {
          int tmp = maxChild.parent.value;
          maxChild.parent.value = maxChild.value;
          maxChild.value = tmp;
          tmp = maxChild.parent.index;
          maxChild.parent.index = maxChild.index;
          maxChild.index = tmp;
          queue.add(maxChild);
        }
      }
    }
  }


  /**
   * Enforces the heap property starting from the element.
   * @param elem The node element to start with.
   */
  public void heapify(Node elem) {
    if (elem.parent != null && elem.parent.value < elem.value) {
      int tmp = elem.parent.value;
      elem.parent.value = elem.value;
      elem.value = tmp;
      tmp = elem.parent.index;
      elem.parent.index = elem.index;
      elem.index = tmp;
      heapify(elem.parent);
    }
  }


  /**
   * Prints the heap starting with the root value.
   * @return String to print.
   */
  @Override
  public String toString() {
    String result = "";
    int currentLevel = 0;
    ArrayList<Node> queue = new ArrayList();
    queue.add(root);
    while (queue.size() > 0) {
      Node node = queue.remove(0);
      if (currentLevel < node.level) {
        result += "\n";
      }
      currentLevel = node.level;
      result += node.toString() + "   ";
      queue.addAll(node.getChildren());
    }
    return result;
  }


  /**
   * Get the last element of the heap, last row, last column.
   * @return Node in the end or null.
   */
  protected Node getLastElement() {
    ArrayList<Node> queue = new ArrayList();
    if (root == null) return null;
    queue.add(root);
    Node element = null;
    while (queue.size() > 0) {
      element = queue.remove(0);
      queue.addAll(element.getChildren());
    }
    return element;
  }


  /**
   * Get the first element, which has less than 2 children.
   * @return Node in the end or null.
   */
  protected Node getFirstFreeParent() {
    ArrayList<Node> queue = new ArrayList();
    if (root == null) return null;
    queue.add(root);
    while (queue.size() > 0) {
      Node element = queue.remove(0);
      if (element.getChildrenNum() < 2) {
        return element;
      }
      queue.addAll(element.getChildren());
    }
    throw new RuntimeException("No childless element found");
  }


  /**
   * Sets the tree, removes an item and prints the results.
   * @param args Array of arguments for launching the program. Ignored.
   */
  public static void main(String[] args) {
    Heap tree = new Heap();
    for (int i = 0; i < 10; i++) {
      tree.add(new Node(i, i));
    }
    System.out.println(tree);
    tree.remove(3);
    System.out.println(tree);
  }
}
