package test.paper;

import com.sunxue.common.file.FileUtils;
import com.sunxue.common.redis.PaperRedisUtilTest;
import com.sunxue.common.sm2.SM2;
import com.sunxue.common.sm2.SM2KeyPair;
import com.sunxue.common.sm2.Sm3Util;
import com.sunxue.common.utils.Assertion;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.pqc.math.linearalgebra.ByteUtils;

import java.io.File;
import java.math.BigInteger;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 13:42
 * @Description
 * @Version 1.0
 */
public class Sm2Test {

    public static SM2 sm02;

    public static ECPoint publicKey;

    public static BigInteger privateKey;

    public static String IDA = "xueyang";

    static {
        sm02 = new SM2();
        publicKey = sm02.importPublicKey("D:\\zpaperdata\\publickey.pem");
        privateKey = sm02.importPrivateKey("D:\\zpaperdata\\privatekey.pem");
    }

    @SuppressWarnings("unused")
	private static void genKeyPair(){
        SM2.genKeyPair();
    }

    public static String encData(String value){
        byte[] data = sm02.encrypt(value, publicKey);
        return ByteUtils.toHexString(data);
    }

    public static String decData(String encValue){
        return sm02.decrypt(ByteUtils.fromHexString(encValue), privateKey);
    }

    public static String sign(String value){
        SM2.Signature signature = sm02.sign(value, IDA, new SM2KeyPair(publicKey, privateKey));
        System.out.println("用户标识:" + IDA);
        System.out.println("签名信息:" + value);
        System.out.println("数字签名:" + signature);
        return signature.toString();
    }

    public static String signFile(File file){
        String hash = Sm3Util.hashStr(FileUtils.fileToByte(file));
        return sign(hash);
    }

    public static String signFile(String filePath){
        String hash = Sm3Util.hashStr(FileUtils.fileToByte(new File(filePath)));
        return sign(hash);
    }

    public static void storeSignFile(String partPath){
        File[] encPartFiles = new File(partPath).listFiles((f, str) -> str.endsWith(".part"));
        for (File encPartFile : encPartFiles) {
            String sign = signFile(encPartFile);
            PaperRedisUtilTest.set("subEncsSign:"+encPartFile.getName(), sign);
        }
    }

    public static void verifyFileSign(String filePath){
        File file = new File(filePath);
        String hash = Sm3Util.hashStr(FileUtils.fileToByte(file));
        String signatureStr =  PaperRedisUtilTest.get("sourceFileSign");
        verifySign(hash,signatureStr, file.getAbsolutePath());
    }

    public static void verifySubFileSign(String partPath){
        File[] encPartFiles = new File(partPath).listFiles((f, str) -> str.endsWith(".part"));
        for (File encPartFile : encPartFiles) {
            String hash = Sm3Util.hashStr(FileUtils.fileToByte(encPartFile));
            String signatureStr =  PaperRedisUtilTest.get("subEncsSign:"+encPartFile.getName());
            verifySign(hash,signatureStr, encPartFile.getAbsolutePath());
        }
    }

    public static void verifySign(String value, String signatureStr, String fileName){
        BigInteger r = new BigInteger(signatureStr.split(",")[0], 16);
        BigInteger s = new BigInteger(signatureStr.split(",")[1], 16);
        SM2.Signature signature =new SM2.Signature(r,s);
        boolean result = sm02.verify(value, signature, IDA, publicKey);
        Assertion.isTrue(result,fileName+"签名验证失败");
        System.out.println(fileName+"签名验证成功");
    }

    public static void main(String[] args) throws Exception{
//        String value = "test123薛阳";
//        String enc = encData(value);
//        System.out.println(enc);
//        System.out.println(decData(enc));

//        String M = "要签名的信息";
//        String sign = sign(M);
//        System.out.println("数字签名:" + sign);
//        verifySign(M,sign);

//        Sm2Test.storeSignFile("D:\\zpaperdata\\sourcefile\\test-100M\\part-enc");

        verifyFileSign("D:\\zpaperdata\\sourcefile\\test-1024M\\test-1024M-dec.zip");
    }


}
