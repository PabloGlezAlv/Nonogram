package com.example.game;

import com.example.engine.Engine;
import com.example.engine.Graphics;
import com.example.engine.Input;

import java.util.ArrayList;

/*Clase que contiene el tablero y toda su funcionalidad*/
public class Board {
    Board(int width, int height) {
        cellBoard_ = new ArrayList<>(size_);

        y = 0;
        completed = false;
        failed = false;
        centered = false;

        gW = width;
        gH = height;

        x = gW/4;
        y = (gH/6)*2;
        side_ = gW*3/4-10;
    }

    /*Inicializacion de las casillas del tablero*/
    public void init() {

        for(int i = 0; i < size_; i++) {
            cellBoard_.add(i, new ArrayList<Cell>(size_));
            for(int j = 0; j < size_; j++) {
                cellBoard_.get(i).add(j, new Cell(gW/4+i*(side_/size_),y+j*(side_/size_),side_/size_, false));
            }
        }

        life = 3;
    }
    /*Pinta cada frame el tablero y el tablero en formato 'fin de partida' una vez se ha completado el nivel*/
    void render(Graphics g) {

        g.setColor(PlayerState.wordColor);

        if(!completed)
        {
            //tablero
            int boardx = gW/4;
            g.drawSquare(boardx, y, side_);
            for(int i = 1; i < size_; i++) {
                g.drawLine(boardx+i*(side_/size_), y, boardx+i*(side_/size_), y+side_);
                g.drawLine(boardx, y+i*(side_/size_), boardx+side_, y+i*(side_/size_));
            }
        }

        //celdas
        for(int i = 0; i < size_; i++) {
            for(int j = 0; j < size_; j++) {
                cellBoard_.get(i).get(j).render(g);
            }
        }

        g.setColor(PlayerState.wordColor);
        if(!completed) {
            //rectangulos y numeros
            g.drawRectangle(10, y, gW/4-10, side_);                    //izq
            g.drawRectangle(gW/4, y - (gW/4-10), side_, gW/4-10);    //arr

            int tamCola = size_ / 2 + 1; //Numero de valores separados q se soportan

            int cellSizey = (gW/4-10) / tamCola;
            int cellSizex = side_ / size_;
            int x0 = gW/4;
            int y0 = y;
            String value;

            //TopNumbers
            for (int fila = 0; fila < tamCola; fila++) {
                for (int columna = 0; columna < size_; columna++) {
                    if (fila >= topNumbers.get(columna).size()) {
                        value = " ";
                    } else
                        value = topNumbers.get(columna).get(topNumbers.get(columna).size()-fila-1).toString();

                    g.drawCenteredText(value, x0, y0 - cellSizey, cellSizex, cellSizey);
                    x0 += cellSizex;
                }
                x0 = gW/4;
                y0 -= cellSizey;
            }

            //LeftNumbers
            x0 = 0;
            y0 = y;
            cellSizex = (gW/4-10) / tamCola;
            cellSizey = side_ / size_;
            int aux = 0;
            for (int fila = 0; fila < size_; fila++) {
                for (int columna = tamCola - 1; columna >= 0; columna--) {
                    if (columna >= leftNumbers.get(fila).size()) {
                        value = " ";
                    } else {
                        value = leftNumbers.get(fila).get(aux).toString();
                        aux++;
                    }

                    g.drawCenteredText(value, x0, y0, cellSizex, cellSizey);
                    x0 += cellSizex;
                }
                y0 += cellSizey;
                x0 = 0;
                aux = 0;
            }

            // Displayed info
            g.setColor(0xFFFF0000);
            g.drawCenteredText(nNotMarkedS, 0, 0, gW, y - (x - 25));
            g.drawCenteredText(nWrongS, 0, 50, gW, y - (x - 25) - 50);
        }
    }
    /*Procesa el input del mouse dentro del tablero enviando a la casilla interactuada la interaccion para su procesamiento*/
    public void handleInput(Input.TouchEvent ev) {
        int ratonx = ev.posX;
        int ratony = ev.posY;

        // Si esta dentro del tablero
        if(!completed && !failed && (ratonx > x && ratonx < x + side_) && (ratony > y && ratony < y + side_)) {

            int casillax = (ratonx-(x)) / (side_/size_);
            int casillay = (ratony-(y)) / (side_/size_);

            cellBoard_.get(casillax).get(casillay).handleInput(ev);

            if((!level.getLevelMap().get(casillay).get(casillax) && cellBoard_.get(casillax).get(casillay).getState() == Cell.State.MARKED) ||
                    (level.getLevelMap().get(casillay).get(casillax) && cellBoard_.get(casillax).get(casillay).getState() == Cell.State.CROSS)) {
                //perdemos vida
                life--;
                cellBoard_.get(casillax).get(casillay).setState(Cell.State.WRONG);
            }
        }
    }
    /*Procesa la posicion de las cells individualmente y llama a su update*/
    public void update(float deltaTime) {
        for(int i = 0; i < size_; i++) {
            for(int j = 0; j < size_; j++) {
                if(completed)
                    cellBoard_.get(i).get(j).setPos(gW/4+i*(side_/size_) - gW/8, cellBoard_.get(i).get(j).getY());
                if(!failed) cellBoard_.get(i).get(j).update(deltaTime);
            }
        }
    }

