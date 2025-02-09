package test.paper;

import com.sunxue.common.file.FileEncryptUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月18日 9:41
 * @Description
 * @Version 1.0
 */
public class FileEncryptTest {

    public static String encKey = "2a6623db56b4f435ce5df3a9d039b1122333f480b4c2a650f1b876453d03310b";

    public static void aesEncTest(String sourceFilePath) throws Exception {
        File sourceFile = new File(sourceFilePath);
        String encFilePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf(".")) + "-enc" + sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
        System.out.println("encFilePath:"+encFilePath);
        File encFile = new File(encFilePath);
        try (FileInputStream fis = new FileInputStream(sourceFile);
             FileOutputStream fos = new FileOutputStream(encFile, true)) {
            FileEncryptUtil.encryptFile(fis, fos, encKey);
        }
    }

    public static void aesDecTest(String encFilePath) throws Exception {
        String decFilePath = encFilePath.replace("-merge", "-dec");
        System.out.println("decFilePath:"+decFilePath);
        File encFile = new File(encFilePath);
        File decFile = new File(decFilePath);
        try (FileInputStream fis = new FileInputStream(encFile);
             FileOutputStream fos = new FileOutputStream(decFile, true)) {
            FileEncryptUtil.decryptedFile(fis, fos, encKey);
        }
    }

    public static void main(String[] args) throws Exception {
//        String sourceFilePath = "D:\\zpaperdata\\sourcefile\\test-100M\\test-100M.zip";
//        aesEncTest(sourceFilePath);
        String encFilePath = "D:\\zpaperdata\\sourcefile\\test-200M\\test-200M-merge.zip";
        aesDecTest(encFilePath);
    }

}
