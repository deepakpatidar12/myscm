package com.myScm.scm.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "myuser")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;
    @Column(name = "user_email", nullable = false, unique = true)
    private String userEmail;
    @Column(name = "user_phone", nullable = true, unique = true)
    private String userNumber;
    @Column(name = "password", nullable = true)
    private String password;
    @Column(name = "profileLink", nullable = true, length = 5000)
    private String profilePic;
    private String profilePublicId;
    @JsonIgnore
    private String userRole = "ROLE_USER";
    // info

    @JsonIgnore
    private boolean enabled = false;
    @JsonIgnore
    private boolean emailValified = false;
    @JsonIgnore
    private boolean numberVarified = false;

    // Self, Google, facebook, Twitter, LinkedIn, GitHub
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Providers providers = Providers.SELF;
    @JsonIgnore
    @Column(name = "providerUserId", nullable = true, length = 1000)
    private String providerUserId;

    // for Mapping
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Contact> contactList = new ArrayList<>();

    // Add More Fields They Are Required

}
