package mystudy.POJOs;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "registrations")
public class Registration {

    @EmbeddedId
    private RegistrationOfStudent registration;

    public Registration() {
    }

    public Registration(RegistrationOfStudent registration) {
        this.registration = registration;
    }

    public RegistrationOfStudent getRegistration() {
        return this.registration;
    }

    public void setRegistration(RegistrationOfStudent registration) {
        this.registration = registration;
    }

    public Registration registration(RegistrationOfStudent registration) {
        this.registration = registration;
        return this;
    }

}