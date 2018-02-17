/**
 * Author: Nils Sohn
 */
class KeySchedule {
    /* Initialize lookup tables for PC_1 and PC_2 */
    private static final int[][] PC_1 =
            {{57, 49, 41, 33, 25, 17, 9, 1},
                    {58, 50, 42, 34, 26, 18, 10, 2},
                    {59, 51, 43, 35, 27, 19, 11, 3},
                    {60, 52, 44, 36, 63, 55, 47, 39},
                    {31, 23, 15, 7, 62, 54, 46, 38},
                    {30, 22, 14, 6, 61, 53, 45, 37},
                    {29, 21, 13, 5, 28, 20, 12, 4}};
    private static final int[][] PC_2 =
            {{14, 17, 11, 24, 1, 5, 3, 28},
                    {15, 6, 21, 10, 23, 19, 12, 4},
                    {26, 8, 16, 7, 27, 20, 13, 2},
                    {41, 52, 31, 37, 47, 55, 30, 40},
                    {51, 45, 33, 48, 44, 49, 39, 56},
                    {34, 53, 46, 42, 50, 36, 29, 32}};

    /**
     * Permuted Choice - 1
     *
     * @param key 64-bit initial key
     * @return 56-bit string to be fed to 16 rounds of transformation
     */
    public static String pc_1(String key) {
        char[] output = new char[56];
        for (int index = 0; index < 56; index++) {
            output[index] = key.charAt(PC_1[index / 8][index % 8] - 1);
        }

        return new String(output);
    }

    /**
     * Permuted Choice - 2
     *
     * @param input 56-bit string generated by a transform
     * @return 48-bit subkey
     */
    public static String pc_2(String input) {
        char[] output = new char[48];
        for (int index = 0; index < 48; index++) {
            output[index] = input.charAt(PC_2[index / 8][index % 8] - 1);
        }

        return new String(output);
    }

    /**
     * The key schedule for encryption generates 16 subkeys from main key
     *
     * @param key main key of 64 bits
     * @return an array of 16 subkeys
     */
    public static String[] generateSubkeysForEncryption(String key) {
        String[] subkeys = new String[16];
        String firstOutput = pc_1(key);
        String c = firstOutput.substring(0, 28);
        String d = firstOutput.substring(28);

        for (int round = 1; round <= 16; round++) {
            if (round == 1 || round == 2 || round == 9 || round == 16) {
                // left shift by 1 bit
                c = c.substring(1) + c.charAt(0);
                d = d.substring(0, 27) + d.charAt(0);
            } else {
                // left shift by 2 bits
                c = d.substring(2) + c.substring(0, 2);
                d = d.substring(2) + d.substring(0, 2);
            }
            // combine c+d, PC-2 substitution, store in subkeys array
            subkeys[round - 1] = pc_2(c + d);
        }
        return subkeys;
    }

    /**
     * The key schedule for encryption generates 16 subkeys from main key
     *
     * @param key main key of 64 bits
     * @return an array of 16 subkeys
     */
    public static String[] generateSubkeysForDecryption(String key) {
        String[] subkeys = new String[16];
        String firstOutput = pc_1(key);
        String c = firstOutput.substring(0, 28);
        String d = firstOutput.substring(28);

        for (int round = 1; round <= 16; round++) {
            if (round == 1) {
                // don't shift
            } else if (round == 2 || round == 9 || round == 16) {
                // right shift by 1 bit
                c = c.charAt(27) + c.substring(0, 27);
                d = d.charAt(27) + d.substring(0, 27);
            } else {
                // right shift by 2 bits
                c = c.substring(26, 28) + c.substring(0, 26);
                d = d.substring(26, 28) + d.substring(0, 26);
            }
            // combine c+d, PC-2 substitution, store in subkeys array
            subkeys[round - 1] = pc_2(c + d);
        }
        return subkeys;
    }
}