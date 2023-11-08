package br.com.spolador.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@ToString @Getter

@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosEpisodio {
    @JsonAlias("Title")
    private String tituloEpisodio;
    @JsonAlias("Episode")
    private Integer numeroEpisodio;
    @JsonAlias("imdbRating")
    private String avaliacao;
    @JsonAlias("Released")
    private String dataDeLancamento;

}
