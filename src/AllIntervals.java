import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;

/**
 * This is a solution for the All Intervals problem from CSPLib
 * http://csplib.org/Problems/prob007/
 *
 * For a given n, there is a set with n integers from 0 to n-1.
 * For the set, there is an intervals set with n-1 elements, in which any element contains
 * the subtraction of the element in the set and the following number in the set, such that
 * it is a permutation of the integers from 1 to n-1.
 */
public class AllIntervals {

    private Model model;
    private IntVar[] set;
    private IntVar[] intervals;

    private AllIntervals(int n) {
        model = new Model(n + " - All Intervals");

        set = model.intVarArray(n, 0, n-1);
        intervals = model.intVarArray(n-1, 1, n-1);

        for(int i = 0; i < intervals.length-1; i++) {
            intervals[i].eq(set[i+1].sub(set[i]).abs()).post();
        }

        model.allDifferent(set).post();
        model.allDifferent(intervals).post();
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

        for (IntVar interval : intervals) {
            // Print the intervals
            System.out.print(interval.getValue());
        }

    }

    public static void main(String[] args) {
        AllIntervals intervals = new AllIntervals(10);

        intervals.run(true);
    }
}
