package br.com.jasf.microservicechallenge.utils;

/**
 * 
 * Representa um valor que pode ser passado "por referência". O objetivo é ser
 * usado dentro de Lambdas para permitir a alteração de valores locais.
 * <p>
 * Exemplo:
 * 
 * <pre>
 * {@code
 * void localFunction()
 * {
 *    Boxed<Interger> n = new Boxed<>(0); 
 *    
 *    visitElements( ()->{
 *    	n.value++;
 *    }); 
 *    
 * }}
 * </pre>
 * 
 * @param <V> o tipo de dados para o boxing
 * 
 * @author jose
 * 
 */
public class Boxed<V> {
	public V value;

	public Boxed() {
	}

	public Boxed(V v) {
		this.value = v;
	}
}
