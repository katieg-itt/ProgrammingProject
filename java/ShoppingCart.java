/**Uses the Product class to create items and add them
 *to a shopping cart stored in an ArrayList.
 *@author Katie Griffiths
 *@version 1.0 */
 //http://www.tutorialspoint.com/java/java_arraylist_class.htm
 
import java.util.*;
import java.text.NumberFormat;

public class ShoppingCart {
	int customer_number;
	private ArrayList<Product> cart;
	
	/** single argument constructor method
	 *@param customer_number  */
	public ShoppingCart(int customer_number) {
		this.cart = new ArrayList<Product>();
		this.customer_number = customer_number;
	}
	/**acessor method to return the customer number
	 *@return customer number */
	public int getCustomerNumber() {
		return this.customer_number;
	}
	/**Adds an item to the shopping cart
	 *@param product the product to add */
	public void addToCart(Product product) {
		this.cart.add(product);
	}
	
	/**method to search for  a mathching product in the array list and remove it
	 *@param product an instance of the product you wish to remove */
	public void removeFromCart(Product product) {
		int index = this.cart.indexOf(product);
		if(index!=-1) {
			this.cart.remove(index);
		}
	}
	/**Converts shopping cart into a String for convenient display
	 *@return a plain text representation of the cart */
	public String showCart() {
		String cartContents = "";
		NumberFormat fmt = NumberFormat.getCurrencyInstance(); 
		for(int i=0;i<this.cart.size();i++) {
			Product product = this.cart.get(i);
			cartContents = cartContents + String.format("%-30s", product.getName())+" "+fmt.format(product.getPrice())+"\n";
		}
		return cartContents;
	}
	/**Adds up price value of items in the cart
	 *@return total value */
	public double total() {
		double total=0;
		
		for(int i=0;i<this.cart.size();i++) {
			Product product = this.cart.get(i);
			total += product.getPrice();	
		}
		
		return total;
	}
}//end of Shop class