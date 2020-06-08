/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

/**
 *
 * @author Tarek
 */
import Entities.Association;
import Services.ServiceAssociation;
import com.codename1.components.FloatingHint;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.Resources;

public class addAssociation extends BaseForm {
    
    public addAssociation(Resources res) {
        super(new BorderLayout());
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        tb.setUIID("Container");
        getTitleArea().setUIID("Container");
        Form previous = Display.getInstance().getCurrent();
        tb.setBackCommand("", e -> previous.showBack());
        setUIID("SignIn");
        //TextField id = new TextField("", "ID", 20, TextField.NUMERIC); 
        TextField name = new TextField("", "Name", 20, TextField.ANY);
        TextField objectif  = new TextField("", "Goals", 20, TextField.ANY);
        TextField email = new TextField("", "Email", 20, TextField.ANY);
        TextField password = new TextField("", "Password", 20, TextField.PASSWORD);
        TextField confirmPassword = new TextField("", "Confirm Password", 20, TextField.PASSWORD);
        TextField address  = new TextField("", "Address", 20, TextField.ANY);
        TextField type = new TextField("", "Type", 20, TextField.ANY);
        TextField description = new TextField("", "Description", 20, TextField.ANY);
        //TextField image = new TextField("", "Image", 20, TextField.ANY);
        
        //id.setSingleLineTextArea(false);
        name.setSingleLineTextArea(false);
        objectif.setSingleLineTextArea(false);
        email.setSingleLineTextArea(false);
        password.setSingleLineTextArea(false);
        confirmPassword.setSingleLineTextArea(false);
        address.setSingleLineTextArea(false);
        type.setSingleLineTextArea(false);
        description.setSingleLineTextArea(false);
        //image.setSingleLineTextArea(false);
        Button next = new Button("Next");
        Button signIn = new Button("Sign In");
        signIn.addActionListener(e -> previous.showBack());
        signIn.setUIID("Link");
        Label alreadHaveAnAccount = new Label("Already have an account?");
         next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
//                if ((tfName.getText().length()==0)||(tfStatus.getText().length()==0))
//                    Dialog.show("Alert", "Please fill all the fields", new Command("OK"));
//                else
//                {
                    try {
                        //Action t = new Action();

                        Association t = new Association(name.getText(), objectif.getText(), email.getText(), password.getText(), address.getText(),type.getText(),description.getText(),"5e911d798ed9c_ras.png");
                        if( ServiceAssociation.getInstance().addAssociation(t))
                        {
                            displayAction c2 = new displayAction(res);
                            c2.show();
                            
                        }
                        else
                            Dialog.show("ERROR", "Server error", new Command("OK"));
                    } catch (NumberFormatException e) {
                        Dialog.show("ERROR", "Status must be a number", new Command("OK"));
                    }
                    
                }
                
                
//            }
        });
        
        
        Container content = BoxLayout.encloseY(
                new Label("Add Association", "LogoLabel"),
               // new FloatingHint(id),
                //createLineSeparator(),
                new FloatingHint(name),
                createLineSeparator(),
                new FloatingHint(objectif),
                createLineSeparator(),
                new FloatingHint(email),
                createLineSeparator(),
                new FloatingHint(password),
                createLineSeparator(),
                new FloatingHint(confirmPassword),
                createLineSeparator(),
                new FloatingHint(address),
                createLineSeparator(),
                new FloatingHint(type),
                createLineSeparator(),
                new FloatingHint(description),
                createLineSeparator()
                //new FloatingHint(image),
                //createLineSeparator()
        );
        content.setScrollableY(true);
        add(BorderLayout.CENTER, content);
        add(BorderLayout.SOUTH, BoxLayout.encloseY(
                next,
                FlowLayout.encloseCenter(alreadHaveAnAccount, signIn)
        ));
        
    }
    
    
}
