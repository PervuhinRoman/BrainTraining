# BrainTraining

### Содержание:
* Передача данных между активностями
* Переключение между активностями
* Работа с seekBar
* работа с динамическими массивами
* работа с модулем random()
* работа с `<EditText android:inputType="number" />`, чтобы исключить введение букв
* работа с всплывающими уведомлениями `Toast`
* работа с классом `Timer`
* форматирование строк для отображения таймера `String.format("%02d"...)`
* Работа с `runOnUiThread`
* новый способ реализации `onClickListener` т.е. один listener для нескольких кнопок
* работа с `ConstraintLayout`
* работа с `ScrollView`
* работа с ресурсами в Java коде
* динамическое создание UI элементов (TextView) и установка парметров разметки из Java кода
* изменение заголовка главной активности так, чтобы не изменилось название приложения в лаунчере `setTitle("Parameters");`

---

### Переключение между активностями
```Java
// Intent <INTENT_NAME> = new Intent(<ТЕКУЩИЙ КОНТЕКСТ>, <ИМЯ_КЛАССА_АКТИВНОСТИ_НА_КОТОРУЮ_ХОТИМ_ПЕРЕКЛЮЧИТЬСЯ>.class);

Intent intent = new Intent(getApplicationContext(), Training.class);
startActivity(intent);
```
### Передача данных между активностями
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
### Работа с seekBar
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
### Работа с динамическими массивами
```Java
// https://javarush.ru/groups/posts/dinamicheskie-massivy-java

List<Integer> userAnswers = new ArrayList<>();              // массив пользовательских ответов
List<Integer> rightAnswers = new ArrayList<>();             // массив правильных ответов

// добавление элемента в массив .add(<ELEMENT>)
rightAnswers.add(111);

// получение элемента .get(<INDEX>)
rightAnswers.get(1);
```
### Работа с методом random() класса Math
```Java
// данная функция генерирует рандомное число в диапозоне от 1 до 9
number = (int)(Math.random() * 9 + 1);

// данная функция генерирует рандомное число в диапозоне от -20 до 20
number = (int)(Math.random() * 20 - 20);
```
### Работа с изменением клавиатуры пользователя
```Xml
<EditText android:inputType="number" />
```
<img src="https://github.com/PervuhinRoman/Images/blob/master/NumberKeyboardScreenshot.jpg" width="180px" height="307px" />

### Работа с всплывающими уведомлениями `Toast`
```Java
// Создать уведомления в нижней части экрана: Toast <ИМЯ> = Toast.makeText(<ТЕКУЩИЙ КОНТЕКСТ>, "<СООБЩЕНИЕ>", Toast.<ПРОДОЛЖИТЕЛЬНОСТЬ_ПОКАЗА_В_ВИДЕ_КОНСТАНТЫ>);
// Отобразить уведомление: <ИМЯ>.show();

Toast nfeToast = Toast.makeText(getApplicationContext(), "Enter the expressions count", Toast.LENGTH_SHORT);
nfeToast.show();
```
### Работа с классом `Timer`
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
### Форматирование строк для отображения таймера `String.format("%02d"..)`
```Java
// форматирование вывода времени
String formatTime(int seconds, int minutes) {
    return String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
}
```
### Работа с `runOnUiThread`
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
### Новый способ реализации `onClickListener` т.е. один listener для нескольких кнопок
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
### Работа с `ConstraintLayout`
[Файл разметки одной из activity](https://github.com/PervuhinRoman/BrainTraining/blob/master/app/src/main/res/layout/activity_results.xml)
### Работа с `ScrollView`
[Файл разметки одной из activity](https://github.com/PervuhinRoman/BrainTraining/blob/master/app/src/main/res/layout/activity_results.xml)
### Работа с ресурсами в Java коде
Например устанвка цвета фона для текстового элемента `rightItem`:
```Java
// через R.java можно обращаться к любым ресурсам приложения

rightItem.setBackgroundResource(R.color.okColor);
```
### Динамическое создание UI элементов (TextView) и установка парметров разметки из Java кода
```Java
// создание нового текстового элемента в () контекст из которого будет создан элемент
TextView rightItem = new TextView(Results.this);

// создание параметров layout-настроек т.е. то же самое, что в `xml` выглядит как `android:layout_params="..."`
LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

// настройка параметров конкретного элемента т.е. то же самое, что, например, `android:gravity="center"`
rightItem.setBackgroundResource(R.color.okColor);
rightItem.setGravity(Gravity.CENTER);
rightItem.setTextSize(20);

// установка созданных параметров layout-настроек
rightItem.setLayoutParams(textViewLayoutParams);

// добавление параметра == `android:layout_margin="10dp"`
textViewLayoutParams.setMargins(10, 10, 10, 10);
```
### Изменение заголовка главной активности так, чтобы не изменилось название приложения в лаунчере `setTitle("Parameters");`
```Java
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);

// изменение заголовка активности так, чтобы не изменилось название приложения в лаунчере
setTitle("Parameters");
```
