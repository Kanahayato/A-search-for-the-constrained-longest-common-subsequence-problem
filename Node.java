import java.util.Vector;

/*
 * Class Node: chua trang thai hien tai cua ky tu con dang xet
 * Thuoc tinh:
 * - Node parent: Node cha cua Node hien tai
 * - Vector<Integer> pL: vi tri cua ky tu ma Node dang giu trong danh sach cac chuoi con
 * - int lV: chi phi tu Node dau tien toi Node hien tai
 * Phuong thuc:
 * - Cac getter, setter va toString()
 * - compareTo(Node o): implement tu class Comparable, so sanh voi Node khac dua vao lV
 */
public class Node implements Comparable<Node> {
    private Node parent;
    private Vector<Integer> pL;
    private int lV;

    public Node(int m) {
        parent = null;
        lV = 0;
        pL = new Vector<>(m);
        for (int i = 0; i < m; i++) {
            pL.add(1);
        }
    }

    public Node() {
        parent = null;
        lV = 0;
        pL = new Vector<>();
    }

    /*
     * Phuong thuc compareTo(Node o)
     * Implement tu class Comparable
     * Dau vao: 1 Node o
     * Tieu chi so sanh: so sanh 2 gia tri lV cua Node hien tai va Node o
     * Dau ra: -1 neu lV < o.lV
     *          0 neu lV = o.lV
     *          1 neu lV > o.lV
     * Time complexity: thuoc BigTheta(1)
    */
    @Override
    public int compareTo(Node o) {
        // So sanh 2 gia tri lV
        if (lV > o.getlV()) {
            return -1;
        }
        if (lV < o.getlV()) {
            return 1;
        }
        return 0;
    }

    public Node getParent() {
        return parent;
    }

    public Vector<Integer> getpL() {
        return pL;
    }

    public int getlV() {
        return lV;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setpL(Vector<Integer> pL) {
        this.pL.clear();
        this.pL.addAll(pL);
    }

    public void setpL(int i, int p) {
        this.pL.set(i, p);
    }

    public void setlV(int lV) {
        this.lV = lV;
    }

    @Override
    public String toString() {
        return "Node{"
        + "pL=" + pL + ", "
        + "lV=" + lV + "}";
    }
}