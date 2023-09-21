package readability;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String[] sentences = sc.nextLine().split("[!.?]");
        String[] words = String.join(" ", sentences).split("\\s+");
        String ans = 1.0 * words.length / sentences.length > 10 ? "HARD" : "EASY";

        System.out.println(ans);
    }
}