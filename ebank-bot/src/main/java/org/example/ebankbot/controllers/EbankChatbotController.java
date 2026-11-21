package org.example.ebankbot.controllers;

import org.example.ebankbot.agents.EbankAIAgent;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EbankChatbotController {

    private final EbankAIAgent agent;

    public EbankChatbotController(EbankAIAgent agent) {
        this.agent = agent;
    }

    @GetMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestParam(defaultValue = "Bonjour") String query) {
        return agent.chat(query);
    }
}