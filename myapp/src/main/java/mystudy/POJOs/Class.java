package mystudy.POJOs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "classes")
public class Class {

    @Id
    @Column(name = "className", nullable = false, length = 10)
    private String className;

    public Class() {
    }

    public Class(String className) {
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return this.className;
    }
}