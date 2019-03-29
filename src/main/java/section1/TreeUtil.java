package section1;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedReader;
import java.io.PipedWriter;
import java.util.*;
import java.util.stream.Collectors;

public class TreeUtil {

    private static final int XOR_KEY = 17;

    private static final int BOUND = 100;

    public static void main(String[] args) {
        int [] randomSequence = randomValidIntegers(5);
        String randomString = Arrays.stream(randomSequence)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));

        System.out.println(randomString);

        TreeNode root = makeBinaryTree(randomString);
        visualize(root);
        stream(root);
    }

    private static int[] randomValidIntegers(int depth) {
        assert(depth >= 0);

        Random random = new Random();

        int length = (1 << depth) - 1;
        int[] index = new int[length];
        for (int i = 0; i < index.length; ++i) {
            index[i] = random.nextInt(BOUND) + 2;
        }

        // TODO: Randomly pick a few indices and set them the their children to null;
        List<Integer> candidates = null;

        return index;
    }

    public enum TraversalMode {
        LEVEL_ORDER, PRE_ORDER, IN_ORDER, POST_ORDER
    }

    public static void treeTraversal(TreeNode tree, TraversalMode mode, int XOR_KEY, PipedWriter pw) {
        assert(tree != null);

        switch(mode) {
            case PRE_ORDER:
                preOrder(tree, XOR_KEY, pw);
                break;
            case IN_ORDER:
                inOrder(tree, XOR_KEY, pw);
                break;
            case POST_ORDER:
            case LEVEL_ORDER:
                throw new UnsupportedOperationException(mode + " is not supported");
        }
    }

    private static void encryptAndWrite(int value, int XOR_KEY, PipedWriter pw) {
        try {
            pw.write(value ^ XOR_KEY);
            pw.flush();
            Thread.sleep(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void preOrder(TreeNode node, int XOR_KEY, PipedWriter pw) {

        if (node == null) {
            return;
        }

        encryptAndWrite(node.val, XOR_KEY, pw);

        if (node.left != null) {
            preOrder(node.left, XOR_KEY, pw);
        }

        if (node.right != null) {
            preOrder(node.right, XOR_KEY, pw);
        }
    }

    private static void inOrder(TreeNode node, int XOR_KEY, PipedWriter pw) {

        if (node == null) {
            return;
        }

        if (node.left != null) {
            inOrder(node.left, XOR_KEY, pw);
        }

        encryptAndWrite(node.val, XOR_KEY, pw);

        if (node.right != null) {
            inOrder(node.right, XOR_KEY, pw);
        }
    }

    public static TreeNode buildTreeFromPreorder(int[] preorder, int[] inorder) {
        if (inorder == null || preorder == null || inorder.length == 0 || preorder.length == 0) {
            return null;
        }

        int rootValue = preorder[0];

        TreeNode root = new TreeNode(rootValue);
        if (inorder.length == 1) {
            return root;
        }

        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i=0; i<inorder.length; ++i) {
            indexMap.put(inorder[i], i);
        }

        int rootIndex = indexMap.get(rootValue);
        // System.out.println("Root Index: " + rootIndex);

        int [] leftInorder  = Arrays.copyOfRange(inorder, 0, rootIndex);
        int [] rightInorder = Arrays.copyOfRange(inorder, rootIndex + 1, inorder.length);
        //printList(leftInorder);
        //printList(rightInorder);

        int [] leftPreorder  = Arrays.copyOfRange(preorder, 1, leftInorder.length+1);
        int [] rightPreorder = Arrays.copyOfRange(preorder, leftInorder.length+1, preorder.length);
        //printList(leftPreorder);
        //printList(rightPreorder);

        root.left  = buildTreeFromPreorder(leftPreorder, leftInorder);
        root.right = buildTreeFromPreorder(rightPreorder, rightInorder);

        return root;
    }

    // part 1: create a random tree of depth 5
    public static TreeNode makeBinaryTree(String serialization) {
        if ("".equals(serialization)) {
            return null;
        }

        String [] tokens = serialization.split(",");
        Queue<String> nodeValues = new LinkedList<>();
        for (String token : tokens) {
            // System.out.print(token + " ");
            nodeValues.offer(token);
        }
        // System.out.println();

        TreeNode root = new TreeNode(Integer.valueOf(nodeValues.poll()));

        List<TreeNode> currentLevel = new LinkedList<>();
        currentLevel.add(root);

        // while (!currentLevel.isEmpty()) <- this doesn't work with leafs
        while (!nodeValues.isEmpty()) {
            List<TreeNode> nextLevel = new LinkedList<>();
            for (TreeNode node : currentLevel) {
                node.left = toNode(nodeValues.poll());
                if (node.left != null) {
                    nextLevel.add(node.left);
                }

                node.right = toNode(nodeValues.poll());
                if (node.right != null) {
                    nextLevel.add(node.right);
                }
            }

            currentLevel.clear();
            currentLevel.addAll(nextLevel);
        }

        return root;
    }

    // part 2: visualize a tree
    public static void visualize(TreeNode root) {

        if (root == null) {
            System.out.println("Tree is empty");
        }

        List<TreeNode> currentLevel = new LinkedList<>();
        currentLevel.add(root);

        while (!currentLevel.isEmpty()) {
            List<Integer> values = new LinkedList<>();
            List<TreeNode> nextLevel = new LinkedList<>();

            for (TreeNode node : currentLevel) {
                values.add(node.val);
                if (node.left != null) {
                    nextLevel.add(node.left);
                }
                if (node.right != null) {
                    nextLevel.add(node.right);
                }
            }

            for (int value : values) {
                System.out.print(value + " ");
            }
            System.out.println();

            currentLevel.clear();
            currentLevel.addAll(nextLevel);
        }

    }

    // part 3 & 4: serialize the tree into stream / deserialize the stream into a tree
    public static void stream(TreeNode root) {
        try {
            // Create writer and reader instances
            PipedReader pr = new PipedReader();
            PipedWriter pw = new PipedWriter();

            // Connect the writer with reader
            pw.connect(pr);

            Thread thread1 = new Thread(new PipedReaderThread(XOR_KEY, pr));
            Thread thread2 = new Thread(new PipedWriterThread(root, XOR_KEY, pw));

            // start both threads
            thread1.start();
            thread2.start();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TreeNode toNode(String nodeValue) {
        // System.out.println("Node Value: " + nodeValue);
        if (nodeValue == null || "#".equals(nodeValue)) {
            return null;
        } else {
            return new TreeNode(Integer.valueOf(nodeValue));
        }
    }

    // part 5: unit tests
}
