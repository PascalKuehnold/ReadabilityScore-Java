package readability;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


public class Main {
    static int wordsCount = 0;
    static int sentenceCount = 0;
    static int characterCount = 0;

    static int syllableCount = 0;

    static int polysyllables = 0;

    static boolean isEverythingCalculated = false;
    static int totalAge = 0;


    static String text;

    public static void main(String[] args) {
        if (args.length == 0 || args[0].isEmpty()) {
            System.out.println("Usage: Please provide a file path as a command-line argument.");
            return;
        }
        run(args[0]);
    }

    private static void run(String filePath) {
        if (!isValidFilePath(filePath)) {
            System.out.println("Invalid file path or file does not exist.");
            return;
        }
        readFile(filePath);
    }

    private static boolean isValidFilePath(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return false;
        }

        // Use Paths.get to create a Path object
        // Use Files.exists to check if the file exists
        // Use Files.isReadable to check if the file is readable
        Path path = Paths.get(filePath);
        return Files.exists(path) && Files.isReadable(path);
    }


    private static void readFile(String filePath) {
        try (FileReader fileReader = new FileReader(filePath)) {

            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;

                stringBuilder.append(line);

                sentenceCount += Utils.calculateSentenceCount(line);
                wordsCount += Utils.calculateWordCount(line);
                characterCount += Utils.calculateCharacters(line);
                syllableCount += Utils.getTotalSyllableCount(line);
                polysyllables += Utils.getPolysyllablesCount();
            }

            text = stringBuilder.toString();
            printResult();
            bufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void printResult() {

        System.out.println("The text is:\n" + text);
        System.out.println();
        System.out.printf("Words: %d\n", wordsCount);
        System.out.printf("Sentences: %d\n", sentenceCount);
        System.out.printf("Characters: %d\n", characterCount);
        System.out.printf("Syllables: %d\n", syllableCount);
        System.out.printf("Polysyllables: %d\n", polysyllables);

        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");

        readInput();

    }

    private static void readInput() {
        try (Scanner scanner = new Scanner(System.in)) {
            String input = scanner.nextLine();

            calculateScores(input);
        }
    }

    private static void calculateScores(String input) {
        switch (input.toUpperCase()) {
            case "ARI" -> calculateARIScore();
            case "FK" -> calculateFKScore();
            case "SMOG" -> calculateSMOGScore();
            case "CL" -> calculateCLScore();
            case "ALL" -> calculateAllScores();
            default -> {
                System.out.println("Invalid Input, please try again.");
                readInput();
            }
        }
    }


    private static void calculateAllScores() {
        isEverythingCalculated = true;

        calculateARIScore();
        calculateFKScore();
        calculateSMOGScore();
        calculateCLScore();

        double ageAverage = (double) totalAge / 4;

        System.out.printf("This text should be understood in average by %.2f-year-olds.\n", ageAverage);
    }

    /**
     * Calculates the Coleman–Liau index score
     *
     * @see <a href="https://en.wikipedia.org/wiki/Coleman–Liau_index">Coleman–Liau index</a>
     */
    private static void calculateCLScore() {
        // l is the average number of characters per 100 words
        double l = (double) characterCount / wordsCount * 100;
        // s is the average number of sentences per 100 words
        double s = (double) sentenceCount / wordsCount * 100;

        double score = 0.0588 * l - 0.296 * s - 15.8;

        int age = Utils.getUpperAge(score);
        System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds).\n\n", score, age);

        if (isEverythingCalculated) {
            totalAge += age;
            return;
        }

        System.out.printf("This text should be understood by %d-year-olds.", age);
    }


    /**
     * Calculates the SMOG index score
     *
     * @see <a href="https://en.wikipedia.org/wiki/SMOG">SMOG</a>
     */
    private static void calculateSMOGScore() {
        double score = 1.043 * Math.sqrt((double) (polysyllables * 30) / sentenceCount) + 3.1291;

        int age = Utils.getUpperAge(score);
        System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds).\n", score, age);

        if (isEverythingCalculated) {
            totalAge += age;
            return;
        }
        System.out.printf("This text should be understood by %d-year-olds.", age);
    }

    /**
     * Calculates the Flesch–Kincaid readability score
     *
     * @see <a href="https://en.wikipedia.org/wiki/Flesch–Kincaid_readability_tests">Flesch–Kincaid readability</a>
     */
    private static void calculateFKScore() {
        double score = 0.39 * ((double) wordsCount / sentenceCount) + (11.8 * ((double) syllableCount / wordsCount)) - 15.59;
        int age = Utils.getUpperAge(score);
        System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds).\n", score, age);

        if (isEverythingCalculated) {
            totalAge += age;
            return;
        }
        System.out.printf("This text should be understood by %d-year-olds.", age);
    }

    /**
     * Calculates the Automated readability index score
     *
     * @see <a href="https://en.wikipedia.org/wiki/Automated_readability_index">Automated readability index</a>
     */
    private static void calculateARIScore() {
        double score = (4.71 * ((double) characterCount / wordsCount)) + (0.5 * ((double) wordsCount / sentenceCount)) - 21.43;

        int age = Utils.getUpperAge(score);
        System.out.printf("Automated Readability Index: %.2f (about %d-year-olds).\n", score, age);

        if (isEverythingCalculated) {
            totalAge += age;
            return;
        }

        System.out.printf("This text should be understood by %d-year-olds.", age);
    }


}
