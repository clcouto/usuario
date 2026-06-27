package br.edu.ccouto.usuario.business.converter;

import br.edu.ccouto.usuario.business.dto.EnderecoDTO;
import br.edu.ccouto.usuario.business.dto.TelefoneDTO;
import br.edu.ccouto.usuario.business.dto.UsuarioDTO;
import br.edu.ccouto.usuario.infrastructure.entity.Endereco;
import br.edu.ccouto.usuario.infrastructure.entity.Telefone;
import br.edu.ccouto.usuario.infrastructure.entity.Usuario;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsuarioConverter {

    public Usuario paraUsuario(UsuarioDTO usuarioDTO){

        return Usuario.builder()
                .email(usuarioDTO.getEmail())
                .nome(usuarioDTO.getNome())
                .senha(usuarioDTO.getSenha())
                .enderecos(paraListaEndereco(usuarioDTO.getEnderecos()))
                .telefones(paraListaTelefone(usuarioDTO.getTelefones()))
                .build();
    }

    //Metodo 1 - Utilizando laco FOR ao inves de stream e map
    public List<Endereco> paraListaEndereco(List<EnderecoDTO> enderecoDTOS){
        List<Endereco> enderecos = new ArrayList<>();
        for(EnderecoDTO enderecoDTO : enderecoDTOS){
            enderecos.add(paraEndereco(enderecoDTO));
        }
        return enderecos;

    }

    public Endereco paraEndereco(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .estado(enderecoDTO.getEstado())
                .cidade(enderecoDTO.getCidade())
                .cep(enderecoDTO.getCep())
                .build();
    }

    //Metodo 2 - Utilizando stream e map no lugar de for
    public List<Telefone> paraListaTelefone(List<TelefoneDTO telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefone).toList();

    }

    public Telefone paraTelefone(TelefoneDTO telefoneDTO){
        return Telefone.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

//---------------------------------------------------------------------------------
public UsuarioDTO paraUsuarioDTO(Usuario usuarioDTO){
    return UsuarioDTO.builder()
            .email(usuarioDTO.getEmail())
            .nome(usuarioDTO.getNome())
            .senha(usuarioDTO.getSenha())
            .enderecos(paraListaEnderecoDTO(usuarioDTO.getEnderecos()))
            .telefones(paraListaTelefoneDTO(usuarioDTO.getTelefones()))
            .build();
}

    //Metodo 1 - Utilizando laco FOR ao inves de stream e map
    public List<EnderecoDTO> paraListaEnderecoDTO(List<Endereco> enderecoDTOS){
        List<EnderecoDTO> enderecos = new ArrayList<>();
        for(Endereco enderecoDTO : enderecoDTOS){
            enderecos.add(paraEnderecoDTO(enderecoDTO));
        }
        return enderecos;

    }

    public EnderecoDTO paraEnderecoDTO(Endereco enderecoDTO){
        return EnderecoDTO.builder()
                .rua(enderecoDTO.getRua())
                .numero(enderecoDTO.getNumero())
                .complemento(enderecoDTO.getComplemento())
                .estado(enderecoDTO.getEstado())
                .cidade(enderecoDTO.getCidade())
                .cep(enderecoDTO.getCep())
                .build();
    }

    //Metodo 2 - Utilizando stream e map no lugar de for
    public List<TelefoneDTO> paraListaTelefoneDTO(List<Telefone telefoneDTOS){
        return telefoneDTOS.stream().map(this::paraTelefoneDTO).toList();

    }

    public TelefoneDTO paraTelefoneDTO(Telefone telefoneDTO){
        return TelefoneDTO.builder()
                .numero(telefoneDTO.getNumero())
                .ddd(telefoneDTO.getDdd())
                .build();
    }

}
