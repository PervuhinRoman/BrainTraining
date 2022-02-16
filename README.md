# BrainTraining
*BraingTraining - приложение снованное на японской системе развития интеллекта и памяти за авторстовом [Рюты Кавашимы](https://ru.wikipedia.org/wiki/%D0%9A%D0%B0%D0%B2%D0%B0%D1%81%D0%B8%D0%BC%D0%B0,_%D0%A0%D1%8E%D1%82%D0%B0) с незначительными изменениеями.*

<br/>
Ниже приведены полезные "моменты" в коде приложения, которые можно использовать в будущем
<br/>
<br/>

## Содержание:
* [Передача данных между активностями](#1)
* [Переключение между активностями](#2)
* [Работа с seekBar](#3)
* [Работа с динамическими массивами](#4)
* [Работа с методом random() класса Math](#5)
* [Работа с `<EditText android:inputType="number" />`, чтобы исключить введение букв](#6)
* [Работа с всплывающими уведомлениями `Toast`](#7)
* [Работа с классом `Timer`](#8)
* [Форматирование строк для отображения таймера `String.format("%02d"...)`](#9)
* [Работа с `runOnUiThread`](#10)
* [Новый способ реализации `onClickListener` т.е. один listener для нескольких кнопок](#11)
* [Работа с `ConstraintLayout`](#12)
* [Работа с `ScrollView`](#13)
* [Работа с ресурсами в Java коде](#14)
* [Динамическое создание UI элементов и установка парметров разметки из Java кода](#15)
* [Изменение заголовка главной активности так, чтобы не изменилось название приложения в лаунчере `setTitle("Parameters");`](#16)
---
<a name="2"></a>
## Переключение между активностями
```Java
// Intent <INTENT_NAME> = new Intent(<ТЕКУЩИЙ КОНТЕКСТ>, <ИМЯ_КЛАССА_АКТИВНОСТИ_НА_КОТОРУЮ_ХОТИМ_ПЕРЕКЛЮЧИТЬСЯ>.class);

Intent intent = new Intent(getApplicationContext(), Training.class);
startActivity(intent);
```

<a name="1"></a>
## Передача данных между активностями
* Отправка данных
```Java
// создать Intent
// <INTENT_NAME>.putExtra("<КЛЮЧ>", <ЗНАЧЕНИЕ>);

Intent intent = new Intent(getApplicationContext(), Results.class);
intent.putExtra("expressionsCount", expressionsCount);
```
* Получение данных
```Java
// создать "получающий / принимающий" Intent: Intent = <ИМЯ_ИНТЕНТА> = getIntent();
// изъять данные: <ИМЯ_ИНТЕНТА>.getIntExtra("<КЛЮЧ>", <ЗНАЧЕНИЕ_ПО_УМОЛЧАНИЮ>);

Intent getingIntent = getIntent();
int var = getingIntent.getIntExtra("expressionsCount", 50);
```

<a name="3"></a>
## Работа с seekBar
```Java
// всё также как с обычной кнопкой, но обязательно нужно прописать реализацию 3-х методов, даже если они остануться пустыми
// переменная progress по дефолту равна 100. Т.е. весть SeekBar делится на 100 частей, что логично :)

SeekBar expressionsCount = findViewById(R.id.seekBar);

expressionsCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        txtExpressionsCount.setText(Integer.toString(progress / 2));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) { }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) { } 
});
```

<a name="4"></a>
## Работа с динамическими массивами
```Java
// https://javarush.ru/groups/posts/dinamicheskie-massivy-java

List<Integer> userAnswers = new ArrayList<>();              // массив пользовательских ответов
List<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов

// добавление элемента в массив .add(<ELEMENT>)
rightAnswers.add(111);

// получение элемента .get(<INDEX>)
rightAnswers.get(1);
```

<a name="5"></a>
## Работа с методом random() класса Math
```Java
// данная функция генерирует рандомное число в диапозоне от 1 до 9
number = (int)(Math.random() * 9 + 1);

// данная функция генерирует рандомное число в диапозоне от -20 до 20
number = (int)(Math.random() * 20 - 20);
```

<a name="6"></a>
## Работа с изменением клавиатуры пользователя
```Xml
<EditText android:inputType="number" />
```
<img src="https://github.com/PervuhinRoman/Images/blob/master/NumberKeyboardScreenshot.jpg" width="180px" height="307px" />

<a name="7"></a>
## Работа с всплывающими уведомлениями `Toast`
```Java
// Создать уведомления в нижней части экрана: Toast <ИМЯ> = Toast.makeText(<ТЕКУЩИЙ КОНТЕКСТ>, "<СООБЩЕНИЕ>", Toast.<ПРОДОЛЖИТЕЛЬНОСТЬ_ПОКАЗА_В_ВИДЕ_КОНСТАНТЫ>);
// Отобразить уведомление: <ИМЯ>.show();

Toast nfeToast = Toast.makeText(getApplicationContext(), "Enter the expressions count", Toast.LENGTH_SHORT);
nfeToast.show();
```

<a name="8"></a>
## Работа с классом `Timer`
```Java
Timer timer;
TimerTask timerTask;
Double time = 0.0;

// создание таймера и его запуск
timer = new Timer();
startTimer();

// метод запуска таймера
private void startTimer()
{
    // для работы таймера необходимо создать timerTask
    timerTask = new TimerTask()
    {
        @Override
        public void run()
        {
            // работа с главным потоком UI
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    time++;
                    txtTimer.setText(getTimerText());
                }
            });
        }

    };
    // timer.scheduleAtFixedRate(<НАШ_ТАЙМЕРТАСК>, <ЗАДЕРЖКА>, <ПЕРИОД>);
    timer.scheduleAtFixedRate(timerTask, 0, 1000);
}

// получение времени
String getTimerText() {
    int rounded = (int) Math.round(time);
    int seconds = ((rounded % 86400) % 3600) % 60;
    int minutes = ((rounded % 86400) % 3600) / 60;

    return formatTime(seconds, minutes);
}

// форматирование вывода времени
String formatTime(int seconds, int minutes) {
    return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
}

// остановка таймера
timerTask.cancel();
timer = 0.0;
```

<a name="9"></a>
## Форматирование строк для отображения таймера `String.format("%02d"..)`
```Java
// форматирование вывода времени
String formatTime(int seconds, int minutes) {
    return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
}
```

<a name="10"></a>
## Работа с `runOnUiThread`
```Java
runOnUiThread(new Runnable()
{
    @Override
    public void run()
    {
        // действия с элементом разметки в главном UI-потоке
        txtTimer.setText(getTimerText());
    }
});
```

<a name="11"></a>
## Новый способ реализации `onClickListener` т.е. один listener для нескольких кнопок
Т.к. действия, которые необходимо было выполнять с тремя разными кнопками отличались одной строчкой, удобнее всего было реализовать обработку нажатий следующим образом:
```Java
// общий listener для кнопок
View.OnClickListener buttonsClickListener = new View.OnClickListener(){
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button1:
                optimize(ans1);
                break;
            case R.id.button2:
                optimize(ans2);
                break;
            case R.id.button3:
                optimize(ans3);
                break;
        }
    }
};

// optimize() - метод реализующий функционал кнопок

// присваивание listener-а кнопкам
ans1.setOnClickListener(buttonsClickListener);
ans2.setOnClickListener(buttonsClickListener);
ans3.setOnClickListener(buttonsClickListener);
```

<a name="12"></a>
## Работа с `ConstraintLayout`
[Файл разметки одной из activity](https://github.com/PervuhinRoman/BrainTraining/blob/master/app/src/main/res/layout/activity_results.xml)

<a name="13"></a>
## Работа с `ScrollView`
[Файл разметки одной из activity](https://github.com/PervuhinRoman/BrainTraining/blob/master/app/src/main/res/layout/activity_results.xml)

<a name="14"></a>
## Работа с ресурсами в Java коде
Например устанвка цвета фона для текстового элемента `rightItem`:
```Java
// через R.java можно обращаться к любым ресурсам приложения
TextView rightItem = new TextView(Results.this);

rightItem.setBackgroundResource(R.color.okColor);

// присвоение переменной типа int значение цвета
// первый вариант
int color = Сolor.GREEN;

// второй вариант
int color = getResources().getColor(R.color.grass);

// третий вариант
color = Color.argb(0,0,255,0);
```

<a name="15"></a>
## Динамическое создание UI элементов (TextView) и установка парметров разметки из Java кода
```Java
// создание нового текстового элемента. В () контекст из которого будет создан элемент
TextView rightItem = new TextView(Results.this);

// создание параметров layout-настроек т.е. то же самое, что в xml выглядит как android:layout_params="..."
LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// настройка параметров конкретного элемента т.е. то же самое, что, например, android:gravity="center"
rightItem.setBackgroundResource(R.color.okColor);
rightItem.setGravity(Gravity.CENTER);
rightItem.setTextSize(20);

// установка созданных параметров layout-настроек
rightItem.setLayoutParams(textViewLayoutParams);

// добавление параметра == android:layout_margin="10dp"
textViewLayoutParams.setMargins(10, 10, 10, 10);
```

<a name="16"></a>
## Изменение заголовка главной активности так, чтобы не изменилось название приложения в лаунчере `setTitle("Parameters");`
```Java
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

// изменение заголовка активности так, чтобы не изменилось название приложения в лаунчере
setTitle("Parameters");
```
