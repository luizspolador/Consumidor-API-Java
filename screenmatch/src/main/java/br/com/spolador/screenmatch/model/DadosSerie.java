package br.com.spolador.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

@ToString @Getter
@JsonIgnoreProperties(ignoreUnknown = true) // ignora o que não foi informado abaixo
public class DadosSerie {
    @JsonAlias("Title") // apenas para leitura de Json
    private String tituloSerie;
    @JsonAlias("totalSeasons")
    private Integer totalTemporadas;
    @JsonAlias("imdbRating")
    private String avaliacao;


    //@JsonProperty -> serve para serialização (convertido de objetos Java para JSON) e desserialização (convertido de JSON para objetos Java)
}
