import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class LCS_solution {
    private final LCS inst; // LCS instance
    private Vector<Integer> s; // LCS solution vector s (store solution)

    public LCS_solution(LCS inst) {
        this.inst = inst;
        s = new Vector<>();
    }

    public Map<Integer, Vector<Integer>> findFeasibleSigma(Node v) {
        Map<Integer, Vector<Integer>> feasibleSigma = new HashMap<>();

        for (int a = 0; a < inst.getSigma(); a++) {
            boolean is_feasible_letter = true;
            Vector<Integer> pL_a = new Vector<>();
            for (int i = 0; i < inst.getM(); i++) {
                Vector<Integer> pL_v = v.getpL();
                Vector<Vector<Vector<Integer>>> successors = inst.getSuccessors();
                if (pL_v.get(i) > inst.getS().get(i).size()) {
                    is_feasible_letter = false;
                    break;
                }
                pL_a.add(successors.get(i).get(a).get(pL_v.get(i) - 1) + 1);
            }
            if (is_feasible_letter) {
                feasibleSigma.put(a, pL_a);
            }
        }

        return feasibleSigma;
    }

    public Map<Integer, Vector<Integer>> findSigmaNd(Node v) {
        Map<Integer, Vector<Integer>> sigma_nd = new HashMap<>();
        Map<Integer, Vector<Integer>> sigma_feasible = findFeasibleSigma(v);

        for (Map.Entry<Integer, Vector<Integer>> it1 : sigma_feasible.entrySet()) {
            int letter1 = it1.getKey();
            Vector<Integer> pL_1 = it1.getValue();
            boolean letter1_is_dominated = false;
            for (Map.Entry<Integer, Vector<Integer>> it2 : sigma_feasible.entrySet()) {
                int letter2 = it2.getKey();
                Vector<Integer> pL_2 = it2.getValue();
                if (letter1 != letter2) {
                    letter1_is_dominated = checkDomination(pL_1, pL_2);
                }
                if (letter1_is_dominated) {
                    break;
                }
            }
            if (!letter1_is_dominated) {
                sigma_nd.put(letter1, pL_1);
            }
        }
        return sigma_nd;
    }

    public boolean checkDomination(Vector<Integer> pL_1, Vector<Integer> pL_2) {
        int match_pos = 0;
        for (int i = 0; i < inst.getM(); i++) {
            if (pL_1.get(i) == pL_2.get(i)) {
                match_pos++;
            } else {
                if (pL_1.get(i) < pL_2.get(i)) {
                    return false; // pL_1 is not dominated by pL_2
                }
            }
        }
        if (match_pos == pL_2.size()) // the same pL-vector
            return false;
        return true; // pL_1 is dominated by pL_2
    }

    public Node expandNode(Node parent, Vector<Integer> pL) {
        Node child = new Node();
        child.setParent(parent);
        child.setlV(parent.getlV() + 1);
        child.setpL(new Vector<>(pL));

        return child;
    }

    public Vector<Integer> deriveSolution(Node v) {
        System.out.println(v);
        Vector<Integer> s = new Vector<>();
        while (v.getParent() != null) {
            Vector<Integer> pL_v = v.getpL();
            s.add(inst.getS().get(0).get(pL_v.get(0) - 2));
            v = v.getParent();
        }
        return s;
    }

    public String getSolution() {
        String sol = "";
        for (Integer a : s) {
            sol += inst.getInt2Char().get(a);
        }
        if (sol == "") {
            sol = "-";
        }
        return sol;
    }

    public void solve() {
        this.s = new AStar(this.inst, this).startSearch();
    }
}

