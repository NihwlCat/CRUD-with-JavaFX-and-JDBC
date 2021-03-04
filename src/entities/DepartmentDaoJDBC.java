package entities;

import db.DB;
import db.DBException;
import db.DBIntegrityException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDAO {

    private Connection conn;

    public DepartmentDaoJDBC (Connection conn){
        this.conn = conn;
    }

    public void closeDepartmentDao () {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Department obj) {
        PreparedStatement statement;

        try {
            statement = conn.prepareStatement(
                    "INSERT INTO department "
                            + "(Name) "
                            + "VALUES (?)", Statement.RETURN_GENERATED_KEYS);

            statement.setString(1,obj.getName());

            int rows = statement.executeUpdate();

            if (rows == 0) {
                throw new DBException("Failed to insert department");
            } else {
                ResultSet rs = statement.getGeneratedKeys();
                if(rs.next()){
                    obj.setId(rs.getInt(1));
                }
            }

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        }

    }

    @Override
    public void update(Department obj) {
        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement(
                    "UPDATE department SET "
                            + "Name = ? "
                            + "WHERE Id = ?");

            statement.setString(1,obj.getName());
            statement.setInt(2,obj.getId());

            statement.executeUpdate();

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }

    }

    @Override
    public void deleteById(Integer id) {

        PreparedStatement statement = null;

        try {
            statement = conn.prepareStatement("DELETE FROM department WHERE Id = ?");

            statement.setInt(1,id);

            int rows = statement.executeUpdate();

            if (rows == 0)
                throw new DBException("Delete failed. You tried to pick an id that doesn't exist");

        } catch(SQLException e){
            throw new DBIntegrityException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.prepareStatement("SELECT * FROM department WHERE department.Id = ?");

            statement.setInt(1,id);
            rs = statement.executeQuery();

            if(rs.next()){
                return new Department(rs.getInt("Id"),rs.getString("Name"));
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
    public List<Department> findAll() {
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = conn.prepareStatement("SELECT * FROM department ORDER BY Name");

            rs = statement.executeQuery();

            List<Department> dep = new ArrayList<>();

            while(rs.next()){
                dep.add(new Department(rs.getInt("Id"),rs.getString("Name")));
            }

            return dep;

        } catch (SQLException e){
            throw new DBException(e.getMessage());
        } finally {
            DB.closeStatement(statement);
            DB.closeResultSet(rs);
        }
    }
}
