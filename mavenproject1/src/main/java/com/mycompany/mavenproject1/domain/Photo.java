

package com.mycompany.mavenproject1.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Photo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    String id;

    String name;

    long filesize;
    String mimeType;

    @ManyToOne
    Item photoItem;

}