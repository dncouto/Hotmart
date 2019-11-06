package br.com.desafioHotMart.Service.Intf;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import br.com.desafioHotMart.Model.UploadModel;

public interface IUploadService {

	void registerStartUpload(String fileName, String userName, int chunks, String sizeFormated, long timestampStart);

	long registerEndUpload(String fileName, String msgError);
	
	List<UploadModel> getListFilesServer();

	UploadModel getFileServer(String fileName);

	void saveFile(String name, byte[] uploadfile, Boolean isFirstPart) throws IOException;

	File getFileStream(String name) throws IOException, NoSuchFileException;

	byte[] getByteStream(String name) throws IOException, NoSuchFileException;

}