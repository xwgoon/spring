package com.myapp.service.util.md5;

import org.apache.commons.codec.digest.DigestUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Test {

    public static void main(String[] args) {
        String str = "ILoveJava";
        String result = "35454B055CC325EA1AF2126E27707052";
//        String hash = md5(str);
        String hash = md5(new File("C:\\Users\\Administrator\\Desktop\\manager_library_detail_0.xlsx"));
        System.out.println(hash);
        String hash2 = md5(new File("C:\\Users\\Administrator\\Desktop\\manager_library_detail_1.xlsx"));
        System.out.println(hash2);
        System.out.println(hash.equals(hash2));

    }

    private static String md5(String str) {
        return md5(str.getBytes());
    }

    private static String md5(File file) {
        try {
            return md5(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String md5(byte[] bytes) {
        //一、jdk方式
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(bytes);
//            byte[] digest = md.digest();
//            return DatatypeConverter.printHexBinary(digest);
//        } catch (NoSuchAlgorithmException e) {
//            throw new IllegalArgumentException(e);
//        }

        //二、Apache Commons方式
        return DigestUtils.md5Hex(bytes);
    }


}
