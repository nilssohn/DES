/**
IP - Initial Permutation
The first operation of DES encryption
*/
class IP{
   /**
   IP lookup table
   */
   private static final int[][] InitialPermutation = 
   {
   {58,50,42,34,26,18,10,2},
   {60,52,44,36,28,20,12,4},
   {62,54,46,38,30,22,14,6},
   {64,56,48,40,32,24,16,8},
   {57,49,41,33,25,17,9,1},
   {59,51,43,35,27,19,11,3},
   {61,53,45,37,29,21,13,5},
   {63,55,47,39,31,23,15,7}
   };
   
   /**
   Use the lookup table to produce permuted bits
   pre-condition: input is a binary string of 64-bits
   @param bits a String of 64 bits
   @return permuted string of 64 bits
   */   
   public static String permute(String bits){
      assert bits.length() == 64;
      
      String result = "";
      for(int row=0; row<8; row++){
         for(int col =0; col<8; col++){
            result += bits.charAt(InitialPermutation[row][col]-1);
          }
      }
      return result;
   }
   
}