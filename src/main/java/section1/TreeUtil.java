package section1;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class TreeUtil {

    private static final int BOUND = 100;

    public static void main(String[] args) {
        Tree<Integer> tree = createRandomTree(4);
        visualize(tree);
    }

    private static int[] randomValidIntegers(int depth) {
        assert(depth >= 0);

        Random random = new Random();

        int length = (1 << depth) - 1;
        int[] index = new int[length];
        for (int i = 0; i < index.length; ++i) {
            index[i] = random.nextInt(BOUND);
        }

        // TODO: Randomly pick a few indices and set them the their children to null;
        List<Integer> candidates = null;

        return index;
    }


    // part 1: create a random tree of depth 5
    public static Tree<Integer> createRandomTree(int depth) {

        //String serialization = randomStringForm(depth);
        int[] serialization = randomValidIntegers(depth);

        Queue<Integer> nodeValues = new LinkedList<>();
        for (int token : serialization) {
            // System.out.print(token + " ");
            nodeValues.offer(token);
        }
        System.out.println(nodeValues.size());

        Tree<Integer> tree = new Tree<>(Integer.valueOf(nodeValues.poll()));

        List<Node<Integer>> currentLevel = new LinkedList<>();
        currentLevel.add(tree.root);

        while (!nodeValues.isEmpty()) {
            List<Node<Integer>> nextLevel = new LinkedList<>();
            for (Node node : currentLevel) {

                Node<Integer> left = toNode(nodeValues.poll());
                node.children.add(left);
                if (left != null) {
                    nextLevel.add(left);
                }

                Node<Integer> right = toNode(nodeValues.poll());
                node.children.add(right);
                if (right != null) {
                    nextLevel.add(right);
                }
            }

            currentLevel.clear();
            currentLevel.addAll(nextLevel);
        }

        return tree;
    }

    private static Node<Integer> toNode(Integer value) {
        if (value != null) {
            Node<Integer> node = new Node<>();
            node.data = Integer.valueOf(value);
            node.children = new ArrayList<>();
            return node;
        } else {
            return null;
        }
    }

    // part 2: visualize a tree
    public static void visualize(Tree<Integer> tree) {
        if (tree == null) {
            System.out.println("Tree is empty");
        }

        BTreePrinter.printNode(tree.root);
    }
    // part 3: serialize the tree into stream
    public OutputStream serialize(Tree<Integer> tree) {
        // level order or
        // pre-order + delimiter + in-order
        return null;
    }

    // part 4: deserialize the stream into a tree
    public Tree<Integer> deserialize(InputStream is) {
        // level order or
        // pre-order + delimiter + in-order
        return null;
    }

    // part 5: unit tests
}

class BTreePrinter {

    public static <T extends Comparable<?>> void printNode(Node<T> root) {
        int maxLevel = BTreePrinter.maxLevel(root);

        printNodeInternal(Collections.singletonList(root), 1, maxLevel);
    }

    private static <T extends Comparable<?>> void printNodeInternal(List<Node<T>> nodes, int level, int maxLevel) {
        if (nodes.isEmpty() || BTreePrinter.isAllElementsNull(nodes))
            return;

        int floor = maxLevel - level;
        int endgeLines = (int) Math.pow(2, (Math.max(floor - 1, 0)));
        int firstSpaces = (int) Math.pow(2, (floor)) - 1;
        int betweenSpaces = (int) Math.pow(2, (floor + 1)) - 1;

        BTreePrinter.printWhitespaces(firstSpaces);

        List<Node<T>> newNodes = new ArrayList<Node<T>>();
        for (Node<T> node : nodes) {
            if (node != null) {
                System.out.print(node.data);
                newNodes.addAll(node.children);
            } else {
                newNodes.add(null);
                newNodes.add(null);
                System.out.print(" ");
            }

            BTreePrinter.printWhitespaces(betweenSpaces);
        }
        System.out.println("");

        for (int i = 1; i <= endgeLines; i++) {
            for (int j = 0; j < nodes.size(); j++) {
                BTreePrinter.printWhitespaces(firstSpaces - i);
                if (nodes.get(j) == null) {
                    BTreePrinter.printWhitespaces(endgeLines + endgeLines + i + 1);
                    continue;
                }

                if (nodes.get(j).children.size() != 0 && nodes.get(j).children.get(0) != null)
                    System.out.print("/");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(i + i - 1);

                if (nodes.get(j).children.size() != 0 && nodes.get(j).children.get(1) != null)
                    System.out.print("\\");
                else
                    BTreePrinter.printWhitespaces(1);

                BTreePrinter.printWhitespaces(endgeLines + endgeLines - i);
            }

            System.out.println("");
        }

        printNodeInternal(newNodes, level + 1, maxLevel);
    }

    private static void printWhitespaces(int count) {
        for (int i = 0; i < count; i++)
            System.out.print(" ");
    }

    private static <T extends Comparable<?>> int maxLevel(Node<T> node) {
        if (node == null)
            return 0;

        if (node.children.size() == 0)
            return 2;

        return Math.max(BTreePrinter.maxLevel(node.children.get(0)), BTreePrinter.maxLevel(node.children.get(1))) + 1;
    }

    private static <T> boolean isAllElementsNull(List<T> list) {
        for (Object object : list) {
            if (object != null)
                return false;
        }

        return true;
    }

}
