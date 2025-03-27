import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LCS {
    private int m;
    private int n;
    private int sigma;
    private int UB;
    private Vector<Vector<Integer>> S;
    private Vector<Vector<Vector<Integer>>> occurance_positions;
    private Vector<Vector<Vector<Integer>>> successors;
    private Vector<Vector<Vector<Integer>>> M;
    private Vector<Character> int2Char;
    private HashMap<Character, Integer> char2Int;

    public LCS(String filename) throws FileNotFoundException {
        File file = new File(filename);
        Scanner sc = new Scanner(file);

        if (sc.hasNextLine()) {
            this.m = sc.nextInt();
            this.sigma = sc.nextInt();
        }

        int count = 0;
        this.n = 0;
        this.char2Int = new HashMap<>();
        this.int2Char = new Vector<>();
        this.S = new Vector<>(m);
        for (int i = 0; i < this.m && sc.hasNextLine(); i++) {
            // read S_i as string, preceded by the length and transform it in vector<int>
            int len = sc.nextInt();
            String seq = sc.next();
            Vector<Integer> seqs = new Vector<>();
            IntStream.range(0, len)
                .forEach(it -> seqs.add(0));
            this.S.add(seqs);
            for (int j = 0; j < len; ++j) { // read characters...
                int a;
                if (!char2Int.containsKey(seq.charAt(j))) {
    
                    char2Int.put(seq.charAt(j), count);
                    int2Char.add(seq.charAt(j));
                    a = count;
                    count++;
                } else {
                    a = char2Int.get(seq.charAt(j));
                }
                
                S.get(i).set(j, a); // coding the letter with number...
            }
            this.n = Math.max(n, len);
        }
        sc.close();

        structure_occurances(); // create occurrences structure...
        this.M = new Vector<>();
        store_M();   // create M structure...
        this.UB = 0;
        Vector<Integer> minc = IntStream.range(0, sigma) // minimal numbers of appearances of each letter in all sequences
                .mapToObj(i -> Integer.MAX_VALUE)
                .collect(Collectors.toCollection(Vector::new)); 

        for (int i = 0; i < m; i++) {
            Vector<Integer> c = IntStream.range(0, sigma) // number of appearances of each letter in S_i
                .mapToObj(it -> 0)
                .collect(Collectors.toCollection(Vector::new));  
            for (int a : S.get(i))
                    c.set(a, c.get(a) + 1);
            for (int a = 0; a < sigma; a++)
                    minc.set(a, Math.min(c.get(a), minc.get(a)));
            c.clear();
        }
        for (int i = 0; i < sigma; i++) {
                if (minc.get(i) >= 1)
                    this.UB += minc.get(i);
        }
    }

    private void structure_occurances() {
        // procedure to establish preprocessing structure Occs
        occurance_positions = IntStream.range(0, m)
                .mapToObj(i -> new Vector<>(IntStream.range(0, sigma)
                        .mapToObj(j -> new Vector<>(IntStream.range(0, n)
                                .mapToObj(k -> 0)
                                .collect(Collectors.toList())))
                        .collect(Collectors.toList())))
                .collect(Collectors.toCollection(Vector::new));

        successors = IntStream.range(0, m)
                .mapToObj(i -> new Vector<>(IntStream.range(0, sigma)
                        .mapToObj(j -> new Vector<>(IntStream.range(0, n + 1)
                                .mapToObj(k -> 0)
                                .collect(Collectors.toList())))
                        .collect(Collectors.toList())))
                .collect(Collectors.toCollection(Vector::new));

        for (int i = 0; i < m; i++) {
            occurance_positions.set(i, occurances_all_letters(S.get(i)));
            successors.set(i, succesors_all_letters(S.get(i)));
        }
    }

    private Vector<Vector<Integer>> occurances_all_letters(Vector<Integer> str) {
        Vector<Vector<Integer>> occurances_all = IntStream.range(0, sigma)
                .mapToObj(i -> new Vector<Integer>(n))
                .collect(Collectors.toCollection(Vector::new));

        for (int a = 0; a < sigma; a++) // iterate through each letter from alphabet and find Occ vector for it for each input string <str>
            occurances_all.set(a, occurances_string_letter(str, a));
        
        return occurances_all;
    }

    private Vector<Integer> occurances_string_letter(Vector<Integer> str, int a) {
        Vector<Integer> occurances_letter = IntStream.range(0, n)
                .mapToObj(i -> 0)
                .collect(Collectors.toCollection(Vector::new));
        int number = 0;
        for (int i = n - 1; i >= 0; i--) {
            if (str.get(i) == a) {
                number++;
                occurances_letter.set(i, number);
            }
        }
        return occurances_letter;
    }

    private Vector<Vector<Integer>> succesors_all_letters(Vector<Integer> str) {
        Vector<Vector<Integer>> successors_all = IntStream.range(0, sigma)
                .mapToObj(i -> new Vector<>(IntStream.range(0, n + 1)
                        .mapToObj(j -> 0)
                        .collect(Collectors.toList())))
                .collect(Collectors.toCollection(Vector::new));
        for (int a = 0; a < sigma; a++) // iterate for all letters from alphabet
            successors_all.set(a, successor_string_letter(str, a));
        return successors_all;
    }

    private Vector<Integer> successor_string_letter(Vector<Integer>str, int a) {
        Vector<Integer> successor_letter = IntStream.range(0, n + 1)
                .mapToObj(i -> 0)
                .collect(Collectors.toCollection(Vector::new));
        int size = str.size();
        int number = size; // starting position meaning that no successor of letter <a>
        for (int i = size - 1; i >= 0; --i) { 
            if (str.get(i) == a) {
                number = i + 1;
            }
            successor_letter.set(i, number);
        }
        if (str.get(0) == a) {
            successor_letter.set(0, 1);
        }
        return successor_letter;
    }

    private void store_M() {
        for (int i = 0; i < m - 1; i++) {
            M.add(lcs_m_ij(i, i + 1));

        }
    }

    private Vector<Vector<Integer>> lcs_m_ij(int i, int j) { // creating LCS M_ij score matrix (of dimension |s_i| x |s_j|)
        int size_i = S.get(i).size();
        int size_j = S.get(j).size();

        Vector<Vector<Integer>> m_ij = IntStream.range(0, size_i + 1)
                .mapToObj(it -> new Vector<>(IntStream.range(0, size_j + 1)
                        .mapToObj(jt -> 0)
                        .collect(Collectors.toList())))
                .collect(Collectors.toCollection(Vector::new));

        for (int x = size_i; x >= 0; x--) {
            for (int y = size_j; y >= 0; y--) {
                if (x == size_i || y == size_j) {
                    m_ij.get(x).set(y, 0);
                } else if (S.get(i).get(x) == S.get(j).get(y)) {
                    m_ij.get(x).set(y, m_ij.get(x).get(y) + 1);
                } else {
                    m_ij.get(x).set(y, Math.max(m_ij.get(x).get(y + 1), m_ij.get(x + 1).get(y)));
                }
            }
        }

        return m_ij;
    }

    
    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getSigma() {
        return sigma;
    }

    public void setSigma(int sigma) {
        this.sigma = sigma;
    }

    public int getUB() {
        return UB;
    }

    public void setUB(int uB) {
        UB = uB;
    }

    public Vector<Vector<Integer>> getS() {
        return S;
    }

    public void setS(Vector<Vector<Integer>> s) {
        S = s;
    }

    public Vector<Vector<Vector<Integer>>> getOccurance_positions() {
        return occurance_positions;
    }

    public void setOccurance_positions(Vector<Vector<Vector<Integer>>> occurance_positions) {
        this.occurance_positions = occurance_positions;
    }

    public Vector<Vector<Vector<Integer>>> getSuccessors() {
        return successors;
    }

    public void setSuccessors(Vector<Vector<Vector<Integer>>> successors) {
        this.successors = successors;
    }

    public Vector<Vector<Vector<Integer>>> getMMatrix() {
        return M;
    }

    public void setMMatrix(Vector<Vector<Vector<Integer>>> m) {
        M = m;
    }

    public Vector<Character> getInt2Char() {
        return int2Char;
    }

    public void setInt2Char(Vector<Character> int2Char) {
        this.int2Char = int2Char;
    }

    public HashMap<Character, Integer> getChar2Int() {
        return char2Int;
    }

    public void setChar2Int(HashMap<Character, Integer> char2Int) {
        this.char2Int = char2Int;
    }

    public static void main(String[] args) {
        try {
            LCS lcs = new LCS("test.txt");
            System.out.println(lcs.S);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}
