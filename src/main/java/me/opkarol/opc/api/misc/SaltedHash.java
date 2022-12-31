package me.opkarol.opc.api.misc;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SaltedHash {
    private static final int SALT_LENGTH = 16; // 16 bytes = 128 bits
    private static final String HASH_ALGORITHM = "SHA-256";

    public static @NotNull String encode(@NotNull String customSalt, String password) {
        return encode(repeat(customSalt, SALT_LENGTH), password);
    }

    public static @NotNull String encode(byte[] salt, String password) {
        // Compute the hash of the password and salt
        byte[] hash = computeHash(password, salt);

        // Concatenate the salt and hash and return the result as a string
        return byteArrayToHexString(salt) + byteArrayToHexString(hash);
    }

    public static @NotNull String encode(String password) {
        // Generate a random salt
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);

        return encode(salt, password);
    }

    public static byte @NotNull [] repeat(@NotNull String str, int size) {
        // Convert the string to a byte array
        byte[] bytes = str.getBytes();

        // Create an empty list of bytes
        List<Byte> result = new ArrayList<>();

        // Append the string to the list until it reaches the desired size
        while (result.size() < size) {
            for (byte b : bytes) {
                result.add(b);
                if (result.size() >= size) {
                    break;
                }
            }
        }

        // Convert the list to a byte array and return it
        Byte[] boxed = result.toArray(new Byte[0]);
        return toPrimitive(boxed);
    }

    @Contract(pure = true)
    private static byte @NotNull [] toPrimitive(Byte @NotNull [] array) {
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    public static boolean verify(String password, @NotNull String encoded) {
        // Split the encoded string into the salt and hash parts
        String saltString = encoded.substring(0, SALT_LENGTH * 2);
        String hashString = encoded.substring(SALT_LENGTH * 2);
        byte[] salt = hexStringToByteArray(saltString);
        byte[] hash = hexStringToByteArray(hashString);

        // Compute the hash of the password and salt and compare it to the stored hash
        byte[] expectedHash = computeHash(password, salt);

        return Arrays.equals(hash, expectedHash);
    }

    private static byte[] computeHash(@NotNull String password, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update(salt);
            byte[] passwordBytes = password.getBytes();
            return md.digest(passwordBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    private static @NotNull String byteArrayToHexString(byte @NotNull [] array) {
        StringBuilder sb = new StringBuilder();
        for (byte b : array) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private static byte @NotNull [] hexStringToByteArray(@NotNull String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
