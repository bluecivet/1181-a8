import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * this class is the game which have 2 player
 * the plays need to click the card on the game window
 * there is a loose card if the play select that play will loose
 * the play who quit the game will also loose
 * each term the play can only choose 1 or 2 card
 * if the player want to end their term click the done button the term will change to the other play
 * the play are not allow to skip their term
 * if the play choose 2 card already the term will change automately
 */


/**
 *  the window will contain 4 part
 *  the top part: have a label which to show some warning message
 *  the left part: have a label which to show some is the left side is going and win or not
 *  the right part: have a label which to show some is the right side is going and win or not
 *  the center part: have a desk which is the area that the uer select the card this will show by drawing the image out
 */

public class GameWindow extends JFrame
{
    private Image backgroundImg;  //the image for the  window background
    private Background background;  // extend JPanel for rewriting the paint method
    Card[] cards;

    private JLabel mainMessage;  // show in the top
    private JLabel leftMessage;
    private JLabel rightMessage;
    private JPanel userMessage;    // panel show in the top contain a label
    private JPanel leftPanel;     // show in left contain a label
    private JPanel rightPanel;    // show in the right contain a label

    JComponent desk;    // the component which contain the cards showing by drawing the image of the card
    JButton doneLeft;    // left side done button(once the user pick one the done option are avalible)
    JButton doneRight;    // right side done button(once the user pick one the done option are avalible)
    JButton quitLeft;     // the quit game option for left side once the user click he will lose
    JButton quitRight;    // the quit game option for right side once the user click he will lose

    private User user1;          //left side user
    private User user2;          // right side user


    //inner class
    //---------------------------------------------------------------------

    /**
     * this is a inner class which extends JPanel
     * the goal for this class is to use painComponent method to paint the background for the window
     * once the this class is add to JFrame it will draw the background
     */

    class Background extends JPanel
    {
        @Override
        public void paintComponent(Graphics g)
        {
            g.drawImage(backgroundImg, 0,0, getWidth(), getHeight(), null);
        }
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this is a inner class which extends ComponentAdapter and implements WindowStateListener
     * the goal for tha
     */

    class ResizeListener extends ComponentAdapter implements WindowStateListener
    {
        @Override
        public void componentResized(ComponentEvent e)
        {
            System.out.println("paing");
            reSize();
        }

        @Override
        public void windowStateChanged(WindowEvent e)
        {
            reSize();
        }

        public void reSize()
        {
            Dimension size = new Dimension(getWidth() >> 3,getHeight() >> 3);
            leftPanel.setPreferredSize(size);
            rightPanel.setPreferredSize(size);
            userMessage.setPreferredSize(new Dimension(getWidth(), getHeight() >> 4));
        }

    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    class MouseAction extends MouseAdapter
    {


        @Override
        public void mouseEntered(MouseEvent e)
        {
            System.out.println("mouse enter");
            Card card = getCard(e);

            if(!card.isSelected())
            {
                card.setWidth(card.getWidth() + (desk.getWidth()>>5));
                card.setHeight(card.getHeight() + (desk.getHeight() >> 5));
                card.setBounds(card.getX(),card.getY(),card.getWidth(),card.getHeight());
                card.repaint();
            }

        }


        //iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii


        @Override
        public void mouseExited(MouseEvent e)
        {
            Card card = getCard(e);

            if(!card.isSelected())
            {
                card.setWidth(card.getWidth() - (desk.getWidth()>>5));
                card.setHeight(card.getHeight() - (desk.getHeight() >> 5));
                card.setBounds(card.getX(),card.getY(),card.getWidth(),card.getHeight());
                card.repaint();
            }
            else  // if the card is selected
            {
                mainMessage.setText("");
            }

        }


        //iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii


        public void mouseClicked(MouseEvent e)
        {
            System.out.println("click");

            Card card = getCard(e);

            if(!card.isSelected())
            {
                if(user1.isThisTerm())
                    user1.setCount((byte)(user1.getCount() + 1));
                else if(user2.isThisTerm())
                    user2.setCount((byte)(user2.getCount() + 1));

                if(!card.isLoose())
                {
                    card.setWidth(card.getWidth() - (desk.getWidth()>>5));
                    card.setHeight(card.getHeight() - (desk.getHeight() >> 5));
                    card.setBounds(card.getX(),card.getY(),card.getWidth(),card.getHeight());
                    card.setImg(new ImageIcon("selected.png").getImage());
                    card.repaint();
                    card.setSelected(true);
                }
                else  // if the card is the loose card
                {
                    card.setImg(new ImageIcon("loose.jpg").getImage());
                    card.repaint();
                    card.setSelected(true);
                    endGame(user2,user1);    // reverse the order so that report the winner is correct
                }

            }
            else  // if the card is selected
            {
                mainMessage.setText("the card is selected");
            }

            termChange();

        }  // end method mouseClick


       //iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii


        public Card getCard(MouseEvent e)
        {
            if(e.getSource().getClass().equals(Card.class))
            {
                return (Card) e.getSource();
            }
            else
            {
                System.out.println("this is not a Card");
                return null;
            }
        }

    }  // end class


