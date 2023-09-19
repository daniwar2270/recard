package bg.codexio.recard.profile.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "profile")
public class Profile {

    @Id
    private Long id;

    private String firstName;

    private String lastName;

    private LocalDate bornOn;

    private String imageUrl;

    private String thumbnailUrl;
}