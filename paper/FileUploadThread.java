package test.paper;

import com.sunxue.common.ftp.FtpUtil;

import java.io.File;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月17日 15:39
 * @Description
 * @Version 1.0
 */
public class FileUploadThread extends Thread{

    private File uploadFile;
    private String fileName;
    private String storePath;

    public FileUploadThread(File uploadFile,String fileName,String storePath) {
        this.uploadFile = uploadFile;
        this.fileName = fileName;
        this.storePath = storePath;
    }

    @Override
    public void run() {
        FileUploadTest.descrease();
        System.out.println("start "+this.getName() + ":" + FileUploadTest.threadSize);
        FtpUtil.upload("192.168.3.101", "21", "test", "12345678", uploadFile, fileName, storePath);
        FileUploadTest.threadSize++;
        System.out.println("end "+this.getName() + ":" + FileUploadTest.threadSize);
    }
}
