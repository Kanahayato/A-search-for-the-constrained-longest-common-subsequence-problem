import java.util.Comparator;

/*
 * class NodeComparator (implement Comparator<Node>):
 *  - So sanh 2 Node, duoc dung trong cac phuong thuc co su dung Comparator
*/
public class NodeComparator implements Comparator<Node> {

    /* So sanh 2 Node bang cach su dung phuong thuc compareTo cua node o1 so sanh voi o2
     * Dau vao: 2 Node o1 va o2
     * Hoat dong: dua tren ket qua cua phuong thuc o1.compareTo(o2)
     * Dau ra: -1 neu o1 < o2
     *         0 neu o1 = o2
     *         1 neu o1 > o2
     * Time complexity: thuoc BigTheta(1)
     */
    @Override
    public int compare(Node o1, Node o2) {
        return o1.compareTo(o2);
    }
}