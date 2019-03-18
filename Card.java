import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Card extends JPanel
{
    static final int DEFAULT_HEIGHT = 20;
    static final int DEFAULT_WIDTH = 10;
    int x,y;
    private int width, height;
    private Image img;
    private boolean isLoose;
    private boolean selected;

    //----------------------------------------------------------------------------------------



    public Card()
    {
       width = DEFAULT_WIDTH;
       height = DEFAULT_HEIGHT;
       ImageIcon imageIcon = new ImageIcon("unSelect.jpg");
       img = imageIcon.getImage();
    }



    //----------------------------------------------------------------------------------------

    @Override
    public void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        //System.out.println("the width is " + width + " the height is " + height);
        g2.drawImage(img, 0, 0, width, height, null);
    }



    //-----------------------------------------------------------------------------------------
     // getter and setter

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public int getX()
    {
        return x;
    }


    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public Image getImg()
    {
        return img;
    }

    public void setImg(Image img)
    {
        this.img = img;
    }

    public boolean isLoose()
    {
        return isLoose;
    }

    public void setLoose(boolean loose)
    {
        isLoose = loose;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }


    //--------------------------------------------------------------------------------------
    //  end getter and setter
}
