package com.mac.model;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO
    @Column(name = "task_id")
    private int id;
    private String description;


    @ManyToOne
    private User user;

    @ManyToOne
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<SubTask> subTaskList;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "in_progress")
    private Boolean inProgress;

    @Column(name = "task_done")
    private Boolean taskDone;
}
