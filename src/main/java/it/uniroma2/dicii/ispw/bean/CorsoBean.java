package it.uniroma2.dicii.ispw.bean;

import java.util.Date;

public class CorsoBean {
    private String name;
    private Date startDate;

    public CorsoBean() {
    }

    public CorsoBean(String name) {
        this.name = name;
    }

    public CorsoBean(String name, Date startDate) {
        this.name = name;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
