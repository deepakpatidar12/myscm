package com.myScm.scm.entities;

import jakarta.persistence.CascadeType;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Contacts")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Contact {

    @Id
    private String contactId;
    private String name;
    private String nickName;
    private String contactEmail;
    private String contactNumber;
    @Column(length = 10000)
    private String about;
    private String address;
    private String contactPic;
    private boolean favorite = false;
    private String imagePublicId;
    @ManyToOne
    @JsonIgnore
    private User user;

    // Mapping for Social Links
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<SocialLink> links = new ArrayList<>();

    // Add More Fields They are Required

}
