package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

import Admin.AdminPanelController;
import User.FirstTimeLogin;
import User.UserPanelController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import userDom.SplitTheName;

public class BankingSystemController implements Initializable {
    @FXML
    private AnchorPane login2, login1, signup2, signup1, forgetpassword, forgetusername,emailverification, resetpassword;

    @FXML
    private Label gotologin, home, gotoforgetpassword, gotoforgetusername;

    @FXML
    private Label gotosignin;
    
    @FXML
    private Button but_createaccount ;
    
    @FXML
    private TextField txt_firstname, txt_lastname, txt_id,  txt_emailid, txt_mobileno;

    @FXML
    private TextField txt_username, txt_emailverify, txt_otp;
    
    @FXML
    private TextArea txt_address;
    
    @FXML
    private TextField hide;
    
    @FXML
    private ChoiceBox<String> drop_gender;

    @FXML
    private DatePicker datepicker_dob;

    @FXML
    private PasswordField txt_password;

    @FXML
    private Button but_cancel, but_sendotp, but_verify;
    
    @FXML
    private Button but_login;
    
    @FXML
    private TextField txt_loginusername;
    
    @FXML
    private PasswordField txt_loginpassword;
    
    
    ///////////forget_username
    @FXML
    private TextField txt_forgetUsernameName,txt_forgetUsernameEmailid, txt_forgetUsernameMobileno;
    @FXML
    private Button but_forgetUsernameSubmit;
    
    ////////////forget_passowrd
    @FXML
    private TextField txt_forgetPasswordUsername, txt_forgetPasswordEmailid, txt_forgetPasswordMobileno;
    @FXML
    private Button but_forgetPasswordSubmit ;
   
    
    
    
    //////////reset password
    @FXML private PasswordField txt_resetPasswordNewPassword, txt_ResetRasswordConformPassword;
    @FXML private Button but_resetPasswordUpdate;
    @FXML private Label lab_resetPasswordStatus;
    
    Connection conn;
    ResultSet rs;
    PreparedStatement prst;

    public void visibility(MouseEvent event) {
    	if(event.getSource() == gotosignin) {
    		
    		login1.setVisible(false);
    		signup1.setVisible(false);
    		login2.setVisible(true);
    		signup2.setVisible(true);
    		forgetusername.setVisible(false);
    		forgetpassword.setVisible(false);
    		forgetpassword.setVisible(false);
    		emailverification.setVisible(false);
    		
    	}else if(event.getSource() == gotologin) {
    		
    		login1.setVisible(true);
    		signup1.setVisible(true);
    		login2.setVisible(false);
    		signup2.setVisible(false);
    		forgetusername.setVisible(false);
    		forgetpassword.setVisible(false);
    		emailverification.setVisible(false);
    		
    	}else if(event.getSource() == gotoforgetpassword) {
    		
    		login1.setVisible(false);
    		signup1.setVisible(false);
    		login2.setVisible(false);
    		signup2.setVisible(false);
    		forgetusername.setVisible(false);
    		forgetpassword.setVisible(true);
    		emailverification.setVisible(false);
    		
    		
    	}else if(event.getSource() == gotoforgetusername) {
    		
    		login1.setVisible(false);
    		signup1.setVisible(false);
    		login2.setVisible(false);
    		signup2.setVisible(false);
    		forgetusername.setVisible(true);
    		forgetpassword.setVisible(false);
    		emailverification.setVisible(false);
    		
    		
    	}else if(event.getSource() == home) {
    		
    		login1.setVisible(true);
    		signup1.setVisible(true);
    		login2.setVisible(false);
    		signup2.setVisible(false);
    		forgetusername.setVisible(false);
    		forgetpassword.setVisible(false);
    		emailverification.setVisible(false);
    		resetpassword.setVisible(false);
    		
    	}
    }

