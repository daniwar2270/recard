package bg.codexio.recard.profile.model.payload.response;

import java.time.LocalDate;

public record ProfileResponse(String firstName,
                              String lastName,
                              LocalDate bornOn,
                              String imageUrl,
                              String thumbnailUrl) {
}