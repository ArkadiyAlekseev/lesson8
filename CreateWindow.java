package ru.geekbrains.J1lesson7.J1Homework7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CreateWindow extends JFrame {//Gamewindow
    // задаем константы размера окна
    private static final int WINDOW_WIDTH = 507;// ширина
    private static final int WINDOW_HEIGHT = 555;// высота
    private static final int WINDOW_POS_X = 650; // позиция окна c верхнего левого угла окна вправо (X)
    private static final int WINDOW_POS_Y = 250;// позиция окна c верхнего левого угла окна вниз (Y)
    /**
     * создать класс для создания окна (название любое)
     * наследник класса Jframe (библиотека javax.swing.) - импортирована из пакета javax.swing.
     */
    private DrawPanel map;// вызов панели
    private SettingsWindow settingWindow; // вызов параметров игры
    CreateWindow() {//конструктор
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// метод, вызываемых для закрытия программы при закрытии окна
        /**
         * во фреймворке swing при закрытии окна оно не закрывается, а становится невидимым
         * (программа продолжает работать), потому используется метод setDefaultCloseOperation();
         * для того, чтобы выбрать, что делает программа - ктр кликаем на sDCO и видим - 4 константы
         * на входе в качестве параметра и выбираем нужную (сейчас выбран выход при закрытии окна)
         */
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);// метод определения ширины и высоты окна
        setLocation(WINDOW_POS_X,WINDOW_POS_Y);// метод определения положения окна
        setResizable(false);//если не хотим, чтобы пользователь менял размер окна (если метода нет, можно менять как хочешь)
        setTitle("Крестики-нолики");//текст в титуле окна
        JButton btnStart = new JButton("Начать новую игру");//кнопка старта c названием
        btnStart.addActionListener(new ActionListener() {// добавить на кнопку старта слушатель некоего события
            @Override
            public void actionPerformed(ActionEvent e) {// метод - действие совершено
            // в кодовом блоке действие которое нужно совершить
                settingWindow.setVisible(true);// сделать окно системных настроек видимым нажатием кнопки Старт
            }
        });
        JButton btnStop = new JButton("Выход");
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // завершение работы всей программы включая подпрограммы
                // нажатием кнопки Выход
                // 0 означает программа завершилась не по ошибке, а по решению пользователя (нормальное завершение)
            }
        });
        //add(btnStart, BorderLayout.NORTH);//добавляем кнопку btnStart в окно при помощи метода add (без параметров кнопка станет на все окно с названием посередине)
        // BorderLayout - один из компановщиков , позволяющих определить место раположения кнопки и ее размер.
        // компановщики удобны так как при изменении размеров окна - не нужно думать об изменении кнопок
        //.NORTH - кнопка сверху, EAST
        /** компановщики за нас думают какие координаты и размеры
         * несколько компановщиков:
         * BoxLayout - в своем порядке (скидываются компоненты в 1 бокс, их можно позиционировать относительно друг друга гуглить
         * BorderLayout (по ориентиру на все окно - верхнее поле, нижнее поле, центральное поле,  правое поле, левое поле)
         * GridLayout располагает кнопки в виде сетки
         * FlowLayout (кнопки иджут подряд , на конце окна начинается новая линия и сначала, размер кнопок минимальног размера и зависит от их титула)
         */
        //add(btnStop, BorderLayout.SOUTH);//без параметров Выход ляжет на Начать игру(не увидим на экране) и растянется на все окно
        JPanel panelBottom = new JPanel();
        /**
         * компонент панель - абстрактный квадрат (прямоугольник)
         * - на котором можно располагать компоненты и компановщики
         * панели можно вкладывать друг в друга (типа верстки)
         */
        panelBottom.setLayout(new GridLayout(1,2));// внутри панели при помощи компановщика устанавливаем две кнопки в одну строку
        panelBottom.add(btnStart);// добавляем в панель кнопку
        panelBottom.add(btnStop);// добавляем в панель кнопку

        map = new DrawPanel();
        settingWindow = new SettingsWindow(this);
        // передаем тот экземпляр CreateWindow, который сейчас создается в классе settingWindow
        add(panelBottom, BorderLayout.SOUTH);// добавляем панель при помощи компановщика BorderLayout.SOUTH в нижнию часть окна
        add (map, BorderLayout.CENTER); // добавляем панель map по центру
        setVisible(true); // сделать окно видимым

    }
    void startNewGame( int mode, int fieldSizeX, int fieldSizeY, int lineVictory){
        // метод принимает от settingWindow параметры и передает их в map (DrawPanel)
        map.startNewGame(mode, fieldSizeX, fieldSizeY, lineVictory);
        // mode режим игры, далее координаты и линия победы
    }
}
