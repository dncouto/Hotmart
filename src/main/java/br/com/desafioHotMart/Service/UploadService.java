package br.com.desafioHotMart.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.desafioHotMart.Dao.Intf.IUploadDao;
import br.com.desafioHotMart.Model.UploadModel;
import br.com.desafioHotMart.Model.UploadModel.StatusUpload;
import br.com.desafioHotMart.Service.Intf.IUploadService;

@Service
public class UploadService implements IUploadService {

	@Autowired
	IUploadDao uploadDao;
	
	@Value("${upload.store.dir}")
    private String UPLOAD_STORE_PATH;
	
	@Override
	public void registerStartUpload(String fileName, String userName, int chunks, String sizeFormated, long timestampStart)
	{
		UploadModel model = new UploadModel();
    	model.setFileName(fileName);
    	model.setIdUser(userName);
    	model.setChunks(chunks);
    	model.setSizeFormated(sizeFormated);
    	model.setTimeStart(timestampStart);
    	model.setStatus(StatusUpload.Andamento);
    	model.setDescStatus("Andamento...");
    	uploadDao.upsert(model);
	}
	
	@Override
	public long registerEndUpload(String fileName, String msgError)
	{
		UploadModel model = uploadDao.getByFileName(fileName);
		if (model != null) {
			long timestampStart = model.getTimeStart();
			Long timestampEnd = new Timestamp(System.currentTimeMillis()).getTime();
			if (timestampStart > 0 && timestampEnd > 0)
			{
				model.setTimeSend((timestampEnd - timestampStart));
				if (msgError != null && !msgError.equals(""))
				{
					model.setStatus(StatusUpload.Erro);
					model.setDescStatus("Erro: " + msgError);
				}
				else
				{
					model.setStatus(StatusUpload.Concluido);
					model.setDescStatus("Concluído!");
				}
				uploadDao.upsert(model);
				return model.getTimeSend();
			}
		}
		return 0;
	}
	
	@Override
	public void saveFile(String name, byte[] uploadfile, Boolean isFirstPart) throws IOException
	{
		File file = new File(UPLOAD_STORE_PATH + name);
    	
        //Primeiro pedaço cria o arquivo
    	if(isFirstPart) {
    		if (file.exists())
    			file.delete();
    		file.createNewFile();
        } 

    	FileUtils.writeByteArrayToFile(file, uploadfile, true);
	}
	
	@Override
	public byte[] getByteStream(String name) throws IOException, NoSuchFileException
	{
		Path path = Paths.get(UPLOAD_STORE_PATH + name);
		return Files.readAllBytes(path);
	}
	
	@Override
	public File getFileStream(String name) throws IOException, NoSuchFileException
	{
		return new File(UPLOAD_STORE_PATH + name);
	}
	
	@Override
	public List<UploadModel> getListFilesServer()
	{
		return uploadDao.getAll();
	}
	
	@Override
	public UploadModel getFileServer(String fileName)
	{
		return uploadDao.getByFileName(fileName);
	}
}
