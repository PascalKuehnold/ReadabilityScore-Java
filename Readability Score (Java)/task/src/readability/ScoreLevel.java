package readability;

public class ScoreLevel {
    int score;
    int ageMin;
    int ageMax;
    String gradeLevel;

    public ScoreLevel(int score, int ageMin, int ageMax, String gradeLevel) {
        this.score = score;
        this.ageMin = ageMin;
        this.ageMax = ageMax;
        this.gradeLevel = gradeLevel;
    }

    public int getScore() {
        return score;
    }

    public int getAgeMin() {
        return ageMin;
    }

    public int getAgeMax() {
        return ageMax;
    }

    public String getGradeLevel() {
        return gradeLevel;
    }
}