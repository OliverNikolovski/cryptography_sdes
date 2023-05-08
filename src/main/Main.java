package main;

import bruteforce.PlaintextCiphertextPair;
import bruteforce.SdesUtility;
import sdes.Sdes;
import sdes.SdesOperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        // if mode == brute force attack
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
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

        // else if mode == encryption
        //Sdes.main(args);
    }

    public static <T> Collection<T> intersection(Collection<T> c1, Collection<T> c2) {
        c1.retainAll(c2);
        return c1;
    }

}
