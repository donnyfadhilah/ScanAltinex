package id.ac.scanaltinex.Models;

public class ListWoDailyModel {
    int id;
    String name;
    String date;
    String employeeId;

    public void OpnameBarcpdeModel(int id, String name, String date, String employeeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.employeeId = employeeId;

    }
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {return date; }

    public String getEmployeeId() {return employeeId; }


}
