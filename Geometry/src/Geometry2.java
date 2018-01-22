import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Geometry2 {
    public static void main(String[] args) {
        int[][] array = load_image("rectangles.bmp");
        ArrayList<Shape> shapes = findShapes(array);
        printShapes(shapes);
        moveShapes(shapes);
        //printArray(array);
    }

    static int[][] load_image(String filename) {
        try {
            BufferedImage image = ImageIO.read(new File(filename));
            int bands = image.getData().getNumBands();
            int width = image.getWidth();
            int height = image.getHeight();

            int[] background_color = new int[bands];
            image.getData().getPixel(0, 0, background_color);

            int[] tmp = new int[width * height * bands];
            image.getData().getPixels(0, 0, width, height, tmp);

            int[][] array = new int[height][width];

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width * bands; j += 3) {
                    if (tmp[i * width * bands + j] != background_color[0]) {
                        array[i][j / 3] = 1;
                    }
                }
            }
            return array;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        return null;
    }

    static ArrayList<Shape> findShapes(int[][] array) {
        int height = array.length;
        int width = array[0].length;

        ArrayList<Shape> shapes = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (array[i][j] == 1 && array[i][j + 1] == 1 && array[i + 1][j] == 1) {
                    if ((i == 21 && j == 42) || (i == 49 && j == 64)) System.out.println("FOUND");
                    //System.out.println("Point: " + j + " " + i);
                    shapes.addAll(findRectangle(array, i, j));
                    //shapes.addAll(findEllipses(array, i, j));
                }
            }
        }
        return shapes;
    }

    static ArrayList<Shape> findEllipses(int[][] array, int row, int column) {
        int direction = 0;  // RIGHT => 0, RIGHT/DOWN => 1, DOWN => 2, DOWN/LEFT => 3
        // LEFT => 4, LEFT/UP => 5, UP => 6, UP/RIGHT => 7

        Point[] points = new Point[8];
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point();
        }
        points[0].setLocation(column, row);

        int actualX = column + 1;
        int actualY = row;

        boolean fallback = false;
        boolean wasHorizontal = false;
        boolean wasVertical = false;

        ArrayList<Integer> steps = new ArrayList<>();
        ArrayList<Shape> ellipses = new ArrayList<>();

        while (true) {
            switch (direction) {
                case 0: {
                    if (!fallback && array[actualY + 1][actualX] == 1 && actualX - points[0].x > 1) {
                        points[1].setLocation(actualX, actualY);
                        direction = 1;
                        actualY++;
                        fallback = true;
                        break;
                    }

                    if (array[actualY][actualX + 1] == 0) {
                        return ellipses;
                    }

                    actualX++;
                    fallback = false;
                    break;
                }
                case 1: {
                    if (!fallback && !wasHorizontal && array[actualY][actualX - 1] == 1) {
                        points[3].setLocation(actualX, actualY);
                        direction = 2;
                        actualX--;

                        for (int i = 0; i < points[3].y - points[2].y + 1; i++) {
                            steps.remove(steps.size() - 1);
                        }
                        System.out.println(steps);
                        break;
                    }

                    if (!fallback && array[actualY + 1][actualX] == 1) {
                        if (wasHorizontal) {
                            points[2].setLocation(actualX, actualY);
                            wasHorizontal = false;
                        }
                        steps.add(1);
                        actualY++;
                        break;
                    }

                    if (array[actualY][actualX + 1] == 0) {
                        direction = 0;
                        actualX = points[1].x;
                        actualY = points[1].y;
                        fallback = true;
                        steps.clear();
                        break;
                    }

                    wasHorizontal = true;
                    fallback = false;
                    steps.add(0);
                    actualX++;
                    break;
                }
                case 2: {

                    int step;
                    for (int i = steps.size() - 1; i >= 0; i--) {
                        step = steps.get(i);
                        if (step == 0) {
                            actualX--;
                        }
                        if (step == 1) {
                            actualY++;
                        }

                        if (array[actualY][actualX] == 0) {
                            direction = 1;
                            actualX = points[3].x;
                            actualY = points[3].y;
                            fallback = true;
                            break;
                        }
                    }

                    actualY++;
                    if (array[actualY][actualX] == 0) {
                        direction = 1;
                        actualX = points[3].x;
                        actualY = points[3].y;
                        fallback = true;
                        break;
                    }
                    points[4].setLocation(actualX, actualY);

                    for (int i = 0; i < points[4].x - points[0].x; i++) {

                        if (array[actualY][actualX] == 0) {
                            direction = 1;
                            actualX = points[3].x;
                            actualY = points[3].y;
                            fallback = true;
                            break;
                        }
                        actualX--;
                    }

                    points[5].setLocation(actualX, actualY);
                    actualY--;
                    if (array[actualY][actualX] == 0) {
                        direction = 1;
                        actualX = points[3].x;
                        actualY = points[3].y;
                        fallback = true;
                        break;
                    }
                    direction = 3;

                    break;
                }
                case 3: {

                    int step;
                    for (int i = 0; i < steps.size(); i++) {
                        step = steps.get(i);
                        if (step == 0) {
                            actualX--;
                        }
                        if (step == 1) {
                            actualY--;
                        }

                        if (array[actualY][actualX] == 0) {
                            direction = 1;
                            actualX = points[3].x;
                            actualY = points[3].y;
                            fallback = true;
                            break;
                        }
                    }
                    actualX--;
                    if (array[actualY][actualX] == 0) {
                        direction = 1;
                        actualX = points[3].x;
                        actualY = points[3].y;
                        fallback = true;
                        break;
                    }

                    points[6].setLocation(actualX, actualY);
                    for (int i = 0; i < points[3].y - points[2].y; i++) {

                        if (array[actualY][actualX] == 0) {

                            direction = 1;
                            actualX = points[3].x;
                            actualY = points[3].y;
                            fallback = true;
                            break;
                        }
                        actualY--;
                    }

                    actualX++;
                    if (array[actualY][actualX] == 0) {
                        direction = 1;
                        actualX = points[3].x;
                        actualY = points[3].y;
                        fallback = true;
                        break;
                    }
                    points[7].setLocation(actualX, actualY);
                    direction = 4;

                }
                case 4: {
                    System.out.println(actualX + " " + actualY);
                    int step;
                    for (int i = steps.size() - 1; i >= 0; i--) {
                        step = steps.get(i);
                        if (step == 0) {
                            actualX++;
                        }
                        if (step == 1) {
                            actualY--;
                        }
                        System.out.println(actualX + " " + actualY);
                        if (array[actualY][actualX] == 0) {

                            direction = 1;
                            actualX = points[3].x;
                            actualY = points[3].y;
                            fallback = true;
                            break;
                        }
                    }
                    actualY--;
                    if (actualX == points[0].x && actualY == points[0].y) {
                        ellipses.add(new Ellipse(points));
                        direction = 1;
                        actualX = points[3].x;
                        actualY = points[3].y;
                        fallback = true;
                        break;
                    }
                    break;
                }
            }
        }


        /*while (true) {
            //System.out.println(actualX + " " + actualY);
            switch (direction) {
                case 0: {
                    if (array[actualY + 1][actualX] == 1 && actualX - points[0].x > 1) {
                        points[1] = new Point(actualX, actualY);
                        direction = 1;
                        actualY++;
                        continue;
                    }


                    if (array[actualY][actualX + 1] == 0) {
                        return ellipses;
                    }

                    actualX++;
                }
                case 1: {
                    if (array[actualY][actualX - 1] == 1 && actualY - points[1].y > 1) {
                        points[2] = new Point(actualX, actualY);
                        direction = 2;
                        actualX--;
                        continue;
                    }

                    if (array[actualY + 1][actualX] == 0) {
                        direction = 0;
                        actualX = points[1].x;
                        actualY = points[1].y;
                        if (array[actualY][actualX + 1] == 0) {
                            return ellipses;
                        }
                        actualX++;
                        continue;
                    }


                    actualY++;
                }
            } else if (direction == 2) {

                if (array[actualY - 1][actualX] == 1 && actualX == points[0].x) {
                    points[3] = new Point(actualX, actualY);
                    direction = 3;
                    actualY--;
                    continue;
                }

                if (actualX < points[0].x || array[actualY][actualX - 1] == 0) {
                    direction = 1;
                    actualX = points[2].x;
                    actualY = points[2].y;
                    if (array[actualY + 1][actualX] == 0) {
                        direction = 0;
                        actualX = points[1].x;
                        actualY = points[1].y;
                        if (array[actualY][actualX + 1] == 0) {
                            return rectangles;
                        }
                        actualX++;
                        continue;
                    }
                    actualY++;
                    continue;
                }

                actualX--;
            } else {
                if (actualY == points[0].y) {
                    rectangles.add(new Rectangle(points));
                    actualY--;
                }

                if (actualY < points[0].y || array[actualY - 1][actualX] == 0) {
                    direction = 2;
                    actualX = points[3].x;
                    actualY = points[3].y;
                    if (array[actualY][actualX - 1] == 0) {
                        direction = 1;
                        actualX = points[2].x;
                        actualY = points[2].y;
                        if (array[actualY + 1][actualX] == 0) {
                            direction = 0;
                            actualX = points[1].x;
                            actualY = points[1].y;
                            if (array[actualY][actualX + 1] == 0) {
                                return rectangles;
                            }
                            actualX++;
                            continue;
                        }
                        actualY++;
                        continue;
                    }
                    actualX--;
                    continue;
                }

                actualY--;
            }
        }*/
    }

    static ArrayList<Rectangle> findRectangle(int[][] array, int row, int column) {
        int direction = 0; // RIGHT => 0, DOWN => 1, LEFT => 2, UP => 3

        Point[] points = new Point[4];
        points[0] = new Point(column, row);

        int actualX = column + 1;
        int actualY = row;

        ArrayList<Rectangle> rectangles = new ArrayList<>();

        while (true) {
            //System.out.println(actualX + " " + actualY);
            if (direction == 0) {
                if (array[actualY + 1][actualX] == 1 && actualX - points[0].x > 1) {
                    points[1] = new Point(actualX, actualY);
                    direction = 1;
                    actualY++;
                    continue;
                }

                if (array[actualY][actualX + 1] == 0) {
                    return rectangles;
                }

                actualX++;
            } else if (direction == 1) {
                if (array[actualY][actualX - 1] == 1 && actualY - points[1].y > 1) {
                    points[2] = new Point(actualX, actualY);
                    direction = 2;
                    actualX--;
                    continue;
                }

                if (array[actualY + 1][actualX] == 0) {
                    direction = 0;
                    actualX = points[1].x;
                    actualY = points[1].y;
                    if (array[actualY][actualX + 1] == 0) {
                        return rectangles;
                    }
                    actualX++;
                    continue;
                }

                actualY++;
            } else if (direction == 2) {

                if (array[actualY - 1][actualX] == 1 && actualX == points[0].x) {
                    points[3] = new Point(actualX, actualY);
                    direction = 3;
                    actualY--;
                    continue;
                }

                if (actualX < points[0].x || array[actualY][actualX - 1] == 0) {
                    direction = 1;
                    actualX = points[2].x;
                    actualY = points[2].y;
                    if (array[actualY + 1][actualX] == 0) {
                        direction = 0;
                        actualX = points[1].x;
                        actualY = points[1].y;
                        if (array[actualY][actualX + 1] == 0) {
                            return rectangles;
                        }
                        actualX++;
                        continue;
                    }
                    actualY++;
                    continue;
                }

                actualX--;
            } else {
                if (actualY == points[0].y) {
                    rectangles.add(new Rectangle(points));
                    actualY--;
                }

                if (actualY < points[0].y || array[actualY - 1][actualX] == 0) {
                    direction = 2;
                    actualX = points[3].x;
                    actualY = points[3].y;
                    if (array[actualY][actualX - 1] == 0) {
                        direction = 1;
                        actualX = points[2].x;
                        actualY = points[2].y;
                        if (array[actualY + 1][actualX] == 0) {
                            direction = 0;
                            actualX = points[1].x;
                            actualY = points[1].y;
                            if (array[actualY][actualX + 1] == 0) {
                                return rectangles;
                            }
                            actualX++;
                            continue;
                        }
                        actualY++;
                        continue;
                    }
                    actualX--;
                    continue;
                }

                actualY--;
            }
        }
    }

    static void printArray(int[][] array) {
        int height = array.length;
        int width = array[0].length;

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                System.out.print(array[i][j]);
            }
            System.out.println();
        }
    }

    static void printShapes(ArrayList<Shape> shapes) {
        for (int i = 0; i < shapes.size(); i++) {
            System.out.println(i + " - " + shapes.get(i).getClass() + " " + shapes.get(i).printCorners());
        }
        System.out.println();
        for (int i = 0; i < shapes.size(); i++) {
            for (int j = 0; j < shapes.size(); j++) {
                if (i == j) {
                    System.out.println(i + " - " + j + " => X");
                    continue;
                }
                System.out.println(i + " - " + j + " => " + checkPosition(shapes.get(i), shapes.get(j)));
            }
        }
    }

    static String checkPosition(Shape s, Shape s2) {
        int x = Math.abs(s.center.x - s2.center.x);
        int y = Math.abs(s.center.y - s2.center.y);

        /*System.out.println(s.printCorners());
        System.out.println(s.width);
        System.out.println(s.height);
        System.out.println(s.center);
        System.out.println(s2.printCorners());
        System.out.println(s2.width);
        System.out.println(s2.height);
        System.out.println(s2.center);*/

        if (s.getTopBound() < s2.getTopBound()
                && s.getBottomBound() > s2.getBottomBound()
                && s.getLeftBound() < s2.getLeftBound()
                && s.getRightBound() > s2.getRightBound()) return "Zawiera";

        if (s.getTopBound() > s2.getTopBound()
                && s.getBottomBound() < s2.getBottomBound()
                && s.getLeftBound() > s2.getLeftBound()
                && s.getRightBound() < s2.getRightBound()) return "Zawiera sie";

        if (s.getTopBound() > s2.getBottomBound()
                || s.getBottomBound() < s2.getTopBound()
                || s.getLeftBound() > s2.getRightBound()
                || s.getRightBound() < s2.getLeftBound()) return "Rozlaczne";

        if (s.getTopBound() == s2.getTopBound()
                || s.getTopBound() == s2.getBottomBound()
                || s.getBottomBound() == s2.getTopBound()
                || s.getBottomBound() == s2.getBottomBound()
                || s.getLeftBound() == s2.getRightBound()
                || s.getLeftBound() == s2.getLeftBound()
                || s.getRightBound() == s2.getLeftBound()
                || s.getRightBound() == s2.getRightBound()) return "Styczne";

        if ((s.getLeftBound() < s2.getLeftBound() && s2.getLeftBound() < s.getRightBound())
                || (s.getLeftBound() < s2.getRightBound() && s2.getRightBound() < s.getRightBound())
                || (s.getTopBound() < s2.getBottomBound() && s2.getBottomBound() < s.getBottomBound())
                || (s.getTopBound() < s2.getTopBound() && s2.getTopBound() < s.getBottomBound()))
            return "Wspolna czesc";

        return "?";
    }

    static void moveShapes(ArrayList<Shape> shapes) {
        Scanner scanner = new Scanner(System.in);
        Point vector = new Point();
        String answer;
        int shape;
        while (true) {
            System.out.println("Ktora figure chcesz przesunac? ");
            shape = scanner.nextInt();
            if (shape < 0 || shape > shapes.size()) {
                continue;
            }
            System.out.println("Podaj wektor przemieszczenia? ");
            vector.setLocation(scanner.nextInt(), scanner.nextInt());
            shapes.get(shape).moveShape(vector);
            do {
                System.out.println("Chcesz potworzyc? [y/n] ");
                answer = scanner.next();
            } while (!answer.equals("y") && !answer.equals("n"));

            if (answer.equals("n")) break;
        }

        printShapes(shapes);
    }
}

