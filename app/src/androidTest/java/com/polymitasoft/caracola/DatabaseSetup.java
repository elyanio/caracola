package com.polymitasoft.caracola;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.datamodel.Bedroom;
import com.polymitasoft.caracola.datamodel.Booking;
import com.polymitasoft.caracola.datamodel.BookingState;
import com.polymitasoft.caracola.datamodel.Client;
import com.polymitasoft.caracola.datamodel.ClientStay;
import com.polymitasoft.caracola.datamodel.Consumption;
import com.polymitasoft.caracola.datamodel.Country;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Gender;
import com.polymitasoft.caracola.datamodel.InternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.datamodel.SupplierService;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static android.os.Environment.getExternalStorageDirectory;
import static com.polymitasoft.caracola.datamodel.Gender.FEMININE;
import static com.polymitasoft.caracola.datamodel.Gender.MASCULINE;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.threeten.bp.LocalDate.of;
import static org.threeten.bp.Month.APRIL;
import static org.threeten.bp.Month.JANUARY;
import static org.threeten.bp.Month.JULY;
import static org.threeten.bp.Month.JUNE;
import static org.threeten.bp.Month.NOVEMBER;
import static org.threeten.bp.Month.SEPTEMBER;

/**
 * @author rainermf
 * @since 11/2/2017
 */
class DatabaseSetup {

    private static DateTimeFormatter format = DateTimeFormatter.ofPattern("y/M/d");
    private final EntityDataStore<Persistable> data;
    private File directory = new File(getExternalStorageDirectory().getAbsolutePath() + "/Caracola");
    private File dbFile = new File(directory.getAbsolutePath() + "/caracola.db");

    DatabaseSetup() {
        data = DataStoreHolder.INSTANCE.getDataStore();
    }

