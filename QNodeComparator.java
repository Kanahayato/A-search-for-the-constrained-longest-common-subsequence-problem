import java.util.Comparator;

/*
 * class QNodeComparator (implement Comparator<QNode>):
 *  - So sanh 2 QNode, duoc dung trong PriorityQueue
 * hoac cac phuong thuc khac co su dung Comparator
*/
public class QNodeComparator implements Comparator<QNode> {

    /* So sanh 2 QNode bang cach su dung phuong thuc compareTo cua node o1 so sanh voi o2
     * Dau vao: 2 QNode o1 va o2
     * Hoat dong: dua tren ket qua cua phuong thuc o1.compareTo(o2)
     * Dau ra: -1 neu o1 < o2
     *         0 neu o1 = o2
     *         1 neu o1 > o2
     * Time complexity: thuoc BigTheta(1)
     */
    @Override
    public int compare(QNode o1, QNode o2) {
        return o1.compareTo(o2);
    }
}