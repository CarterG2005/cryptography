import java.util.*;
public class IOC {
	// Compute IoC for a string of letters
	public static double computeIoC(String text) {
		int N = text.length();
		if (N <= 1) return 0.0;
		
		int[] freq = new int[26];
		for (char c : text.toCharArray()) {
			if (Character.isLetter(c)) {
				freq[Character.toLowerCase(c) - 'a']++;
			}
		}

		long numerator = 0;
		for (int count : freq) {
			numerator += (long) count * (count - 1);
		}
		return (double) numerator / (N * (N - 1));
	}

	// Compute average IoC using a given key length
	public static double averageIoC(String ciphertext, int keyLength) {
		double total = 0.0;
		int groups = keyLength;
		
		for (int i = 0; i < groups; i++) {
			String group = "";
			for (int j = i; j < ciphertext.length(); j += keyLength) {
				group += ciphertext.charAt(j);
			}
			total += computeIoC(group);
		}
		return total / groups;
	}

	public static void main(String[] args) {
		// The ciphertext goes here
		String cipher =
		"tgicbsbwogessifdmnmsoqqfoegfiylzpjpbmrnuebarmkcytuelezosbjiew" +
		"zczotpnlbbacfgtpnbhikhzognaozfksfsovnaylslvxagcoghdxfqftxoefn" +
		"yixhmwybusebfywdryarxjuexhohwhpctemjlnrfigjltjrjislrtqcjrrcqsjtsmltvomglsvmuestzvfvnlchvsomcmazrtfasxrdhmmtbtgifvtardroexy" +
		"fbcrlqimknjtpjbyabojfcbroaaaslusqgfresswgtpngeotrujtqbhrrdxybtqqaiesegfdbqeyarxtpuxxnvfxslbrmaendhrxuhqbybuviifajuegogeeelmbievhkvoeznsuamhzxoztsbjnfnfltmoae";
		
		// make ciphertext lowercase if it isnt already
		cipher = cipher.toLowerCase();
		System.out.println("IoC of whole text: " + computeIoC(cipher));
		
		// test key lengths 1..20
		for (int k = 1; k <= 20; k++) {
			double avgIoC = averageIoC(cipher, k);
			System.out.printf("Key length %2d -> Avg IoC = %.5f%n", k, avgIoC);
		}
	}
}