package app.vercel.josegabriel.parking_api.entity.clientPark;

import app.vercel.josegabriel.parking_api.entity.client.Client;
import app.vercel.josegabriel.parking_api.entity.clientPark.dto.ClientParkCreateDTO;
import app.vercel.josegabriel.parking_api.entity.parking.ParkingSpace;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "clients_park")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@EntityListeners(AuditingEntityListener.class)
public class ClientPark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 23)
    private String receipt;
    @Column(nullable = false, length = 8)
    private String plate;
    @Column(nullable = false, length = 45)
    private String brand;
    @Column(nullable = false, length = 45)
    private String model;
    @Column(nullable = false, length = 45)
    private String color;
    @Column(nullable = false)
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    @Column(columnDefinition = "DECIMAL(7,2) default 0.00")
    private BigDecimal value;
    @Column(columnDefinition = "DECIMAL(7,2) default 0.00")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @ManyToOne
    @JoinColumn(name = "parking_space_id", nullable = false)
    private ParkingSpace parkingSpace;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @CreatedBy
    private String createdBy;
    @LastModifiedBy
    private String updatedBy;

    public ClientPark(ClientParkCreateDTO dto) {
        this.plate = dto.plate();
        this.brand = dto.brand();
        this.model = dto.model();
        this.color = dto.color();
    }
}
