package com.books.shiro;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AccountProfile implements Serializable {
    private Integer id;

    private String username;

    private Integer role;
}
