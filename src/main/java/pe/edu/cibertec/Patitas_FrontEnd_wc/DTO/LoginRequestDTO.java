package pe.edu.cibertec.Patitas_FrontEnd_wc.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginRequestDTO(String tipoDocumento, String numeroDocumento, String password) {}