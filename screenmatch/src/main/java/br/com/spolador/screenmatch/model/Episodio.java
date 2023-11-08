package br.com.spolador.screenmatch.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Getter @Setter @ToString
public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.titulo = dadosEpisodio.getTituloEpisodio();
        this.numeroEpisodio = dadosEpisodio.getNumeroEpisodio();

        try{
            this.avaliacao = Double.valueOf(dadosEpisodio.getAvaliacao());
        } catch (NumberFormatException ex){
            this.avaliacao = 0.0;
        }

        try{
            this.dataLancamento = LocalDate.parse(dadosEpisodio.getDataDeLancamento());
        } catch (DateTimeParseException ex){
            this.dataLancamento = null;
        }
    }
}
