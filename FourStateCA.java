import java.awt.*;
import javax.swing.*;

public class FourStateCA {
    final static int N = 100;
    final static int cellSize = 2;
    final static int delay = 50;
    static int[][] state = new int[N][N];
    static boolean[][] excitedNeighbour = new boolean[N][N];
    static int[][] timeToStateChange = new int[N][N];
    static Display display = new Display();

    final static int resting = 0;
    final static int waveBack = 1;
    final static int plateau = 2;
    final static int waveFront = 3;

    public static void main(String args[]) throws Exception {
        // Define initial state - excited bottom row / resting elsewhere.
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
            state[i][j] = j == N - 1 ? waveFront : resting;
            timeToStateChange[i][j] = 0;
            }
        }

        // Define initial state - top right / resting elsewhere.
       /* for(int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (i == N - 1 && j == 0) {
                    state[i][j] = waveFront;
                    timeToStateChange[i][j] = 0;
                } else {
                    state[i][j] = resting;
                    timeToStateChange[i][j] = 0;
                }
            }
        }*/


        display.repaint();
        pause();

        //main loop
        int iter = 0;
        while(true) {
            System.out.println("iter = " + iter++);

            //spiral
            if (iter == N / 2) {
                for (int i = 0; i < N / 2; i++) {
                    for (int j = 0; j < N; j++) {
                        state[i][j] = resting;
                        timeToStateChange[i][j] = 0;
                    }
                }
            }

            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    //find neighbours
                    int ip = Math.min(i + 1, N - 1);
                    int im = Math.max(i - 1, 0);

                    int jp = Math.min(j + 1, N - 1);
                    int jm = Math.max(j - 1, 0);

                    excitedNeighbour[i][j] =
                            state[im][jm] >= waveFront || state[i ][jm] >= waveFront || state[ip][jm] >= waveFront ||
                            state[im][j ] >= waveFront ||                               state[ip][j ] >= waveFront ||
                            state[im][jp] >= waveFront || state[i ][jp] >= waveFront || state[ip][jp] >= waveFront;
                }
            }

            //update state
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    switch (state[i][j]) {
                        case resting:
                            if (excitedNeighbour[i][j]) {
                                state[i][j] = waveFront;
                                timeToStateChange[i][j] = 2;
                            }
                            break;
                        case waveBack:
                            if (timeToStateChange[i][j] == 0) {
                                state[i][j] = resting;
                                timeToStateChange[i][j] = 0;
                            } else {
                                timeToStateChange[i][j]--;
                            }
                            break;
                        case plateau:
                            if (timeToStateChange[i][j] == 0) {
                                state[i][j] = waveBack;
                                timeToStateChange[i][j] = 3;
                            } else {
                                timeToStateChange[i][j]--;
                            }
                            break;
                        case waveFront:
                            if (timeToStateChange[i][j] == 0) {
                                state[i][j] = plateau;
                                timeToStateChange[i][j] = 3;
                            } else {
                                timeToStateChange[i][j]--;
                            }
                            break;
                    }
                }
            }
            display.repaint();
            pause();
        }
    }



    static class Display extends JPanel {

        final static int WINDOW_SIZE = N * cellSize;

        Display() {

            setPreferredSize(new Dimension(WINDOW_SIZE, WINDOW_SIZE));

            JFrame frame = new JFrame("Excitable media model");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(this);
            frame.pack();
            frame.setVisible(true);
        }

        public void paintComponent(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, WINDOW_SIZE, WINDOW_SIZE);
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    if (state[i][j] > 0) {
                        if (state[i][j] == 2) {
                            g.setColor(Color.BLACK);
                        } else if (state[i][j] == 3) {
                            g.setColor(Color.GRAY);
                        } else {
                            g.setColor(Color.BLUE);
                        }
                        g.fillRect(cellSize * i, cellSize * j,
                                cellSize, cellSize);
                    }
                }
            }
        }
    }

    static void pause() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
