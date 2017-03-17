
/*Represents an item which is contained in a cataloge or cart.*/

import java.text.NumberFormat;
import java.io.Serializable;

public class Product implements Serializable
{
    private String name;
    private double price;
    
  
    //  Create a new item with the given attributes.
    public Product (String name, double price)
    {
        this.name = name;
        this.price = price;
    }

    //   Return a string with the information about the Product
    public String toString ()
    {
        NumberFormat fmt = NumberFormat.getCurrencyInstance();
        return (name + "\t\t    " + fmt.format(price));
    }

    //   Returns the unit price of the Product
    public double getPrice()
    {
        return price;
    }

    //   Returns the name of the Product
    public String getName()
    {
        return name;
    }
    // sets the name of the Product
    public void setName(String name)
    {
    	this.name = name;
    }
    // sets the Price of the Product
    public void setPrice(double price)
    {
    	this.price = price;
    }
}//end of class Product  