abstract class Shape {
    int width;
    int height;
    Point center;
    Point[] corners;

    void moveShape(Point vector) {
        center.setLocation(vector);
        for (int i = 0; i < corners.length; i++) {
            corners[i].setLocation(vector);
        }
    }

    String printCorners() {
        return Arrays.toString(corners);
    }

    int getTopBound() {
        return corners[0].y;
    }

    int getBottomBound() {
        return corners[corners.length / 2].y;
    }

    int getLeftBound() {
        return corners[corners.length - 1].x;
    }

    int getRightBound() {
        return corners[corners.length / 4].x;
    }
}

class Rectangle extends Shape {

    Rectangle(Point[] corners) {
        this.corners = corners;
        this.width = corners[1].x - corners[0].x;
        this.height = corners[3].y - corners[0].y;
        this.center = new Point((corners[2].x - corners[0].x / 2), (corners[2].y - corners[0].y / 2));

        System.out.println("Rectangle");
        System.out.print("(" + corners[0].x + "," + corners[0].y + ")  ");
        System.out.println("(" + corners[1].x + "," + corners[1].y + ")");
        System.out.print("(" + corners[3].x + "," + corners[3].y + ")  ");
        System.out.println("(" + corners[2].x + "," + corners[2].y + ")");
    }
}

class Ellipse extends Shape {

    Ellipse(Point[] corners) {
        this.corners = corners;
        this.width = corners[2].x - corners[7].x;
        this.height = corners[5].y - corners[0].y;
        this.center = new Point((corners[4].x - corners[0].x / 2), (corners[4].y - corners[0].y / 2));

        System.out.println("Ellipse");
        System.out.print("(" + corners[0].x + "," + corners[0].y + ")  ");
        System.out.println("(" + corners[1].x + "," + corners[1].y + ")");
        System.out.print("(" + corners[7].x + "," + corners[7].y + ")  ");
        System.out.println("(" + corners[2].x + "," + corners[2].y + ")");
        System.out.print("(" + corners[6].x + "," + corners[6].y + ")  ");
        System.out.println("(" + corners[3].x + "," + corners[3].y + ")");
        System.out.print("(" + corners[5].x + "," + corners[5].y + ")  ");
        System.out.println("(" + corners[4].x + "," + corners[4].y + ")");
    }
}