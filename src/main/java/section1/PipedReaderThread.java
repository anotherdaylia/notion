package section1;

import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;

public class PipedReaderThread implements Runnable {
    PipedReader pr;
    private int XOR_KEY;

    public PipedReaderThread(int key, PipedReader pr) {
        this.XOR_KEY = key;
        this.pr = pr;
    }

    public void run() {
        List<Integer> preOrder = new ArrayList<>();
        List<Integer> inOrder = new ArrayList<>();
        List<Integer> currentOrder = preOrder;

        try {
            // continuously read data from stream and print it in console
            while (true) {
                int cipherText = pr.read(); // read a int
                int clearText = cipherText ^ XOR_KEY;

                if (clearText == 0) {
                    System.out.println(" <-- In Order Traversal");
                    System.out.println("Got terminate signal");
                    TreeNode root = TreeUtil.buildTreeFromPreorder(
                            preOrder.stream().mapToInt(Integer::intValue).toArray(),
                            inOrder.stream().mapToInt(Integer::intValue).toArray()
                    );
                    System.out.println("\nOriginal Tree");
                    TreeUtil.visualize(root);
                } else if (clearText == 1) {
                    System.out.println(" <-- Pre Order Traversal");
                    System.out.println("Got demiliter signal");
                    currentOrder = inOrder;
                } else {
                    System.out.print(clearText + " ");
                    currentOrder.add(clearText);
                }
            }
        } catch (Exception e) {
            System.out.println(" PipeThread Exception: " + e);
        }
    }
}
