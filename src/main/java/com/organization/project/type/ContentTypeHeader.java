package com.organization.project.type;

public enum ContentTypeHeader {
	TEXTHTML("text/html", ".html"), TEXTCSS("text/css", ".css"), TEXTPLAIN("text/plain",
			".txt"), APPLICATIONJSON("application/json", ".json"), APPLICATIONJAVASCRIPT("application/json", ".js");
	
	private final String mimeType;
	private final String fileExtentsion;

	private ContentTypeHeader(String mimeType, String fileExtentsion) {
		this.mimeType = mimeType;
		this.fileExtentsion = fileExtentsion;
	}

	public String getMimeType() {
		return mimeType;
	}

	public String getFileExtentsion() {
		return fileExtentsion;
	}

}
