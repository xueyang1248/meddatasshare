package test.paper;

import com.sunxue.common.ftp.FtpUtil;
import com.sunxue.common.redis.RedisUtils;
import com.sunxue.common.utils.Detect;
import com.sunxue.project.system.platform.model.consts.RedisBusinessConsts;
import org.apache.log4j.Logger;

import java.io.File;
import java.time.Duration;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月17日 10:06
 * @Description
 * @Version 1.0
 */
public class FileUploadTest {

    private static Logger logger = Logger.getLogger(FileUploadTest.class);

    public static int threadSize = 3;
    public static int threadSizeStandard = 3;

    public static String SOURCEFILENODROP = "SourceFileNoDrop";
    public static String SUBFILENODROP = "SubFileNoDrop";
    public static String SOURCEFILEWITHDROP = "SourceFileWithDrop";
    public static String SUBFILEWITHDROP = "SubFileWithDrop";

    public static void testupload() throws Exception{
        testFileupload();
        testSplitFileupload();
    }

    private static void setSecond(String fileName,String flag,long seconds){
        String key = "seconds:" + fileName  + ":" + flag;
        String value = RedisUtils.getString(key, RedisBusinessConsts.INDEX_PAPER);
        if(Detect.notEmpty(value)){
            value += ":" + seconds;
        }else{
            value = "" + seconds;
        }
        RedisUtils.setString(key,value,RedisBusinessConsts.INDEX_PAPER);
    }

    private static void testFileupload() throws Exception{
        //int[] sizes = new int[]{20,50,100,200,500,1024,2048};
        int[] sizes = new int[]{20};
        for (int size : sizes) {
            String filePathTmp = "D:\\zpaperdata\\sourcefile\\test-"+size+"M\\test-"+size+"M-enc.zip";
            File uploadFile = new File(filePathTmp);
            long startTime = System.currentTimeMillis();
            FtpUtil.upload("192.168.3.101", "21", "test", "12345678", uploadFile, filePathTmp.substring(filePathTmp.lastIndexOf("\\")+1),"/enc-testData");
            long seconds = Duration.ofMillis(System.currentTimeMillis() - startTime).getSeconds();
            logger.info("test-"+size+"M-enc.zip文件传输时间：" + seconds + "秒");
            setSecond("test-"+size+"M-enc", SOURCEFILEWITHDROP,seconds);
        }
    }

	private static void testSplitFileupload() throws Exception{
        int[] sizes = new int[]{20,50,100,200,500,1024,2048};
        //int[] sizes = new int[]{20,50,100};
        for (int size : sizes) {
            String partPath = "D:\\zpaperdata\\sourcefile\\test-"+ size +"M\\part-enc";
            File file = new File(partPath);
            File[] partFiles = file.listFiles((f, str) -> str.endsWith(".part"));
            long startTime = System.currentTimeMillis();
            int i;
            for (i = 0; i<partFiles.length; i++) {//多个并行
                FileUploadThread fileThread = new FileUploadThread(partFiles[i], partFiles[i].getName(), "part-enc-testData-" + size + "M");
                fileThread.start();
            }
            if(i==partFiles.length){
                while (true){
                    Thread.sleep(100);
                    if(threadSize==threadSizeStandard) break;
                }
            }
            long seconds = Duration.ofMillis(System.currentTimeMillis() - startTime).getSeconds();
            logger.info("part-"+size+"M文件传输时间：" + seconds + "秒");
            setSecond("test-"+size+"M",SUBFILEWITHDROP,seconds);
        }
    }

    public static synchronized void descrease(){
        while (true){
            if(threadSize!=0) break;
            try {
                Thread.sleep(100);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        threadSize--;
    }

    public static void main(String[] args) throws Exception{
        testupload();
    }
}