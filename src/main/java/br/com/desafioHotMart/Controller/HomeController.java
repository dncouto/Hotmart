package br.com.desafioHotMart.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.ApiOperation;

@Controller
public class HomeController {

	@ApiOperation(value = "Carrega a página do site", response = String.class)
    @GetMapping("/")
    public String index(Authentication authentication) {
    	return "upload";
    }

    @ApiOperation(value = "Mensagem de despedida do logout do site", response = String.class)
    @RequestMapping(value="/exit", method = RequestMethod.GET)
    public ResponseEntity logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
            new SecurityContextLogoutHandler().logout(request, response, auth);
            auth.setAuthenticated(false);
        }
        
        HttpSession session= request.getSession(false);
        SecurityContextHolder.clearContext();
        session= request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(org.springframework.http.MediaType.TEXT_PLAIN);
        return new ResponseEntity("Fim da sessao!", httpHeaders, HttpStatus.OK);
    }
}