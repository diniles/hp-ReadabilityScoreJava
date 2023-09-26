package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static readability.AgesTable.getGroupByScore;


public class Main {

    private static final double DOUBLE0_5 = 0.5;
    private static final double DOUBLE4_71 = 4.71;
    private static final double DOUBLE21_43 = 21.43;

    public static String readFileToString(String fName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fName)));
    }

    public static void main(String[] args) {
        try {
            String text = readFileToString(args[0]);
            int scoreFloorPrecision = 2;  // args[1]

            int sentences = text.split("[?.!]+\\s*").length;
            int words = text.split("\\s*(?<!\\d)[-?.!,:;\\s]+|[-?.!,:;\\s]+(?!\\d)\\s*").length;
            int characters = text.replace("\s", "").length();

            System.out.printf("The text is:%n%s%n%n", text);
            System.out.printf("Words: %s%n", words);
            System.out.printf("Sentences: %s%n", sentences);
            System.out.printf("Characters: %s%n", characters);

            double score = DOUBLE4_71 * characters / words + DOUBLE0_5 * words / sentences - DOUBLE21_43;

            double scoreTruncated = truncate(score, scoreFloorPrecision);

            System.out.printf("The score is: %s%n", scoreTruncated);
            System.out.printf("This text should be understood by %s.", getGroupByScore(score));

        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
    }

    public static double truncate(double n, int decimals) {
        int whole = (int) (n * Math.pow(10, decimals));
        return whole / Math.pow(10, decimals);
    }
}