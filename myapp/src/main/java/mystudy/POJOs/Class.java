package mystudy.POJOs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static Class readClassFromCSV(InputStreamReader fileReader) throws IOException {
        BufferedReader reader = new BufferedReader(fileReader);
        // Đọc tên lớp
        String className = reader.readLine().split(",")[0];
        reader.close();
        return new Class(className);
    }
}