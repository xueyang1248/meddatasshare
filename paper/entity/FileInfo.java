package test.paper.entity;

import com.sunxue.common.utils.Detect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 16:51
 * @Description
 * @Version 1.0
 */
public class FileInfo {

    private String fileName;

    private String fileSign;

    private String password;

    private String splitCount;

    List<SubFileInfo> subFileInfos;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileSign() {
        return fileSign;
    }

    public void setFileSign(String fileSign) {
        this.fileSign = fileSign;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSplitCount() {
        return splitCount;
    }

    public void setSplitCount(String splitCount) {
        this.splitCount = splitCount;
    }

    public List<SubFileInfo> getSubFileInfos() {
        return subFileInfos;
    }

    public void setSubFileInfos(List<SubFileInfo> subFileInfos) {
        this.subFileInfos = subFileInfos;
    }

    public void addSubFileInfo(SubFileInfo subFileInfo){
        if(!Detect.notEmpty(subFileInfos)) subFileInfos = new ArrayList<>();
        subFileInfos.add(subFileInfo);
    }
}
