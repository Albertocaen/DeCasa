package org.proyecto.decasa.application.service;

import lombok.RequiredArgsConstructor;
import org.proyecto.decasa.api.dto.request.PackRequest;
import org.proyecto.decasa.api.dto.response.PackResponse;
import org.proyecto.decasa.domain.entity.Pack;
import org.proyecto.decasa.exception.ResourceNotFoundException;
import org.proyecto.decasa.infrastructure.repository.PackRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackService {

    private final PackRepository packRepository;

    @Transactional(readOnly = true)
    public List<PackResponse> findAvailable() {
        return packRepository.findAvailableWithComponents()
                .stream()
                .map(PackResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public PackResponse findById(Long id) {
        return packRepository.findByIdWithComponents(id)
                .map(PackResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Pack no encontrado: " + id));
    }

    @Transactional
    public PackResponse create(PackRequest req) {
        Pack pack = Pack.builder()
                .name(req.name())
                .description(req.description())
                .priceMin(req.priceMin())
                .priceMax(req.priceMax())
                .available(req.available())
                .sortOrder(req.sortOrder())
                .build();

        return PackResponse.from(packRepository.save(pack));
    }

    @Transactional
    public PackResponse update(Long id, PackRequest req) {
        Pack pack = packRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pack no encontrado: " + id));

        pack.setName(req.name());
        pack.setDescription(req.description());
        pack.setPriceMin(req.priceMin());
        pack.setPriceMax(req.priceMax());
        pack.setAvailable(req.available());
        pack.setSortOrder(req.sortOrder());

        return PackResponse.from(pack);
    }
}
