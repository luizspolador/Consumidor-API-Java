package br.com.spolador.screenmatch.principal;

import br.com.spolador.screenmatch.model.DadosEpisodio;
import br.com.spolador.screenmatch.model.DadosSerie;
import br.com.spolador.screenmatch.model.DadosTemporada;
import br.com.spolador.screenmatch.model.Episodio;
import br.com.spolador.screenmatch.service.ConsumoAPI;
import br.com.spolador.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversorDeDados = new ConverteDados();
    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";


    public void exibeMenu(){
        System.out.println("digite o nome da serie: ");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = conversorDeDados.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i=1; i<=dadosSerie.getTotalTemporadas(); i++){
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") +"&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = conversorDeDados.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

//        for(int i=0; i< dadosSerie.getTotalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTeporada = temporadas.get(i).getEpisodios();
//            for(int j=0; j< episodiosTeporada.size(); j++){
//                System.out.println(episodiosTeporada.get(j).getTituloEpisodio());
//            }
//        }
        temporadas.forEach(t -> t.getEpisodios().forEach(e -> System.out.println(e.getTituloEpisodio())));

        //dentro da lista possui outras listas e gostaria de reuni-las em uma lista - nesse caso, aglutina todos os episodios de todas as temporadas

        List<DadosEpisodio> dadosEpisodios = temporadas.stream() // fluxo de dados que eram temporadas
                .flatMap(t -> t.getEpisodios().stream())  // me gera um fluxo de dadosepisodios de todas as temporadas
                .collect(Collectors.toList()); // coleta para uma lista -> QUE É MUTÁVEL
//                .toList(); // GERARIA UMA LISTA IMUTÁVEL

        // ordernar os episodios por ordem decrescente de avaliacao
        System.out.println("\nTOP 10 episódios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.getAvaliacao().equalsIgnoreCase("N/A")) // ignora os episodios que possuem avaliação N/A
                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
                .sorted(Comparator.comparing(DadosEpisodio::getAvaliacao).reversed())
                .peek(e -> System.out.println("Ordenacao " + e))
                .limit(10)
                .peek(e -> System.out.println("Limit " + e))
                .map(e -> e.getTituloEpisodio().toUpperCase())
                .peek(e -> System.out.println("Mapeamento " + e))
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.getEpisodios().stream()
                        .map(d -> new Episodio(t.getNumero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("Informe um trecho do título: ");
        var trechoTitulo = leitura.nextLine();

        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado");
            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
        } else {
            System.out.println("Episodio não encontrado");
        }

        System.out.println("A partir de qual ano deseja ver os episódios? ");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        episodios.stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada:  " + e.getTemporada() + "Episódio: " + e.getNumeroEpisodio() + " Data lanãmento: "
                                + e.getDataLancamento().format(dtf)
                ));

        //pegando as avaliações dos episodios por temporada e calculando a media por temporada
        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0) // pega os episodios que possuem avaliação maior que 0
                .collect(Collectors.groupingBy(Episodio::getTemporada, Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics statistics = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("média: " + statistics.getAverage());
        System.out.println("min: " + statistics.getMin());
        System.out.println("max: " + statistics.getMax());
        System.out.println("qtd ep: " + statistics.getCount());

        // tmb é possível utilizar IntSummaryStatistics

    }
}



//EXEMPLOS DE LAMBDA
//List<Integer> lista = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
//
//for(Integer i: lista) {
//        if(i % 2 == 0) {
//        System.out.println(i);
//        }
// }

//lista.stream().filter(i -> i % 2 == 0).forEach(System.out::println);




//STREAMS
//List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//
//    List<Integer> numerosPares = numeros.stream()
//            .filter(n -> n % 2 == 0)
//            .collect(Collectors.toList());
//
//System.out.println(numerosPares); // Output: [2, 4, 6, 8, 10]




//    List<String> nomes = Arrays.asList("João", "Maria", "Pedro", "Ana");
//
//nomes.stream()
//        .forEach(nome -> System.out.println("Olá, " + nome + "!"));




//    List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
//
//    Set<Integer> numerosPares = numeros.stream()
//            .filter(n -> n % 2 == 0)
//            .collect(Collectors.toSet());
//
//System.out.println(numerosPares); // Output: [2, 4, 6, 8, 10]


//public class ExemploFindAnyParallelStream {
//    public static void main(String[] args) {
//        List<Integer> numeros = new ArrayList<>();
//        for (int i = 1; i <= 100; i++) {
//            numeros.add(i);
//        }
//
//        // Utilizando parallelStream para encontrar um elemento qualquer em paralelo
//        Optional<Integer> numeroQualquer = numeros.parallelStream()
//                .filter(numero -> numero % 10 == 0) // Filtra os números que são múltiplos de 10
//                .findAny();
//
//        if (numeroQualquer.isPresent()) {
//            System.out.println("Encontrado: " + numeroQualquer.get());
//        } else {
//            System.out.println("Nenhum número encontrado.");
//        }
//    }
//}



//    filtra as transações com valores superiores a 5000, imprime um registro de cada uma com o método peek,
//        coleta os resultados em um Set e retorna o primeiro elemento do Set.

//transacoes.stream()
//        .filter(t -> t.getValor() > 5000)
//        .peek(System.out::println)
//        .collect(Collectors.toSet()).stream()
//        .findFirst();


//MAP

//    List<String> palavras = Arrays.asList("Java", "Stream", "Operações", "Intermediárias");
//
//    List<Integer> tamanhos = palavras.stream()
//            .map(s -> s.length())
//            .collect(Collectors.toList());
//
//System.out.println(tamanhos); // Output: [4, 6, 11, 17]




// calculando o periodo de idade

//    public int getIdade() {
//        Period periodo = Period.between(nascimento, LocalDate.now());
//        return periodo.getYears();
//    }



