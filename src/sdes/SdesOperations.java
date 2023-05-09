package sdes;

import java.util.BitSet;

public class SdesOperations extends BitSet {

    private int n;

    public SdesOperations(int size) {
        super(size);
        n = size;
    }

    public static SdesOperations of(String message) {
        SdesOperations bits = new SdesOperations(message.length());
        char[] chars = message.toCharArray();
        for (int i = 0; i < chars.length; i++)
            if (chars[i] == '1') bits.set(i);

        return bits;
    }

    public int getSize() {
        return n;
    }

    public void swapHalves() {
        if ((n % 2) == 1) {
            System.err.println("SDESBits.swapHalves( ): Swapping halves on odd-length bit string");
        }
        SdesOperations temp = (SdesOperations) clone();
        for (int i = 0; i < n / 2; i++) {
            set(i, temp.get(i + n / 2));
            set(i + n / 2, temp.get(i));
        }
    }

    public void leftShift(int start, int stop) {
        boolean temp = get(start);
        for (int i = start; i < stop; i++) {
            set(i, get(i + 1));
        }
        set(stop, temp);
    }


    public void permute(int[] aperm) {
        // aperm is the bottom row a 2xn representation
        // of a permuation as an array of integer indices
        SdesOperations temp = (SdesOperations) clone();
        for (int i = 0; i < n; i++) {
            set(i, temp.get(aperm[i]));
        }
    }

    public void remove(int somebit) {
        // remove a bit, scoot others down one slot
        for (int i = somebit; i < (n - 1); i++) {
            set(i, get(i + 1));
        }
        n--;
    }

    public void expansion() {
        if (n != 4) {
            System.err.println("SDESBits.expansion( ): Operating on input with length unequal to 4");
        }
        SdesOperations temp = (SdesOperations) clone();
        n = 8;
        set(0, temp.get(3));
        set(1, temp.get(0));
        set(2, temp.get(1));
        set(3, temp.get(2));
        set(4, temp.get(1));
        set(5, temp.get(2));
        set(6, temp.get(3));
        set(7, temp.get(0));
    }


    public void leftHalf() {
        if (n != 8) {
            System.err.println("SDESBits.leftHalf( ): Operating on input with length unequal to 8");
        }
        n = 4;
    }

    public void rightHalf() {
        if (n != 8) {
            System.err.println("SDESBits.rightHalf( ): Operating on input with length unequal to 8");
        }
        n = 4;
        for (int i = 0; i < 4; i++) {
            set(i, get(i + 4));
        }
    }

    public void sboxzero() {
        if (n != 4) {
            System.err.println("SDESBits.sboxzero( ): Operating on input with length unequal to 4");
        }
        SdesOperations temp = (SdesOperations) clone();
        n = 2;
        int[][] box = {{1, 0, 3, 2}, {3, 2, 1, 0}, {0, 2, 1, 3}, {3, 1, 3, 2}};
        int row = 0;
        if (temp.get(0)) {
            row = row + 2;
        }
        if (temp.get(3)) {
            row = row + 1;
        }
        int col = 0;
        if (temp.get(1)) {
            col = col + 2;
        }
        if (temp.get(2)) {
            col = col + 1;
        }
        int result = box[row][col];
        set(0, (result / 2) == 1);
        set(1, (result % 2) == 1);
    }

    public void sboxone() {
        if (n != 4) {
            System.err.println("SDESBits.sboxone( ): Operating on input with length unequal to 4");
        }
        SdesOperations temp = (SdesOperations) clone();
        n = 2;
        int[][] box = {{0, 1, 2, 3}, {2, 0, 1, 3}, {3, 0, 1, 0}, {2, 1, 0, 3}};
        int row = 0;
        if (temp.get(0)) {
            row = row + 2;
        }
        if (temp.get(3)) {
            row = row + 1;
        }
        int col = 0;
        if (temp.get(1)) {
            col = col + 2;
        }
        if (temp.get(2)) {
            col = col + 1;
        }
        int result = box[row][col];
        set(0, (result / 2) == 1);
        set(1, (result % 2) == 1);
    }

    public void xor(SdesOperations bits) {
        if (this.n != bits.getSize()) {
            System.err.println("SDESBits.xor( ): XOR'ing inputs with unequal lengths");
        }
        super.xor(bits);
    }

    public void append(SdesOperations bits) {
        int m = bits.getSize();
        for (int i = 0; i < m; i++) {
            set(n + i, bits.get(i));
        }
        n = n + m;
    }

    public Object clone() {
        SdesOperations acopy = (SdesOperations) super.clone();
        acopy.n = this.n;
        return acopy;
    }

    public String toString() {
        StringBuilder outstring = new StringBuilder();
        for (int i = 0; i < n; i++) {
            if (get(i)) {
                outstring.append('1');
            } else {
                outstring.append('0');
            }
        }
        return outstring.toString();
    }



}

