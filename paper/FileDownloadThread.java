package test.paper;

import com.sunxue.common.ftp.FtpUtil;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月17日 15:39
 * @Description
 * @Version 1.0
 */
public class FileDownloadThread extends Thread{

    private String fullFilePath;
    @SuppressWarnings("unused")
	private String downloadFilePath;

    public FileDownloadThread(String fullFilePath, String downloadFilePath) {
        this.fullFilePath = fullFilePath;
        this.downloadFilePath = downloadFilePath;
    }

    @Override
    public void run() {
        FileDownloadTest.descrease();
        System.out.println("start "+this.getName() + ":" + FileDownloadTest.threadSize);
        try {
            //byte[] bytes =
            FtpUtil.getFile("192.168.3.101", "21", "test", "12345678", fullFilePath);
            //FileUtils.byteArrayToFile(bytes, downloadFilePath);
        }catch (Exception e){
            e.printStackTrace();
        }
        FileDownloadTest.threadSize++;
        System.out.println("end "+this.getName() + ":" + FileDownloadTest.threadSize);
    }
}
