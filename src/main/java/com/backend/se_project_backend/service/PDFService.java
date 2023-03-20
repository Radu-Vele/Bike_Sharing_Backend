package com.backend.se_project_backend.service;

import com.backend.se_project_backend.model.Ride;
import com.backend.se_project_backend.utils.exceptions.IllegalOperationException;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface PDFService {

    public String[] formatRide(Ride ride);

    public void generatePDF(String[] content) throws IOException, IllegalOperationException;

    public void generateRideReceipt(Ride ride) throws IOException, IllegalOperationException;
}
