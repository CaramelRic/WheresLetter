package com.example.project;

public class Fake {
    public static boolean isFake(String letter,String[] label){
        boolean monitor = false;
        boolean screen = false;
        boolean laptop = false;
        boolean phone = false;
        boolean photo = false;
        boolean picture = false;
        if(letter!="M"){
            for(int i=0;i<3;i++){
                if(label[i]=="Monitor"){
                    monitor = true;
                }
            }
        }
        if(letter!="S"){
            for(int i=0;i<3;i++){
                if(label[i]=="Screen"){
                    screen = true;
                }
            }
        }
        if(letter!="l"){
            for(int i=0;i<3;i++){
                if(label[i]=="Laptop"){
                    laptop = true;
                }
            }
        }
        if(letter!="P"){
            for(int i=0;i<3;i++){
                if(label[i]=="Phone"){
                    phone = true;
                }
                if(label[i]=="Photo"){
                    photo = true;
                }
                if(label[i]=="Picture"){
                    picture = true;
                }
            }
        }

        return (monitor || screen || laptop || phone || photo || picture);
    }
}
