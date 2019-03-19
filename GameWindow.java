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
 *  the left part and right part will both have two button which are the quit button and the done button
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
     * the goal for tha class is to redraw the right size for each component when the window is resize
     */

    class ResizeListener extends ComponentAdapter implements WindowStateListener
    {
        /**
         * override the method in the ComponentAdapter
         * this is the method when the window is resize then redraw the thing in the window
         * @param e
         */
        @Override
        public void componentResized(ComponentEvent e)
        {
            reSize();
        }

        /**
         * overrige the method in the WindowStatedListener
         * the goal for this method is to redraw thing in the window when the window reach the max size
         * @param e
         */
        @Override
        public void windowStateChanged(WindowEvent e)
        {
            reSize();
        }

        /**
         * this method redraw the component in the window
         * set the size of the left, right and top part
         * the left and right part is the 1/8 part of the width of window
         * the tip part is the 1 / 16 part of the height of the window
         */
        public void reSize()
        {
            Dimension size = new Dimension(getWidth() >> 3,getHeight() >> 3);
            leftPanel.setPreferredSize(size);
            rightPanel.setPreferredSize(size);
            userMessage.setPreferredSize(new Dimension(getWidth(), getHeight() >> 4));
        }

    }



    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * thei is the class extends MouseAdapter
     * the entity for this class is user for the card
     * when the user put the mouse the the card the image of the card will become bigger
     * when the user exit the area of the card the image will resize as before
     * when the user click the image of the card will change and also determine who's term are going
     * if the loose card are click the game will be end
     */
    class MouseAction extends MouseAdapter
    {
        /**
         * this method override the method in MouseAdapter
         * when the mouse enter the area of the card the image of the card will become larger
         * @param e
         */
        @Override
        public void mouseEntered(MouseEvent e)
        {
            Card card = getCard(e);

            if(!card.isSelected())
            {
                card.setWidth(card.getWidth() + (desk.getWidth()>>5));
                card.setHeight(card.getHeight() + (desk.getHeight() >> 5));
                card.setBounds(card.getX(),card.getY(),card.getWidth(),card.getHeight()); // change the size
                card.repaint();
            }
            // if the card is already selected donot do anything

        }


        //iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii

        /**
         * this method override the method in MouseAdapter
         * when the mouse exit the area of the card the image of the card will become normal size
         */
        @Override
        public void mouseExited(MouseEvent e)
        {
            Card card = getCard(e);

            if(!card.isSelected())
            {
                card.setWidth(card.getWidth() - (desk.getWidth()>>5));
                card.setHeight(card.getHeight() - (desk.getHeight() >> 5));
                card.setBounds(card.getX(),card.getY(),card.getWidth(),card.getHeight());  // change the size
                card.repaint();
            }
            else  // if the card is selected
            {
                mainMessage.setText("");   //clear the top message
            }

        }


        //iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii

        /**
         * his method override the method in MouseAdapter
         * the goal for this method is when the mouse click the card the card's image will change if the card if not selected
         * if the card is select a error message will show in the top
         * if the user click the loose card the game will over and tell the winner
         * when the user select 2 card the user term will change automately
         * @param e the MouseEvent which will create by card
         */
        public void mouseClicked(MouseEvent e)
        {

            Card card = getCard(e);

            if(!card.isSelected())  // if the card is not selected
            {
                if(user1.isThisTerm())  // if it is user1 term
                    user1.setCount((byte)(user1.getCount() + 1));  // add a count to user1 which mean how many card user1  is selected
                else if(user2.isThisTerm())  // if it is user2 term
                    user2.setCount((byte)(user2.getCount() + 1));     // add a count to user2 which mean how many card user2  is selected

                if(!card.isLoose())  // if this is not a loose card
                {
                    /*
                      change the image to selected
                      because the mouse will have to enter the area of the card
                      so the card will become large
                      but when will it change the image i want to original size when it change
                      so i resize the image
                     */
                    card.setWidth(card.getWidth() - (desk.getWidth()>>5));
                    card.setHeight(card.getHeight() - (desk.getHeight() >> 5));
                    card.setBounds(card.getX(),card.getY(),card.getWidth(),card.getHeight());   // set size
                    card.setImg(new ImageIcon("selected.png").getImage());    // change image
                    card.repaint();
                    card.setSelected(true);  // now this card is selected
                }
                else  // if the card is the loose card
                {
                    // change the image of the card to loose image
                    card.setImg(new ImageIcon("loose.jpg").getImage());
                    card.repaint();
                    card.setSelected(true);
                    endGame(user2,user1);    // reverse the order so that report the winner is correct
                }

            }
            else  // if the user click the card is selected
            {
                // show the warning message
                mainMessage.setText("the card is selected");
            }

            termChange(); // determine the term chang or not base on the number of card selected

        }  // end method mouseClick


       //iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii

        /**
         * the mehtod is to get the card from the event source
         * @param e a MouseEvent t
         * @return
         */
        public Card getCard(MouseEvent e)
        {
            // if the event source's class  is same as the class of the card
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

    /**
     * this class implements ActionListener
     * the class override the actionPerformed method
     * the entity of the class will attach to the done button on both left and right side
     * when the done button is click term will exchange
     * but if the user donot selected any card there will a warning message
     * but the user can only click the button on their size otherwise they will get a warning message
     * and user not allow to skip their term
     */
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
                        termChange();    // change term
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
                        termChange();   // change term
                    }
                }

            }// end right button is click
        } // end mouse click method
    }  // end inner class


    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * this class implement ActionListener
     * the entiey for this class will attach to the quit button on both left and right side
     * if the quit button is click the the user that is on his term will loose
     * but the user can only click the button on their size otherwise they will get a warning message
     * after telling the winner the program will end
     */
    class QuitActionListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            if(user1.isThisTerm())  // if on usere term
            {
                if(e.getSource().equals(quitRight)) // and the button is on user2 side
                {
                    mainMessage.setText("this is not your turn cannot quit");
                }
                else
                {
                    endGame(user2, user1); // reverse the order so that report the winner is correct
                }
            }

            if(user2.isThisTerm())  // if on user2 term
            {
                if(e.getSource().equals(quitLeft)) // and the button is on user1 side
                {
                    mainMessage.setText("this is not your turn cannot quit");
                }
                else
                {
                    endGame(user2, user1); // reverse the order so that report the winner is correct
                }
            }
        } // end method

    }

      // the method that the inner class user
    //----------------------------------------------------------------------------------------------------

    /**
     * the method will exchange the term once some condition are met
     * the user select 2 card already
     * there is a signal for the user to start the tern
     * then
     * tell the user to start their term by the change of the message on left and right side
     */
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

    /**
     * this is the method for checking how many card is the user selected
     * if the user selected more than or equal 2 card
     * the signal will change
     * @param user1
     * @param user2
     */

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

    /**
     * the method will clear all the count that user selected
     * @param user1 the user
     * @param user2 the user
     */

        private void clearCount(User user1, User user2)
        {
            user1.setCount((byte) 0);
            user2.setCount((byte) 0);
        }


        ///////////////////////////////////////////////////////////////////////////

    /**
     * the method is the process the ending the game
     * it will report the winner
     * then pop up a dialog message which tell the winner
     * alse change the winner in the left or right message
     * if the user close the dialog window the program end
     * @param user1 the user
     * @param user2 ther user
     */
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

    /**
     * this is the constructor for the game
     * first create all the variable it need
     * set the window that is in the right size and right locate make is visible
     * set the layout and style for the window
     * add the listener to the component
     */
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

    /**
     * the method will create all the variable that is need for the program
     * but the listener will be create in other method
     */
    private void createVariable()
    {
        //create user
        user1 = new User();
        user2 = new User();

        // create left right top message label
        mainMessage = new JLabel("",JLabel.CENTER);
        leftMessage = new JLabel("go",JLabel.CENTER);
        rightMessage = new JLabel("stop",JLabel.CENTER);

        // create done and quit button for both side
        doneLeft = new JButton("done");
        doneRight = new JButton("done");
        quitLeft = new JButton("quit");
        quitRight = new JButton("quit");

        // create component for left right and top
        userMessage = new JPanel();
        leftPanel = new JPanel();
        rightPanel = new JPanel();

        // create background image
        backgroundImg = new ImageIcon("bg.jpg").getImage();
        background = new Background();

        // create the card and disk
        // also add the card to the desk
        createCard();
        desk = new Desk(cards);
        addCard(desk,cards);

        // set user1 will go first
        user1.setThisTerm(true);
        user2.setThisTerm(false);
    }


    /////////////////////////////////////////////////////////////////////////

    /**
     * the method will create the card
     * add the each card create to the array
     * also at the end of the method a card is randomly selected to become a loose card
     */
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

    /**
     * the method will add all the card the desk
     * @param desk the JComponent represent the desk
     * @param cards the card array that need to add
     */

    public void addCard(JComponent desk, Card[] cards)
    {
        for(Card card: cards)
        {
            desk.add(card);
        }
    }


    ////////////////////////////////////////////////////////////////////////

    /**
     * this method will set how the window and the game look like
     * set all the component to the right style
     */
    private void setStyle()
    {
        //set the size of the left right and top part
        leftPanel.setPreferredSize(new Dimension(getWidth() >> 3,getHeight() >> 3));
        rightPanel.setPreferredSize(new Dimension(getWidth() >> 3,getHeight() >> 3));
        userMessage.setPreferredSize(new Dimension(getWidth(), getHeight() >> 4));

        // the the border layout for the panel
        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());
        userMessage.add(mainMessage);

        //add components to the left Panel
        leftPanel.add(leftMessage,BorderLayout.EAST);
        leftPanel.add(doneLeft,BorderLayout.NORTH);
        leftPanel.add(quitLeft,BorderLayout.SOUTH);

        // add the components to the right Panel
        rightPanel.add(rightMessage);
        rightPanel.add(doneRight,BorderLayout.NORTH);
        rightPanel.add(quitRight,BorderLayout.SOUTH);

        // set the font size and color to the label
        leftMessage.setFont(new Font("Calibri",1,30));
        mainMessage.setFont(new Font("Calibri",1,22));
        rightMessage.setFont(new Font("Calibri",1,30));
        leftMessage.setForeground(Color.blue);
        rightMessage.setForeground(Color.cyan);
        mainMessage.setForeground(Color.yellow);

        // make the background of the Panel to transparent so that people can see the background image
        desk.setOpaque(false);
        leftPanel.setOpaque(false);
        rightPanel.setOpaque(false);
        userMessage.setOpaque(false);

        // add the component back on the background layer
        background.setLayout(new BorderLayout());
        add(background);
        background.add(desk,BorderLayout.CENTER);
        background.add(leftPanel, BorderLayout.WEST);
        background.add(rightPanel,BorderLayout.EAST);
        background.add(userMessage, BorderLayout.NORTH);

    }


    /////////////////////////////////////////////////////////////////////////

    /**
     * the method will set all the listener we need
     * and add to the component
     */
    private void setListener()
    {
        // when the window resize
        ResizeListener resizeListener = new ResizeListener();
        this.addComponentListener(resizeListener);
        this.addWindowStateListener(resizeListener);

        // when the mouse do something to the add
        // each card add the listener
        MouseAction mouseAction = new MouseAction();
        for(Card card : cards)
        {
            card.addMouseListener(mouseAction);
        }

        // add listener for done button
        DoneActionListener doneListener  =  new DoneActionListener();
        doneLeft.addActionListener(doneListener);
        doneRight.addActionListener(doneListener);

        // add listener for the quit button
        QuitActionListener quitListner = new QuitActionListener();
        quitLeft.addActionListener(quitListner);
        quitRight.addActionListener(quitListner);

    }


    /////////////////////////////////////////////////////////////////////////

    /**
     * teh method set the
     * size of the window
     * location of the window
     * title for the window
     */
    private void setWindow()
    {
        setLocation(200,200);
        setSize(600,500);
        setTitle("game");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }



} // end GameWindow class
