import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

//Генерация файла с наборами случайных чисел

public class Main {
    public static void main(String[] args) throws IOException {
        int amountOfLines = 100; //Количество наборов случайных чисел
        Random random = new Random();

        File input = new File("input.txt");

        input.createNewFile();

        PrintWriter printWriter = new PrintWriter(input);

        for (int i = 0; i < amountOfLines; i++){
            int amountOfnumbers = random.nextInt(10000) + 100; //Количество чисел в каждой строке
            for (int j = 0; j < amountOfnumbers; j++){
                int number = random.nextInt(1000);
                printWriter.print(number + " ");
            }
            printWriter.println();
        }
        printWriter.close();
    }
}