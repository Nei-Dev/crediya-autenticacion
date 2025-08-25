package com.crediya.usecase.crear_usuario;

import com.crediya.model.constants.LimitesSalarioBase;
import com.crediya.model.constants.MensajeError;
import com.crediya.model.usuario.RolUsuario;
import com.crediya.model.usuario.Usuario;
import com.crediya.model.usuario.gateways.UsuarioRepository;
import com.crediya.model.exceptions.usuario.UsuarioInvalidoException;
import com.crediya.model.exceptions.usuario.UsuarioYaExisteException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CrearUsuarioClienteClienteUseCaseTest {
    
    @InjectMocks
    private CrearUsuarioClienteClienteUseCase useCase;
    
    @Mock
    private UsuarioRepository usuarioRepository;

    private Usuario usuarioValido() {
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan");
        usuario.setApellidos("Perez");
        usuario.setCorreoElectronico("juan.perez@correo.com");
        usuario.setSalarioBase(LimitesSalarioBase.MINIMO + 1000);
        return usuario;
    }

    @Test
    void debeRegistrarUsuarioCorrectamente() {
        Usuario usuario = usuarioValido();
        when(usuarioRepository.buscarPorCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.empty());
        when(usuarioRepository.registrarUsuario(any(Usuario.class))).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.execute(usuario))
                .expectNextMatches(u -> u.getCorreoElectronico().equals(usuario.getCorreoElectronico()) && u.getRol() == RolUsuario.CLIENTE)
                .verifyComplete();
    }

    @Test
    void debeLanzarExcepcionSiUsuarioYaExiste() {
        Usuario usuario = usuarioValido();
        when(usuarioRepository.buscarPorCorreoElectronico(usuario.getCorreoElectronico())).thenReturn(Mono.just(usuario));

        StepVerifier.create(useCase.execute(usuario))
                .expectErrorMatches(e -> e instanceof UsuarioYaExisteException && e.getMessage().contains("ya existe"))
                .verify();
    }

    @Test
    void debeLanzarExcepcionSiUsuarioEsNulo() {
        StepVerifier.create(useCase.execute(null))
                .expectErrorMatches(e -> e instanceof UsuarioInvalidoException && e.getMessage().contains("nulo"))
                .verify();
    }

    @Test
    void debeLanzarExcepcionSiDatosInvalidos() {
        Usuario usuarioSinNombre = usuarioValido();
        usuarioSinNombre.setNombre("");
        StepVerifier.create(useCase.execute(usuarioSinNombre))
                .expectErrorMatches(e -> e instanceof UsuarioInvalidoException && e.getMessage().contains(MensajeError.NOMBRE_USUARIO_VACIO))
                .verify();

        Usuario usuarioSinApellidos = usuarioValido();
        usuarioSinApellidos.setApellidos("");
        StepVerifier.create(useCase.execute(usuarioSinApellidos))
                .expectErrorMatches(e -> e instanceof UsuarioInvalidoException && e.getMessage().equals(MensajeError.APELLIDOS_USUARIO_VACIO))
                .verify();

        Usuario usuarioCorreoInvalido = usuarioValido();
        usuarioCorreoInvalido.setCorreoElectronico("correo-invalido");
        StepVerifier.create(useCase.execute(usuarioCorreoInvalido))
                .expectErrorMatches(e -> e instanceof UsuarioInvalidoException && e.getMessage().equals(MensajeError.CORREO_ELECTRONICO_INVALIDO))
                .verify();

        Usuario usuarioSalarioInvalido = usuarioValido();
        usuarioSalarioInvalido.setSalarioBase(LimitesSalarioBase.MINIMO - 1);
        StepVerifier.create(useCase.execute(usuarioSalarioInvalido))
                .expectErrorMatches(e -> e instanceof UsuarioInvalidoException && e.getMessage().equals(MensajeError.SALARIO_BASE_INVALIDO))
                .verify();
        
        usuarioSalarioInvalido.setSalarioBase(-1000);
        StepVerifier.create(useCase.execute(usuarioSalarioInvalido))
                .expectErrorMatches(e -> e instanceof UsuarioInvalidoException && e.getMessage().equals(MensajeError.SALARIO_BASE_INVALIDO))
                .verify();
    }
}

