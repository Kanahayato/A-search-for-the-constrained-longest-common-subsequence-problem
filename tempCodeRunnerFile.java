public class LongestCommonSubstring {
    public static String longestCommonSubstring(String[] strings) {
        if (strings == null || strings.length == 0) {
            return "";
        }

        int totalStrings = strings.length;
        int shortestLength = findShortestStringLength(strings);
        String shortestString = findShortestString(strings);
        int[][] dp = new int[totalStrings][shortestLength];
        int maxLength = 0;
        int endIndex = 0;

        for (int i = 0; i < shortestLength; i++) {
            for (int j = 0; j < totalStrings; j++) {
                if (strings[j].charAt(i) == shortestString.charAt(i)) {
                    if (i == 0 || j == 0) {
                        dp[j][i] = 1;
                    } else {
                        dp[j][i] = dp[j - 1][i - 1] + 1;
                    }

                    if (dp[j][i] > maxLength) {
                        maxLength = dp[j][i];
                        endIndex = i;
                    }
                }
            }
        }

        if (maxLength == 0) {
            return "";
        }

        return shortestString.substring(endIndex - maxLength + 1, endIndex + 1);
    }

    private static int findShortestStringLength(String[] strings) {
        int shortestLength = Integer.MAX_VALUE;
        for (String str : strings) {
            shortestLength = Math.min(shortestLength, str.length());
        }
        return shortestLength;
    }

    private static String findShortestString(String[] strings) {
        String shortestString = strings[0];
        for (String str : strings) {
            if (str.length() < shortestString.length()) {
                shortestString = str;
            }
        }
        return shortestString;
    }

    public static void main(String[] args) {
        String[] strings = {"abcdef", "abcfed", "abcdeffed", "abcfedxyzde"};

        String longestCommonSubstring = longestCommonSubstring(strings);

        System.out.println("Longest common substring: " + longestCommonSubstring);
    }
}