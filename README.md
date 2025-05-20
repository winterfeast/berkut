# Telegram Message Relay API

Проект представляет собой backend-сервис на Java + Spring Boot для приема сообщений от пользователей и отправки их в Telegram-бота.

---

## Функциональность

- ✅ Регистрация пользователя через REST API
- ✅ Авторизация с JWT-токеном
- ✅ Генерация Telegram-токена и привязка Telegram-аккаунта к пользователю
- ✅ Отправка сообщений через API и пересылка их в Telegram-чат
- ✅ Получение истории отправленных сообщений
- ✅ Хранение пользователей и сообщений в базе данных
- ✅ Unit тесты

---

## Технологии

| Категория           | Технологии                   |
|---------------------|------------------------------|
| Язык                | Java 17                      |
| Backend Framework   | Spring Boot 3                |
| Безопасность        | Spring Security + JWT        |
| Работа с Telegram   | TelegramBots 6.9.7.1         |
| Хранение данных     | PostgreSQL + Spring Data JPA |
| Тестирование        | JUnit 5, Mockito,            |
| Сборка              | Gradle                       |

---

## Требования

- Java 17+
- PostgreSQL 14+
- Telegram бот, созданный через [@BotFather](https://t.me/BotFather)

---

## Авторизация

- Используется JWT-токен
- Эндпоинты `/api/auth/**` доступны без авторизации
- Все остальные требуют `Authorization: Bearer <токен>`

---
## API Эндпоинты

### Регистрация

```
POST /api/auth/register
{
  "email": "user@mail.com",
  "password": "123456"
}
```

### Авторизация

```
POST /api/auth/login
{
  "email": "user@mail.com",
  "password": "123456"
}
```

### Генерация Telegram токена

```
POST /api/user/telegram-token
Authorization: Bearer <JWT>
```

### Отправка сообщения

```
POST /api/message
Authorization: Bearer <JWT>
{
  "text": "Привет, бот!"
}
```

### Получение истории сообщений

```
GET /api/message
Authorization: Bearer <JWT>
```

---

## TelegramBot логика

- Пользователь генерирует токен через API
- Отправляет этот токен в Telegram боту
- Бот сохраняет `chatId` и привязывает его к пользователю
- Все сообщения через API будут дублироваться в Telegram чат

---

## Тестирование

```bash
./gradlew test
```

Присутствуют:

- Unit-тесты сервисов (UserService, MessageService)
- Используется `ArgumentCaptor` и `argThat` для проверки аргументов

---

## TelegramBot настройка

1. Создайте бота через [@BotFather](https://t.me/BotFather)
2. Получите токен
3. Установите его в `application.yml` или как переменную окружения:
   ```bash
   export TELEGRAM_BOT_TOKEN=123456:ABC...
   ```
4. Убедитесь, что Webhook не установлен:
   ```
   https://api.telegram.org/bot<TOKEN>/deleteWebhook
   ```

---

## Запуск

```bash
./gradlew bootRun
```

---

## Пример `.env` файла

```
JWT_SECRET=supersecurekeyyoushouldchangethis
TELEGRAM_BOT_TOKEN=123456:ABC...
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/telegramdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password
```