package org.brapi.test.SampleOrchestratorServer.model.json;

public class SubmissionStatusResponse {
	String id;
	String stage;
	String status;
	String errorMsg;
	String vendorName;
	String color;
	Integer percentComplete;
	
	
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}	
	public Integer getPercentComplete() {
		return percentComplete;
	}
	public void setPercentComplete(Integer percentComplete) {
		this.percentComplete = percentComplete;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getStage() {
		return stage;
	}
	public void setStage(String stage) {
		this.stage = stage;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
}
