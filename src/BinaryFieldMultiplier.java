import java.math.BigInteger;

public class BinaryFieldMultiplier {

    public static BigInteger multiplyPolynomials(BigInteger poly1, BigInteger poly2, int wordSize) {
        // Convert polynomials to binary strings
        String binaryPoly1 = poly1.toString(2);
        String binaryPoly2 = poly2.toString(2);

        //Calculate number of words needed for the polynomials
        int maxLength = Math.max(binaryPoly1.length(), binaryPoly2.length());
        int s = (int) Math.ceil((double)maxLength / wordSize);

        // Pad polynomials with leading zeros if necessary
        binaryPoly1 = padWithZeros(binaryPoly1, s * wordSize);
        binaryPoly2 = padWithZeros(binaryPoly2, s * wordSize);
        System.out.println("a: " + binaryPoly1);
        System.out.println("b: " + binaryPoly2);
        System.out.println("=============================================");

        // Initialize intermediate result
        BigInteger result = BigInteger.ZERO;

        for (int j = 0; j < wordSize; j++){
            for (int i = 0; i < s; i++){
                // Adding an if that isn't in the pseudocode for some optimization (no need to do anything if b_i is 0)
                if (binaryPoly2.charAt(s * wordSize - (wordSize * i + j) - 1) == '1'){
                    result = result.xor(poly1.shiftLeft(wordSize * i));
                }
            }
            System.out.println("Result after iteration j = " + j + " : " + result.toString(2));
            poly1 = poly1.shiftLeft(1);
        }
        System.out.println("=============================================");
        return result;
    }

    private static String padWithZeros(String str, int length) {
        int curr_length = str.length();
        if(curr_length == length)
            return str;
        StringBuilder padded = new StringBuilder();
        int num_zeros = length - curr_length; // How many zero's need to be added
        for(int i = 0; i < num_zeros; i++)
            padded.append('0');
        padded.append(str);
        return padded.toString();
    }

    public static void main(String[] args) {
        BigInteger poly1 = new BigInteger("1011101", 2); // x^6 + x^4 + x^3 + x^2 + 1
        BigInteger poly2 = new BigInteger("110110", 2); // x^5 + x^4 + x^2 + x
        int wordSize = 4; // Processor word size in bits

        BigInteger result = multiplyPolynomials(poly1, poly2, wordSize);
        System.out.println("Result: " + result.toString(2)); // Result as bit string

    }
}