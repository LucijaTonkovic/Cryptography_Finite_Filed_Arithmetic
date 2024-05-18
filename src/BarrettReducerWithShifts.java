import java.math.BigInteger;

public class BarrettReducerWithShifts {

    private final BigInteger p;
    private final BigInteger mu;
    private final int wordSize;
    private final int n;

    public BarrettReducerWithShifts(BigInteger primeNum, int wordSize) {
        this.p = primeNum; //Also often called modulus
        this.wordSize = wordSize;
        // The number of digits in the decimal example will be the number of words now
        // We calculate the number of words needed to fit our prime number/modulus
        this.n = (int)Math.ceil(Math.log(this.p.intValue()) / Math.log(Math.pow(2, wordSize)));
        // Since we are working with a processor using a word length of 8 bits, we will use the base b = 2^8
        BigInteger base = BigInteger.valueOf((long)Math.pow(2, wordSize));
        this.mu = base.pow(2 * n).divide(this.p);
        System.out.println("Precomputer value of mu is: " + mu);// Precompute mu value once
        System.out.println("==================================");
    }

    public BigInteger reduce(BigInteger x) {
        // Q1 = ⌊x / b^(n-1)⌋ = x >> wordSize * (n-1), same as removing the last (n-1) words
        BigInteger q1 = x.shiftRight(wordSize * (n - 1));
        System.out.println("Q1: " + q1);

        // Q2 = q1 * mu
        BigInteger q2 = q1.multiply(mu);
        System.out.println("Q2: " + q2);

        // Q3 = ⌊q2 / b^(n+1)⌋ = x >> wordSize * (n+1), same as removing the last (n+1) words
        BigInteger q3 = q2.shiftRight(wordSize * (n + 1));
        System.out.println("Q3: " + q3);

        // R1 = X mod b^(n+1), it's like removing the (n+1) words from the front
        BigInteger r1 = truncateFront(x, wordSize, n + 1);
        System.out.println("R1: " + r1);

        // R2 = Q3 * p mod b^(n+1)
        BigInteger r2 = truncateFront(q3.multiply(p), wordSize, n + 1);
        System.out.println("R2: " + r2);

        // R = R1 - R2
        BigInteger r = r1.subtract(r2);
        System.out.println("R before while: " + r);

        // The while
        while(r.compareTo(p) == 1)
            r = r.subtract(p);

        return r;
    }

    // Note: In general, for the mod operation simple shift left and shift right can be done to erase the front bits
    // However, BitInteger doesn't erase the bits on the left as normally registers do, so we need a mask
    private static BigInteger truncateFront(BigInteger x, int wordSize, int numWordsKeep){
        int xLen = x.bitLength();
        int xNumWords = (int) Math.ceil((double) xLen / wordSize);
        int numWordsRemove = xNumWords - numWordsKeep; // How many words we will remove from the front
        // Shift left for the amount of bits needed to be removed
        BigInteger result = x.shiftLeft(wordSize * numWordsRemove);
        BigInteger mask = BigInteger.ONE.shiftLeft(xLen).subtract(BigInteger.ONE);
        // Use AND operation with the mask to erase the front bits
        result = result.and(mask);
        return result.shiftRight(wordSize * numWordsRemove);
    }

    public static void main(String[] args) {
        BigInteger primeNumber = new BigInteger("9001");
        // In this example we assume the processor is using 8-bit words, we want 2^(wordLength) < our prime number
        int wordLength = 8;
        BarrettReducerWithShifts reducer = new BarrettReducerWithShifts(primeNumber, wordLength);

        // Same example used in the first example where we used a decimal base
        BigInteger x = new BigInteger("486092");
        BigInteger reduced = reducer.reduce(x);
        // The correct reduced value should be: 486092 - 54 * 9001 = 38
        System.out.println("Reduced value of " + x + ": " + reduced);
        System.out.println("==================================");

        // Second example with a larger value that needs reducing
        x = new BigInteger("1956810323");
        reduced = reducer.reduce(x);
        // Value should be: 1956810323 - 217399 * 9001 = 1924
        System.out.println("Reduced value of " + x + ": " + reduced);
        System.out.println("==================================");
    }
}