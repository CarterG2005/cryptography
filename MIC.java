import java.util.*;
public class MIC {
	
	// English letter frequencies (values taken from formula sheet). Index 0 = a, ... index 25 = z
	private static final double[] ENGLISH_FREQ = {
			0.082, 0.015, 0.028, 0.043, 0.127, 0.022,
			0.020, 0.061, 0.070, 0.002, 0.008, 0.040,
			0.024, 0.067, 0.075, 0.019, 0.001, 0.060,
			0.063, 0.091, 0.028, 0.010, 0.023, 0.001,
			0.020, 0.001
	};
	
	// Compute frequencies of letters in a string
	private static double[] computeFrequencies(String text) {
		double[] freq = new double[26];
		int total = 0;
		
		for (char c : text.toCharArray()) {
			if (Character.isLetter(c)) {
				int idx = Character.toLowerCase(c) - 'a';
				if (idx >= 0 && idx < 26) {
					freq[idx]++;
					total++;
				}
			}
		}
		if (total > 0) {
			for (int i = 0; i < 26; i++) {
				freq[i] /= total;
			}
		}
		return freq;
	}
	// Break the ciphertext into k groups
	private static String[] createGroups(String text, int k) {
		String[] groups = new String[k];
		for (int i = 0; i < k; i++) {
			String group = "";
			for (int j = i; j < text.length(); j += k) {
				char c = text.charAt(j);
				if (Character.isLetter(c)) {
					group += Character.toLowerCase(c);
				}
			}
			groups[i] = group;
		}
		return groups;
	}
	// Analyze the MIC for each group, determining the best shift
	public static void analyze(String ciphertext, int keyLength) {
		String[] groups = createGroups(ciphertext, keyLength);
		
		for (int g = 0; g < groups.length; g++) {
			double[] freq = computeFrequencies(groups[g]);
			int bestShift = 0;
			double bestMIC = -1.0;

			// Try all 26 shifts for each group
			for (int shift = 0; shift < 26; shift++) {
				double mic = 0.0;
				for (int i = 0; i < 26; i++) {
					int shiftedIndex = (i + shift) % 26;
					mic += freq[shiftedIndex] * ENGLISH_FREQ[i];
				}
				if (mic > bestMIC) {
					bestMIC = mic;
					bestShift = shift;
				}
			}
			System.out.printf("Group %d best shift: %d (MIC = %.5f)%n", g + 1,
					bestShift, bestMIC);
		}
	}
	public static void main(String[] args) {
		// Ciphertext goes here
		String ciphertext =
				"tgicbsbwogessifdmnmsoqqfoegfiylzpjpbmrnuebarmkcytuelezosbjiewzczotpnlbbacfgtpnbhikhzognaozfksfsovnaylslvxagcoghdxfqftxoefnyixhmwybusebfywdryarxjuexhohwhpctemjlnrfigjltjrjislrtqcjrrcqsjtsmltvomglsvmuestzvfvnlchvsomcmazrtfasxrdhmmtbtgifvtardroexyfbcrlqimknjtpjbyabojfcbroaaaslusqgfresswgtpngeotrujtqbhrrdxybtqqaiesegfdbqeyarxtpuxxnvfxslbrmaendhrxuhqbybuviifajuegogeeelmbievhkvoeznsuamhzxoztsbjnfnfltmoae";
		// key length goes here
		int k = 10;
		analyze(ciphertext, k);
	}
}
