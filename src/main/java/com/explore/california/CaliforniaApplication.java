package com.explore.california;

import com.explore.california.model.Difficulty;
import com.explore.california.model.Region;
import com.explore.california.repository.TourPackageRepository;
import com.explore.california.service.TourPackageService;
import com.explore.california.service.TourService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;

@SpringBootApplication
public class CaliforniaApplication implements CommandLineRunner {

    @Value("${ec.importfile}")
    private String importFile;

    private TourPackageService tourPackageService;
    private TourService tourService;

    @Autowired
    CaliforniaApplication(TourPackageService tourPackageService, TourService tourService) {

        this.tourPackageService = tourPackageService;
        this.tourService = tourService;
    }

    public static void main(String[] args) {

        SpringApplication.run(CaliforniaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        createTourPackages();
        long numOfTourPackages = tourPackageService.total();
        createTours(importFile);
        long numOfTours = tourService.total();
    }

    /**
     * Initialize all the known tour packages
     */
    private void createTourPackages() {

        tourPackageService.createTourPackage("BC", "Backpack Cal");
        tourPackageService.createTourPackage("CC", "California Calm");
        tourPackageService.createTourPackage("CH", "California Hot springs");
        tourPackageService.createTourPackage("CY", "Cycle California");
        tourPackageService.createTourPackage("DS", "From Desert to Sea");
        tourPackageService.createTourPackage("KC", "Kids California");
        tourPackageService.createTourPackage("NW", "Nature Watch");
        tourPackageService.createTourPackage("SC", "Snowboard Cali");
        tourPackageService.createTourPackage("TC", "Taste of California");
    }

    /**
     * Create tour entities from an external file
     */
    private void createTours(String fileToImport) throws IOException {

        TourFromFile.read(fileToImport).forEach(importedTour ->
                tourService.createTour(importedTour.getTitle(),
                        importedTour.getDescription(),
                        importedTour.getBlurb(),
                        importedTour.getPrice(),
                        importedTour.getLength(),
                        importedTour.getBullets(),
                        importedTour.getKeywords(),
                        importedTour.getPackageType(),
                        importedTour.getDifficulty(),
                        importedTour.getRegion()));
    }

    /**
     * Helper class to import ExploreCalifornia.json
     */
    private static class TourFromFile {

        //fields
        private String packageType, title, description, blurb, price, length,
                bullets, keywords, difficulty, region;

        //reader
        static List<TourFromFile> read(String fileToImport) throws IOException {

            ClassPathResource res = new ClassPathResource(fileToImport);
            return new ObjectMapper().setVisibility(FIELD, ANY).
                    readValue(new FileInputStream(res.getFile()), new TypeReference<List<TourFromFile>>() {
                    });
        }

        protected TourFromFile() {

        }

        String getPackageType() {

            return packageType;
        }

        String getTitle() {

            return title;
        }

        String getDescription() {

            return description;
        }

        String getBlurb() {

            return blurb;
        }

        Integer getPrice() {

            return Integer.parseInt(price);
        }

        String getLength() {

            return length;
        }

        String getBullets() {

            return bullets;
        }

        String getKeywords() {

            return keywords;
        }

        Difficulty getDifficulty() {

            return Difficulty.valueOf(difficulty);
        }

        Region getRegion() {

            return Region.findByLabel(region);
        }
    }
}
