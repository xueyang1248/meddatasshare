package test.paper.entity;

import com.sunxue.common.utils.Detect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 16:48
 * @Description
 * @Version 1.0
 */
public class PatientInfo {

    private String idCard="411328196701011230";

    private String userName="张三";

    private String userSex="男";

    private String birthday="1967-01-01";

    private String illnessDescription="心脑血管疾病";

    List<FileInfo> fileInfos;

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getIllnessDescription() {
        return illnessDescription;
    }

    public void setIllnessDescription(String illnessDescription) {
        this.illnessDescription = illnessDescription;
    }

    public List<FileInfo> getFileInfos() {
        return fileInfos;
    }

    public void setFileInfos(List<FileInfo> fileInfos) {
        this.fileInfos = fileInfos;
    }

    public void addFileInfo(FileInfo fileInfo){
        if(!Detect.notEmpty(fileInfos)) fileInfos = new ArrayList<>();
        fileInfos.add(fileInfo);
    }
}
