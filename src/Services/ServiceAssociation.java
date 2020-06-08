/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;




import java.util.*;
import Entities.Association;
import Utils.MaConnection;
import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Tarek
 */
public class ServiceAssociation {
    
    public ArrayList<Association> Associations;
    private ConnectionRequest req;
    public boolean resultOK;

    
    public static ServiceAssociation instance=null;
    
     public ServiceAssociation() {
        req = new ConnectionRequest();
    }
     
     public static ServiceAssociation getInstance() {
        if (instance == null) {
            instance = new ServiceAssociation();
        }
        return instance;
    }
        public ArrayList<Association> parseAssociations(String jsonText){
        try {
            Associations=new ArrayList<>();
            JSONParser j = new JSONParser();
            Map<String,Object> PostsListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String,Object>> list = (List<Map<String,Object>>)PostsListJson.get("root");
            for(Map<String,Object> obj : list){
                Association p = new Association();
                float id = Float.parseFloat(obj.get("idAssociation").toString());
                p.setId_Association((int)id);
                p.setNom_Association(obj.get("nomAssociation").toString());
                p.setEmail_Association(obj.get("emailAssociation").toString());
                p.setObjectif_Association(obj.get("objectifAssociation").toString());
                p.setPassword_Association(obj.get("passwordAssociation").toString());
                p.setAddress_Association(obj.get("addressAssociation").toString());
                p.setDescription_Association(obj.get("descriptionAssociation").toString());
                p.setImage_name(obj.get("imageName").toString());
               
                System.out.println("obj : "+p);
                Associations.add(p);
            }
        } catch (IOException ex) {
        }
        return Associations;
    }
       public ArrayList<Association> getAllAssociations(){
        String url = MaConnection.BASE_URL+"Donation/"+"getAllAssociations";
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Associations = parseAssociations(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return Associations;
    }
    
     public boolean addAssociation(Association t) {
        String url = MaConnection.BASE_URL + "Donation/addAsso/" + t.getNom_Association()+ "/" + t.getObjectif_Association()+ "/" + t.getEmail_Association()+ "/" + t.getPassword_Association()+ "/" + t.getAddress_Association()+ "/" + t.getType_Association()+ "/" + t.getDescription_Association()+ "/" + t.getImage_name();
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
      public boolean updateAssociation(Association t) {
        String url = MaConnection.BASE_URL + "Donation/editAsso/" + t.getId_Association()+ "/" + t.getNom_Association()+ "/" + t.getObjectif_Association()+ "/" + t.getEmail_Association()+ "/" + t.getPassword_Association()+ "/" + t.getAddress_Association()+ "/" + t.getType_Association()+ "/" + t.getDescription_Association()+ "/" + t.getImage_name();
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultOK = req.getResponseCode() == 200; //Code HTTP 200 OK
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return resultOK;
    }
      
     public ArrayList<Association> getbyId(int id){
        String url = MaConnection.BASE_URL+"Donation/"+"getbyIdMobile/"+id;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                Associations = parseAssociations(new String(req.getResponseData()));
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueueAndWait(req);
        return Associations;
    }
    
}
