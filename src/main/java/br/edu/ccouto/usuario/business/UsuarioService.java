package br.edu.ccouto.usuario.business;

import br.edu.ccouto.usuario.business.converter.UsuarioConverter;
import br.edu.ccouto.usuario.business.dto.UsuarioDTO;
import br.edu.ccouto.usuario.infrastructure.entity.Usuario;
import br.edu.ccouto.usuario.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        //usuario = usuarioRepository.save(usuario);
        //Simplificando as duas linhas acima
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

}
