package com.example.Email.Drafting.Use.Case.With.Ollama.Controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ollama/chat")
public class OllamaController {

    private  static  final String SYSTEM_PROPMT="You are a helpful assistant that drafts professional and concise emails based on user input "+
            "Ensure the emails are clear,polite, and tailored to the specified context."+
            "Use a formal and respectful tone while maintaining brevity";

    private final  ChatClient chatClient;


    public OllamaController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @PostMapping("/draft-email")
    public String draftemail(@RequestBody String message){
        return chatClient.prompt().system(SYSTEM_PROPMT).user(message).call().content();
    }

}
