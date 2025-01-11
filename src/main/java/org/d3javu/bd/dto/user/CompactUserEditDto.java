package org.d3javu.bd.dto.user;

import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
public class CompactUserEditDto {

    public String firstName;
    public String lastName;
    public MultipartFile avatar;

}
