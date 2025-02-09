package com.sunxue.blockchain.chaincode.contract;

import com.alibaba.fastjson.JSON;
import com.sunxue.blockchain.chaincode.model.MedData;
import com.sunxue.blockchain.chaincode.util.ChaincodeExtractAttributes;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;

@Log
@Contract(name = "MedDataShare")
public class MedDataShareContract implements ContractInterface {

    @Transaction(intent =Transaction.TYPE.EVALUATE)
    public MedData queryMedData(final Context ctx, final String idCard) {
        ChaincodeStub stub = ctx.getStub();
        String catState = stub.getStringState(idCard);
        if (StringUtils.isBlank(catState)) {
            String errorMessage = String.format("MedData %s does not exist", idCard);
            throw new ChaincodeException(errorMessage);
        }
        return JSON.parseObject(catState , MedData.class);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public MedData createMedData(final Context ctx, String idCard, String patientName, String jsonValue) {
        ChaincodeStub stub = ctx.getStub();
        String catState = stub.getStringState(idCard);
        if (StringUtils.isNotBlank(catState)) {
            String errorMessage = String.format("MedData %s already exists", idCard);
            throw new ChaincodeException(errorMessage);
        }
        MedData medData = new MedData().setPatientName(patientName).setIdCard(idCard).setJsonValue(jsonValue);
        String json = JSON.toJSONString(medData);
        stub.putStringState(idCard, json);
        stub.setEvent("createMedDataEvent" , org.apache.commons.codec.binary.StringUtils.getBytesUtf8(json));
        return medData;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public MedData updateMedData(final Context ctx, String idCard, String patientName, String jsonValue) {
        ChaincodeStub stub = ctx.getStub();
        String catState = stub.getStringState(idCard);
        if (StringUtils.isBlank(catState)) {
            String errorMessage = String.format("MedData %s does not exist", idCard);
            throw new ChaincodeException(errorMessage);
        }
        MedData medData = new MedData().setPatientName(patientName).setIdCard(idCard).setJsonValue(jsonValue);
        stub.putStringState(idCard, JSON.toJSONString(medData));
        return medData;
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public MedData deleteMedData(final Context ctx, final String idCard) {
        ChaincodeStub stub = ctx.getStub();
        String medDataState = stub.getStringState(idCard);
        if (StringUtils.isBlank(medDataState)) {
            String errorMessage = String.format("MedData %s does not exist", idCard);
            throw new ChaincodeException(errorMessage);
        }
        stub.delState(idCard);
        return JSON.parseObject(medDataState , MedData.class);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String addMedData(final Context ctx, String fid, String value) {
        ChaincodeStub stub = ctx.getStub();
        String valueCheck = stub.getStringState(fid);
        if (StringUtils.isNotBlank(valueCheck)) {
            String errorMessage = String.format("MedData %s already exists", valueCheck);
            throw new ChaincodeException(errorMessage);
        }
        stub.putStringState(fid, value);
        return "success";
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String patientQuery(final Context ctx, String fid){
        ChaincodeStub stub = ctx.getStub();
        String userTypeValue = ChaincodeExtractAttributes.getUserAttribute(stub,"userType");
        if(!"Patient".equals(userTypeValue)){
            throw new ChaincodeException("Authentication failed");
        }
        String value = stub.getStringState(fid);
        if (StringUtils.isBlank(value)) {
            String errorMessage = String.format("MedData %s does not exist", value);
            throw new ChaincodeException(errorMessage);
        }
        return value;
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String hospitalQuery(final Context ctx, String fid){
        ChaincodeStub stub = ctx.getStub();
        String userTypeValue = ChaincodeExtractAttributes.getUserAttribute(stub,"userType");
        if(!"Hospital".equals(userTypeValue)){
            throw new ChaincodeException("Authentication failed");
        }
        String value = stub.getStringState(fid);
        if (StringUtils.isBlank(value)) {
            String errorMessage = String.format("MedData %s does not exist", value);
            throw new ChaincodeException(errorMessage);
        }
        return value;
    }

}
