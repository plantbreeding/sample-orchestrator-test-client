package org.brapi.test.SampleOrchestratorServer.model.entity;

public enum SubmissionStageEnum {
	NEW_SUBMISSION(0, "green"),  
	WAITING_FOR_USER(10, "#cece00"),
	WAITING_FOR_VENDOR(25, "green"), 
	SENT_TO_GENOTYPE_DB(50, "green"),
	WAITING_FOR_GENOTYPE_DATABASE(75, "green"), 
	COMPLETE(100, "green"), 
	ERROR(100, "#c80000");

	SubmissionStageEnum(Integer percentComplete, String color) {
		this.percentComplete = percentComplete;
		this.color = color;
	}

	Integer percentComplete;
	String color;
	
	public Integer getPercentComplete() {
		return percentComplete;
	}
	public String getColor() {
		return color;
	}
	
	
}
