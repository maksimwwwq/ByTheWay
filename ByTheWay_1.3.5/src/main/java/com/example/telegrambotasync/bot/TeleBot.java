package com.example.telegrambotasync.bot;

import com.example.telegrambotasync.bot.Config.Comand;
import com.example.telegrambotasync.bot.Config.UserData;
import com.example.telegrambotasync.bot.processing_request.Independent_routes;
import com.example.telegrambotasync.entity.Routes;
import com.example.telegrambotasync.util.GptRequest;
import com.example.telegrambotasync.util.Keyboards;
import com.example.telegrambotasync.util.ParserFromJsonFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

@Component
@Configuration
public class TeleBot extends TelegramLongPollingBot {
    public ConcurrentHashMap<Long, UserData> userDataMap = new ConcurrentHashMap<>();
    private final ParserFromJsonFile parserFromJsonFile;
    private final Keyboards keyboards;
    private Independent_routes independentRoutes;
    private final Map<Long, Integer> count = new ConcurrentHashMap<>();
    private final Map<Long, ArrayList<String>> caption1 = new ConcurrentHashMap<>();
    private final Map<Long, Integer> countTotal = new ConcurrentHashMap<>();
    private final Map<Long, String> NameFoto = new ConcurrentHashMap<>();
    private final Map<Long, String> Url = new ConcurrentHashMap<>();
    private final Map<Long, String> Text = new ConcurrentHashMap<>();
    private final Map<Long, String[]> getCharack = new ConcurrentHashMap<>();
    private final Map<Long,InlineKeyboardMarkup> markup = new ConcurrentHashMap<>();
    private final Map<Long,String> countMarkup = new ConcurrentHashMap<>();
    List<Routes> arrayList;

    public Integer getCount() {
        return count.get(id);
    }

    public Integer getCountTotal() {
        return countTotal.get(id);
    }

    private com.example.telegrambotasync.bot.Config.Comand Comand;
    Long id = null;

    @Autowired
    public TeleBot(GptRequest gptRequest, ParserFromJsonFile parser, Keyboards keyboards) {
        this.parserFromJsonFile = parser;
        this.keyboards = keyboards;
        this.independentRoutes = independentRoutes;
        this.Comand = new Comand();
        //   this.deckription.put(id, new ArrayList<>());
    }

