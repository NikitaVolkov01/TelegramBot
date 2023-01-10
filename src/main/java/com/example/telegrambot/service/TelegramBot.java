package com.example.telegrambot.service;

import com.example.telegrambot.config.BotConfig;
import com.example.telegrambot.dto.Users;
import com.example.telegrambot.repository.GasStationFuelTypeRepository;
import com.example.telegrambot.repository.GasStationRepository;
import com.example.telegrambot.repository.UsersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot{
    // Делаем DI через поля, с помощью аннотации @Autowired.
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    GasStationRepository gasStationRepository;
    @Autowired
    GasStationFuelTypeRepository gasStationFuelTypeRepository;
    @Autowired
    RegisteredUser registeredUser;
    @Autowired
    final BotConfig config;

    //Метод создает Menu.
    public TelegramBot(BotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        listofCommands.add(new BotCommand("/start", StaticTextForTgBot.COMMAND_START_TEXT));
        listofCommands.add(new BotCommand("/help", StaticTextForTgBot.COMMAND_HELP_TEXT));
        listofCommands.add(new BotCommand("/fuel", StaticTextForTgBot.COMMAND_FUEL_TEXT));
        listofCommands.add(new BotCommand("/location", StaticTextForTgBot.COMMAND_LOCATION_TEXT));
        listofCommands.add(new BotCommand("/nearby", StaticTextForTgBot.COMMAND_NEARBY_TEXT));
        listofCommands.add(new BotCommand("/profitably", StaticTextForTgBot.COMMAND_PROFITABLY_TEXT));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot's command list: " + e.getMessage());
        }
    }
    // Переопределенный метод из абстрактного класса TelegramLongPollingBot, которому мы передаем имя нашего бота.
    @Override
    public String getBotUsername() {
        return config.getBotName();
    }
    // Переопределенный метод из абстрактного класса TelegramLongPollingBot, которому мы передаем токен нашего бота.
    @Override
    public String getBotToken() {
        return config.getToken();
    }
    // Переопределенный метод из абстрактного класса TelegramLongPollingBot, в котором происходит основная логика обработки сообщений от юзера.
    @Override
    public void onUpdateReceived(Update update) { //здесь происходит обработка сообщений от пользователя

        if (update.hasMessage() && update.getMessage().hasText()){ // Убеждаемся, что нам, что-то прислали и что там есть какой-то текст.
            String messageText = update.getMessage().getText(); // Помещаем в эту переменную то, что пользователь нам прислал.
            long chatID = update.getMessage().getChatId(); // В переменную chatID помещаем уникальный номер чата, с которым взаимодействую юзер.

            switch (messageText){ // В зависимости от того, что у нас хранится в messageText мы будем, что-то делать.
                case "/start":
                    registeredUser.registeredUser(update.getMessage());
                    startCommandReceived(chatID, update.getMessage().getChat().getFirstName()); // Сохраняем информацию о человеке, который нам пишет.
                    break;

                case "/help":
                    sendMessage(chatID, StaticTextForTgBot.HELP_TEXT);
                    break;

                case "/fuel":
                    selectFuel(chatID);
                    break;

                case "/location":
                    sendMessage(chatID, StaticTextForTgBot.LOCATION_TEXT);
                    break;

                case "/nearby":
                    Users usersNearby = usersRepository.findById(chatID).get();
                    //Если пользователь ввел свои координаты, то все ок, если нет, то выводим сообщение об ошибке.
                    if(usersNearby.getLatitude() != null && usersNearby.getLongitude() != null) {
                        //Берем координаты пользователя из БД.
                        double x1 = usersNearby.getLatitude();
                        double y1 = usersNearby.getLongitude();
                        //Высчитываем расстояние между 2-мя точками: между пользователем и заправочной станцией.
                        double distance1 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(1L), gasStationRepository.findByIdIY(1L));
                        double distance2 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(2L), gasStationRepository.findByIdIY(2L));
                        double distance3 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(3L), gasStationRepository.findByIdIY(3L));
                        double distance4 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(4L), gasStationRepository.findByIdIY(4L));
                        double distance5 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(5L), gasStationRepository.findByIdIY(5L));
                        double distance6 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(6L), gasStationRepository.findByIdIY(6L));
                        double distance7 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(7L), gasStationRepository.findByIdIY(7L));
                        double distance8 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(8L), gasStationRepository.findByIdIY(8L));
                        double distance9 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(9L), gasStationRepository.findByIdIY(9L));
                        double distance10 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(10L), gasStationRepository.findByIdIY(10L));
                        double distance11 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(11L), gasStationRepository.findByIdIY(11L));
                        double distance12 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(12L), gasStationRepository.findByIdIY(12L));
                        double distance13 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(13L), gasStationRepository.findByIdIY(13L));
                        double distance14 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(14L), gasStationRepository.findByIdIY(14L));
                        double distance15 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(15L), gasStationRepository.findByIdIY(15L));
                        double distance16 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(16L), gasStationRepository.findByIdIY(16L));
                        double distance17 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(17L), gasStationRepository.findByIdIY(17L));
                        double distance18 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(18L), gasStationRepository.findByIdIY(18L));
                        double distance19 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(19L), gasStationRepository.findByIdIY(19L));
                        double distance20 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(20L), gasStationRepository.findByIdIY(20L));
                        double distance21 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(21L), gasStationRepository.findByIdIY(21L));
                        double distance22 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(22L), gasStationRepository.findByIdIY(22L));
                        double distance23 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(23L), gasStationRepository.findByIdIY(23L));
                        double distance24 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(24L), gasStationRepository.findByIdIY(24L));
                        double distance25 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(25L), gasStationRepository.findByIdIY(25L));
                        double distance26 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(26L), gasStationRepository.findByIdIY(26L));
                        double distance27 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(27L), gasStationRepository.findByIdIY(27L));
                        double distance28 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(28L), gasStationRepository.findByIdIY(28L));
                        double distance29 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(29L), gasStationRepository.findByIdIY(29L));
                        double distance30 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(30L), gasStationRepository.findByIdIY(30L));
                        double distance31 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(31L), gasStationRepository.findByIdIY(31L));
                        double distance32 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(32L), gasStationRepository.findByIdIY(32L));
                        double distance33 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(33L), gasStationRepository.findByIdIY(33L));
                        double distance34 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(34L), gasStationRepository.findByIdIY(34L));
                        double distance35 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(35L), gasStationRepository.findByIdIY(35L));
                        double distance36 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(36L), gasStationRepository.findByIdIY(36L));
                        double distance37 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(37L), gasStationRepository.findByIdIY(37L));
                        double distance38 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(38L), gasStationRepository.findByIdIY(38L));
                        double distance39 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(39L), gasStationRepository.findByIdIY(39L));
                        double distance40 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(40L), gasStationRepository.findByIdIY(40L));
                        double distance41 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(41L), gasStationRepository.findByIdIY(41L));
                        double distance42 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(42L), gasStationRepository.findByIdIY(42L));
                        double distance43 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(43L), gasStationRepository.findByIdIY(43L));
                        double distance44 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(44L), gasStationRepository.findByIdIY(44L));
                        double distance45 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(45L), gasStationRepository.findByIdIY(45L));
                        double distance46 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(46L), gasStationRepository.findByIdIY(46L));
                        double distance47 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(47L), gasStationRepository.findByIdIY(47L));
                        double distance48 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(48L), gasStationRepository.findByIdIY(48L));
                        double distance49 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(49L), gasStationRepository.findByIdIY(49L));
                        double distance50 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(50L), gasStationRepository.findByIdIY(50L));
                        double distance51 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(51L), gasStationRepository.findByIdIY(51L));
                        double distance52 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(52L), gasStationRepository.findByIdIY(52L));
                        double distance53 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(53L), gasStationRepository.findByIdIY(53L));
                        double distance54 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(54L), gasStationRepository.findByIdIY(54L));
                        double distance55 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(55L), gasStationRepository.findByIdIY(55L));
                        double distance56 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(56L), gasStationRepository.findByIdIY(56L));
                        double distance57 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(57L), gasStationRepository.findByIdIY(57L));
                        double distance58 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(58L), gasStationRepository.findByIdIY(58L));
                        double distance59 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(59L), gasStationRepository.findByIdIY(59L));
                        double distance60 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(60L), gasStationRepository.findByIdIY(60L));
                        double distance61 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(61L), gasStationRepository.findByIdIY(61L));
                        double distance62 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(62L), gasStationRepository.findByIdIY(62L));
                        double distance63 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(63L), gasStationRepository.findByIdIY(63L));
                        double distance64 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(64L), gasStationRepository.findByIdIY(64L));
                        double distance65 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(65L), gasStationRepository.findByIdIY(65L));
                        double distance66 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(66L), gasStationRepository.findByIdIY(66L));
                        double distance67 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(67L), gasStationRepository.findByIdIY(67L));
                        double distance68 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(68L), gasStationRepository.findByIdIY(68L));
                        double distance69 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(69L), gasStationRepository.findByIdIY(69L));
                        double distance70 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(70L), gasStationRepository.findByIdIY(70L));
                        double distance71 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(71L), gasStationRepository.findByIdIY(71L));
                        double distance72 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(72L), gasStationRepository.findByIdIY(72L));
                        double distance73 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(73L), gasStationRepository.findByIdIY(73L));
                        double distance74 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(74L), gasStationRepository.findByIdIY(74L));
                        double distance75 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(75L), gasStationRepository.findByIdIY(75L));
                        double distance76 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(76L), gasStationRepository.findByIdIY(76L));
                        double distance77 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(77L), gasStationRepository.findByIdIY(77L));
                        double distance78 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(78L), gasStationRepository.findByIdIY(78L));
                        double distance79 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(79L), gasStationRepository.findByIdIY(79L));
                        double distance80 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(80L), gasStationRepository.findByIdIY(80L));
                        double distance81 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(81L), gasStationRepository.findByIdIY(81L));
                        double distance82 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(82L), gasStationRepository.findByIdIY(82L));
                        double distance83 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(83L), gasStationRepository.findByIdIY(83L));
                        double distance84 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(84L), gasStationRepository.findByIdIY(84L));
                        double distance85 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(85L), gasStationRepository.findByIdIY(85L));
                        double distance86 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(86L), gasStationRepository.findByIdIY(86L));
                        double distance87 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(87L), gasStationRepository.findByIdIY(87L));
                        double distance88 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(88L), gasStationRepository.findByIdIY(88L));
                        double distance89 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(89L), gasStationRepository.findByIdIY(89L));
                        double distance90 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(90L), gasStationRepository.findByIdIY(90L));
                        double distance91 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(91L), gasStationRepository.findByIdIY(91L));
                        double distance92 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(92L), gasStationRepository.findByIdIY(92L));
                        double distance93 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(93L), gasStationRepository.findByIdIY(93L));
                        double distance94 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(94L), gasStationRepository.findByIdIY(94L));
                        double distance95 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(95L), gasStationRepository.findByIdIY(95L));
                        double distance96 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(96L), gasStationRepository.findByIdIY(96L));
                        double distance97 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(97L), gasStationRepository.findByIdIY(97L));
                        double distance98 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(98L), gasStationRepository.findByIdIY(98L));
                        double distance99 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(99L), gasStationRepository.findByIdIY(99L));
                        double distance100 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(100L), gasStationRepository.findByIdIY(100L));
                        double distance101 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(101L), gasStationRepository.findByIdIY(101L));
                        double distance102 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(102L), gasStationRepository.findByIdIY(102L));
                        double distance103 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(103L), gasStationRepository.findByIdIY(103L));
                        double distance104 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(104L), gasStationRepository.findByIdIY(104L));
                        double distance105 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(105L), gasStationRepository.findByIdIY(105L));
                        double distance106 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(106L), gasStationRepository.findByIdIY(106L));
                        double distance107 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(107L), gasStationRepository.findByIdIY(107L));
                        double distance108 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(108L), gasStationRepository.findByIdIY(108L));
                        double distance109 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(109L), gasStationRepository.findByIdIY(109L));
                        double distance110 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(110L), gasStationRepository.findByIdIY(110L));
                        double distance111 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(111L), gasStationRepository.findByIdIY(111L));
                        double distance112 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(112L), gasStationRepository.findByIdIY(112L));
                        double distance113 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(113L), gasStationRepository.findByIdIY(113L));
                        double distance114 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(114L), gasStationRepository.findByIdIY(114L));
                        double distance115 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(115L), gasStationRepository.findByIdIY(115L));
                        double distance116 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(116L), gasStationRepository.findByIdIY(116L));
                        double distance117 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(117L), gasStationRepository.findByIdIY(117L));
                        double distance118 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(118L), gasStationRepository.findByIdIY(118L));
                        double distance119 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(119L), gasStationRepository.findByIdIY(119L));
                        double distance120 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(120L), gasStationRepository.findByIdIY(120L));
                        double distance121 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(121L), gasStationRepository.findByIdIY(121L));
                        double distance122 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(122L), gasStationRepository.findByIdIY(122L));
                        double distance123 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(123L), gasStationRepository.findByIdIY(123L));
                        double distance124 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(124L), gasStationRepository.findByIdIY(124L));
                        double distance125 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(125L), gasStationRepository.findByIdIY(125L));
                        double distance126 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(126L), gasStationRepository.findByIdIY(126L));
                        double distance127 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(127L), gasStationRepository.findByIdIY(127L));
                        double distance128 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(128L), gasStationRepository.findByIdIY(128L));
                        double distance129 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(129L), gasStationRepository.findByIdIY(129L));
                        double distance130 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(130L), gasStationRepository.findByIdIY(130L));
                        double distance131 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(131L), gasStationRepository.findByIdIY(131L));
                        double distance132 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(132L), gasStationRepository.findByIdIY(132L));
                        double distance133 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(133L), gasStationRepository.findByIdIY(133L));
                        double distance134 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(134L), gasStationRepository.findByIdIY(134L));
                        double distance135 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(135L), gasStationRepository.findByIdIY(135L));
                        double distance136 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(136L), gasStationRepository.findByIdIY(136L));
                        double distance137 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(137L), gasStationRepository.findByIdIY(137L));
                        double distance138 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(138L), gasStationRepository.findByIdIY(138L));
                        double distance139 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(139L), gasStationRepository.findByIdIY(139L));
                        double distance140 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(140L), gasStationRepository.findByIdIY(140L));
                        double distance141 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(141L), gasStationRepository.findByIdIY(141L));
                        double distance142 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(142L), gasStationRepository.findByIdIY(142L));
                        double distance143 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(143L), gasStationRepository.findByIdIY(143L));
                        double distance144 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(144L), gasStationRepository.findByIdIY(144L));
                        double distance145 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(145L), gasStationRepository.findByIdIY(145L));
                        double distance146 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(146L), gasStationRepository.findByIdIY(146L));
                        double distance147 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(147L), gasStationRepository.findByIdIY(147L));
                        double distance148 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(148L), gasStationRepository.findByIdIY(148L));
                        double distance149 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(149L), gasStationRepository.findByIdIY(149L));
                        double distance150 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(150L), gasStationRepository.findByIdIY(150L));
                        double distance151 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(151L), gasStationRepository.findByIdIY(151L));
                        double distance152 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(152L), gasStationRepository.findByIdIY(152L));
                        double distance153 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(153L), gasStationRepository.findByIdIY(153L));
                        double distance154 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(154L), gasStationRepository.findByIdIY(154L));
                        double distance155 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(155L), gasStationRepository.findByIdIY(155L));
                        double distance156 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(156L), gasStationRepository.findByIdIY(156L));
                        double distance157 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(157L), gasStationRepository.findByIdIY(157L));
                        double distance158 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(158L), gasStationRepository.findByIdIY(158L));
                        double distance159 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(159L), gasStationRepository.findByIdIY(159L));
                        double distance160 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(160L), gasStationRepository.findByIdIY(160L));
                        double distance161 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(161L), gasStationRepository.findByIdIY(161L));
                        double distance162 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(162L), gasStationRepository.findByIdIY(162L));
                        double distance163 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(163L), gasStationRepository.findByIdIY(163L));
                        double distance164 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(164L), gasStationRepository.findByIdIY(164L));
                        double distance165 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(165L), gasStationRepository.findByIdIY(165L));
                        double distance166 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(166L), gasStationRepository.findByIdIY(166L));
                        double distance167 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(167L), gasStationRepository.findByIdIY(167L));
                        double distance168 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(168L), gasStationRepository.findByIdIY(168L));
                        double distance169 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(169L), gasStationRepository.findByIdIY(169L));
                        double distance170 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(170L), gasStationRepository.findByIdIY(170L));
                        double distance171 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(171L), gasStationRepository.findByIdIY(171L));
                        double distance172 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(172L), gasStationRepository.findByIdIY(172L));
                        double distance173 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(173L), gasStationRepository.findByIdIY(173L));
                        double distance174 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(174L), gasStationRepository.findByIdIY(174L));
                        double distance175 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(175L), gasStationRepository.findByIdIY(175L));
                        double distance176 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(176L), gasStationRepository.findByIdIY(176L));
                        double distance177 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(177L), gasStationRepository.findByIdIY(177L));
                        double distance178 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(178L), gasStationRepository.findByIdIY(178L));
                        double distance179 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(179L), gasStationRepository.findByIdIY(179L));
                        double distance180 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(180L), gasStationRepository.findByIdIY(180L));
                        double distance181 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(181L), gasStationRepository.findByIdIY(181L));
                        double distance182 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(182L), gasStationRepository.findByIdIY(182L));
                        double distance183 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(183L), gasStationRepository.findByIdIY(183L));
                        double distance184 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(184L), gasStationRepository.findByIdIY(184L));
                        double distance185 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(185L), gasStationRepository.findByIdIY(185L));
                        double distance186 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(186L), gasStationRepository.findByIdIY(186L));
                        double distance187 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(187L), gasStationRepository.findByIdIY(187L));
                        double distance188 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(188L), gasStationRepository.findByIdIY(188L));
                        double distance189 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(189L), gasStationRepository.findByIdIY(189L));
                        double distance190 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(190L), gasStationRepository.findByIdIY(190L));
                        double distance191 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(191L), gasStationRepository.findByIdIY(191L));
                        double distance192 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(192L), gasStationRepository.findByIdIY(192L));
                        double distance193 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(193L), gasStationRepository.findByIdIY(193L));
                        double distance194 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(194L), gasStationRepository.findByIdIY(194L));
                        double distance195 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(195L), gasStationRepository.findByIdIY(195L));
                        double distance196 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(196L), gasStationRepository.findByIdIY(196L));
                        double distance197 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(197L), gasStationRepository.findByIdIY(197L));
                        double distance198 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(198L), gasStationRepository.findByIdIY(198L));
                        double distance199 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(199L), gasStationRepository.findByIdIY(199L));
                        double distance200 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(200L), gasStationRepository.findByIdIY(200L));
                        double distance201 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(201L), gasStationRepository.findByIdIY(201L));
                        double distance202 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(202L), gasStationRepository.findByIdIY(202L));
                        double distance203 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(203L), gasStationRepository.findByIdIY(203L));
                        double distance204 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(204L), gasStationRepository.findByIdIY(204L));
                        double distance205 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(205L), gasStationRepository.findByIdIY(205L));
                        double distance206 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(206L), gasStationRepository.findByIdIY(206L));
                        double distance207 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(207L), gasStationRepository.findByIdIY(207L));
                        double distance208 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(208L), gasStationRepository.findByIdIY(208L));
                        double distance209 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(209L), gasStationRepository.findByIdIY(209L));
                        double distance210 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(210L), gasStationRepository.findByIdIY(210L));
                        double distance211 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(211L), gasStationRepository.findByIdIY(211L));
                        double distance212 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(212L), gasStationRepository.findByIdIY(212L));
                        double distance213 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(213L), gasStationRepository.findByIdIY(213L));
                        double distance214 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(214L), gasStationRepository.findByIdIY(214L));
                        double distance215 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(215L), gasStationRepository.findByIdIY(215L));
                        double distance216 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(216L), gasStationRepository.findByIdIY(216L));
                        double distance217 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(217L), gasStationRepository.findByIdIY(217L));
                        double distance218 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(218L), gasStationRepository.findByIdIY(218L));
                        double distance219 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(219L), gasStationRepository.findByIdIY(219L));
                        double distance220 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(220L), gasStationRepository.findByIdIY(220L));
                        double distance221 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(221L), gasStationRepository.findByIdIY(221L));
                        double distance222 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(222L), gasStationRepository.findByIdIY(222L));
                        double distance223 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(223L), gasStationRepository.findByIdIY(223L));
                        double distance224 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(224L), gasStationRepository.findByIdIY(224L));
                        double distance225 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(225L), gasStationRepository.findByIdIY(225L));
                        double distance226 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(226L), gasStationRepository.findByIdIY(226L));
                        double distance227 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(227L), gasStationRepository.findByIdIY(227L));
                        double distance228 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(228L), gasStationRepository.findByIdIY(228L));
                        double distance229 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(229L), gasStationRepository.findByIdIY(229L));
                        double distance230 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(230L), gasStationRepository.findByIdIY(230L));
                        double distance231 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(231L), gasStationRepository.findByIdIY(231L));
                        double distance232 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(232L), gasStationRepository.findByIdIY(232L));
                        double distance234 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(234L), gasStationRepository.findByIdIY(234L));
                        double distance235 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(235L), gasStationRepository.findByIdIY(235L));
                        double distance236 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(236L), gasStationRepository.findByIdIY(236L));
                        double distance237 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(237L), gasStationRepository.findByIdIY(237L));
                        double distance238 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(238L), gasStationRepository.findByIdIY(238L));
                        double distance239 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(239L), gasStationRepository.findByIdIY(239L));
                        double distance240 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(240L), gasStationRepository.findByIdIY(240L));
                        double distance241 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(241L), gasStationRepository.findByIdIY(241L));
                        double distance242 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(242L), gasStationRepository.findByIdIY(242L));
                        double distance243 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(243L), gasStationRepository.findByIdIY(243L));
                        double distance244 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(244L), gasStationRepository.findByIdIY(244L));
                        double distance245 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(245L), gasStationRepository.findByIdIY(245L));
                        double distance246 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(246L), gasStationRepository.findByIdIY(246L));
                        double distance247 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(247L), gasStationRepository.findByIdIY(247L));
                        double distance248 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(248L), gasStationRepository.findByIdIY(248L));
                        double distance249 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(249L), gasStationRepository.findByIdIY(249L));
                        double distance250 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(250L), gasStationRepository.findByIdIY(250L));
                        double distance251 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(251L), gasStationRepository.findByIdIY(251L));
                        double distance252 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(252L), gasStationRepository.findByIdIY(252L));
                        double distance253 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(253L), gasStationRepository.findByIdIY(253L));
                        double distance254 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(254L), gasStationRepository.findByIdIY(254L));
                        double distance255 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(255L), gasStationRepository.findByIdIY(255L));
                        double distance256 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(256L), gasStationRepository.findByIdIY(256L));
                        double distance257 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(257L), gasStationRepository.findByIdIY(257L));
                        double distance258 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(258L), gasStationRepository.findByIdIY(258L));
                        double distance259 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(259L), gasStationRepository.findByIdIY(259L));
                        double distance260 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(260L), gasStationRepository.findByIdIY(260L));
                        double distance261 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(261L), gasStationRepository.findByIdIY(261L));
                        double distance262 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(262L), gasStationRepository.findByIdIY(262L));
                        double distance263 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(263L), gasStationRepository.findByIdIY(263L));
                        double distance264 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(264L), gasStationRepository.findByIdIY(264L));
                        double distance265 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(265L), gasStationRepository.findByIdIY(265L));
                        double distance266 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(266L), gasStationRepository.findByIdIY(266L));
                        double distance267 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(267L), gasStationRepository.findByIdIY(267L));
                        double distance268 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(268L), gasStationRepository.findByIdIY(268L));
                        double distance269 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(269L), gasStationRepository.findByIdIY(269L));
                        double distance270 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(270L), gasStationRepository.findByIdIY(270L));
                        double distance271 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(271L), gasStationRepository.findByIdIY(271L));
                        double distance272 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(272L), gasStationRepository.findByIdIY(272L));
                        double distance273 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(273L), gasStationRepository.findByIdIY(273L));
                        double distance274 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(274L), gasStationRepository.findByIdIY(274L));
                        double distance275 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(275L), gasStationRepository.findByIdIY(275L));
                        double distance276 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(276L), gasStationRepository.findByIdIY(276L));
                        double distance277 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(277L), gasStationRepository.findByIdIY(277L));
                        double distance278 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(278L), gasStationRepository.findByIdIY(278L));
                        double distance279 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(279L), gasStationRepository.findByIdIY(279L));
                        double distance280 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(280L), gasStationRepository.findByIdIY(280L));
                        double distance281 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(281L), gasStationRepository.findByIdIY(281L));
                        double distance282 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(282L), gasStationRepository.findByIdIY(282L));
                        double distance283 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(283L), gasStationRepository.findByIdIY(283L));
                        double distance284 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(284L), gasStationRepository.findByIdIY(284L));
                        double distance285 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(285L), gasStationRepository.findByIdIY(285L));
                        double distance286 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(286L), gasStationRepository.findByIdIY(286L));
                        double distance287 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(287L), gasStationRepository.findByIdIY(287L));
                        double distance288 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(288L), gasStationRepository.findByIdIY(288L));
                        double distance289 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(289L), gasStationRepository.findByIdIY(289L));
                        double distance290 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(290L), gasStationRepository.findByIdIY(290L));
                        double distance291 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(291L), gasStationRepository.findByIdIY(291L));
                        double distance292 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(292L), gasStationRepository.findByIdIY(292L));
                        double distance293 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(293L), gasStationRepository.findByIdIY(293L));
                        double distance294 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(294L), gasStationRepository.findByIdIY(294L));
                        double distance295 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(295L), gasStationRepository.findByIdIY(295L));
                        double distance296 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(296L), gasStationRepository.findByIdIY(296L));
                        double distance297 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(297L), gasStationRepository.findByIdIY(297L));
                        double distance298 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(298L), gasStationRepository.findByIdIY(298L));
                        double distance299 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(299L), gasStationRepository.findByIdIY(299L));
                        double distance300 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(300L), gasStationRepository.findByIdIY(300L));
                        double distance301 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(301L), gasStationRepository.findByIdIY(301L));
                        double distance302 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(302L), gasStationRepository.findByIdIY(302L));
                        double distance303 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(303L), gasStationRepository.findByIdIY(303L));
                        double distance304 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(304L), gasStationRepository.findByIdIY(304L));
                        double distance305 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(305L), gasStationRepository.findByIdIY(305L));
                        double distance306 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(306L), gasStationRepository.findByIdIY(306L));
                        double distance307 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(307L), gasStationRepository.findByIdIY(307L));
                        double distance308 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(308L), gasStationRepository.findByIdIY(308L));
                        double distance309 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(309L), gasStationRepository.findByIdIY(309L));
                        double distance310 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(310L), gasStationRepository.findByIdIY(310L));
                        double distance311 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(311L), gasStationRepository.findByIdIY(311L));
                        double distance312 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(312L), gasStationRepository.findByIdIY(312L));
                        double distance313 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(313L), gasStationRepository.findByIdIY(313L));
                        double distance314 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(314L), gasStationRepository.findByIdIY(314L));
                        double distance315 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(315L), gasStationRepository.findByIdIY(315L));
                        double distance316 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(316L), gasStationRepository.findByIdIY(316L));
                        double distance317 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(317L), gasStationRepository.findByIdIY(317L));
                        double distance318 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(318L), gasStationRepository.findByIdIY(318L));
                        double distance319 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(319L), gasStationRepository.findByIdIY(319L));
                        double distance320 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(320L), gasStationRepository.findByIdIY(320L));
                        double distance321 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(321L), gasStationRepository.findByIdIY(321L));
                        double distance322 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(322L), gasStationRepository.findByIdIY(322L));
                        double distance323 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(323L), gasStationRepository.findByIdIY(323L));
                        double distance324 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(324L), gasStationRepository.findByIdIY(324L));
                        double distance325 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(325L), gasStationRepository.findByIdIY(325L));
                        double distance326 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(326L), gasStationRepository.findByIdIY(326L));
                        double distance327 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(327L), gasStationRepository.findByIdIY(327L));
                        double distance328 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(328L), gasStationRepository.findByIdIY(328L));
                        double distance329 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(329L), gasStationRepository.findByIdIY(329L));
                        double distance330 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(330L), gasStationRepository.findByIdIY(330L));
                        double distance331 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(331L), gasStationRepository.findByIdIY(331L));
                        double distance332 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(332L), gasStationRepository.findByIdIY(332L));
                        double distance333 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(333L), gasStationRepository.findByIdIY(333L));
                        double distance334 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(334L), gasStationRepository.findByIdIY(334L));
                        double distance335 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(335L), gasStationRepository.findByIdIY(335L));
                        double distance336 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(336L), gasStationRepository.findByIdIY(336L));
                        double distance337 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(337L), gasStationRepository.findByIdIY(337L));
                        double distance338 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(338L), gasStationRepository.findByIdIY(338L));
                        double distance339 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(339L), gasStationRepository.findByIdIY(339L));
                        double distance340 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(340L), gasStationRepository.findByIdIY(340L));
                        double distance341 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(341L), gasStationRepository.findByIdIY(341L));
                        double distance342 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(342L), gasStationRepository.findByIdIY(342L));
                        double distance343 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(343L), gasStationRepository.findByIdIY(343L));
                        double distance344 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(344L), gasStationRepository.findByIdIY(344L));
                        double distance345 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(345L), gasStationRepository.findByIdIY(345L));
                        double distance346 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(346L), gasStationRepository.findByIdIY(346L));
                        double distance347 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(347L), gasStationRepository.findByIdIY(347L));
                        double distance348 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(348L), gasStationRepository.findByIdIY(348L));
                        double distance349 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(349L), gasStationRepository.findByIdIY(349L));
                        double distance350 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(350L), gasStationRepository.findByIdIY(350L));
                        double distance351 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(351L), gasStationRepository.findByIdIY(351L));
                        double distance352 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(352L), gasStationRepository.findByIdIY(352L));
                        double distance353 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(353L), gasStationRepository.findByIdIY(353L));
                        double distance354 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(354L), gasStationRepository.findByIdIY(354L));
                        double distance355 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(355L), gasStationRepository.findByIdIY(355L));
                        double distance356 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(356L), gasStationRepository.findByIdIY(356L));
                        double distance357 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(357L), gasStationRepository.findByIdIY(357L));
                        double distance358 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(358L), gasStationRepository.findByIdIY(358L));
                        double distance359 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(359L), gasStationRepository.findByIdIY(359L));
                        double distance360 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(360L), gasStationRepository.findByIdIY(360L));
                        double distance361 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(361L), gasStationRepository.findByIdIY(361L));
                        double distance362 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(362L), gasStationRepository.findByIdIY(362L));
                        double distance363 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(363L), gasStationRepository.findByIdIY(363L));
                        double distance364 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(364L), gasStationRepository.findByIdIY(364L));
                        double distance365 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(365L), gasStationRepository.findByIdIY(365L));
                        double distance366 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(366L), gasStationRepository.findByIdIY(366L));
                        double distance367 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(367L), gasStationRepository.findByIdIY(367L));
                        double distance368 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(368L), gasStationRepository.findByIdIY(368L));
                        double distance369 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(369L), gasStationRepository.findByIdIY(369L));
                        double distance370 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(370L), gasStationRepository.findByIdIY(370L));
                        double distance371 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(371L), gasStationRepository.findByIdIY(371L));
                        double distance372 = calculateDistanceBetweenPointsWithPoint2D(x1, y1, gasStationRepository.findByIdX(372L), gasStationRepository.findByIdIY(372L));
                        //Высчитанное расстояния помещаем в виде ключа в TreeMap, а в виде значения адрес заправочной станции. В TreeMap сортировка происходит по ключу,
                        //поэтому под 1 элементом в TreeMap будет лежать ближайший адрес.
                        TreeMap<Double, String> distanceMap = new TreeMap<>();
                        distanceMap.put(distance1, "Роснефть, Санкт-Петербург, Ириновский просп., 22, корп. 1");
                        distanceMap.put(distance2, "Татнефть, Санкт-Петербург, Ириновский просп., 26");
                        distanceMap.put(distance3, "Газпромнефть, Санкт-Петербург, ул. Потапова, 7");
                        distanceMap.put(distance4, "Лукойл, Санкт-Петербург, Индустриальный просп., 46");
                        distanceMap.put(distance5, "Роснефть, Санкт-Петербург, ш. Революции, 86, корп. 3");
                        distanceMap.put(distance6, "NordPoint, Санкт-Петербург, Ириновский просп., 16");
                        distanceMap.put(distance7, "Газпромнефть, Санкт-Петербург, ул. Химиков, 4");
                        distanceMap.put(distance8, "Лукойл, Санкт-Петербург, ул. Коммуны, 76");
                        distanceMap.put(distance9, "Роснефть, Санкт-Петербург, ул. Коммуны, 17");
                        distanceMap.put(distance10, "Роснефть, Санкт-Петербург, Ириновский просп., 52");
                        distanceMap.put(distance11, "Кириши, Санкт-Петербург, Лапинский просп., 10");
                        distanceMap.put(distance12, "Лукойл, Санкт-Петербург, Херсонский пр., 4");
                        distanceMap.put(distance13, "Лукойл, Санкт-Петербург, Синопская наб., 12");
                        distanceMap.put(distance14, "Neste, Санкт-Петербург, Малоохтинский просп., 59");
                        distanceMap.put(distance15, "Лукойл, Санкт-Петербург, 19-я линия, д. 24");
                        distanceMap.put(distance16, "Лукойл, Сестрорецк, 39-й км Приморского шоссе");
                        distanceMap.put(distance17, "Татнефть, Санкт-Петербург, Авангардная ул., 36");
                        distanceMap.put(distance18, "Роснефть, Санкт-Петербург, Автобусная ул., 12А");
                        distanceMap.put(distance19, "Газпромнефть, Санкт-Петербург, ул. Академика Константинова, 1, корп. 2");
                        distanceMap.put(distance20, "Роснефть, Санкт-Петербург, Александровский парк, 8");
                        distanceMap.put(distance21, "Лукойл, Санкт-Петербург, Анисимовская дорога, 17 литер А");
                        distanceMap.put(distance22, "Роснефть, Санкт-Петербург, Аптекарская набережная, 16");
                        distanceMap.put(distance23, "Лукойл, Санкт-Петербург, ул. Арсенальная, 68");
                        distanceMap.put(distance24, "Лукойл, Санкт-Петербург, ул. Бабушкина, 127");
                        distanceMap.put(distance25, "Газпромнефть, Санкт-Петербург, ул. Белградская, 1");
                        distanceMap.put(distance26, "Teboil, Санкт-Петербург, ул. Белградская, 9-А");
                        distanceMap.put(distance27, "Teboil, Санкт-Петербург, Белоостровская ул., 8");
                        distanceMap.put(distance28, "Лукойл, Санкт-Петербург, Бестужевская ул., 52, корп. 2");
                        distanceMap.put(distance29, "Роснефть, Санкт-Петербург, ул. Благодатная, 48");
                        distanceMap.put(distance30, "Лукойл, Санкт-Петербург, Благодатная ул., 10А");
                        distanceMap.put(distance31, "Teboil, Санкт-Петербург, Богатырский просп., 12, корп. 2");
                        distanceMap.put(distance32, "Лукойл, Санкт-Петербург, Богатырский проспект, 17А");
                        distanceMap.put(distance33, "NordPoint, Санкт-Петербург, Богатырский проспект, 23");
                        distanceMap.put(distance34, "Татнефть, Санкт-Петербург, Большая Пороховская ул., 53");
                        distanceMap.put(distance35, "Лукойл, Санкт-Петербург, Большой Сампсониевский проспект, 89А");
                        distanceMap.put(distance36, "Татнефть, Санкт-Петербург, Будапештская ул., 84");
                        distanceMap.put(distance37, "Роснефть, Санкт-Петербург, ул. Будапештская, 116");
                        distanceMap.put(distance38, "Лукойл, Санкт-Петербург, Бумажная ул., 13");
                        distanceMap.put(distance39, "Лукойл, Санкт-Петербург, Бухарестская ул., 12А");
                        distanceMap.put(distance40, "Газпромнефть, Санкт-Петербург, ул. Верхняя, 10 литера А");
                        distanceMap.put(distance41, "Лукойл, Санкт-Петербург, Витебский проспект, 10А");
                        distanceMap.put(distance42, "Лукойл, Санкт-Петербург, Витебский проспект, 17");
                        distanceMap.put(distance43, "Лукойл, Санкт-Петербург, Витебский проспект, 22А");
                        distanceMap.put(distance44, "Газпромнефть, Санкт-Петербург, Витебский проспект, 90");
                        distanceMap.put(distance45, "Роснефть, Санкт-Петербург, Витебский проспект, 91");
                        distanceMap.put(distance46, "Лукойл, Санкт-Петербург, Витебский просп., 22");
                        distanceMap.put(distance47, "Газпромнефть, Санкт-Петербург, ул. Возрождения, 36А");
                        distanceMap.put(distance48, "Роснефть, Санкт-Петербург, Волковский проспект, 61, лит. \"А\"");
                        distanceMap.put(distance49, "Газпромнефть, Санкт-Петербург, Волхонское ш., 115, корп. 5");
                        distanceMap.put(distance50, "Лукойл, Санкт-Петербург, ул. Ворошилова, 10А");
                        distanceMap.put(distance51, "Лукойл, Санкт-Петербург, Выборгская наб., 18");
                        distanceMap.put(distance52, "NordPoint, Санкт-Петербург, Выборгская набережная, д. 57/1 А");
                        distanceMap.put(distance53, "Кириши, Санкт-Петербург, Выборгское шоссе, 6 Б,лит А");
                        distanceMap.put(distance54, "Neste, Санкт-Петербург, Выборгское шоссе, 21");
                        distanceMap.put(distance55, "Роснефть, Санкт-Петербург, Выборгское шоссе, 222, корп.2");
                        distanceMap.put(distance56, "Роснефть, Санкт-Петербург, Выборгское шоссе, 354А");
                        distanceMap.put(distance57, "Neste, Санкт-Петербург, Выборгское шоссе, 216к3");
                        distanceMap.put(distance58, "Лукойл, Санкт-Петербург, Выборгское шоссе, 416 литер А");
                        distanceMap.put(distance59, "Газпромнефть, Санкт-Петербург, Выборгское шоссе, 503к5");
                        distanceMap.put(distance60, "Роснефть, Санкт-Петербург, г. Ломоносов, Морская ул., 94");
                        distanceMap.put(distance61, "Газпромнефть, Санкт-Петербург, г. Ломоносов, ул. Победы, 27");
                        distanceMap.put(distance62, "Газпромнефть, Санкт-Петербург, Гатчинское шоссе, 17");
                        distanceMap.put(distance63, "Лукойл, Санкт-Петербург, ул. Генерала Хрулева, 2");
                        distanceMap.put(distance64, "Роснефть, Санкт-Петербург, ул. Генерала Хрулева, 14 литер А");
                        distanceMap.put(distance65, "Лукойл, Санкт-Петербург, ул. Гидростроителей, 3");
                        distanceMap.put(distance66, "Роснефть, Санкт-Петербург, ул. Гидростроителей, 9");
                        distanceMap.put(distance67, "Teboil, Санкт-Петербург, Глухоозёрское ш., 2соор1");
                        distanceMap.put(distance68, "Лукойл, Санкт-Петербург, Глухоозёрское шоссе, 10А");
                        distanceMap.put(distance69, "Татнефть, Санкт-Петербург, Горское шоссе, 1");
                        distanceMap.put(distance70, "Татнефть, Санкт-Петербург, Гражданский проспект, 93");
                        distanceMap.put(distance71, "Роснефть, Санкт-Петербург, Дальневосточный проспект, 1А");
                        distanceMap.put(distance72, "Газпромнефть, Санкт-Петербург, Дальневосточный проспект, д. 11, к. 2");
                        distanceMap.put(distance73, "Линос, Санкт-Петербург, Дальневосточный проспект, 20");
                        distanceMap.put(distance74, "Роснефть, Санкт-Петербург, Дальневосточный проспект, 22к1");
                        distanceMap.put(distance75, "Лукойл, Санкт-Петербург, Дальневосточный проспект, 40А");
                        distanceMap.put(distance76, "Лукойл, Санкт-Петербург, Дальневосточный проспект, 43 литер Ы");
                        distanceMap.put(distance77, "Татнефть, Санкт-Петербург, Дальневосточный проспект, 48А");
                        distanceMap.put(distance78, "NordPoint, Санкт-Петербург, ул. Демьяна Бедного, 15");
                        distanceMap.put(distance79, "NordPoint, Санкт-Петербург, ул. Десантников, 13");
                        distanceMap.put(distance80, "Газпромнефть, Санкт-Петербург, ул. Десантников, 21");
                        distanceMap.put(distance81, "Neste, Санкт-Петербург, Колпинское ш., 117");
                        distanceMap.put(distance82, "Роснефть, Санкт-Петербург, Днепропетровская ул., 20, лит. \"А\"");
                        distanceMap.put(distance83, "Роснефть, Санкт-Петербург, ул. Доблести, 19");
                        distanceMap.put(distance84, "Лукойл, Санкт-Петербург, ул. Долгоозерная, д. 30");
                        distanceMap.put(distance85, "Газпромнефть, Санкт-Петербург, дорога на Турухтанные Острова, 10, корп. 3");
                        distanceMap.put(distance86, "Кириши, Санкт-Петербург, дорога на Турухтанные острова, 18А");
                        distanceMap.put(distance87, "Лукойл, Санкт-Петербург, Дунайский проспект, 17а");
                        distanceMap.put(distance88, "Татнефть, Санкт-Петербург, Дунайский просп., 19");
                        distanceMap.put(distance89, "Роснефть, Санкт-Петербург, Дунайский проспект, 25А");
                        distanceMap.put(distance90, "Teboil, Санкт-Петербург, Дунайский проспект, 62");
                        distanceMap.put(distance91, "Газпромнефть, Санкт-Петербург, Дунайский проспект, 29");
                        distanceMap.put(distance92, "Газпромнефть, Санкт-Петербург, ул. Дыбенко, 1к1");
                        distanceMap.put(distance93, "Лукойл, Санкт-Петербург, ул. Есенина, 21");
                        distanceMap.put(distance94, "Teboil, Санкт-Петербург, Ждановская ул., 2А");
                        distanceMap.put(distance95, "Лукойл, Санкт-Петербург, Железноводская ул., 1А");
                        distanceMap.put(distance96, "Роснефть, Санкт-Петербург, Железнодорожная ул., 40А");
                        distanceMap.put(distance97, "Лукойл, Санкт-Петербург, ул. Замшина, 2А");
                        distanceMap.put(distance98, "Лукойл, Санкт-Петербург, Заневский проспект, 75А");
                        distanceMap.put(distance99, "Роснефть, Санкт-Петербург, ул. Запорожская, 33");
                        distanceMap.put(distance100, "Роснефть, Санкт-Петербург, Зеленогорск, Торфяная ул., 27Б");
                        distanceMap.put(distance101, "Газпромнефть, Санкт-Петербург, Земледельческая ул., 5");
                        distanceMap.put(distance102, "Газпромнефть, Санкт-Петербург, Зотовский проспект, 9, лит. А");
                        distanceMap.put(distance103, "Лукойл, Санкт-Петербург, Индустриальный проспект, 46А");
                        distanceMap.put(distance104, "Teboil, Санкт-Петербург, Индустриальный проспект, 47,корп. 1, лит. А");
                        distanceMap.put(distance105, "Газпромнефть, Санкт-Петербург, Индустриальный проспект, 68");
                        distanceMap.put(distance106, "Teboil, Санкт-Петербург, КАД, 34-й километр");
                        distanceMap.put(distance107, "Газпромнефть, Санкт-Петербург, ул. Камышовая, 21");
                        distanceMap.put(distance108, "Татнефть, Санкт-Петербург, Караваевская ул., 15");
                        distanceMap.put(distance109, "Лукойл, Санкт-Петербург, Караваевская ул., 3к2");
                        distanceMap.put(distance110, "Роснефть, Санкт-Петербург, ул. Карбышева, 5");
                        distanceMap.put(distance111, "Газпромнефть, Санкт-Петербург, ул. Карпатская, 15");
                        distanceMap.put(distance112, "Роснефть, Санкт-Петербург, ул. Книпович, 11 литер А");
                        distanceMap.put(distance113, "Лукойл, Санкт-Петербург, Кожевенная линия, 43А");
                        distanceMap.put(distance114, "NordPoint, Санкт-Петербург, ул. Коллонтай, 8");
                        distanceMap.put(distance115, "NordPoint, Санкт-Петербург, Коломяжский проспект, 19");
                        distanceMap.put(distance116, "Газпромнефть, Санкт-Петербург, Коломяжский проспект, 31");
                        distanceMap.put(distance117, "Роснефть, Санкт-Петербург, Коломяжский проспект, 13к7");
                        distanceMap.put(distance118, "Teboil, Санкт-Петербург, Комендантский проспект, 41А");
                        distanceMap.put(distance119, "Лукойл, Санкт-Петербург, Комендантский проспект, 43к2А");
                        distanceMap.put(distance120, "Кириши, Санкт-Петербург, ул. Коммуны, 14 литер А");
                        distanceMap.put(distance121, "Лукойл, Санкт-Петербург, ул. Композиторов, 30");
                        distanceMap.put(distance122, "Роснефть, Санкт-Петербург, ул. Кондратенко, 6");
                        distanceMap.put(distance123, "Газпромнефть, Санкт-Петербург, Краснопутиловская, 46/3");
                        distanceMap.put(distance124, "Teboil, Санкт-Петербург, Краснопутиловская ул., 86-А");
                        distanceMap.put(distance125, "Neste, Санкт-Петербург, Краснопутиловская ул., 95а");
                        distanceMap.put(distance126, "Роснефть, Санкт-Петербург, б-р Красных зорь, 15А");
                        distanceMap.put(distance127, "Газпромнефть, Санкт-Петербург, ул. Кржижановского, 10, корп. 3");
                        distanceMap.put(distance128, "Лукойл, Санкт-Петербург, ул. Кржижановского, 19");
                        distanceMap.put(distance129, "Роснефть, Санкт-Петербург, Кронштадтское шоссе, 5А");
                        distanceMap.put(distance130, "Teboil, Санкт-Петербург, Кронштадтская площадь, 2");
                        distanceMap.put(distance131, "Татнефть, Санкт-Петербург, ул. Крупской, 43, корп. 3");
                        distanceMap.put(distance132, "Neste, Санкт-Петербург, Кубинская ул., 74");
                        distanceMap.put(distance133, "Газпромнефть, Санкт-Петербург, ул. Кубинская, 75 корп. 3");
                        distanceMap.put(distance134, "Газпромнефть, Санкт-Петербург, ул. Кубинская, д. 82, к. 3");
                        distanceMap.put(distance135, "Роснефть, Санкт-Петербург, ул. Кубинская, 92");
                        distanceMap.put(distance136, "Лукойл, Санкт-Петербург, Кушелевская дорога, 9А");
                        distanceMap.put(distance137, "Газпромнефть, Санкт-Петербург, Кушелевская дорога, 8, литера А");
                        distanceMap.put(distance138, "Лукойл, Санкт-Петербург, Кушелевская дорога, 18А");
                        distanceMap.put(distance139, "Татнефть, Санкт-Петербург, Лабораторный проспект, 21");
                        distanceMap.put(distance140, "Кириши, Санкт-Петербург, Лапинский проспект, 10");
                        distanceMap.put(distance141, "Газпромнефть, Санкт-Петербург, Лахтинский проспект, 2к2А");
                        distanceMap.put(distance142, "Газпромнефть, Санкт-Петербург, Лахтинский проспект, 149");
                        distanceMap.put(distance143, "Роснефть, Санкт-Петербург, Левашовский проспект, 19");
                        distanceMap.put(distance144, "Teboil, Санкт-Петербург, Ленинский проспект, 102А");
                        distanceMap.put(distance145, "Лукойл, Санкт-Петербург, Ленинский проспект, 139к2а");
                        distanceMap.put(distance146, "Лукойл, Санкт-Петербург, Лиговский проспект, 246Т");
                        distanceMap.put(distance147, "Роснефть, Санкт-Петербург, Лиственная ул., 6 литер А");
                        distanceMap.put(distance148, "Лукойл, Санкт-Петербург, Литовская ул., 5-А, корпус 2");
                        distanceMap.put(distance149, "Лукойл, Санкт-Петербург, Ломоносов, Краснофлотское шоссе, 18/20");
                        distanceMap.put(distance150, "Газпромнефть, Санкт-Петербург, г. Ломоносов, Краснофлотское шоссе, 54");
                        distanceMap.put(distance151, "Лукойл, Санкт-Петербург, Магнитогорская ул., 34А");
                        distanceMap.put(distance152, "Роснефть, Санкт-Петербург, Малая Балканская ул., 13, лит. \"А\"");
                        distanceMap.put(distance153, "Газпромнефть, Санкт-Петербург, ул. Малая Балканская, 51к6");
                        distanceMap.put(distance154, "Лукойл, Санкт-Петербург, ул. Малая Балканская, 55к1а");
                        distanceMap.put(distance155, "Лукойл, Санкт-Петербург, Малая Балканская ул., 21");
                        distanceMap.put(distance156, "Роснефть, Санкт-Петербург, Малоохтинская набережная, 16");
                        distanceMap.put(distance157, "Neste, Санкт-Петербург, Малоохтинский проспект, 59");
                        distanceMap.put(distance158, "Роснефть, Санкт-Петербург, Малый проспект, 61");
                        distanceMap.put(distance159, "Ладога, Санкт-Петербург, Малый проспект В.О., 79");
                        distanceMap.put(distance160, "Роснефть, Санкт-Петербург, ул. Маршала Говорова, 35к3");
                        distanceMap.put(distance161, "Газпромнефть, Санкт-Петербург, ул. Маршала Говорова, 41");
                        distanceMap.put(distance162, "Роснефть, Санкт-Петербург, Минеральная ул., 16");
                        distanceMap.put(distance163, "Neste, Санкт-Петербург, Московский проспект, 102а");
                        distanceMap.put(distance164, "Neste, Санкт-Петербург, Московский проспект, 156А");
                        distanceMap.put(distance165, "Лукойл, Санкт-Петербург, наб. Обводного канала, 34");
                        distanceMap.put(distance166, "Татнефть, Санкт-Петербург, наб. Обводного канала, 118к8");
                        distanceMap.put(distance167, "Роснефть, Санкт-Петербург, наб. реки Смоленки, 27А");
                        distanceMap.put(distance168, "Роснефть, Санкт-Петербург, наб. реки Фонтанки, 156А, литер 3");
                        distanceMap.put(distance169, "Татнефть, Санкт-Петербург, ул. Народная, 100");
                        distanceMap.put(distance170, "Роснефть, Санкт-Петербург, ул. Нахимова, 18А");
                        distanceMap.put(distance171, "Neste, Санкт-Петербург, Нейшлотский переулок, 12");
                        distanceMap.put(distance172, "Teboil, Санкт-Петербург, ул. Новороссийская, 15, литер А");
                        distanceMap.put(distance173, "Лукойл, Санкт-Петербург, ул. Оборонная, 30 литер А");
                        distanceMap.put(distance174, "Роснефть, Санкт-Петербург, Зольная ул., 1");
                        distanceMap.put(distance175, "Татнефть, Санкт-Петербург, Октябрьская набережная, 12А");
                        distanceMap.put(distance176, "Линос, Санкт-Петербург, Октябрьская набережная, 38, корп.3");
                        distanceMap.put(distance177, "Лукойл, Санкт-Петербург, Октябрьская набережная, 46к2 литер А");
                        distanceMap.put(distance178, "Teboil, Санкт-Петербург, Октябрьская набережная, 46А");
                        distanceMap.put(distance179, "Татнефть, Санкт-Петербург, Октябрьская набережная, 56 к. 2");
                        distanceMap.put(distance180, "Роснефть, Санкт-Петербург, Октябрьская набережная, 68К2");
                        distanceMap.put(distance181, "Роснефть, Санкт-Петербург, Октябрьская набережная, 112 корп. 4");
                        distanceMap.put(distance182, "Татнефть, Санкт-Петербург, ул. Оптиков, 1 корп.2 лит.А");
                        distanceMap.put(distance183, "Газпромнефть, Санкт-Петербургское ш., 130, корп. 1, Петергоф");
                        distanceMap.put(distance184, "Teboil, Санкт-Петербург, Парашютная ул., 73, корп. 1");
                        distanceMap.put(distance185, "Neste, Санкт-Петербург, ул. Партизана Германа, 4");
                        distanceMap.put(distance186, "Лукойл, Санкт-Петербург, пер. Декабристов, 9 литер А");
                        distanceMap.put(distance187, "Газпромнефть, Санкт-Петербург, пер. Матюшенко, 3А");
                        distanceMap.put(distance188, "Сургутнефтегаз, Санкт-Петербург, Песочная набережная, 30");
                        distanceMap.put(distance189, "Neste, Санкт-Петербург, Петергофское шоссе, 98к2");
                        distanceMap.put(distance190, "Роснефть, Санкт-Петербург, просп. КИМа, 32");
                        distanceMap.put(distance191, "Газпромнефть, Санкт-Петербург, Петродворец, ул. Астрономическая, д. 11");
                        distanceMap.put(distance192, "Teboil, Санкт-Петербург, Пискаревский проспект, 169К2");
                        distanceMap.put(distance193, "Газпромнефть, Санкт-Петербург, Пискаревский проспект, 4");
                        distanceMap.put(distance194, "Роснефть, Санкт-Петербург, Пискаревский проспект, 27/1");
                        distanceMap.put(distance195, "Лукойл, Санкт-Петербург, Пискаревский проспект, 30");
                        distanceMap.put(distance196, "Лукойл, Санкт-Петербург, Пискаревский проспект, 43к2");
                        distanceMap.put(distance197, "Сургутнефтегаз, Санкт-Петербург, Пискарёвский проспект, 135");
                        distanceMap.put(distance198, "Газпромнефть, Санкт-Петербург, ул. Планерная, 30");
                        distanceMap.put(distance199, "Татнефть, Санкт-Петербург, Планерная ул., 57к1");
                        distanceMap.put(distance200, "Лукойл, Санкт-Петербург, ул. Полевая Сабировская, 48к2А");
                        distanceMap.put(distance201, "Газпромнефть, Санкт-Петербург, ул. Полевая-Сабировская, 56");
                        distanceMap.put(distance202, "Норд-Лайн, Санкт-Петербург, Политехническая ул., 4, корп. 3Б");
                        distanceMap.put(distance203, "Роснефть, Санкт-Петербург, Полюстровский проезд, 73");
                        distanceMap.put(distance204, "Лукойл, Санкт-Петербург, Полюстровский проспект, 93");
                        distanceMap.put(distance205, "Газпромнефть, Санкт-Петербург, пос. Парголово, проспект Энгельса, 173");
                        distanceMap.put(distance206, "Газпромнефть, Санкт-Петербург, пос. Парголово, проспект Энгельса, д. 190, к.1");
                        distanceMap.put(distance207, "Кириши, Санкт-Петербург, Фронтовая ул., 8БА, посёлок Стрельна");
                        distanceMap.put(distance208, "Газпромнефть, Санкт-Петербург, ул. Потапова, 7");
                        distanceMap.put(distance209, "Роснефть, Санкт-Петербург, Придорожная аллея, 24");
                        distanceMap.put(distance210, "Газпромнефть, Санкт-Петербург, Придорожная аллея, 28");
                        distanceMap.put(distance211, "Роснефть, Санкт-Петербург, Приморский проспект, 56");
                        distanceMap.put(distance212, "Лукойл, Санкт-Петербург, Приморское шоссе, 45А");
                        distanceMap.put(distance213, "Teboil, Санкт-Петербург, Приморское шоссе, 47А");
                        distanceMap.put(distance214, "Кириши, Санкт-Петербург, Приморское шоссе, 142");
                        distanceMap.put(distance215, "NordPoint, Санкт-Петербург, Приморское шоссе, 251");
                        distanceMap.put(distance216, "Роснефть, Санкт-Петербург, Сестрорецк, Приморское шоссе, 262");
                        distanceMap.put(distance217, "Кириши, Санкт-Петербург, промзона «Парнас», 1-й верхний переулок, 3А");
                        distanceMap.put(distance218, "Роснефть, Санкт-Петербург, проспект Александровской Фермы, 17к2");
                        distanceMap.put(distance219, "Татнефть, Санкт-Петербург, проспект Большевиков, 36");
                        distanceMap.put(distance220, "Роснефть, Санкт-Петербург, проспект Большевиков, 44 литер А");
                        distanceMap.put(distance221, "Газпромнефть, Санкт-Петербург, проспект Ветеранов, 161");
                        distanceMap.put(distance222, "Роснефть, Санкт-Петербург, проспект Ветеранов, 182");
                        distanceMap.put(distance223, "Лукойл, Санкт-Петербург, проспект Добролюбова, 20к6А");
                        distanceMap.put(distance224, "Татнефть, Санкт-Петербург, проспект Испытателей, 2 корп.1 литер А");
                        distanceMap.put(distance225, "Роснефть, Санкт-Петербург, проспект Королева, 40");
                        distanceMap.put(distance226, "Газпромнефть, Санкт-Петербург, проспект Королева, 51");
                        distanceMap.put(distance227, "Лукойл, Санкт-Петербург, проспект Косыгина, 2А");
                        distanceMap.put(distance228, "Neste, Санкт-Петербург, проспект Косыгина, 20");
                        distanceMap.put(distance229, "Газпромнефть, Санкт-Петербург, проспект Культуры, 3А");
                        distanceMap.put(distance230, "Роснефть, Санкт-Петербург, проспект Культуры, 30");
                        distanceMap.put(distance231, "Газпромнефть, Санкт-Петербург, проспект Культуры, 33А");
                        distanceMap.put(distance232, "Лукойл, Санкт-Петербург, проспект Культуры, д.49 лит.А");
                        distanceMap.put(distance234, "Neste, Санкт-Петербург, проспект Маршала Блюхера, 2К7");
                        distanceMap.put(distance235, "Лукойл, Санкт-Петербург, проспект Маршала Блюхера, 39А");
                        distanceMap.put(distance236, "Роснефть, Санкт-Петербург, проспект Маршала Жукова, 10");
                        distanceMap.put(distance237, "Кириши, Санкт-Петербург, проспект Маршала Жукова, 23 литер А");
                        distanceMap.put(distance238, "Лукойл, Санкт-Петербург, проспект Маршала Жукова, 46 литер А");
                        distanceMap.put(distance239, "Лукойл, Санкт-Петербург, проспект Маршала Жукова, 49А");
                        distanceMap.put(distance240, "Роснефть, Санкт-Петербург, проспект Маршала Казакова, 25");
                        distanceMap.put(distance241, "Роснефть, Санкт-Петербург, проспект Народного Ополчения, 16");
                        distanceMap.put(distance242, "Роснефть, Санкт-Петербург, проспект Народного Ополчения, 26к3");
                        distanceMap.put(distance243, "Лукойл, Санкт-Петербург, проспект Народного Ополчения, 80");
                        distanceMap.put(distance244, "Teboil, Санкт-Петербург, проспект Народного Ополчения, 147А");
                        distanceMap.put(distance245, "Татнефть, Санкт-Петербург, Проспект Народного Ополчения, 201А");
                        distanceMap.put(distance246, "Лукойл, Санкт-Петербург, проспект Наставников, 1 литер А");
                        distanceMap.put(distance247, "Роснефть, Санкт-Петербург, проспект Наставников, 2К1");
                        distanceMap.put(distance248, "Лукойл, Санкт-Петербург, проспект Науки, 52А");
                        distanceMap.put(distance249, "Роснефть, Санкт-Петербург, проспект Непокоренных, 15");
                        distanceMap.put(distance250, "Роснефть, Санкт-Петербург, проспект Непокоренных, 53");
                        distanceMap.put(distance251, "Татнефть, Санкт-Петербург, проспект Непокорённых, 62");
                        distanceMap.put(distance252, "Роснефть, Санкт-Петербург, проспект Обуховской Обороны, 3");
                        distanceMap.put(distance253, "Татнефть, Санкт-Петербург, проспект Обуховской Обороны, 49к3");
                        distanceMap.put(distance254, "Сургутнефтегаз, Санкт-Петербург, проспект Обуховской обороны, 138");
                        distanceMap.put(distance255, "Газпромнефть, Санкт-Петербург, проспект Обуховской Обороны, 303");
                        distanceMap.put(distance256, "Лукойл, Санкт-Петербург, проспект Просвещения, 10А");
                        distanceMap.put(distance257, "Neste, Санкт-Петербург, проспект Сизова, 19");
                        distanceMap.put(distance258, "Лукойл, Санкт-Петербург, проспект Стачек, 81А");
                        distanceMap.put(distance259, "Роснефть, Санкт-Петербург, проспект Стачек, 108 литер А");
                        distanceMap.put(distance260, "Татнефть, Санкт-Петербург, проспект Стачек, 115а");
                        distanceMap.put(distance261, "Лукойл, Санкт-Петербург, проспект Стачек, 119а");
                        distanceMap.put(distance262, "Лукойл, Санкт-Петербург, проспект Художников, 47А");
                        distanceMap.put(distance263, "Лукойл, Санкт-Петербург, проспект Энгельса, 164");
                        distanceMap.put(distance264, "NordPoint, Санкт-Петербург, проспект Энгельса, 179");
                        distanceMap.put(distance265, "Газпромнефть, Санкт-Петербург, проспект Энергетиков / ул. Стасовой");
                        distanceMap.put(distance266, "Teboil, Санкт-Петербург, проспект Юрия Гагарина, 2к2");
                        distanceMap.put(distance267, "Роснефть, Санкт-Петербург, проспект Юрия Гагарина, 32 литер В");
                        distanceMap.put(distance268, "Газпромнефть, Санкт-Петербург, Пулковское шоссе, 21к2А");
                        distanceMap.put(distance269, "Роснефть, Санкт-Петербург, Пулковское шоссе, 27, Р23, справа");
                        distanceMap.put(distance270, "Лукойл, Санкт-Петербург, Пулковское шоссе, 37к1А, Р23, справа");
                        distanceMap.put(distance271, "Татнефть, Санкт-Петербург, Пулковское шоссе, 38, Р23, слева");
                        distanceMap.put(distance272, "Газпромнефть, Санкт-Петербург, Пулковское шоссе, 42А, Р23, слева");
                        distanceMap.put(distance273, "Teboil, Санкт-Петербург, Пулковское шоссе, 46/3, Р23, слева");
                        distanceMap.put(distance274, "Роснефть, Санкт-Петербург, Пулковское шоссе, 55, Р23, справа");
                        distanceMap.put(distance275, "Татнефть, Санкт-Петербург, Пулковское шоссе, 72 А, Р23, слева");
                        distanceMap.put(distance276, "Газпромнефть, Пулковское ш., 68, посёлок Шушары");
                        distanceMap.put(distance277, "Neste, Санкт-Петербург, Пулковское шоссе, 109а, Р23, справа");
                        distanceMap.put(distance278, "Лукойл, Санкт-Петербург, Гусарская улица, 10А");
                        distanceMap.put(distance279, "Лукойл, Санкт-Петербург, ул. Розенштейна, 37-Б");
                        distanceMap.put(distance280, "Neste, Санкт-Петербург, Российский проспект, 2");
                        distanceMap.put(distance281, "Роснефть, Санкт-Петербург, Рощинская ул., 46");
                        distanceMap.put(distance282, "Роснефть, Санкт-Петербург, ул. Руставели, 25А");
                        distanceMap.put(distance283, "Teboil, Санкт-Петербург, ул. Руставели, 42А");
                        distanceMap.put(distance284, "Газпромнефть, Санкт-Петербург, ул. Руставели, д. 45, к. 2");
                        distanceMap.put(distance285, "Кириши, Санкт-Петербург, ул. Руставели, 48А");
                        distanceMap.put(distance286, "Линос, Санкт-Петербург, ул. Руставели, 54А");
                        distanceMap.put(distance287, "Лукойл, Санкт-Петербург, ул. Руставели, 81 литер А");
                        distanceMap.put(distance288, "Татнефть, Санкт-Петербург, ул. Савушкина, 87");
                        distanceMap.put(distance289, "Газпромнефть, Санкт-Петербург, ул. Савушкина, 110");
                        distanceMap.put(distance290, "Роснефть, Санкт-Петербург, улица Салова, 55А");
                        distanceMap.put(distance291, "Лукойл, Санкт-Петербург, ул. Салова, 74к1");
                        distanceMap.put(distance292, "Линос, Санкт-Петербург, ул. Салова, 82");
                        distanceMap.put(distance293, "Роснефть, Санкт-Петербург, проспект Будённого, соор33/1\n");
                        distanceMap.put(distance294, "Газпромнефть, Санкт-Петербург, п. Саперный, Петрозаводское шоссе, 52");
                        distanceMap.put(distance295, "Лукойл, Санкт-Петербург, Свердловская набережная, 58к4а");
                        distanceMap.put(distance296, "Лукойл, Санкт-Петербург, Свердловская набережная, 9-А");
                        distanceMap.put(distance297, "Роснефть, Санкт-Петербург, Северная площадь, 2");
                        distanceMap.put(distance298, "Лукойл, Санкт-Петербург, Северный проспект, 13");
                        distanceMap.put(distance299, "Лукойл, Санкт-Петербург, Северный проспект, 20к2");
                        distanceMap.put(distance300, "Neste, Санкт-Петербург, Северный проспект, 32");
                        distanceMap.put(distance301, "Татнефть, Санкт-Петербург, Северный проспект, 97");
                        distanceMap.put(distance302, "Teboil, Санкт-Петербург, ул. Седова, 11А");
                        distanceMap.put(distance303, "Лукойл, Санкт-Петербург, ул. Седова, 12к2а");
                        distanceMap.put(distance304, "Роснефть, Санкт-Петербург, Советский проспект, 37");
                        distanceMap.put(distance305, "Татнефть, Санкт-Петербург, Советский проспект, 57А");
                        distanceMap.put(distance306, "Лукойл, Санкт-Петербург, Советский проспект, 59");
                        distanceMap.put(distance307, "Лукойл, Санкт-Петербург, ул. Софийская, уч. 1");
                        distanceMap.put(distance308, "Газпромнефть, Санкт-Петербург, ул. Софийская, 69");
                        distanceMap.put(distance309, "Роснефть, Санкт-Петербург, Софийская ул., д. 73, к. 2");
                        distanceMap.put(distance310, "NordPoint, Санкт-Петербург, ул. Софийская, 77");
                        distanceMap.put(distance311, "Татнефть, Санкт-Петербург, ул. Софийская, д. 89, лит. А");
                        distanceMap.put(distance312, "Neste, Санкт-Петербург, ул. Софийская, 127 к. 1");
                        distanceMap.put(distance313, "Teboil, Санкт-Петербург, ул. Софийская, 135к1");
                        distanceMap.put(distance314, "Лукойл, Санкт-Петербург, Софийская ул., 1А");
                        distanceMap.put(distance315, "Neste, Санкт-Петербург, Софийская ул., 18А");
                        distanceMap.put(distance316, "Лукойл, Санкт-Петербург, Софийская ул., 59к1а");
                        distanceMap.put(distance317, "NordPoint, Санкт-Петербург, Софийская ул., 77");
                        distanceMap.put(distance318, "Газпромнефть, Санкт-Петербург, Софийская ул., 85 литер А");
                        distanceMap.put(distance319, "Татнефть, Санкт-Петербург, Софийская ул., 89 литер А");
                        distanceMap.put(distance320, "Лукойл, Санкт-Петербург, Средний проспект, 82 корп.2а");
                        distanceMap.put(distance321, "Neste, Санкт-Петербург, Средний проспект, 91к2");
                        distanceMap.put(distance322, "Роснефть, Санкт-Петербург, ул. Стасовой, 13");
                        distanceMap.put(distance323, "Лукойл, Санкт-Петербург, Стрельна, Волхонское шоссе, 4А");
                        distanceMap.put(distance324, "NordPoint, Санкт-Петербург, ул. Студенческая, 15");
                        distanceMap.put(distance325, "Роснефть, Санкт-Петербург, Суздальский проспект, 99 литер А");
                        distanceMap.put(distance326, "Лукойл, Санкт-Петербург, Суздальский проспект, 49к1А");
                        distanceMap.put(distance327, "Роснефть, Санкт-Петербург, Таврический переулок, 13");
                        distanceMap.put(distance328, "Neste, Санкт-Петербург, Таллинское ш., 161, корп. 2");
                        distanceMap.put(distance329, "Роснефть, Санкт-Петербург, Театральная площадь, 7А");
                        distanceMap.put(distance330, "Лукойл, Санкт-Петербург, Токсовская ул., 2");
                        distanceMap.put(distance331, "Teboil, Санкт-Петербург, Торжковская ул., 19А");
                        distanceMap.put(distance332, "Кириши, Санкт-Петербург, Торфяная дорога, 10 литер А");
                        distanceMap.put(distance333, "Роснефть, Санкт-Петербург, ул. Трефолева, 42, корп. 3");
                        distanceMap.put(distance334, "Газпромнефть, Санкт-Петербург, п. Тярлево, Фильтровское шоссе, 1");
                        distanceMap.put(distance335, "Сургутнефтегаз, Санкт-Петербург, ул. Балтийская, 43");
                        distanceMap.put(distance336, "Татнефть, Санкт-Петербург, ул. Вербная, 23а");
                        distanceMap.put(distance337, "NordPoint, Санкт-Петербург, ул. Демьяна Бедного, 15");
                        distanceMap.put(distance338, "Роснефть, Санкт-Петербург, ул. Карпатская, 1");
                        distanceMap.put(distance339, "Татнефть, Санкт-Петербург, ул. Кузнецовская, 35А");
                        distanceMap.put(distance340, "Татнефть, Санкт-Петербург, ул. Лабораторная, 19");
                        distanceMap.put(distance341, "Лукойл, Санкт-Петербург, ул. Орджоникидзе, 50А");
                        distanceMap.put(distance342, "NordPoint, Санкт-Петербург, ул. Планерная, 22");
                        distanceMap.put(distance343, "Газпромнефть, Санкт-Петербург, ул. Седова, 43, корп. 2");
                        distanceMap.put(distance344, "NordPoint, Санкт-Петербург, ул. Студенческая, 15");
                        distanceMap.put(distance345, "Лукойл, Санкт-Петербург, Кожевенная линия, 43");
                        distanceMap.put(distance346, "Газпромнефть, Санкт-Петербург, п. Усть-Славянка, Советский проспект, д. 55, к. 1");
                        distanceMap.put(distance347, "Кириши, Санкт-Петербург, Фермское шоссе, 35");
                        distanceMap.put(distance348, "Teboil, Санкт-Петербург, ул. Фучика, 6к3");
                        distanceMap.put(distance349, "Газпромнефть, Санкт-Петербург, ул. Фучика, 8к2");
                        distanceMap.put(distance350, "Роснефть, Санкт-Петербург, ул. Фучика, 23");
                        distanceMap.put(distance351, "Teboil, Санкт-Петербург, Хасанская ул., 1к1");
                        distanceMap.put(distance352, "CircleK, Санкт-Петербург, Химический пер., 1, к. 1");
                        distanceMap.put(distance353, "Teboil, Санкт-Петербург, ул. Хошимина, 2А");
                        distanceMap.put(distance354, "Газпромнефть, Санкт-Петербург, ул. Циолковского, 18Я");
                        distanceMap.put(distance355, "Кириши, Санкт-Петербург, пер. Челиева, 14А");
                        distanceMap.put(distance356, "Лукойл, Санкт-Петербург, Черниговская ул., 25А");
                        distanceMap.put(distance357, "Лукойл, Санкт-Петербург, ул. Шаумяна, 15А");
                        distanceMap.put(distance358, "Лукойл, Санкт-Петербург, Шафировский проспект, 10к4а");
                        distanceMap.put(distance359, "Татнефть, Санкт-Петербург, Шафировский проспект, 20");
                        distanceMap.put(distance360, "Лукойл, Санкт-Петербург, Шафировский проспект, 21");
                        distanceMap.put(distance361, "Сургутнефтегаз, Санкт-Петербург, Шафировский проспект, 24А");
                        distanceMap.put(distance362, "Лукойл, Санкт-Петербург, ул. Швецова, 31 литер А");
                        distanceMap.put(distance363, "Татнефть, Санкт-Петербург, ул. Шелгунова, 12к2");
                        distanceMap.put(distance364, "Neste, Санкт-Петербург, Школьная ул., 77");
                        distanceMap.put(distance365, "Газпромнефть, Санкт-Петербург, Школьная ул., 100А");
                        distanceMap.put(distance366, "Лукойл, Санкт-Петербург, Школьная ул., 91А");
                        distanceMap.put(distance367, "Лукойл, Санкт-Петербург, шоссе Революции, 70");
                        distanceMap.put(distance368, "Лукойл, Санкт-Петербург, ул. Шотландская, 14 литер А");
                        distanceMap.put(distance369, "Лукойл, Санкт-Петербург, Штурманская ул., 7к1А");
                        distanceMap.put(distance370, "Роснефть, Санкт-Петербург, Южное шоссе, 61");
                        distanceMap.put(distance371, "Роснефть, г. Сестрорецк, Владимирский проспект, 16");
                        distanceMap.put(distance372, "Газпромнефть, Санкт-Петербург, Кронштадтское шоссе, 15А");
                        //С помощью Stream API TreeMap преобразуем в List, где
                        List<String> ListDistance = distanceMap.entrySet().parallelStream().collect(ArrayList::new,
                                (list, element) -> list.add(element.getValue()), ArrayList::addAll);

                        //Отправляем сообщение пользователю с 3 самими близкими заправками к нему
                        sendMessage(chatID, StaticTextForTgBot.NEARBY_1 +
                                ListDistance.get(0));
                        sendMessage(chatID, StaticTextForTgBot.NEARBY_2 +
                                ListDistance.get(1));
                        sendMessage(chatID, StaticTextForTgBot.NEARBY_3 +
                                ListDistance.get(2));
                    }else{
                        sendMessage(chatID, StaticTextForTgBot.NEARBY_ERROR_TEXT);
                    }

                    break;

                case "/profitably":
                    Users usersFuel = usersRepository.findById(chatID).get();
                    //Если пользователь выбрал тип топлива, то все ок, если не выбрал, ты выводим сообщение об ошибке.
                    if(usersFuel.getFuelType() != null) {
                        if (usersFuel.getFuelType().equals("92")) {
                            sendMessage(chatID, StaticTextForTgBot.PROFITABLY_AI_92_TEXT +
                                    gasStationFuelTypeRepository.getByAddress92());
                        } else if (usersFuel.getFuelType().equals("95")) {
                            sendMessage(chatID, StaticTextForTgBot.PROFITABLY_AI_95_TEXT +
                                    gasStationFuelTypeRepository.getByAddress95());
                        } else if (usersFuel.getFuelType().equals("98")) {
                            sendMessage(chatID, StaticTextForTgBot.PROFITABLY_AI_98_TEXT +
                                    gasStationFuelTypeRepository.getByAddress98());
                        } else if (usersFuel.getFuelType().equals("100")) {
                            sendMessage(chatID, StaticTextForTgBot.PROFITABLY_AI_100_TEXT +
                                    gasStationFuelTypeRepository.getByAddress100());
                        } else if (usersFuel.getFuelType().equals("50")) {
                            sendMessage(chatID, StaticTextForTgBot.PROFITABLY_DT_TEXT +
                                    gasStationFuelTypeRepository.getByAddressDT());
                        }
                    }else {
                        sendMessage(chatID, StaticTextForTgBot.PROFITABLY_ERROR_TEXT);
                    }
                    break;

                default:
                    //обработка координат, пользователь отсылает их обычным текстом, а не командой.
                    var originalMessage = update.getMessage();
                    var response = new SendMessage();
                    response.setChatId(originalMessage.getChatId().toString());

                    String str = (originalMessage.getText());
                    String[] locations = str.split("\\s");

                    if (locations.length == 2) {

                        var stringLatitude = locations[0];
                        var stringLongitude = locations[1];

                        String stringLatitudeNew = stringLatitude.replaceAll(",$", "");
                        String stringLongitudeNew = stringLongitude.replaceAll(",$", "");

                        response.setText(StaticTextForTgBot.RESPONSE_1 +
                                StaticTextForTgBot.RESPONSE_2 + stringLatitudeNew + "\n" +
                                StaticTextForTgBot.RESPONSE_3 + stringLongitudeNew + "\n" +
                                StaticTextForTgBot.RESPONSE_4 + "\n" +
                                StaticTextForTgBot.RESPONSE_5);
                        sendAnswerMessage(response);

                        var doubleLatitude = Double.parseDouble(String.valueOf(stringLatitudeNew));
                        var doubleLongitude = Double.parseDouble(String.valueOf(stringLongitudeNew));

                        Users users = usersRepository.findById(chatID).get();
                        users.setLatitude(doubleLatitude);
                        users.setLongitude(doubleLongitude);
                        usersRepository.save(users);
                        log.info("user saved: " + users);
                    }else {
                        sendMessage(chatID, StaticTextForTgBot.DEFAULT_ERROR_ANSWER);
                    }
            }

        } else if (update.hasCallbackQuery()) { // Здесь мы проверяем, если нам пришла не команда, а id какой-то кнопки, то выполняем какие-то действия
            String callbackData = update.getCallbackQuery().getData();
            long messageID = update.getCallbackQuery().getMessage().getMessageId(); //получаем id сообщения, которое отправил бот и редактируем его
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            switch (callbackData) {
                case StaticTextForTgBot.AI_92: {
                    executeEditMessageText(StaticTextForTgBot.AI_92_TEXT, chatID, messageID);
                    Users users = usersRepository.findById(chatID).get();
                    users.setFuelType("92");
                    usersRepository.save(users);
                    break;
                }
                case StaticTextForTgBot.AI_95: {
                    executeEditMessageText(StaticTextForTgBot.AI_95_TEXT, chatID, messageID);
                    Users users = usersRepository.findById(chatID).get();
                    users.setFuelType("95");
                    usersRepository.save(users);
                    break;
                }
                case StaticTextForTgBot.AI_98: {
                    executeEditMessageText(StaticTextForTgBot.AI_98_TEXT, chatID, messageID);
                    Users users = usersRepository.findById(chatID).get();
                    users.setFuelType("98");
                    usersRepository.save(users);
                    break;
                }
                case StaticTextForTgBot.AI_100: {
                    executeEditMessageText(StaticTextForTgBot.AI_100_TEXT, chatID, messageID);
                    Users users = usersRepository.findById(chatID).get();
                    users.setFuelType("100");
                    usersRepository.save(users);
                    break;
                }
                case StaticTextForTgBot.AI_50_DT: {
                    executeEditMessageText(StaticTextForTgBot.AI_DT_TEXT, chatID, messageID);
                    Users users = usersRepository.findById(chatID).get();
                    users.setFuelType("50");
                    usersRepository.save(users);
                    break;
                }
            }
        }
    }
    // Метод рассчитывает расстояние между двумя точками.
    public double calculateDistanceBetweenPointsWithPoint2D(
            double x1,
            double y1,
            double x2,
            double y2) {
        return Point2D.distance(x1, y1, x2, y2);
    }
    public void sendAnswerMessage(SendMessage message){
        if (message != null){
            try {
                execute(message);
            } catch (TelegramApiException e){
                log.error(String.valueOf(e));
            }
        }
    }
    private void executeEditMessageText(String text, long chatID, long messageID){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatID));
        message.setText(text);
        message.setMessageId((int)messageID);

        try {
            execute(message);
        }catch (TelegramApiException e){
            log.error("Error occurred: " + e.getMessage());
        }
    }

    //Создаем кнопки под сообщением, которое отправил бот после вызова команды /fuel
    private void selectFuel(Long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(StaticTextForTgBot.FUEL_TEXT);

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();

        var button92 = new InlineKeyboardButton();
        var button95 = new InlineKeyboardButton();
        var button98 = new InlineKeyboardButton();
        var button100 = new InlineKeyboardButton();
        var buttonDt = new InlineKeyboardButton();

        button92.setText("АИ-92");
        button92.setCallbackData(StaticTextForTgBot.AI_92);
        button95.setText("АИ-95");
        button95.setCallbackData(StaticTextForTgBot.AI_95);
        button98.setText("АИ-98");
        button98.setCallbackData(StaticTextForTgBot.AI_98);
        button100.setText("АИ-100");
        button100.setCallbackData(StaticTextForTgBot.AI_100);
        buttonDt.setText("ДТ");
        buttonDt.setCallbackData(StaticTextForTgBot.AI_50_DT);

        rowInLine.add(button92);
        rowInLine.add(button95);
        rowInLine.add(button98);
        rowInLine.add(button100);
        rowInLine.add(buttonDt);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        executeMessage(message);
    }
    private void startCommandReceived(long chatID, String name) {
        String answer = "Привет, " + name + StaticTextForTgBot.START_TEXT;
        log.info("Replied to user " +  name);
        sendMessage(chatID, answer);
    }
    //Метод для отправки сообщений пользователю.
    private void sendMessage(long chatID, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatID)); //из сообщения достаем chatId
        message.setText(textToSend); //добавляем ответ из нашего приложения

        executeMessage(message); //вызываем метод для отправки вью и передаем в него ответ
    }
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        }catch (TelegramApiException e){
            log.error("Error occurred: " + e.getMessage());
        }
    }
}
