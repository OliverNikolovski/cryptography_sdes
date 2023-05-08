package bruteforce;

import sdes.SdesOperations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SdesUtility {

    // The intermediate steps are referenced by an enumerated type
    // For each step, there is a constant, and an SDES bit string
    // containing the result
    // MAX_OUT is the total number of steps here
    public static final int P10KEY = 0;
    public static final int SHIFTKEY5 = 1;
    public static final int P8KEY1 = 2;
    public static final int SHIFTKEY2 = 3;
    public static final int P8KEY2 = 4;
    public static final int IPMESSAGE = 5;
    public static final int RIGHTHALFR1 = 6;
    public static final int EXPANSIONR1 = 7;
    public static final int XORKR1 = 8;
    public static final int LEFTBOXR1 = 9;
    public static final int LEFTSBOX0R1 = 10;
    public static final int RIGHTBOXR1 = 11;
    public static final int RIGHTSBOX1R1 = 12;
    public static final int JOINNSBOXR1 = 13;
    public static final int P4R1 = 14;
    public static final int LEFTHALFR1 = 15;
    public static final int XORFR1 = 16;
    public static final int REPLACELEFTR1 = 17;
    public static final int SWAPHALVES = 18;
    public static final int RIGHTHALFR2 = 19;
    public static final int EXPANSIONR2 = 20;
    public static final int XORKR2 = 21;
    public static final int LEFTBOXR2 = 22;
    public static final int LEFTSBOX0R2 = 23;
    public static final int RIGHTBOXR2 = 24;
    public static final int RIGHTSBOX1R2 = 25;
    public static final int JOINNSBOXR2 = 26;
    public static final int P4R2 = 27;
    public static final int LEFTHALFR2 = 28;
    public static final int XORFR2 = 29;
    public static final int REPLACELEFTR2 = 30;
    public static final int IPINV = 31;
    public static final int NUM_STEPS = 32;

    public static String encrypt(String messageAsString, String keyAsString) {
        SdesOperations message = SdesOperations.of(messageAsString);
        SdesOperations key = SdesOperations.of(keyAsString);

        SdesOperations[] bits = new SdesOperations[NUM_STEPS];

        // Convenience variable, runs through intermediate steps enumerated type
        int inter;

        // Various fixed permutations that are constant in the SDES algorithm
        final int[] ip = {1, 5, 2, 0, 3, 7, 4, 6};
        final int[] ipinv = {3, 0, 2, 4, 6, 1, 7, 5};
        final int[] p8 = {0, 1, 5, 2, 6, 3, 7, 4, 9, 8};
        final int[] p10 = {2, 4, 1, 6, 3, 9, 0, 8, 7, 5};
        final int[] p4 = {1, 3, 2, 0};



        // Begin key generation

        // Permute 10 bits of key by P10
        inter = P10KEY;
        bits[inter] = (SdesOperations) key.clone();
        bits[inter].permute(p10);

        // Circular left shift, once, on each half
        inter = SHIFTKEY5;
        bits[inter] = (SdesOperations) bits[P10KEY].clone();
        bits[inter].leftShift(0, 4);
        bits[inter].leftShift(5, 9);

        // P8 permutation, drop leading two bits, result is K1
        inter = P8KEY1;
        bits[inter] = (SdesOperations) bits[SHIFTKEY5].clone();
        bits[inter].permute(p8);
        bits[inter].remove(0);
        bits[inter].remove(0);

        // Now two more circular shifts of full key
        inter = SHIFTKEY2;
        bits[inter] = (SdesOperations) bits[SHIFTKEY5].clone();
        bits[inter].leftShift(0, 4);
        bits[inter].leftShift(0, 4);
        bits[inter].leftShift(5, 9);
        bits[inter].leftShift(5, 9);

        // P8 permutation, drop leading two bits, result is K2
        inter = P8KEY2;
        bits[inter] = (SdesOperations) bits[SHIFTKEY2].clone();
        bits[inter].permute(p8);
        bits[inter].remove(0);
        bits[inter].remove(0);

        // End key generation

        // Permute whole message with P10
        inter = IPMESSAGE;
        bits[inter] = (SdesOperations) message.clone();
        bits[inter].permute(ip);

        // Begin Round 1

        // Grab right half of permuted message text
        inter = RIGHTHALFR1;
        bits[inter] = (SdesOperations) bits[IPMESSAGE].clone();
        bits[inter].rightHalf();

        // Expand right half to 8 bits
        inter = EXPANSIONR1;
        bits[inter] = (SdesOperations) bits[RIGHTHALFR1].clone();
        bits[inter].expansion();

        // XOR with key 1
        inter = XORKR1;
        bits[inter] = (SdesOperations) bits[EXPANSIONR1].clone();
        bits[inter].xor(bits[P8KEY1]);

        // Left half for S0
        inter = LEFTBOXR1;
        bits[inter] = (SdesOperations) bits[XORKR1].clone();
        bits[inter].leftHalf();

        // Left half thru S0
        inter = LEFTSBOX0R1;
        bits[inter] = (SdesOperations) bits[LEFTBOXR1].clone();
        bits[inter].sboxzero();

        // Right half for S1
        inter = RIGHTBOXR1;
        bits[inter] = (SdesOperations) bits[XORKR1].clone();
        bits[inter].rightHalf();

        // Right half thru S1
        inter = RIGHTSBOX1R1;
        bits[inter] = (SdesOperations) bits[RIGHTBOXR1].clone();
        bits[inter].sboxone();

        // Join halves from S-boxes
        inter = JOINNSBOXR1;
        bits[inter] = (SdesOperations) bits[LEFTSBOX0R1].clone();
        bits[inter].append(bits[RIGHTSBOX1R1]);

        // Permute 4-bit, creates big F
        inter = P4R1;
        bits[inter] = (SdesOperations) bits[JOINNSBOXR1].clone();
        bits[inter].permute(p4);

        // Grab left half of permuted message text
        inter = LEFTHALFR1;
        bits[inter] = (SdesOperations) bits[IPMESSAGE].clone();
        bits[inter].leftHalf();

        // XOR left half of message with result of big F
        inter = XORFR1;
        bits[inter] = (SdesOperations) bits[LEFTHALFR1].clone();
        bits[inter].xor(bits[P4R1]);

        // Keep left half as is, restore righthalf
        inter = REPLACELEFTR1;
        bits[inter] = (SdesOperations) bits[XORFR1].clone();
        bits[inter].append(bits[RIGHTHALFR1]);

        // End Round 1

        // Swap halves , acts as  IPMESSAGE  in round 2 code recycle
        inter = SWAPHALVES;
        bits[inter] = (SdesOperations) bits[REPLACELEFTR1].clone();
        bits[inter].swapHalves();

        // Begin Round 2

        // Grab right half of permuted message text
        inter = RIGHTHALFR2;
        bits[inter] = (SdesOperations) bits[SWAPHALVES].clone();
        bits[inter].rightHalf();

        // Expand right half to 8 bits
        inter = EXPANSIONR2;
        bits[inter] = (SdesOperations) bits[RIGHTHALFR2].clone();
        bits[inter].expansion();

        // XOR with key 2
        inter = XORKR2;
        bits[inter] = (SdesOperations) bits[EXPANSIONR2].clone();
        bits[inter].xor(bits[P8KEY2]);

        // Left half for S0
        inter = LEFTBOXR2;
        bits[inter] = (SdesOperations) bits[XORKR2].clone();
        bits[inter].leftHalf();

        // Left half thru S0
        inter = LEFTSBOX0R2;
        bits[inter] = (SdesOperations) bits[LEFTBOXR2].clone();
        bits[inter].sboxzero();

        // Right half for S1
        inter = RIGHTBOXR2;
        bits[inter] = (SdesOperations) bits[XORKR2].clone();
        bits[inter].rightHalf();

        // Right half thru S1
        inter = RIGHTSBOX1R2;
        bits[inter] = (SdesOperations) bits[RIGHTBOXR2].clone();
        bits[inter].sboxone();

        // Join halves from S-boxes
        inter = JOINNSBOXR2;
        bits[inter] = (SdesOperations) bits[LEFTSBOX0R2].clone();
        bits[inter].append(bits[RIGHTSBOX1R2]);

        // Permute 4-bit, creates big F
        inter = P4R2;
        bits[inter] = (SdesOperations) bits[JOINNSBOXR2].clone();
        bits[inter].permute(p4);

        // Grab left half of permuted message text
        inter = LEFTHALFR2;
        bits[inter] = (SdesOperations) bits[SWAPHALVES].clone();
        bits[inter].leftHalf();

        // XOR left half of message with result of big F
        inter = XORFR2;
        bits[inter] = (SdesOperations) bits[LEFTHALFR2].clone();
        bits[inter].xor(bits[P4R2]);

        // Keep left half as is, restore righthalf
        inter = REPLACELEFTR2;
        bits[inter] = (SdesOperations) bits[XORFR2].clone();
        bits[inter].append(bits[RIGHTHALFR2]);

        // End Round 2

        // Apply the inverse of P10 to finish
        inter = IPINV;
        bits[inter] = (SdesOperations) bits[REPLACELEFTR2].clone();
        bits[inter].permute(ipinv);

        return bits[inter].toString();
    }

    public static Set<String> bruteForce(String plaintext, String ciphertext) {
        Set<String> possibleKeys = new HashSet<>();
        for (int i = 0; i <= 1023; i++) {
            String key = paddedKey(Integer.toBinaryString(i));
            String encryptedMessage = encrypt(plaintext, key);
            if (encryptedMessage.equals(ciphertext))
                possibleKeys.add(key);
        }
        return possibleKeys;
    }

    // pads key with zeroes from the left
    private static String paddedKey(String key) {
        return String.format("%10s", key).replace(' ', '0');
    }
}
