package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Plot")String sinopse,
                         @JsonAlias("Actors")String atores,
                         @JsonAlias("Genre")String genero,
                         @JsonAlias("Poster")String poster
) {

    @Override
    public String toString() {
        return "Titulo: " + this.titulo + ", total de temporadas: " + totalTemporadas + ", avaliação: " + avaliacao + ", gênero: " + genero + ", atores: " + atores + ", resumo: " + sinopse + poster;
    }
}
