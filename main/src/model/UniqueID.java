package model;

public class UniqueID {
    public Integer ID;
    private static int uniqueID = 1;

    public UniqueID()
    {
        this.ID = uniqueID;
        nextID();
    }

    public void editUniqueID(Integer value)
    {
        this.ID = value;
    }

    @Override
    public String toString()
    {
        return String.valueOf(ID);
    }

    public static int nextID(){
        return uniqueID++;
    }

    public static int revertID(){
        return uniqueID--;
    }

    public static void resetID(){
        uniqueID = 1;
    }

    public static int getUniqueID(){
        return uniqueID;
    }
}
