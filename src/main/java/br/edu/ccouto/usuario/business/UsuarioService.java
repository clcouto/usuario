package br.edu.ccouto.usuario.business;

import br.edu.ccouto.usuario.business.converter.UsuarioConverter;
import br.edu.ccouto.usuario.business.dto.EnderecoDTO;
import br.edu.ccouto.usuario.business.dto.TelefoneDTO;
import br.edu.ccouto.usuario.business.dto.UsuarioDTO;
import br.edu.ccouto.usuario.infrastructure.entity.Endereco;
import br.edu.ccouto.usuario.infrastructure.entity.Telefone;
import br.edu.ccouto.usuario.infrastructure.entity.Usuario;
import br.edu.ccouto.usuario.infrastructure.exceptions.ConflictException;
import br.edu.ccouto.usuario.infrastructure.exceptions.ResourceNotFoundException;
import br.edu.ccouto.usuario.infrastructure.repository.EnderecoRepository;
import br.edu.ccouto.usuario.infrastructure.repository.TelefoneRepository;
import br.edu.ccouto.usuario.infrastructure.repository.UsuarioRepository;
import br.edu.ccouto.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvaUsuario(UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        //usuario = usuarioRepository.save(usuario);
        //Simplificando as duas linhas acima
        return usuarioConverter.paraUsuarioDTO(
                usuarioRepository.save(usuario)
        );
    }

    public void emailExiste(String email){
        try{
            boolean existe = verificaEmailExistente(email);
            if(existe){
                throw new ConflictException("E-mail já cadastrado" + email);
            }

        } catch (ConflictException e){
            throw new ConflictException("E-mail já cadastrado" + e.getCause());

        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(
                    usuarioRepository.findByEmail(email)
                            .orElseThrow(
                    () -> new ResourceNotFoundException("Email não encontrado " + email)
                            )
            );
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("E-mail não encontrado " + email);
        }
    }

    public void deletarUsuarioPorEmail(String email){usuarioRepository.deleteByEmail(email);}

    public UsuarioDTO atualizaDadosUsuario(String token, UsuarioDTO usuarioDTO){
        String email = jwtUtil.extractUsername(token.substring(7));

        usuarioDTO.setSenha(usuarioDTO.getSenha() != null ? passwordEncoder.encode(usuarioDTO.getSenha()) : null );

        Usuario usuarioEntity = usuarioRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("E-mail não localizado "));

        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO,usuarioEntity);

        //usuario.setSenha(passwordEncoder.encode(usuario.getPassword()));
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEndereco(Long idEndereco, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(idEndereco).orElseThrow(
                () -> new ResourceNotFoundException("ID não encontrado " + idEndereco));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO atualizaTelefone(Long idTelefone, TelefoneDTO telefoneDTO){
        Telefone entity = telefoneRepository.findById(idTelefone).orElseThrow(
                () -> new ResourceNotFoundException("ID não encontrado " + idTelefone));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, entity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}

