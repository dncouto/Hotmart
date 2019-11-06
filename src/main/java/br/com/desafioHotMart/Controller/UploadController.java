package br.com.desafioHotMart.Controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.desafioHotMart.Model.UploadModel;
import br.com.desafioHotMart.Service.Intf.IUploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@Api(value="UploadController", description="Operações com arquivos (Upload, Download, recuperação de informações")
public class UploadController {

	@Autowired
	IUploadService uploadService;

	
	@ApiOperation(value = "Realiza o upload do arquivo e suas partes quando fragmentado para o servidor", response = HttpStatus.class)
	@ApiResponses({
            @ApiResponse(code = 200, message = "Arquivo ou fragmento de arquivo particionado foi enviado com sucesso"),
            @ApiResponse(code = 401, message = "Usuário não efetuou login"),
            @ApiResponse(code = 500, message = "Ocorreu um erro no envio, e a causa é retornada na mensagem")
    })
    @PostMapping("/api/upload")
    public ResponseEntity<?> uploadFile(
    		@ApiParam( value = "Nome original do arquivo enviado", required = true )
    		@RequestParam("name") String name,
    		@ApiParam( value = "Dados do binários do arquivos ou parte de arquivo enviado", required = true )
            @RequestParam("file") MultipartFile uploadfile,
            @ApiParam( value = "Quantidade de fragmentos que o arquivo foi dividido (1 quando não foi fragmentado)", required = true )
            @RequestParam(required=false, defaultValue="-1") int chunks,
            @ApiParam( value = "Número na ordem sequencial do fragmento que está sendo enviado (0 quando primeiro ou única parte)", required = true )
            @RequestParam(required=false, defaultValue="-1") int chunk,
            @ApiParam( value = "Timestampo do envio da primeira parte", required = true )
            @RequestParam("startTime") String startTime,
            @ApiParam( value = "Informação de tamanho do arquivo enviado (já formatado na escala correta)", required = true )
            @RequestParam("sizeFile") String sizeFile) {

        if (uploadfile.isEmpty()) {
            return new ResponseEntity("Nenhum arquivo foi selecionado!", HttpStatus.OK);
        }

        try {
        	if (chunk == 0)
        	{
        		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        		uploadService.registerStartUpload(name, auth.getName(), chunks, sizeFile, Long.valueOf(startTime));
        	}
        	
        	if (uploadfile.getBytes().length == 0) {
                throw new IOException("Arquivo vazio!"); 
            }
        	
        	//Chama função que realiza o upload do arquivo e concatena as partes
        	uploadService.saveFile(name, uploadfile.getBytes(), (chunk == 0));
        	
        	if (chunk == (chunks-1))
        		uploadService.registerEndUpload(name, null);
                
        } catch (IOException e) {
        	uploadService.registerEndUpload(name, e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity("Concluído!", new HttpHeaders(), HttpStatus.OK);
    }
    
    
	@ApiOperation(value = "Solicita o download do arquivo pelo nome", response = ByteArrayResource.class, produces = "Array de bytes do arquivo")
    @RequestMapping(path = "/api/download/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<ByteArrayResource> downloadFile(
    		@ApiParam( value = "Nome do arquivo solicitado no servidor para download", required = true )
    		@PathVariable("fileName") String name) throws IOException {

		try
			{
				File file = uploadService.getFileStream(name);
		        byte[] bytes = uploadService.getByteStream(name);
		        ByteArrayResource resource = new ByteArrayResource(bytes);
			
		        HttpHeaders headers = new HttpHeaders();
		        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		        headers.add("Content-Disposition", "attachment");
		        headers.add("filename", name);
		        headers.add("Pragma", "no-cache");
		        headers.add("Expires", "0");
		        
		        return ResponseEntity.ok()
		                .headers(headers)
		                .contentLength(file.length())
		                .contentType(MediaType.parseMediaType("application/octet-stream"))
		                .body(resource);
		        
			} catch (NoSuchFileException ef) {
				byte[] bytes = "ATENCAO! Arquivo nao foi encontrado no servidor...".getBytes("UTF-8");
				ByteArrayResource resource = new ByteArrayResource(bytes);
				return new ResponseEntity<ByteArrayResource>(resource, HttpStatus.NOT_FOUND);
			}
			catch (IOException e) {
				byte a[] = "ATENCAO! Houve um problema para baixar o arquivo: \n".getBytes("UTF-8");
				byte b[] = e.getMessage().getBytes("UTF-8");

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
				outputStream.write( a );
				outputStream.write( b );

		        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray( ));
				return new ResponseEntity<ByteArrayResource>(resource, HttpStatus.INTERNAL_SERVER_ERROR);
	        }
    }
    
    
	@ApiOperation(value = "Solicita as informações de um arquivos enviado via upload e armazenado no servidor", response = UploadModel.class, produces = "JSON com informações do arquivo")
    @GetMapping("/api/fileInfo")
    @ResponseBody
    public UploadModel getFileInfo(
    		@ApiParam( value = "Nome do arquivo para recuperar as informações", required = true )
    		String fileName)
    {
    	return uploadService.getFileServer(fileName);
    }
    
    
	@ApiOperation(value = "Solicita a lista de arquivos do servidor para download, com informações detalhadas", response = UploadModel.class, responseContainer = "List", produces="Lista JSON com informações do arquivo")
    @GetMapping("/api/list")
    @ResponseBody
    public List<UploadModel> getListFilesServer()
    {
        return uploadService.getListFilesServer();
    }

}
