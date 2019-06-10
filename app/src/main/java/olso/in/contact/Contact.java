package olso.in.contact;

import java.util.HashMap;
import java.util.List;

public class Contact {
    private String ContactImage;
    private String ContactName;
    private String ContactNumber;
   public static boolean start=false;
    public static HashMap<String,String> hashMap;

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        this.ContactImage = ContactImage;
    }

    public String getContactName() {
        return ContactName;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }
}
