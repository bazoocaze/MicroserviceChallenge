package br.com.jasf.microservicechallenge.utils;

import br.com.jasf.microservicechallenge.data.UrlWhitelistDAO;

/**
 * Representa um booleano que pode ser passado "por referência".
 * O objeto é ser usado dentro de Lambdas para permitir
 * a alteração de valores locais.
 * @see UrlWhitelistDAO
 */
public class BooleanReference {
	public boolean value;
	
	public BooleanReference()
	{
		this.value = false;
	}
	
	public BooleanReference(boolean setVal)
	{
		this.value = setVal;
	}
}
