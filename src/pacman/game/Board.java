package pacman.game;

import javax.swing.*;
import java.awt.*;

public class Board extends JPanel {

    private Dimension d;
    private final Font smallFont = new Font("Helvetica", Font.BOLD, 14);
    private Image ii;
    private final Color dotColor = new Color(192, 192, 0);
    private Color mazeColor;

    private boolean inGame = true;
    private boolean dying = false;

    private final int BLOCK_SIZE = 14;
    private final int N_BLOCKS = 15;
    private final int SCREEN_SIZE = N_BLOCKS*BLOCK_SIZE;
    private final int PAC_ANIM_DELAY = 2;
    private final int PACMAN_ANIM_COUNT = 4;
    private final int MAX_GHOSTS = 12;
    private final int PACMAN_SPEED = 6;

    private int pacmanAnimCount = PAC_ANIM_DELAY;
    private int pacAnimDir = 1;
    private int pacmanAnimPos = 0;
    private int N_GHOSTS = 6;
    private int pacsLeft, score;
    private int[] dx, dy;
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;

    private int pacman_x, pacman_y, pacman_dx;
    private int req_dx, req_dy, view_dx, view_dy;

    private final short levelData[] = {
            19, 26, 26, 26, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
            21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20,
            21, 0, 0, 0, 17, 16, 16, 24, 16, 16, 16, 16, 16, 16, 20,
            17, 18, 18, 18, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 20,
            17, 16, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 16, 24, 20,
            25, 16, 16, 16, 24, 24, 28, 0, 25, 24, 24, 16, 20, 0, 21,
            1, 17, 16, 20, 0, 0, 0, 0, 0, 0, 0, 17, 20, 0, 21,
            1, 17, 16, 16, 18, 18, 22, 0, 19, 18, 18, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 20, 0, 17, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 16, 18, 16, 16, 16, 16, 20, 0, 21,
            1, 17, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0, 21,
            1, 25, 24, 24, 24, 24, 24, 24, 24, 24, 16, 16, 16, 18, 20,
            9, 8, 8, 8, 8, 8, 8, 8, 8, 8, 25, 24, 24, 24, 28
    };

    private final int validSpeeds[] = { 1, 2, 3, 4, 5, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    public Board () {
        initVariables();
        initBoard();

    }

    private void initBoard(){
        setFocusable(true);
        setBackground(Color.black);
    }

    private void initVariables() {
        screenData = new short [N_BLOCKS*N_BLOCKS];
        mazeColor = new Color(5, 100, 5);
        d = new Dimension(400, 400);
        ghost_x = new int [MAX_GHOSTS];
        ghost_dx = new int [MAX_GHOSTS];
        ghost_y = new int [MAX_GHOSTS];
        ghost_dy = new int [MAX_GHOSTS];
        ghostSpeed = new int [MAX_GHOSTS];
        dx = new int [4];
        dy = new int [4];
    }

    @Override
    public void addNotify() {
        super.addNotify();

        initGame();
    }

    public void initGame() {
        pacsLeft = 3;
        score = 0;
        initLevel();
        N_GHOSTS = 6;
        currentSpeed = 3;
    }

    private void initLevel() {
        int i;
        for(i=0; i<N_BLOCKS*N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }
    }

    private void drawMaze (Graphics2D g2d){
        short i = 0;
        int x, y;

        for(y=0; y < SCREEN_SIZE; y += BLOCK_SIZE){
            for(x=0; x < SCREEN_SIZE; x += BLOCK_SIZE){
                g2d.setColor(mazeColor);
                g2d.setStroke(new BasicStroke(2));

                if((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE-1);
                }

                if((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + BLOCK_SIZE-1, y );
                }

                if((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + BLOCK_SIZE-1, y, x + BLOCK_SIZE-1, y + BLOCK_SIZE - 1);
                }

                if((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + BLOCK_SIZE-1, x + BLOCK_SIZE-1, y + BLOCK_SIZE-1 );
                }

                if((screenData[i] & 16) != 0) {
                    g2d.setColor(dotColor);
                    g2d.fillRect(x + 11, y + 11, 2, 2);
                }

                i++;

            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        g2d.drawImage(ii, 5, 5, this);
        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
}
