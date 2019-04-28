import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class RemoveTest {
    public static void main(String[] args) throws IOException {
        RedBlackTree<Integer> r = new RedBlackTree<>();
        File input = new File("input.txt");
        File output = new File("remove.csv");
        output.createNewFile();
        Scanner sc = new Scanner(input);
        PrintWriter pw = new PrintWriter(output);
        Random random = new Random();
        pw.println("Time" + "\t" + "Array length");
        while (sc.hasNextLine()){
            int[] arr = getArray(sc);
            for(int i = 0; i < arr.length; i++){
                r.put(arr[i]);
            }
            long elapsedTime = 0; int c = 0;
            for(int j = 0; j < 5; j++){
                long start = System.nanoTime();
                while (c < arr.length / 2) {
                   try {
                       int randNumber = random.nextInt(1000);
                       r.remove(randNumber);
                       c++;
                   }
                   catch (NullPointerException e){
                       continue;
                   }
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