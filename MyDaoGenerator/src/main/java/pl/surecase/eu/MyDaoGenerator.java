package pl.surecase.eu;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.belatrix.apadea.datamodel");

        // Create User
        Entity user = schema.addEntity("User");
        user.addIdProperty();
        user.addStringProperty("FirstName");
        user.addStringProperty("LastName");
        user.addIntProperty("Age");
        user.addStringProperty("Gender");
        user.addStringProperty("Type");
        user.addStringProperty("Avatar");
        user.addByteArrayProperty("ProfileImage");

        Entity session = schema.addEntity("Session");
        session.addIdProperty();
        session.addBooleanProperty("closed");
        session.addDateProperty("sessionStartDate");
        session.addDateProperty("sessionEndDate");

        Property therapistIdProperty = session.addLongProperty("therapistId").getProperty();
        session.addToOne(user, therapistIdProperty, "therapist");

        Property subjectIdProperty = session.addLongProperty("subjectId").getProperty();
        session.addToOne(user, subjectIdProperty, "subject");

        Entity log = schema.addEntity("Log");

        log.addIdProperty();

        log.addStringProperty("CurrentLevel");
        log.addStringProperty("Result");
        log.addStringProperty("Observations");

        Property sessionId = log.addLongProperty("sessionId").notNull().getProperty();
        ToMany sessionToLogs = session.addToMany(log, sessionId);

        schema.enableKeepSectionsByDefault();

        new DaoGenerator().generateAll(schema, args[0]);
    }
}
