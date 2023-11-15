package guru.springframework.sfgpetclinic.services.springdatajpa;

import guru.springframework.sfgpetclinic.model.Visit;
import guru.springframework.sfgpetclinic.repositories.VisitRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VisitSDJpaServiceTest {

    @Mock
    VisitRepository repository;

    @InjectMocks
    VisitSDJpaService service;

    @Test
    void findAll() {
        Set<Visit> visits = new HashSet<>();
        Visit visit = new Visit();
        visits.add(visit);

        when(repository.findAll()).thenReturn(visits);

        Set<Visit> foundVisits = service.findAll();

        verify(repository).findAll();
        assertThat(foundVisits).isNotNull();
        assertThat(foundVisits).hasSize(1);
    }

    @Test
    void findById() {
        Visit visit = new Visit();

        when(repository.findById(1L)).thenReturn(Optional.of(visit));

        Visit foundVisit = service.findById(1L);

        verify(repository).findById(1L);

        assertThat(foundVisit).isNotNull();
    }

    @Test
    void save() {
        //given
        Visit visit = new Visit();
        given(repository.save(any(Visit.class))).willReturn(visit);

        //when
        Visit savedVisit = service.save(new Visit());

        //then
        then(repository).should().save(any(Visit.class));
        assertThat(savedVisit).isNotNull();
    }

    @Test
    void delete() {
        Visit visit = new Visit();

        service.delete(visit);

        verify(repository).delete(visit);
    }

    @Test
    void deleteById() {
        service.deleteById(1L);

        verify(repository).deleteById(1L);
    }
}