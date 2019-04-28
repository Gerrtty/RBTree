import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

public class RedBlackTree<V extends Comparable<V>> {
    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private static int size = 0;
    private static int modCount = 0;
    private static Node root;

    public int size() {
        return size;
    }

    static class Node<V extends Comparable<V>> {
        private int index;
        private int num = 1;
        V value;
        Node<V> right, left, parent;
        boolean color = BLACK;

        public Node(V value, Node parent) {
            this.value = value;
            this.parent = parent;
            index = size;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        private static boolean colorOf(Node p) {
            return (p == null ? BLACK : p.color);
        }

        private static Node parentOf(Node p) {
            return (p == null ? null : p.parent);
        }

        private static void setColor(Node p, boolean c) {
            if (p != null)
                p.color = c;
        }

        private static Node leftOf(Node p) {
            return (p == null) ? null : p.left;
        }

        private static Node rightOf(Node p) {
            return (p == null) ? null : p.right;
        }

        private static void rotateLeft(Node p) {
            if (p != null) {
                Node r = p.right;
                p.right = r.left;
                if (r.left != null)
                    r.left.parent = p;
                r.parent = p.parent;
                if (p.parent == null)
                    root = r;
                else if (p.parent.left == p)
                    p.parent.left = r;
                else
                    p.parent.right = r;
                r.left = p;
                p.parent = r;
            }
        }

        private static void rotateRight(Node p) {
            if (p != null) {
                Node l = p.left;
                p.left = l.right;
                if (l.right != null) l.right.parent = p;
                l.parent = p.parent;
                if (p.parent == null)
                    root = l;
                else if (p.parent.right == p)
                    p.parent.right = l;
                else p.parent.left = l;
                l.right = p;
                p.parent = l;
            }
        }

        private static void fixAfterInsertion(Node x) {
            x.color = RED;
            while (x != null && x != root && x.parent.color == RED) {
                if (parentOf(x) == leftOf(parentOf(parentOf(x)))) {
                    Node y = rightOf(parentOf(parentOf(x)));
                    if (colorOf(y) == RED) {
                        setColor(parentOf(x), BLACK);
                        setColor(y, BLACK);
                        setColor(parentOf(parentOf(x)), RED);
                        x = parentOf(parentOf(x));
                    } else {
                        if (x == rightOf(parentOf(x))) {
                            x = parentOf(x);
                            rotateLeft(x);
                        }
                        setColor(parentOf(x), BLACK);
                        setColor(parentOf(parentOf(x)), RED);
                        rotateRight(parentOf(parentOf(x)));
                    }
                } else {
                    Node y = leftOf(parentOf(parentOf(x)));
                    if (colorOf(y) == RED) {
                        setColor(parentOf(x), BLACK);
                        setColor(y, BLACK);
                        setColor(parentOf(parentOf(x)), RED);
                        x = parentOf(parentOf(x));
                    } else {
                        if (x == leftOf(parentOf(x))) {
                            x = parentOf(x);
                            rotateRight(x);
                        }
                        setColor(parentOf(x), BLACK);
                        setColor(parentOf(parentOf(x)), RED);
                        rotateLeft(parentOf(parentOf(x)));
                    }
                }
            }
            root.color = BLACK;
        }

        private static void fixAfterDeletion(Node x) {
            while (x != root && colorOf(x) == BLACK) {
                if (x == leftOf(parentOf(x))) {
                    Node sib = rightOf(parentOf(x));

                    if (colorOf(sib) == RED) {
                        setColor(sib, BLACK);
                        setColor(parentOf(x), RED);
                        rotateLeft(parentOf(x));
                        sib = rightOf(parentOf(x));
                    }

                    if (colorOf(leftOf(sib)) == BLACK &&
                            colorOf(rightOf(sib)) == BLACK) {
                        setColor(sib, RED);
                        x = parentOf(x);
                    } else {
                        if (colorOf(rightOf(sib)) == BLACK) {
                            setColor(leftOf(sib), BLACK);
                            setColor(sib, RED);
                            rotateRight(sib);
                            sib = rightOf(parentOf(x));
                        }
                        setColor(sib, colorOf(parentOf(x)));
                        setColor(parentOf(x), BLACK);
                        setColor(rightOf(sib), BLACK);
                        rotateLeft(parentOf(x));
                        x = root;
                    }
                } else {
                    Node sib = leftOf(parentOf(x));

                    if (colorOf(sib) == RED) {
                        setColor(sib, BLACK);
                        setColor(parentOf(x), RED);
                        rotateRight(parentOf(x));
                        sib = leftOf(parentOf(x));
                    }

                    if (colorOf(rightOf(sib)) == BLACK &&
                            colorOf(leftOf(sib)) == BLACK) {
                        setColor(sib, RED);
                        x = parentOf(x);
                    } else {
                        if (colorOf(leftOf(sib)) == BLACK) {
                            setColor(rightOf(sib), BLACK);
                            setColor(sib, RED);
                            rotateLeft(sib);
                            sib = leftOf(parentOf(x));
                        }
                        setColor(sib, colorOf(parentOf(x)));
                        setColor(parentOf(x), BLACK);
                        setColor(leftOf(sib), BLACK);
                        rotateRight(parentOf(x));
                        x = root;
                    }
                }
            }
            setColor(x, BLACK);
        }

        private static Node successor(Node t) {
            if (t == null)
                return null;
            else if (t.right != null) {
                Node p = t.right;
                while (p.left != null)
                    p = p.left;
                return p;
            } else {
                Node p = t.parent;
                Node ch = t;
                while (p != null && ch == p.right) {
                    ch = p;
                    p = p.parent;
                }
                return p;
            }
        }

        private static void deleteNode(Node p) {
            modCount++;
            size--;
            if (p.left != null && p.right != null) {
                Node s = successor(p);
                p.value = s.value;
                p = s;
            }
            Node replacement = (p.left != null ? p.left : p.right);

            if (replacement != null) {
                replacement.parent = p.parent;
                if (p.parent == null)
                    root = replacement;
                else if (p == p.parent.left)
                    p.parent.left = replacement;
                else
                    p.parent.right = replacement;
                p.left = p.right = p.parent = null;

                if (p.color == BLACK)
                    fixAfterDeletion(replacement);
            } else if (p.parent == null) {
                root = null;
            } else {
                if (p.color == BLACK)
                    fixAfterDeletion(p);

                if (p.parent != null) {
                    if (p == p.parent.left)
                        p.parent.left = null;
                    else if (p == p.parent.right)
                        p.parent.right = null;
                    p.parent = null;
                }
            }
        }

        private Node<V> getNode(V value) {
            Node<V> p = root;
            while (p != null) {
                int cmp = value.compareTo(p.value);
                if (cmp < 0)
                    p = p.left;
                else if (cmp > 0)
                    p = p.right;
                else
                    return p;
            }
            return null;
        }
    }

