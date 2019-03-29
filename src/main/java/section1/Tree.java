package section1;

import java.util.ArrayList;
import java.util.List;

public class Tree<T>
{
    public Node<T> root;
    public Tree(T rootData)
    {
        root = new Node<T>();
        root.data = rootData;
        root.children = new ArrayList<Node<T>>();
    }
}

class Node<T>
{
    public T data;
    public Node<T> parent;
    public List<Node<T>> children;
}

class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;
    public TreeNode(int x) {
        val = x;
    }
}