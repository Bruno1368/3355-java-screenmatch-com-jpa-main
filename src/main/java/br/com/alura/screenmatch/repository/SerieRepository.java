package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;


//serie repository, extende do jparepository, para utilizar os metodos do jpa, passando como parametro a serie e o tipo do id em serie, que é um long
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
