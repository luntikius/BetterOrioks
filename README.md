<h1>
  Better Orioks
</h1>

<ul>
  <li>
    <a href="#p1">О приложении</a>
  </li>
  
  <li>
    <a href="#p2">Как работает?</a>
  </li>
  
  <li>
    <a href="#p3">Возможности</a>
  </li>
  
  <li>
    <a href="#p4">Куда уходят мои данные?</a>
  </li>
  
  <li>
    <a href="#p5">Будет ли на IOS?</a>
  </li>
  
  <li>
    <a href="#p6">Контакты</a>
  </li>
</ul>

<h2 id="p1">
  О приложении
</h2>
<p>
  Приложение-компаньон для мониторинга успеваемости и расписания студентами МИЭТа. Пользуясь приложением, пользователь может получить доступ к большей части важной информации, представленной на сайте orioks.miet.ru, в более удобном мобильном формате.
</p>

<h2 id="p2">
  Как работает?
</h2>
<h3>Вход</h3>
<p>
  Приложение "обменивает" логин и пароль, введенные пользователем, на специальный токен через HTTP протокол. Во всех последующих запросах к серверу ОРИОКСа используется исключительно полученный токен. Такой подход позволяет гарантировать безопасность данных пользователя. Важно заметить, что приложение обменивается данными, хранящимися в памяти устройства, только с серверами ОРИОКСа, что не позволяет получить данные пользователя третьим лицам (даже создателям приложения). 
</p>

<h3>Расписание, оценки, информация о пользователе</h3>
<p>
  Приложение получает информацию с официального сайта МИЭТ (расписание) через открытый API ОРИОКСа (номер группы / дата начала семеста) и общаясь напрямую с сайтом orioks.miet.ru (все остальное). Данный подход можно считать практичкески оптимальным, он позволяет показывать пользователю правильное расписание и актуальные оценки сразу за весь год, а не только за прошедшие недели.
</p>

<h3>Уведомления</h3>
<p>
  При включении функции уведомлений приложение с определенной частотой делает запрос к серверу ОРИОКСА и сравнивает данные, полученные в ответе, с данными, которые сохранены в памяти устройства. Информация о различиях между информацией отображается пользователю в виде уведомлений.
</p>


<h2 id="p3">
  Возможности
</h2>
<p>
  Приложение предоставляет следующую информацию: 
<ul>
  <li>
    Расписание
  </li>
  
  <li>
    Успеваемость студента
  </li>
  
  <li>
    Ресурсы по дисциплинам
  </li>
  
  <li>
    Задолженности студента
  </li>
  
  <li>
    Информация о студенте
  </li>
  
  <li>
    Новости
  </li>
</ul>
</p>

<p>
  Также приложение может присылать уведомления об изменении оценок.
</p>

<h3>
  Просмотр успеваемости студента
</h3>
<p>
  Приложение позволяет смотреть текущие баллы по дисциплинам и по каждому из контрольных мероприятий.
 </p>
 <p>
  <img src="https://github.com/luntikius/BetterOrioks/blob/master/img/academic_performance_screen.png?raw=true" height=450px>
</p>

<h3>
  Получение уведомлений об изменении оценок
</h3>
<p>
  Есть возможность включить уведомления об изменении баллов по дисциплинам.
 </p>
 <p>
  <img src="https://github.com/luntikius/BetterOrioks/blob/master/img/notifications_toggle.png?raw=true" height=75px>
</p>

<h3>
  Просмотр ресурсов
</h3>
<p>
  В приложении можно посмотреть ресурсы ко всей дисциплине и к конкретным контрольным мероприятиям.
 </p>
 <p>
  <img src="https://github.com/luntikius/BetterOrioks/blob/master/img/resources_screen.png?raw=true" height=350px>
</p>

<h3>
  Просмотр расписания текущего семестра
</h3>
<p>
  Доступен просмотр расписания на конкретный день.
</p>
 <p>
  <img src="https://github.com/luntikius/BetterOrioks/blob/master/img/schedule_screen.png?raw=true" height=600px>
</p>


<h2 id="p4">
  Куда уходят мои данные?
</h2>
<p>
  Все данные остаются в памяти устройства и используются только для общения с сервером ОРИОКСа. Третьим лицам данные не передаются.
</p>

<h2 id="p5">
  Будет ли на IOS?
</h2>
<p>
  <details>
    <summary>
      Развернуть
    </summary>
    <img src="https://github.com/luntikius/BetterOrioks/blob/a302491f1184e63b91401d85bc61e3e1cf0e49ab/img/DOG_NO.png?raw=true" height=350px>
    <p>
      Сам я этого делать не буду, но если кто-то хочет портировать на IOS, буду рад посодействовать.
    </p>
  </details>
</p>

<h2 id="p6">
  Контакты
</h2>
<p>
  Узнать новости приложения и оставить свой отзыв можно в
  <a href="https://t.me/+YQD5-csbrqk4ZjEy" target="_blank">
    Телеграм-канале
  </a>
  <br>
  Почта для связи: 
  <a href="mailto:BetterOrioks@yandex.ru">BetterOrioks@yandex.ru</a>
  <br>
  Моя страничка в 
  <a href="https://vk.com/luntikius">VK</a>
</p>

