package com.driver;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WhatsappService {

    static int GroupNum=0;
    static int messageId=0;
    WhatsappRepository whatsappRepository=new WhatsappRepository();
    public String createUser(String name, String mobile) throws Exception {
        if(whatsappRepository.getUserMobile().contains(mobile))
        {
            throw new Exception("User already exists");
        }

        User user=new User(name,mobile);
        whatsappRepository.userList.add(user);
        return "SUCCESS";
    }

    public Group createGroup(List<User> users){

//        users=whatsappRepository.userList;
        Group group;

        if(users.size()==2)
        {
            group=new Group(users.get(1).getName(),users.size());
            whatsappRepository.getAdminMap().put(group,users.get(0));
            return group;
        }

        group=new Group("Group "+GroupNum,users.size());
        whatsappRepository.getAdminMap().put(group,users.get(0));
        return group;

    }

    public int createMessage(String content)
    {

        return messageId++;
    }

    public int sendMessage(Message message, User sender, Group group) throws Exception{
        boolean flag=false;
        if(whatsappRepository.getGroupUserMap().containsKey(group)){
            List<User> list=whatsappRepository.getGroupUserMap().get(group);



            for(User u: list)
            {
                if(u.equals(sender))
                {
                    flag=true;
                    break;
                }
            }

            if(flag==false)
            {
                throw new Exception("You are not allowed to send message");
            }
        }
        else {
            throw new Exception("Group does not exist");
        }

        return whatsappRepository.getGroupMessageMap().get(group).size();
    }

    public String changeAdmin(User approver, User user, Group group) throws Exception {

        boolean f=false;
        if(whatsappRepository.getAdminMap().containsKey(group))
        {
            if(whatsappRepository.getAdminMap().get(group).equals(approver))
            {
                List<User> list=whatsappRepository.getGroupUserMap().get(group);

                for(User u : list)
                {
                    if(u.equals(user))
                    {
                        f=true;
                        break;
                    }
                }
            }
            else {
                throw new Exception("Approver does not have rights");
            }
        }
        else {
            throw  new Exception("Group does not exist");
        }

        if(f==false)
        {
            return "User is not a participant";
        }

        whatsappRepository.getAdminMap().put(group,user);
        return "SUCCESS";
    }

    public int removeUser(User user) throws Exception{
        boolean f=false;
//        Group g=null;
        Group k=null;

        for( Group g: whatsappRepository.getGroupUserMap().keySet())
        {
           List <User> list=whatsappRepository.getGroupUserMap().get(g);

           for(int i=0;i<list.size();i++)
           {
               if(list.get(i).equals(user))
               {
                   if(i==0)
                   {
                       throw new Exception("Cannot remove admin");
                   }
                   else {
                       list.remove(i);
                       k=g;
                       f=true;
                   }
               }
           }

           if(f) break;

        }
        if(f==false)
        {
            throw new Exception("User not found");
        }

        return whatsappRepository.getGroupMessageMap().get(k).size();
    }

    public String findMessage(Date start, Date end, int K) throws Exception{
        return null;
    }
}
