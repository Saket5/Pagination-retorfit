package com.example.payo;

import android.util.Patterns;
import android.widget.Toast;

class UserDetails {
    private String email,password,fullName;
    private String phoneNo,Address;
    public UserDetails()
    {}
    public UserDetails(String fullName,String email,String password,String phoneNo,String Address)
    {
        this.fullName=fullName;
        this.email=email;
        this.password=password;
        this.phoneNo=phoneNo;
        this.Address=Address;
    }
    public String getFullName(){
        return fullName;
    }
    public String getEmail()
    {
        return email;
    }
    public String getPassword(){
        return password;
    }
    public String getPhoneNo(){
        return phoneNo;
    }
    public String getAddress(){
        return Address;
    }
    public void setAddress(String address){
        this.Address=address;
    }
    public void setFullName(String fullName) {
        if(validateName(fullName)==null)
        this.fullName = fullName;
    }
    public void setEmail(String email)
    {
        this.email=email;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    private String validateName(String name) {
        if (name.trim().length() == 0) {
            return "Name cannot be empty";
        } else if (name.trim().matches("^[0-9]+$")) {
            return "Name cannot have numbers in it";
        } else if (!name.trim().matches("^[a-zA-Z][a-zA-Z ]++$")) {
            return "Invalid Name";
        }
        return null;
    }


}
