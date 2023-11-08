package br.com.spolador.screenmatch.service;

public interface IConverteDados {
    // recebe um Json que é uma string, recebe uma classe
    <T> T obterDados(String json, Class<T> classe);
}
