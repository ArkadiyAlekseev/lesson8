package ru.geekbrains.J1lesson7.J1Homework7;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame {//отдельное окно с настройками SettingsWindow

    private static final int WINDOW_WIDTH = 350;// ширина окна
    private static final int WINDOW_HEIGHT = 230;// высота окна
    private static final int MIN_WIN_LENGTH = 3;// минимальная выйгрышная длина победы
    private static final int MIN_FIELD_SIZE = 3;//минимальный размер поля игры
    private static final int MAX_FIELD_SIZE = 10;// максимальный размер поля игры
    private static final String FIELD_SIZE_PREFIX = "Field size is: ";// описание размера поля
    private static final String WIN_LENGTH_PREFIX = "Win length is: ";// описание длины линии победы

    private CreateWindow gameWindow;
    //передаем объект класса CreateWindow, чтобы SettingsWindow концентрировался вокруг него
    // блягодаря ему выясняем свое текущее месторасположение и передачи сигнала о старте игры
    private JRadioButton humVSAI;
    // JRadioButton можно выбрать только 1 из представленных вариантов (например, режим игры)
    private JRadioButton humVShum;
    private JSlider slideWinLen;
    // JSlider можно выбрать значение между минимумом и максимумом
    // (например размер поля и размер выйгрышной послежовательности)
    private JSlider slideFieldSize;

    SettingsWindow(CreateWindow gameWindow) {// конструктор
        this.gameWindow = gameWindow;
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);// метод определения ширины и высоты окна
        Rectangle gameWindowBounds = gameWindow.getBounds();
        // Rectangle  - класс, описывающий прямоугольники, обращается к
        // объекту gameWindow класса CreateWindow для получения границ прямоугольника
        // используя библиотечный метод .getBounds()
        int posX = (int) gameWindowBounds.getCenterX() - WINDOW_WIDTH / 2;
        // находим центр прямоугольника по длине из параметров объекта gameWindow класса CreateWindow
        // при помощи метода .getCenterX() и отнимаем половину ширины окна класса SettingsWindow
        // приводим к целочисленному, потому что метод установки позиции окна setLocation принимает только инт
        int posY = (int) gameWindowBounds.getCenterY() - WINDOW_HEIGHT / 2;// то же самое по высоте
        setLocation(posX, posY);// указываем позицию по высоте и ширине
        setResizable(false);// нельзя менять размер
        setTitle("Создание новой игры");// название окна
        //setVisible(true);// сделать окно видимым так не получится, потому что оно будет спрятано за окном CreateWindow с самого начала
        // поэтому делаем видимым из окна CreateWindow при помощи кнопки Старта
        // при закрытии окна из-за отсутсвия метода setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE) - программа не закрывается
        //  окно просто становится невидимым и перезапускается
        setLayout(new GridLayout(10, 1));//метод библиотеки Jframe - вызвает комплектовщик 10 строк в одну колонку внутри окна при помощи компановщика GridLayout
        addGameModeControls();//вытащили код в отдельный метод клавишами ctrl alt m
        addFieldControls();
        JButton btnStart = new JButton("Старт игры");//кнопка принятия настроек
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applySettings();
            }

        });
        add(btnStart);
    }

    private void addGameModeControls() {
        add (new JLabel("Выбери режим игры"));//добавляет надпись
        humVSAI = new JRadioButton("Human vs. AI");//ставим точку или нет
        humVShum = new JRadioButton("Human vs. Human");
        ButtonGroup gameMode = new ButtonGroup();// создаем группу кнопок
        gameMode.add(humVSAI);//всатвляем компонент, группа ButtonGroup  остается невидимой
        gameMode.add(humVShum);//
        // благодаря тому что JRadioButton попали в группу ButtonGroup точка будет гулять между ними теперь
        humVSAI.setSelected(true);
        // установить флажок на "Human vs. AI" при помощи метода .setSelected()
        // это для игроков сделано, чтобы он понимал, что можно выбрать каждый вариант
        // при отсутствии setSelected точка появится при первом нажатии

        add(humVSAI);//всатвляем кнопку
        add(humVShum);
    }
    //
    //}

   private void addFieldControls() {
        JLabel lblFieldSize = new JLabel(FIELD_SIZE_PREFIX + MIN_FIELD_SIZE);
        // чтобы видеть какое установлено значение
        JLabel lblWinLength = new JLabel(WIN_LENGTH_PREFIX + MIN_WIN_LENGTH);
       // чтобы видеть какое установлено значение
        slideFieldSize = new JSlider(MIN_FIELD_SIZE, MAX_FIELD_SIZE, MIN_FIELD_SIZE);
        // если бы он был пустой, то 0 (минимальное значение),100 (максимальное значение),50(текущее положение)
        slideWinLen = new JSlider(MIN_WIN_LENGTH, MIN_FIELD_SIZE, MIN_WIN_LENGTH);
        // максмальная линия победы равна минимальному полю, положение слайдера на минимальной линнии победы
        slideFieldSize.addChangeListener(new ChangeListener() {// слушатель изменений положения слайдера
            @Override
            public void stateChanged(ChangeEvent e) {// метод состояние изменилось
                int currentValue = slideFieldSize.getValue();// присваивание текущего значения slideFieldSize
                lblFieldSize.setText(FIELD_SIZE_PREFIX + currentValue);//изменение тескста
                slideWinLen.setMaximum(currentValue);// меняем максимум слайдера с выйгрышной длинной
            }
        });
        slideWinLen.addChangeListener(new ChangeListener() {// слушатель изменений положения слайдера
            @Override
            public void stateChanged(ChangeEvent e) {
                lblWinLength.setText(WIN_LENGTH_PREFIX + slideWinLen.getValue());//изменение текста
            }
        });

        add (new JLabel("Выбери размер поля"));// добавляет надпись
        add(lblFieldSize);// размер поля 3
        add(slideFieldSize);// ползунок передвижной
        add (new JLabel("Выбери длину линии победы"));// добавляет надпись
        add(lblWinLength);// размер линии победы 3
        add(slideWinLen);// ползунок передвижной
    }

    void applySettings() {
        int mode;
        if (humVSAI.isSelected())// если выбран режим humVSAI
            mode = DrawPanel.MODE_AI_HUM;// константа берется из DrawPanel
        else if (humVShum.isSelected())// если выбран режим humVShum
            mode = DrawPanel.MODE_HUM_HUM;// константа берется из DrawPanel
        else
            throw new RuntimeException("Unexpected game mode");// проверка: если вддруг появится новый   режим - программа выдаст "Unexpected game mode", чтобы мы не забыли его и сюда включить

        int fieldSize = slideFieldSize.getValue();// какое поле установили
        int winLen = slideWinLen.getValue();//какую длину установили

        gameWindow.startNewGame(mode, fieldSize, fieldSize, winLen);//вызывает у объекта gameWindow класса CreateWindow метод .startNewGame
        setVisible(false);
    }
}
