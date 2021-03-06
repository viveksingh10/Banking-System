package Admin;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.sqlconnect;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import userDom.SplitTheName;

public class UsersController implements Initializable  {
	
	@FXML 
	private Label lab_name;
	public void setName(String name) {
		lab_name.setText(name);
	}
	
    @FXML
    private TableView<user> table_user;

    @FXML
    private TableColumn<user, Integer> col_anumber;

    @FXML
    private TableColumn<user, String> col_name;

    @FXML
    private TableColumn<user, String> col_gender;

    @FXML
    private TableColumn<user, String> col_dob;

    @FXML
    private TableColumn<user, String> col_idno;

    @FXML
    private TableColumn<user, String> col_email;

    @FXML
    private TableColumn<user, String> col_mnumber;

    @FXML
    private TableColumn<user, String> col_username;

    
    @FXML
    private TextField txt_username;

    @FXML
    private TextField txt_id;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_gender;
    
    @FXML
    private TextField txt_name;
    
    @FXML
    private TextField txt_anumber;
    
    @FXML
    private TextField txt_mnumber;
    
    ObservableList<user> listM;
    
    int index = -1;
    
    Connection conn = null;
    ResultSet rs = null;
    PreparedStatement ps = null;
    Statement stmt;
    
    public void Add_user(){
    	if(txt_anumber.getText().trim().isEmpty()){

        	//*************************** new account created by admin ****************************************
        	
        	if(!(txt_name.getText().trim().isEmpty() && txt_gender.getText().trim().isEmpty()&& txt_id.getText().trim().isEmpty() && txt_email.getText().trim().isEmpty() && txt_username.getText().trim().isEmpty())){
        		conn = sqlconnect.dbconnect();
        		SplitTheName.split(txt_name.getText());
        		try {
        			ps = conn.prepareStatement("insert into user(firstname, lastname,gender, dob, id, address, emailid, mobileno,username,password, datetime) values(?,?,?,?,?,?,?,?,?,?, datetime('now','localtime'))");
    			
        			ps.setString(1, SplitTheName.getFirstname());
        			ps.setString(2, SplitTheName.getLastname());
        			ps.setString(3, txt_gender.getText());
        			ps.setString(4, "Nil");
        			ps.setString(5, txt_id.getText());
        			ps.setString(6, "Nil");
        			ps.setString(7, txt_email.getText());
        			ps.setString(8, txt_mnumber.getText());
        			ps.setString(9, txt_username.getText());
        			ps.setString(10, "0000");
    			
        			ps.executeUpdate();
    			
        			//************** both the tables are created here i.e, user balance table and user txn table
        			try {
        				ps = conn.prepareStatement("select * from user where username = ?");
        				ps.setString(1, txt_username.getText());
        				rs = ps.executeQuery();
        				int accno = rs.getInt("accno");
    			
        				// create table balance
        				String tablebalance ="CREATE TABLE IF NOT EXISTS " + txt_username.getText()+accno + " (balance real default 0.0);";
        				try {
        					Statement stmt = conn.createStatement();
        					stmt.execute(tablebalance);
        					stmt.execute("insert into " + txt_username.getText()+accno + "  values(0.0)");
    				
        				} catch (SQLException e) {
        					
        					JOptionPane.showMessageDialog(null, e);
        				}
    			
        				// create table trx
        				String tabletrx ="create table IF NOT EXISTS " + "trx"+txt_username.getText()+accno + " (date text, remarks text, type text, amount real, balance real)";
        				try {
        					Statement stmt = conn.createStatement();
        					stmt.execute(tabletrx);
    				
        				} catch (SQLException e) {
        					
        					JOptionPane.showMessageDialog(null, e);
        				}
    			
        			}catch(Exception e) {
        				JOptionPane.showMessageDialog(null, "inside create table"+e);
        			}
    			
        			JOptionPane.showMessageDialog(null, "User Account created. Note user Default password is '0000'");
    			
        			Update();
    			
        			txt_id.setText("");
        			txt_username.setText("");
        			txt_gender.setText("");
        			txt_email.setText("");
        			txt_name.setText("");
        			txt_mnumber.setText("");
        			txt_anumber.setText("");
        			conn.close();
        		} catch (SQLException e) {
        			
        			JOptionPane.showMessageDialog(null, e);
        		}
        	}else {
        		JOptionPane.showMessageDialog(null,"Please enter all the details, except Account No.(it will auto-generate).");
        	}
    	}else{
    		JOptionPane.showMessageDialog(null, "Please enter new details by clearing the previous data using clear button before adding new user.");
    	}
    	
    }
    
    public void clear() {
    	txt_anumber.setText(null);
    	txt_name.setText(null);
    	txt_gender.setText(null);
    	txt_email.setText(null);
    	txt_id.setText(null);
    	txt_mnumber.setText(null);
    	txt_username.setText(null);
    
    }
    
