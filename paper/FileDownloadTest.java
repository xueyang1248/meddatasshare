package test.paper;

import com.sunxue.common.ftp.FtpUtil;
import com.sunxue.common.redis.PaperRedisUtilTest;
import com.sunxue.common.redis.RedisUtils;
import com.sunxue.common.utils.Detect;
import com.sunxue.project.system.platform.model.consts.RedisBusinessConsts;
import org.apache.log4j.Logger;

import java.time.Duration;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月17日 10:06
 * @Description
 * @Version 1.0
 */
public class FileDownloadTest {

    private static Logger logger = Logger.getLogger(FileDownloadTest.class);

    public static int threadSize = 3;
    public static int threadSizeStandard = 3;

    public static String SOURCEFILEWITHDROP = "SourceFileWithDrop";
    public static String SUBFILEWITHDROP = "SubFileWithDrop";

    public static void testupload() throws Exception{
        // test five times
        for (int i=1;i<2;i++){
            testFileDownload();
            testSplitFileDownload();
        }
    }

    private static void setSecond(String fileName,String flag,long seconds){
        String key = "DownloadSeconds:" + fileName  + ":" + flag;
        String value = RedisUtils.getString(key, RedisBusinessConsts.INDEX_PAPER);
        if(Detect.notEmpty(value)){
            value += ":" + seconds;
        }else{
            value = "" + seconds;
        }
        RedisUtils.setString(key,value,RedisBusinessConsts.INDEX_PAPER);
    }

    private static void testFileDownload() throws Exception{
        //int[] sizes = new int[]{20,50,100,200,500,1024,2048};
        int[] sizes = new int[]{1024,2048};
        for (int size : sizes) {
            String fullFilePath = "/enc-testData/test-"+size+"M-enc.zip";
            long startTime = System.currentTimeMillis();
            FtpUtil.getFile("192.168.3.101", "21", "test", "12345678", fullFilePath);
            long seconds = Duration.ofMillis(System.currentTimeMillis() - startTime).getSeconds();
            logger.info("test-"+size+"M-download.zip文件传输时间：" + seconds + "秒");
            setSecond("test-"+size+"M-download", SOURCEFILEWITHDROP,seconds);
        }
    }

	private static void testSplitFileDownload() throws Exception{
        int[] sizes = new int[]{20,50,100,200,500,1024,2048};
        //int[] sizes = new int[]{20,50};
        for (int size : sizes) {
            PaperRedisUtilTest.setCurrentDealFile("test-"+size+"M");
            int count = Integer.parseInt(PaperRedisUtilTest.get("subFilecount"));
            long startTime = System.currentTimeMillis();
            int i;
            for (i = 0; i<count; i++) {//多个并行
                String fullFilePath = "/part-enc-testData-"+size+"M/"+(i+1)+".part";
                String downloadFilePath = "D:\\zpaperdata\\sourcefile\\test-"+size+"M\\part-enc-download\\"+(i+1)+".part";
                FileDownloadThread fileThread = new FileDownloadThread(fullFilePath,downloadFilePath);
                fileThread.start();
            }
            if(i==count){
                while (true){
                    Thread.sleep(100);
                    if(threadSize==threadSizeStandard) break;
                }
            }
            long seconds = Duration.ofMillis(System.currentTimeMillis() - startTime).getSeconds();
            logger.info("part-enc-"+size+"M文件传输时间：" + seconds + "秒");
            setSecond("test-"+size+"M-download",SUBFILEWITHDROP,seconds);
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