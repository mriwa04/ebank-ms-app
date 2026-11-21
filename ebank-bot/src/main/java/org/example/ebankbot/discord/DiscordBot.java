package org.example.ebankbot.discord;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.ebankbot.agents.EbankAIAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DiscordBot extends ListenerAdapter {

    private final EbankAIAgent agent;
    private final String token;

    public DiscordBot(EbankAIAgent agent,
                      @Value("${discord.token}") String token) {
        this.agent = agent;
        this.token = token;
    }

    @PostConstruct
    public void init() {
        if (token == null || token.isBlank()) {
            return;
        }
        JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(this)
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String query = event.getMessage().getContentRaw();
        if (query == null || query.isBlank()) {
            return;
        }

        String response = agent.chat(query);
        String text = (response != null && !response.isBlank()) ? response : "Je ne sais pas";

        // Discord limite chaque message à 2000 caractères
        for (int i = 0; i < text.length(); i += 1900) {
            String chunk = text.substring(i, Math.min(text.length(), i + 1900));
            event.getChannel().sendMessage(chunk).queue();
        }
    }
}