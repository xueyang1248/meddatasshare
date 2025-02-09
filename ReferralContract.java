package com.sunxue.blockchain.chaincode.contract;

import com.alibaba.fastjson.JSON;
import com.sunxue.blockchain.chaincode.model.MedData;
import com.sunxue.blockchain.chaincode.util.ChaincodeExtractAttributes;
import com.sunxue.blockchain.chaincode.util.ReferralUtil;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Log
@Contract(name = "Referral")
public class ReferralContract implements ContractInterface {

    public static String TRUE = "TRUE";

    @Transaction(intent =Transaction.TYPE.EVALUATE)
    public MedData queryReferralData(final Context ctx, final String idCard) {
        ChaincodeStub stub = ctx.getStub();
        String userTypeValue = ChaincodeExtractAttributes.getUserAttribute(stub,"userType");
        if(!"Hospital".equals(userTypeValue)){
            throw new ChaincodeException("Authentication failed");
        }
        String referralState = stub.getStringState(idCard);
        if (StringUtils.isBlank(referralState)) {
            String errorMessage = String.format("MedData %s does not exist", idCard);
            throw new ChaincodeException(errorMessage);
        }
        return JSON.parseObject(referralState , MedData.class);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String referral(final Context ctx, String idCard, String lowerLevelHospital, String higherLevelHospital, String jsonValue) {
        ChaincodeStub stub = ctx.getStub();
        if(ReferralUtil.referralCheck(idCard,jsonValue)){  //转诊评估
            String errorMessage = String.format("%s referral error", idCard);
            throw new ChaincodeException(errorMessage);
        }
        String referralForm = ReferralUtil.generateReferralForm(idCard,jsonValue); //生成转诊单
        MedData referralData = new MedData().setIdCard(idCard).setLowerLevelHospital(lowerLevelHospital)
                .setHigherLevelHospital(higherLevelHospital).setReferralForm(referralForm).setJsonValue(jsonValue);
        String json = JSON.toJSONString(referralData);
        stub.putStringState(idCard, json);
        return "success";
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String referralConfirm(final Context ctx, String fid, String idCard, String referralConfirm) {
        ChaincodeStub stub = ctx.getStub();
        String referralDataState = stub.getStringState(idCard);
        if (StringUtils.isBlank(referralDataState)) {
            String errorMessage = String.format("ReferralData %s does not exist", idCard);
            throw new ChaincodeException(errorMessage);
        }
        MedData referralData =  JSON.parseObject(referralDataState , MedData.class);
        referralData.setReferralConfirm(referralConfirm);
        String value = JSON.toJSONString(referralData);
        stub.putStringState(idCard, value);
        if(TRUE.equals(referralConfirm)){
            new MedDataShareContract().addMedData(ctx, fid, value);  //数据共享
        }
        return "success";
    }

}