    /*Settea al nivel deseado el tablero*/
    public void setLevel(Level l) {
        level = l;

        size_ = l.getWidth();
        topNumbers = l.getTopNumbers();
        leftNumbers = l.getLeftNumbers();
    }
    /*Comprueba si alguna casilla marcada no deberia estarlo asi como cuantas casillas faltan por marcar
    * Tambien pone azules las casillas una vez completado el nivel*/
    public boolean checkResults() {
        int nWrong = 0;
        int nNotMarked = 0;
        int max = y-(x-10);

        int i = 0, j = 0;
        while(i<size_) {
            j = 0;
            while(j<size_) {
                if((cellBoard_.get(i).get(j).isMarked() || cellBoard_.get(i).get(j).getState() == Cell.State.WRONG) && !level.getLevelMap().get(j).get(i)) {
                    nWrong++;
                    cellBoard_.get(i).get(j).setState(Cell.State.WRONG);
                }
                if(level.getLevelMap().get(j).get(i) && !cellBoard_.get(i).get(j).isMarked())
                {
                    nNotMarked++;
                }
                j++;
            }
            i++;
        }
        if(nNotMarked == 0 && nWrong==0)
        {
            completed = true;
        }

        if (completed)
        {
            i = 0;

            while(i<size_)
            {
                j = 0;
                while(j<size_)
                {
                    if(level.getLevelMap().get(j).get(i) && cellBoard_.get(i).get(j).isMarked())
                    {
                        cellBoard_.get(i).get(j).setState(Cell.State.COMPLETED);
                    }
                    j++;
                }
                i++;
            }
        }

        return completed;
    }

    //Marca las casillas si hay reenganche de tablero guardado
    public void setMarkedBoard(ArrayList<ArrayList<Boolean>> marked) {
        cellBoard_ = new ArrayList<>(size_);
        for(int i = 0; i < size_; i++) {
            cellBoard_.add(i, new ArrayList<Cell>(size_));
            for(int j = 0; j < size_; j++) {
                cellBoard_.get(i).add(j, new Cell(gW/4+i*(side_/size_),y+j*(side_/size_),side_/size_, marked.get(j).get(i)));
            }
        }
    }

    public void setFailed(boolean aux) {
        failed = aux;
    }

    public int getLifes(){return life;}
    public void setLifes(int lifes){ life = lifes;}
    public int getSize() { return size_; }
    public ArrayList<ArrayList<Cell>> getCellBoard() {
        return cellBoard_;
    }
    private Level level;

    private ArrayList<ArrayList<Cell>> cellBoard_;

    private ArrayList<ArrayList<Integer>> topNumbers;
    private ArrayList<ArrayList<Integer>> leftNumbers;

    private int size_;
    private int side_;
    private int x, y;

    //Cadenas de informacion de la IU
    String nWrongS = " ";
    String nNotMarkedS = " ";

    boolean completed;
    boolean failed;
    boolean centered;

    int gW = 0;
    int gH = 0;

    int life;
}
