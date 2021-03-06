Требуется реализовать клиент-серверное приложение. Описание:
 
1. На сервере хранится текст, разбитый построчно. Клиенты могут изменять этот текст построчно (изменить/добавить строку).
2. Клиент отправляет на сервер по TCP сообщение (не через HTTP), содержащее:
  - имя пользователя
  - строка
  - номер строки
  - операция, которую необходимо выполнить
И держит соединение, пока не получит ответ.
3. Сервер принимает сообщение и:
  - выполняет заказанную операцию
  - возвращает клиенту измененный текст
  - запоминает имя пользователя на пять минут (если клиент не обращался к серверу в течение 5 минут, пользователь удаляется из списка)
4. Сервер поддерживает возможность работы одновременно с большим числом пользователей (1000 и более)
5. Сервер подразумевает возможность подключать новые операции без рестарта приложения (например, можно подключить удаление строки)

# Описание тонкостей работы приложения.
1. Сервер<br>
1.1. Для запуска сервера необходимо выполнить команду:<br>
      java -jar server-1.0-jar-with-dependencies.jar<br>
После запуска сервер ожидает ввода от пользователя:<br>
1.1.1. Ввод "exit" - завершить выполнение сервера<br>
1.1.2. Ввод любых других символов распознается, как ввод имени файла для добавления новой операции<br>
1.2. Для добавления новой операции необходимо ввести полное имя файла. Например, C:\1\custom-operations-1.0.jar и нажать Enter.<br>
Сервер проинформирует об произведенных изменениях:<br>
10:22:16.633 [main] INFO ru.iskratel.server.Application - Loading file:/C:/1/custom-operations-1.0.jar to classpath<br>
10:22:16.641 [main] INFO ru.iskratel.server.Application - file:/C:/1/custom-operations-1.0.jar loaded<br>
1.3. Правила, описывающие одновременную работу множеством пользователей в примерах:<br>
Пользователь 1 и пользователь 2 считали один и тот же список строк:<br>
  - если пользователь 1 изменил строку 10, то пользователь 2 может добавить или удалить любую строку, или изменить любую строку кроме строки 10;<br>
  - если пользователь 1 добавил или удалил строку 10, то пользователь 2 может добавить, удалить или изменить строки, номер которых меньше 10.<br>
1.4. Изменения в тексте, которые приводят к изменению порядка строк блокируют любые изменения строк, находящихся после строки, у которой был изменен порядок другими пользователями. Для проведения операции необходимо считать весь текст с сервера, и убедившись, что изменения подходят, выполнить операцию еще раз.<br>
Изменения текста конкретной строки блокируют любые операции с этой строкой другими пользователями. Для проведения операции необходимо считать весь текст с сервера, и убедившись, что изменения подходят, выполнить операцию еще раз.<br>
Как правило, при отказе сервером в проведении операции, клиенту приходит весь текущий текст для сверки.<br>
2. Клиент<br>
2.1. Для запуска клиента необходимо выполнить команду:<br>
      java -jar client-1.0-jar-with-dependencies.jar<br>
2.2. Клиент потребует ввести имя пользователя, после чего будет находится в режиме ожидания комманд:<br>
Enter username:<br>
kovalev<br>
Enter command in format: operation[/index][/content]<br>
2.3. Формат команд: имя команды/номер строки/содержимое строки<br>
2.4. Особенности взаимодействия клиента и сервера:<br>
2.4.1. Перед любой операцией изменения строки необходимо сначала запросить с сервера весь текст командой "get"<br>
2.4.2. Примеры других команд:<br>
  - изменение строки: set/2/Hello
  - добавление строки: add/10/World
  - удаление строки: delete/15
