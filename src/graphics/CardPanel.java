package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import cards.Card;
import logic.*;

public class CardPanel extends JPanel {
    private final String cardImagesPath = "src/cardImages/";
    private final String cardImageExtension = ".png";
    private BufferedImage image;
    private Card card;
    private int imageWidth = 200;
    private int imageHeight;

    public CardPanel(Card card) {
        super();
        this.card = card;
        this.setOpaque(false);
        loadImage();
        setPreferredSize(new Dimension(imageWidth, imageHeight));
    }

    public CardPanel(Card card, int width) {
        super();
        this.card = card;
        this.setOpaque(false);
        setImageWidth(width);
        loadImage();
        setPreferredSize(new Dimension(imageWidth, imageHeight));
    }

    private void loadImage() {
        try {
            String imagePath = setImagePath();
            image = ImageIO.read(new File(imagePath));
            scaleImage();
        } catch (IOException e) {
            image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        }
    }

    private String setImagePath() {
        if (card.isFaceUp()) {
            return cardImagesPath + card.toString() + cardImageExtension;
        } else {
            return cardImagesPath + "back" + cardImageExtension;
        }
    }

    private void scaleImage() {
        if (image != null) {
            double ratio = (double) imageWidth / image.getWidth();
            imageHeight = (int) (image.getHeight() * ratio);
            Image scaledImage = image.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, this);
            g2d.dispose();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
        loadImage();
        repaint();
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }
}
