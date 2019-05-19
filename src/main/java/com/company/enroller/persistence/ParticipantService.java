package com.company.enroller.persistence;

import com.company.enroller.model.Participant;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component("participantService")
public class ParticipantService {

    DatabaseConnector connector;

    //Wstrzyknij swojego PasswordEncodera do usługi obsługującej funkcjonalność dodawania nowego użytkownika (Participant).
    @Autowired
    PasswordEncoder passwordEncoder;

    public ParticipantService() {
        connector = DatabaseConnector.getInstance();
    }

    public Collection<Participant> getAll() {
        return connector.getSession().createCriteria(Participant.class).list();
    }

    public Participant findByLogin(String login) {
        return (Participant) connector.getSession().get(Participant.class, login);
    }

    public Participant add(Participant participant) {
        //Użyj wstrzykniętej zależności by zahashować hasło użytkownika przed jego dodaniem do bazy
        String hashedPassword = passwordEncoder.encode(participant.getPassword());
        participant.setPassword(hashedPassword);
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().save(participant);
        transaction.commit();
        return participant;
    }

    public void update(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().merge(participant);
        transaction.commit();
    }

    public void delete(Participant participant) {
        Transaction transaction = connector.getSession().beginTransaction();
        connector.getSession().delete(participant);
        transaction.commit();
    }

}
