package tech.getarrays.inventorymanager.models.POJO;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "subscription")
public class Subscription implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;

    @Column(name = "subscriber")
    private String subscriber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan", nullable = false)
    private Plan plan;

    @Column(name = "price")
    private Float price;

    @Column(name = "startDate")
    private LocalDateTime startDate;

    @Column(name = "expirationDate")
    private LocalDateTime expirationDate;

    @Column(name = "status")
    private String status;

}
