package com.dm.ecommerce.service;

import com.dm.ecommerce.DTOs.LoginRequestDTO;
import com.dm.ecommerce.DTOs.UsuarioResponseDTO;
import com.dm.ecommerce.DTOs.UsuarioRequestDTO;
import com.dm.ecommerce.entity.Usuario;
import com.dm.ecommerce.repositories.PedidoRepository;
import com.dm.ecommerce.repositories.UsuarioRepository;
import com.dm.ecommerce.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final JwtService jwtService;

    public UsuarioService(PasswordEncoder passwordEncoder, UsuarioRepository usuarioRepository,
                          PedidoRepository pedidoRepository, JwtService jwtService) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.jwtService = jwtService;
    }

    public String saveUsuario(UsuarioRequestDTO usuarioRequestDTO) {
        String senhaCriptografada = passwordEncoder.encode(usuarioRequestDTO.getSenha());
        Usuario usuario = new Usuario(usuarioRequestDTO.getNome(), usuarioRequestDTO.getEmail(),
                usuarioRequestDTO.getTelefone(), senhaCriptografada, usuarioRequestDTO.getRoles());
        usuarioRepository.save(usuario);
        return "Usuário criado com sucesso.";
    }

    public String login(LoginRequestDTO loginRequestDTO) {
        Usuario findUsuario = usuarioRepository.findByEmail(loginRequestDTO.getEmail());

        if (findUsuario == null) {
            return "{\"error\": \"Usuário não encontrado.\", \"status\": 401}";
        }

        if (!passwordEncoder.matches(loginRequestDTO.getSenha(), findUsuario.getSenha())) {
            return "{\"error\": \"Senha incorreta.\", \"status\": 401}";
        }

        String token = jwtService.generateToken(findUsuario.getEmail());
        return "{\"message\": \"Logado com sucesso.\", \"token\": \"" + token + "\", \"status\": 200}";
    }

    public List<UsuarioResponseDTO> mostrar() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(UsuarioResponseDTO::new).toList();
    }

    public String deleteUsuario(UUID id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return "Usuário deletado com sucesso.";
        } else {
            return "ID inválido.";
        }
    }
}