package test.paper.entity;

/**
 * @author xueyang
 * @Date 创建时间 2024年02月21日 16:53
 * @Description
 * @Version 1.0
 */
public class SubFileInfo {

    private String subFileName;

    private String subFileSign;

    private int insertStart;

    private int insertEnd;
    
    private String location;

    public String getSubFileName() {
        return subFileName;
    }

    public void setSubFileName(String subFileName) {
        this.subFileName = subFileName;
    }

    public String getSubFileSign() {
        return subFileSign;
    }

    public void setSubFileSign(String subFileSign) {
        this.subFileSign = subFileSign;
    }

	public int getInsertStart() {
		return insertStart;
	}

	public void setInsertStart(int insertStart) {
		this.insertStart = insertStart;
	}

	public int getInsertEnd() {
		return insertEnd;
	}

	public void setInsertEnd(int insertEnd) {
		this.insertEnd = insertEnd;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
    
}
