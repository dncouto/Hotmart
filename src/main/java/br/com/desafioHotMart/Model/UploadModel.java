package br.com.desafioHotMart.Model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel( value = "Upload", description = "Informações de arquivo enviado pelo upload" )
public class UploadModel {

    public enum StatusUpload {Andamento, Concluido, Erro}
	
    @ApiModelProperty( value = "Identificação do usuário que fez o upload", required = true )
	private String IdUser;
    @ApiModelProperty( value = "Nome original do arquivo", required = true )
    private String FileName;
    @ApiModelProperty( value = "Quantidade de partes que o arquivo foi fragmentado", required = true )
    private int Chunks;
    @ApiModelProperty( value = "Informação de tamanho do arquivo (formatado com escala)", required = true )
    private String SizeFormated;
    @ApiModelProperty( value = "Timestamp de início do upload (primeiro fragmento)", required = true )
    private long TimeStart;
    @ApiModelProperty( value = "Tempo total de envio, do primeiro ao último fragmento", required = true )
    private long TimeSend;
    @ApiModelProperty( value = "Descrição do status do upload (quando erro, consta mensagem da exceção)", required = true )
	private String DescStatus;
    @ApiModelProperty( value = "Status do upload", required = true )
	private StatusUpload Status;
    
    public String getIdUser() {
		return IdUser;
	}
	public void setIdUser(String idUser) {
		IdUser = idUser;
	}

	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}

	public int getChunks() {
		return Chunks;
	}
	public void setChunks(int chunks) {
		this.Chunks = chunks;
	}

	public String getSizeFormated() {
		return SizeFormated;
	}
	
	public void setSizeFormated(String sizeFormated) {
		this.SizeFormated = sizeFormated;
	}
	
	public String getDescStatus() {
		return DescStatus;
	}
	
	public void setDescStatus(String descStatus) {
		this.DescStatus = descStatus;
	}
	
	public StatusUpload getStatus() {
		return Status;
	}
	
	public void setStatus(StatusUpload status) {
		Status = status;
	}
	
	public long getTimeStart() {
		return TimeStart;
	}
	
	public void setTimeStart(long timeStart) {
		TimeStart = timeStart;
	}
	
	public long getTimeSend() {
		return TimeSend;
	}
	
	public void setTimeSend(long timeSend) {
		TimeSend = timeSend;
	}
	
    @Override
    public String toString() {
        return FileName;
    }
}