    public V remove(V value) {
        Node<V> n = new Node<>(value, root);
        Node<V> p = n.getNode(value);
        if (p.num > 1) {
            p.num--;
            return null;
        }
        if (p == null)
            return null;
        V oldValue = p.value;
        Node.deleteNode(p);
        return oldValue;
    }


    public V put(V value) {
        Node<V> t = root;
        if (t == null) {
            root = new Node(value, null);
            size = 1;
            modCount++;
            return null;
        }
        int cmp;
        Node parent;
        do {
            parent = t;
            cmp = value.compareTo(t.value);
            if (cmp < 0)
                t = t.left;
            else if (cmp > 0)
                t = t.right;
            else {
                t.num++;
                return t.setValue(value);
            }
        } while (t != null);
        Node<V> e = new Node(value, parent);
        if (cmp < 0)
            parent.left = e;
        else
            parent.right = e;
        Node.fixAfterInsertion(e);
        size++;
        modCount++;
        return null;
    }


    private void print(Node root) {
        if (root == null) return;
        print(root.left);
        String str = root.color ? "Black" : "Red";
        for (int i = 0; i < root.num; i++) {
            System.out.println(root.value + " " + str);
        }
        print(root.right);
    }

    public void print() {
        print(root);
    }

    public void printTreeInFile(File f) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(f);
        printTreeInFile(root, f, printWriter);
        printWriter.close();
    }

    private void printTreeInFile(Node<V> root, File f, PrintWriter pw) throws FileNotFoundException {
        if (root == null) return;
        printTreeInFile(root.left, f, pw);
        for (int i = 0; i < root.num; i++) {
            pw.print(root.value + " ");
        }
        printTreeInFile(root.right, f, pw);
    }

    public void toArr(ArrayList<V> arr) {
        toArr(root, arr);
    }

    private void toArr(Node<V> root, ArrayList<V> al) {
        if (root == null) return;
        toArr(root.left, al);
        for (int i = 0; i < root.num; i++) {
            al.add(root.value);
        }
        toArr(root.right, al);
    }

    public boolean contains(V value) {
        Node<V> n = new Node<>(value, root);
        return n.getNode(value) != null;
    }


}
