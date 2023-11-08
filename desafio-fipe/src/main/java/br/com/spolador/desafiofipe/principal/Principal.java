package br.com.spolador.desafiofipe.principal;

import br.com.spolador.desafiofipe.model.DadosAutomovel;
import br.com.spolador.desafiofipe.model.DadosModelos;
import br.com.spolador.desafiofipe.model.DadosVeiculos;
import br.com.spolador.desafiofipe.service.ConsumoAPI;
import br.com.spolador.desafiofipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();
    private final String ENDERECO_PRINCIPAL = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeDadosAPI() {
        var menu = """
                ----- OPÇÕES DE VEÍCULOS -----
                * Carro
                * Moto
                * Caminhão
                                
                Escolha uma das opções para consulta: 
                """;
        System.out.println(menu);
        var tipoVeiculo = sc.nextLine();
        String endereco;

        if (tipoVeiculo.toLowerCase().contains("carr")) {
            endereco = ENDERECO_PRINCIPAL + "carros/marcas";
        } else if (tipoVeiculo.toLowerCase().contains("mot")) {
            endereco = ENDERECO_PRINCIPAL + "motos/marcas";
        } else {
            endereco = ENDERECO_PRINCIPAL + "caminhoes/marcas";
        }

        var json = consumoAPI.obterDados(endereco);
        System.out.println("Veículos: " + json);
        // -----------------------------------------------------------------------------------------------------------

        var marcas = converteDados.listarDados(json, DadosVeiculos.class);
        marcas.stream().sorted(Comparator.comparing(DadosVeiculos::codigo)).forEach(System.out::println); // exibe todas as marcas por código

        System.out.println("Informe o código da marca do " + tipoVeiculo + " que deseja consultar: ");
        var codigoMarca = sc.nextLine();
        endereco += "/" + codigoMarca + "/modelos";
        json = consumoAPI.obterDados(endereco);
        var modeloLista = converteDados.obterDados(json, DadosModelos.class);
        System.out.println("Modelos da marca esolhida: ");
        modeloLista.modelos().stream().sorted(Comparator.comparing(DadosVeiculos::codigo)).forEach(System.out::println); // exibe todos os modelos de acordo com codigo da marca digitada
        // -----------------------------------------------------------------------------------------------------------

        System.out.println("Digite um trecho do nome do " + tipoVeiculo + " a ser buscado: ");
        var nomeVeiculo = sc.nextLine();
        //pega a lista de modelos filtrados pela marca, filtra com um nome parecido com que o usuário digitou e joga em uma nova lista
        List<DadosVeiculos> modelosFiltrados = modeloLista.modelos().stream().filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase())).collect(Collectors.toList());
        System.out.println("Modelos filtrados: ");
        modelosFiltrados.forEach(System.out::println); // exibindo nova lista

        // -----------------------------------------------------------------------------------------------------------

        System.out.println("Digite o código do modelo para que façamos a busca da avaliação : ");
        var codigoModelo = sc.nextLine();
        endereco += "/" + codigoModelo + "/anos";
        json = consumoAPI.obterDados(endereco);
        List<DadosVeiculos> anos = converteDados.listarDados(json, DadosVeiculos.class);

        //percorre a lsita de anos, busca os dados do veículo e joga em uma lista
        List<DadosAutomovel> automoveis = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumoAPI.obterDados(enderecoAnos);
            DadosAutomovel automovel = converteDados.obterDados(json, DadosAutomovel.class);
            automoveis.add(automovel);
        }

        System.out.println("Todos os veículos filtrados com avaliações por ano: ");
        automoveis.forEach(System.out::println);
    }
}
