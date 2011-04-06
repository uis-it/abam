package no.uis.abam.util;

public class LevenshteinDistance {

	public static int getLevenshteinDistance(String word1, String word2) {

		char word1CharI;
		char word2CharJ;
		int cost;

		int word1Length = word1.length();
		int word2Length = word2.length();
		
		if (word1Length == 0) return word2Length;
		if (word2Length == 0) return word1Length;

		int[][] matrix = new int[word1Length + 1][word2Length + 1];

		for (int i = 0; i <= word1Length; i++) {
			matrix[i][0] = i;
		}

		for (int j = 0; j <= word2Length; j++) {
			matrix[0][j] = j;
		}

		for (int i = 1; i <= word1Length; i++) {
			word1CharI = word1.charAt(i - 1);

			for (int j = 1; j <= word2Length; j++) {
				word2CharJ = word2.charAt(j - 1);

				if (word1CharI == word2CharJ) {
					cost = 0;
				} else {
					cost = 1;
				}

				matrix[i][j] = minimum(matrix[i - 1][j] + 1, matrix[i][j - 1] + 1,
						matrix[i - 1][j - 1] + cost);
			}
		}
		return matrix[word1Length][word2Length];
	}
	
	private static int minimum(int a, int b, int c) {
		int mi = a;
		if (b < mi) mi = b;
		if (c < mi)	mi = c;
		
		return mi;
	}
}
