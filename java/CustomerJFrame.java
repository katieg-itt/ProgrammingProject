//Customer.java
/* This class manages the GUI and its interaction with the other classes. Some code gotten from Java API */
//import statements
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.NumberFormat;
import java.io.*;

public class CustomerJFrame extends JFrame {

	private JMenu fileMenu, cartMenu;
	private JTextField custNum;
	private JLabel prompt;
	private JButton newOrder;
	private JButton checkout;
	private JLabel imageLabel;
	private JPanel productList;
	int count;
	ArrayList<Product> catalog; //holds all avalible products in the store.
	private ShoppingCart cart = null;
	
	public static void main (String args[]){
		CustomerJFrame frame = new CustomerJFrame();
		frame.setVisible(true);
	} 
		
	public CustomerJFrame () {
		catalog = new ArrayList<Product>(); //Instantiate array list.
		
		Container cPane;
		setTitle ("Customer Order Login");
		setSize(400,400);
		setResizable (false);
		setLocation (250,200);
		
		cPane = getContentPane();
		cPane.setLayout(new FlowLayout());
		
		//adding two jlable objects
        imageLabel = new JLabel(new ImageIcon("cart2.png"));
        cPane.add(imageLabel);
		
		prompt = new JLabel("Please Press New Order & Enter Customer Number");
		cPane.add(prompt);
		
		ButtonHandler bHandle = new ButtonHandler();
		
		
		newOrder = new JButton ("New Order");
		newOrder.setActionCommand("newCart");
		cPane.add(newOrder);
		newOrder.addActionListener(bHandle);
		
		checkout = new JButton ("Checkout");
		checkout.setActionCommand("checkoutCart");
		checkout.setEnabled(false);
		cPane.add(checkout);
		checkout.addActionListener(bHandle);
		
		GridLayout grid = new GridLayout(0,3); //Makes table type layout for product list, 3 columns.
		
		this.productList = new JPanel();  //Create an empty pannel.
		this.productList.setLayout(grid); //Set the layout.
		cPane.add(this.productList);     //Puts in pane.
		
		createFileMenu();
		createCartMenu();
		loadCatalog();
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		menuBar.add(fileMenu);
		menuBar.add(cartMenu);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	
	}
	//Saves the catalog to file
	public void saveCatalog() throws IOException{
		ObjectOutputStream os;
		os = new ObjectOutputStream(new FileOutputStream("catalog.dat"));
		os.writeObject(this.catalog);
		os.close();
	}
	
	private void loadCatalog() {
		try{
			ObjectInputStream is;
			is = new ObjectInputStream(new FileInputStream ("catalog.dat"));
			this.catalog = (ArrayList<Product>)is.readObject();
			
			drawProductList();
			is.close();
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(null,"open didn't work");
			e.printStackTrace();
		}
	}
	
	//update the list of products on the main window
	private void drawProductList() {   
		this.productList.removeAll();  //empty exsiting pannel   
		NumberFormat fmt = NumberFormat.getCurrencyInstance(); 
		for(int i=0; i<this.catalog.size();i++) {  //Add 3 JComponents per product
			Product product = this.catalog.get(i);
			this.productList.add(new JLabel(product.getName()));
			this.productList.add(new JLabel(fmt.format(product.getPrice())));
			
			JButton add = new JButton("Add to Cart");
			if(CustomerJFrame.this.cart == null) {
				add.setEnabled(false);				//Java api
			}
			this.productList.add(add);
			//http://stackoverflow.com/questions/11590407/is-possible-to-add-swing-control-action-listener-dynamically
			add.setActionCommand(""+i);
			add.addActionListener(new ProductHandler());  //the ActionCommand references the index of the array list
	}
		this.productList.revalidate();  //Tells swing that the component has been updated.
		
	}

	private void createFileMenu() {
		fileMenu= new JMenu ("File");
		MenuHandler handler=new MenuHandler();
		JMenuItem item;
		item = new JMenuItem ("New Product");
		item.addActionListener(handler);
		fileMenu.add(item);
		
		item = new JMenuItem("Save Catalog");
		item.addActionListener(handler);
		fileMenu.add(item);
		
		item = new JMenuItem("Quit");
		item.addActionListener(handler);
		fileMenu.add(item);			
	}
		
