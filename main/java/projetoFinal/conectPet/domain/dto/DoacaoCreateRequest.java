package projetoFinal.conectPet.domain.dto;

import lombok.Data;
import projetoFinal.conectPet.domain.entity.UsuarioEntity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;
//import com.javatechie.entity.ImageData;

@Data
public class DoacaoCreateRequest {

    private MultipartFile image;

    private String jsonData ;

}
