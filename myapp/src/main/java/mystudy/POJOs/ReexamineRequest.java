package mystudy.POJOs;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import mystudy.Enum.GradeColumn;
import mystudy.Enum.RequestStatus;

@Entity
@Table(name = "reexamine_requests")
public class ReexamineRequest {
    @EmbeddedId
    private RegistrationOfStudent registration;

    @JoinColumn(name = "reexamineId")
    @OneToOne(fetch = FetchType.EAGER)
    private Reexamine reexamine;

    @Column(name = "requestTime", nullable = false)
    private Date requestTime;

    @Column(name = "requestStatus", nullable = false)
    private RequestStatus status;

    @Column(name = "gradeColumn", nullable = false)
    private GradeColumn gradeColumn;

    @Column(name = "desireGrade", nullable = false)
    private float desireGrade;

    @Column(name = "reason", nullable = false)
    private String reason;

    public ReexamineRequest() {
    }

    public ReexamineRequest(RegistrationOfStudent registration, Reexamine reexamine, Date requestTime,
            RequestStatus status, GradeColumn gradeColumn, float desireGrade, String reason) {
        this.registration = registration;
        this.reexamine = reexamine;
        this.requestTime = requestTime;
        this.status = status;
        this.gradeColumn = gradeColumn;
        this.desireGrade = desireGrade;
        this.reason = reason;
    }

    public RegistrationOfStudent getRegistration() {
        return this.registration;
    }

    public void setRegistration(RegistrationOfStudent registration) {
        this.registration = registration;
    }

    public Reexamine getReexamine() {
        return this.reexamine;
    }

    public void setReexamine(Reexamine reexamine) {
        this.reexamine = reexamine;
    }

    public Date getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public RequestStatus getStatus() {
        return this.status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public GradeColumn getGradeColumn() {
        return this.gradeColumn;
    }

    public void setGradeColumn(GradeColumn gradeColumn) {
        this.gradeColumn = gradeColumn;
    }

    public float getDesireGrade() {
        return this.desireGrade;
    }

    public void setDesireGrade(float desireGrade) {
        this.desireGrade = desireGrade;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}