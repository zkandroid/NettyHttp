package NettyHttp.NettyHttp;

public class ResultInfo{
	private int machineId = 0;
	private double price = 0.0;
	private int tradeid = 0;
	private String openId = "";
	private String result ="false";
	public int getMachineId() {
		return machineId;
	}
	public void setMachineId(int machineId) {
		this.machineId = machineId;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getTradeid() {
		return tradeid;
	}
	public void setTradeid(int tradeid) {
		this.tradeid = tradeid;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
}