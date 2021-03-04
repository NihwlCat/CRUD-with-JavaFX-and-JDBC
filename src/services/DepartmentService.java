package services;

import entities.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    public List<Department> findAll(){
        List<Department> departmentList = new ArrayList<>();

        // Mock de dados

        departmentList.add(new Department(1,"Livros"));
        departmentList.add(new Department(2,"Lojas"));
        departmentList.add(new Department(3,"Vendas"));

        return departmentList;
    }



}
