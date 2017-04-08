package com.polymitasoft.caracola.report;

import com.polymitasoft.caracola.dataaccess.BedroomDao;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;

import org.threeten.bp.LocalDate;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import io.requery.query.Result;

import static com.polymitasoft.caracola.util.FormatUtils.*;

/**
 * @author rainermf
 * @since 8/4/2017
 */

public class Report {

    private BedroomDao bedroomDao = new BedroomDao();

    public void write(String path) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedOutputStream(new FileOutputStream(path)));
            for (Bedroom bedroom: bedroomDao.getBedrooms()) {
                writer.println("=== " + bedroom.getName() + " ===");
                writeBedroom(writer, bedroom);
                writer.println();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                writer.close();
            }
        }
    }

    private void writeBedroom(PrintWriter writer, Bedroom bedroom) {
        Result<Booking> bookings = bedroomDao.getBookings(bedroom, LocalDate.now(), LocalDate.now().plusYears(1));
        for (Booking booking: bookings) {
            writer.print(formatDate(booking.getCheckInDate()) + " / "  + formatDate(booking.getCheckOutDate()));
            String state = null;
            switch (booking.getState()) {
                case PENDING:
                    state = "Pendiente";
                    break;
                case CONFIRMED:
                    state = "Confirmada";
                    break;
                case CHECKED_IN:
                    state = "Registrada";
                    break;
            }
            writer.print(" [" + state + "]");
            if(!booking.getNote().trim().isEmpty()) {
                writer.print(" (" + booking.getNote() + ")");
            }
            writer.println();
        }
    }

}
