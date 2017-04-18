package com.polymitasoft.caracola;

import com.polymitasoft.caracola.dataaccess.DataStoreHolder;
import com.polymitasoft.caracola.dataaccess.SupplierDao;
import com.polymitasoft.caracola.datamodel.ExternalService;
import com.polymitasoft.caracola.datamodel.Supplier;
import com.polymitasoft.caracola.report.pdf.PdfReport;

import org.junit.Test;

import java.io.File;
import java.util.List;

import io.requery.Persistable;
import io.requery.sql.EntityDataStore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author rainermf
 * @since 1/3/2017
 */

public class ReportPdfTest {

    @Test
    public void testServices() {
        File file = new File(DataStoreHolder.INSTANCE.getDbFile().getParentFile(), "report.pdf");
        try {
            new PdfReport().manipulatePdf(file.getAbsolutePath());
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
