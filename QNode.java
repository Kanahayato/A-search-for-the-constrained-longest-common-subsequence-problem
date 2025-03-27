/*
 * Class QNode (implement Comparable): chua thong tin cua Node va chi phi de luu vao PriorityQueue
 * Thuoc tinh:
 *  - Node node: thong tin cua node dang xet
 *  - int ub: chi phi de di den Node, xac dinh do uu tien trong PriorityQueue
 * Phuong thuc:
 *  - Cac getter, setter, toString()
 *  - compareTo(QNode o): implement tu class Comparable, so sanh voi QNode khac dua vao ub va Node
*/
public class QNode implements Comparable<QNode> {
    private Node node;
    private int ub;

    public QNode(Node node, int ub) {
        this.node = node;
        this.ub = ub;
    }

    /*
     * Phuong thuc compareTo(QNode o)
     * Implement tu class Comparable
     * Dau vao: 1 QNode o
     * Tieu chi so sanh: so sanh 2 gia tri ub cua 2 QNode,
     *neu ub cua ca 2 bang nhau thi tiep tuc so sanh 2 Node
     * Dau ra: -1 neu ub < o.ub
     *          1 neu ub > o.ub
     *          nguoc lai: ket qua cua phep so sanh Node va o.Node
     * Time complexity: thuoc BigTheta(1)
    */
    @Override
    public int compareTo(QNode o) {
        // So sanh 2 gia tri ub
        if (ub > o.getUb()) {
            return -1;
        }
        if (ub < o.getUb()) {
            return 1;
        }
        // Neu 2 gia tri ub bang nhau thi so sanh 2 Node
        return node.compareTo(o.getNode());
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public int getUb() {
        return ub;
    }

    public void setUb(int ub) {
        this.ub = ub;
    }

    @Override
    public String toString() {
        return "QNode{"
        + "node=" + node + ", "
        + "ub=" + ub + "}";
    }
}