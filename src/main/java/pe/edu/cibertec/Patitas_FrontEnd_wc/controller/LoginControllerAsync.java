package pe.edu.cibertec.Patitas_FrontEnd_wc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.cibertec.Patitas_FrontEnd_wc.DTO.LoginRequestDTO;
import pe.edu.cibertec.Patitas_FrontEnd_wc.DTO.LoginResponseDTO;
import pe.edu.cibertec.Patitas_FrontEnd_wc.ViewModel.LoginModel;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:5176")
public class LoginControllerAsync {

    @Autowired
    WebClient webClientAuthentication;


    @PostMapping("/authentication-async")
    public Mono<LoginResponseDTO> autenticar(@RequestBody LoginRequestDTO loginRequestDTO) {

        // Validar campos de entrada
        if (loginRequestDTO.tipoDocumento() == null || loginRequestDTO.tipoDocumento().trim().length() == 0 ||
                loginRequestDTO.numeroDocumento() == null || loginRequestDTO.numeroDocumento().trim().length() == 0 ||
                loginRequestDTO.password() == null || loginRequestDTO.password().trim().length() == 0) {

            return Mono.just(new LoginResponseDTO("01","Error: Debe completar sus credenciales", "",
                    ""));

        }

        try {
            return webClientAuthentication.post()
                    .uri("/login")
                    .body(Mono.just(loginRequestDTO), LoginRequestDTO.class)
                    .retrieve()
                    .bodyToMono(LoginResponseDTO.class)
                    .flatMap(response -> {

                        if (response.codigo().equals("00")){
                            return Mono.just(
                                    new LoginResponseDTO("00","",
                                            response.nombreUsuario(), ""));
                        }else{
                            return Mono.just(
                                    new LoginResponseDTO(
                                            "02","Error: Autenticacion fallida","",""));
                        }

                    });



        } catch(Exception e) {

            System.out.println(e.getMessage());
            return Mono.just(new LoginResponseDTO("99", "Error: Ocurrió un problema en la autenticación", "", ""));
        }


//        //Llamada al servicio de auntenticacion del backend
//        String backendurl = "http://localhost:8090/autenticacion/login";
//        LoginRq loginRq = new LoginRq(tipoDocumento, numeroDocumento, password);
//        LoginModel loginModel = restTemplate.postForObject(backendurl, loginRq, LoginModel.class);
//
//        if (loginModel != null && "00".equals(loginModel.codigo())) {
//            model.addAttribute("loginModel", loginModel);
//            return "principal";
//        }
//        model.addAttribute("loginModel", new LoginModel("01", "Credenciales erroneas..", ""));
//        return "inicio";


    }
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





