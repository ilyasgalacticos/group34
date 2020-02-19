package kz.iitu.csse.group34.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_items")
public class Items extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

}
