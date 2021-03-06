import java.nio.charset.StandardCharsets;

/**
 * Author: Nils Sohn
 * FeistelNetwork consists of 16 rounds.
 * Each round performs identical operations.
 */
class FeistelNetwork {
    /* Initialize lookup tables */

    private static final int[][] Expansion =
            {
                    {32, 1, 2, 3, 4, 5},
                    {4, 5, 6, 7, 8, 9},
                    {8, 9, 10, 11, 12, 13},
                    {12, 13, 14, 15, 16, 17},
                    {16, 17, 18, 19, 20, 21},
                    {20, 21, 22, 23, 24, 25},
                    {24, 25, 26, 27, 28, 29},
                    {28, 29, 30, 31, 32, 1}
            };
    private static final int[][][] SBoxes =
            {
                    {
                            {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                            {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                            {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                            {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
                    },
                    {
                            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
                            {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                            {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                            {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}
                    },
                    {
                            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                            {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                            {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                            {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
                    },
                    {
                            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                            {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                            {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                            {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}
                    },
                    {
                            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                            {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                            {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                            {11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
                    },
                    {
                            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                            {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                            {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                            {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
                    },
                    {
                            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                            {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                            {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                            {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
                    },
                    {
                            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                            {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                            {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                            {2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
                    }
            };
    private static final int[][] PPermutation =
            {
                    {16, 7, 20, 21, 29, 12, 28, 17},
                    {1, 15, 23, 26, 5, 18, 31, 10},
                    {2, 8, 24, 14, 32, 27, 3, 9},
                    {19, 13, 30, 6, 22, 11, 4, 25},
            };

    /**
     * The main part of DES encryption
     * In each round, left-half of the input is XORed with the output
     * of the f function, and then the two halves are swapped.
     *
     * @param input   64-bit output of IP
     * @param subkeys an array of 16 subkeys
     * @return a 64-bit string generated after round 16.
     */
    public static String iterate(String input, String[] subkeys) {
        String left, right;
        int iterationCount = 0;
        left = input.substring(input.length() / 2);
        right = input.substring(0, input.length() / 2);
        while (iterationCount < 16) {
            right = xor(right, fFunction(left, subkeys[iterationCount++]));
        }
        return left + right;
    }

    /**
     * F function - expands r from 32-bit to 48-bit. The result is XORed
     * with the key. That result is divided into 8 6-bit blocks and fed into
     * 8 different substitution boxes that each output 4-bit blocks. The 8
     * 4-bit blocks are combined to be 32-bit and fed into permutation P
     *
     * @param r   - 32-bit right half of the input used for this round
     * @param key - 48-bit subkey used for this round
     * @return a 32-bit output F(r, key)
     */
    public static String fFunction(String r, String key) {
        return pPermutation(sBoxes(xor(expansion(r), key)));
    }

    /**
     * Performs the XOP operation between 2 equal length strings
     *
     * @param firstInput  - first string to XOR
     * @param secondInput - second string to XOR
     * @return resulting string of the XOR operation
     */
    public static String xor(String firstInput, String secondInput) {
        char[] firstInputBits = firstInput.toCharArray();
        char[] secondInputBits = secondInput.toCharArray();
        char[] finalBits = new char[firstInputBits.length];

        // Set bit to 1 if two xored bits are different
        for (int index = 0; index < firstInputBits.length; index++) {
            if (firstInputBits[index] != secondInputBits[index]) {
                finalBits[index] = '1';
            } else {
                finalBits[index] = '0';
            }
        }
        return new String(finalBits);
    }

    /**
     * Expand 32-bit input to 48-bit result using the expansion substitution table
     *
     * @param r - right half of the input to expand
     * @return a 48-bit string
     */
    public static String expansion(String r) {
        char[] expandedBits = new char[48];
        char[] rBits = r.toCharArray();


        for (int index = 0; index < 48; index++) {
            expandedBits[index] = rBits[Expansion[index / 6][index % 6] - 1];
        }
        return new String(expandedBits);
    }

    /**
     * Divide the 48-bit input into 8 6-bit blocks and feed those blocks into
     * 8 different substitution boxes that each output 4-bit blocks. The 8
     * 4-bit blocks are combined to be a 32-bit string
     *
     * @param input - 48-bit string to feed into the S-boxes
     * @return a 32-bit string; combined outputs of the S-boxes
     */
    public static String sBoxes(String input) {
        StringBuilder output = new StringBuilder();
        String sixBits, buffer;
        int x = 0;
        int y = 0;
        int sBoxVal = 0;

        for (int index = 0; index < 48; index += 6) {
            sixBits = input.substring(index, index + 6);
            buffer = sixBits.charAt(0) + sixBits.substring(5, 6);
            x = Integer.parseInt(buffer, 2);
            buffer = sixBits.substring(1, 5);
            y = Integer.parseInt(buffer, 2);
            buffer = formatIntToPaddedBinaryString(SBoxes[sBoxVal][x][y], 4);
            output.append(buffer);
        }
        return output.toString();
    }

    /**
     * Permutes 32-bit input using the P permutation table
     *
     * @param i      - integer that needs to be converted to binary format
     * @param length - 32-bit string to feed into the P permutation table
     * @return a 32-bit string
     */
    private static String formatIntToPaddedBinaryString(int i, int length) {
        return String.format("%" + length + "s", Integer.toBinaryString(1)).replace(' ', '0');
    }


    /**
     * Permutes 32-bit input using the P permutation table
     *
     * @param input - 32-bit string to feed into the P permutation table
     * @return a 32-bit string
     */
    public static String pPermutation(String input) {
        char[] output = new char[32];
        for (int index = 0; index < 32; index++) {
            output[index] = input.charAt(PPermutation[index / 8][index % 8] - 1);
        }
        return new String(output);
    }
}
