/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.domain;

/**
 *
 * @author Bruker
 */

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import no.ntnu.tollefsen.auth.User;
import javax.persistence.*;
import java.io.Serializable;
import static com.mycompany.mavenproject1.domain.Item.FIND_ALL_ITEMS;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "ITEMS")
@NamedQuery(name = FIND_ALL_ITEMS, query = "select i from Item i")
public class Item implements Serializable {
    public static final String FIND_ALL_ITEMS = "Item.findAllItems";

//    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;
    private String description;
    private int price;

//    @OneToMany
//    private List<Photo> itemImages;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User itemOwner;

    @ManyToOne
    private User itemBuyer;
    
   
}
