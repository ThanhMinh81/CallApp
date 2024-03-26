package com.example.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.Model.ChatMessage;
import com.example.Model.User;
import com.example.Model.UserWithChat;


import java.util.ArrayList;
import java.util.List;

@Dao
public interface MessageDao {

    @Insert
    void insert(ChatMessage message);

    @Query("SELECT * FROM messages WHERE userId = :userId")
    List<ChatMessage> getMessagesByUserId(String userId);


//    @Query("SELECT * FROM messages LEFT JOIN personDB ON messages.userId = personDB.id")
//    void deleteMessagesByUserId2(String userId);

    @Insert
    void insertListPerson(List<User> userList);

    @Query("SELECT *  FROM personDB")
    List<User> getListPerson();

    @Transaction
    @Query("SELECT * FROM personDB")
    List<UserWithChat> getJoinChatPersons();


    //them
    // xoa tin nhan theo id user
    @Query("DELETE FROM messages WHERE userId = :userId")
    void deleteMessagesByUserId(int userId);

    // xoa xong thi them danh sach tin nhan moi lai
    @Insert
    void insertList(List<ChatMessage> messages);


//    @Query("UPDATE messages SET userId = :end_address WHERE userId = :id")
//    int updateTour(List<ChatMessage> chatMessages , int id);

//    @Query("UPDATE messages SET messageText = :strings WHERE id = :idUser")
//    int updateTour(List<String> strings ,  int idUser);




}

