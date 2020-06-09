package mystudy.POJOs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "results")
public class Result {
    @EmbeddedId
    private RegistrationOfStudent registration;

    @Column(name = "midTermGrade", nullable = false)
    private float midTermGrade;

    @Column(name = "finalExamGrade", nullable = false)
    private float finalExamGrade;

    @Column(name = "otherGrade", nullable = false)
    private float otherGrade;

    @Column(name = "totalGrade", nullable = false)
    private float totalGrade;

    public Result() {
    }

    public Result(RegistrationOfStudent registration, float midTermGrade, float finalExamGrade, float otherGrade,
            float totalGrade) {
        this.registration = registration;
        this.midTermGrade = midTermGrade;
        this.finalExamGrade = finalExamGrade;
        this.otherGrade = otherGrade;
        this.totalGrade = totalGrade;
    }

    public RegistrationOfStudent getRegistration() {
        return this.registration;
    }

    public void setRegistration(RegistrationOfStudent registration) {
        this.registration = registration;
    }

    public float getMidTermGrade() {
        return this.midTermGrade;
    }

    public void setMidTermGrade(float midTermGrade) {
        this.midTermGrade = midTermGrade;
    }

    public float getFinalExamGrade() {
        return this.finalExamGrade;
    }

    public void setFinalExamGrade(float finalExamGrade) {
        this.finalExamGrade = finalExamGrade;
    }

    public float getOtherGrade() {
        return this.otherGrade;
    }

    public void setOtherGrade(float otherGrade) {
        this.otherGrade = otherGrade;
    }

    public float getTotalGrade() {
        return this.totalGrade;
    }

    public void setTotalGrade(float totalGrade) {
        this.totalGrade = totalGrade;
    }

}