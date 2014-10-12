package com.dominion.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IOUtils {

    public static String readFile(String path, Charset encoding) {
        String res = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(path));
            res = new String(encoded, encoding);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

}
