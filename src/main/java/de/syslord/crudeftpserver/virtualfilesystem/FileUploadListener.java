package de.syslord.crudeftpserver.virtualfilesystem;

import de.syslord.crudeftpserver.utils.SimpleFile;

public interface FileUploadListener {

	void onFileUploadDone(SimpleFile file);

}
