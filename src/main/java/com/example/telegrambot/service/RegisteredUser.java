package com.example.telegrambot.service;

import com.example.telegrambot.dto.Users;
import com.example.telegrambot.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class RegisteredUser {
    @Autowired
    UsersRepository usersRepository;

    protected void registeredUser(Message message) {
        if (usersRepository.findById(message.getChatId()).isEmpty()){

            var chatId = message.getChatId();
            var chat = message.getChat();

            Users user = new Users();

            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());

            usersRepository.save(user);
            log.info("user saved: " + user);
        }
    }
}
