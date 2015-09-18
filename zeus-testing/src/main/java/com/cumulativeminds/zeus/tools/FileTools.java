package com.cumulativeminds.zeus.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.springframework.util.StreamUtils;

public class FileTools {

    public static void main(String[] args) throws Exception {
        FileTools tools = new FileTools();
        tools.doIt(args[0], args[1], args[2]);
    }

    private void doIt(String action, String infile, String outfile) throws IOException {
        if ("decode".equals(action)) {
            decodeBase64(infile, outfile);
        }
    }

    private void decodeBase64(String infile, String outfile) throws IOException {
        FileInputStream fin = new FileInputStream(infile);
        byte[] bytes = StreamUtils.copyToByteArray(fin);
        byte[] decode = Base64.getDecoder().decode(bytes);

        FileOutputStream fout = new FileOutputStream(outfile);
        StreamUtils.copy(decode, fout);
        fout.close();
        fin.close();
    }

}