	private void createCartMenu() {
		cartMenu = new JMenu ("Cart");
		MenuHandler handler=new MenuHandler();
		JMenuItem item;
		
		item = new JMenuItem ("View Cart");
		item.addActionListener(handler);
		cartMenu.add(item);
	}
		
	

	public void showMessage (JTextArea s){
		JOptionPane.showMessageDialog(null,s);
	}
	/** utility methods to make the code simpler */
	public void showMessage (String s){
		JOptionPane.showMessageDialog(null,s);
	}   
	//Add product to cataloge
	public void addProduct(){
		String name = JOptionPane.showInputDialog("What is the name of the product");
		double price = Double.parseDouble(JOptionPane.showInputDialog("What is the price of "+name));
		
		Product p = new Product(name, price);
		
		this.catalog.add(p);  //adds to main cataloge
		drawProductList();   //updates main window to include new product.
	
	}
    //This handles what happens when peple choose a menu item.  
	public class MenuHandler implements ActionListener{
	
		public void actionPerformed (ActionEvent e){
			if (e.getActionCommand() .equals ("Quit")){
				showMessage("Shutting down the system");
				System.exit(0);
			} else if (e.getActionCommand() .equals ("New Product")){
				addProduct(); 
			} else if (e.getActionCommand() .equals ("Save Catalog")){
				try{
					saveCatalog();
					showMessage("Catalog Saved");
				} catch (IOException f){
					showMessage("Not able to save the file:\n"+
      	 			"Check the console printout for clues to why ");
      	 			f.printStackTrace();
				}			
			} else if (e.getActionCommand() .equals ("View Cart")){
				String cart_contents = CustomerJFrame.this.cart.showCart();
				showMessage(cart_contents);
			} else if (e.getActionCommand() .equals("")){
				showMessage("Cart is empty");
			} else
				showMessage(e.getActionCommand()+" menu item not handled");
		} // actionPerformed
	}	
		
	//This handles clicking on the add to cart buttons.	
	public class ProductHandler implements ActionListener{
		public void actionPerformed(ActionEvent e){
			// disable button if cart has not been created
			if(CustomerJFrame.this.cart == null) {
				showMessage("Please create a new order first");
			} else {
				Product product = CustomerJFrame.this.catalog.get(Integer.parseInt(e.getActionCommand()));
				CustomerJFrame.this.cart.addToCart(product);
				showMessage(product.getName()+" added to cart");
			}
		}	 
	}
	//http://www.tutorialspoint.com/java/java_arraylist_class.htm	
	// This handles the new order button	
	public class ButtonHandler implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("newCart")) {
				// Create a new cart, or destroy the cart, depending on status
				if(CustomerJFrame.this.cart == null) {
					int customerNumber = Integer.parseInt(JOptionPane.showInputDialog("What is the customer number?"));
					CustomerJFrame.this.cart = new ShoppingCart(customerNumber);
					CustomerJFrame.this.newOrder.setText("Cancel Order");
					CustomerJFrame.this.checkout.setEnabled(true);
				} else {
					CustomerJFrame.this.cart = null;
					CustomerJFrame.this.newOrder.setText("New Order");
					CustomerJFrame.this.checkout.setEnabled(false);
				}
				
				
				CustomerJFrame.this.drawProductList();  // Update list with enabled/disabled buttons
			} else {
				NumberFormat fmt = NumberFormat.getCurrencyInstance(); 
				int customer_number = CustomerJFrame.this.cart.getCustomerNumber();
				String order_total = fmt.format(CustomerJFrame.this.cart.total());
				String cart_contents = CustomerJFrame.this.cart.showCart();
				
				showMessage("The order for customer #"+customer_number+ " \t\tis " + order_total +"\n"+cart_contents);
				showMessage("Your order has been processed. Thank You ( * u * )");
			}
		}
	}
}