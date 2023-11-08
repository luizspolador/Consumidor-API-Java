package br.com.spolador.desafiofipe.service;

import java.util.List;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);

    <T>List<T> listarDados(String json, Class<T> classe);
}
