package com.example.uitest.autoaddcontact;

import android.util.Log;

import java.util.Random;

/**
 * 生成联系人姓名电话信息
 * Created by Administrator on 17-6-21.
 */

public class ContactBuilder {
    private String TAG = "AddContact";;
    private String[] first_name = {"赵","钱","孙","李","周","吴","郑","王",
            "冯","陈","楮","卫","蒋","沈","韩","杨","祝","聂",
            "朱","秦","尤","许","何","吕","施","张","向","莫",
            "孔","曹","严","华","金","魏","陶","姜","左","幕"};
    private String[] name = {"若雨","雨婷","晟涵","美莲","雅静" ,"梦舒", "婳祎","檀雅", "若翾",
            "熙雯", "语嫣", "妍洋", "滢玮", "沐卉", "琪涵", "佳琦", "伶韵","思睿",
            "爱轩", "高雅", "杨扬", "高阳", "致远", "智明", "智鑫", "智勇" ,"智敏",
            "晗昱", "晗日", "涵畅" ,"涵涤", "乐童", "乐贤", "护心", "乐欣", "乐逸"};
    private String[] phone_number_head = {"131","132","133","134","135","136","137","138","139",
            "150","151","158","185","188","189"};
    private char[] eng_name = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    private Contact contact;
    private StringBuilder stringBuilder = new StringBuilder();
    private Random random = new Random(System.currentTimeMillis());


    public ContactBuilder(){
        this.contact = new Contact();
    }

    public Contact getEngContact(){

        stringBuilder.append(eng_name[random.nextInt(eng_name.length)])
                    .append(eng_name[random.nextInt(eng_name.length)])
                    .append(eng_name[random.nextInt(eng_name.length)])
                    .append(eng_name[random.nextInt(eng_name.length)])
                    .append(eng_name[random.nextInt(eng_name.length)]);
        contact.setName(stringBuilder.toString());
        stringBuilder.delete(0,stringBuilder.length());
        contact.setPhone_number(createPhoneNumber());

        Log.e(TAG,contact.getName()+":"+contact.getPhone_number());
        return contact;
    }
    public Contact getChContact(){

        //随机取数组的元素组成姓名
        stringBuilder.append(first_name[random.nextInt(first_name.length)]).append(name[random.nextInt(name.length)]);
        contact.setName(stringBuilder.toString());
        stringBuilder.delete(0,stringBuilder.length());
        //随机生成电话号码
        contact.setPhone_number(createPhoneNumber());
        Log.e(TAG,contact.getName()+":"+contact.getPhone_number());
        return contact;
    }

    private String createPhoneNumber(){
        String phone_number ;
        stringBuilder.append(phone_number_head[random.nextInt(phone_number_head.length)])
                .append(String.valueOf(random.nextInt(8999)+1000))
                .append(String.valueOf(random.nextInt(8999)+1000));
        phone_number = stringBuilder.toString();
        stringBuilder.delete(0,stringBuilder.length());
        return phone_number;
    }
}
