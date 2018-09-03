package br.com.jasf.microservicechallenge.data;

import java.util.function.Function;

import org.springframework.dao.DataAccessException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/********************
 * Interface de acesso ao repositório de whitelist.
 * 
 * @author jose
 *
 */
public interface UrlWhitelistDAO {

	/*****************************
	 * Insere o regex na whitelist para o cliente informado (null para lista
	 * global). Não duplica caso já exista a regex para o cliente informado.
	 * 
	 * @param client Identificação do cliente (ou null para lista global)
	 * @param regex  Regex a ser inserida (ignora caso regex já cadastrada).
	 * @return True/false se a regex foi inserida.
	 * @throws DataAccessException Em caso de erro de sql/banco de dados
	 */
	boolean insertRegex(@Nullable String client, @NonNull String regex) throws DataAccessException;

	/*****************************
	 * Executa o delegate para cada entrada da whitelist do cliente informado (null
	 * para lista global).
	 * 
	 * @param client Identificação do cliente (ou null para lista global)
	 * @param action Delegate a ser executado para cada item da whitelist. A busca
	 *               termina quando o delegate retornar true.
	 * @return Retorna true se o delegate encontrou a informação (retornou true) ou
	 *         false caso contrário.
	 */
	boolean forEach(@Nullable String client, @NonNull Function<UrlWhitelistItem, Boolean> action);

}