    /////////////////////////////////////////////////////////////////////////////////////////////////////


    class DoneActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(e.getSource().equals(doneLeft))  // the left button is click
            {
                if(!user1.isThisTerm())  // if it is not left side term
                {
                    mainMessage.setText("now is right side term");
                }
                else    // if it is left side term
                {
                    if(user1.getCount() < 1)  // if left user not select card
                    {
                        mainMessage.setText("you cannot skip your term");
                    }
                    else  // if left user already select card
                    {
                        user1.setThisTerm(false);
                        user2.setThisTerm(true);
                        clearCount(user1,user2);
                        termChange();
                    }
                }
            } // end left button is click

            if(e.getSource().equals(doneRight))
            {
                if(!user2.isThisTerm())
                {
                    mainMessage.setText("now is left side term");
                }
                else
                {
                    if(user2.getCount() < 1)  // if left user not select card
                    {
                        mainMessage.setText("you cannot skip your term");
                    }
                    else  // if left user already select card
                    {
                        user1.setThisTerm(true);
                        user2.setThisTerm(false);
                        clearCount(user1,user2);
                        termChange();
                    }
                }

            }// end right button is click
        } // end mouse click method
    }  // end inner class


    //////////////////////////////////////////////////////////////////////////////////////////////////////


    class QuitActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(user1.isThisTerm())
            {
                if(e.getSource().equals(quitRight))
                {
                    mainMessage.setText("this is not your turn cannot quit");
                }
                else
                {
                    endGame(user2, user1); // reverse the order so that report the winner is correct
                }
            }

