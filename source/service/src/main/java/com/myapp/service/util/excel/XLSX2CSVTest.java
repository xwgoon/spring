package com.myapp.service.util.excel;

import com.myapp.data.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.extractor.XSSFEventBasedExcelExtractor;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFComment;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A rudimentary XLSX -> CSV processor modeled on the
 * POI sample program XLS2CSVmra from the package
 * org.apache.poi.hssf.eventusermodel.examples.
 * As with the HSSF version, this tries to spot missing
 * rows and cells, and output empty entries for them.
 * <p>
 * Data sheets are read using a SAX parser to keep the
 * memory footprint relatively small, so this should be
 * able to read enormous workbooks.  The styles table and
 * the shared-string table must be kept in memory.  The
 * standard POI styles table class is used, but a custom
 * (read-only) class is used for the shared string table
 * because the standard POI SharedStringsTable grows very
 * quickly with the number of unique strings.
 * <p>
 * For a more advanced implementation of SAX event parsing
 * of XLSX files, see {@link XSSFEventBasedExcelExtractor}
 * and {@link XSSFSheetXMLHandler}. Note that for many cases,
 * it may be possible to simply use those with a custom
 * {@link SheetContentsHandler} and no SAX code needed of
 * your own!
 */
@SuppressWarnings({"java:S106", "java:S4823", "java:S1192"})
public class XLSX2CSVTest {
    /**
     * Uses the XSSF Event SAX helpers to do most of the work
     * of parsing the Sheet XML, and outputs the contents
     * as a (basic) CSV.
     */
    private static class SheetToCSV implements SheetContentsHandler {
        private int currentRow = -1;
        private int currentCol = -1;

        private User user;
        private List<User> list = new ArrayList<>();

        @Override
        public void startRow(int rowNum) {
            currentRow = rowNum;
            currentCol = -1;
            user = new User();
        }

        @Override
        public void endRow(int rowNum) {
            if (StringUtils.isBlank(user.getName())) {
                throw new RuntimeException("第" + (rowNum + 1) + "行的姓名不能为空");
            }

            list.add(user);

            if (list.size() == 2) {
                System.out.println(list);
                list = new ArrayList<>();
            }
        }

        @Override
        public void endSheet() {
            if (list.size() > 0) {
                System.out.println(list);
            }
        }

        @Override
        public void cell(String cellReference, String formattedValue, XSSFComment comment) {

            // gracefully handle missing CellRef here in a similar way as XSSFCell does
            if (cellReference == null) {
                cellReference = new CellAddress(currentRow, currentCol).formatAsString();
            }

            currentCol = new CellReference(cellReference).getCol();

            switch (currentCol) {
                case 1:
                    user.setName(formattedValue);
                    break;
                case 2:
                    user.setSex(StringUtils.isBlank(formattedValue) ? null : "男".equals(formattedValue) ? 1 : 2);
                    break;
                case 3:
                    user.setAge(StringUtils.isBlank(formattedValue) ? null : Integer.valueOf(formattedValue));
                    break;
            }
        }
    }


    ///////////////////////////////////////

    private final ReadOnlySharedStringsTable strings;
    private final XSSFReader xssfReader;
    private final StylesTable styles;

    public XLSX2CSVTest(OPCPackage pkg) throws IOException, SAXException, OpenXML4JException {
        this.strings = new ReadOnlySharedStringsTable(pkg);
        this.xssfReader = new XSSFReader(pkg);
        this.styles = xssfReader.getStylesTable();
    }

    /**
     * Parses and shows the content of one sheet
     * using the specified styles and shared-strings tables.
     *
     * @param styles           The table of styles that may be referenced by cells in the sheet
     * @param strings          The table of strings that may be referenced by cells in the sheet
     * @param sheetInputStream The stream to read the sheet-data from.
     * @throws IOException  An IO exception from the parser,
     *                      possibly from a byte stream or character stream
     *                      supplied by the application.
     * @throws SAXException if parsing the XML data fails.
     */
    public void processSheet(
            SheetContentsHandler sheetHandler,
            InputStream sheetInputStream) throws IOException, SAXException {
        DataFormatter formatter = new DataFormatter();
        InputSource sheetSource = new InputSource(sheetInputStream);
        try {
            XMLReader sheetParser = XMLHelper.newXMLReader();
            ContentHandler handler = new XSSFSheetXMLHandler(styles, null, strings, sheetHandler, formatter, false);
            sheetParser.setContentHandler(handler);
            sheetParser.parse(sheetSource);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("SAX parser appears to be broken - " + e.getMessage());
        }
    }

    /**
     * Initiates the processing of the XLS workbook file to CSV.
     *
     * @throws IOException  If reading the data from the package fails.
     * @throws SAXException if parsing the XML data fails.
     */
    public void processSheet(int sheetIndex, SheetContentsHandler sheetHandler) throws IOException, OpenXML4JException, SAXException {
        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        int index = 0;
        while (iter.hasNext()) {
            try (InputStream stream = iter.next()) {
                if (index == sheetIndex) {
                    System.out.println(iter.getSheetName() + " [index=" + index + "]:");
                    processSheet(sheetHandler, stream);
                }
            }
            ++index;
        }
    }

    public static void main(String[] args) throws Exception {
        File xlsxFile = new File("E:\\Excel测试\\test1.xlsx");

        // The package open is instantaneous, as it should be.
        try (OPCPackage p = OPCPackage.open(xlsxFile.getPath(), PackageAccess.READ)) {
            XLSX2CSVTest xlsx2csv = new XLSX2CSVTest(p);
            xlsx2csv.processSheet(0, new SheetToCSV());
            xlsx2csv.processSheet(1, new SheetToCSV());
        }

    }

}
