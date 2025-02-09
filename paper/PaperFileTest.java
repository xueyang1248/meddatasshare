package test.paper;

import com.sunxue.common.file.FileUtils;
import com.sunxue.common.ftp.FtpUtil;
import com.sunxue.common.redis.PaperRedisUtilTest;
import com.sunxue.common.sm2.Sm3Util;

import java.io.File;
import java.time.Duration;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 9:56
 * @Description
 * @Version 1.0
 */
public class PaperFileTest {

    public static void main(String[] args) throws Exception{
        int[] sizes = new int[]{20,50,100,200,500,1024,2048};
        //int[] sizes = new int[]{2048};
        for (int size : sizes) {
            long startTime = System.currentTimeMillis();
            PaperRedisUtilTest.setCurrentDealFile("test-"+size+"M");
            fileTest("D:\\zpaperdata\\sourcefile\\test-"+size+"M\\test-"+size+"M.zip");
            long seconds = Duration.ofMillis(System.currentTimeMillis() - startTime).getSeconds();
            System.out.println("D:\\zpaperdata\\sourcefile\\test-"+size+"M\\test-"+size+"M.zip时间：" + seconds + "秒");
            PaperRedisUtilTest.set("test-"+size+"M:after",seconds+"");
        }
    }

    /**
     * before:
     * 1 source file sign
     * 2 use aes arithmetic to encrypt source file
     * 3 split encrypt file, the size of every file is 20M
     * 4 add padding info to every subfile
     * 5 subfile sign
     *
     * blockchain infomation:
     * 1 file sign,split information,padding infomation, subfile sign
     *
     * transmission:
     * 1 every subfile upload to higher hospital
     *
     * after:
     * 1 check subfile sign
     * 2 delete every subfile padding info
     * 3 subfile merge
     * 4 use aes arithmetic to decrypt file
     * 5 check file sign
     * @param sourcefilePath
     * @throws Exception
     */
    private static void fileTest(String sourcefilePath) throws Exception{
        //1 before
        //fileTestBefore(sourcefilePath);
        //2 blockchain infomation
        //BlockChainTest.getPatientValue();
        //3 file upload
        //FileUploadTest.testupload();
        //4 after
        fileTestAfter(sourcefilePath);
    }

    /**
     * 1 source file sign
     * 2 use aes arithmetic to encrypt source file
     * 3 split encrypt file, the size of every file is 20M
     * 4 add padding info to every subfile
     * 5 subfile sign
     * @param sourceFilePath
     * @throws Exception
     */
    @SuppressWarnings("unused")
	private static void fileTestBefore(String sourceFilePath) throws Exception{
        //1
        String signSourceFile = Sm2Test.sign(Sm3Util.hashStr(FileUtils.fileToByte(new File(sourceFilePath))));
        PaperRedisUtilTest.set("sourceFileSign", signSourceFile);
        //2
        FileEncryptTest.aesEncTest(sourceFilePath);
        //3
        String encFilePath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf(".")) + "-enc" + sourceFilePath.substring(sourceFilePath.lastIndexOf("."));
        FileSplitMergeTest.splitFile(encFilePath);
        //4
        String partPath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("\\"))+"\\part";
        FilePaddingTest.encSubFile(partPath);
        //5
        String encPartPath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("\\"))+"\\part-enc";
        Sm2Test.storeSignFile(encPartPath);
    }

    /**
     * 1 check subfile sign
     * 2 check and delete every subfile padding info
     * 3 subfile merge
     * 4 use aes arithmetic to decrypt file
     * 5 check file sign
     * @param sourceFilePath
     * @throws Exception
     */
    private static void fileTestAfter(String sourceFilePath) throws Exception{
        //1
        String encPartPath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("\\"))+"\\part-enc";
        Sm2Test.verifySubFileSign(encPartPath);
        //2
        FilePaddingTest.decSubFile(encPartPath);
        //3
        String decPartPath = sourceFilePath.substring(0, sourceFilePath.lastIndexOf("\\"))+"\\part-dec";
        FileSplitMergeTest.mergeFile(decPartPath);
        //4
        String mergeFilePath = sourceFilePath.substring(0,sourceFilePath.indexOf(".")) + "-merge" + sourceFilePath.substring(sourceFilePath.indexOf("."));
        FileEncryptTest.aesDecTest(mergeFilePath);
        //5
        String decFilePath = sourceFilePath.substring(0,sourceFilePath.indexOf(".")) + "-dec" + sourceFilePath.substring(sourceFilePath.indexOf("."));
        Sm2Test.verifyFileSign(decFilePath);
    }
}
