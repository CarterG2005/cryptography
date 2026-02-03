// MillerRabin2.java
// Minimal edits to instructor code (Arup Guha) to run the requested experiment.
// Added experiment loop, Fermat-with-specified-a, MillerRabin-with-specified-a,
// and frequency tracking. Outputs results to console.
//
// Usage: compile and run. Adjust NUM_TRIALS if you want more than 10,000 trials.

import java.math.BigInteger;
import java.util.*;

public class MillerRabin2 {
    
    // Configurable number of trials:
    private static final int NUM_TRIALS = 10000; // change as desired (>=10000 per request)
    private static final int NUM_BASES = 50;     // number of a-values per composite

    // Just used to test out our MillerRabinTest
    public static void main(String[] args) {
        Random r = new Random();

        // Frequency arrays: index i = number of "probably prime" results in a row (0..50)
        int[] fermatCounts = new int[NUM_BASES + 1];
        int[] mrCounts     = new int[NUM_BASES + 1];

        int trialsDone = 0;
        while (trialsDone < NUM_TRIALS) {
            // 1) Generate a random composite NOT divisible by 2,3,5 in [1e8,1e9)
            BigInteger n = randomCompositeBetween1e8And1e9(r);
            if (n == null) continue; // just in case, continue to next attempt

            // 2) Fermat experiment: count how many "probably prime" before composite detected
            int fermatResult = runFermatExperiment(n, r, NUM_BASES);
            fermatCounts[fermatResult]++;

            // 3) Miller-Rabin experiment: same counting behavior, skip gcd!=1 cases
            int mrResult = runMillerRabinExperiment(n, r, NUM_BASES);
            mrCounts[mrResult]++;

            trialsDone++;
            if (trialsDone % 1000 == 0) {
                System.out.println("Completed trials: " + trialsDone);
            }
        }

        // Print results
        System.out.println("\n=== Experiment complete ===");
        System.out.println("Trials: " + NUM_TRIALS + ", Bases per trial: " + NUM_BASES);
        System.out.println("\nFermat frequency (k => count) [k = number of consecutive 'probably prime' before composite]:");
        for (int i = 0; i <= NUM_BASES; i++) {
            System.out.printf("%2d : %6d\n", i, fermatCounts[i]);
        }

        System.out.println("\nMiller-Rabin frequency (k => count):");
        for (int i = 0; i <= NUM_BASES; i++) {
            System.out.printf("%2d : %6d\n", i, mrCounts[i]);
        }

        // Simple summary stats
        System.out.println("\nSummary:");
        System.out.printf("Fermat: fraction where all %d bases said 'probably prime' = %.4f\n",
                NUM_BASES, (double)fermatCounts[NUM_BASES] / NUM_TRIALS);
        System.out.printf("Miller-Rabin: fraction where all %d bases said 'probably prime' = %.4f\n",
                NUM_BASES, (double)mrCounts[NUM_BASES] / NUM_TRIALS);

        System.out.println("\n(You can copy the frequency tables into Excel or Python to produce charts.)");
    }

    // --- Helper experiment methods -------------------------------------------------

    // Run up to numBases valid Fermat tests (skipping gcd!=1); return k in 0..numBases
    // meaning first k were 'probably prime', (k+1)th detected composite (or k == numBases if none did).
    private static int runFermatExperiment(BigInteger n, Random r, int numBases) {
        int passed = 0;
        int attempts = 0;
        while (passed < numBases) {
            // choose random a in [2, n-2]
            BigInteger a = randomAbetween2andNminus2(n, r);
            if (a == null) break; // should not happen for our range
            if (!n.gcd(a).equals(BigInteger.ONE)) {
                // Per assignment: skip such a (do NOT count it)
                attempts++;
                if (attempts > numBases * 10) break; // safety to avoid infinite loops
                continue;
            }
            boolean passes = fermatTestWithA(n, a);
            if (!passes) {
                // detected composite now; return number that passed before this detection
                return passed;
            } else {
                passed++;
            }
        }
        // If we get here, either passed == numBases or something prevented more trials.
        return passed;
    }

    // Run up to numBases valid Miller-Rabin tests (skipping gcd!=1); return k in 0..numBases
    private static int runMillerRabinExperiment(BigInteger n, Random r, int numBases) {
        int passed = 0;
        int attempts = 0;
        while (passed < numBases) {
            BigInteger a = randomAbetween2andNminus2(n, r);
            if (a == null) break;
            if (!n.gcd(a).equals(BigInteger.ONE)) {
                // Per assignment: skip such a (do NOT count it)
                attempts++;
                if (attempts > numBases * 10) break;
                continue;
            }
            boolean passes = myMillerRabinWithA(n, a);
            if (!passes) {
                return passed;
            } else {
                passed++;
            }
        }
        return passed;
    }

    // --- Smallutility functions ---------------------------------------------------

    // Fermat test but with specified 'a' value (returns true iff a^(n-1) ≡ 1 (mod n))
    public static boolean fermatTestWithA(BigInteger n, BigInteger a) {
        BigInteger ans = a.modPow(n.subtract(BigInteger.ONE), n);
        return ans.equals(BigInteger.ONE);
    }

