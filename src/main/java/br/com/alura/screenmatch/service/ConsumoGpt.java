package br.com.alura.screenmatch.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsumoGpt {

    public static String obterTraducao(String texto) {

        try {
            OpenAiService service = new OpenAiService(System.getenv("OPENAI_KEY"));

            CompletionRequest requisicao = CompletionRequest.builder()
                    .model("text-davinci-003")
                    .prompt("traduza para o português o texto: " + texto)
                    .maxTokens(1000)
                    .temperature(0.7)
                    .build();

            var resposta = service.createCompletion(requisicao);

            if (resposta != null && resposta.getChoices() != null && !resposta.getChoices().isEmpty()) {
                return resposta.getChoices().get(0).getText();
            } else {
                return "Resposta vazia ou imcompleta";
            }
        }catch (Exception e){
            return "Erro ao obter traução" + e.getMessage();
        }
    }
}



