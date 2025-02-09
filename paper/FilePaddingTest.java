package test.paper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunxue.common.file.FilePadding;
import com.sunxue.common.file.FilePaddingUtil;
import com.sunxue.common.redis.PaperRedisUtilTest;
import net.sf.json.JSONObject;

import java.io.File;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 9:00
 * @Description
 * @Version 1.0
 */
public class FilePaddingTest {

    public static void encSubFile(String partPath) throws Exception{
        String partEncPath = partPath.replace("\\part", "\\part-enc");
        /*创建一个part文件夹*/
        File encPartFile = new File(partEncPath);
        if (!encPartFile.exists()) {
            boolean mkdirs = encPartFile.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("创建文件夹失败!!!");
            }
        }
        //padding
        File partFile = new File(partPath);
        File[] partFiles = partFile.listFiles((f, str) -> str.endsWith(".part"));
        for (File file : partFiles) {
            FilePadding filePadding = FilePaddingUtil.encPaddingFile(file);
            PaperRedisUtilTest.set("FilePadding:"+file.getName(), JSONObject.fromObject(filePadding).toString());
        }
    }

    public static void decSubFile(String encPartPath) throws Exception{
        String partDecPath = encPartPath.replace("\\part-enc", "\\part-dec");
        /*创建一个part文件夹*/
        File decPartFile = new File(partDecPath);
        if (!decPartFile.exists()) {
            boolean mkdirs = decPartFile.mkdirs();
            if (!mkdirs) {
                throw new RuntimeException("创建文件夹失败!!!");
            }
        }
        File partFile = new File(encPartPath);
        File[] partFiles = partFile.listFiles((f, str) -> str.endsWith(".part"));
        for (File file : partFiles) {
            FilePadding filePadding = new ObjectMapper().readValue(PaperRedisUtilTest.get("FilePadding:" + file.getName()), FilePadding.class);
            FilePaddingUtil.decPaddingFile(filePadding);
        }
    }

    public static void main(String[] args) throws Exception {
//        for(int i=1;i<=3;i++){
//            File file = new File("D:\\part-50M\\"+i+".part");
//            FilePaddingUtil.decPaddingFile(FilePaddingUtil.encPaddingFile(file));
//        }
//        String partPath = "D:\\zpaperdata\\sourcefile\\test-100M\\part";
//        FilePaddingTest.encSubFile(partPath);
        String encPartPath = "D:\\zpaperdata\\sourcefile\\test-200M"+"\\part-enc";
        decSubFile(encPartPath);
    }




}
