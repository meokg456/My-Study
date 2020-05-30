package mystudy.POJOs;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "registrations")
public class Registration {

    @EmbeddedId
    private RegistrationPK registration;

    public Registration() {
    }

    public Registration(RegistrationPK registration) {
        this.registration = registration;
    }

    public RegistrationPK getRegistration() {
        return this.registration;
    }

    public void setRegistration(RegistrationPK registration) {
        this.registration = registration;
    }

    public Registration registration(RegistrationPK registration) {
        this.registration = registration;
        return this;
    }

}