/*original author: Jason Niu  from University of Tasmania, E-mail: Jason.qniu@gmail.com */
package application;

import java.io.*; 
import java.net.*; 
import java.util.Optional;
import java.io.IOException;
import java.lang.reflect.Field;
import com.sun.javafx.robot.FXRobot;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javafx.scene.control.Slider;
import javafx.event.EventHandler;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.collections.*;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {
	static public String calibrated_value;
	static public FXRobot robot2;
	static public CheckBox check_mouth,check_eyebrow,check_smile,check_nose;
	static public Label statusbar_text;
	String Combo1_selected,Combo2_selected,Combo3_selected,Combo4_selected;
	File file_out;
	 @Override
	    public void start(Stage primaryStage) {
		 StackPane root = new StackPane();					//Set up initial layout of the window
		 Scene scene = new Scene(root, 856, 340);
		 primaryStage.setResizable(false);					//turn the Resizable off
		 root.setId("pane");
		 ObservableList<String> options = 					//option list for all combox
			     FXCollections.observableArrayList(
			    	    "F12",
					    "RIGHT_CLICK",
					    "ENTER",
					    "SPACE",
					    "PAGE_UP",
					    "PAGE_DOWN",
					    "LEFT_CLICK"          
			     );
			    
		  final ComboBox<String> ComboBox1 = new ComboBox<String>(options);
		  final ComboBox<String> ComboBox2 = new ComboBox<String>(options);
		  final ComboBox<String> ComboBox3 = new ComboBox<String>(options);
		  final ComboBox<String> ComboBox4 = new ComboBox<String>(options);
		try
		{
		 scene.getStylesheets().add("application/application.css");					//css file for layout
		 primaryStage.getIcons().add(new Image("application/java-jar-icon.png"));	//e-life icon
		 }catch (Exception e){
			System.out.println("Css file, icons missing");
			}
		 	Button btn_load= new Button("Load...");									//create load, save button and add css style
		    Button btn_save= new Button("Save...");
		    btn_load.getStyleClass().add("button_store");
		    btn_save.getStyleClass().add("button_store");
		    HBox hbox_store= new HBox();											//put the two buttons into a horizon group of box for layout
		    hbox_store.getStyleClass().add("hbox_btn_LS");
		    hbox_store.getChildren().addAll(btn_load,btn_save);
		 check_mouth = new CheckBox("Opening mouth");
		 check_mouth.getStyleClass().add("check");
		 check_eyebrow = new CheckBox("Raising eyebrows");
		 check_eyebrow.getStyleClass().add("check");
		 check_smile = new CheckBox("Smile");
		 check_smile.getStyleClass().add("check");
		 check_nose = new CheckBox("Snarling");
		 check_nose.getStyleClass().add("check");
		 
		 check_mouth.setOnAction(new EventHandler<ActionEvent>() { 
			 @Override 
			 public void handle(ActionEvent e) { 
			    				    	
			    System.out.println("Check box mouth is selected " + check_mouth.isSelected());   //Output check box status
			   } 
		 });
	     check_eyebrow.setOnAction(new EventHandler<ActionEvent>() { 
			 @Override 
			 public void handle(ActionEvent e) { 
			    
			    System.out.println("check box eyebrow is selected " + check_eyebrow.isSelected() );
			  } 
		 });
		 check_smile.setOnAction(new EventHandler<ActionEvent>() { 
			 @Override 
			 public void handle(ActionEvent e) { 
			    
			    	System.out.println("check box smile is selected " + check_smile.isSelected() );
			   } 
		  });
			    
		  check_nose.setOnAction(new EventHandler<ActionEvent>() { 
			 @Override 
			 public void handle(ActionEvent e) { 
			    	
			    	System.out.println("check box nose is selected " + check_nose.isSelected());
			   } 
		   });
		 
		  HBox hbox_check = new HBox();
		  hbox_check.getChildren().addAll(check_mouth,check_eyebrow,check_smile,check_nose);
		  hbox_check.getStyleClass().add("hbox_checks");
		  Label label_1 = new Label("");							//create a set of gray labels for displaying selected key name
		  label_1.setWrapText(true);
		  Label label_2 = new Label("");
		  label_2.setWrapText(true);
		  Label label_3 = new Label("");
		  Label label_4 = new Label("");
		  HBox hbox_label = new HBox();
		  label_1.getStyleClass().add("label_mouth");
		  label_2.getStyleClass().add("label_2");
		  label_3.getStyleClass().add("label_3");
		  label_4.getStyleClass().add("label_4");
		  
		  hbox_label.getChildren().addAll(label_1,label_2,label_3,label_4);
		  hbox_label.setPadding(new Insets(-62, 0, 0, 40));				//setting the group of labels' position on the window
		  ComboBox1.setEditable(true);  
		  ComboBox2.setEditable(true);  
		  ComboBox3.setEditable(true);  
		  ComboBox4.setEditable(true);  
		  ComboBox1.getStyleClass().add("combox"); 						//setting layout style for combox
		  ComboBox2.getStyleClass().add("combox");
		  ComboBox3.getStyleClass().add("combox");
		  ComboBox4.getStyleClass().add("combox");
		  
		  ComboBox1.setOnAction(new EventHandler<ActionEvent>() {       //setting up functions for combobox drop down list, what functions will be activated if users opened their mouth
			    public void handle(ActionEvent me) {
			    	 Combo1_selected = ComboBox1.getSelectionModel().getSelectedItem();
			    	 label_1.setText(Combo1_selected.toString());
			    	 int selected_index=ComboBox1.getSelectionModel().getSelectedIndex();
			         switch (selected_index) {
			            case 0:  server_data.mouth_action = KeyEvent.VK_F12;
			                     break;
			            case 1:  server_data.mouth_action = KeyEvent.BUTTON3_MASK;
			                     break;
			            case 2:  server_data.mouth_action = KeyEvent.VK_ENTER;
			                     break;
			            case 3:  server_data.mouth_action = KeyEvent.VK_SPACE;
			                     break;
			            case 4:  server_data.mouth_action = KeyEvent.VK_PAGE_UP;
			                     break;
			            case 5:  server_data.mouth_action = KeyEvent.VK_PAGE_DOWN;
			                     break;
			            case 6:  server_data.mouth_action = KeyEvent.BUTTON1_MASK;
			            		 break;
			    	    }
			    	}
				});
		 ComboBox2.setOnAction((event2) -> {
			  		 Combo2_selected = ComboBox2.getSelectionModel().getSelectedItem();
			    	 label_2.setText(Combo2_selected.toString());
			    	 int selected_index=ComboBox2.getSelectionModel().getSelectedIndex();
			    	 switch (selected_index) {
			            case 0:  server_data.eyebrow_action = KeyEvent.VK_F12;
			                     break;
			            case 1:  server_data.eyebrow_action = KeyEvent.BUTTON3_MASK;
			                     break;
			            case 2:  server_data.eyebrow_action = KeyEvent.VK_ENTER;
			                     break;
			            case 3:  server_data.eyebrow_action = KeyEvent.VK_SPACE;
			                     break;
			            case 4: 
			            		 server_data.eyebrow_action = KeyEvent.VK_PAGE_UP;
			                     break;
			            case 5:  server_data.eyebrow_action = KeyEvent.VK_PAGE_DOWN;
			                     break;
			            case 6:  server_data.eyebrow_action = KeyEvent.BUTTON1_MASK;
			            		 break;
			    	    }
			    	 
			    });
		  
		 ComboBox3.setOnAction((event3) -> {
			 		 Combo3_selected = ComboBox3.getSelectionModel().getSelectedItem();
			    	 label_3.setText(Combo3_selected.toString());
			    	 int selected_index=ComboBox3.getSelectionModel().getSelectedIndex();
			    	switch (selected_index) {
			            case 0:  server_data.smile_action = KeyEvent.VK_F12;
			                     break;
			            case 1:  server_data.smile_action = KeyEvent.BUTTON3_MASK;
			                     break;
			            case 2:  server_data.smile_action = KeyEvent.VK_ENTER;
			                     break;
			            case 3:  server_data.smile_action = KeyEvent.VK_SPACE;
			                     break;
			            case 4:  server_data.smile_action = KeyEvent.VK_PAGE_UP;
			                     break;
			            case 5:  server_data.smile_action = KeyEvent.VK_PAGE_DOWN;
			                     break;
			            case 6:  server_data.smile_action = KeyEvent.BUTTON1_MASK;
			                     break;
			    	    }
			    	
			    });
		  
			ComboBox4.setOnAction((event4) -> {
					 Combo4_selected = ComboBox4.getSelectionModel().getSelectedItem();
			    	 label_4.setText(Combo4_selected.toString());
			    	 int selected_index=ComboBox4.getSelectionModel().getSelectedIndex();
			    	 switch (selected_index) {
			            case 0:  server_data.nose_action = KeyEvent.VK_F12;
			                     break;
			            case 1:  server_data.nose_action = KeyEvent.BUTTON3_MASK;
			                     break;
			            case 2:  server_data.nose_action = KeyEvent.VK_ENTER;
			                     break;
			            case 3:  server_data.nose_action = KeyEvent.VK_SPACE;
			                     break;
			            case 4:  server_data.nose_action = KeyEvent.VK_PAGE_UP;
			                     break;
			            case 5:  server_data.nose_action = KeyEvent.VK_PAGE_DOWN;
			                     break;
			            case 6:  server_data.nose_action = KeyEvent.BUTTON1_MASK;
			            		 break;
			    	    }
			 });
			    
			  
			 Map<String , Integer> into = new HashMap<String, Integer>();          
			 try {	//to send keys from keyboard to OS,I need to use java.awt.Robot, but it can only use constant integer variableS defined by API like VK_A (65) for A key in java.awt.KeyEvent
			    	//But javafx.Event only can return A if A key was pressed, so for enabling Mapping any key from keyboard function, I need to link javafx.Event.Keycode.A with java.awt.KeyEvent.VK_A
				 	//basically I need convert this (VK_A,65),(VK_B,66) to this (A,65), (B,66) by creating new HashMap , so when when javafx components receive an A , the APP will know I can use 65 in Java.awt.Robot
				
			        Field[] fields = KeyEvent.class.getDeclaredFields();
			        for(Field f : fields) {
			            if(f.getName().startsWith("VK_")) { 
			            	if(f.getName().startsWith("VK_DIGIT")){
			            		int code = ((Integer)f.get(null)).intValue();
				                into.put(f.getName().substring(8), code);
			            	}else{
			            		
			            		int code = ((Integer)f.get(null)).intValue();
			            		into.put(f.getName().substring(3), code);
			            	}
			                
			            }
			        }
			    } catch(Exception ex) {
			    	System.out.println("Errors happen in converting keycode to keyevent value");
			    	
			    }
			    /*
			    for(String key: into.keySet()){
		            System.out.println(key  +" :: "+ into.get(key));
		        }
			 */
			ComboBox1.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
			        public void handle(javafx.scene.input.KeyEvent e) {
			        ComboBox1.getEditor().clear();							// after a Key was pressed, clear the combobox for the new key
			        System.out.println("CLEAR ");
			        }
			    });
			
			   ComboBox1.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED,new EventHandler<javafx.scene.input.KeyEvent>() {
				@Override public void handle(javafx.scene.input.KeyEvent e) {
					
					System.out.println("Key code: " + e.getCode());
		        	if(e.getCode().toString().startsWith("DIGIT")){				//in java.awt.KeyEvent, it use DIGIT0 for 0 key on keyboard, so I need get rid of "DIGIT" before linking 0 key in javafx.keycode with its integer value in Java.awt.KeyEvent.VK_DIGIT0
		        		System.out.println("Key code substract " + e.getCode().toString().substring(5));     
	            		server_data.mouth_action= into.get(e.getCode().toString().substring(5));      //assign the integer value of the key in Java.awt.KeyEvent to a variable in sub thread which is responsible for detecting gesture and activating the key
	            	}else 
	            		server_data.mouth_action= into.get(e.getCode().toString());
		        	label_1.setText(e.getCode().toString());											// change the gray label to mapped key name
					ComboBox1.getEditor().setText(e.getCode().toString());
					}
                });	  
			   
			   ComboBox2. addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
			        public void handle(javafx.scene.input.KeyEvent e) {
			           ComboBox2.getEditor().clear();					
			     	   System.out.println("CLEAR ");
			     	  ComboBox2.setOnKeyReleased(null);
						   
			        }
			    });
			   
			   ComboBox2.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, new EventHandler<javafx.scene.input.KeyEvent>() {
					@Override public void handle(javafx.scene.input.KeyEvent e) {
						
						System.out.println("Key Pressed: " + e.getCode());
			        	if(e.getCode().toString().startsWith("DIGIT")){       
			        		System.out.println("Key code substract " + e.getCode().toString().substring(5));
			        		server_data.eyebrow_action= into.get(e.getCode().toString().substring(5));
		            	}else 
		            		server_data.eyebrow_action= into.get(e.getCode().toString());
			        	
			        	label_2.setText(e.getCode().toString());
						 ComboBox2.getEditor().setText(e.getCode().toString());
						
					}
	               });	
			   
			   ComboBox3.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
			        public void handle(javafx.scene.input.KeyEvent e3) {
			        	ComboBox3.getEditor().clear();
			     	   System.out.println("CLEAR ");
			        }
			    });
			    
			    ComboBox3.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, new EventHandler<javafx.scene.input.KeyEvent>() {
			        @Override public void handle(javafx.scene.input.KeyEvent e3) {
			        	if(e3.getCode().toString().startsWith("DIGIT")){
			        		System.out.println("Key code substract " + e3.getCode().toString().substring(5));
			        		server_data.smile_action= into.get(e3.getCode().toString().substring(5));
		            	}else 
		            		server_data.smile_action= into.get(e3.getCode().toString());
			        	
			        	label_3.setText(e3.getCode().toString());
				    	ComboBox3.getEditor().setText(e3.getCode().toString());
				    	System.out.println("Key Pressed: " + server_data.smile_action);
			         }
			    });
			  
			    ComboBox4.addEventHandler(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<javafx.scene.input.KeyEvent>() {
			        public void handle(javafx.scene.input.KeyEvent e4) {
			        	ComboBox4.getEditor().clear();
			     	   System.out.println("CLEAR ");
			        }
			    });
			    ComboBox4.addEventHandler(javafx.scene.input.KeyEvent.KEY_RELEASED, new EventHandler<javafx.scene.input.KeyEvent>() {
			        @Override public void handle(javafx.scene.input.KeyEvent e4) {
			        	if(e4.getCode().toString().startsWith("DIGIT")){
			        		System.out.println("Key code substract " + e4.getCode().toString().substring(5));
			        		server_data.nose_action= into.get(e4.getCode().toString().substring(5));
		            	}else 
		            		server_data.nose_action= into.get(e4.getCode().toString());
			        	label_4.setText(e4.getCode().toString());
			        	System.out.println("Key Pressed: " + e4.getCode());
				    	ComboBox4.getEditor().setText(e4.getCode().toString());
			         }
			    });
			    
			    
			    final Slider mouth_slider = new Slider(0, 1, 0.5); 					//adding sensitiveness slider bars for different gestures
			    mouth_slider.setPrefWidth(165.0f);									//set width of the slider bar
			    mouth_slider.setShowTickLabels(true);								//show the tick labels
			    final Slider eyebrow_slider = new Slider(0, 1, 0.5); 
			    eyebrow_slider.setShowTickLabels(true);
			    eyebrow_slider.setPrefWidth(165.0f);
			    final Slider smile_slider = new Slider(0, 1, 0.5);
			    smile_slider.setShowTickLabels(true);
			    smile_slider.setPrefWidth(165.0f);
			    final Slider nose_slider = new Slider(0, 1, 0.5); 
			    nose_slider.setShowTickLabels(true);
			    nose_slider.setPrefWidth(165.0f);
			    
			    HBox hbox_sliders = new HBox();										//put all slider bars into a horizon group
			    hbox_sliders.getStyleClass().add("hbox_slider");
			    hbox_sliders.getChildren().addAll(mouth_slider,eyebrow_slider,smile_slider,nose_slider);
			    final Label mouth_slider_value = new Label(Double.toString(mouth_slider.getValue()));
			    mouth_slider_value.getStyleClass().add("slider_label");
			    final Label eyebrow_slider_value = new Label(Double.toString(eyebrow_slider.getValue()));
			    eyebrow_slider_value.getStyleClass().add("slider_label");
			    final Label smile_slider_value = new Label(Double.toString(smile_slider.getValue()));
			    smile_slider_value.getStyleClass().add("slider_label");
			    final Label nose_slider_value = new Label(Double.toString(nose_slider.getValue()));
			    nose_slider_value.getStyleClass().add("slider_label");
			    
			    mouth_slider.valueProperty().addListener(new ChangeListener<Number>() {
		            public void changed(ObservableValue<? extends Number> ov,
		                Number old_val, Number new_val) {
		            	server_data.mouth_sensitiveness=new_val.floatValue();
		                    mouth_slider_value.setText(String.format("%.2f", new_val));      //display the change when users is adjusting the slider bar
		                    
		            }
		        });
			    
			    eyebrow_slider.valueProperty().addListener(new ChangeListener<Number>() {
		            public void changed(ObservableValue<? extends Number> ov,
		                Number old_val, Number new_val) {
		            	server_data.eyebrow_sensitiveness=new_val.floatValue();
		            	eyebrow_slider_value.setText(String.format("%.2f", new_val));
		            }
		        });
			    
			    smile_slider.valueProperty().addListener(new ChangeListener<Number>() {
		            public void changed(ObservableValue<? extends Number> ov,
		                Number old_val, Number new_val) {
		            	server_data.smile_sensitiveness=new_val.floatValue();
		            	smile_slider_value.setText(String.format("%.2f", new_val));
		            }
		        });
			    nose_slider.valueProperty().addListener(new ChangeListener<Number>() {
		            public void changed(ObservableValue<? extends Number> ov,
		                Number old_val, Number new_val) {
		            	server_data.nose_sensitiveness=new_val.floatValue();
		            	nose_slider_value.setText(String.format("%.2f", new_val));
		            }
		        });
			    
			    HBox hbox_slider_labels = new HBox();
			    hbox_slider_labels.getStyleClass().add("hbox_slider_label");
			    hbox_slider_labels.getChildren().addAll(mouth_slider_value,eyebrow_slider_value,smile_slider_value,nose_slider_value);
			    
			    HBox hbox_combo = new HBox();
			    
			    hbox_combo.getChildren().addAll(ComboBox1,ComboBox2,ComboBox3,ComboBox4);
			    hbox_combo.setPadding(new Insets(-10, 0, 0, 0));
			    hbox_combo.getStyleClass().add("hbox_combo");
			    Button btn_calibration= new Button("Calibration");
			    btn_calibration.getStyleClass().add("button_CRQ");
			    Button btn_pause= new Button("Pause");
			    //set the prefered width for pause button, when users click the button, 
			    //the text "pause" will be replaced by "resume". if we didn't set the width value, 
			    //the width of the button will be changed when the button was clicked
			    
			    btn_pause.setStyle("-fx-pref-width:128px;");					
			    btn_pause.setOnAction((event) -> {
			        System.out.println("Button pause");
			        if(server_data.pause_s==true){					//disable the detection function
			        server_data.pause_m=false;
			        server_data.pause_e=false;
			        server_data.pause_s=false;
			        server_data.pause_n=false;
			        statusbar_text.setText("Paused");
			        btn_pause.setText("Resume");
			        }else{
			        	server_data.pause_m=true;
				        server_data.pause_e=true;
				        server_data.pause_s=true;
				        server_data.pause_n=true;
			        	btn_pause.setText("Pause");
			        	 statusbar_text.setText("Resumed");
			        }
			     
			    });
			    btn_pause.getStyleClass().add("button_CRQ");
			    Button btn_reset= new Button("Reset");
			    btn_reset.getStyleClass().add("button_CRQ");
			    Button btn_quit= new Button("Quit");
			    btn_quit.getStyleClass().add("button_CRQ");
			    HBox hbox_button= new HBox();					//put all four buttons at the bottom in a horizon group and set the layout
			    hbox_button.setPadding(new Insets(30, 0, 0, 0));
			    hbox_button.getStyleClass().add("hbox_btn");
			    hbox_button.getChildren().addAll(btn_calibration,btn_pause,btn_reset,btn_quit);
			    HBox statusbar = new HBox();					//set up the status bar
			    statusbar_text = new Label("Ready...");
			    statusbar_text.getStyleClass().add("status-text");
			    statusbar.getChildren().add(statusbar_text);
			    statusbar.getStyleClass().add("status-bar");
			    
			    VBox statusbar_in = new VBox();
			    statusbar_in.getChildren().add(statusbar);
			    
			    btn_calibration.setOnAction((event) -> {
			    	statusbar_text.setText("Calibrated");				// use the calibration_flag to control when to do a calibration
			        server_data.calibration_flag=true;
			    });
			    
			    btn_reset.setOnAction((event) -> {						// reset all options 
			    	ComboBox1.getEditor().setText("");
			    	label_1.setText("");
			    	check_mouth.setSelected(false);
			    	server_data.mouth_action=0;
			    	ComboBox2.getEditor().setText("");
			    	label_2.setText("");
			    	check_eyebrow.setSelected(false);
			    	server_data.eyebrow_action=0;
			    	ComboBox3.getEditor().setText("");
			    	label_3.setText("");
			    	check_smile.setSelected(false);
			    	server_data.smile_action=0;
			    	ComboBox4.getEditor().setText("");
			    	label_4.setText("");
			    	check_nose.setSelected(false);
			    	server_data.nose_action=0;
			    	eyebrow_slider.setValue(0.5);
			    	mouth_slider.setValue(0.5);
			    	smile_slider.setValue(0.5);
			    	nose_slider.setValue(0.5);
			    	statusbar_text.setText("Reset");
			    });
			    btn_quit.setOnAction((event) -> {						//quit button
			    	Alert quit = new Alert(AlertType.CONFIRMATION);
			    	quit.setTitle("Calibration");
			    	quit.setHeaderText("Would you like to quit?");
			    	quit.setContentText("Click Ok to exit");
			        Optional<ButtonType> quit_result = quit.showAndWait();
			        
			        if (quit_result.get() == ButtonType.OK){
			        	try{											//kill face tracker window
			        	Runtime.getRuntime().exec("taskkill /F /IM Facial_data_to_Java.exe");
			        	}catch ( IOException e){
			        		System.out.println("Kill subprocess failed.");
			        	}
			        	try{
			        		if(server_data.socket!=null)				//if server is still open , close it
			        			server_data.socket.close();
			        	}catch (IOException e){
			        		System.out.println("Connection closing error");
			        	}
			        	System.exit(0);
			        }
			        System.out.println("Button quit");
			    });
			    
			    
			    btn_save.setOnAction((event) -> {
			        System.out.println("Button save");					//save the preference data into preference.txt
			        try{  
			        	file_out = new File("preference.txt");
			        	FileOutputStream fos = new FileOutputStream(file_out);
			        	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			    		bw.write(check_mouth.isSelected()+" "+check_eyebrow.isSelected()+" "+check_smile.isSelected()+" "+check_nose.isSelected());
			    		bw.newLine();
			    		bw.write(ComboBox1.getEditor().getText()+" "+ComboBox2.getEditor().getText()+" "+ComboBox3.getEditor().getText()+" "+ComboBox4.getEditor().getText());
			    		bw.newLine();
			    		bw.write(server_data.mouth_action+" "+server_data.eyebrow_action+" "+server_data.smile_action+" "+server_data.nose_action);
			    		bw.newLine();
			    		bw.write(server_data.mouth_sensitiveness +" "+server_data.eyebrow_sensitiveness +" "+ server_data.smile_sensitiveness +" " +server_data.nose_sensitiveness);
			    		bw.close();
			    		statusbar_text.setText("Data saved");
			        }catch (IOException ex) {
			        	System.out.println("Data writing error");
			        }  
			     
			    });
			 
			    btn_load.setOnAction((event) -> {
			    	StringBuilder contents = new StringBuilder();
			    	 
			        try {														// if the file exists, load the file and set up all values for options
			            BufferedReader input =  new BufferedReader(new FileReader("preference.txt"));
			            try {
			                String line = null;
			                line = input.readLine();
			                String[] seperated_check = line.split(" ");
			                check_mouth.setSelected(Boolean.valueOf(seperated_check[0]));
			                check_eyebrow.setSelected(Boolean.valueOf(seperated_check[1]));
			                check_smile.setSelected(Boolean.valueOf(seperated_check[2]));
			                check_nose.setSelected(Boolean.valueOf(seperated_check[3]));
			                line = input.readLine();
			                String[] seperated_combobox = line.split(" ");
			                label_1.setText(seperated_combobox[0]);
							ComboBox1.getEditor().setText(seperated_combobox[0]);
							label_2.setText(seperated_combobox[1]);
							ComboBox2.getEditor().setText(seperated_combobox[1]);
							label_3.setText(seperated_combobox[2]);
							ComboBox3.getEditor().setText(seperated_combobox[2]);
							label_4.setText(seperated_combobox[3]);
							ComboBox4.getEditor().setText(seperated_combobox[3]);
							line = input.readLine();
			                String[] seperated_keyvalue = line.split(" ");
			                server_data.mouth_action=Integer.valueOf(seperated_keyvalue[0]);
			                server_data.eyebrow_action=Integer.valueOf(seperated_keyvalue[1]);
			                server_data.smile_action=Integer.valueOf(seperated_keyvalue[2]);
			                server_data.nose_action=Integer.valueOf(seperated_keyvalue[3]);
			                line = input.readLine();
			                String[] seperated_sensitive = line.split(" ");
			                mouth_slider.adjustValue(Double.valueOf(seperated_sensitive[0]));
			                server_data.mouth_sensitiveness=Float.valueOf(seperated_sensitive[0]);
			                eyebrow_slider.adjustValue(Double.valueOf(seperated_sensitive[1]));
			                server_data.eyebrow_sensitiveness=Float.valueOf(seperated_sensitive[1]);
			                smile_slider.adjustValue(Double.valueOf(seperated_sensitive[2]));
			                server_data.smile_sensitiveness=Float.valueOf(seperated_sensitive[2]);
			                nose_slider.adjustValue(Double.valueOf(seperated_sensitive[3]));
			                server_data.nose_sensitiveness=Float.valueOf(seperated_sensitive[3]);
			                statusbar_text.setText("Data loaded");
			            }
			            finally {
			                input.close();
			            }
			        }
			        catch (IOException ex){
			            //ex.printStackTrace();
			        	System.out.println("Data loading error - file is not exits");
			        }
			        System.out.println(contents.toString());
			    
			    });
			    
			    VBox vbox_all= new VBox();
			    vbox_all.setPadding(new Insets(15, -10, 0, 0));
			    vbox_all.getChildren().addAll(hbox_store,hbox_check,hbox_label,hbox_combo,hbox_slider_labels,hbox_sliders,hbox_button,statusbar);
				root.getChildren().add(vbox_all);
		        primaryStage.setTitle("Facial Gesture Detection");
		        primaryStage.setScene(scene);
		        primaryStage.show();
		        Alert calibration = new Alert(AlertType.CONFIRMATION);
		        calibration.setTitle("Calibration");
		        calibration.setHeaderText("Wait util facial point rested at right position");
		        calibration.setContentText("When it is done, click Ok to continue");
		        Optional<ButtonType> result = calibration.showAndWait();
		        if (result.get() == ButtonType.OK){
		     
	    	   server_data.calibration_flag=true;
			
	        	
	        } else {
	            // ... user chose CANCEL or closed the dialog
	        }
	        
	        
	    }

