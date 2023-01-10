package com.example.telegrambot.service;

import com.vdurmont.emoji.EmojiParser;

public class StaticTextForTgBot {

    static final String AI_92_TEXT = EmojiParser.parseToUnicode(":ok_hand: " + "Отлично, вы выбрали АИ-92 \n" +
            ":chart_with_downwards_trend: " + "Вы уже сейчас сможете узнать адрес заправки с самым дешевым топливом 'АИ-92', отправив боту команду /profitably \n" +
            ":world_map: " + "Если вы хотите узнать по какому адресу находятся 3 ближайшие заправки, отправьте боту свои координаты с помощью команды /location.");
    static final String AI_95_TEXT = EmojiParser.parseToUnicode(":ok_hand: " + "Отлично, вы выбрали АИ-95 \n" +
            ":chart_with_downwards_trend: " + "Вы уже сейчас сможете узнать адрес заправки с самым дешевым топливом 'АИ-95', отправив боту команду /profitably \n" +
            ":world_map: " + "Если вы хотите узнать по какому адресу находятся 3 ближайшие заправки, отправьте боту свои координаты с помощью команды /location.");
    static final String AI_98_TEXT = EmojiParser.parseToUnicode(":ok_hand: " + "Отлично, вы выбрали АИ-98 \n" +
            ":chart_with_downwards_trend: " + "Вы уже сейчас сможете узнать адрес заправки с самым дешевым топливом 'АИ-98', отправив боту команду /profitably \n" +
            ":world_map: " + "Если вы хотите узнать по какому адресу находятся 3 ближайшие заправки, отправьте боту свои координаты с помощью команды /location.");
    static final String AI_100_TEXT = EmojiParser.parseToUnicode(":ok_hand: " + "Отлично, вы выбрали АИ-100 \n" +
            ":chart_with_downwards_trend: " + "Вы уже сейчас сможете узнать адрес заправки с самым дешевым топливом 'АИ-100', отправив боту команду /profitably \n" +
            ":world_map: " + "Если вы хотите узнать по какому адресу находятся 3 ближайшие заправка, отправьте боту свои координаты с помощью команды /location.");
    static final String AI_DT_TEXT = EmojiParser.parseToUnicode(":ok_hand: " + "Отлично, вы выбрали ДТ \n" +
            ":chart_with_downwards_trend: " + "Вы уже сейчас сможете узнать адрес заправки с самым дешевым топливом 'ДТ', отправив боту команду /profitably \n" +
            ":world_map: " + "Если вы хотите узнать по какому адресу находятся 3 ближайшие заправки, отправьте боту свои координаты с помощью команды /location.");

    static final String COMMAND_START_TEXT = "Отправьте боту /start, чтобы запустить бота";
    static final String COMMAND_HELP_TEXT = "Отправьте боту /start, чтобы запустить бота";
    static final String COMMAND_FUEL_TEXT = "Отправьте боту /start, чтобы запустить бота";
    static final String COMMAND_LOCATION_TEXT = "Отправьте боту /start, чтобы запустить бота";
    static final String COMMAND_NEARBY_TEXT = "Отправьте боту /nearby, чтобы найти 3 ближайшие заправки";
    static final String COMMAND_PROFITABLY_TEXT = "Отправьте боту /profitably, чтобы найти заправку с самым дешевым топливом";

