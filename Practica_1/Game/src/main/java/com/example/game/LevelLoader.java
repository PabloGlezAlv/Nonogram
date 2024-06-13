package com.example.game;

import com.example.engine.Engine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class LevelLoader {

    private Engine e_;
    private ArrayList<ArrayList<Boolean>> levelContinueMarked;

    LevelLoader(Engine e) {
        e_ = e;
    }

    public Level loadLevel(String levelName) {
        try {
            InputStream is = e_.openInputStream("levels/"+levelName);
            Scanner reader = new Scanner(is);

            Level l = new Level();

            int width = reader.nextInt();
            int height = reader.nextInt();

            ArrayList<ArrayList<Boolean>> levelMap = new ArrayList<ArrayList<Boolean>>(height);

            for (int i = 0; i < height; i++) {
                levelMap.add(new ArrayList<Boolean>(width));
                for(int j = 0; j < width; j++) {
                    int nextInt = reader.nextInt();
                    if(nextInt==1) levelMap.get(i).add(true);
                    else levelMap.get(i).add(false);
                }
            }

            // Generacion de arrays de numeros que apareceran
            // a la izquierda y encima del tablero indicando la
            // cantidad de casillas a marcar
            ArrayList<ArrayList<Integer>> topNumbers = new ArrayList<ArrayList<Integer>>(width);
            ArrayList<ArrayList<Integer>> leftNumbers = new ArrayList<ArrayList<Integer>>(width);
            boolean isOne = false;
            int n = 0;
            for(int j = 0; j < width; j++) {
                isOne = false;
                n = 0;
                topNumbers.add(j, new ArrayList<Integer>(width/2+1));
                for(int i = 0; i < width; i++) {
                    if(levelMap.get(i).get(j))
                    {
                        n++;
                        isOne = true;

                        if(i == width -1 )
                        {
                            topNumbers.get(j).add(n);
                        }
                    }
                    else if(!levelMap.get(i).get(j)&& isOne)
                    {
                        isOne = false;
                        topNumbers.get(j).add(n);
                        n = 0;
                    }
                }
            }

            for(int i = 0; i < width; i++) {
                isOne = false;
                n = 0;

                leftNumbers.add(i, new ArrayList<Integer>(width/2+1));
                for(int j = 0; j < width; j++) {
                    if(levelMap.get(i).get(j))
                    {
                        n++;
                        isOne = true;
                        if(j == width -1 )
                        {
                            leftNumbers.get(i).add(n);
                        }
                    }
                    else if(!levelMap.get(i).get(j)&& isOne)
                    {
                        isOne = false;
                        leftNumbers.get(i).add(n);
                        n = 0;
                    }
                }
            }

            l.init(width, height, levelMap, topNumbers, leftNumbers);

            reader.close();

            return l;
        }
        catch (IOException e) {
            System.out.println("Nivel no encontrado o CORRUPTO.");
            e.printStackTrace();
        }
        return null;
    }

    public Level loadLevelRandom() {

        Level l = new Level();

        int max = 10;
        int min = 5;

        int size = (int)(Math.random()*max+min);

        ArrayList<ArrayList<Boolean>> levelMap = new ArrayList<ArrayList<Boolean>>(size);

        for (int i = 0; i < size; i++) {
            levelMap.add(new ArrayList<Boolean>(size));
            for(int j = 0; j < size; j++) {
                int nextInt = (int) (Math.random()*2); //random 0 o 1

                if(nextInt==1) levelMap.get(i).add(true);
                else levelMap.get(i).add(false);
            }
        }

        int width = size;
        // Generacion de arrays de numeros que apareceran
        // a la izquierda y encima del tablero indicando la
        // cantidad de casillas a marcar
        ArrayList<ArrayList<Integer>> topNumbers = new ArrayList<ArrayList<Integer>>(width);
        ArrayList<ArrayList<Integer>> leftNumbers = new ArrayList<ArrayList<Integer>>(width);
        boolean isOne = false;
        int n = 0;
        for(int j = 0; j < width; j++) {
            isOne = false;
            n = 0;
            topNumbers.add(j, new ArrayList<Integer>(width/2+1));
            for(int i = 0; i < width; i++) {
                if(levelMap.get(i).get(j))
                {
                    n++;
                    isOne = true;

                    if(i == width -1 )
                    {
                        topNumbers.get(j).add(n);
                    }
                }
                else if(!levelMap.get(i).get(j)&& isOne)
                {
                    isOne = false;
                    topNumbers.get(j).add(n);
                    n = 0;
                }
            }
        }

        for(int i = 0; i < width; i++) {
            isOne = false;
            n = 0;

            leftNumbers.add(i, new ArrayList<Integer>(width/2+1));
            for(int j = 0; j < width; j++) {
                if(levelMap.get(i).get(j))
                {
                    n++;
                    isOne = true;
                    if(j == width -1 )
                    {
                        leftNumbers.get(i).add(n);
                    }
                }
                else if(!levelMap.get(i).get(j)&& isOne)
                {
                    isOne = false;
                    leftNumbers.get(i).add(n);
                    n = 0;
                }
            }
        }

        l.init(size, size, levelMap, topNumbers, leftNumbers);

        return l;
    }

    //Metodo para inicializar el nivel en el que nos quedamos y los inputs que habia
    //realizado el jugador antes de salir de la aplicacion
    public Level loadLevelContinue() {
        try {
            FileInputStream file = e_.getFileInputStream("savedBoard.ser");
            ObjectInputStream in = new ObjectInputStream(file);

            Level l = new Level();

            String reader = (String)in.readObject();

            String aux[] = reader.split("\n");

            int width = Integer.parseInt(aux[0]);
            int height = Integer.parseInt(aux[1]);

            ArrayList<ArrayList<Boolean>> levelMap = new ArrayList<ArrayList<Boolean>>(height);
            String line[] = new String[height];
            for (int i = 0; i < height; i++) {
                line = aux[i+2].split(" ");
                levelMap.add(new ArrayList<Boolean>(width));
                for(int j = 0; j < width; j++) {
                    int nextInt = Integer.parseInt(line[j]);
                    if(nextInt==1) levelMap.get(i).add(true);
                    else levelMap.get(i).add(false);
                }
            }

            // Generacion de arrays de numeros que apareceran
            // a la izquierda y encima del tablero indicando la
            // cantidad de casillas a marcar
            ArrayList<ArrayList<Integer>> topNumbers = new ArrayList<ArrayList<Integer>>(width);
            ArrayList<ArrayList<Integer>> leftNumbers = new ArrayList<ArrayList<Integer>>(width);
            boolean isOne = false;
            int n = 0;
            for(int j = 0; j < width; j++) {
                isOne = false;
                n = 0;
                topNumbers.add(j, new ArrayList<Integer>(width/2+1));
                for(int i = 0; i < width; i++) {
                    if(levelMap.get(i).get(j))
                    {
                        n++;
                        isOne = true;

                        if(i == width -1 )
                        {
                            topNumbers.get(j).add(n);
                        }
                    }
                    else if(!levelMap.get(i).get(j)&& isOne)
                    {
                        isOne = false;
                        topNumbers.get(j).add(n);
                        n = 0;
                    }
                }
            }

            for(int i = 0; i < width; i++) {
                isOne = false;
                n = 0;

                leftNumbers.add(i, new ArrayList<Integer>(width/2+1));
                for(int j = 0; j < width; j++) {
                    if(levelMap.get(i).get(j))
                    {
                        n++;
                        isOne = true;
                        if(j == width -1 )
                        {
                            leftNumbers.get(i).add(n);
                        }
                    }
                    else if(!levelMap.get(i).get(j)&& isOne)
                    {
                        isOne = false;
                        leftNumbers.get(i).add(n);
                        n = 0;
                    }
                }
            }

            l.init(width, height, levelMap, topNumbers, leftNumbers);

            //Guardado de casillas ya marcadas por el jugador
            levelContinueMarked = new ArrayList<ArrayList<Boolean>>(height);
            String line2[] = new String[height];
            for (int i = 0; i < height; i++) {
                line2 = aux[i+height+2].split(" ");
                levelContinueMarked.add(new ArrayList<Boolean>(width));
                for(int j = 0; j < width; j++) {
                    int nextInt = Integer.parseInt(line2[j]);
                    if(nextInt==1) levelContinueMarked.get(i).add(true);
                    else levelContinueMarked.get(i).add(false);
                }
            }

            in.close();
            file.close();

            return l;
        }
        catch (IOException | ClassNotFoundException e) {
            System.out.println("Nivel no encontrado o CORRUPTO.");
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<ArrayList<Boolean>> getLevelContinueMarked() {return levelContinueMarked;}
}
