package com.crediya.model.usuario;

import com.crediya.model.constants.LimitesSalarioBase;
import com.crediya.model.constants.MensajeError;
import com.crediya.model.exceptions.UsuarioInvalidoException;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
	
	private Long idUsuario;
	private String nombre;
	private String apellidos;
	private LocalDate fechaNacimiento;
	private String direccion;
	private String telefono;
	private String correoElectronico;
	private Integer salarioBase;
	private RolUsuario rol;
	
	public void validar() {
		validarNombres(this.nombre);
		validarApellidos(this.apellidos);
		validarCorreoElectronico(this.correoElectronico);
		validarSalarioBase(this.salarioBase);
		validarRol(this.rol);
	}

	private static void validarNombres(String nombres) {
		if (nombres == null || nombres.trim().isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.NOMBRE_USUARIO_VACIO);
		}
	}

	private static void validarApellidos(String apellidos) {
		if (apellidos == null || apellidos.trim().isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.APELLIDOS_USUARIO_VACIO);
		}
	}

	private static void validarCorreoElectronico(String correoElectronico) {
		if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
			throw new UsuarioInvalidoException(MensajeError.CORREO_ELECTRONICO_VACIO);
		}
		if (!correoElectronico.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
			throw new UsuarioInvalidoException(MensajeError.CORREO_ELECTRONICO_INVALIDO);
		}
	}

	private static void validarSalarioBase(Integer salarioBase) {
		if (salarioBase == null || salarioBase < LimitesSalarioBase.MINIMO || salarioBase > LimitesSalarioBase.MAXIMO) {
			throw new UsuarioInvalidoException(MensajeError.SALARIO_BASE_INVALIDO);
		}
	}
	
	private static void validarRol(RolUsuario rol) {
		if (rol == null) {
			throw new UsuarioInvalidoException(MensajeError.ROL_USUARIO_VACIO);
		}
	}

}
