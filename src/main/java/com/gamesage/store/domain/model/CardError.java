package com.gamesage.store.domain.model;

import java.util.List;

public class CardError {

    public static final List<CardError> cardErrors = List.of(
            new CardError(5001, "Refer To Card Issuer", "Отказ эмитента проводить онлайн-операцию", "Свяжитесь с вашим банком или воспользуйтесь другой картой\n"),
            new CardError(5003, "Invalid Merchant", "Отказ эмитента проводить онлайн-операцию", "Свяжитесь с вашим банком или воспользуйтесь другой картой\n"),
            new CardError(5004, "Pick Up Card", "Карта потеряна", "Свяжитесь с вашим банком или воспользуйтесь другой картой\n"),
            new CardError(5005, "Do Not Honor", "Отказ эмитента без объяснения причин", "Свяжитесь с вашим банком или воспользуйтесь другой картой\n"),
            new CardError(5006, "Error", "Отказ сети проводить операцию или неправильный CVV-код", "Проверьте правильность введенных данных карты или воспользуйтесь другой картой\n"),
            new CardError(5007, "Pick Up Card Special Conditions", "Карта потеряна", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5012, "Invalid Transaction", "Карта не предназначена для онлайн-платежей", "Воспользуйтесь другой картой или свяжитесь с банком, выпустившим карту"),
            new CardError(5013, "Amount Error", "Слишком маленькая или слишком большая сумма операции", "Проверьте правильность введенных данных карты или воспользуйтесь другой картой\n"),
            new CardError(5014, "Invalid Card Number", "Некорректный номер карты", "Проверьте правильность введенных данных карты или воспользуйтесь другой картой\n"),
            new CardError(5015, "No Such Issuer", "Эмитент не найден", "Проверьте правильность введенных данных карты или воспользуйтесь другой картой\n"),
            new CardError(5019, "Transaction Error", "Отказ эмитента без объяснения причин" +
                    "( - неверно указан код CVV на картах Mastercard; " +
                    " - внутренние ограничения банка, выпустившего карту; " +
                    " - карта заблокирована или еще не активирована; " +
                    " - на карте не включены интернет-платежи или не подключен 3DS.)", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5030, "Format Error", "Ошибка на стороне эквайера — неверно сформирована транзакция", "Повторите попытку позже"),
            new CardError(5031, "Bank Not Supported By Switch", "Неизвестный эмитент карты", "Воспользуйтесь другой картой"),
            new CardError(5033, "Expired Card Pickup", "Истёк срок утери карты", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5034, "Suspected Fraud", "Отказ эмитента — подозрение на мошенничество", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5036, "Restricted Card", "Карта не предназначена для платежей", "Платежи для этой карты запрещены. Попробуйте другую карту"),
            new CardError(5041, "Lost Card", "Карта потеряна", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5043, "Stolen Card", "Карта украдена", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5054, "Insufficient Funds", "Недостаточно средств", "Недостаточно средств на карте"),
            new CardError(5057, "Transaction Not Permitted", "Ограничение на карте", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5059, "Suspected Fraud Decline", "Транзакция была отклонена банком по подозрению в мошенничестве", "Свяжитесь с вашим банком или воспользуйтесь другой картой"),
            new CardError(5063, "Security Violation", "Карта заблокирована из-за нарушений безопасности", "Воспользуйтесь другой картой"),
            new CardError(5065, "Exceed Withdrawal Frequency", "Превышен лимит операций по карте", "Свяжитесь с вашим банком или воспользуйтесь другой картой")
    );
    private final Integer code;
    private final String name;
    private final String reason;
    private final String cardErrorMessage;

    public CardError(Integer code, String name, String reason, String cardErrorMessage) {
        this.code = code;
        this.name = name;
        this.reason = reason;
        this.cardErrorMessage = cardErrorMessage;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getCardErrorMessage() {
        return cardErrorMessage;
    }
}

