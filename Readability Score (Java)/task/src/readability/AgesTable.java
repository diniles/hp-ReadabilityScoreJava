package readability;

import java.util.Arrays;


public enum AgesTable {
    GROUP_ONE("5-6", 1),
    GROUP_TWO("6-7", 2),
    GROUP_THR("7-8", 3),
    GROUP_FOU("8-9", 4),
    GROUP_FIV("9-10", 5),
    GROUP_SIX("10-11", 6),
    GROUP_SEV("11-12", 7),
    GROUP_EIG("12-13", 8),
    GROUP_NIN("13-14", 9),
    GROUP_TEN("14-15", 10),
    GROUP_ELE("15-16", 11),
    GROUP_TWE("16-17", 12),
    GROUP_THI("17-18", 13),
    GROUP_FIN("18-22", 14);

    private final String group;
    private final int score;

    private final String postfix = "year-olds";

    AgesTable(String group, int score) {
        this.group = group;
        this.score = score;
    }

    private String getGroup() {
        return group;
    }

    public static String getGroupByScore(double score) {
        int scoreCeilInt = (int) Math.ceil(score);
        return Arrays.stream(values()).filter(e -> e.score == scoreCeilInt)
                .findFirst()
                .map(e -> String.join("\s", e.getGroup(), e.postfix))
                .orElse("<< not specified >>");
    }
}