package com.explore.california.controller;

import com.explore.california.model.Tour;
import com.explore.california.repository.TourRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/tours")
public class TourController {

    private TourRepository tourRepository;

    @Autowired
    public TourController(TourRepository tourRepository) {

        this.tourRepository = tourRepository;
    }

    /**
     * Create Tour
     *
     * @param tour
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createTour(@RequestBody @Validated Tour tour) {

        tourRepository.save(tour);
    }

    /**
     * Get Tour by Id.
     *
     * @param tourId
     * @return
     */
    @GetMapping("/{tourId}")
    public Tour getTourById(@PathVariable(value = "tourId") int tourId) {

        return tourRepository.findById(tourId)
                .orElseThrow(() ->
                        new NoSuchElementException("Tour does not exist " + tourId));
    }

    /**
     * Delete Tour by Id.
     *
     * @param tourId
     */
    @DeleteMapping
    public void deleteTourById(@RequestParam(value = "tourId") int tourId) {

        if (tourRepository.findById(tourId).isPresent()) {
            tourRepository.deleteById(tourId);
        } else {
            throw new NoSuchElementException("Tour does not exist " + tourId);
        }
    }

    /**
     * Exception handler if NoSuchElementException is thrown in this Controller
     *
     * @param ex exception
     * @return Error message String.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {

        return ex.getMessage();

    }
}