    public void buttonhandler(ActionEvent event) {
    	if(event.getSource() == but_login){
    		
    		try {
				login(event);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}else if(event.getSource() == but_createaccount) {
    	
    		createaccount();
    		
    	}else if(event.getSource() == but_cancel) {
    		
    		emailverification.setVisible(false);
    		txt_firstname.setText("");
    		txt_lastname.setText("");
    		drop_gender.setValue(null);
    		datepicker_dob.setValue(null);
    		txt_id.setText("");
    		txt_address.setText("");
    		txt_emailid.setText("");
    		txt_mobileno.setText("");
    		txt_username.setText("");
    		txt_password.setText("");
    		
    		
    	}else if(event.getSource() == but_sendotp) {
    		
    		Random();
    		sendOTP();
    		
    	}else if(event.getSource() == but_verify) {
    		
    		verify();
    	}else if(event.getSource() == but_resetPasswordUpdate) {
    		resetpassword();
    	}
    }
   
    private void login(ActionEvent event) throws IOException {
		// TODO Auto-generated method stub
		conn = sqlconnect.dbconnect();
		try {
			prst= conn.prepareStatement("select *from user where username = ? and password = ? ");
			prst.setString(1, txt_loginusername.getText());
			prst.setString(2, txt_loginpassword.getText());
			rs = prst.executeQuery();
			if(rs.next()) {
				//********************** user is found ***********************************/
				if(rs.getString("type").equals("Admin")) {
					
					//******************** if user is admin ***********************************
					
					JOptionPane.showMessageDialog(null, "Welcome admin, "+ rs.getString("firstname"));
					
					((Node)event.getSource()).getScene().getWindow().hide();
					Stage primaryStage = new Stage();
					FXMLLoader loader = new FXMLLoader();
					Pane root = loader.load(getClass().getResource("/Admin/AdminPanel.fxml").openStream());
					
					AdminPanelController adminpanelController = (AdminPanelController)loader.getController();
					adminpanelController.SetAdminName(rs.getString("firstname") + " " +rs.getString("lastname"));
					
					Scene scene = new Scene(root);
					primaryStage.initStyle(StageStyle.TRANSPARENT);
					primaryStage.setScene(scene);
					
					primaryStage.show();
			 		
					
				}else {
					// ************************ if user is a client **********************************
					if(txt_loginpassword.getText().equals("0000")) {
						//*************************** if the user account is created by admin only **********************
						JOptionPane.showMessageDialog(null, "Welcome, "+ rs.getString("firstname"));
						
						((Node)event.getSource()).getScene().getWindow().hide();
						Stage primaryStage = new Stage();
						FXMLLoader loader = new FXMLLoader();
						
						
						Pane root = loader.load(getClass().getResource("/User/FirstTimeLogin.fxml").openStream());
						
						FirstTimeLogin firsttimelogin = (FirstTimeLogin)loader.getController();
						firsttimelogin.SetName(rs.getString("firstname")+rs.getString("lastname"));
						firsttimelogin.setAccno(rs.getInt("accno"));
						firsttimelogin.setUsername(rs.getString("username"));
						firsttimelogin.setEmailid(rs.getString("emailid"));
						
						Scene scene = new Scene(root);
						primaryStage.initStyle(StageStyle.TRANSPARENT);
						primaryStage.setScene(scene);
						
						primaryStage.show();
					}else {
					
					//************** normal user *****************************
					JOptionPane.showMessageDialog(null, "Welcome, "+ rs.getString("firstname"));
					
					((Node)event.getSource()).getScene().getWindow().hide();
					Stage primaryStage = new Stage();
					FXMLLoader loader = new FXMLLoader();
					
					
					Pane root = loader.load(getClass().getResource("/User/UserPanel.fxml").openStream());
					
					UserPanelController userpanelController = (UserPanelController)loader.getController();
					userpanelController.setAccno(rs.getInt("accno"));
					userpanelController.setName(rs.getString("firstname")+rs.getString("lastname"));
					userpanelController.setUsername(rs.getString("username"));
					
					Scene scene = new Scene(root);
					primaryStage.initStyle(StageStyle.TRANSPARENT);
					primaryStage.setScene(scene);
					
					primaryStage.show();
					}	
				}
			}else {
				//************************ user not found ****************************************
				
				JOptionPane.showMessageDialog(null, "Username or Password is Incorrect!!!!");
			}
			conn.close();
		} catch (SQLException e) {
			
		}
	}

	public void verify() {
		// TODO Auto-generated method stub
		if(txt_otp.getText().equals(hide.getText())) {
			//************************* if otp is correct then ****************************************
			
			JOptionPane.showMessageDialog(null, "Email id verified");
			
			String sql = "insert into user(firstname, lastname,gender, dob, id, address, emailid, mobileno,username,password, datetime) values(?,?,?,?,?,?,?,?,?,?, datetime('now','localtime'))";
			conn = sqlconnect.dbconnect();
			String username = txt_username.getText();
			int accno;
			try {
				prst = conn.prepareStatement(sql);
				prst.setString(1, txt_firstname.getText());
				prst.setString(2, txt_lastname.getText());
				prst.setString(3, drop_gender.getSelectionModel().getSelectedItem().toString());
				prst.setString(4, datepicker_dob.getValue().toString());
				prst.setString(5, txt_id.getText());
				prst.setString(6, txt_address.getText());
				prst.setString(7, txt_emailverify.getText());
				prst.setString(8, txt_mobileno.getText());
				prst.setString(9, txt_username.getText());
				prst.setString(10, txt_password.getText());
				boolean flag = prst.execute();
				
				if(!flag) {
					JOptionPane.showMessageDialog(null, "Account Created");
					//************** both the tables are created here i.e, user balance table and user txn table
					try {
						prst = conn.prepareStatement("select * from user where username = ?");
						prst.setString(1, username);
						rs = prst.executeQuery();
						accno = rs.getInt("accno");
					
						// create table balance
						String tablebalance ="CREATE TABLE IF NOT EXISTS " + username+accno + " (balance real default 0.0);";
						try {
							Statement stmt = conn.createStatement();
							stmt.execute(tablebalance);
							stmt.execute("insert into " + username+accno + "  values(0.0)");
						
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e);
						}
					
						// create table trx
						String tabletrx ="create table IF NOT EXISTS " + "trx"+username+accno + " (date text, remarks text, type text, amount real, balance real)";
						try {
							Statement stmt = conn.createStatement();
							stmt.execute(tabletrx);
						
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							JOptionPane.showMessageDialog(null, e);
						}
					
					}catch(Exception e) {
						JOptionPane.showMessageDialog(null, "inside create table"+e);
					}
					
					
					// back to home screen
					login1.setVisible(true);
					signup1.setVisible(true);
					login2.setVisible(false);
					signup2.setVisible(false);
					forgetusername.setVisible(false);
					forgetpassword.setVisible(false);
	    			emailverification.setVisible(false);
	    			sendWELCOME();
				}
				conn.close();
			}catch(Exception e) {
				JOptionPane.showMessageDialog(null, e);
			}
		}else {
			JOptionPane.showMessageDialog(null, "Please enter the Correct OTP or Check your Email id.");
		}
		txt_firstname.setText(null);
    	txt_lastname.setText(null);
    	txt_id.setText(null);
    	txt_address.setText(null);
    	txt_mobileno.setText(null);
    	txt_emailid.setText(null);
    	txt_username.setText(null);
    	txt_password.setText(null);
    	datepicker_dob.setValue(null);
    	drop_gender.setValue(null);
    	txt_otp.setText(null);
	}

