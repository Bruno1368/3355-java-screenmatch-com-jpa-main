package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadoSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBuscada;

    public Principal(SerieRepository respositorio) {
        this.repositorio = respositorio;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listas séries buscadas
                    4 - Buscar série por titulo
                    5 - Buscar séries por ator
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries
                    9 - Buscar episódio por trecho
                    10 - Buscar os melhores episódios por série
                    11 - Buscar episódios a partir de uma data 
                    0 - Sair                      
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorCategoria();
                    break;
                case 8:
                    buscaPorTemporada();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosPorData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarEpisodiosPorData() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            System.out.println("Digite o ano limite do lançamento: ");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();
            Serie serie = serieBuscada.get();
            List<Episodio> episodiosAno = repositorio.episodioPorData(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }

    }

    private void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e ->
                    System.out.printf("Série: %s, Temporada %s - Episódio %s - %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEpisodio(), e.getTitulo()));
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual episódio para busca: ");
        var trechoEpisodio = leitura.nextLine();
        List<Episodio> episodios = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodios.forEach(e ->
                System.out.printf("Série: %s, Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEpisodio(), e.getTitulo()));

    }

    private void buscaPorTemporada() {
        System.out.println("Você quer uma série até quantas temporadas ?");
        var temporadas = leitura.nextInt();
        System.out.println("Qual a avaliação do seriado que você quer assistir ? ");
        var avaliacao = leitura.nextDouble();

        List<Serie> seriePorTemporada = repositorio.seriesPorTemporadaEAvaliacao(temporadas, avaliacao);

        seriePorTemporada.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao() + ", temporadas: " + s.getTotalTemporadas()));
    }

    private void buscarSeriePorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero ? ");
        var nomeGenero = leitura.nextLine();
        var categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Séries da categoria " + categoria + ":");
        seriesPorCategoria.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao() + ", gênero: " + s.getGenero()));

    }

    private void buscarTop5Series() {
        List<Serie> topSeries = repositorio.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("Top 5 séries: ");
        topSeries.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));

    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator que você deseja pesquisar: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Você deseja ver séries a partir de qual nota de avaliação ? ");
        var notaAvaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, notaAvaliacao);
        if (seriesEncontradas.isEmpty()) {
            System.out.println("Série não encontrada");
        } else {
            System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
            seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
        }

    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var nomeSerie = leitura.nextLine();
        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada");
        }

    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repositorio.save(serie);
        dadoSeries.add(dados);
        System.out.println(dados);


    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();

        System.out.println("Escolha uma série pelo nome");
        var nomeSerie = leitura.nextLine();


        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);
        if (serie.isPresent()) {
            List<DadosTemporada> temporadas = new ArrayList<>();
            Serie serieEncontrada = serie.get();
            for (int i = 1; i < serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(t -> t.episodios().stream()
                            .map(e -> new Episodio(t.numero(), e))).collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        } else {
            System.out.println("Série não encontrada");
        }


//        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
//            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") + "&season=" + i + API_KEY);
//            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
//            temporadas.add(dadosTemporada);
//        }
//        temporadas.forEach(System.out::println);
    }
}