import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.ListIterator;

public class AStar {
    private LCS_solution sol;
    private LCS inst;
    private Map<List<Integer>, List<Integer>> N;
    private PriorityQueue<QNode> Q;

    public AStar(LCS inst, LCS_solution sol) {
        this.inst = inst;
        this.sol = sol;
        N = new HashMap<>();
        Q = new PriorityQueue<>();
    }

    private void addNode(Node v) {
        Q.add(new QNode(v, ub(v)));
        List <Integer> pLs = new ArrayList<>();
        pLs.add(v.getlV());
        N.put(v.getpL(), pLs);
    }

    private int ub(Node v) {
        int ub_1 = ub1(v);
        int ub_2 = ub2(v);
        
        return Math.min(ub_1, ub_2);
    }

    private int ub1(Node v) {
        int ub_1 = 0;
        Vector<Integer> min_occurances_node = IntStream.range(0, inst.getSigma())
                .mapToObj(i -> inst.getS().get(0).size())
                .collect(Collectors.toCollection(Vector::new));
        
        for (int a = 0; a < inst.getSigma(); a++) {
            for (int i = 0; i < inst.getM(); i++) {
                if (v.getpL().get(i) - 1 < inst.getS().get(i).size()) { // not feasible letter
                    int ocurrance_pos_a_pL_i = inst.getOccurance_positions().get(i)
                                                    .get(a).get(v.getpL().get(i) - 1);
                    if (ocurrance_pos_a_pL_i < min_occurances_node.get(a)) {
                        min_occurances_node.set(a, ocurrance_pos_a_pL_i);
                    }
                } else {
                    min_occurances_node.set(a, 0); // some of indices of pL exceed max indices...
                }
            }
        }
        for (int i = 0; i < min_occurances_node.size(); i++) {
            ub_1 += min_occurances_node.get(i); // sum of c_a
        }

        return v.getlV() + ub_1;
    }

    private int ub2(Node v) {
        int ub_2 = inst.getS().get(0).size();

        for (int i = 0; i < inst.getM() - 1; i++) {
            int j = i + 1;
            int score_i_pL_i_j = inst.getMMatrix().get(i)
                                        .get(v.getpL().get(i) - 1)
                                        .get(v.getpL().get(j) - 1);
            if (ub_2 > score_i_pL_i_j) {
                ub_2 = score_i_pL_i_j;
            }
        }

        return v.getlV() + ub_2;
    }

    public Vector<Integer> startSearch() {
        Node root = new Node(inst.getM());
        addNode(root);

        while (!Q.isEmpty()) {
            System.out.println("Q=");
            System.out.println(Q);
            Node v = Q.poll().getNode();
            // find non-dominated sigma
            Map<Integer, Vector<Integer>> sigma_nd = sol.findSigmaNd(v);
            System.out.println(sigma_nd);
            // check if we arrived at a complete node (i.e., an optimal solution was found)
            if (sigma_nd.isEmpty()) {
                return sol.deriveSolution(v);
            }

            // create an expansion for every letter from sigma_nd
            for (Vector<Integer> pL : sigma_nd.values()) {
                Node v_ext = sol.expandNode(v, pL);
                List<Integer> N_rel = N.get(pL);
                if (N_rel == null) {
                    addNode(v_ext); // it's the first node for this sub-problem, so just add it to N and Q (unvisited)
                } else {
                    // compare with existing nodes of same sub-problem and possibly merge them
                    boolean insert = true;
                    ListIterator<Integer> it_rel = N_rel.listIterator();
                    while(it_rel.hasNext()) {
                        Integer l_v_rel = it_rel.next();

                        if (l_v_rel >= v_ext.getlV()) {
                            insert = false;
                            break; // the extension is inferior and can be ignored
                        }
                        if (v_ext.getlV() >= l_v_rel) {
                            it_rel.remove();
                        }
                    }
                    if (insert) {
                        addNode(v_ext);
                    }
                }
            }
        }
        return new Vector<Integer>(); // result not found
    }
}