    @Override
    @Async("asyncExecutor")
    public void onUpdateReceived(Update update) {
        try {
            proceesUpdateAsync(update);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Async("asyncExecutor")
    public CompletableFuture<Void> proceesUpdateAsync(Update update) throws Exception {
        String msg;

        // long id = update.getMessage().getChatId();

        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("Test");
            System.out.println(update.getMessage().getText());

            String chatId = update.getMessage().getChatId().toString();

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);

            id = update.getMessage().getChatId();
            msg = update.getMessage().getText();

            userDataMap.putIfAbsent(id, new UserData(id));

            // Доступ к данным пользователя через userData
            UserData userData = userDataMap.get(id);


            userData.setDeckription(new ArrayList<>());
            userData.setCharacteristicsKeyboard(new String[][]{
                    {"История", "Природа"},
                    {"С детьми", "Спорт"},
                    {"Архитектура", "Гастрономия"},
                    {"Искусство", "Фотография"},
                    {"Животные", "Религия"},
                    {"Фестивали", "Экстрим"}
            });
            userData.setLoketionKeyboard(new String[][]{
                    {"Минск", "Минская обл."},
                    {"Брестская обл.", "Гомельская обл."},
                    {"Могилёвская обл.", "Гродненская обл."},
                    {"Витебская обл.", "Выбрать все"}
            });

            if ("/start".equals(update.getMessage().getText())) {
                startCommandRequest(msg, sendMessage);
            }

            FavoritesRequest(msg, sendMessage);
            attractionsRequest(msg, sendMessage);
            СonnectionRequest(msg, sendMessage);
        } else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();

            System.out.println(chatId);

            EditMessageText messageText = new EditMessageText();
            messageText.setMessageId((int) messageId);
            messageText.setChatId(String.valueOf(chatId));

            System.out.println(id);
            if (citiesProcess(callbackData, messageText, chatId, userDataMap)) {
                return CompletableFuture.completedFuture(null);
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    private void attractionsRequest(String msg, SendMessage sendMessage) {
        if ("\uD83D\uDE98Самостоятельные  маршруты".equals(msg)) {
            sendMessage.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" +
                    "\n" +
                    "Выберите несколько интересов:");
            keyboards.inlineKeyboardButtonCharacteristics(sendMessage);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void FavoritesRequest(String msg, SendMessage sendMessage) {
        if ("\uD83C\uDF1FИзбранные".equals(msg)) {
            sendMessage.setText("Извините, данная функция в настоящее время недоступна. Мы активно работаем над ее разработкой\uD83D\uDC68\u200D\uD83D\uDCBB");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void СonnectionRequest(String msg, SendMessage sendMessage) {
        if ("\uD83D\uDCF2Связь".equals(msg)) {
            sendMessage.setText("Если у вас возникли вопросы относительно маршрутов или желание получить дополнительную информацию о них, пожалуйста, свяжитесь с @Uliana_Dem1. Мы рады помочь вам с выбором и предоставить необходимую информацию \uD83D\uDE0A\n" +
                    "\n" +
                    "Если у вас есть вопросы или предложения относительно работы бота, или вы столкнулись с какими-либо проблемами при его использовании, пожалуйста, свяжитесь с разработчиком бота: @maksimwwwq. Он готов ответить на ваши вопросы и учесть ваши пожелания для дальнейшего улучшения бота \uD83D\uDEE0\uFE0F \nБлагодарим за ваше обращение и за использование нашего бота!");
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void startCommandRequest(String msg, SendMessage sendMessage) {

        if ("/start".equals(msg)) {
            sendMessage.setText("Приветствует вас команда TheWay\uD83C\uDF1F:\n" +
                    "\n" +
                    "\uD83D\uDDFA Добро пожаловать в чат-бот по туристическим маршрутам Беларуси! Мы с удовольствием проведем вас по всем живописным уголкам нашей уникальной страны.\n" +
                    "\n" +
                    "✈\uFE0F Погрузитесь в настоящее приключение и откройте для себя всю красоту Беларуси!");
            keyboards.replyKeyboardMarkupbasic(sendMessage);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public void sendPhoto(Long chatId, InlineKeyboardMarkup markup, String caption, String nameFoto) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setPhoto(new InputFile(new File("img/" + nameFoto + ".jpg")));
        sendPhoto.setReplyMarkup(markup);
        sendPhoto.setCaption(caption);

        try {
            execute(sendPhoto); // Вызов метода для отправки фото
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public InlineKeyboardMarkup inlineKeyboardButtonTours(String url, String text) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();


        String[][] cities = {
                //      {"В избранное", "Подробнее"},
                {"⬅\uFE0F", text, "➡\uFE0F"},
        };

        for (String[] cityRow : cities) {
            List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
            Arrays.stream(cityRow)
                    .map(city -> {
                        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
                        inlineKeyboardButton.setText(city);
                        inlineKeyboardButton.setCallbackData(city);
                        return inlineKeyboardButton;
                    })
                    .forEach(keyboardButtonsRow::add);
            rowList.add(keyboardButtonsRow);
        }
        List<InlineKeyboardButton> buttonRow = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("В путь\uD83D\uDE97");
        button.setCallbackData("В путь");
        button.setUrl(url);
        buttonRow.add(button);
        rowList.add(buttonRow);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    public boolean citiesProcess(String callbackData, EditMessageText messageText, long id, ConcurrentHashMap<Long, UserData> userDataMap) throws Exception {
        UserData userData = userDataMap.get(id);
        //ArrayList<String> caption1;
        //String[] getCharack;
        //  String NameFoto, Url, Text;
        //InlineKeyboardMarkup markup;
        switch (callbackData) {
            case "Подобрать⌛\uFE0F":
                caption1.put(id, Independent_routes.SelectionOfIndependentRoutes(userData.getDeckription()));
                if (caption1.get(id).size() > 1) {
                    count.put(id, 1);
                    countTotal.put(id, Integer.parseInt(caption1.get(id).get(0)));
                    getCharack.put(id, caption1.get(id).get(count.get(id)).split("\n\n"));
                    NameFoto.put(id, getCharack.get(id)[0]);
                    Url.put(id, getCharack.get(id)[getCharack.get(id).length - 1]);
                    Text.put(id, String.join("\n\n", Arrays.copyOfRange(getCharack.get(id), 0, getCharack.get(id).length - 1)));

                    messageText.setText("Нейросеть подобрала для вас " + countTotal.get(id) + " подходящий маршрут!\uD83C\uDF89\n");
                    countMarkup.put(id, "стр. " + count.get(id) + "/" + countTotal.get(id));
                    markup.put(id,inlineKeyboardButtonTours(Url.get(id),countMarkup.get(id)));
                    sendPhoto(id, markup.get(id), Text.get(id), NameFoto.get(id));
                } else {
                    messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n\n" +
                            "Выберите несколько областей:\n\n*Приносим извинения, но на данный момент нет доступных маршрутов по указанным категориям\uD83D\uDE1E \nМы рекомендуем выбрать другие категории. Благодарим за понимание)");
                    messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                }
                sendEditMessageText(messageText);
                return true;
            case "⬅\uFE0F":
                  caption1.put(id,Independent_routes.SelectionOfIndependentRoutes(userData.getDeckription()));
                if (count.get(id) != null && count.get(id) > 1 && count.get(id) < caption1.get(id).size()) {
                    count.put(id, count.get(id) - 1);
                }
                countTotal.put(id, Integer.parseInt(caption1.get(id).get(0)));
                getCharack.put(id, caption1.get(id).get(count.get(id)).split("\n\n"));
                NameFoto.put(id, getCharack.get(id)[0]);
                Url.put(id, getCharack.get(id)[getCharack.get(id).length - 1]);
                Text.put(id, String.join("\n\n", Arrays.copyOfRange(getCharack.get(id), 0, getCharack.get(id).length - 1)));
                countMarkup.put(id, "стр. " + count.get(id) + "/" + countTotal.get(id));
                markup.put(id,inlineKeyboardButtonTours(Url.get(id),countMarkup.get(id)));
                sendPhoto(id, markup.get(id), Text.get(id), NameFoto.get(id));
                sendEditMessageText(messageText);

                return true;

            case "➡\uFE0F":
                  caption1.put(id,Independent_routes.SelectionOfIndependentRoutes(userData.getDeckription()));
                if (count.get(id) != null && count.get(id) > 0 && count.get(id) < caption1.get(id).size() - 1) {
                    count.put(id, count.get(id) + 1);
                }
                countTotal.put(id, Integer.parseInt(caption1.get(id).get(0)));
                getCharack.put(id, caption1.get(id).get(count.get(id)).split("\n\n"));
                NameFoto.put(id, getCharack.get(id)[0]);
                Url.put(id, getCharack.get(id)[getCharack.get(id).length - 1]);
                Text.put(id, String.join("\n\n", Arrays.copyOfRange(getCharack.get(id), 0, getCharack.get(id).length - 1)));
                countMarkup.put(id, "стр. " + count.get(id) + "/" + countTotal.get(id));
                markup.put(id,inlineKeyboardButtonTours(Url.get(id),countMarkup.get(id)));
                sendPhoto(id, markup.get(id), Text.get(id), NameFoto.get(id));
                sendEditMessageText(messageText);
                return true;
            case "Подробнее":
                messageText.setText("00000000000");
                sendEditMessageText(messageText);
                return true;

            case "⬅\uFE0FНазад":
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;
            case "Далее":
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                System.out.println(userData.getDeckription());
                if (userData.getDeckription().isEmpty()) {
                    messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                    messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Пожалуйста, выберите один или несколько интересов:");
                    sendEditMessageText(messageText);
                } else {
                    messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                    messageText.setText("\n" +
                            "\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" +
                            "\n" +
                            "Выберите несколько областей:");
                    sendEditMessageText(messageText);
                }
                return true;
            case "Минск":
                userData.setLoketionCheck("Минск");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Минск":
                userData.setLoketionCheck("Минск");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "Минская обл.":
                userData.setLoketionCheck("Минская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Минская обл.":
                userData.setLoketionCheck("Минская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "Брестская обл.":
                userData.setLoketionCheck("Брестская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "Гомельская обл.":
                userData.setLoketionCheck("Гомельская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "Могилёвская обл.":
                userData.setLoketionCheck("Могилёвская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "Гродненская обл.":
                userData.setLoketionCheck("Гродненская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "Витебская обл.":
                userData.setLoketionCheck("Витебская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "Выбрать все":
                userData.setLoketionCheck("Выбрать все");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Брестская обл.":
                userData.setLoketionCheck("Брестская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Гомельская обл.":
                userData.setLoketionCheck("Гомельская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Могилёвская обл.":
                userData.setLoketionCheck("Могилёвская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Гродненская обл.":
                userData.setLoketionCheck("Гродненская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Витебская обл.":
                userData.setLoketionCheck("Витебская обл.");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "✅Выбрать все":
                userData.setLoketionCheck("Выбрать все");
                userData.setLoketionKeyboard(Comand.Сhecklokation(userData.getLoketionCheck(), userData.getDeckription(), userData.getLoketionKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonLokationUpdate(userData.getLoketionKeyboard()));
                messageText.setText("\uD83E\uDD16Куда бы вы хотели отправится в путешествие в этот раз?\n" + "\n" + "Выберите несколько областей:");
                sendEditMessageText(messageText);
                return true;
            case "История":
                userData.setCharacteristicsCheck("История");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;
            case "✅История":
                userData.setCharacteristicsCheck("История");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Природа":
                userData.setCharacteristicsCheck("Природа");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "С детьми":
                userData.setCharacteristicsCheck("С детьми");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Спорт":
                userData.setCharacteristicsCheck("Спорт");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Архитектура":
                userData.setCharacteristicsCheck("Архитектура");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Гастрономия":
                userData.setCharacteristicsCheck("Гастрономия");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Искусство":
                userData.setCharacteristicsCheck("Искусство");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Фотография":
                userData.setCharacteristicsCheck("Фотография");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Животные":
                userData.setCharacteristicsCheck("Животные");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Религия":
                userData.setCharacteristicsCheck("Религия");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Фестивали":
                userData.setCharacteristicsCheck("Фестивали");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "Экстрим":
                userData.setCharacteristicsCheck("Экстрим");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;
            case "✅Природа":
                userData.setCharacteristicsCheck("Природа");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅С детьми":
                userData.setCharacteristicsCheck("С детьми");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Спорт":
                userData.setCharacteristicsCheck("Спорт");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Архитектура":
                userData.setCharacteristicsCheck("Архитектура");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Гастрономия":
                userData.setCharacteristicsCheck("Гастрономия");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Искусство":
                userData.setCharacteristicsCheck("Искусство");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Фотография":
                userData.setCharacteristicsCheck("Фотография");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Животные":
                userData.setCharacteristicsCheck("Животные");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Религия":
                userData.setCharacteristicsCheck("Религия");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Фестивали":
                userData.setCharacteristicsCheck("Фестивали");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;

            case "✅Экстрим":
                userData.setCharacteristicsCheck("Экстрим");
                System.out.println(userData.getCharacteristicsCheck());
                userData.setCharacteristicsKeyboard(Comand.СheckCharacteristic(userData.getCharacteristicsCheck(), userData.getDeckription(), userData.getCharacteristicsKeyboard()));
                System.out.println(Arrays.deepToString(userData.getCharacteristicsKeyboard()));
                messageText.setReplyMarkup(keyboards.inlineKeyboardButtonCharacteristicsUpdate(userData.getCharacteristicsKeyboard()));
                messageText.setText("\uD83E\uDD16Сейчас нейросеть поможет подобрать вам подходящий маршрут!\n" + "\n" + "Выберите несколько интересов:");
                sendEditMessageText(messageText);
                return true;
            default:
                // Обработка, если ни один из case не соответствует
                return false;
        }


    }

    @Async
    public void sendEditMessageText(EditMessageText messageText) {
        try {
            executeAsync(messageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Async
    public void sendMessageText(long chatId, String text, boolean modeReply, int replyToMessageId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);

        if (modeReply)
            sendMessage.setReplyToMessageId(replyToMessageId);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
        }

    }


    @Override
    public String getBotUsername() {
        // Возвращает имя вашего бота
        //return "@belarustoursquestsBot";
        return "@TheWayByBot";
    }

    @Override
    public String getBotToken() {
        // Возвращает токен вашего бота
        return "6888336081:AAEg1B_b9_iDKQEdvNdoOOPAfUPbR0_vtWw";
     //   return "6692247773:AAHi6jGFe-4fy1WJ0oN4Y03u_E6J0J5cAxA";
    }

    @Bean(name = "asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(15);
        executor.setMaxPoolSize(30);
        executor.setQueueCapacity(30);
        executor.setThreadNamePrefix("AsyncThread-");
        executor.initialize();
        return executor;
    }

}
