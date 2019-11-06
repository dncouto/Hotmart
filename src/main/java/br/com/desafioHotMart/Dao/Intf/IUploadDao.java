package br.com.desafioHotMart.Dao.Intf;

import java.util.List;

import br.com.desafioHotMart.Model.UploadModel;

public interface IUploadDao {

	UploadModel getByFileName(String fileName);
	
	void upsert(UploadModel model);

	List<UploadModel> getAll();
}
