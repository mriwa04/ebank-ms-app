package org.example.ebankbot.agents;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.stereotype.Service;

@Service
public class EbankAIAgent {

    private final ChatClient chatClient;

    public EbankAIAgent(ChatClient.Builder chatClient, ToolCallbackProvider tools, ChatMemory chatMemory) {
        this.chatClient = chatClient
                .defaultSystem("""
                        Vous êtes un assistant spécialisé uniquement dans la gestion des clients et des comptes bancaires.
                        Répondez aux questions de l'utilisateur en fonction du contexte et des outils disponibles.
                        Si une question est en dehors de ce domaine ou si vous n'avez pas l'information, répondez : "Je ne sais pas".
                        """)
                .defaultTools(tools)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    public String chat(String query) {
        return chatClient.prompt(query)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, "C1"))
                .call()
                .content();
    }
}