	public void createaccount() {
    	
    	if(txt_firstname.getText().trim().isEmpty() || txt_lastname.getText().trim().isEmpty() || txt_id.getText().trim().isEmpty() ||
    			txt_address.getText().trim().isEmpty() || txt_mobileno.getText().trim().isEmpty() || txt_emailid.getText().trim().isEmpty() ||
    			txt_username.getText().trim().isEmpty() || txt_password.getText().trim().isEmpty()) {
    		
    		JOptionPane.showMessageDialog(null, "Enter all the details.");
    		
    	}else {
    		//**************if user enter all the details then verify the email id and then create account *******
    		
    		txt_emailverify.setText(txt_emailid.getText());
    		emailverification.setVisible(true);
    		
    	}
    	
    	
    	    	
    }
    
	public void forgetusername() {
		conn = sqlconnect.dbconnect();
		try {
			SplitTheName.split(txt_forgetUsernameName.getText()); // it will split the name into firstname and lastname
			prst= conn.prepareStatement("select *from user where (firstname = ?) and (lastname = ?) and (emailid = ? or mobileno = ?)");
			prst.setString(1, SplitTheName.getFirstname());
			prst.setString(2, SplitTheName.getLastname());
			
			if(txt_forgetUsernameMobileno.getText().isEmpty()) {
				/////user enter the email id
				prst.setString(3, txt_forgetUsernameEmailid.getText());
				prst.setString(4, null);
			}else {
				//////user enter the mobile no
				prst.setString(3, null);
				prst.setString(4, txt_forgetUsernameMobileno.getText());
			}
			rs = prst.executeQuery();
			
			if(rs.next()) {
				// ////user id found
				if(sendMail.sendmail("Dear Customer,\n" + "\tYour Username is : "+ rs.getString("username") , txt_forgetUsernameEmailid.getText(), "Forget Username")) {
					JOptionPane.showMessageDialog(null, "Your Username is sent to your email id.");
				}
				
				
				login1.setVisible(true);
	    		signup1.setVisible(true);
	    		login2.setVisible(false);
	    		signup2.setVisible(false);
	    		forgetusername.setVisible(false);
	    		forgetpassword.setVisible(false);
	    		emailverification.setVisible(false);
	    		resetpassword.setVisible(false);
	    		
			}else {
				///////// user id not found
				JOptionPane.showMessageDialog(null, "Please Enter the Correct Details");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txt_forgetUsernameName.setText("");
		txt_forgetUsernameEmailid.setText("");
		txt_forgetUsernameMobileno.setText("");
		SplitTheName.setFirstname(null);
		SplitTheName.setLastname(null);
	}
	int accno = 0;
	public void forgetpassword(ActionEvent event) {
		conn = sqlconnect.dbconnect();
		try {
						
			prst = conn.prepareStatement("select *from user where username = ? and (emailid =? or mobileno =?)");
			prst.setString(1, txt_forgetPasswordUsername.getText().trim());
			
			if(txt_forgetUsernameMobileno.getText().trim().isEmpty()) {
				/////user enter the email id
				prst.setString(2, txt_forgetPasswordEmailid.getText());
				prst.setString(3, null);
			}else {
				//////user enter the mobile no
				prst.setString(2, null);
				prst.setString(3, txt_forgetPasswordMobileno.getText());
			}
			rs = prst.executeQuery();
			
			if(rs.next()) {
				// ////user id found
				resetpassword.setVisible(true);
				accno = rs.getInt("accno");
					
				
			}else {
				///////// user id not found
				JOptionPane.showMessageDialog(null, "Please Enter the Correct Details");
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		txt_forgetPasswordUsername.setText("");
		txt_forgetPasswordEmailid.setText("");
		txt_forgetPasswordMobileno.setText("");
	}
	public void resetpassword() {
		try {
			conn = sqlconnect.dbconnect();
			if(txt_resetPasswordNewPassword.getText().equals(txt_ResetRasswordConformPassword.getText())) {
				prst = conn.prepareStatement("update user set password = ? where accno = ?");
				prst.setString(1, txt_resetPasswordNewPassword.getText());
				prst.setInt(2, accno);
				prst.executeUpdate();
				JOptionPane.showMessageDialog(null,"Password Update Successfully.");
				login1.setVisible(true);
	    		signup1.setVisible(true);
	    		login2.setVisible(false);
	    		signup2.setVisible(false);
	    		forgetusername.setVisible(false);
	    		forgetpassword.setVisible(false);
	    		emailverification.setVisible(false);
	    		resetpassword.setVisible(false);
				
			}else {
				lab_resetPasswordStatus.setText("Password do not match !!!");
			}
			conn.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
		txt_resetPasswordNewPassword.setText(null);
		txt_ResetRasswordConformPassword.setText(null);
	}
	

	public void Random() {
    		Random rd = new Random();
    		hide.setText(""+rd.nextInt(10000 + 1));		
    }
    public void sendOTP() {
    		Properties props=new Properties();
            props.put("mail.smtp.host","smtp.gmail.com");
            props.put("mail.smtp.port",465);
            props.put("mail.smtp.user","avbank2@gmail.com"); // your email id
            props.put("mail.smtp.auth",true);
            props.put("mail.smtp.starttls.enable",true);
            props.put("mail.smtp.debug",true);
            props.put("mail.smtp.socketFactory.port",465);
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback",false); 
            
            try {             
                    Session session = Session.getDefaultInstance(props, null);
                    session.setDebug(true);
                    MimeMessage message = new MimeMessage(session);
                    message.setText("Your OTP is " + hide.getText());	// msg send to the email id
                    message.setSubject("AV Bank ... Bank of Benifits - Email Verfication");
                    message.setFrom(new InternetAddress("avbank2@gmail.com"));
                    message.addRecipient(RecipientType.TO, new InternetAddress(txt_emailverify.getText().trim()));		// email of the reciever
                    message.saveChanges();
                    try
                    {
                    Transport transport = session.getTransport("smtp");
                    transport.connect("smtp.gmail.com","avbank2@gmail.com","Logitech@2");
                    transport.sendMessage(message, message.getAllRecipients());
                    transport.close();
                                  
                    JOptionPane.showMessageDialog(null,"OTP send to your Email id"); 
                    }catch(Exception e)
                    {
                        JOptionPane.showMessageDialog(null,"Invalid Emailid.");
                    }              
                
            } catch (Exception e) {
                e.printStackTrace();  
                JOptionPane.showMessageDialog(null,e);
            }  

    	}
    public void sendWELCOME() {
		Properties props=new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port",465);
        props.put("mail.smtp.user","avbank2@gmail.com"); // your email id
        props.put("mail.smtp.auth",true);
        props.put("mail.smtp.starttls.enable",true);
        props.put("mail.smtp.debug",true);
        props.put("mail.smtp.socketFactory.port",465);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback",false); 
        
        try {             
                Session session = Session.getDefaultInstance(props, null);
                session.setDebug(true);
                MimeMessage message = new MimeMessage(session);
                message.setText("Your Account Created Successfully. Login to your account using your credentials. And Enjoy Bank Benifits.!!!  |:)|");	// msg send to the email id
                message.setSubject("AV Bank ... Bank of Benifits - Welcome");
                message.setFrom(new InternetAddress("avbank2@gmail.com"));
                message.addRecipient(RecipientType.TO, new InternetAddress(txt_emailverify.getText().trim()));		// email of the reciever
                message.saveChanges();
                try
                {
                Transport transport = session.getTransport("smtp");
                transport.connect("smtp.gmail.com","avbank2@gmail.com","Logitech@2");
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
                              
//                JOptionPane.showMessageDialog(null,"OTP has send to your Email id"); 
                }catch(Exception e)
                {
                    JOptionPane.showMessageDialog(null,"Please check your internet connection");
                }              
            
        } catch (Exception e) {
            e.printStackTrace();  
            JOptionPane.showMessageDialog(null,e);
        }  

	}
    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		login1.setVisible(true);
		signup1.setVisible(true);
		login2.setVisible(false);
		signup2.setVisible(false);
		forgetusername.setVisible(false);
		forgetpassword.setVisible(false);
		emailverification.setVisible(false);
		resetpassword.setVisible(false);
		
		ObservableList<String> list = FXCollections.observableArrayList("Male", "Female", "Others");
		drop_gender.setItems(list);
	}
    
	public void exit(ActionEvent event) {
		System.exit(0);
	}
}
/* after 115 

 */
 