package de.syslord.crudeftpserver.virtualfilesystem;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.ftpserver.ftplet.FtpFile;

import de.syslord.crudeftpserver.utils.ByteArrayOutputStreamHook;

public class VirtualFtpFile implements FtpFile {

	private static final String OWNER = "system";

	private ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(0);

	private String name;

	private VirtualFtpRootDir rootFile;

	private VirtualFileSystemView virtualFileSystemView;

	private long lastModified;

	public VirtualFtpFile(VirtualFileSystemView virtualFileSystemView, VirtualFtpRootDir rootFile, String name) {
		this.virtualFileSystemView = virtualFileSystemView;
		this.rootFile = rootFile;
		this.name = name;
	}

	public void updatePath(String newName) {
		this.name = newName;
	}

	public byte[] getData() {
		return byteArrayOutputStream.toByteArray();
	}

	@Override
	public String getAbsolutePath() {
		return name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return false;
	}

	@Override
	public boolean isFile() {
		return true;
	}

	@Override
	public boolean doesExist() {
		return true;
	}

	@Override
	public boolean isReadable() {
		return true;
	}

	@Override
	public boolean isWritable() {
		return true;
	}

	@Override
	public boolean isRemovable() {
		return true;
	}

	@Override
	public String getOwnerName() {
		return OWNER;
	}

	@Override
	public String getGroupName() {
		return OWNER;
	}

	@Override
	public int getLinkCount() {
		return 0;
	}

	@Override
	public long getLastModified() {
		return lastModified;
	}

	@Override
	public boolean setLastModified(long lastModified) {
		this.lastModified = lastModified;
		return true;
	}

	@Override
	public long getSize() {
		return byteArrayOutputStream.size();
	}

	@Override
	public Object getPhysicalFile() {
		return null;
	}

	@Override
	public boolean mkdir() {
		return false;
	}

	@Override
	public boolean delete() {
		return rootFile.removeFile(this.getName());
	}

	@Override
	public boolean move(FtpFile destination) {
		return rootFile.moveFile(this, destination);
	}

	@Override
	public List<? extends FtpFile> listFiles() {
		return new ArrayList<>();
	}

	@Override
	public OutputStream createOutputStream(long offset) throws IOException {
		if (offset != 0) {
			throw new IOException("only zero offsets allowed");
		}

		ByteArrayOutputStreamHook byteArrayOutputStreamHook = new ByteArrayOutputStreamHook(new ByteArrayOutputStream());
		byteArrayOutputStreamHook.onClose(() -> virtualFileSystemView.onFileUploadDone(this));

		byteArrayOutputStream = byteArrayOutputStreamHook;
		return byteArrayOutputStream;
	}

	@Override
	public InputStream createInputStream(long offset) throws IOException {
		if (offset != 0) {
			throw new IOException("only zero offsets allowed");
		}
		return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
	}

}
