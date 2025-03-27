import java.io.FileNotFoundException;

public class Test {
    public static void main(String[] args) {
        try {
            LCS lcs = new LCS("test.txt");
            LCS_solution sol = new LCS_solution(lcs);
            sol.solve();
            System.out.println(sol.getSolution());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}