
package Gui;

import Entities.Action;
import Services.ServiceAction;
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

public class addAction extends BaseForm{
    
     public addAction(Resources res) {
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
        TextField date = new TextField("", "Date", 20, TextField.ANY);
        TextField location = new TextField("", "Location", 20, TextField.ANY);
        TextField nbv = new TextField("", "NbV", 20, TextField.NUMERIC);
        TextField description = new TextField("", "Description", 20, TextField.ANY);
        //TextField image = new TextField("", "Image", 20, TextField.ANY);
        //TextField password = new TextField("", "Password", 20, TextField.PASSWORD);
        //TextField confirmPassword = new TextField("", "Confirm Password", 20, TextField.PASSWORD);
        //id.setSingleLineTextArea(false);
        name.setSingleLineTextArea(false);
        date.setSingleLineTextArea(false);
        location.setSingleLineTextArea(false);
        nbv.setSingleLineTextArea(false);
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

                        Action t = new Action(15,name.getText(), date.getText(), location.getText(), Integer.parseInt(nbv.getText()), description.getText(), "5e8563e9429d0_single_blog_2.png");
                        if( ServiceAction.getInstance().addAction(t))
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
                new Label("Add Action", "LogoLabel"),
               // new FloatingHint(id),
                //createLineSeparator(),
                new FloatingHint(name),
                createLineSeparator(),
                new FloatingHint(date),
                createLineSeparator(),
                new FloatingHint(location),
                createLineSeparator(),
                new FloatingHint(nbv),
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
