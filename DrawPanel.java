package ru.geekbrains.J1lesson7.J1Homework7;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class DrawPanel extends JPanel {//панель, на которой рисуем // Map
    public static final int MODE_HUM_HUM = 0;// константа для проверки режима человек против человека
    public static final int MODE_AI_HUM = 1;// константа для проверки режима человек против компьютера

    private static final int DOT_EMPTY = 0;
    private static final int DOT_HUMAN = 1;
    private static final int DOT_AI = 2;
    private static final int DOT_PADDING = 5;

    private int stateGameOver;
    private static final int STATE_DRAW = 0;
    private static final int STATE_WIN_HUMAN = 1;
    private static final int STATE_WIN_AI = 2;

    private static final String MSG_WIN_HUMAN = "Победил игрок!";
    private static final String MSG_WIN_AI = "Победил компьютер!";
    private static final String MSG_DRAW = "Ничья!";

    private static final Random RANDOM = new Random();

    private int[][] field;//поле
    private int fieldSizeX;//размер поля по Х
    private int fieldSizeY;// размер поля по Y
    private int lineVictory;// выйгрышная длина
    private int cellWidth;// ширина ячейки
    private int cellHeight;// высота ячейки
    private boolean initialized;//инициализирована игра или нет
    // отрисовка компонентов после начала игры
    private boolean isGameOver;// окончени ли игра

    DrawPanel(){//конструктор
        setBackground(Color.PINK);// цвет фона панель
    }
    void startNewGame( int mode, int fieldSizeX, int fieldSizeY, int lineVictory){
        this.fieldSizeY = fieldSizeY;
        this.fieldSizeX = fieldSizeX;
        this.lineVictory = lineVictory;
        this.field = new int[fieldSizeY][fieldSizeX];
        this.initialized = true;
        this.isGameOver = false;

        // mode режим игры, далее координаты и линия победы
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {// Released работает по опусканию кнопки , а не по нажатию
                // MouseEvent хранит события мышки координаты, клики, движения и т.д.
                super.mouseReleased(e);//по каждому клику
                update(e);// обновляется поле игры
                repaint();//перерисовывается
            }
        });
        repaint();// нужен для исполнения метода paintComponent
    }
    private void update(MouseEvent e) {
        if (isGameOver) return;// если игра окончена, поле не обнавляется
        int cellX = e.getX() / cellWidth;//координаты клика по Х в ячейках (ячека как единица измерения)
        int cellY = e.getY() / cellHeight;// координаты клика по Y в ячейках(ячека как единица измерения)
        //System.out.printf ("X: %d; Y: %d \n", cellX, cellY); посмотреть в консоле что координация по ячейкам идет
        if (!isValidCell(cellX, cellY) || !isEmptyCell(cellX, cellY)) return;//если ячейка не пустая, и не корректная обновление прервется.
        field[cellY][cellX] = DOT_HUMAN;//присваиваем ячейке крестик с координатами мышки
        if (checkWin(DOT_HUMAN, lineVictory)) {// проверка на победу пользователя
            stateGameOver = STATE_WIN_HUMAN;// победил пользователь (1)
            isGameOver = true;// ИГРА ОКОНЧЕНА
            return;
        }
        if (isMapFull()) {// ПРОВЕРКА НИЧЬЕЙ
            stateGameOver = STATE_DRAW;// ничья (0)
            isGameOver = true;
            return;
        }
        aiTurn();//ход компьютера
        repaint();// перерисовываем поле
        if (checkWin(DOT_AI, lineVictory)) {//проверка победы компьютера
            stateGameOver = STATE_WIN_AI;//победил компьютер (2)
            isGameOver = true;
            return;
        }
        if (isMapFull()) {// проверка ничьей
            stateGameOver = STATE_DRAW;
            isGameOver = true;
            return;
        }
    }
    //ctrl+o показыввает все методы родителей текущего кламма для переопределения

    @Override
    protected void paintComponent(Graphics g) {//как надо отрисовать поле
        super.paintComponent(g);
        render(g); //в любой программе обновление update , потом отрисовка render
    }
    private void render(Graphics g) {
        if (!initialized) return;// если игра не инициализирована ничего не отрисуется (return прервет выполнение метода)
        // инициализация игры через "Начать играть"
        int panelWidth = getWidth();//высчитываем ширину панели
        int panelHeight = getHeight();//высчитываем высоту панели
        cellHeight = panelHeight / fieldSizeY;//высчитываем высоту ячейки
        cellWidth = panelWidth / fieldSizeX;//высчитываем ширину ячейки
        g.setColor(Color.BLACK);//рисуем черным цветом
        for (int i = 0; i <= fieldSizeY; i++) {
            int y = i * cellHeight;
            g.drawLine(0, y, panelWidth, y);// рисуем линии по ширине
        }
        for (int i = 0; i <= fieldSizeX; i++) {
            int x = i * cellWidth;// рисуем линии по высоте
            g.drawLine(x, 0, x, panelHeight);
        }
        for (int y = 0; y < fieldSizeY; y++) {//пробегаемся по полю
            for (int x = 0; x < fieldSizeX; x++) {
                if (isEmptyCell(x, y)) continue;// если ячейка пустая то ничего не делаем
                if (field[y][x] == DOT_HUMAN)//если в ячейке ход игрока
                    g.setColor(new Color(104, 255, 253));//рисуем синим
                else if (field[y][x] == DOT_AI)
                    g.setColor(new Color(255, 128, 132));//красный цвет
                else
                    throw new RuntimeException("Can't recognize cell: " + field[y][x]);// исключение третий игрок и т.д. на случай расширения предметной области

                g.fillOval(x * cellWidth + DOT_PADDING,// центр ячейки по икс+радиус по икс
                        y * cellHeight + DOT_PADDING,// аналогично по y
                        cellWidth - DOT_PADDING * 2,
                        cellHeight - DOT_PADDING * 2);// рисуем овал
                // DOT_PADDING =5
            }
        }
        if (isGameOver) {
            showMessageGameOver(g);//вывод сообщения при окончании игры
        }
    }
    private void showMessageGameOver(Graphics g) {//вывод сообщения при окончании игры
        g.setColor(Color.DARK_GRAY);//церный фон
        g.fillRect(0, 200, getWidth(), 70);// высота
        g.setColor(Color.YELLOW);// желтые буквы
        g.setFont(new Font("Times new roman", Font.BOLD, 48));//шрифт
        switch (stateGameOver) {
            case STATE_DRAW:// варианты ответа
                g.drawString(MSG_DRAW, 180, getHeight() / 2);
                break;
            case STATE_WIN_AI:
                g.drawString(MSG_WIN_AI, 20, getHeight() / 2);
                break;
            case STATE_WIN_HUMAN:
                g.drawString(MSG_WIN_HUMAN, 70, getHeight() / 2);
                break;
            default:
                throw new RuntimeException("Unexpected gameOver state: " + stateGameOver);
                //искючение на непредвиденные обстоятельства
        }
    }
        /**
        *   Game logic
        * */
        // Ход компьютера
    private void aiTurn() {// выкидываем все статик, так как метод в обьъекте
        if(turnAIWinCell()) return;		// проверим, не выиграет-ли комп на следующем ходу
        if(turnHumanWinCell()) return; // проверим, не выиграет-ли игрок на следующем ходу
        if(turnHumanWinCell2()) return; // проверим, не выиграет-ли игрок на следующем ходу
        int x, y;
        do {							// или комп ходит в случайную клетку
            x = RANDOM.nextInt(fieldSizeX);
            y = RANDOM.nextInt(fieldSizeY);
        } while (!isEmptyCell(x, y));
        field[y][x] = DOT_AI;
    }
    // Проверка, может ли выиграть комп
    private boolean turnAIWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {				// поставим нолик в каждую клетку поля по очереди
                    field[i][j] = DOT_AI;
                    if (checkWin(DOT_AI, lineVictory)) return true;	// если мы выиграли, вернём истину, оставив нолик в выигрышной позиции
                    field[i][j] = DOT_EMPTY;			// если нет - вернём обратно пустоту в клетку и пойдём дальше
                }
            }
        }
        return false;
    }
    // Проверка, выиграет-ли игрок своим следующим ходом
    private boolean turnHumanWinCell() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = DOT_HUMAN;			// поставим крестик в каждую клетку по очереди
                    if (checkWin(DOT_HUMAN, lineVictory)) {			// если игрок победит
                        field[i][j] = DOT_AI;			// поставить на то место нолик
                        return true;
                    }
                    field[i][j] = DOT_EMPTY;			// в противном случае вернуть на место пустоту
                }
            }
        }
        return false;
    }
    // проверка - выйграет ли игрок следующими двумя ходами
    private boolean turnHumanWinCell2() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = DOT_HUMAN;			// поставим крестик в каждую клетку по очереди
                    for (int y = 0; y < fieldSizeY; y++) {
                        for (int x = 0; x < fieldSizeX; x++) {
                            if (isEmptyCell(x, y)) {
                                field[y][x] = DOT_HUMAN;			// поставим крестик в каждую клетку по очереди
                                if (checkWin1(DOT_HUMAN, lineVictory)) {			// если игрок победит
                                    field[y][x] = DOT_AI;			// поставить на то место нолик
                                    field[i][j] = DOT_EMPTY;
                                    return true;
                                }
                                field[y][x] = DOT_EMPTY;			// в противном случае вернуть на место пустоту
                            }
                        }
                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
        }
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isEmptyCell(j, i)) {
                    field[i][j] = DOT_HUMAN;			// поставим крестик в каждую клетку по очереди
                    for (int y = 0; y < fieldSizeY; y++) {
                        for (int x = 0; x < fieldSizeX; x++) {
                            if (isEmptyCell(x, y)) {
                                field[y][x] = DOT_HUMAN;			// поставим крестик в каждую клетку по очереди
                                if (checkWin2(DOT_HUMAN, lineVictory)) {			// если игрок победит
                                    field[y][x] = DOT_AI;			// поставить на то место нолик
                                    field[i][j] = DOT_EMPTY;
                                    return true;
                                }
                                field[y][x] = DOT_EMPTY;			// в противном случае вернуть на место пустоту
                            }
                        }
                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
        }
        return false;
    }
    // проверка на победу
    private boolean checkWin(int c, int wL) {//wL длина линии победы


            /**
             * сначала проверка по всем диагоналям
             */
        if (checkWin1(c, wL)) return true;    // проверим по диагонали х -у
/**
             * потом проверка по всем горизонталям
             */
        if (checkWin2(c, wL)) return true;    // проверим линию по у
        return false;
    }

    private boolean checkWin2(int c, int wL) {
        for (int i = 0; i < fieldSizeX; i++) {            // ползём по всему полю
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 0, 1, wL, c)) return true;
                // складывается ли линия победы по вертикали с ij ячейки?
                if (checkLine(i, j, 1, 0, wL, c)) return true;
                // складывается ли линия победы по горизонтали с ij ячейки?
            }
        }
        return false;
    }

    private boolean checkWin1(int c, int wL) {
        for (int i = 0; i < fieldSizeX; i++) {            // ползём по всему полю
            for (int j = 0; j < fieldSizeY; j++) {
                if (checkLine(i, j, 1, 1, wL, c)) return true;
                // складывается ли линия победы по диагонали с ij ячейки (спускаемся с горки, так как ноль сверху справа)
                if (checkLine(i, j, 1, -1, wL, c)) return true;
                // складывается ли линия победы по диагонали с ij ячейки (поднимаемся в гору, так как ноль сверху справа)

            }
        }
        return false;
    }

    // проверка линии
    // координаты x y , длина линии len
    // vx смещение по x
    // vy смещение по y
    private boolean checkLine(int x, int y, int vx, int vy, int len, int c) {
        final int far_x = x + (len - 1) * vx;			// посчитаем конец проверяемой линии
        //движение по исксу пример  x 0 y 0 , линия победы 3, горизонталь (vx =1, vy = 0)
        // с = DOT_HUMAN // проверка победы пользователя
        // far_x = 0 + (3-1)*1 == 2
        final int far_y = y + (len - 1) * vy;
        // far_y = 0 + (3-1)*0 = 0
        if (!isValidCell(far_x, far_y)) return false;	// проверим не выйдет-ли проверяемая линия за пределы поля
        // поле 0,2 существует
        for (int i = 0; i < len; i++) {					// ползём по проверяемой линии

            if (field[y + i * vy][x + i * vx] != c) return false;	// проверим одинаковые-ли символы в ячейках
            // 0+0*0, 0+0*1 == 0,0
            // 0+1*0, 0+1*1 == 0,1
            // 0+2*0, 0+2*1 == 0,2 // по линии победы на 0 строке с 0 ячейки пользователь победил true или нет
        }
        return true;
    }
    // ничья?
    private boolean isMapFull() {
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (field[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }
    // ячейка-то вообще правильная?
    private boolean isValidCell(int x, int y) { return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY; }
    // а пустая?
    private boolean isEmptyCell(int x, int y) { return field[y][x] == DOT_EMPTY; }
}


