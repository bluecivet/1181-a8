public class User
{
    private boolean loose;
    private boolean thisTerm;
    private byte count;  // count the number of selected card one user should the max number is 2

    //-------------------------------------------------------------------------


    public boolean isLoose()
    {
        return loose;
    }

    public void setLoose(boolean loose)
    {
        this.loose = loose;
    }

    public boolean isThisTerm()
    {
        return thisTerm;
    }

    public void setThisTerm(boolean thisTerm)
    {
        this.thisTerm = thisTerm;
    }

    public byte getCount()
    {
        return count;
    }

    public void setCount(byte count)
    {
        this.count = count;
    }
}
