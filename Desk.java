import javax.swing.*;
import java.awt.*;
public class Desk extends JComponent
{

    Card[] cards;

    //---------------------------------------------------


    public Desk(Card[] cards)
    {
        this.cards = cards;
    }

    //-------------------------------------------------

    @Override
    public void paintComponent(Graphics g)
    {
        System.out.println("paint desk");
        Graphics2D g2 = (Graphics2D) g;

        paintCards(cards, g2);
    }

    /////////////////////////////////////////////


    public void paintCards(Card[] cards, Graphics2D g2)
    {
        int cellWidth = getWidth() / 5;
        int cellHeight = getHeight() >> 2;
        int marginWidth = cellWidth >> 3;
        int marginHeight = cellHeight >> 3;
        int cardX =  marginWidth;
        int cardY =  marginHeight;

        for(Card card: cards)
        {
            if(cardX >= getWidth())
            {
                cardX = marginWidth;
                cardY += cellHeight;
            }

            card.setWidth(cellWidth - 2 * marginWidth);
            card.setHeight(cellHeight- 2 * marginHeight);
            card.setX(cardX);
            card.setY(cardY);
            card.setBounds(cardX, cardY, card.getWidth(),card.getHeight());
            card.repaint();
            cardX = cardX + cellWidth;

        }
    }

}
