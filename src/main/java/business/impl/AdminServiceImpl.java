package business.impl;

import business.IAdminService;
import dao.IAdminDAO;
import dao.impl.AdminDAOImpl;

public class AdminServiceImpl implements IAdminService {
    IAdminDAO dao=new AdminDAOImpl();
    @Override
    public boolean login(String username, String password) {
        if(username==null||username.isEmpty()){
            throw new IllegalArgumentException("Wrong username or password");
        }
        return dao.login(username,password);
    }
}
