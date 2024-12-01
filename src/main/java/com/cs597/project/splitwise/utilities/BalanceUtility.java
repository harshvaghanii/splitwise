package com.cs597.project.splitwise.utilities;

import com.cs597.project.splitwise.dto.UserDTO;

public class BalanceUtility {
    public static String generateBalanceId(UserDTO user1, UserDTO user2) {
        Long user1ID = user1.getId();
        Long user2ID = user2.getId();
        return user1ID < user2ID ?
                user1ID + "_" + user2ID :
                user2ID + "_" + user1ID;
    }
}
