package br.com.spolador.desafiofipe.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosModelos(List<DadosVeiculos> modelos) {
}
