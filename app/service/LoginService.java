package app.service;

import app.model.User;
import java.util.HashMap;

public class LoginService {
    private HashMap<String, User> akunMap;

    public LoginService(HashMap<String, User> akunMap) {
        this.akunMap = akunMap;
    }

    public User login(String username, String password) {
        if (!akunMap.containsKey(username)) return null;
        User u = akunMap.get(username);
        if (u.getPassword().equals(password)) return u;
        return null;
    }
}
