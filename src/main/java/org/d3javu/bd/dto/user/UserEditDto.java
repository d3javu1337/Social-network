package org.d3javu.bd.dto.user;

import lombok.Value;
import org.d3javu.bd.models.tag.Tag;

import java.util.HashSet;
import java.util.Set;

@Value
public class UserEditDto {

    public Long id;
    public String firstName;
    public String lastName;
    public String username;
    public String customLink;
    //
    public String password;
    public Set<Tag> tags;

}