public static void main(String[] args) {
		 try 
	        { 
			 
	         server_data obj=new server_data();  			//start sub thread for starting a TCP server, and detecting facial gestures
	         Thread start_server =new Thread(obj);  
	         start_server.start(); 
	         launch(args);									//launch the interface
	        } 
	         catch (Exception e) 
	         { 
	         	System.out.println(e.toString()); 
	         } 
	     } 
}

/*
// a thread for retrieving cursor coordinates, it's for experiment

class global_mouse_pos implements Runnable{
 public void run(){  
	while(true)	{
		int x_pos=MouseInfo.getPointerInfo().getLocation().x;
		int y_pos=MouseInfo.getPointerInfo().getLocation().y;
	//	System.out.println("current coordinates is " +x_pos +" , "+y_pos);
	}
	}
}
*/

class server_data implements Runnable{
	public static ServerSocket socket; 
	public static Socket incoming; 
    BufferedReader readerIn; 
    PrintStream printOut; 
    public int count=0;
    public int framerate=6;
    int port = 8080; 
   
    public float[] sample;
    public int frame=0;
    public boolean isrun=true;
	public static Boolean calibration_flag=false;			//if true, calibration function will be trigger
	public static int mouth_action=0;						//key value for each gestures, what key will be activated after a gesture
	public static int eyebrow_action=0;
	public static int smile_action=0;
	public static int nose_action=0;
	public static float mouth_sensitiveness=0.5f;			//variables for sensitiveness, default value is 0.5
	public static float eyebrow_sensitiveness=0.5f;
	public static float smile_sensitiveness=0.5f;
	public static float nose_sensitiveness=0.5f;
	public static boolean pause_m=true;						//pause a gesture detection
	public static boolean pause_e=true;
	public static boolean pause_s=true;
	public static boolean pause_n=true;
	float mouth_open_initial;     							//initial distances for calibration purpose
	float mouth_width_initial;
	float ridge_width_initial;
	float left_eyebrow_initial;
	float right_eyebrow_initial;
	float mouth_width2_initial;
	float mouth_width3_initial;
	float left_eyebrow_nose;
	float right_eyebrow_nose;
	float right_eyebrow_noseside;
	float left_eyebrow_noseside;
	float average_allpoints_inital;
	float left_eyebrow_var;
	float right_eyebrow_var;
	float facial_variance;
	float [] points_distance;
	int key_press_frequency=1;  //Define a key stroke rate by multiplying counter_frame_rates , a 1/7.5 second, if counter_frame_rates is 4 frames
	int counter_frame_rates=4;
	int pause_counter=0;
	int times_counter_ck1=key_press_frequency*counter_frame_rates;
	int times_counter_ck2=key_press_frequency*counter_frame_rates;
	int times_counter_ck3=key_press_frequency*counter_frame_rates;
	int times_counter_ck4=key_press_frequency*counter_frame_rates;
	
