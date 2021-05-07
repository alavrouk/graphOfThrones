import java.io.IOException;

public class Main {
    /**
     * Main method to print the correct output from the graph of thrones function.
     *
     * @param args Main method command line args.
     * @throws IOException This is thrown when the input file does not exist.
     */
    public static void main(String[] args) throws IOException {
        if (GraphOfThrones.isStructurallyBalancedTwoElectricBoogaloo()) {
            System.out.println("Balanced");
        } else {
            System.out.println("Not Balanced");
        }
    }
}
