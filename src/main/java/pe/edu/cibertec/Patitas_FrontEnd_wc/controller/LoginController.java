package pe.edu.cibertec.Patitas_FrontEnd_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pe.edu.cibertec.Patitas_FrontEnd_wc.DTO.LoginRequestDTO;
import pe.edu.cibertec.Patitas_FrontEnd_wc.DTO.LoginResponseDTO;
import pe.edu.cibertec.Patitas_FrontEnd_wc.ViewModel.LoginModel;


@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    WebClient webClientAuthentication;

    @GetMapping("/inicio")
    public String inicio(Model model){
        LoginModel loginModel = new LoginModel("00","","");
        model.addAttribute("loginModel",loginModel);
        return "inicio";
    }

    @PostMapping("/authentication")
    public String authentication(@RequestParam("tipoDocumento") String tipoDocumento,
                                 @RequestParam("numeroDocumento") String numeroDocumento,
                                 @RequestParam("password") String password,
                                 Model model) {

        if (tipoDocumento == null || tipoDocumento.trim().length() == 0 ||
                numeroDocumento == null || numeroDocumento.trim().length() == 0 ||
                password == null || password.trim().length() == 0) {

            LoginModel loginModel = new LoginModel("01", "Error: Debe completar correctamente sus credenciales", "");
            model.addAttribute("loginModel", loginModel);
            return "inicio";

        }

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);

        try {
            // Realizar la solicitud con WebClient
            LoginResponseDTO response = webClientAuthentication
                    .post()
                    .uri("/login")
                    .bodyValue(loginRequestDTO)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class)
                    .block();


            if (response != null && "00".equals(response.codigo())) {
                model.addAttribute("loginModel", response);
                return "principal";
            } else {
                model.addAttribute("loginModel", new LoginModel("01", "Credenciales Incorrectas", ""));
                return "inicio";
            }
        } catch (WebClientResponseException e) {
            model.addAttribute("loginModel", new LoginModel("99", "Error de conexión al servidor", ""));
            return "inicio";
        }

    }

}

