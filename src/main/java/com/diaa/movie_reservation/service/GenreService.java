package com.diaa.movie_reservation.service;

import com.diaa.movie_reservation.dto.genre.GenreRequest;
import com.diaa.movie_reservation.dto.genre.GenreResponse;
import com.diaa.movie_reservation.entity.Genre;
import com.diaa.movie_reservation.mapper.GenreMapper;
import com.diaa.movie_reservation.repository.GenreRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final EntityManager entityManager;

    @Transactional
    @CacheEvict(value = "genres", allEntries = true)
    public GenreResponse addGenre(GenreRequest request) {
        Genre genre = genreMapper.toEntity(request);
        Genre savedGenre = genreRepository.save(genre);
        return genreMapper.toDTO(savedGenre);
    }

    @Transactional
    @CacheEvict(value = "genres", allEntries = true)
    public GenreResponse updateGenre(short id, GenreRequest request) {
        Genre genre = genreRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Genre with id " + id + " does not exist."));
        genre.setName(request.name());
        Genre updatedGenre = genreRepository.save(genre);
        return genreMapper.toDTO(updatedGenre);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "genres")
    public List<GenreResponse> getAllGenres() {
        log.info("Fetching all genres");
        simulateSlowService(); // Simulate a slow service for demonstration purposes
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(genreMapper::toDTO)
                .toList();
    }

    private void simulateSlowService() {
        log.info("Simulating a slow service for demonstration purposes");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted while simulating slow service", e);
        }
    }

    @Transactional(readOnly = true)
    public boolean genreExists(short id) {
        return genreRepository.existsById(id);
    }

    @Transactional(readOnly = true)
    public boolean isGenresValid(Set<Short> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return false;
        }
        long count = genreRepository.countByIdIn(genreIds);
        return count == genreIds.size();
    }

    @Transactional(readOnly = true)
    public Genre getReference(short id) {
        return entityManager.getReference(Genre.class, id);
    }

    @Transactional
    @CacheEvict(value = "genres", allEntries = true)
    public void deleteGenre(short id) {
        if (!genreRepository.existsById(id)) {
            throw new EntityNotFoundException("Genre with id " + id + " does not exist.");
        }
        genreRepository.deleteGenreFromMovieGenres(id);
        genreRepository.deleteById(id);
    }

}
