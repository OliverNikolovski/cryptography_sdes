package main;

import bruteforce.PlaintextCiphertextPair;
import bruteforce.SdesUtility;
import sdes.Sdes;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        if (args[0].equals("brute-force")) {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter plaintext-ciphertext pairs separated by space, one pair per line (Send EOF signal when done):");

            Set<PlaintextCiphertextPair> pairs =
                    br.lines()
                            .map(PlaintextCiphertextPair::of)
                            .collect(Collectors.toUnmodifiableSet());

            Set<String> keys =
                    pairs.stream()
                            .map(pair -> SdesUtility.bruteForce(pair.getPlaintext(), pair.getCiphertext()))
                            .reduce((acc, el) -> (HashSet<String>) intersection(acc, el)).orElse(Collections.emptySet());

            System.out.println("Keys:");
            keys.forEach(System.out::println);
        }
        else if (args[0].equals("encrypt")) {
            Sdes.main(args);
        }
        else {
            System.err.println("Unknown option: " + args[0]);
            System.exit(1);
        }

    }

    public static <T> Collection<T> intersection(Collection<T> c1, Collection<T> c2) {
        c1.retainAll(c2);
        return c1;
    }

}
