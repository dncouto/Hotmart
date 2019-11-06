package br.com.desafioHotMart.Dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import br.com.desafioHotMart.Dao.Intf.IUploadDao;
import br.com.desafioHotMart.Model.UploadModel;

@Repository
public class UploadDao implements IUploadDao{

	private Map<String, UploadModel> listUploadModel = new HashMap<String, UploadModel>();
	
	@Override
	public UploadModel getByFileName(String fileName)
	{
		return listUploadModel.get(fileName);
	}
	
	@Override
	public void upsert(UploadModel model)
	{
		listUploadModel.put(model.getFileName(), model);
	}
	
	@Override
	public List<UploadModel> getAll()
	{
		return new ArrayList<UploadModel>(listUploadModel.values());
	}
}
