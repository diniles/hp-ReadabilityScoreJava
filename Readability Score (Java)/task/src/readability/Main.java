package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    private static final double DOUBLE0_5 = 0.5;
    private static final double DOUBLE4_71 = 4.71;
    private static final double DOUBLE21_43 = 21.43;
    private static final double DOUBLE0_39 = 0.39;
    private static final double DOUBLE11_8 = 11.8;
    private static final double DOUBLE15_59 = 15.59;

    public static String readFileToString(String fName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fName)));
    }

    public static void main(String[] args) {
        try {
            String text = readFileToString(args[0]);
            int scoreFloorPrecision = 2;

            int sentences = text.split("[?.!]+\\s*").length;
            int words = text.split("\\s*(?<!\\d)[-?.!,:;\\s]+|[-?.!,:;\\s]+(?!\\d)\\s*").length;
            int characters = text.replace("\s", "").length();
            int syllables = countSyllablesInText(text);
            int polySyllables = countPolySyllablesInText(text);
            double L = ((double) characters / words) * 100;
            double S = ((double) sentences / words) * 100;

            System.out.printf("The text is:%n%s%n%n", text);
            System.out.printf("Words: %s%n", words);
            System.out.printf("Sentences: %s%n", sentences);
            System.out.printf("Characters: %s%n", characters);
            System.out.printf("Syllables: %s%n", syllables);
            System.out.printf("Polysyllables: %s%n", polySyllables);
            System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): all\n");

            double scoreARI = DOUBLE4_71 * characters / words + DOUBLE0_5 * words / sentences - DOUBLE21_43;
            double scoreFK = DOUBLE0_39 * words / sentences + DOUBLE11_8 * syllables / words - DOUBLE15_59;
            double scoreSMOG = 1.043 * Math.sqrt(polySyllables * 30 / sentences) + 3.1291;
            double scoreCLI = 0.0588 * L - 0.296 * S - 15.8;

            double scoreTruncatedARI = truncate(scoreARI, scoreFloorPrecision);
            double scoreTruncatedFK = truncate(scoreFK, scoreFloorPrecision);
            double scoreTruncatedSMOG = truncate(scoreSMOG, scoreFloorPrecision);
            double scoreTruncatedCLI = truncate(scoreCLI, scoreFloorPrecision);

            System.out.printf("Automated Readability Index: %s (about %s-year-olds).%n", scoreTruncatedARI, getGroup(scoreARI));
            System.out.printf("Flesch–Kincaid readability tests:%s (about %s-year-olds).\n", scoreTruncatedFK, getGroup(scoreFK));
            System.out.printf("Simple Measure of Gobbledygook: %s (about %s-year-olds).\n", scoreTruncatedSMOG, getGroup(scoreSMOG));
            System.out.printf("Coleman–Liau index: %s (about %s-year-olds).\n", scoreTruncatedCLI, getGroup(scoreCLI));

            System.out.printf("This text should be understood in average by %.2f-year-olds.\n",
                    (double) (getGroup(scoreARI)
                            + getGroup(scoreFK)
                            + getGroup(scoreSMOG)
                            + getGroup(scoreCLI)) / 4);

        } catch (IOException err) {
            System.out.println(err.getMessage());
        }
    }

    public static double truncate(double n, int decimals) {
        int whole = (int) (n * Math.pow(10, decimals));
        return whole / Math.pow(10, decimals);
    }

    public static int countPolySyllablesInText(String text) {
        String[] words = text.split("\\s*(?<!\\d)[-?.!,:;\\s]+|[-?.!,:;\\s]+(?!\\d)\\s*");
        int polysyllabicWords = 0;

        for (String word : words) {
            int syllables = countSyllables(word);

            if (syllables > 2) {
                polysyllabicWords++;
            }
        }
        return polysyllabicWords;
    }

    public static int countSyllablesInText(String text) {
        String[] words = text.split("\\s*(?<!\\d)[-?.!,:;\\s]+|[-?.!,:;\\s]+(?!\\d)\\s*");
        int totalSyllables = 0;

        for (String word : words) {
            totalSyllables += countSyllables(word);
        }

        return totalSyllables;
    }

    public static int countSyllables(String word) {
        word = word.toLowerCase();

        if (word.length() == 0) {
            return 1;
        }

        int syllables = 0;
        boolean prevVowel = false;

        for (char c : word.toCharArray()) {
            if ("aeiouy".indexOf(c) >= 0) {
                if (!prevVowel) {
                    syllables++;
                    prevVowel = true;
                }
            } else {
                prevVowel = false;
            }
        }

        if (word.endsWith("e")) {
            syllables--;
        }

        if (syllables == 0) {
            syllables = 1;
        }

        return syllables;
    }

    private static int getGroup(double score) {
        int scoreGroup = (int) Math.ceil(score);
        int result = 0;
        switch (scoreGroup) {
            case 1 -> result = 6;
            case 2 -> result = 7;
            case 3 -> result = 8;
            case 4 -> result = 9;
            case 5 -> result = 10;
            case 6 -> result = 11;
            case 7 -> result = 12;
            case 8 -> result = 13;
            case 9 -> result = 14;
            case 10 -> result = 15;
            case 11 -> result = 16;
            case 12 -> result = 17;
            case 13 -> result = 18;
            case 14 -> result = 22;

        }
        return result;
    }
}