package section1;

import java.io.PipedWriter;

public class PipedWriterThread implements Runnable {
    private PipedWriter pw;
    private TreeNode tree;
    private int XOR_KEY;

    public PipedWriterThread(TreeNode tree, int key, PipedWriter pw) {
        this.tree = tree;
        this.XOR_KEY = key;
        this.pw = pw;
    }

    @Override
    public void run() {
        try {
            TreeUtil.treeTraversal(tree, TreeUtil.TraversalMode.PRE_ORDER, XOR_KEY, pw);
            // For simplicity, write 1 as a delimiter
            pw.write(1 ^ XOR_KEY);
            pw.flush();
            Thread.sleep(300);

            TreeUtil.treeTraversal(tree, TreeUtil.TraversalMode.IN_ORDER, XOR_KEY, pw);
            // For simplicity, write 0 as a terminator
            pw.write(0 ^ XOR_KEY);
            pw.flush();

            System.out.println("Done writing");

            Thread.sleep(5000);
        } catch (Exception e) {
            System.out.println(" PipeThread Exception: " + e);
        }
    }
}
