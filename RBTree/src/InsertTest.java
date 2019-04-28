import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class InsertTest {
    public static void main(String[] args) throws IOException {
        RedBlackTree<Integer> r = new RedBlackTree<>();
        File input = new File("input.txt");
        File output = new File("insert.csv");
        output.createNewFile();
        Scanner sc = new Scanner(input);
        PrintWriter pw = new PrintWriter(output);
        pw.println("Time" + "\t" + "Array length");
        while (sc.hasNextLine()){
            int[] arr = getArray(sc);
            long elapsedTime = 0;
            for(int j = 0; j < 5; j++){
                long start = System.nanoTime();
                for(int i = 0; i < arr.length; i++){
                    r.put(arr[i]);
                }
                long end = System.nanoTime();
                elapsedTime += (end - start);
            }
            long mediumElapsedTime = elapsedTime / 5;
            pw.println(mediumElapsedTime + "\t" + arr.length);
        }
        pw.close();
    }

    public static int[] getArray(Scanner sc) {
        String[] arr = sc.nextLine().split(" ");
        int[] numbers = new int[arr.length];
        for(int i = 0; i < arr.length; i++ ){
            numbers[i] = Integer.parseInt(arr[i]);
        }
        return numbers;
    }
}
