package golfgame;
import java.awt.*;
import java.awt.event.*;
import static java.lang.Math.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener, MouseListener{

	static final int SCREEN_WIDTH = 1200;
	static final int SCREEN_HEIGHT = 800;
	static final int DELAY = 150;
        
        public class Ball {
            double x = 20;
            double y = 7;
            double speedX = 0.0;
            double speedY = 0.0;
            int width = 20;
            int height = 20;
        }
        
        public class Gravity {
            int x = 256;
            int y = 56;
            int width = 100;
            int height = 100;
        }
        
        public class Mouse {
            int x1;
            int y1;
            int x2;
            int y2;
        }
        
        Image earth;
        Image asteroid;
        boolean mousePress = false;
        
	Timer timer;
        Random rand = new Random();
	
        public class Hole {
            int x = 70;
            int y = 70;
            int width = 50;
            int height = 50;
        }
        
        Ball ball = new Ball();
        Mouse mouse = new Mouse();
        Hole hole = new Hole();
        Gravity star = new Gravity();
        
	GamePanel(){
            this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
            this.setBackground(Color.BLACK);
            this.setFocusable(true);
            addMouseListener(this);
            //Runs initalization proccess
            loadImages();
            startGame();
	}
    public void loadImages() {
        ImageIcon iia = new ImageIcon(getClass().getResource("/res/earth.png"));
        earth = iia.getImage();

        ImageIcon iib = new ImageIcon(getClass().getResource("/res/asteroid.png"));
        asteroid = iib.getImage();
    }
	public void startGame() {
            timer = new Timer(DELAY,this);
            timer.start();
	}
        @Override
	public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
	}
	public void draw(Graphics g) {
            g.setColor(new Color(242, 235, 194));
            g.fillOval(star.x, star.y, star.width, star.height);

            g.setColor(Color.BLACK);
            //Draws Hole
            g.drawImage(earth, hole.x, hole.y, this);


            g.setColor(Color.WHITE);
            //Draws Player 1
            g.drawImage(asteroid, (int)ball.x, (int)ball.y, this);


            if (this.getMousePosition() != null) {
                mouse.x2 = this.getMousePosition().x;
                mouse.y2 = this.getMousePosition().y;
            }

            //Draws Target Line
            if (mousePress) {
                g.drawLine(mouse.x1, mouse.y1, mouse.x2, mouse.y2);
            }
	}
	public void gameLoop() {
            //Increment
            if (abs(ball.speedX) > 0) {
                ball.speedX = 0.999 * ball.speedX;
            }
            if (abs(ball.speedY) > 0) {
                    ball.speedY = 0.999 * ball.speedY;
            }

            if (abs(ball.speedX) < 0.01) {
                ball.speedX = 0.0;
            }
            if (abs(ball.speedY) < 0.01) {
                ball.speedY = 0.0;
            }

            //Collision with left wall
            if (ball.x < 0) {
                ball.x = 0;
                ball.speedX = -ball.speedX;
            }

            //Collision with right wall
            if (ball.x > SCREEN_WIDTH - ball.width) {
                ball.x = SCREEN_WIDTH - ball.width;
                ball.speedX = -ball.speedX;
            }

            //Collision with ceiling
            if (ball.y < 0) {
                ball.y = 0;
                ball.speedY = -ball.speedY;
            }

            //Collision with floor
            if (ball.y > SCREEN_HEIGHT - ball.height) {
                ball.y = SCREEN_HEIGHT - ball.height;
                ball.speedY = -ball.speedY;
            }

            if (((int)ball.x + ball.width >= hole.x && (int)ball.x <= hole.x + hole.width) && ((int)ball.y + ball.height >= hole.y && (int)ball.y <= hole.y + hole.height)) {
                newGame();
            }

            //ball.speedX += (0.001 * (100 * 0.858407 * (star.height * star.width) / ((abs(star.width/2 + star.x - ball.x) * (star.width/2 + star.x - ball.x)) + (abs(star.height/2 + star.y - ball.y) * (star.height/2 + star.y - ball.y))))) *  sin(atan(((star.width/2 + star.x) - ball.x) / ((star.height/2 + star.y) - ball.y)));
            //ball.speedY += (0.001 * (1 / ((abs(star.width/2 + star.x - ball.x) * (star.width/2 + star.x - ball.x)) + (abs(star.height/2 + star.y - ball.y) * (star.height/2 + star.y - ball.y))))) *  cos(atan(((star.width/2 + star.x) - ball.x) / ((star.height/2 + star.y) - ball.y)));
            
            ball.speedX += 0.0000004 * 0.858407 * (star.height * star.width) * (star.width/2 + star.x - ball.x);
            ball.speedY += 0.0000004 * 0.858407 * (star.height * star.width) * (star.height/2 + star.y - ball.y);
            
            ball.x+=ball.speedX;
            ball.y+=ball.speedY;
	}
        
    public void newGame() {
        ball.speedX = 0.0;
        ball.speedY = 0.0;
        
        ball.x = rand.nextInt(SCREEN_WIDTH - ball.width);
        ball.y = rand.nextInt(SCREEN_HEIGHT - ball.height);
        
        hole.x = rand.nextInt(SCREEN_WIDTH - hole.width);
        hole.y = rand.nextInt(SCREEN_HEIGHT - hole.height);
        
        star.x = rand.nextInt(SCREEN_HEIGHT - star.width);
        star.y = rand.nextInt(SCREEN_HEIGHT - star.width);
        
        star.width = rand.nextInt(300 - 30) + 30;
        star.height = star.width;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
        gameLoop();
		repaint();
	}

    @Override
    public void mousePressed(MouseEvent e) {
        mousePress = true;
        mouse.x1 = e.getX();
        mouse.y1 = e.getY();
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        mousePress = false;
        mouse.x2 = e.getX();
        mouse.y2 = e.getY();

        ball.speedX += 0.1 * (mouse.x1 - mouse.x2);
        ball.speedY += 0.1 * (mouse.y1 - mouse.y2);
        }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }
}