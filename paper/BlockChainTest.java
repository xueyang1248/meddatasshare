package test.paper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunxue.common.file.FilePadding;
import com.sunxue.common.redis.PaperRedisUtilTest;
import com.sunxue.common.redis.RedisUtils;
import com.sunxue.project.system.platform.model.consts.RedisBusinessConsts;
import net.sf.json.JSONArray;
import test.paper.entity.FileInfo;
import test.paper.entity.PatientInfo;
import test.paper.entity.SubFileInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 16:57
 * @Description
 * @Version 1.0
 */
public class BlockChainTest {

    public static void main(String[] args) throws Exception{
        getPatientValue();
    }

    public static void getPatientValue() throws Exception{
        //int[] sizes = new int[]{20,50,100,200,500,1024,2048};
        int[] sizes = new int[]{200};
        List<PatientInfo> patientInfos = new ArrayList<>();
        for (int size : sizes) {
            String fileName = "test-" + size + "M.zip";
            PatientInfo patientInfo = new PatientInfo();
            FileInfo fileInfo = new FileInfo();
            fileInfo.setFileName(fileName);
            fileInfo.setPassword(FileEncryptTest.encKey);
            fileInfo.setFileSign(PaperRedisUtilTest.get("sourceFileSign"));
            int count = Integer.parseInt(PaperRedisUtilTest.get("subFilecount"));
            fileInfo.setSplitCount(count+"");
            for (int i=1;i<=count;i++){
                SubFileInfo subFileInfo = new SubFileInfo();
                subFileInfo.setSubFileName(i+".part");
                subFileInfo.setSubFileSign(PaperRedisUtilTest.get("subEncsSign:"+i+".part"));
                subFileInfo.setLocation("/hospitalName/date/"+i+".part");
                FilePadding filePadding = new ObjectMapper().readValue(PaperRedisUtilTest.get("FilePadding:"+i+".part"), FilePadding.class);
                subFileInfo.setInsertStart(filePadding.getStartLocation());
                subFileInfo.setInsertEnd(filePadding.getEndLocation());
                fileInfo.addSubFileInfo(subFileInfo);
            }
            patientInfo.addFileInfo(fileInfo);
            patientInfos.add(patientInfo);
        }
        String value = JSONArray.fromObject(patientInfos).toString();
        System.out.println("链上明文信息："+value);
        RedisUtils.setString("blockchainInfo",value, RedisBusinessConsts.INDEX_PAPER);
        String encValue = Sm2Test.encData(value);
        System.out.println("链上密文信息："+encValue);
        RedisUtils.setString("blockchainEncInfo",value, RedisBusinessConsts.INDEX_PAPER);
    }
}
