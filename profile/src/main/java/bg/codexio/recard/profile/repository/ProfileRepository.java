package bg.codexio.recard.profile.repository;

import bg.codexio.recard.profile.model.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT p.imageUrl FROM Profile p")
    Set<String> findAllImageUrls();

    @Query("SELECT p.thumbnailUrl FROM Profile p")
    Set<String> findAllThumbnailUrls();
}