package br.com.spolador.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString @Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosTemporada {
    @JsonAlias("Season")
    private Integer numero;
    @JsonAlias("Episodes")
    List<DadosEpisodio> episodios;
}
