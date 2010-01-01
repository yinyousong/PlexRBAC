package com.plexobject.rbac.domain;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.validator.GenericValidator;

import com.sleepycat.persist.model.DeleteAction;
import com.sleepycat.persist.model.PrimaryKey;
import com.sleepycat.persist.model.Relationship;
import com.sleepycat.persist.model.SecondaryKey;

/**
 * This class defines a simple user class Note: This project does not deal with
 * authentication and recommends openid solutions for it (such as RPX)
 * 
 * @author bhatti_shahzad
 * 
 */
public class User extends Auditable implements Validatable {
    @PrimaryKey(sequence = "user_seq")
    private int id;
    private String username;
    @SecondaryKey(relate = Relationship.MANY_TO_ONE, relatedEntity = Application.class, onRelatedEntityDelete = DeleteAction.CASCADE)
    private String applicationName;

    // for JPA
    User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(final String username) {
        setUsername(username);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (GenericValidator.isBlankOrNull(username)) {
            throw new IllegalArgumentException("username not specified");
        }
        this.username = username;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof User)) {
            return false;
        }
        User rhs = (User) object;
        return new EqualsBuilder().append(this.applicationName,
                rhs.applicationName).append(this.username, rhs.username)
                .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(786529047, 1924536713).append(this.username)
                .toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("applicationName",
                applicationName).append("username", this.username).toString();
    }

    public void setApplicationName(String applicationName) {
        if (GenericValidator.isBlankOrNull(applicationName)) {
            throw new IllegalArgumentException(
                    "applicationName is not specified");
        }
        this.applicationName = applicationName;
    }

    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public void validate() throws ValidationException {
        final Map<String, String> errorsByField = new HashMap<String, String>();
        if (GenericValidator.isBlankOrNull(applicationName)) {
            errorsByField.put("applicationName",
                    "applicationName is not specified");
        }
        if (GenericValidator.isBlankOrNull(username)) {
            errorsByField.put("username", "username is not specified");
        }

        if (errorsByField.size() > 0) {
            throw new ValidationException(errorsByField);
        }
    }
}