    public void getSelected(MouseEvent event) {
    	index = table_user.getSelectionModel().getSelectedIndex();
    	if(index <= -1) {
    		return;
    	}
    	txt_id.setText(col_idno.getCellData(index).toString());
    	txt_username.setText(col_username.getCellData(index).toString());
    	txt_gender.setText(col_gender.getCellData(index).toString());
    	txt_email.setText(col_email.getCellData(index).toString());
    	txt_name.setText(col_name.getCellData(index).toString());
    	txt_anumber.setText(col_anumber.getCellData(index).toString());
    	txt_mnumber.setText(col_mnumber.getCellData(index).toString());
    	
    }
    
    public void Edit(ActionEvent event){
    	if(!txt_anumber.getText().isEmpty()){
    		try{
    			SplitTheName.split(txt_name.getText());
    			conn = sqlconnect.dbconnect();
    			String id = txt_id.getText();
    			String username = txt_username.getText();
    			String gender = txt_gender.getText();
    			String email = txt_email.getText();
    			String firstname = SplitTheName.getFirstname();
    			String lastname = SplitTheName.getLastname();
    			String anumber = txt_anumber.getText();
    			String mnumber = txt_mnumber.getText();
    		
    			ps = conn.prepareStatement("update user set firstname = ? , lastname = ? , gender = ? , id = ?, emailid = ? , mobileno = ? , username = ? where accno = ?  ");
    			ps.setString(1, firstname);
    			ps.setString(2, lastname);
    			ps.setString(3, gender);
    			ps.setString(4, id);
    			ps.setString(5, email);
    			ps.setString(6, mnumber);
    			ps.setString(7, username);
    			ps.setString(8, anumber);
    			ps.execute();
    			
    			JOptionPane.showMessageDialog(null, "Successfully Updated");
    		
    			Update();
    		
    			txt_id.setText("");
    			txt_username.setText("");
    			txt_gender.setText("");
    			txt_email.setText("");
    			txt_name.setText("");
    			txt_mnumber.setText("");
    			txt_anumber.setText("");
    		
    			conn.close();
    		}catch(SQLException e) {
    			JOptionPane.showMessageDialog(null, e);
    		}
    	}else {
    		JOptionPane.showMessageDialog(null, "Please select a account befour prociding.");
    	}
    	txt_anumber.setText("");
    }
    
    @FXML
    public void Delete_user(ActionEvent event){
    	if(!txt_anumber.getText().isEmpty() && index != -1) {
    		conn = sqlconnect.dbconnect();
    	
    		try {
    		
    			ps = conn.prepareStatement("delete from user where accno = ?");
    			ps.setInt(1, col_anumber.getCellData(index));			
    			ps.executeUpdate();
			
    			stmt = conn.createStatement();
    			stmt.execute("drop table " + col_username.getCellData(index)+col_anumber.getCellData(index));
				stmt.execute("drop table trx"+ col_username.getCellData(index)+col_anumber.getCellData(index) );
				JOptionPane.showMessageDialog(null, "Deleted Successfully");
			
				Update();
			
				txt_id.setText("");
				txt_username.setText("");
				txt_gender.setText("");
				txt_email.setText("");
				txt_name.setText("");
				txt_mnumber.setText("");
				txt_anumber.setText("");
			
				conn.close();
    		} catch (SQLException e) {
    			
    			JOptionPane.showMessageDialog(null, e);
    		}
    	}else {
    		JOptionPane.showMessageDialog(null, "Please select a account befour prociding.");
    	}
    	txt_anumber.setText("");
    	index = -1;
    }
    
    public void Update() {
    	
    	col_anumber.setCellValueFactory(new PropertyValueFactory<user, Integer>("accno"));
		col_name.setCellValueFactory(new PropertyValueFactory<user, String>("name"));
		col_gender.setCellValueFactory(new PropertyValueFactory<user, String>("gender"));
		col_dob.setCellValueFactory(new PropertyValueFactory<user, String>("dob"));
		col_idno.setCellValueFactory(new PropertyValueFactory<user, String>("idno"));
		col_email.setCellValueFactory(new PropertyValueFactory<user, String>("email"));
		col_mnumber.setCellValueFactory(new PropertyValueFactory<user, String>("mobileno"));
		col_username.setCellValueFactory(new PropertyValueFactory<user, String>("username"));
		
		listM = sqlconnect.getDatauser();
		table_user.setItems(listM);
    }
    
	public void exxit(ActionEvent event) {
		System.exit(0);	
		}

    public void out(ActionEvent event) {		/////************ logout
	try {
		
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/application/BankingSystem.fxml").openStream());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
		
	} catch (Exception e) {
		
	}
}

    public void out1(ActionEvent event) {		////******* go back to admin panel
	try {
		
		((Node)event.getSource()).getScene().getWindow().hide();
		Stage primaryStage = new Stage();
		FXMLLoader loader = new FXMLLoader();
		Pane root = loader.load(getClass().getResource("/Admin/AdminPanel.fxml").openStream());
		
		AdminPanelController adminpanel = (AdminPanelController)loader.getController();
		adminpanel.SetAdminName(lab_name.getText());
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.initStyle(StageStyle.TRANSPARENT);
		primaryStage.show();
		
	} catch (Exception e) {
		
	}
}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		Update();
		lab_name.setVisible(false);

		txt_anumber.setEditable(false);
	}
}
