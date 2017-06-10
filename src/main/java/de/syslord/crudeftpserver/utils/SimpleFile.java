package de.syslord.crudeftpserver.utils;

import java.time.LocalDateTime;

public class SimpleFile {

	private String uploader;

	private String name;

	private byte[] data;

	private LocalDateTime uploaded;

	public SimpleFile(String name, byte[] data, String uploader, LocalDateTime uploaded) {
		this.name = name;
		this.data = data;
		this.uploader = uploader;
		this.uploaded = uploaded;
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

	public LocalDateTime getUploaded() {
		return uploaded;
	}

	@Override
	public String toString() {
		return "SimpleFile [uploader=" + uploader + ", name=" + name + ", uploaded=" + uploaded + "]";
	}

}
