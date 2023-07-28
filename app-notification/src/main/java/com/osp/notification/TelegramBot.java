package com.osp.notification;


import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component
@PropertySources(
        {
                @PropertySource(name = "notifi", value = "classpath:/notifi.properties", encoding = "UTF-8", ignoreResourceNotFound = true),
        }
)
public class TelegramBot extends TelegramLongPollingBot {
    @Resource private Environment environment;

    public void sendBotMessage(String message) {
        try {
            if (!environment.getProperty("telegram.bot.enable").equals("true")) return;
            String[] strs = environment.getProperty("telegram.bot.chatIds").split(",");
            List<Long> chatIds = new ArrayList();
            for (String str : strs) {
                chatIds.add(Long.parseLong(str));
            }
            for (Long chatId : chatIds) {
                SendMessage response = new SendMessage();
                response.setChatId(chatId);
                response.setText(message);
                sendMessage(response);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            SendMessage response = new SendMessage();
            Long chatId = message.getChatId();
            String command = update.getMessage().getText();
            if (command.equals("/subscribe") || command.equals("/start")) {
                response.setChatId(chatId);
                if (update.getMessage().getFrom().getUserName() == null) {
                    response.setText("Test : Please change your telegram username and restart this bot again!");
                } else {
                    try {
                        response.setText("ChatId : "+ chatId + " Username : "+  update.getMessage().getFrom().getUserName() );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    sendMessage(response);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public String getBotToken() {
        return environment.getProperty("telegram.bot.token");
    }

    @Override
    public String getBotUsername() {
        return environment.getProperty("telegram.bot.username");
    }

    @PostConstruct
    public void start() {}

}
