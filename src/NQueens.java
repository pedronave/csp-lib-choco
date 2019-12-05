import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 * This is a solution for the N-Queens problem from CSPLib
 * http://csplib.org/Problems/prob054/
 *
 * Can n queens (of the same colour) be placed on a n×n chessboard so that none of the queens can attack each other?
 */
public class NQueens {

    private Model model;
    private IntVar[] queens;

    /**
     * Creates an instance of the n-queens problem.
     * @param n number of queens
     * @param globalConstraint whether the model should use global constraints or not
     */
    private NQueens(int n, boolean globalConstraint) {
        model = new Model(n + " - Queens");

        queens = model.intVarArray(n, 1, n);

        if (globalConstraint) {
            /*
             * We can define the diagonals as variables and apply
             * the allDifferent global constraint, which has more
             * efficient propagation for pruning.
             */
            IntVar[] diag1 = new IntVar[n];
            IntVar[] diag2 = new IntVar[n];

            for (int i = 0; i < n; i++) {
                diag1[i] = queens[i].sub(i).intVar();
                diag2[i] = queens[i].add(i).intVar();
            }
            model.allDifferent(queens).post();
            model.allDifferent(diag1).post();
            model.allDifferent(diag2).post();
        } else {
            for(int i = 0; i < n-1; i++) {
                for (int j = i + 1; j < n; j++){
                    model.arithm(queens[i], "!=", queens[j]).post();
                    model.arithm(queens[i], "!=", queens[j], "-", j - i).post();
                    model.arithm(queens[i], "!=", queens[j], "+", j - i).post();
                }
            }
        }
    }

    public void run(boolean findAll) {
        Solver sv = model.getSolver();

        sv.solve();

        do {
            solveAndPrint(sv);
        } while((sv.solve() && findAll));

        System.out.print("\n No (more) solutions found after " + sv.getTimeCount() + " secs");

    }

    private void solveAndPrint(Solver sv) {
        System.out.println(String.format("Solution for %s",model.getName()));
        System.out.println("\n -- solution " + sv.getSolutionCount() + " :");

        for (IntVar queen : queens) {
            // Print the set
            System.out.print(String.format("%d ", queen.getValue()));

        }

        System.out.println();
    }

    public static void main(String[] args) {
        NQueens queens = new NQueens(12, false);

        queens.run(true);
    }
}
