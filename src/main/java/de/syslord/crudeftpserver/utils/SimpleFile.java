package de.syslord.crudeftpserver.utils;

public class SimpleFile {

	private String uploader;

	private String name;

	private byte[] data;

	public SimpleFile(String name, byte[] data, String uploader) {
		this.name = name;
		this.data = data;
		this.uploader = uploader;
	}

	public String getName() {
		return name;
	}

	public byte[] getData() {
		return data;
	}

	public String getUploader() {
		return uploader;
	}

	@Override
	public String toString() {
		return "SimpleFile [uploader=" + uploader + ", name=" + name + "]";
	}

}
