package com.polymitasoft.caracola.report.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.polymitasoft.caracola.dataaccess.BedroomDao;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.view.booking.CalendarState;
import com.polymitasoft.pdfcal.Calendar;
import com.polymitasoft.pdfcal.CalendarRange;

import org.threeten.bp.YearMonth;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

import io.requery.query.Result;

public class PdfReport {

    private static EnumMap<BookingState, BaseColor> colors;

    public void manipulatePdf(String dest) throws FileNotFoundException, DocumentException {
        Document doc = new Document();
        PdfWriter.getInstance(doc, new FileOutputStream(dest));
        doc.open();

        BedroomDao bedroomDao = new BedroomDao();
        for(Bedroom bedroom: bedroomDao.getBedrooms()) {
            YearMonth startMonth = YearMonth.now();
            YearMonth endMonth = YearMonth.now().plusMonths(11);
            Result<Booking> bookings = bedroomDao.getBookings(bedroom, startMonth.atDay(1), endMonth.atEndOfMonth());
            List<CalendarRange> ranges = new ArrayList<>();
            for(Booking booking: bookings) {
                BaseColor color = colors.get(booking.getState());
                ranges.add(CalendarRange.of(booking.getCheckInDate(), booking.getCheckOutDate(), color));
            }
            Calendar calendar = new Calendar.Builder()
                    .startingOn(startMonth)
                    .endingOn(endMonth)
                    .withFilledCells(ranges)
                    .build();
            doc.add(title(bedroom.getName()));
            doc.add(calendar.build());
            doc.newPage();
        }

        doc.close();
    }

    private static Paragraph title(String titleText) {
        Paragraph title = new Paragraph();
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD));
        title.setSpacingAfter(18);
        title.add(titleText);

        return title;
    }

    static {
        colors = new EnumMap<>(BookingState.class);
        for (BookingState state: BookingState.values()) {
            colors.put(state, new BaseColor(CalendarState.toCalendarState(state).color()));
        }
    }
}
