package ru.vadim.webfluxcourse.sec09.dto;

import java.util.UUID;

public record UploadResponse(UUID confirmationId, Long productsCount) {
}
