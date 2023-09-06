package readability;

import java.util.Arrays;
import java.util.List;

public class Utils {
    private static final ScoreLevel[] scoreLevels = new ScoreLevel[]{
            new ScoreLevel(1, 5, 6, "Kindergarten"),
            new ScoreLevel(2, 6, 7, "First Grade"),
            new ScoreLevel(3, 7, 8, "Second Grade"),
            new ScoreLevel(4, 8, 9, "Third Grade"),
            new ScoreLevel(5, 9, 10, "Fourth Grade"),
            new ScoreLevel(6, 10, 11, "Fifth Grade"),
            new ScoreLevel(7, 11, 12, "Sixth Grade"),
            new ScoreLevel(8, 12, 13, "Seventh Grade"),
            new ScoreLevel(9, 13, 14, "Eighth Grade"),
            new ScoreLevel(10, 14, 15, "Ninth Grade"),
            new ScoreLevel(11, 15, 16, "Tenth Grade"),
            new ScoreLevel(12, 16, 17, "Eleventh Grade"),
            new ScoreLevel(13, 17, 18, "Twelfth Grade"),
            new ScoreLevel(14, 18, 19, "College student"),
    };

    private static int totalSyllableCount = 0;
    private static int totalPolysyllables = 0;
    private static int countSyllables(String sentence) {

        String[] words = sentence.toLowerCase().replaceAll("[.!?]", "").split(" ");
        for (String word : words) {
            int count = 0;
            boolean isPreviousVowel = false;

            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);

                if (isVowel(c)) {
                    if (!isPreviousVowel) {
                        count++;
                    }
                    isPreviousVowel = true;
                } else {
                    isPreviousVowel = false;
                }
            }

            // Adjust for some special cases
            if (word.endsWith("e"))
                count--;

            if (count <= 0)
                count = 1;  // Ensure a minimum count of 1

            totalSyllableCount += count;

            if(count > 2){
                totalPolysyllables += 1;
            }
        }

        return totalSyllableCount;
    }

    public static int getTotalSyllableCount(String sentence){
        return countSyllables(sentence);
    }

    public static boolean isVowel(char c) {
        return "aeiouy".indexOf(c) != -1;
    }

    public static int getPolysyllablesCount() {
        return totalPolysyllables;
    }

    public static long calculateWordCount(String lineToRead) {
        return Arrays.stream(lineToRead.split("\\s+")).count();
    }

    public static int calculateSentenceCount(String lineToRead) {
        return getSentences(lineToRead).size();
        //sentences.addAll(getSentences(lineToRead));
    }

    public static int calculateCharacters(String lineToRead) {
        return lineToRead.replaceAll("\\s+", "").length();

    }

    private static int calculateUpperAge(double score) {
        return Arrays.stream(scoreLevels).filter(scoreLevel -> scoreLevel.getScore() == Math.ceil(score)).mapToInt(scoreLevel -> scoreLevel.ageMax).sum();
    }

    public static int getUpperAge(double score){
        return calculateUpperAge(score);
    }
    private static List<String> getSentences(String input) {
        return Arrays.asList(input.split("[.!?]"));
    }


}
