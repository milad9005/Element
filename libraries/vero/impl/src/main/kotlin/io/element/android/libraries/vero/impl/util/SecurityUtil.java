package io.element.android.libraries.vero.impl.util;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VIDEO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Base64;

import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.Nullable;
import org.spongycastle.asn1.ASN1Primitive;
import org.spongycastle.asn1.x509.SubjectPublicKeyInfo;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.cert.X509Certificate;


/**
 * Created by Conor on 17/06/2015.
 */
public class SecurityUtil {
    private static final String TAG = SecurityUtil.class.getSimpleName();

    public static final int REQUEST_CODE_ASK_PERMISSIONS = 123;

    // md5 on Android will sometimes generate less than 32 digits
    // in which case we append with 0's.
    private static final int HASH_LENGTH = 32;


    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String hex = Integer.toHexString(0xFF & aMessageDigest);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String sha256(String base) {
        return sha256(base, "UTF-8");
    }

    public static String sha256(String base, String charset) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(charset));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] sha256Bytes(String base, String charset) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(base.getBytes(charset));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Base64
    public static String encryptBase64(String input) {
        // Simple encryption, not very strong!
        return Base64.encodeToString(input.getBytes(), Base64.DEFAULT);
    }

    public static String decryptBase64(String input) {
        return new String(Base64.decode(input, Base64.DEFAULT));
    }

    // AES
    public static byte[] encryptAES(String plainText, String encryptionKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
        byte[] iv = new byte[cipher.getBlockSize()];
        IvParameterSpec ivParams = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
        return cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
    }

    public static String decryptAES(byte[] input, String secretKey) {
        String result = null;
        Cipher cipher;
        try {
            Key key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            // The block size (in bytes), or 0 if the underlying algorithm is not a block cipher
            byte[] ivByte = new byte[cipher.getBlockSize()];
            // This class specifies an initialization vector (IV). Examples which use
            // IVs are ciphers in feedback mode, e.g., DES in CBC mode and RSA ciphers with OAEP encoding operation.
            IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParamsSpec);
            result = new String(cipher.doFinal(input));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    public static String decryptRSAPKS1(PublicKey publicKey) {
        try {
            final SubjectPublicKeyInfo spkInfo = SubjectPublicKeyInfo.getInstance(publicKey.getEncoded());
            final ASN1Primitive primitive = spkInfo.parsePublicKey();
            final byte[] pubPKCS1 = primitive.getEncoded();
            final String key = Base64.encodeToString(pubPKCS1, Base64.NO_WRAP);
            return key;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // size passed is 256
    public static String generateAESForCalls(int size) {
        final byte[] aesKeyData  = new byte[size / Byte.SIZE];
        new SecureRandom().nextBytes(aesKeyData);
        final SecretKey aesKey = new SecretKeySpec(aesKeyData, "AES");
        return bytesToHex(aesKey.getEncoded());
    }

    public static String bytesToHex(final byte[] data) {
        final StringBuilder sb = new StringBuilder(data.length * 2);
        for (final byte b : data) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static byte[] hexToBytes(String hex) {
        int l = hex.length();
        byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

   /* public static String generateAESkey(int size) {
        SecureRandom rand = new SecureRandom();
        KeyGenerator generator;
        try {
            generator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        generator.init(size, rand);
        SecretKey secretKey = generator.generateKey();
        try {
            return new String(org.apache.commons.codec.binary.Base64.encodeBase64(secretKey.getEncoded()));
        } catch (Exception e) {
            return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
        }
    }*/

    //region Permissions
    public static boolean isGPSPermissionGranted(Context context) {
        return arePermissionsGranted(context, ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION);
    }

    public static boolean isCoarseLocationPermissionGranted(Context context) {
        return arePermissionsGranted(context, ACCESS_COARSE_LOCATION);
    }

    public static boolean isFineLocationPermissionGranted(Context context) {
        return arePermissionsGranted(context, ACCESS_FINE_LOCATION);
    }

    public static boolean isStorageReadPermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            return arePermissionsGranted(context, READ_EXTERNAL_STORAGE);
        } else {
            return arePermissionsGranted(context, READ_MEDIA_IMAGES, READ_MEDIA_VIDEO);
        }
    }

    public static boolean isStorageWritePermissionGranted(Context context) {
        return arePermissionsGranted(context, WRITE_EXTERNAL_STORAGE);
    }

    public static boolean isCameraAndStorageWritePermissionGranted(Context context) {
        return arePermissionsGranted(context, CAMERA, WRITE_EXTERNAL_STORAGE);
    }

    public static boolean isCameraPermissionGranted(Context context) {
        return arePermissionsGranted(context, CAMERA);
    }

    public static boolean isContactReadPermissionGranted(Context context) {
        return arePermissionsGranted(context, READ_CONTACTS);
    }

    private static boolean arePermissionsGranted(Context context, String... permissions) {
        if (context == null) {
            return false;
        }

        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ActivityCompat.checkSelfPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }
    //endregion

    // HMAC algorithm

    public static String generateHashWithHmac256(String message, String key) {
        try {
            final String hashingAlgorithm = "HmacSHA256"; //or "HmacSHA1", "HmacSHA512"

            byte[] bytes = hmac(hashingAlgorithm, hexStringToByteArray(message), hexStringToByteArray(key));

            final String messageDigest = bytesToHex(bytes);


            return messageDigest;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("SameParameterValue") // algorithm can also one of HmacSHA256, HmacSHA1, HmacSHA512.
    private static byte[] hmac(String algorithm, byte[] message, byte[] key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key, algorithm));
        mac.update(message);
        return mac.doFinal();
    }

    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.length() % 2 != 0 ? "0" + hexString : hexString;
        byte[] b = new byte[hexString.length() / 2];
        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hexString.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

    public static String getCertPin(X509Certificate cert) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        digest.reset();

        final byte[] spki = cert.getPublicKey().getEncoded();
        final byte[] spkiHash = digest.digest(spki);
        return Base64.encodeToString(spkiHash, Base64.DEFAULT).trim();
    }
}