	public void terminate() {
        isrun = false;
    }

	  public void run(){  
		  
		  System.out.println(">> Starting TCP Server "); 
	        try 
	        { 
	            // Establishing local server at port 8080, so it can retrieve facial data from Beyond Reality Face SDK
	        	socket = new ServerSocket(port); 
	            incoming = socket.accept(); 
	            //Creating data stream for Lab streaming layer, if you use facial data for cleanning up EEG singal, keep it
	            printOut = new PrintStream(incoming.getOutputStream()); 
	            printOut.println("Enter EXIT to exit.\r"); /*
	            System.out.println("Creating a new StreamInfo...");
	            LSL.StreamInfo info = new LSL.StreamInfo("Facial_data","EEG",136,100,LSL.ChannelFormat.float32,"myuid324457");
	            System.out.println("Creating an outlet...");
	            LSL.StreamOutlet outlet = new LSL.StreamOutlet(info);
	            */
	            System.out.println("Sending data...");
	            String[] substring=new String[136];
	            sample = new float[136];
	            String str; 								//Temporally save one frame of facial data(0 - 67) 
	            Boolean m_pressed_flag=false;				//the four flag varables is for preventing faulty detection
	            Boolean e_pressed_flag=false;				//Once the application detect one gesture, it will turn off
	            Boolean s_pressed_flag=false;
	            Boolean n_pressed_flag=false;
	            
	            Robot robot = new Robot();
	            readerIn = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
	            System.out.println("Sever initialized ...");
	            //Receiving data from BRF program
	            while ((str= readerIn.readLine()) != null) 
	            { 
		            substring=str.split(",");					//split facial data string received from Beyond Reality Face, and save it to array ( sample) 
	            	for (int k=0;k<136;k++){					//data structure is X,Y,X,Y... from point 0 - 67, so there will be 136 values.
	           		 sample[k] = Float.parseFloat(substring[k]);
	           		}
	            	
	            //saving calibrated values,      these values is key for calculating threshold values of different gestures
	            	if(calibration_flag){
	            		mouth_open_initial=cal_distance(51,57);			//initial distance while people have their mouth closed
	            		mouth_width_initial=cal_distance(48,54);		//3 initial distances for smile detection
	            		mouth_width2_initial=cal_distance(49,53);
	            		mouth_width3_initial=cal_distance(59,55);
	            		ridge_width_initial=cal_distance(38,44);		//initial distance for detecting a smile
	            		left_eyebrow_initial=cal_distance(23,25);		//height of left eyebrow initial distance for calculating variance while people move close to or move away from screen
	            		right_eyebrow_initial=cal_distance(17,19);		//height of right eyebrow, initial distance for calculating variance while people move close to or move away from screen
	            		left_eyebrow_nose=cal_distance(24,37);			//initial distance between left eyebrow to nose, for detecting raising left eyebrow
	            		right_eyebrow_nose=cal_distance(18,45);			//initial distance between right eyebrow to nose
	            		left_eyebrow_noseside=cal_distance(24,38);		//initial distance between left eyebrow to lower nose, for detecting snarling gesture
	            		right_eyebrow_noseside=cal_distance(18,44);		//initial distance between right eyebrow to lower nose, for detecting snarling gesture
	            		facial_variance=(float)getVariance(0,15,67);	// tried to use calculating variance for preventing faulty detection, non-finished function
	            		left_eyebrow_var=(float)getVariance(21,6,37);
	            		right_eyebrow_var=(float)getVariance(15,6,45);
	            		calibration_flag=false;							//after a calibration, change the flag to false
	            	}
	            		
	            		//calculating the variation, how much users move close or away from the screen (percentage value) according to initial distance
            			float current_ridge_width=cal_distance(38,44);
	            		float nose_ridge_offset_ratio=current_ridge_width/ridge_width_initial;
	            		float calculated_closed_mouth=mouth_open_initial*nose_ridge_offset_ratio;     //After the movement, calculate the distance between lips based on initial lips distance, (not current distance)
	            		Boolean reyebrow_flag=false,leyebrow_flag=false;							  //whether right eyebrow or left eyebrow is raised
	            		
	            		float current_lips_distance=cal_distance(51,57);							  //calculating current distance
	            		double mouth_threhold=calculated_closed_mouth*(1.35+mouth_sensitiveness*(1.95-1.35));  			//calculating the threshold value based on the sensitiveness assigned by users, minimum value is 1.35 times as big as mouth closed, maximum value is 1.95 times
	            		
	            	if(pause_m && Main.check_mouth.isSelected()){									  //whether pause function and the mouth check box are activated   
	            		try{
	            			++times_counter_ck1;													  //time counter for activating an action after a certain period of break, the application will not be able to keep sending a key to OS
	            		
	            			if(m_pressed_flag==false && (times_counter_ck1%(key_press_frequency*counter_frame_rates))==0){         
	            			
	            				if(current_lips_distance>mouth_threhold){							  //if current distance between lips is bigger than threshold
	            				
	            					times_counter_ck1=0;												//reset the time counter
	            					if(mouth_action==KeyEvent.BUTTON1_MASK){							//Added left click for experiment
		            					robot.mousePress(InputEvent.BUTTON1_MASK);
		            				}else if(mouth_action==KeyEvent.BUTTON3_MASK){						//activating right click 
		            						robot.mousePress(InputEvent.BUTTON3_MASK);
		            						robot.mouseRelease(InputEvent.BUTTON3_MASK);
		            				}else
		            					robot.keyPress(Integer.valueOf(mouth_action));					//press the mapped key 
		            				m_pressed_flag=true;												//Make sure the App can only start to detect month closed gesture after a month open, otherwise Release a key command will be sent constantly 
		            				pause_e=false;														//disable other gestures detection function for a half second to prevent faulty detection, nose sometimes will affect smile
			            			pause_s=false;														//after users moved their nose, smile will be detected immediately
			            			pause_n=false;
		            				System.out.println("Mouth opened ");
		            				}
	            				}
	            		
	            		if(m_pressed_flag && current_lips_distance<calculated_closed_mouth*1.35){   	//current distance is smaller than 1.35 times of calculated distance while users have their mouth closed
	            			if(mouth_action==KeyEvent.BUTTON1_MASK){
            					robot.mouseRelease(InputEvent.BUTTON1_MASK);
            				}else if(mouth_action==KeyEvent.BUTTON3_MASK){								//if it's right click ,do nothing
	            			
            					}else
            						robot.keyRelease(Integer.valueOf(mouth_action));
	            				m_pressed_flag=false;
	            				System.out.println("Mouth closed");
	            				Thread.sleep(500);													          
	            				pause_e=true;
	            				pause_s=true;
	            				pause_n=true;
	            				}
	            		}catch(Exception ex) {
	            				System.out.println("Output errors through mouth detection");
	            			}
	            		
	            		}
	            		
	            		
	            		try{
	            			if(pause_e && Main.check_eyebrow.isSelected()){
	            				//calculating the changing ratio of left eyebrow to reduce the fault detection when users move close or far away from screen
	    	            		//to do that using distance between point 23 and point 25 as standard value
	            				float current_leyebrow_height=cal_distance(23,25);
	    	            		float leyeborw_height_offset_ratio=current_leyebrow_height/left_eyebrow_initial;
	    	            		float calculated_leyebrow_distance=left_eyebrow_nose*leyeborw_height_offset_ratio;
	    	            		float current_leyebrow_nose_distance=cal_distance(24,37);
	            				
	    	            		float current_reyebrow_height=cal_distance(17,19);
	            				float reyeborw_height_offset_ratio=current_reyebrow_height/right_eyebrow_initial;
	            				float calculated_reyebrow_distance=right_eyebrow_nose*reyeborw_height_offset_ratio;
	            				float current_reyebrow_nose_distance=cal_distance(18,45);
	            				//calculating the threshold value based on the sensitiveness assigned by users, minimum value is 1.15 times as big as eyebrow in normal, maximum value is 1.5 times
	            				double leyebrow_nose_threhold=calculated_leyebrow_distance*(1.15+eyebrow_sensitiveness*(1.5-1.15)); 
	            				double reyebrow_nose_threhold=calculated_reyebrow_distance*(1.15+eyebrow_sensitiveness*(1.5-1.15));
	            				
	            				if(current_leyebrow_nose_distance>leyebrow_nose_threhold){
	            					leyebrow_flag=true;
	            				}       		
	            				if(current_reyebrow_nose_distance>reyebrow_nose_threhold){
	            					reyebrow_flag=true;
	            				}
	            				if(leyebrow_flag && reyebrow_flag  ){		   //If both eyebrows is raised
		            			
		            					++times_counter_ck2;
	            			
		            					if(e_pressed_flag==false && (times_counter_ck2%(key_press_frequency*counter_frame_rates))==0){
		            						times_counter_ck2=0;
		            						if(eyebrow_action==KeyEvent.BUTTON1_MASK){
		            							robot.mousePress(InputEvent.BUTTON1_MASK);
		            						}else if(eyebrow_action==KeyEvent.BUTTON3_MASK){
		            							robot.mousePress(InputEvent.BUTTON3_MASK);
		            							robot.mouseRelease(InputEvent.BUTTON3_MASK);
		            						}else
		            							robot.keyPress(Integer.valueOf(eyebrow_action));
		            						e_pressed_flag=true;
		            						pause_m=false;
		            						pause_s=false;
		            						pause_n=false;
		            						System.out.println("Eyebrow Raised");
		            					}
	            			
	            			
	            				}
	            		if(e_pressed_flag && current_leyebrow_nose_distance<leyebrow_nose_threhold-calculated_leyebrow_distance*0.05){
	            			leyebrow_flag=false;
	            		}
	            				
	            		if(e_pressed_flag && current_reyebrow_nose_distance<reyebrow_nose_threhold-calculated_reyebrow_distance*0.05){
	    	            	reyebrow_flag=false;
	    	            	if(eyebrow_action==KeyEvent.BUTTON1_MASK){
            					robot.mouseRelease(InputEvent.BUTTON1_MASK);
            				}else if(eyebrow_action==KeyEvent.BUTTON3_MASK){
	            				//robot.mouseRelease(InputEvent.BUTTON3_MASK);
	            			}else
	            				robot.keyRelease(Integer.valueOf(eyebrow_action));
	    	            	System.out.println("Eyebrow backed");
	    	            	e_pressed_flag=false;
	    	            	Thread.sleep(500);
	    	            	pause_m=true;
	    	            	pause_s=true;
	    	            	pause_n=true;
	    	            }
	            		
	            		}
	            		}catch(Exception e) {
	            			
	            			System.out.println("Output errors through eyebrows detection");
	            		}
	            		
	            		//detecting smile expression
	            		
	            		float current_mouth_width=cal_distance(48,54);
	            		float calculated_mouth_width=mouth_width_initial*nose_ridge_offset_ratio;
	            		
	            		
	            		float calculated_leyebrow_noseside=left_eyebrow_noseside*nose_ridge_offset_ratio;
	            		float calculated_reyebrow_noseside=right_eyebrow_noseside*nose_ridge_offset_ratio;
	            		float current_leyebrow_noseside=cal_distance(24,38);
	            		float current_reyebrow_noseside=cal_distance(18,44);
	            		
	            		double smile_threhold=calculated_mouth_width*(1.05+smile_sensitiveness*(1.17-1.05));
	            		
	            	try{
	            		if(pause_s && Main.check_smile.isSelected()){
	            			++times_counter_ck3;
	            			
	            			if(s_pressed_flag==false && (times_counter_ck3%(key_press_frequency*counter_frame_rates))==0){
	            				if(current_mouth_width>smile_threhold ){
	            					
	            						times_counter_ck3=0;
	            						if(smile_action==KeyEvent.BUTTON1_MASK){
			            					robot.mousePress(InputEvent.BUTTON1_MASK);
			            				}else if(smile_action==KeyEvent.BUTTON3_MASK){
	        	            				robot.mousePress(InputEvent.BUTTON3_MASK);
	        	            				robot.mouseRelease(InputEvent.BUTTON3_MASK);
	        	            			}else
	        	            				robot.keyPress(Integer.valueOf(smile_action));
	        	            				s_pressed_flag=true;
	        	            				pause_m=false;
	    	            					pause_e=false;
	    	            					pause_n=false;
	        	            				System.out.println("Smiled");
	        	            			
	            					}
	            				}
	            			
	            				if(s_pressed_flag && (current_mouth_width<calculated_mouth_width*1.05)){
	            					System.out.println("Normal" );
	            					if(smile_action==KeyEvent.BUTTON1_MASK){
		            					robot.mouseRelease(InputEvent.BUTTON1_MASK);
		            				}else if(smile_action==KeyEvent.BUTTON3_MASK){
	    	            				//robot.mouseRelease(InputEvent.BUTTON3_MASK);
	    	            			}else
	    	            				robot.keyRelease(Integer.valueOf(smile_action));
	            					s_pressed_flag=false;
	            					
	            					Thread.sleep(500);
	            					pause_m=true;
	            					pause_e=true;
	            					pause_n=true;
	            					
	            				}
	            			
	            		}
	            		}catch(Exception ex) {
	            			System.out.println("Output errors through smile detection");
	            		}
	            		//detecting a kiss
	            		/*
	            		//float current_mouth_width=cal_distance(48,54);
	            		float calculated_mouth2_width=mouth_width2_initial*nose_ridge_offset_ratio;
	            		float calculated_mouth3_width=mouth_width3_initial*nose_ridge_offset_ratio;
	            		float current_mouth2_width=cal_distance(49,53);
	            		float current_mouth3_width=cal_distance(59,55);
	            		float calculated_mouth_width_min=calculated_mouth_width-(float)(calculated_mouth_width*0.15);
	            		float calculated_mouth2_width_min=calculated_mouth2_width-(float)(calculated_mouth2_width*0.15);
	            		float calculated_mouth3_width_min=calculated_mouth3_width-(float)(calculated_mouth3_width*0.15);
	            		try{
	            			Boolean k_pressed_flag=false;
	            			if(Main.check_kiss.isSelected()){
	            				if(current_mouth_width<calculated_mouth_width_min && current_mouth2_width<calculated_mouth2_width && current_mouth3_width<calculated_mouth3_width){
	            					if(kiss_action==KeyEvent.BUTTON3_MASK){
	    	            				robot.mousePress(InputEvent.BUTTON3_MASK);
	    	            				robot.mouseRelease(InputEvent.BUTTON3_MASK);
	    	            			}else
	    	            				robot.keyPress(Integer.valueOf(kiss_action));
	            					k_pressed_flag=true;
	            					System.out.println("///////////////////////////////////////////////// kissed");
	            		}
	            		if(k_pressed_flag && (current_mouth_width<calculated_mouth_width+(calculated_mouth_width*0.1) && current_mouth_width>calculated_mouth_width-(calculated_mouth_width*0.1))){
	            			 robot.keyRelease(Integer.valueOf(kiss_action));
	            			 k_pressed_flag =false;
	            			System.out.println("///////////////////////////////////////////////// without kiss");
	            			
	            		
	            		}
	            		
	            		}
	            		}catch(Exception ex) {}
	            		
	            		*/
	            		//detecting nose movement
	            		try{
	            			if(pause_n && Main.check_nose.isSelected()){
	            					
	            					float calculated_leyebrow_noseside_min=calculated_leyebrow_noseside-(float)(calculated_leyebrow_noseside*(0.065+nose_sensitiveness*(0.095-0.065)));
	        	            		float calculated_mreyebrow_noseside_min=calculated_reyebrow_noseside-(float)(calculated_reyebrow_noseside*(0.065+nose_sensitiveness*(0.095-0.065)));
        	            			++times_counter_ck4;
        	            			if(n_pressed_flag==false && (times_counter_ck4%(key_press_frequency*counter_frame_rates))==0){
        	            				
        	            				if(current_leyebrow_noseside<calculated_leyebrow_noseside_min && current_reyebrow_noseside<calculated_mreyebrow_noseside_min){
        	            					times_counter_ck4=0;
        	            					
        	            					if(nose_action==KeyEvent.BUTTON1_MASK){
    			            					robot.mousePress(InputEvent.BUTTON1_MASK);
    			            				}else if(nose_action==KeyEvent.BUTTON3_MASK){
        	            						robot.mousePress(InputEvent.BUTTON3_MASK);
        	            						robot.mouseRelease(InputEvent.BUTTON3_MASK);
        	            					}else
        	            						robot.keyPress(Integer.valueOf(nose_action));
        	            					n_pressed_flag=true;
        	            					System.out.println("Nose up");
        	            					pause_m=false;
        	    	            			pause_e=false;
        	    	            			pause_s=false;
        	            				}
        	            		}
        	            		
	            				
	            	if(n_pressed_flag && (current_leyebrow_noseside>calculated_leyebrow_noseside_min*1.06)){
	            		if(nose_action==KeyEvent.BUTTON1_MASK){
        					robot.mouseRelease(InputEvent.BUTTON1_MASK);
        				}else if(nose_action==KeyEvent.BUTTON3_MASK){
            				
            			}else
            				robot.keyRelease(Integer.valueOf(nose_action));
	            			 n_pressed_flag =false;
	            			System.out.println("Nose backed");
	            			Thread.sleep(500);
	            			pause_m=true;
	            			pause_e=true;
	            			pause_s=true;
	            		  }
	            		}
	            		}catch(Exception ex) {
	            			System.out.println("Output errors through nose detection");
	            			}
	            	frame++;
	            	//outlet.push_sample(sample);
	            } 
	          //  outlet.close();								//Close lab streaming layer
	          //  info.destroy();
	        } 
	        catch (Exception e){ 
	            System.out.println(e.toString()); 
	        } 
		}   
	  public server_data() 
	    { 
		} 
	  //calculate average distance from points(consecutive) to a target point, eg, point 1 to point 67, point 2 to point 67.... point 14 to 67, and calculate the average
	    public float average_allpoints(int start_point, int number, int target_point){   
	    	points_distance= new float[number];
	    	for(int i=0; i<number;i++){
	    		points_distance[i]=cal_distance(i+start_point,target_point);
	    	}
	    	
	    	
	    	int size = points_distance.length;
	    	float sum = 0;
	        for(float a : points_distance){
	        	
	            sum += a;
	        }
	        return sum/size;
	    }
	    // calculate variance from points(consecutive) to a target point
	    double getVariance(int start_point, int number, int target_point)
	    {
	        double mean = average_allpoints(start_point, number, target_point);
	        double temp = 0;
	        int size = points_distance.length;
	        for(double a :points_distance){
	            temp += (a-mean)*(a-mean);
	           
	        }
	        return Math.sqrt(temp/size);
	    }
	    
	    //calculate distance beween two points
	    public float cal_distance(int point_one, int point_two){
	    	 int point_one_x=point_one*2;
	    	 int point_two_x=point_two*2;
	    	 return (float)Math.sqrt(Math.pow(sample[point_one_x]- sample[point_two_x],2)+Math.pow(sample[point_one_x+1] - sample[point_two_x+1],2));
	    }
	
	
}
