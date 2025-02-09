package test.paper;

import com.sunxue.common.file.FileSplitMergeUtil;

import java.io.File;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 9:03
 * @Description
 * @Version 1.0
 */
public class FileSplitMergeTest {

    //subfile size 20M
    private static int bufferSize = 1024 * 1024 * 20;

    public static void splitFile(String filePath) throws Exception{
        File file = new File(filePath);
        String partPath = filePath.substring(0, filePath.lastIndexOf("\\"))+"\\part";
        FileSplitMergeUtil.splitFile(file, bufferSize, partPath);
    }

    public static void mergeFile(String partFilePath) throws Exception{
        File file = new File(partFilePath);
        FileSplitMergeUtil.mergeFile(file, bufferSize);
    }

    public static void main(String[] args) throws Exception {
        String filePath = "D:\\zpaperdata\\sourcefile\\test-200M\\part-dec";
        mergeFile(filePath);
    }
}
