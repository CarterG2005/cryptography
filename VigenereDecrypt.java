public class VigenereDecrypt {
	
	// Decrypt a single lowercase character. This method assumes its lowercase
	private static char decryptChar(char c, char keyChar) {
		// Only process letters a-z
		if (c >= 'a' && c <= 'z') {
			int decrypted = (c - 'a' - (keyChar - 'a') + 26) % 26;
			return (char) (decrypted + 'a');
		}
		return c;
	}
	
	// Decrypt using the given key word
	public static String decrypt(String ciphertext, String key) {
		String plaintext = "";
		int keyLen = key.length();
		for (int i = 0, j = 0; i < ciphertext.length(); i++) {
			char c = ciphertext.charAt(i);
			if (c >= 'a' && c <= 'z') {
				plaintext += decryptChar(c, key.charAt(j % keyLen));
				j++;
			} else {
				plaintext += c;
			}	
		}
		return plaintext;
	}
	public static void main(String[] args) {
		// Cipher text and key go here
		String ciphertext =
		"tgicbsbwogessifdmnmsoqqfoegfiylzpjpbmrnuebarmkcytuelezosbjiewzczotpnlbbacfgtpnbhikhzognaozfksfsovnaylslvxagcoghdxfqftxoefnyixhmwybusebfywdryarxjuexhohwhpctemjlnrfigjltjrjislrtqcjrrcqsjtsmltvomglsvmuestzvfvnlchvsomcmazrtfasxrdhmmtbtgifvtardroexyfbcrlqimknjtpjbyabojfcbroaaaslusqgfresswgtpngeotrujtqbhrrdxybtqqaiesegfdbqeyarxtpuxxnvfxslbrmaendhrxuhqbybuviifajuegogeeelmbievhkvoeznsuamhzxoztsbjnfnfltmoae";
		String key = "azerbaijan";
		
		String decryptedText = decrypt(ciphertext, key);
		System.out.println("Decrypted text: " + decryptedText);
	}
}