    void cleanDatabase() {
        directory.mkdirs();
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    void mockDatabase() {
        cleanDatabase();
        List<Client> clients = getClients();
        List<Supplier> suppliers = getSuppliers();
        List<Bedroom> bedrooms = getBedrooms();
        List<InternalService> internalServices = getInternalServices();
        List<Booking> bookings = getBookings(bedrooms);
        List<ClientStay> stays = getClientStays(bookings, clients);
        List<Consumption> consumptions = getConsumptions(bookings, internalServices);
        List<ExternalService> externalServices = getExternalServices();
        List<SupplierService> supplierServices = getSupplierServices(suppliers, externalServices);

        insertList(clients);
        insertList(suppliers);
        insertList(bedrooms);
        insertList(internalServices);
        insertList(bookings);
        insertList(stays);
        insertList(consumptions);
        insertList(externalServices);
        insertList(supplierServices);
    }

    private void insertList(List<? extends Persistable> list) {
        for (Persistable p : list) {
            data.insert(p);
        }
    }

    private List<Client> getClients() {
        String[] firstNames = new String[]{"claudia", "Rainer", "Llilian", "Yadriel", "Yanier", "Asiel", "Alejandro"};
        String[] lastNames = new String[]{"Luque", "Martínez", "Martínez", "Miranda", "Alfonso", "Alonso", "Cabriales"};
        LocalDate[] birthdayDates = new LocalDate[]{
                of(1990, JULY, 3), of(2003, JANUARY, 30), of(1989, APRIL, 16),
                of(1990, SEPTEMBER, 12), of(1995, JUNE, 28), of(1989, NOVEMBER, 16), of(1970, APRIL, 16)
        };
        Country[] countries = new Country[]{
                new Country("CU"), new Country("VE"), new Country("TV"), new Country("UK"),
                new Country("ES"), new Country("AR"), new Country("CO")};
        Gender[] genders = new Gender[]{FEMININE, MASCULINE, FEMININE, MASCULINE, MASCULINE, MASCULINE, MASCULINE};
        String[] passports = new String[]{
                "39938493849348", "90122633288", "39938493849348", "90120536024",
                "90122636040", "39938493849348", "39938493849348"
        };
        List<Client> clients = new ArrayList<>(firstNames.length);

        for (int i = 0; i < 7; i++) {
            Client client = new Client()
                    .setFirstName(firstNames[i])
                    .setLastName(lastNames[i])
                    .setBirthday(birthdayDates[i])
                    .setGender(genders[i])
                    .setCountry(countries[i])
                    .setPassport(passports[i]);

            clients.add(client);
        }

        return clients;
    }

    private List<Supplier> getSuppliers() {
        String[] names = new String[]{
                "yanio alfonso", "Yadrio Miranda", "asio alonso", "Hextiandro Manuel",
                "chacal right now", "yanio layout", "Chacón"
        };
        String[] addresses = new String[]{
                "carretera central", "main Street", "Barrio África", "Fruta Bomba DF", "Marta Abreu", "Mejunje", "Maceo"
        };
        String[] emailAdresses = new String[]{
                "rmartinez@uclv.cu", "barrio@correo.das", "africa@yandex.com", "hello@gmail.com",
                "cubadebate@yandex.ru", "fbcabriales@infomed.sld.cu", "claulisse@yahoo.com"
        };

        List<String>[] phoneNumbers = (List<String>[]) (new List[]{
                singletonList("39938493849348"), asList("90122633288", "90122633288"), emptyList(),
                singletonList("39938493849348"), singletonList("39938493849348"), asList("90122633288", "90122633288"), emptyList(),
        });

        List<Supplier> suppliers = new ArrayList<>(names.length);

        for (int i = 0; i < 7; i++) {
            Supplier client = new Supplier();
            client.setName(names[i]);
            client.setAddress(addresses[i]);
            client.setEmailAddress(emailAdresses[i]);
            client.setPhoneNumbers(phoneNumbers[i]);

            suppliers.add(client);
        }

        return suppliers;
    }

    private List<Bedroom> getBedrooms() {
        int[] capacity = new int[]{4, 2, 6, 4};
        double[] lowPrices = new double[]{25, 20, 25, 30};
        double[] highPrices = new double[]{30, 25, 30, 35};
        List<Bedroom> bedrooms = new ArrayList<>(capacity.length);

        for (int i = 0; i < 4; i++) {
            Bedroom bedroom = new Bedroom()
                    .setName("Habitación " + (i + 1))
                    .setCapacity(capacity[i])
                    .setPriceInLowSeason(BigDecimal.valueOf(lowPrices[i]))
                    .setPriceInHighSeason(BigDecimal.valueOf(highPrices[i]));

            bedrooms.add(bedroom);
        }

        return bedrooms;
    }

    private List<InternalService> getInternalServices() {
        String[] names = new String[]{"Desayuno", "Almuerzo", "Cena", "Botella de vino tinto"};
        double[] prices = new double[]{10, 15, 15, 12.5};
        List<InternalService> services = new ArrayList<>(names.length);

        for (int i = 0; i < names.length; i++) {
            InternalService service = new InternalService();
            service.setName(names[i]);
            service.setDefaultPrice(BigDecimal.valueOf(prices[i]));
            services.add(service);
        }

        return services;
    }

    private List<Booking> getBookings(List<Bedroom> bedrooms) {
        int[] bookingNumbers = new int[]{1, 13, 15, 16, 18, 10, 14};
        int[] bookNumbers = new int[]{3434, 5555, 7878, 1212, 7518, 6510, 2314};
        String[] checkInDates = new String[]{"2017/01/21", "2017/02/11", "2017/03/11", "2017/04/01", "2017/07/13", "2017/05/01", "2017/10/22"};
        String[] checkOutDates = new String[]{"2017/01/21", "2017/02/21", "2017/03/30", "2017/04/02", "2017/08/13", "2017/05/02", "2017/12/05"};
        String[] notes = new String[]{"polymita sera ", " una empresa ", "enorme en ", "diversion ", "y alomejor en soft", " les habla", "un polymitero"};
        double[] prices = new double[]{12, 32, 45, 65, 7, 78, 6};
        BookingState[] states = new BookingState[]{BookingState.CHECKED_IN, BookingState.CONFIRMED, BookingState.CONFIRMED, BookingState.PENDING, BookingState.CHECKED_IN, BookingState.CHECKED_IN, BookingState.CHECKED_IN};
        List<Booking> bookings = new ArrayList<>(bookingNumbers.length);

        int length = bedrooms.size();
        for (int i = 0; i < bookingNumbers.length; i++) {
            Booking booking = new Booking();
            booking.setBedroom(bedrooms.get(i % length));
            booking.setBookingNumber(bookingNumbers[i]);
            booking.setBookNumber(bookNumbers[i]);
            booking.setCheckInDate(LocalDate.parse(checkInDates[i], format));
            booking.setCheckOutDate(LocalDate.parse(checkOutDates[i], format));
            booking.setNote(notes[i]);
            booking.setPrice(BigDecimal.valueOf(prices[i]));
            booking.setState(states[i]);

            bookings.add(booking);
        }

        return bookings;
    }

    private List<ClientStay> getClientStays(List<Booking> bookings, List<Client> clients) {
        boolean[] holders = new boolean[]{true, false, true, true, false, false, false, false};
        List<ClientStay> stays = new ArrayList<>(holders.length);

        for (int i = 0; i < holders.length; i++) {
            ClientStay stay = new ClientStay();
            stay.setBooking(bookings.get(i % bookings.size()));
            stay.setClient(clients.get(i % clients.size()));
            stay.setHolder(holders[i]);

            stays.add(stay);
        }
        return stays;
    }

    private List<Consumption> getConsumptions(List<Booking> bookings, List<InternalService> services) {
        int[] amounts = new int[]{12, 34, 4, 5, 455, 6, 43};
        double[] prices = new double[]{23.3, 23.5, 65.3, 4.5, 23, 23, 33};
        String[] dates = new String[]{"2015/04/21", "2015/04/21", "2015/4/11", "2015/04/21", "2015/04/13", "2015/04/01", "2016/04/22"};

        List<Consumption> consumptions = new ArrayList<>();

        for (int i = 0; i < amounts.length; i++) {
            Consumption consumption = new Consumption();
            consumption.setBooking(bookings.get(i % bookings.size()));
            consumption.setInternalService(services.get(i % services.size()));
            consumption.setAmount(amounts[i]);
            consumption.setPrice(BigDecimal.valueOf(prices[i]));
            consumption.setDate(LocalDate.parse(dates[i], format));

            consumptions.add(consumption);
        }

        return consumptions;
    }

    private List<ExternalService> getExternalServices() {
        String[] names = new String[]{"Paseos a caballo", "Caminatas", "Buceo", "ir rincon salsa", "Taxis", "Fiesta en las cueva", "Comer jevita"};
        String[] drawables = new String[]{ "ic_florist", "ic_gym", "ic_scuba_diving", "", "ic_taxi_stand", "ic_sync_black_24dp", "ic_beauty_salon" };

        List<ExternalService> services = new ArrayList<>(names.length);

        for (int i = 0; i < names.length; i++) {
            ExternalService service = new ExternalService();
            service.setName(names[i]);
            service.setIcon(drawables[i]);
            services.add(service);
        }

        return services;
    }

    private List<SupplierService> getSupplierServices(List<Supplier> suppliers, List<ExternalService> services) {
        String[] descriptions = new String[]{
                "Hey Jude", "Don't let me down", "Take a sad song", "and make it",
                "better", "remember to let her into you heart", ""
        };

        List<SupplierService> supplierServices = new ArrayList<>();

        for (int i = 0; i < descriptions.length; i++) {
            SupplierService supplierService = new SupplierService();
            supplierService.setSupplier(suppliers.get(i % suppliers.size()));
            supplierService.setService(services.get(i % services.size()));
            supplierService.setDescription(descriptions[i]);

            supplierServices.add(supplierService);
        }

        return supplierServices;
    }
}
