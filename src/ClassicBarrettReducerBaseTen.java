import java.math.BigInteger;

public class ClassicBarrettReducerBaseTen {

    private final BigInteger p;
    private final BigInteger mu;
    private final int n;

    public ClassicBarrettReducerBaseTen(BigInteger primeNumber) {
        this.p = primeNumber; // Also known as modulus
        this.n = primeNumber.toString().length(); // Number of digits in modulus (because we work with base 10)
        this.mu = BigInteger.TEN.pow(2 * n).divide(primeNumber); // Precompute mu value
        System.out.println("Precomputer value of mu is: " + mu);// Precompute mu value once
        System.out.println("==================================");
    }

    public BigInteger reduce(BigInteger x) {
        // Q1 = ⌊X / 10^n-1⌋
        BigInteger q1 = x.divide(BigInteger.TEN.pow(n - 1));
        System.out.println("Q1: " + q1);

        // Q2 = Q1 * mu
        BigInteger q2 = q1.multiply(mu);
        System.out.println("Q2: " + q2);

        // \hat{Q} = ⌊Q2 / 10^(n+1)⌋
        BigInteger q3 = q2.divide(BigInteger.TEN.pow(n + 1));
        System.out.println("Q3 = " + q3);

        // R1 = X mod 10^(n+1)
        BigInteger r1 = x.mod(BigInteger.TEN.pow(n + 1));
        System.out.println("R1: " + r1);

        // R2 = Q3 * p mod 10^(n+1)
        BigInteger r2 = q3.multiply(p).mod(BigInteger.TEN.pow(n + 1));
        System.out.println("R2: " + r2);

        // R = R1 - R2
        BigInteger r = r1.subtract(r2);

        if(r.compareTo(BigInteger.ZERO) < 0)
            r = r.add(BigInteger.TEN.pow(n + 1));

        System.out.println("R before while: " + r);

        // The while
        while(r.compareTo(p) >= 0)
            r = r.subtract(p);

        return r;
    }

    public static void main(String[] args) {
        BigInteger primeNumber = new BigInteger("9001"); // Example prime modulus
        ClassicBarrettReducerBaseTen reducer = new ClassicBarrettReducerBaseTen(primeNumber);

        // Example from the documentation
        BigInteger x = new BigInteger("486092");
        BigInteger reduced = reducer.reduce(x);
        // The correct reduced value should be: 486092 - 54 * 9001 = 38
        System.out.println("Reduced value of " + x + ": " + reduced);
        System.out.println("==================================");
    }
}