package bruteforce;

import java.util.Objects;

public class PlaintextCiphertextPair {
    private final String plaintext;
    private final String ciphertext;

    public PlaintextCiphertextPair(String plaintext, String ciphertext) {
        Objects.requireNonNull(plaintext);
        Objects.requireNonNull(ciphertext);
        if (plaintext.length() != 8 || ciphertext.length() != 8)
            throw new IllegalArgumentException("Plaintext and ciphertext must be exactly 8 bits each");
        this.plaintext = plaintext;
        this.ciphertext = ciphertext;
    }

    public static PlaintextCiphertextPair of(String pair) {
        String[] parts = pair.split("\\s+");
        return new PlaintextCiphertextPair(parts[0], parts[1]);
    }

    public String getPlaintext() {
        return plaintext;
    }

    public String getCiphertext() {
        return ciphertext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaintextCiphertextPair that = (PlaintextCiphertextPair) o;
        return plaintext.equals(that.plaintext) && ciphertext.equals(that.ciphertext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(plaintext, ciphertext);
    }
}