            if(user2.isThisTerm())
            {
                if(e.getSource().equals(quitLeft))
                {
                    mainMessage.setText("this is not your turn cannot quit");
                }
                else
                {
                    endGame(user2, user1); // reverse the order so that report the winner is correct
                }
            }
        }

    }

      // the method that the inner class user
    //----------------------------------------------------------------------------------------------------


        public void termChange()
        {
          checkCount(user1, user2);

            if(user1.isThisTerm())
                {
                    leftMessage.setText("go");
                    rightMessage.setText("stop");
                }

                if(user2.isThisTerm())
                {
                    leftMessage.setText("stop");
                    rightMessage.setText("go");
                }
        }


    /////////////////////////////////////////////////////////////////////////////////////////////////////


        private void checkCount(User user1, User user2)
        {
            if(user1.getCount() >= 2) // when the user select 2 or more card his term is pass
            {
                clearCount(user1,user2);
                user1.setThisTerm(false);
                user2.setThisTerm(true);
            }

            if(user2.getCount() >= 2) // when the user select 2 or more card his term is pass
            {
                clearCount(user1,user2);
                user2.setThisTerm(false);
                user1.setThisTerm(true);
            }
        }


        /////////////////////////////////////////////////////////////////////////////


        private void clearCount(User user1, User user2)
        {
            user1.setCount((byte) 0);
            user2.setCount((byte) 0);
        }


        ///////////////////////////////////////////////////////////////////////////

        private void endGame(User user1, User user2)
        {
            if(user1.isThisTerm())
            {
                leftMessage.setText("Win");
                JOptionPane.showMessageDialog(this, "Game over! the Winner is left side");
                System.exit(0);
            }

            if(user2.isThisTerm())
            {
                rightMessage.setText("Win");
                JOptionPane.showMessageDialog(this, "Game over! the Winner is right side");
                System.exit(0);
            }
        }




    //---------------------------------------------------------------------
    // constructor

    GameWindow()
    {
        createVariable();
        setWindow();
        setStyle();
        setListener();
    }

    // method for constructor use
    // set up all the layout and style print the window out
    /////////////////////////////////////////////////////////////////////////


    private void createVariable()
    {
        user1 = new User();
        user2 = new User();
        mainMessage = new JLabel("",JLabel.CENTER);
        leftMessage = new JLabel("go",JLabel.CENTER);
        rightMessage = new JLabel("stop",JLabel.CENTER);
        doneLeft = new JButton("done");
        doneRight = new JButton("done");
        quitLeft = new JButton("quit");
        quitRight = new JButton("quit");
        userMessage = new JPanel();
        leftPanel = new JPanel();
        rightPanel = new JPanel();
        backgroundImg = new ImageIcon("bg.jpg").getImage();
        background = new Background();
        createCard();
        desk = new Desk(cards);
        addCard(desk,cards);

        user1.setThisTerm(true);
        user2.setThisTerm(false);
      //  game = new Thread(new PlayGame());
        //game.start();
    }


    /////////////////////////////////////////////////////////////////////////


    public void createCard()
    {
        cards = new Card[20];
        int random = 0;
        System.out.println("create card");

        for(int i = 0; i < cards.length; i++)
        {
            cards[i] = new Card();
        }

        random = (int) (Math.random() * 21);
        cards[random].setLoose(true);
    }


    ////////////////////////////////////////////////////////////////////////


    public void addCard(JComponent desk, Card[] cards)
    {
        for(Card card: cards)
        {
            desk.add(card);
        }
    }


    ////////////////////////////////////////////////////////////////////////


    private void setStyle()
    {

        leftPanel.setPreferredSize(new Dimension(getWidth() >> 3,getHeight() >> 3));
        rightPanel.setPreferredSize(new Dimension(getWidth() >> 3,getHeight() >> 3));
        userMessage.setPreferredSize(new Dimension(getWidth(), getHeight() >> 4));

        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());
        userMessage.add(mainMessage);

        leftPanel.add(leftMessage,BorderLayout.EAST);
        leftPanel.add(doneLeft,BorderLayout.NORTH);
        leftPanel.add(quitLeft,BorderLayout.SOUTH);

        rightPanel.add(rightMessage);
        rightPanel.add(doneRight,BorderLayout.NORTH);
        rightPanel.add(quitRight,BorderLayout.SOUTH);

        leftMessage.setFont(new Font("Calibri",1,30));
        mainMessage.setFont(new Font("Calibri",1,22));
        rightMessage.setFont(new Font("Calibri",1,30));
        leftMessage.setForeground(Color.blue);
        rightMessage.setForeground(Color.cyan);
        mainMessage.setForeground(Color.yellow);

        desk.setOpaque(false);
        leftPanel.setOpaque(false);
        rightPanel.setOpaque(false);
        userMessage.setOpaque(false);
        background.setLayout(new BorderLayout());

        add(background);
        background.add(desk,BorderLayout.CENTER);
        background.add(leftPanel, BorderLayout.WEST);
        background.add(rightPanel,BorderLayout.EAST);
        background.add(userMessage, BorderLayout.NORTH);

    }


    /////////////////////////////////////////////////////////////////////////


    private void setListener()
    {
        ResizeListener resizeListener = new ResizeListener();
        this.addComponentListener(resizeListener);
        this.addWindowStateListener(resizeListener);

        MouseAction mouseAction = new MouseAction();
        for(Card card : cards)
        {
            card.addMouseListener(mouseAction);
        }

        DoneActionListener doneListener  =  new DoneActionListener();
        doneLeft.addActionListener(doneListener);
        doneRight.addActionListener(doneListener);

        QuitActionListener quitListner = new QuitActionListener();
        quitLeft.addActionListener(quitListner);
        quitRight.addActionListener(quitListner);

    }


    /////////////////////////////////////////////////////////////////////////


    private void setWindow()
    {
        setLocation(200,200);
        setSize(600,500);
        setTitle("game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }



} // end GameWindow class