    static final String START_TEXT = EmojiParser.parseToUnicode(
            " приятно познакомиться! " + " \uD83E\uDD70" + "\n" +
                    ":oil_drum: " + "Этот бот поможет вам заправляться намного выгоднее! Бот будет показывать где находятся 3 ближайшие заправки или заправка с самой " +
                    "выгодной ценой на топливо для вашего автомобиля" + ":red_car:" + "\n"+
                    "Чтобы бот смог помочь, вам необходимо сделать следующее:\n" +
                    " :red_circle: " + "1. Выберите топливо, которым вы хотели бы заправить свой автомобиль с помощью команды /fuel, и вы уже сможете узнать название и адрес заправки где находится самое дешевое топливо с помощью команды /profitably \n" +
                    " :red_circle: " + "2. Отправьте боту свои координаты с помощью команды /location , чтобы бот мог определить ваше местоположение.\n" +
                    " :red_circle: " + "3. После того, как бот получит ваши координаты, он сможет показать вам, где находятся 3 ближайшие заправки с помощью команды /nearby.\n");
    static final String FUEL_TEXT = EmojiParser.parseToUnicode("Выберите каким топливом вы бы хотели заправить свой автомобиль" + " :fuelpump:");
    static final String NEARBY_ERROR_TEXT = EmojiParser.parseToUnicode("Вы еще не ввели свои координаты" + ":sweat:" + "\n" +
            "Используйте команду /location и отправьте боту свои координаты  " + ":world_map:");
    static final String PROFITABLY_ERROR_TEXT = EmojiParser.parseToUnicode("Вы еще не выбрали топливо, которым хотели бы заправить свой автомобиль" + ":sweat:" + "\n" +
            "Используйте команду /fuel и выберите топливо, которым вы хотите заправиться" + " :fuelpump:");
    static final String HELP_TEXT = EmojiParser.parseToUnicode(":red_circle: " + "/start - команда запускает бота. Если вы уже запустили бота, больше не используйте эту команду. \n" +
            ":red_circle: " + "/help - команда даст вам информацию о функциях, которые доступны в боте. \n" +
            ":red_circle: " + "/fuel - команда предоставляет вам выбор топлива, которое вы хотите заправить в свой автомобиль. \n" +
            ":red_circle: " + "/location - команда запросит у вас ваши координаты, чтобы определить ваше местоположение. \n" +
            ":red_circle: " + "/nearby - команда отправит вам адрес 3 ближайших заправок. \n" +
            ":red_circle: " + "/profitably - команда отправит вам адрес заправки с самым дешевым топливом");
    static final String LOCATION_TEXT = EmojiParser.parseToUnicode(":red_circle: " + "Свои координаты вы можете узнать на сайте: https://share-my-location.com/ru/my-location \n" +
            "или на своем мобильном устройстве с помощью таких приложений, как Яндекс карты или Яндекс навигатор. \n" +
            ":red_circle: " + "После того, как вы узнаете свои координаты отправьте их боту. \n" +
            ":red_circle: " +"Сначала введите широту, а затем введите долготу, например так: 60.016852 29.728543 или так: 60.016852, 29.728543");

    static final String  NEARBY_1 = EmojiParser.parseToUnicode(":red_circle: " + "Первая самая близкая заправка находится по адресу: ");
    static final String  NEARBY_2 = EmojiParser.parseToUnicode(":red_circle: " + "Вторая самая близкая заправка находится по адресу: ");
    static final String  NEARBY_3 = EmojiParser.parseToUnicode(":red_circle: " + "Третья самая близкая заправка находится по адресу: ");

    static final String  PROFITABLY_AI_92_TEXT = EmojiParser.parseToUnicode(":red_circle: " + "Заправочная станция с самым дешевым АИ-92 находится по адресу: \n");
    static final String  PROFITABLY_AI_95_TEXT = EmojiParser.parseToUnicode(":red_circle: " + "Заправочная станция с самым дешевым АИ-95 находится по адресу: \n");
    static final String  PROFITABLY_AI_98_TEXT = EmojiParser.parseToUnicode(":red_circle: " + "Заправочная станция с самым дешевым АИ-98 находится по адресу: \n");
    static final String  PROFITABLY_AI_100_TEXT = EmojiParser.parseToUnicode(":red_circle: " + "Заправочная станция с самым дешевым АИ-100 находится по адресу: \n");
    static final String  PROFITABLY_DT_TEXT = EmojiParser.parseToUnicode(":red_circle: " + "Заправочная станция с самым дешевым ДТ находится по адресу: \n");

    static final String RESPONSE_1 = EmojiParser.parseToUnicode(":ok_hand: " + "Супер, бот считал ваши координаты: \n");
    static final String RESPONSE_2 = EmojiParser.parseToUnicode(":red_circle: " + "Ваша широта: ");
    static final String RESPONSE_3 = EmojiParser.parseToUnicode(":red_circle: " + "Ваша долгота: ");
    static final String RESPONSE_4 = EmojiParser.parseToUnicode(":world_map: " + "Если вы хотите найти 3 ближайшие к вам заправки, отправьте боту команду /nearby");
    static final String RESPONSE_5 = EmojiParser.parseToUnicode(":chart_with_downwards_trend: " + "Если вы хотите найти заправку с самым дешевым топливом, отправьте боту команду /profitably, но перед этим вы должны выбрать тополиво с помощью команды /fuel");

    static final String DEFAULT_ERROR_ANSWER = EmojiParser.parseToUnicode("Вы неправильно ввели координаты или отправили боту команду, которую он не знает" + ":sweat:" + "\n" +
            ":red_circle: " + "Воспользуйтесь Menu или отправьте /help, чтобы посмотреть доступные команды" + "\n" +
            ":red_circle: " + "Координаты вводятся в формате: широта долгота - 60.016852 29.728543 или широта, долгота - 60.016852, 29.728543");

    static final String AI_92 = "АИ-92";
    static final String AI_95 = "АИ-95";
    static final String AI_98 = "АИ-98";
    static final String AI_100 = "АИ-100";
    static final String AI_50_DT = "ДТ";

}