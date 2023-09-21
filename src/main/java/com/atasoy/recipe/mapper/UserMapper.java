package com.atasoy.recipe.mapper;

import com.atasoy.recipe.entity.User;
import com.atasoy.recipe.model.UserModel;

import java.util.Date;

public class UserMapper {
    public static UserModel toUserModel(User user) {
        UserModel userModel = new UserModel();
        userModel.setId(user.getId());
        userModel.setUserName(user.getUserName());
        userModel.setFirstName(user.getFirstName());
        userModel.setLastName(user.getLastName());
        userModel.setRole(user.getRole());
        return userModel;
    }

    public static User toUser(UserModel userModel) {
        User user = new User();
        user.setFirstName(userModel.getFirstName());
        user.setLastName(userModel.getLastName());
        user.setDeleted(userModel.isDeleted());
        user.setRole(userModel.getRole());
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setDeleteDate(null);
        user.setUserName(userModel.getUserName());
        user.setPassword(userModel.getPassword());
        return user;
    }
}
