package projetoFinal.conectPet.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import projetoFinal.conectPet.domain.dto.DoacaoCreateRequest;
import projetoFinal.conectPet.domain.dto.DoacaoResponse;
import projetoFinal.conectPet.domain.dto.DoacaoUpdateRequest;
import projetoFinal.conectPet.domain.entity.DoacaoEntity;
import projetoFinal.conectPet.domain.entity.UsuarioEntity;
import projetoFinal.conectPet.exception.DoacaoInvalidoException;
import projetoFinal.conectPet.exception.DoacaoNaoEncontradaException;
import projetoFinal.conectPet.repository.DoacoesRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
public class DoacoesService {
    private final String FOLDER_PATH="c://Users/Acer/Fotos/";
    @Autowired
    private DoacoesRepository doacoesRepository;
    private static List<DoacaoEntity> listaDeDoacao;

    ObjectMapper objectMapper = new ObjectMapper();
    private static int proximoId = 1;

    public DoacoesService(){
        if (listaDeDoacao == null){
            listaDeDoacao = new ArrayList<>();
            listaDeDoacao.add(new DoacaoEntity(proximoId++,"Scooby","cachorro", 1 ,5 , 4  , "teste",new UsuarioEntity()));
        }
    }


    public DoacaoEntity criarDoacao (MultipartFile image, String jsonData) throws IOException{
        //caminho da pasta que sera armazenado a imagem
        String filePath = FOLDER_PATH+image.getOriginalFilename();
        //transforma o jsonData em um objeto
        DoacaoResponse doacaoRequest = objectMapper.readValue(jsonData, DoacaoResponse.class);

        DoacaoEntity novaDoacao = new DoacaoEntity
                (proximoId++,
                        doacaoRequest.getNome(),
                        doacaoRequest.getEspecie(),
                        doacaoRequest.getIdade(),
                        doacaoRequest.getNivelDeFofura(),
                        doacaoRequest.getNivelDeCarencia(),
                        filePath,
                        doacaoRequest.getUsuario()
                 ); //        filePath,

        //salva imagem no caminho local
        image.transferTo(new File(filePath));

        //salva no banco
        doacoesRepository.save(novaDoacao);
        return novaDoacao;
    }

    public List<DoacaoEntity> listar(){
        return listaDeDoacao;
    }


    public DoacaoEntity buscarPorId(int idDoacao) {
        var doacaoEncontrada = listaDeDoacao.stream()
                .filter(doacao -> doacao.getId() == idDoacao)
                .findFirst();

        if(doacaoEncontrada.isEmpty()){
            throw new DoacaoNaoEncontradaException();
        }

        return doacaoEncontrada.get();
    }

    //metodo buscarPorNome usado no Controller
    public DoacaoEntity buscarPorNome(String nome){
        var doacaoEncontrada = listaDeDoacao.stream()
                .filter(doacao -> doacao.getNome().equals(nome))
                .findFirst();

        if (doacaoEncontrada.isEmpty()){
            throw new DoacaoNaoEncontradaException();
        }

        return doacaoEncontrada.get();
    }

    //metodo buscarPorUsuario usado no Controller
    public DoacaoEntity buscarDoacaoPorUsuario(String usuario){
        var doacaoEncontrada = listaDeDoacao.stream()
                .filter(doacao -> doacao.getUsuario().equals(usuario))
                .findAny();

        if (doacaoEncontrada.isEmpty()){
            throw new DoacaoNaoEncontradaException();
        }

        return doacaoEncontrada.get();
    }


    public DoacaoEntity deletarDoacao (int idDoacao){
        var doacaoEncontrada = listaDeDoacao.stream()
                .filter(doacao -> doacao.getId() == idDoacao)
                .findFirst();

        if (doacaoEncontrada.isEmpty()){
            throw new DoacaoNaoEncontradaException();
        }

        var doacao = doacaoEncontrada.get();
        listaDeDoacao.remove(doacao);

        return doacao;
    }

    public DoacaoEntity atualizarDoacao (int idDoacao , DoacaoUpdateRequest doacaoUpdateRequest){

        var doacaoEncontrada = listaDeDoacao.stream()
                .filter(doacao -> doacao.getId() == idDoacao)
                .findFirst();

        if (doacaoEncontrada.isEmpty()){
            throw new DoacaoNaoEncontradaException();
        }

        var doacao = doacaoEncontrada.get();
        doacao.setNome(doacaoUpdateRequest.getNome());
        doacao.setEspecie(doacaoUpdateRequest.getEspecie());
        doacao.setIdade(doacaoUpdateRequest.getIdade());
        doacao.setNivelDeFofura(doacaoUpdateRequest.getNivelDeFofura());
        doacao.setNivelDeCarencia(doacaoUpdateRequest.getNivelDeCarencia());

        return doacaoEncontrada.get();

    }



}
