package com.crediya.model.constants;

public class MensajeError {
	
	private MensajeError() {}
	
	public static final String NOMBRE_USUARIO_VACIO = "El nombre del usuario no puede ser nulo o estar vacío";
	public static final String APELLIDOS_USUARIO_VACIO = "Los apellidos del usuario no pueden ser nulos o estar vacíos";
	public static final String CORREO_ELECTRONICO_VACIO = "El correo electrónico no puede ser nulo o estar vacío";
	public static final String CORREO_ELECTRONICO_INVALIDO = "El correo electrónico no es válido";
	public static final String SALARIO_BASE_INVALIDO = String.format(
	   "El salario base debe ser un número positivo entre %d y %d",
	   LimitesSalarioBase.MINIMO, LimitesSalarioBase.MAXIMO
	);
	public static final String ROL_USUARIO_VACIO = "El rol del usuario no puede ser nulo";
	
}
