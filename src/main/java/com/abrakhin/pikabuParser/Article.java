package com.abrakhin.pikabuParser;

import javax.persistence.*;
import javax.persistence.Entity;

@Entity
@Table(name = "article", schema = "public")
public class Article {
    public Article(String link) {
        this.link = link;
    }

    public long getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String link;

}
