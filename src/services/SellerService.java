package services;

import entities.DaoFactory;
import entities.Seller;
import entities.SellerDAO;

import java.util.List;

public class SellerService {

    private SellerDAO dao = DaoFactory.createSellerDao();

    public List<Seller> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate (Seller obj){
        if(obj.getId() == null){
            dao.insert(obj);
        } else {
            dao.update(obj);
        }
    }

    public void removeSeller(Seller obj){
        dao.deleteById(obj.getId());

    }

}
