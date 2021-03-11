package entities;

import db.DB;
import db.DBException;
import db.DBIntegrityException;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDAO {
    private Connection conn; // Dependência de Connection para poder puxar DB.getConnection

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    public void closeSellerDAO () {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Seller obj) {
        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement(
                    "INSERT INTO seller "
                            + "(Name,Email,BirthDate,BaseSalary,DepartmentId) "
                            + "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1,obj.getName());
            statement.setString(2,obj.getEmail());
            statement.setDate(3,new Date(obj.getBirthDate().getTime()));
            statement.setDouble(4,obj.getBaseSalary());
            statement.setInt(5,obj.getDepartment().getId());

            int rows = statement.executeUpdate();

            if(rows > 0){
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next()){
                    // ResultSet .getInt() recupera o valor da coluna designada na linha atual como um int
                    obj.setId(rs.getInt(1));
                }
                DB.closeResultSet(rs);
            } else {
                throw new DBException("Insert failed for no especified reason");
            }

        } catch(SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }

    }

    @Override
    public void update(Seller obj) {
        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement(
                    "UPDATE seller SET "
                            + "Name = ?,Email = ?,BirthDate = ?,BaseSalary = ?,DepartmentId = ? "
                            + "WHERE Id = ?");

            statement.setString(1,obj.getName());
            statement.setString(2,obj.getEmail());
            statement.setDate(3,new Date(obj.getBirthDate().getTime()));
            statement.setDouble(4,obj.getBaseSalary());
            statement.setInt(5,obj.getDepartment().getId());

            statement.setInt(6,obj.getId());

            statement.executeUpdate();

        } catch(SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement("DELETE FROM seller WHERE Id = ?");

            statement.setInt(1,id);

            int rows = statement.executeUpdate();

            if (rows == 0)
                throw new DBException("Delete failed. You tried to pick an id that doesn't exist");

        } catch(SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet rs = null;

        /*
        * O que ocorre aqui é uma consulta com placeholder ao banco de dados. Código adquirido através de boilerplate.
        * O método .executeQuery() faz a busca e armazena os dados em uma tabela ResultSet.
        * Cada coluna da tabela é usada para instanciar um objeto do tipo Seller que é retornado ao final da execução.
        * Caso rs.next() retorne falso na condição, significa que não há linha de dados em rs e portanto houve falha na busca.
        */

        try {
            statement = conn.prepareStatement(
                    "SELECT seller.*, department.Name as Departamento "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE seller.Id = ?");

            statement.setInt(1,id);
            rs = statement.executeQuery();

            if(rs.next()){
                Seller seller = new Seller(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        new java.util.Date(rs.getDate("BirthDate").getTime()), /*Data em util.Date*/
                        rs.getDouble("BaseSalary"),
                        new Department(rs.getInt("DepartmentId"),rs.getString("Departamento")));

                return seller;
            } else {
                throw new DBException("Search failed");
            }

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.prepareStatement(
                    "SELECT seller.*,department.Name as Departamento "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "ORDER BY Name");

            rs = statement.executeQuery();

            List<Seller> sellers = new ArrayList<>();

            /*
            * A estrutura Map que possui uma chave do tipo Integer e um elemento do tipo Department é criado.
            * Dentro do while, um objeto Department pega o elemento do map, caso não exista o elemento então essa operação retorna null.
            * Caso dep == null, o dep é finalmente instanciado.
            *
            * Esse método também serve para diferentes departamentos pois ao utilizar o método get e não encontrar elemento equivalente
            * a chave do ResultSet, então será criado uma instância de Department e essa instância será colocada no map.
            */

            Map<Integer,Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));

                if(dep == null){
                    dep = new Department(rs.getInt("DepartmentId"),rs.getString("Departamento"));
                    map.put(rs.getInt("DepartmentId"),dep);
                }

                Seller seller = new Seller(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        new java.util.Date(rs.getDate("BirthDate").getTime()),
                        rs.getDouble("BaseSalary"),
                        dep
                );
                sellers.add(seller);
            }

            return sellers;

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }


    @Override
    public List<Seller> findByDepartment(Department department) {

        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.prepareStatement(
                    "SELECT seller.*,department.Name as Departamento "
                    + "FROM seller INNER JOIN department "
                    + "ON seller.DepartmentId = department.Id "
                    + "WHERE DepartmentId = ? "
                    + "ORDER BY Name");

            statement.setInt(1,department.getId());

            rs = statement.executeQuery();

            List<Seller> sellers = new ArrayList<>();

            /*
            * A estrutura Map que possui uma chave do tipo Integer e um elemento do tipo Department é criado.
            * Dentro do while, um objeto Department pega o elemento do map, caso não exista o elemento então essa operação retorna null.
            * Caso dep == null, o dep é finalmente instanciado.
            */

            Map<Integer,Department> map = new HashMap<>();

            while(rs.next()){
                Department dep = map.get(rs.getInt("DepartmentId"));

                if(dep == null){
                    dep = new Department(rs.getInt("DepartmentId"),rs.getString("Departamento"));
                    map.put(rs.getInt("DepartmentId"),dep);
                }

                Seller seller = new Seller(
                        rs.getInt("Id"),
                        rs.getString("Name"),
                        rs.getString("Email"),
                        new java.util.Date(rs.getDate("BirthDate").getTime()),
                        rs.getDouble("BaseSalary"),
                        dep
                );
                sellers.add(seller);
            }

            return sellers;

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }
}
