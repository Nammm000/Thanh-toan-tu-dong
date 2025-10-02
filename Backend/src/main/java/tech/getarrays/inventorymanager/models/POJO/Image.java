package tech.getarrays.inventorymanager.models.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Data
@Entity
@DynamicUpdate
@DynamicInsert
@Table(name = "imagePayment")
public class Image implements Serializable {

    private static final long serialVersionUID = 81L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    private String name;

    private String type;

    @Column(length = 50000000)
    private byte[] picByte;

    @Column(name = "attachedTo")
    private String attachedTo;

//    public Image(Long id, String name, byte[] picByte, String type) {
//        this.id = id;
//        this.name = name;
//        this.type = type;
//        this.picByte = picByte;
//    }
//
//    public Image(String name, String type, byte[] picByte) {
//        this.name = name;
//        this.type = type;
//        this.picByte = picByte;
//    }
}