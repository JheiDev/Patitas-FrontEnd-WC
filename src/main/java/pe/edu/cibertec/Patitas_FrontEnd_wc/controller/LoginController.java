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
import reactor.core.publisher.Mono;


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


        try {

            // Invocar servicio de autenticación
            //String endpoint = "http://localhost:8090/autenticacion/login";
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);
            //LoginResponseDTO loginResponseDTO = webClientAuthentication.postForObject("/login", loginRequestDTO, LoginResponseDTO.class);

            Mono<LoginResponseDTO> monoLoginResponseDTO = webClientAuthentication.post()
                    .uri("/inicio")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class);

            // Bloquear la respuesta para obtener el resultado bloqueante o sincronico
            LoginResponseDTO loginResponseDTO = monoLoginResponseDTO.block();

            if (loginResponseDTO.codigo().equals("00")){

                LoginModel loginModel = new LoginModel("00", "", loginResponseDTO.nombreUsuario());
                model.addAttribute("loginModel", loginModel);
                return "principal";

            } else {

                LoginModel loginModel = new LoginModel("02", "Error: Autenticación fallida", "");
                model.addAttribute("loginModel", loginModel);
                return "inicio";

            }

        } catch(Exception e) {

            LoginModel loginModel = new LoginModel("99", "Error: Ocurrió un problema en la autenticación", "");
            model.addAttribute("loginModel", loginModel);
            System.out.println(e.getMessage());
            return "inicio";

        }


        /*try {

            LoginRequestDTO loginRequestDTO = new LoginRequestDTO(tipoDocumento, numeroDocumento, password);

            LoginResponseDTO response = webClientAuthentication
                    .post()
                    .uri("http://localhost:8081/vehiculo/inicio")
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
        }*/

    }

}

