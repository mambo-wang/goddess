package com.h3c.vdi.athena.netdisk.utils;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author w14014
 */
@NoArgsConstructor
@AllArgsConstructor
public enum EditUserKey {
    /**email*/
    email("email"),
    quota("quota"),
    displayname("displayname"),
    phone("phone"),
    address("address"),
    website("website"),
    twitter("twitter"),
    password("password");

    @Getter
    private String key;
}
