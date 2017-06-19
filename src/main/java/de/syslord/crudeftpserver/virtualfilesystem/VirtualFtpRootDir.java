package de.syslord.crudeftpserver.virtualfilesystem;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ftpserver.ftplet.FtpException;
import org.apache.ftpserver.ftplet.FtpFile;

public class VirtualFtpRootDir implements FtpFile {

	private static final String OWNER_NAME = "system";

	private Map<String, VirtualFtpFile> files = new HashMap<>();

	private VirtualFileSystemView virtualFileSystemView;

	public VirtualFtpRootDir(VirtualFileSystemView virtualFileSystemView) {
		this.virtualFileSystemView = virtualFileSystemView;
	}

	public boolean removeFile(String name) {
		VirtualFtpFile result = files.remove(name);
		return result != null;
	}

	public FtpFile getFile(String file) throws FtpException {
		if (isRootDir(file)) {
			return this;
		}

		String fileName = getFilename(file);

		if (files.containsKey(fileName)) {
			return files.get(fileName);
		} else {
			VirtualFtpFile fileFile = new VirtualFtpFile(virtualFileSystemView, this, fileName);
			files.put(fileName, fileFile);
			return fileFile;
		}
	}

	private boolean isRootDir(String file) {
		return ".".equals(file) || "./".equals(file) || "/".equals(file) || "..".equals(file);
	}

	private String getFilename(String file) {
		// absolute or relative path

		if (file.matches("^/[^/]+$")) {
			return file.substring(1);

		} else if (file.matches("^[.]/[^/]+$")) {
			return file.substring(2);

		} else {
			return file;
		}
	}

	private boolean isRootFile(String destPath) {
		return destPath.matches("^/?[^/]+$");
	}

	public boolean moveFile(VirtualFtpFile sourceFile, FtpFile destination) {
		String destPath = destination.getAbsolutePath();

		if (!isRootFile(destPath)) {
			return false;
		}

		String sourceName = sourceFile.getName();
		String destName = getFilename(destPath);

		sourceFile.updatePath(destName);

		// remove emtpy destination file
		files.remove(destName);
		files.remove(sourceName);
		files.put(destName, sourceFile);

		virtualFileSystemView.onFileUploadDone(sourceFile);

		return true;
	}

	@Override
	public String getAbsolutePath() {
		return "/";
	}

	@Override
	public String getName() {
		return "/";
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public boolean isDirectory() {
		return true;
	}

	@Override
	public boolean isFile() {
		return false;
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
		return OWNER_NAME;
	}

	@Override
	public String getGroupName() {
		return OWNER_NAME;
	}

	@Override
	public int getLinkCount() {
		return 0;
	}

	@Override
	public long getLastModified() {
		return 0;
	}

	@Override
	public boolean setLastModified(long time) {
		return false;
	}

	@Override
	public long getSize() {
		return 0;
	}

	@Override
	public Object getPhysicalFile() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean mkdir() {
		return false;
	}

	@Override
	public boolean delete() {
		return false;
	}

	@Override
	public boolean move(FtpFile destination) {
		return false;
	}

	@Override
	public List<? extends FtpFile> listFiles() {
		return new ArrayList<>(files.values());
	}

	@Override
	public OutputStream createOutputStream(long offset) throws IOException {
		return null;
	}

	@Override
	public InputStream createInputStream(long offset) throws IOException {
		return null;
	}

}
