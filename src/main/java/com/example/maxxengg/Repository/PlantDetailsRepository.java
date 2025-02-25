package com.example.maxxengg.Repository;

import com.example.maxxengg.Model.Plantdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantDetailsRepository extends JpaRepository<Plantdetails,Long> {
}