    // Miller-Rabin test but use provided 'a' as the base; returns true if 'probably prime' for that a.
    // Minimal change from instructor's MyMillerRabin but uses the given 'a' and assumes caller
    // has already ensured gcd(a,n) == 1 (we still check nowhere else).
    private static boolean myMillerRabinWithA(BigInteger n, BigInteger a) {
        BigInteger temp = a; // use given base

        BigInteger base = n.subtract(BigInteger.ONE);
        BigInteger TWO = new BigInteger("2");

        // Figure out the largest power of two that divides evenly into n-1.
        int k=0;
        while ( (base.mod(TWO)).equals(BigInteger.ZERO)) {
            base = base.divide(TWO);
            k++;
        }

        BigInteger curValue = temp.modPow(base,n);

        // If this works out, we just say it's prime (for this base).
        if (curValue.equals(BigInteger.ONE))
            return true;

        // Otherwise, check if successive squares yield -1 (n-1).
        for (int i=0; i<k; i++) {
            if (curValue.equals(n.subtract(BigInteger.ONE)))
                return true;
            else
                curValue = curValue.modPow(TWO, n);
        }
        // If none of our checks pass, n is definitively composite relative to this base.
        return false;
    }

    // Pick a random a with 2 <= a <= n-2. Works because n < 1e9 in our experiments.
    private static BigInteger randomAbetween2andNminus2(BigInteger n, Random r) {
        int nInt = n.intValue(); // safe because n < 1e9
        if (nInt <= 4) return null;
        int aInt = 2 + r.nextInt(nInt - 3); // gives 2 .. n-2 inclusive
        return BigInteger.valueOf(aInt);
    }

    // Generate a random composite number between 1e8 (inclusive) and 1e9 (exclusive),
    // not divisible by 2,3,5. Returns BigInteger composite (guaranteed composite by isProbablePrime check).
    private static BigInteger randomCompositeBetween1e8And1e9(Random r) {
        final int MIN = 100_000_000;
        final int RANGE = 900_000_000; // so MIN + r.nextInt(RANGE) in [1e8, 1e9)
        int attempts = 0;
        while (true) {
            int candidate = MIN + r.nextInt(RANGE);
            // Make odd:
            if ((candidate & 1) == 0) candidate++; // ensure odd
            // Ensure not divisible by 3 or 5
            if (candidate % 3 == 0 || candidate % 5 == 0) {
                candidate += 2; // try next odd
            }
            // Safety: keep candidate in range
            if (candidate >= MIN + RANGE) candidate = candidate - 2;

            BigInteger n = BigInteger.valueOf(candidate);
            // Use a decent certainty for primality check; if it's prime, reject and try again.
            if (!n.isProbablePrime(20)) {
                return n; // composite
            }
            attempts++;
            if (attempts > 5000) {
                // extremely unlikely, but avoid infinite loop; return null to skip this trial
                return null;
            }
        }
    }

    // ----- Original instructor code left unchanged below (where possible) ----------
    // (I refrained from altering original MyMillerRabin and FermatTest methods;
    //  instead created *_WithA variants above to preserve instructor code.)
    public static boolean FermatTest(BigInteger n, Random r) {
        // Ensures that temp > 1 and temp < n.
        BigInteger temp = BigInteger.ZERO;
        do {
            temp = new BigInteger(n.bitLength()-1, r);
        } while (temp.compareTo(BigInteger.ONE) <= 0);

        // Just calculate temp^*(n-1) mod n
        BigInteger ans = temp.modPow(n.subtract(BigInteger.ONE), n);

        // Return true iff it passes the Fermat Test!
        return (ans.equals(BigInteger.ONE));
    }

    private static boolean MyMillerRabin(BigInteger n, Random r) {
        // Ensures that temp > 1 and temp < n.
        BigInteger temp = BigInteger.ZERO;
        do {
            temp = new BigInteger(n.bitLength()-1, r);
        } while (temp.compareTo(BigInteger.ONE) <= 0);

        // Screen out n if our random number happens to share a factor with n.
        if (!n.gcd(temp).equals(BigInteger.ONE)) return false;

        // For debugging, prints out the integer to test with.
        //System.out.println("Testing with " + temp);

        BigInteger base = n.subtract(BigInteger.ONE);
        BigInteger TWO = new BigInteger("2");

        // Figure out the largest power of two that divides evenly into n-1.
        int k=0;
        while ( (base.mod(TWO)).equals(BigInteger.ZERO)) {
            base = base.divide(TWO);
            k++;
        }

        // This is the odd value r, as described in our text.
        //System.out.println("base is " + base);

        BigInteger curValue = temp.modPow(base,n);

        // If this works out, we just say it's prime.
        if (curValue.equals(BigInteger.ONE))
            return true;

        // Otherwise, we will check to see if this value successively
        // squared ever yields -1.
        for (int i=0; i<k; i++) {
            // We need to really check n-1 which is equivalent to -1.
            if (curValue.equals(n.subtract(BigInteger.ONE)))
                return true;
            // Square this previous number - here I am just doubling the
            // exponent. A more efficient implementation would store the
            // value of the exponentiation and square it mod n.
            else
                curValue = curValue.modPow(TWO, n);
        }

        // If none of our tests pass, we return false. The number is
        // definitively composite if we ever get here.
        return false;
    }

    public static boolean MillerRabin(BigInteger n, int numTimes) {
        Random r = new Random();

        // Run Miller-Rabin numTimes number of times.
        for (int i=0; i<numTimes; i++)
            if (!MyMillerRabin(n,r)) return false;

        // If we get here, we assume n is prime. This will be incorrect with
        // a probability no greater than 1/4^numTimes.
        return true;
    }
}
