package com.example.uitest.autoaddcontact;

/**
 * Created by Administrator on 17-6-21.
 *
 *
 */

public class Contact {
    private String name;
    private String phone_number;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Contact(){};

    public Contact(String name,String phone_number){
        this.name = name;
        this.phone_number = phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }



}
