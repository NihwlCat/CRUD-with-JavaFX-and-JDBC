package services;

import entities.DaoFactory;
import entities.Department;
import entities.DepartmentDAO;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private DepartmentDAO dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        return dao.findAll();
    }

    public void saveOrUpdate (Department department){
        if(department.getId() == null){
            dao.insert(department);
        } else {
            dao.update(department);
        }
    }

    public void removeDepartment(Department obj){
        dao.deleteById(obj.getId());

    }



}
