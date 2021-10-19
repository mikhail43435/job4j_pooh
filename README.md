# job4j_pooh JMS
[![Build Status](https://app.travis-ci.com/mikhail43435/job4j_pooh.svg?branch=master)](https://app.travis-ci.com/github/mikhail43435/job4j_pooh)
[![codecov](https://codecov.io/gh/mikhail43435/job4j_pooh/branch/master/graph/badge.svg?token=iSNwLRM7gP)](https://codecov.io/gh/mikhail43435/job4j_pooh)

## О проекте. 
Этот проект - аналог асинхронной очереди. Работает по принципу Java Message Service (JMS).\
В проекте использовано:
- Шаблон проектирования "Наблюдатель" (Observer).
- Java SE
- Concurrency
- Sockets
- Java IO
## Сборка или установка. 
Проект собирается с помощью Maven:\
`mvn install`

## Как использовать. 
Приложение запускает Socket и ждет клиентов. 
Клиенты могут быть двух типов: отправители (publisher), получатели (subscriber).
В качестве протокола используется HTTP.
Pooh имеет два режима: queue, topic.

### Queue:

Отправитель посылает сообщение с указанием очереди. 
Получатель читает первое сообщение и удаляет его из очереди.
Если приходят несколько получателей, то они читают из одной очереди.
Уникальное сообщение может быть прочитано, только одним получателем.

Пример запросов:

`POST` запрос добавит элементы в очередь `weather`.

`curl -X POST -d "temperature=18" http://localhost:9000/queue/weather`

`queue` указывает на режим "очередь".

`weather` указывает на имя очереди. 
Если очереди нет в сервисе, то создается новая.

`GET` запрос получает элементы из очереди `weather`.

`curl -X GET http://localhost:9000/queue/weather`


Ответ: temperature=`18`

### Topic:

Отправитель посылает сообщение с указанием темы.
Получатель читает первое сообщение и удаляет его из очереди.
Для каждого потребителя в режиме "topic" - уникальная очередь потребления,
в отличие от режима "queue", где очередь для всех клиентов одна и та же.

Пример запросов:

`POST /topic/weather -d "temperature=18"`

`GET /topic/weather/1`

`topic` указывает на режим темы.

`weather` имя темы, если темы нет, то создается новая.

`1` - ID клиента.

Ответ: temperature=`18`