/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.tpsoft.utils.components;

import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author tomas.praslicak
 */
public class JImagedPanel extends JPanel {
    public static enum BackgroundImageType {
        STRETCHED_BOTH {
            @Override protected void draw(Graphics g, BufferedImage img, JImagedPanel panel) {
                g.drawImage(img, 0, 0, panel.getWidth(), panel.getHeight(), null);
            }
        },
        STRETCHED_AUTO {
            @Override protected void draw(Graphics g, BufferedImage img, JImagedPanel panel) {
                double relativeSize = Math.min(
                        (double)panel.getWidth() / (double)img.getWidth(),
                        (double)panel.getHeight() / (double)img.getHeight());
                int[] move = {
                    (panel.getWidth() - (int)((double)img.getWidth() * relativeSize)) / 2,
                    (panel.getHeight() - (int)((double)img.getHeight() * relativeSize)) / 2
                };
                g.drawImage(
                        img,
                        move[0],
                        move[1],
                        (int)((double)img.getWidth() * relativeSize),
                        (int)((double)img.getHeight() * relativeSize),
                        null);
            }
        },
        STRETCHED_X {
            @Override protected void draw(Graphics g, BufferedImage img, JImagedPanel panel) {
                double relativeSize = (double)panel.getWidth() / (double)img.getWidth();
                int move = (panel.getHeight() - (int)((double)img.getHeight() * relativeSize)) / 2;
                g.drawImage(
                        img,
                        0,
                        move,
                        (int)((double)img.getWidth() * relativeSize),
                        (int)((double)img.getHeight() * relativeSize),
                        null);
            }
        },
        STRETCHED_Y {
            @Override protected void draw(Graphics g, BufferedImage img, JImagedPanel panel) {
                double relativeSize = (double)panel.getHeight() / (double)img.getHeight();
                int move = (panel.getWidth() - (int)((double)img.getWidth() * relativeSize)) / 2;
                g.drawImage(
                        img,
                        move,
                        0,
                        (int)((double)img.getWidth() * relativeSize),
                        (int)((double)img.getHeight() * relativeSize),
                        null);
            }
        },
        REPEATED {
            @Override protected void draw(Graphics g, BufferedImage img, JImagedPanel panel) {
                Point pos = new Point(0, 0);
                while (pos.y < panel.getHeight()) {
                    pos.x = 0;
                    while (pos.x < panel.getWidth()) {
                        g.drawImage(img, pos.x, pos.y, null);
                        pos.x += img.getWidth();
                    }
                    pos.y += img.getHeight();
                }
            }
        };
        
        protected abstract void draw(Graphics g, BufferedImage img, JImagedPanel panel);
    }
    
    private BufferedImage image = null;
    private BackgroundImageType imageType = BackgroundImageType.STRETCHED_BOTH;

    public JImagedPanel() {
    }

    public JImagedPanel(LayoutManager layout) {
        super(layout);
    }

    public JImagedPanel(boolean isDoubleBuffered) {
        super(isDoubleBuffered);
    }

    public JImagedPanel(LayoutManager layout, boolean isDoubleBuffered) {
        super(layout, isDoubleBuffered);
    }
    
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    public void setImage(File image) {
        try {
            setImage(ImageIO.read(image));
        } catch (IOException ex) {
            this.image = null;
        }
    }
    
    public void setImage(String image) {
        setImage(new File(image));
    }

    public void setImage(BufferedImage image, BackgroundImageType type) {
        setImage(image);
        imageType = type;
    }
    
    public void setImage(File image, BackgroundImageType type) {
        setImage(image);
        imageType = type;
    }
    
    public void setImage(String image, BackgroundImageType type) {
        setImage(image);
        imageType = type;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (image != null) {
            imageType.draw(g, image, this);
        }
    }
}
