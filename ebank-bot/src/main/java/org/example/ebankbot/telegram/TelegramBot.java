package org.example.ebankbot.telegram;

import jakarta.annotation.PostConstruct;
import org.example.ebankbot.agents.EbankAIAgent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final EbankAIAgent agent;
    private final String botUsername;

    public TelegramBot(EbankAIAgent agent,
                       @Value("${telegram.bot.token}") String botToken,
                       @Value("${telegram.bot.username:ebank_bot}") String botUsername) {
        super(botToken);
        this.agent = agent;
        this.botUsername = botUsername;
    }

    @PostConstruct
    public void init() {
        String token = getBotToken();
        if (token == null || token.isBlank()) {
            return;
        }
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du bot Telegram", e);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }

        String chatId = update.getMessage().getChatId().toString();
        String query = update.getMessage().getText();

        try {
            // Affiche "en train d'écrire..." pendant l'appel à l'agent
            SendChatAction typing = new SendChatAction();
            typing.setChatId(chatId);
            typing.setAction(ActionType.TYPING);
            execute(typing);

            String reply = agent.chat(query);
            String text = (reply != null && !reply.isBlank()) ? reply : "Je ne sais pas";

            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(text);
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}