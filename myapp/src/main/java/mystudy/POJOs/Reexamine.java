package mystudy.POJOs;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reexamines")
public class Reexamine {

    @Id
    @Column(name = "reexamineId", length = 100, nullable = false)
    private String reexamineId;

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    public Reexamine() {
    }

    public Reexamine(Date startDate, Date endDate) {
        this.reexamineId = UUID.randomUUID().toString();
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getReexamineId() {
        return this.reexamineId;
    }

    public void setReexamineId(String reexamineId) {
        this.reexamineId = reexamineId;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        return formatter.format(startDate) + " - " + formatter.format(endDate);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Reexamine)) {
            return false;
        }
        Reexamine reexamine = (Reexamine) o;
        return reexamine.reexamineId.equals(reexamineId);
    }

}