package game;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import utils.Value.*;

public class Card implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private value value;
	private suit suit;
	private ImageIcon icon;
	private int hash;
	
	public Card(value value, suit suit){
		if (value == null){
			setIcon("empty.png");
			return;
		}
		this.value = value;
		this.suit = suit;
		createHash();
		setIcon();
	}

	private void createHash(){
		this.hash = suit.ordinal() * utils.Value.value.values().length + value.ordinal();
	}
	
	public value getValue(){
		return this.value;
	}

	public suit getSuit(){
		return this.suit;
	}
	
	public String getName(){
		return suit.toString() + value.toString();
	}
	
	public ImageIcon getIcon(){
		return icon;
	}
	
	private Image concatenate(String path){
			BufferedImage img1, img2, img = null;
			int height = 16;
			try {
				img1 = ImageIO.read(new File(path + suit.toString() + ".png"));
				img2 = ImageIO.read(new File(path + value.toString() + ".png"));
				int widthImg1 = img1.getWidth();
				int heightImg1 = img1.getHeight();
				int widthImg2 = img2.getWidth();
				img = new BufferedImage(widthImg1 + widthImg2, heightImg1 + height, BufferedImage.TYPE_INT_ARGB);
				img.getGraphics().setColor(Color.WHITE);
				img.getGraphics().fillRect(0, 0, widthImg2 + widthImg1, heightImg1 + height);
				img.createGraphics().drawImage(img1, 0, height/2, null);
				img.createGraphics().drawImage(img2, widthImg1, height/2, null);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return img;
	}

	private void setIcon(){
		icon = new ImageIcon(concatenate("img/"));
	}

	private void setIcon(String path){
		icon = new ImageIcon("img/" + path);
	}
	
	public int getHash(){
		return this.hash;
	}
	
	public String toString(){
		return getName();
//		return "Card: " + getName();
	}
}
