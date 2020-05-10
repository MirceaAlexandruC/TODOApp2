package com.mac.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private int project_id;

    private String name;

    @ManyToMany(mappedBy = "projects")
    private List<User> users;

    @OneToMany( mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

}
