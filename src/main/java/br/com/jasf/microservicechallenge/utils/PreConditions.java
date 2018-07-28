package br.com.jasf.microservicechallenge.utils;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

/****************
 * PreConditions simples
 * 
 * @author jose
 *
 */
public class PreConditions {

	public static void checkNotNull(@Nullable Object value, @NonNull String parameterName)
			throws IllegalArgumentException {
		Assert.notNull(value, String.format("Argumento '%s' não pode ser nulo", parameterName));
	}

	public static void checkString(@Nullable String value, @NonNull String parameterName)
			throws IllegalArgumentException {
		Assert.notNull(value, String.format("Argumento '%s' não pode ser nulo", parameterName));
		if (value.isEmpty())
			throw new IllegalArgumentException(
					String.format("Argumento '%s' não pode ser o string vazio", parameterName));
	}
}
