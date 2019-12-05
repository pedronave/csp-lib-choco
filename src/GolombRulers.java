import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 * This is a solution for the Golomb Rulers problem from CSPLib
 * http://csplib.org/Problems/prob006/
 */
public class GolombRulers {

    private Model model;
    private IntVar[] set;
    private IntVar[] diffs;

    private GolombRulers(int m, int ub) {
        model = new Model(m + " - All Intervals");

        set = model.intVarArray(m, 1, ub);
        diffs = model.intVarArray(m * (m-1)/2, 1, ub-1);

//        model.arithm(set[0], "=" , 0).post();

        int p = 0;

        for(int i = 0; i < m-1; i++) {
            for(int j = i+1; j < m-2; j++) {
                model.arithm(diffs[p],"=" , set[j], "-", set[i] ).post();
                p++;
            }
        }

        for (int i = 0; i < set.length-1; i++) {
            model.arithm(set[i], "<" , set[i+1]).post();
        }

        model.allDifferent(diffs).post();
    }

    public void run(boolean printAll) {
        Solver sv = model.getSolver();


        sv.solve();

        do {
            solveAndPrint(sv);
        } while((sv.solve() && printAll));

        System.out.print("\n No (more) solutions found after " + sv.getTimeCount() + " secs");

    }

    private void solveAndPrint(Solver sv) {

        System.out.println("\n -- solution " + sv.getSolutionCount() + " :");

        for (IntVar aSet : set) {
            // Print the set
            System.out.print(String.format("%d ", aSet.getValue()));
        }

        System.out.println();

        for (IntVar diff : diffs) {
            // Print the set
            System.out.print(String.format("%d ", diff.getValue()));
        }

        System.out.println();

    }

    public static void main(String[] args) {
        GolombRulers rulers = new GolombRulers(5, 11);

        rulers.run(false);
    }
}
