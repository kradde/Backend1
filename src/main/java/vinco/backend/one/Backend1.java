package vinco.backend.one;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Alex Garcia
 */
public class Backend1 {

    public static void main(String[] args) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("input.txt"));
            String line = reader.readLine();
            int[][] matrix;
            if (line != null) {
                String[] split = line.trim().split(" ");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                matrix = new int[x][y];

                int row = 0;
                line = reader.readLine();
                while (line != null) {
                    if (row < x) {
                        if (line.length() > y) {
                            System.out.println("Dato no valido en la fila: " + (row + 1));
                            return;
                        }
                        for (int i = 0; i < y; i++) {
                            matrix[row][i] = Character.getNumericValue(line.charAt(i));
                        }
                        row++;
                        line = reader.readLine();
                    } else {
                        break;
                    }
                }

                int start;
                Scanner scan = new Scanner(System.in);
                System.out.print("En qué posición deseas iniciar: ");
                start = scan.nextInt();
                
                if (start < 1 || start > y || matrix[0][start - 1] == 1) {
                    System.out.println("Inicio no valido");
                    return;
                }

                process(matrix, start);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    System.out.println("Error al cerrar el archivo: " + e.getMessage());
                }
            }
        }
    }
    
    public static void process(int[][] matrix, int start) {
        int curColumn = start - 1;
        int curRow = 0;
        ArrayList<int[]> curPaths = new ArrayList<>();
        curPaths.add(new int[]{curRow, curColumn});
        
        // Get all available paths to down in the matrix
        ArrayList<ArrayList<Integer>> rowPaths = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            ArrayList<Integer> paths = new ArrayList<>();
            if (i < matrix.length - 1) {
                for (int j = 0; j < matrix[i].length; j++){
                    if (matrix[i][j] == 0 && matrix[i + 1][j] == 0) {
                        paths.add(j);
                    }
                }
                rowPaths.add(paths);
            }
        }
        
        // Walking through the matrix
        while (curRow < matrix.length - 1) {
            boolean changedRow = false;
            
            pathsToDown:
            for (Integer path : rowPaths.get(curRow)) {
                if (path != curColumn) {
                    int d = path - curColumn;
                    if (d < 0) {  // walking to left
                        for (int i = curColumn - 1; i >= path; i--) {
                            if (matrix[curRow][i] == 1) {
                                continue pathsToDown;
                            }
                            curPaths.add(new int[]{curRow, i});
                        }
                    } else {  // walking to right
                        for (int i = curColumn + 1; i <= path; i++) {
                            if (matrix[curRow][i] == 1) {
                                continue pathsToDown;
                            }
                            curPaths.add(new int[]{curRow, i});
                        }
                    }
                }
                
                // walking to down or attempt the next path to down
                if (matrix[curRow + 1][path] == 0) {
                    curRow++;
                    curColumn = path;
                    curPaths.add(new int[]{curRow, curColumn});
                    changedRow = true;
                    break;
                } else {
                    continue;
                }
            }
            
            // if no more paths to down from the current row and column, go back!!
            if (!changedRow) {
                for (int i = curPaths.size() - 1; i >= 0; i--) {
                    if (curPaths.get(i)[0] == curRow) {
                        curPaths.remove(i);
                    }else{
                        break;
                    }
                }
                curRow--;
                if (curRow < 0) { // all available paths to down already tested
                    System.out.println("No hay rutas que lleguen hasta la fila inferior");
                    break;
                }
                curColumn = rowPaths.get(curRow).get(0);
                rowPaths.get(curRow).remove(0);
            }
        }
        
        // print the correct path if exist
        if (!curPaths.isEmpty()) {
            System.out.println("Ruta: ");
            for (int[] path : curPaths) {
                System.out.println("[FILA: " + (path[0] +  1) + " COLUMNA: " + (path[1] +  1) + "] ");
            }
        }
    }
}
