package org.d3javu.bd.dto.user;

import lombok.Data;
import lombok.Value;
import org.d3javu.bd.models.tag.Tag;

import java.util.HashSet;
import java.util.Set;

@Data
//@Value
public class UserCreateDto {

    public String firstName;
    public String lastName;
    public String login;
    public String password;
    public Set<Tag> preferredTags;

    @Override
    public String toString() {
        return "UserCreateDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + login + '\'' +
                ", password='" + password + '\'' +
                ", preferredTags=" + preferredTags +
                '}';
    }
}
